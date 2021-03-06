package com.qualmercado.importer;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import com.qualmercado.importer.external.model.Exportacao;
import com.qualmercado.importer.external.model.Registros;
import com.qualmercado.importer.internal.model.Product;
import com.qualmercado.importer.util.ProductUtil;
import com.qualmercado.importer.util.StringUtil;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.json.JettisonMappedXmlDriver;

/**
 * http://stackoverflow.com/questions/21283052/com-thoughtworks-xstream-mapper-cannotresolveclassexception
 * 
 * http://www.leveluplunch.com/java/examples/convert-xml-to-from-json-using-xstream/
 *
 */
public class DataImporter2 {

	public static void main(String[] args) {
		
		DataImporter2 xmlToJson = new DataImporter2();
		xmlToJson.parse();
		
	}
	
	public void parse() {
		try {
			InputStream xmlFile = DataImporter2.class.getResourceAsStream("/pao-expresso/estoque.xml");
			
			XStream xstream = new XStream();
			xstream.alias("Exportacao", Exportacao.class);
			xstream.autodetectAnnotations(true);
			xstream.ignoreUnknownElements();
			
			Exportacao exportacao = new Exportacao();
			xstream.fromXML(xmlFile, exportacao);
			
			Product product = null;
			List<Product> products = new ArrayList<Product>();
			
			System.out.println("Total de produtos: " + exportacao.getRegistros().size());
			System.out.println("======================");
			
			for (Registros reg : exportacao.getRegistros()) {
				product = extractData(reg);
				
				if (product != null) {
                    products.add(product);
				}
			}
			
			System.out.println("Total products before elimination of inconsistencies: " + products.size());
			
			eliminateInconsistencies(products);
			
			System.out.println("Total products after elimination of inconsistencies: " + products.size());
			
			ProductUtil.printData(products);
			
			// create a new xstream object w/json provider
			XStream xstreamForJson = new XStream(new JettisonMappedXmlDriver());
			xstreamForJson.setMode(XStream.NO_REFERENCES);
			System.out.println(xstreamForJson.toXML(products));
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	private Product extractData(Registros reg) {
		String size = null;
		String unity = null;
		Product product = null;
		Integer validSize = null;
		String trechos[] = reg.getNome().trim().split(" ");
		
		if (trechos.length > 1) {
			size = trechos[trechos.length - 1];
			
			product = new Product();
			product.setDesc(StringUtil.normalizeText(reg.getNome()));
			product.setBarcode(reg.getBarras().trim());
			product.setImported(true);
			product.setRecommended(false);
			
			if (StringUtil.containsIntegerNumber(size)) {
				validSize = StringUtil.extractNumber(size);
				unity = size.replace(validSize.toString(), "").trim();

				product.setSize(validSize);
				product.setUnity(unity.toLowerCase());
			} else if (getAcceptUnities().contains(size)) {
				product.setSize(1);
				product.setUnity(size.toLowerCase().trim());
			} else {
				product.setSize(1);
				product.setUnity(reg.getUnidade().toLowerCase().trim());
			}
		}
		
		return product;
	}
	
	private List<String> getAcceptUnities() {
		List<String> unities = new ArrayList<String>();
		
		unities.add("kg");
		unities.add("g");
		unities.add("un");
		unities.add("ml");
		unities.add("l");
		unities.add("lta");
		
		return unities;
	}
	
	private void eliminateInconsistencies(List<Product> products) {
		for (Product product : new ArrayList<Product>(products)) {
			if (product.getImported() == false || product.getBarcode() == null || product.getBarcode().length() < 3 || !getAcceptUnities().contains(product.getUnity())) {
				products.remove(product);
			}
			
			product.setBarcode("zpto" + product.getBarcode());
		}
	}

}

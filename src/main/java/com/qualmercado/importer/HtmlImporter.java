package com.qualmercado.importer;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.qualmercado.importer.internal.model.Product;
import com.qualmercado.importer.util.StringUtil;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.json.JettisonMappedXmlDriver;

public class HtmlImporter {

	public static void main(String[] args) throws IOException {
		
		HtmlImporter htmlImporter = new HtmlImporter();
		htmlImporter.parse();

	}
	
	public void parse() throws IOException {
		InputStream htmlFile = HtmlImporter.class.getResourceAsStream("/rancho-karina/produtos-rancho-karina.html");

		Document doc = Jsoup.parse(htmlFile, "Cp1252", "http://example.com/");

		Product product = null;
		List<Product> products = new ArrayList<Product>();
		
		for (Element row : doc.select("tr")) {
			Elements tds = row.select("td");
			
			if (tds.size() == 3) {
				product = extractData(tds);
				
				if (product != null) {
	                products.add(product);
				}
				
//				System.out.println("(" + i++ + ")" + tds.get(0).text() + ":" + tds.get(1).text() + "PRECO:" + tds.get(2).text());
			}
		}
		
		System.out.println("Total products before elimination of inconsistencies: " + products.size());
		
		eliminateInconsistencies(products);
		
		System.out.println("Total products after elimination of inconsistencies: " + products.size());
		
		System.out.println("=======================================================================================");
		
		printData(products);
		
		eliminateInconsistencies(products);
		
		XStream xstreamForJson = new XStream(new JettisonMappedXmlDriver());
		xstreamForJson.setMode(XStream.NO_REFERENCES);
		System.out.println(xstreamForJson.toXML(products));
	}
	
	private Product extractData(Elements tds) {
		Product product = new Product();
		product.setBarcode(tds.get(0).text());
		String description = tds.get(1).text();
		String[] descFragments = description.split(" ");
		
		if (descFragments.length > 0) {
			String lastFragment = descFragments[descFragments.length - 1].toLowerCase();
			if (getAcceptUnities().contains(lastFragment)) {
				if (StringUtil.isNumeric(descFragments[descFragments.length - 2])) {
					//product.setSize(Integer.parseInt(descFragments[descFragments.length - 2]));
					defineProductSize(product, descFragments[descFragments.length - 2]);
					product.setUnity(descFragments[descFragments.length - 1]);
				}
				
				description = "";
				
				for (int i = 0; i < descFragments.length - 2; i++) {
					description += descFragments[i] + " ";
				}
			} else {
				for (String unity : getAcceptUnities()) {
					if (lastFragment.endsWith(unity)) {
						String size = lastFragment.replace(unity, "");
						
						if (StringUtil.isNumeric(size)) {
							defineProductSize(product, size);
						
							if (unity.equals("k")) {
								unity = "kg";
							}
							
							product.setUnity(unity);
							
							description = "";
							
							for (int i = 0; i < descFragments.length - 1; i++) {
								description += descFragments[i] + " ";
							}
						}
					}
				}
			}
			
		}
		
		product.setDesc(StringUtil.normalizeText(description));
		product.setImported(true);
		
		return product;
	}

	private void defineProductSize(Product product, String size) {
		if (size.contains(",")) {
			size = size.replace(",", ".");
			product.setSize(Double.parseDouble(size));
		} else {
			product.setSize(Integer.parseInt(size));
		}
	}
	
	private List<String> getAcceptUnities() {
		List<String> unities = new ArrayList<String>();
		
		unities.add("kg");
		unities.add("g");
		unities.add("un");
		unities.add("ml");
		unities.add("l");
		unities.add("lta");
		
		// exclusive for rancho karina
		unities.add("k");
		
		return unities;
	}
	
	private void printData(List<Product> products) {
		for (Product prod : products) {
		    System.out.println(prod);
		    System.out.println("=======================================================================================");
		}
	}
	
	private void eliminateInconsistencies(List<Product> products) {
		for (Product product : new ArrayList<Product>(products)) {
			if (product.getImported() == false || product.getBarcode() == null || product.getBarcode().length() < 3 || !getAcceptUnities().contains(product.getUnity()) || product.getDesc().startsWith("Zz-")) {
				products.remove(product);
			}
			
			product.setBarcode("zpto" + product.getBarcode());
		}
	}

}

package com.qualmercado.importer;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import com.qualmercado.importer.external.model.BarcodeNPrice;
import com.qualmercado.importer.external.model.Exportacao;
import com.qualmercado.importer.external.model.Registros;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.json.JettisonMappedXmlDriver;

public class BarcodeNPricePadariaGenerator {

	private int indice;
	
	public static void main(String[] args) {
		
		BarcodeNPricePadariaGenerator xmlToJson = new BarcodeNPricePadariaGenerator();
		xmlToJson.parse();
		
	}
	
	public void parse() {
		try {
			InputStream xmlFile = BarcodeNPricePadariaGenerator.class.getResourceAsStream("/estoque.xml");
			
			XStream xstream = new XStream();
			xstream.alias("Exportacao", Exportacao.class);
			xstream.autodetectAnnotations(true);
			xstream.ignoreUnknownElements();
			
			Exportacao exportacao = new Exportacao();
			xstream.fromXML(xmlFile, exportacao);
			
			BarcodeNPrice product = null;
			List<BarcodeNPrice> products = new ArrayList<BarcodeNPrice>();
			
			System.out.println("Total de produtos: " + exportacao.getRegistros().size());
			System.out.println("======================");
			
			for (Registros reg : exportacao.getRegistros()) {
				product = new BarcodeNPrice();
				product.setBarcode(reg.getBarras().trim());
				product.setPrice(formatPrice(reg.getPrecoConsumidor()));
				
				products.add(product);
			}
			
			eliminateInconsistencies(products);
			
			printData(products);
			
			// create a new xstream object w/json provider
			XStream xstreamForJson = new XStream(new JettisonMappedXmlDriver());
			xstreamForJson.setMode(XStream.NO_REFERENCES);
			System.out.println(xstreamForJson.toXML(products));
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	private Double formatPrice(String price) {
		return Double.parseDouble(price.replace("R$ ", "").replace(",", "."));
	}
	
	private void eliminateInconsistencies(List<BarcodeNPrice> products) {
		for (BarcodeNPrice product : new ArrayList<BarcodeNPrice>(products)) {
			if (product.getBarcode() == null || product.getBarcode().length() < 3 || product.getPrice() == null) {
				products.remove(product);
			}
			
			product.setBarcode("zpto" + product.getBarcode());
		}
	}

	private void printData(List<BarcodeNPrice> products) {
		for (BarcodeNPrice prod : products) {
			System.out.println("#" + ++indice);
		    System.out.println(prod);
		    System.out.println("=======================================================================================");
		}
	}
	
}

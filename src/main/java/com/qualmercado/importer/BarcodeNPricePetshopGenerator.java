package com.qualmercado.importer;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.qualmercado.importer.external.model.BarcodeNPrice;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.json.JettisonMappedXmlDriver;

public class BarcodeNPricePetshopGenerator {

	private int indice;
	
	public static void main(String[] args) {
		
		BarcodeNPricePetshopGenerator xmlToJson = new BarcodeNPricePetshopGenerator();
		xmlToJson.parse();
		
	}
	
	public void parse() {
		try {
			InputStream htmlFile = HtmlImporter.class.getResourceAsStream("/rancho-karina/produtos-rancho-karina.html");

			Document doc = Jsoup.parse(htmlFile, "Cp1252", "http://example.com/");

			BarcodeNPrice product = null;
			List<BarcodeNPrice> products = new ArrayList<BarcodeNPrice>();
			
			for (Element row : doc.select("tr")) {
				Elements tds = row.select("td");
				
				if (tds.size() == 3) {
					product = new BarcodeNPrice();
					product.setBarcode(tds.get(0).text());
					product.setPrice(formatPrice(tds.get(2).text()));
					
	                products.add(product);
				}
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

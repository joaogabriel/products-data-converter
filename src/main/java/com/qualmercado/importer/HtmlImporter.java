package com.qualmercado.importer;

import java.io.IOException;
import java.io.InputStream;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.qualmercado.importer.internal.model.Product;
import com.qualmercado.importer.util.StringUtil;

public class HtmlImporter {

	public static void main(String[] args) throws IOException {
		InputStream htmlFile = HtmlImporter.class.getResourceAsStream("/rancho-karina/produtos-rancho-karina.html");

		Document doc = Jsoup.parse(htmlFile, "Cp1252", "http://example.com/");

		int i = 1;
		
		for (Element row : doc.select("tr")) {
			Elements tds = row.select("td");
			
			
			
			if (tds.size() == 3) {
				System.out.println("(" + i++ + ")" + tds.get(0).text() + ":" + tds.get(1).text());
			}
		}

	}
	
	private Product extractData(Elements tds) {
		Product product = new Product();
		product.setBarcode(tds.get(0).text());
		product.setDesc(StringUtil.normalizeText(tds.get(1).text()));
		product.setImported(true);
		return product;
	}

}

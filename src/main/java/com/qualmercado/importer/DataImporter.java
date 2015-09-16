package com.qualmercado.importer;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import com.qualmercado.importer.external.model.Exportacao;
import com.qualmercado.importer.external.model.Registros;
import com.qualmercado.importer.internal.model.ProductMarket;
import com.qualmercado.importer.internal.model.ProductMarketResults;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.json.JettisonMappedXmlDriver;

/**
 * http://stackoverflow.com/questions/21283052/com-thoughtworks-xstream-mapper-cannotresolveclassexception
 * 
 * http://www.leveluplunch.com/java/examples/convert-xml-to-from-json-using-xstream/
 *
 */
public class DataImporter {

	private int indice;
	
	public static void main(String[] args) {
		
		DataImporter xmlToJson = new DataImporter();
		xmlToJson.parse();
		
	}
	
	public void parse() {
		try {
			InputStream xmlFile = DataImporter.class.getResourceAsStream("/estoque.xml");
			
			XStream xstream = new XStream();
			xstream.alias("Exportacao", Exportacao.class);
			xstream.autodetectAnnotations(true);
			xstream.ignoreUnknownElements();
			
			Exportacao exportacao = new Exportacao();
			xstream.fromXML(xmlFile, exportacao);
			
			ProductMarketResults productMarketResults = null;
			ProductMarket productMarket = new ProductMarket();
			List<ProductMarketResults> results = new ArrayList<ProductMarketResults>();

			System.out.println("Total de produtos: " + exportacao.getRegistros().size());
			System.out.println("======================");
			
			for (Registros reg : exportacao.getRegistros()) {
				extractData(reg);
				
				productMarketResults = new ProductMarketResults();
				productMarketResults.setMarket("xpto");
				productMarketResults.setProduct("abc");
				productMarketResults.setPrice(reg.getPreco_consumidor());
				
				results.add(productMarketResults);
			}
			
			productMarket.setResults(results);
			
			// create a new xstream object w/json provider
			XStream xstreamForJson = new XStream(new JettisonMappedXmlDriver());
			xstreamForJson.setMode(XStream.NO_REFERENCES);
			xstream.alias("status", Exportacao.class);
			System.out.println(xstreamForJson.toXML(productMarket));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void extractData(Registros reg) {
		String trechos[] = reg.getNome().trim().split(" ");
		String marca = null;
		String produto = null;
		String quantidade = null;
		
		if (trechos.length == 2) {
			marca = trechos[0];
			produto = trechos[1];
			quantidade = "1 " + reg.getUnidade();
		}
		
		if (trechos.length == 3) {
			marca = trechos[0];
			produto = trechos[1];
			quantidade = trechos[2];
		}
		
		// CACHACA ARTESANAL PINHEIRINHA 700ML
		if (trechos.length == 4) {
			produto = trechos[0] + " " + trechos[1];
			marca = trechos[2];
			quantidade = trechos[3];
		}
		
		// se KG, entao eh 1KG
		// LTA = 1 Lata
		if (quantidade == null) {
			quantidade = trechos[trechos.length - 1];
		}
		
		if (!isValidAmount(quantidade)) {
			quantidade = null;
		}
		
		if (marca != null && produto != null && quantidade != null) {
			System.out.println("#" + indice++);
			System.out.println("Cod. de Barras: " + reg.getBarras());
			System.out.println("Marca         : " + marca);
			System.out.println("Produto       : " + produto);
			System.out.println("Quantidade    : " + quantidade);
			System.out.println("======================");
		}
	}
	
	private static boolean isValidAmount(String str) {
		return str.matches(".*[0-9].*");
	}

}

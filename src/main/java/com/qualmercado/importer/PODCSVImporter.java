package com.qualmercado.importer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.map.ObjectMapper;

import com.qualmercado.importer.external.model.PODCSVProductLine;
import com.qualmercado.importer.internal.model.Product;
import com.qualmercado.importer.util.ProductUtil;
import com.qualmercado.importer.util.StringUtil;

public class PODCSVImporter {

	public static void main(String[] args) {
		PODCSVImporter csvImporter = new PODCSVImporter();
		csvImporter.parse();
	}

	public void parse() {
		InputStream csvFile = HtmlImporter.class.getResourceAsStream("/pod-gtin/pod_gtin.csv");
		BufferedReader buffReader = null;
		String line = "";
		String csvSplit = ";";
		PODCSVProductLine podProduct = null;
		List<PODCSVProductLine> podProducts = new ArrayList<PODCSVProductLine>();
		List<Product> products = new ArrayList<Product>();
		
		try {
			buffReader = new BufferedReader(new InputStreamReader(csvFile));
			
			while ((line = buffReader.readLine()) != null) {
				String[] product = line.split(csvSplit);
				
				if (product.length > 30) {
					podProduct = new PODCSVProductLine();
					podProduct.setGtinCode(verifyNull(product[0]));
					podProduct.setGtinName(verifyNull(product[1]));
					podProduct.setGtinImage(verifyNull(product[7]));
					podProduct.setPackageTypeName(verifyNull(product[30]));
					podProduct.setWeightInG(verifyNull(product[31]));
					podProduct.setWeightInOz(verifyNull(product[32]));
					podProduct.setVolumeInML(verifyNull(product[33]));
					podProduct.setVolumeInFlOz(verifyNull(product[34]));
					
					podProducts.add(podProduct);
				}
			}
			
			podProducts.remove(0);

			for (PODCSVProductLine onePodProduct : podProducts) {
				products.add(extractData(onePodProduct));
			}

			ProductUtil.printData(products);
			
			System.out.println("Total products before elimination of inconsistencies: " + products.size());
			
			List<String> removed = eliminateInconsistencies(products);
			
			System.out.println("Total products after elimination of inconsistencies: " + products.size());
			System.out.println("Products removed (" + removed.size() + "): " + removed);
			
			ObjectMapper mapper = new ObjectMapper();
			String json = mapper.writeValueAsString(products);
			
			System.out.println(json);
			
			PrintWriter printWriter = new PrintWriter("C:\\env-dev\\temp\\json-products\\Product.json");
			printWriter.print(json);
			printWriter.flush();
			printWriter.close();
		} catch (Exception e) {
			System.out.println("Prd com erro:" + podProduct.getGtinCode());
			e.printStackTrace();
		} finally {
			if (buffReader != null) {
				try {
					buffReader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		System.out.println("Done");
	}
	
	// 7898043192775
	// 7891080000256
	// 7898922011005
	private Product extractData(PODCSVProductLine podProduct) {
		String unity = null;
		String size = null;
		Integer validSize = null;
		Product product = new Product();
		String name = podProduct.getGtinName();
		String lastWord = null;
		String words[] = name.trim().split(" ");

		if (words.length > 1) {
			lastWord = words[words.length - 1];
			
			// verifica se existe uma unidade no texto
			if (getAcceptUnities().contains(lastWord) && StringUtil.isNumeric(words[words.length - 2])) {
				unity = getCorrectUnity(lastWord);
				size = words[words.length - 2];
				
				product.setUnity(unity.toLowerCase());
				
				ProductUtil.defineSize(product, size);
				
				name = name.replace(unity, "").replace(size, "").replace("KGRS", "").replace("KGR", "").trim();
				
				if (name.endsWith(" DE")) {
					name = name.substring(0, name.length() - 3);
				}
			} else if (lastWord.equalsIgnoreCase("kg") || lastWord.equalsIgnoreCase("kgr") || lastWord.equalsIgnoreCase("kgrs")) {
				validSize = 1;
				
				if (StringUtil.containsIntegerNumber(lastWord)) {
					validSize = StringUtil.extractNumber(lastWord);
				}
				
				name = name.toUpperCase().replace("KG", "").replace("KGR", "").replace("KGES", "").trim();
				
				product.setDesc(name);
				product.setSize(validSize);
				product.setUnity("kg");
			}/* else if (lastWord.endsWith("G")) {
				validSize = 1;
				unity = lastWord;
				
				if (StringUtil.containsIntegerNumber(lastWord)) {
					validSize = StringUtil.extractNumber(lastWord);
					unity = lastWord.replace(validSize + "", "");
				}
				
				name = name.replace(validSize + "", "").replace(unity, "").trim();
				
				product.setDesc(name);
				product.setSize(validSize);
				product.setUnity(unity);
			}*/
		}
		
		product.setBarcode(podProduct.getGtinCode());
		product.setDesc(StringUtil.normalizeText(name));
		product.setImage(podProduct.getGtinImage());
		product.setPackaging(podProduct.getPackageTypeName());
		product.setImported(true);
		product.setRecommended(false);
		
		return product;
	}
	
	private List<String> getAcceptUnities() {
		List<String> unities = new ArrayList<String>();
		
		unities.add("KG");
		unities.add("G");
		unities.add("UN");
		unities.add("ML");
		unities.add("L");
		unities.add("LTA");
		unities.add("GR");
		unities.add("KGR");
		unities.add("KGRS");
		
		return unities;
	}
	
	// TODO substituir por map 
	private String getCorrectUnity(String unity) {
		if ("KGR".equals(unity) || "KGRS".equals(unity)) {
			unity = "kg";
		}
		return unity;
	}
	
	private String verifyNull(String str) {
		return str == null ? "" : str;
	}
	
	private List<String> eliminateInconsistencies(List<Product> products) {
		List<String> productsRemoved = new ArrayList<String>();
		
		for (Product product : new ArrayList<Product>(products)) {
			if (product.getImported() == false || StringUtil.isEmpty(product.getBarcode()) || product.getBarcode().length() < 3
				|| StringUtil.isEmpty(product.getDesc()) || isInvalidUnity(product.getUnity())) {
		
				productsRemoved.add(product.getBarcode());
				products.remove(product);
			}
			
			product.setBarcode("zpto" + product.getBarcode());
		}
		
		return productsRemoved;
	}
	
	private boolean isInvalidUnity(String unity) {
		return unity != null && !getAcceptUnities().contains(unity.toUpperCase());
	}
	
}

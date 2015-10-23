package com.qualmercado.importer.util;

import java.util.List;

import com.qualmercado.importer.internal.model.Product;

public class ProductUtil {

	public static void defineSize(Product product, String size) {
		if (size.contains(",")) {
			size = size.replace(",", ".");
			product.setSize(Double.parseDouble(size));
		} else if (size.contains(".")) {
			product.setSize(Double.parseDouble(size));
		} else {
			product.setSize(Integer.parseInt(size));
		}
	}
	
	public static void printData(List<Product> products) {
		int count = 1;
		
		for (Product prod : products) {
			System.out.println("#" + count++);
		    System.out.println(prod);
		    System.out.println("=======================================================================================");
		}
		
	}
	
}

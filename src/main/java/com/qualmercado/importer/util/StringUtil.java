package com.qualmercado.importer.util;

public class StringUtil {

	public static String normalizeText(String str) {
		final String WHITE_SPACE = " ";
		StringBuilder text = new StringBuilder();
		
		String[] words = str.split(WHITE_SPACE);
		
		for (String word : words) {
			if (!word.trim().isEmpty()) {
				text.append(word.substring(0, 1).toUpperCase())
					.append(word.substring(1).toLowerCase())
					.append(WHITE_SPACE);
			}
		}
		
		return text.toString().trim();
	}
	
	public static boolean isNumeric(String str) {
		try {
			str = str.replace(",", ".");
			
			Double.parseDouble(str);
			
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	
}

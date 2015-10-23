package com.qualmercado.importer.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtil {

	public static String normalizeText(String str) {
		String wordTrim = null;
		final String WHITE_SPACE = " ";
		StringBuilder text = new StringBuilder();
		
		String[] words = str.split(WHITE_SPACE);
		
		for (String word : words) {
			wordTrim = word.trim();
			
			if (!wordTrim.isEmpty()) {
				if (wordTrim.length() > 2) {
					text.append(wordTrim.substring(0, 1).toUpperCase())
						.append(wordTrim.substring(1).toLowerCase())
						.append(WHITE_SPACE);
				} else {
					text.append(wordTrim.toLowerCase()).append(WHITE_SPACE);
				}
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
	
	public static boolean isEmpty(String str) {
		return str == null || str.isEmpty();
	}
	
	public static boolean containsIntegerNumber(String str) {
		return str.matches(".*[0-9].*") && !str.contains(",") && !str.contains(".");
	}
	
	public static Integer extractNumber(String str) {
		Pattern pattern = Pattern.compile("-?\\d+");
		Matcher matcher = pattern.matcher(str);
		matcher.find();
		return Integer.parseInt(matcher.group());
	}
	
}

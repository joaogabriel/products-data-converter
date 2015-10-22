package com.qualmercado.importer.external.model;

public class BarcodeNPrice {

	private String barcode;
	private Double price;

	public String getBarcode() {
		return barcode;
	}

	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}
	
	@Override
	public String toString() {
		return "Barcode: " + barcode + " | Price: " + price;
	}

}

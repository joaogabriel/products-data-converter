package com.qualmercado.importer.external.model;

public class PODCSVProductLine {

	private String gtinCode;
	private String gtinName;
	private String gtinImage;
	private String packageTypeName;

	private String weightInG;
	private String weightInOz;
	private String volumeInML;
	private String volumeInFlOz;

	public String getGtinCode() {
		return gtinCode;
	}

	public void setGtinCode(String gtinCode) {
		this.gtinCode = gtinCode;
	}

	public String getGtinName() {
		return gtinName;
	}

	public void setGtinName(String gtinName) {
		this.gtinName = gtinName;
	}

	public String getGtinImage() {
		return gtinImage;
	}

	public void setGtinImage(String gtinImage) {
		this.gtinImage = gtinImage;
	}

	public String getPackageTypeName() {
		return packageTypeName;
	}

	public void setPackageTypeName(String packageTypeName) {
		this.packageTypeName = packageTypeName;
	}

	public String getWeightInG() {
		return weightInG;
	}

	public void setWeightInG(String weightInG) {
		this.weightInG = weightInG;
	}

	public String getWeightInOz() {
		return weightInOz;
	}

	public void setWeightInOz(String weightInOz) {
		this.weightInOz = weightInOz;
	}

	public String getVolumeInML() {
		return volumeInML;
	}

	public void setVolumeInML(String volumeInML) {
		this.volumeInML = volumeInML;
	}

	public String getVolumeInFlOz() {
		return volumeInFlOz;
	}

	public void setVolumeInFlOz(String volumeInFlOz) {
		this.volumeInFlOz = volumeInFlOz;
	}

}

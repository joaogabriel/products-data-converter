package com.qualmercado.importer.internal.model;

public class Product {

	private String brand;
	private String category;
	private String image;
	private Boolean recommended;
	private Integer size;
	private String unity;
	private String barcode;
	private String desc;
	private String generecUnity;
	private String packaging;
	private Boolean imported;

	public String getBrand() {
		return brand;
	}

	public void setBrand(String brand) {
		this.brand = brand;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public Boolean getRecommended() {
		return recommended;
	}

	public void setRecommended(Boolean recommended) {
		this.recommended = recommended;
	}

	public Integer getSize() {
		return size;
	}

	public void setSize(Integer size) {
		this.size = size;
	}

	public String getUnity() {
		return unity;
	}

	public void setUnity(String unity) {
		this.unity = unity;
	}

	public String getBarcode() {
		return barcode;
	}

	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String getGenerecUnity() {
		return generecUnity;
	}

	public void setGenerecUnity(String generecUnity) {
		this.generecUnity = generecUnity;
	}

	public String getPackaging() {
		return packaging;
	}

	public void setPackaging(String packaging) {
		this.packaging = packaging;
	}

	public Boolean getImported() {
		return imported;
	}

	public void setImported(Boolean imported) {
		this.imported = imported;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((barcode == null) ? 0 : barcode.hashCode());
		result = prime * result + ((desc == null) ? 0 : desc.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Product other = (Product) obj;
		if (barcode == null) {
			if (other.barcode != null)
				return false;
		} else if (!barcode.equals(other.barcode))
			return false;
		if (desc == null) {
			if (other.desc != null)
				return false;
		} else if (!desc.equals(other.desc))
			return false;
		return true;
	}

	@Override
	public String toString() {
		StringBuilder out = new StringBuilder();
		
		out.append("Barcode: " + barcode)
		   .append(" | Desc: " + desc)
		   .append(" | Size: " + size)
		   .append(" | Unity: " + unity);
		
		return out.toString();
	}
	
}

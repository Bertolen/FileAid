package com.eulerhermes.dri.fileaid.model;

import lombok.Data;

@Data
public class DataInfo {
	private String name;
	private TypeEnum type;
	private int position;
	private int size;
	private String alphanumValue;
	private float numValue;
	
	public String toString() {
		return "nom : " + name + " | position : " + position +  " | taille : " + size;
	}
}

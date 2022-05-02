package com.eulerhermes.dri.fileaid.model;

import lombok.Data;

@Data
public class DataInfo {
	private String name;
	//private TypeEnum type; // TODO
	private int position;
	private int size;
	private String alphanumValue;
	private int numValue;
}

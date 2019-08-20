package com.prototype.type;

import com.core.lang.Property;


public class Parameter {

	private String name;
	private String value;
	private Property<byte[]> files;

	public Parameter(String name) {

		this.name = name;
	}

	public boolean hasValue() {

		return value != null;
	}

	public boolean hasFiles() {

		return files != null;
	}

	public String getName() {

		return name;
	}

	public void setName(String name) {

		this.name = name;
	}

	public String getValue() {

		return value;
	}

	public void setValue(String value) {

		this.value = value;
	}

	public Property<byte[]> getFiles() {

		return files;
	}

	public void setFiles(Property<byte[]> files) {

		this.files = files;
	}

}

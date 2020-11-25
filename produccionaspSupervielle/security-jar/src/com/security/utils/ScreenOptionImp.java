/*
 * Copyright 2011 Dividato. All rights reserved
 *
 * Copyright Version 1.0
 *
 * Create on 29/06/2011
 */
package com.security.utils;

/**
 * @author Ezequiel Beccaria
 *
 */
public class ScreenOptionImp implements ScreenOption{
	private String value;
	private String message;
	private Boolean selected;
		
	public ScreenOptionImp(String value, String message, Boolean selected) {
		super();
		this.value = value;
		this.message = message;
		this.selected = selected;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public Boolean getSelected() {
		return selected;
	}
	public void setSelected(Boolean selected) {
		this.selected = selected;
	}		
}

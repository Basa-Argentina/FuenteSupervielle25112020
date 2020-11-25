/*
 * Copyright 2011 Dividato. All rights reserved
 *
 * Copyright Version 1.0
 *
 * Create on 09/06/2011
 */
package com.security.utils;

/**
 * @author Ezequiel Beccaria
 *
 */
public class CampoDisplayTag {
	private String property;
	private String titleKey;	
	private Boolean hidden;
	
	public CampoDisplayTag(String property, String titleKey, Boolean hidden) {
		super();
		this.property = property;
		this.titleKey = titleKey;
		this.hidden = hidden;
	}
	public String getProperty() {
		return property;
	}
	public void setProperty(String property) {
		this.property = property;
	}
	public String getTitleKey() {
		return titleKey;
	}
	public void setTitleKey(String titleKey) {
		this.titleKey = titleKey;
	}
	public Boolean getHidden() {
		return hidden;
	}
	public void setHidden(Boolean hidden) {
		this.hidden = hidden;
	}
	
	
}

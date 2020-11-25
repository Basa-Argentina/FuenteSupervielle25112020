/*
 * Copyright 2011 Dividato. All rights reserved
 *
 * Copyright Version 1.0
 *
 * Create on 31/05/2011
 */
package com.security.utils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Ezequiel Beccaria
 *
 */
public class ScreenMessageImp implements ScreenMessage{
	private String messageCode;
	private List<String> messageArguments;
	
	public ScreenMessageImp(String messageCode, List<String> messageArguments) {
		super();
		this.messageCode = messageCode;
		this.messageArguments = messageArguments;
	}
	
	@Override
	public String getMessageCode() {
		return messageCode;
	}
	public void setMessageCode(String messageCode) {
		this.messageCode = messageCode;
	}
	
	@Override
	public List<String> getMessageArguments() {
		return messageArguments;
	}
	public void setMessageArguments(List<String> messageArguments) {
		this.messageArguments = messageArguments;
	}

	public void addMessageArgument(String s){
		if(messageArguments == null)
			messageArguments = new ArrayList<String>();
		messageArguments.add(s);
	}

	

}

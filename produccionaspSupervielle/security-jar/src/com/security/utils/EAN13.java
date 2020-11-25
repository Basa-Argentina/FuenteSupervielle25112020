package com.security.utils;

public class EAN13 {

	/*
	 * Calcula un checkSum segun el tipo de EAN13 requerido.	
	 */

	//Cálculo del dígito de control EAN
	public static Long EAN13_CHECKSUM(String message) {
	    Long checksum = 0L;	     
	    message = new StringBuffer(message).reverse().toString();
	    String[] splitMessage = message.split("");
	    for(int pos=1;pos<splitMessage.length; pos++){
	    	int par = ((pos+1) % 2);
	        checksum += Long.parseLong(splitMessage[pos]) * (3 - 2*par);
	    }
	    return ((10 - (checksum % 10 )) % 10);
	}
	
//	public static void main(String args[]){
//		String codigo = "123123123123";
//		codigo += EAN13_CHECKSUM(codigo).toString();
//		System.out.println(codigo);
//	}

}
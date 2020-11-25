package com.security.utils;

public class CheckDigit {
	public static final int A = 103;
	public static final int B = 104;

	/*
	 * Calcula un checkSum segun el tipo de de code128 requerido.
	 * Lo realiza unicamente con START y DIVISOR del mismo tipo. 
	 * Por defecto calcula con Code128A
	 */
	public static char calcCheckdigitType(String msg, String type) {
		String msgOriginal = msg.toString();
		//code128A no soporta minusculas.
		int code = A;
		int corteControl = 65;
		int sumASCCI = 32;
		//int sumASCCISpecial = 50;		
		msg.toUpperCase();
		if("B".equals(type)){
			code = B;
			corteControl = 95;			
			msg = msgOriginal.toString();
		}
		int evenSum = code;
		for (int i = 0; i < msg.length(); i++) {
			evenSum += (i + 1) * ((int) msg.charAt(i) - 32);
		}
		int check = evenSum % code;
		//Se comprueba los caracteres especiales de ASCCI
		if (check < corteControl){
			check += sumASCCI;
		}/* Si queremos que soporte ASCCI especiales, descomentar esta linea
			y comentar el siguiente else.
		else if("B".equals(type)){			
			check += sumASCCISpecial;
		}*/else{
			check = 0;
		}
		return (char) check;
	}
}
package com.security.utils;

public class ParseNumberUtils {
	/**
	 * recibe un String que contiene solo numeros, borra todos los 0 de la izquierda y lo parsea a Long.
	 * @param codigo
	 * @return
	 */
	public static Long parseLongCodigo(String codigo){
		Long result= null;
		//si el codigo es distinto de vacio o null
		if(codigo!=null && codigo.length()>0){
			//cuenta el primer digito diferente de 0
			int cont = 0;
			
			while(codigo.substring( cont, cont).equals("0")){
				cont++;
			}
			//si el codigo esta formado solo por 0
			if(codigo.length()==0 || (cont>0 && cont == codigo.length()-1)){
				result = new Long(0);
			}else{
				//devuelve el Integer formado por el substring desde el cont hasta el final del codigo
				try{
					result = Long.parseLong(codigo.substring(cont));
				}catch (NumberFormatException e){
					result = 0L;
				}
			}
		}else{
			result = new Long(0);
		}
		return result;
	}
	
	/**
	 * recibe un String que contiene solo numeros, borra todos los 0 de la izquierda y lo parsea a Integer.
	 * @param codigo
	 * @return
	 */
	public static Integer parseIntegerCodigo(String codigo){
		Integer result= null;
		//si el codigo es distinto de vacio o null
		if(codigo!=null && codigo.length()>0){
			//cuenta el primer digito diferente de 0
			int cont = 0;
			while(codigo.substring( cont, cont).equals("0")){
				cont++;
			}
			//si el codigo esta formado solo por 0
			if(codigo.length()==0 || (cont>0 && cont == codigo.length()-1)){
				result = new Integer(0);
			}else{
				//devuelve el Integer formado por el substring desde el cont hasta el final del codigo
				try{
					result = Integer.parseInt(codigo.substring(cont));
				}catch (NumberFormatException e){
					result = 0;
				}
			}
		}else{
			result = new Integer(0);
		}
		return result;
	}
	
	public static String parseStringCodigo (Long codigo, int length){
		StringBuilder result = new StringBuilder();
		String cod = String.valueOf(codigo);
		for (int i = 0 ; i<length - cod.length(); i++){
			result.append("0");
		}
		result.append(cod);
		return result.toString();
	}
}

package com.security.recursos;

public class Test {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String username = "gabriel.maineros";
		if(!ValidacionUsername.validar(username))
			System.out.println("Mal");
		else
			System.out.println("Bien");
	}

}

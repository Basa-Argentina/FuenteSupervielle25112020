package com.security.recursos;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.RandomStringUtils;

/**
 * 
 * @author Gabriel Mainero
 * @modify Federico Muñoz - Agrego generación automática de contraseña y validación de fortaleza.
 *
 */
public class RecursosPassword {
	
	private static String regex="((?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).{8,12})";
	private static String semilla = "01234567";
	private static String algoritmo = "DES/ECB/PKCS5Padding";
	/**
	 * Encripta un string.
	 * No sirve para encriptar contraseñas de usuarios ya que no es el mismo algoritmo.
	 * 
	 * @param input
	 * @return el string encriptado
	 * 
	 * @throws InvalidKeyException
	 * @throws BadPaddingException
	 * @throws IllegalBlockSizeException
	 * @throws NoSuchAlgorithmException
	 * @throws NoSuchPaddingException
	 */
	public static String encrypt(String input) throws InvalidKeyException,
			BadPaddingException, IllegalBlockSizeException,
			NoSuchAlgorithmException, NoSuchPaddingException {
		String claveEncriptada = "";
		SecretKeySpec desKey = new SecretKeySpec(semilla.getBytes(), "DES");
		Cipher cipher = Cipher.getInstance(algoritmo);
		cipher.init(Cipher.ENCRYPT_MODE, desKey);
		byte[] claveEncriptadaBytes = cipher.doFinal(input.getBytes());
		org.apache.commons.codec.binary.Base64 encriptar = new Base64();
		claveEncriptada = new String(encriptar.encode(claveEncriptadaBytes));
		return claveEncriptada;
	}
	/**
	 * desencripta un string encriptado con esta misma clase.
	 * 
	 * @param claveEncriptada
	 * @return el string desencriptado
	 * 
	 * @throws InvalidKeyException
	 * @throws BadPaddingException
	 * @throws IllegalBlockSizeException
	 * @throws NoSuchAlgorithmException
	 * @throws NoSuchPaddingException
	 * @throws IOException
	 */
	public static String decrypt(String claveEncriptada)
			throws InvalidKeyException, BadPaddingException,
			IllegalBlockSizeException, NoSuchAlgorithmException,
			NoSuchPaddingException, IOException {
		String cadenaDesencriptada;
		SecretKeySpec desKey = new SecretKeySpec(semilla.getBytes(), "DES");
		org.apache.commons.codec.binary.Base64 encriptar = new Base64();
		byte[] claveDesc = encriptar.decode(claveEncriptada.getBytes());
		Cipher cipher = Cipher.getInstance(algoritmo);
		cipher.init(Cipher.DECRYPT_MODE, desKey);
		cadenaDesencriptada = new String(cipher.doFinal(claveDesc));
		return cadenaDesencriptada;
	}

	/**
	 * este método solo se utiliza para encriptar passwords para los 
	 * usuarios que se crean directamente en la base.
	 * 
	 * @param passEncriptar
	 * @return el pass encriptado
	 */
	public static String encriptar(String passEncriptar) {
		org.springframework.security.providers.encoding.ShaPasswordEncoder passEnc = new org.springframework.security.providers.encoding.ShaPasswordEncoder();
		return passEnc.encodePassword(passEncriptar, null);
	}
	/**
	 * validación de fuerza de una contraseña
	 * @param passValidar
	 * @return
	 */
	public static boolean validar(String passValidar){
		return passValidar.matches(regex);
	}
	/**
	 * Genera y retorna un password alfanumérico.
	 * Este String debe informarse al usuario así como está pero 
	 * debe ser encriptado con el método 'encriptar' antes de guardarlo en la base de datos.
	 * 
	 * @return un password alfanumérico de largo 10 NO ENCRIPTADO.
	 */
	public static String generarPassword(){
		return RandomStringUtils.randomAlphanumeric(10);
	}
}

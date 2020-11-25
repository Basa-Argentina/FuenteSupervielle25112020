/*
 * Copyright 2011 Dividato. All rights reserved
 *
 * Copyright Version 1.0
 *
 * Create on 09/06/2011
 */
package com.security.modelo.configuraciongeneral;

import static org.junit.Assert.*;

import java.math.BigDecimal;

import org.junit.Test;

/**
 * @author Ezequiel Beccaria
 *
 */
public class ImpuestoIvaTest {

	/**
	 * Test method for {@link com.security.modelo.configuraciongeneral.ImpuestoIva#calcular(java.math.BigDecimal)}.
	 */
	@Test
	public void testCalcular() {
		ImpuestoIva imp = new ImpuestoIva();
		imp.setAlicuota(new BigDecimal(21));
		//test 1
		double expected = new BigDecimal(121).doubleValue();
		double result = imp.calcular(new BigDecimal(100)).doubleValue();
		double delta = 0.0001;
		assertEquals(expected, result, delta);
		System.out.print("Test Nº1 -> Esperado: "+expected+" / Resultado: "+result+" / Delta: "+delta+"\n");
		
		//test 2
		expected = new BigDecimal(1210).doubleValue();
		result = imp.calcular(new BigDecimal(1000)).doubleValue();
		delta = 0.0001;
		assertEquals(expected, result, delta);
		System.out.print("Test Nº2 -> Esperado: "+expected+" / Resultado: "+result+" / Delta: "+delta+"\n");
	}

}

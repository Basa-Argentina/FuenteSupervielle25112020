package com.security.utils;

import static org.junit.Assert.assertEquals;

import org.junit.Test;


public class CuitUtilsTest {
	@Test
	public void testValidar() {
		assertEquals("Result", true, CuitUtils.validar("23-32336200-9"));
		assertEquals("Result", true, CuitUtils.validar("20-30087403-8"));
		assertEquals("Result", true, CuitUtils.validar("23-07733302-9"));
		assertEquals("Result", false, CuitUtils.validar("20-32336200-9"));
		assertEquals("Result", false, CuitUtils.validar("20-30087403-5"));
		assertEquals("Result", false, CuitUtils.validar("20-300g7403-8"));
	}
}

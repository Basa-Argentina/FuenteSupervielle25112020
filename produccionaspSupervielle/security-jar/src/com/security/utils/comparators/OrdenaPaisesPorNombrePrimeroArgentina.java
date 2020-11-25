/*
 * Copyright 2011 Dividato. All rights reserved
 *
 * Copyright Version 1.0
 *
 * Create on 04/07/2011
 */
package com.security.utils.comparators;

import java.util.Comparator;

import com.security.modelo.general.Pais;

/**
 * @author Ezequiel Beccaria
 *
 */
public class OrdenaPaisesPorNombrePrimeroArgentina implements Comparator<Pais>{

	@Override
	public int compare(Pais o1, Pais o2) {
		if("argentina".equalsIgnoreCase(o1.getNombre()) && !"argentina".equalsIgnoreCase(o2.getNombre()))
			return -1;
		if(!"argentina".equalsIgnoreCase(o1.getNombre()) && "argentina".equalsIgnoreCase(o2.getNombre()))
			return 1;
		int cmp = o1.getNombre().compareTo(o2.getNombre());
		return cmp;
	}

}

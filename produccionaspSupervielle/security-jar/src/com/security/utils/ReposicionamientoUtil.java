package com.security.utils;

import java.util.Collection;
import java.util.List;

import com.security.accesoDatos.configuraciongeneral.interfaz.ElementoService;
import com.security.modelo.administracion.ClienteAsp;
import com.security.modelo.configuraciongeneral.Deposito;
import com.security.modelo.configuraciongeneral.Elemento;
import com.security.modelo.configuraciongeneral.Grupo;
import com.security.modelo.configuraciongeneral.Modulo;
import com.security.modelo.configuraciongeneral.TipoElemento;

public class ReposicionamientoUtil {
	
	
	public ReposicionamientoUtil(){
		
	}
	
	
	/**
	 * Verifica que todos los elementos de la coleccion sean del mismo tipo de elemento. Se calcula la igualdad por el prefijo del tipo de elemento.
	 * @param elementos la coleccion con los elementos a comparar
	 * @return true si todos los elementos son del mismo tipo
	 */
	public Boolean verificarDetallesLecturaMismoPrefijo(Collection<Elemento> elementos, TipoElemento tipoElemento){
		Boolean result = true;
		String prefijo = null;
		try{
			for(Elemento e : elementos){
				prefijo = e.getCodigo().substring(0,1);
				if(!prefijo.equals(tipoElemento.getPrefijoCodigo())){
					result = false;
					break;
				}
			}
		}catch(Exception ex){
			result = false;
		}
		return result;		
	}
	
	/**
	 * Verifica que todos los elementos de la coleccion sean del mismo tipo de elemento. Se calcula la igualdad por el metodo equals del tipo de elemento.
	 * @param elementos la coleccion con los elementos a comparar
	 * @return true si todos los elementos son del mismo tipo
	 */
	public Boolean verificarTodosElementosMismoTipo(Collection<Elemento> elementos, TipoElemento tipoElemento){
		Boolean result = true;
		try{
			for(Elemento e : elementos){
				if(!e.getTipoElemento().equals(tipoElemento)){
					result = false;
					break;
				}
			}
		}catch(Exception ex){
			result = false;
		}
		return result;		
	}
	
	/**
	 * Verifica que todos los elementos de la coleccion sean del mismo tipo de elemento. Se calcula la igualdad por el metodo equals del tipo de elemento.
	 * @param elementos la coleccion con los elementos a comparar
	 * @return true si todos los elementos son del mismo tipo
	 */
	public Boolean verificarTodosElementosPosicionables(Collection<Elemento> elementos){
		Boolean result = true;
		try{
			for(Elemento e : elementos){
				if(!Boolean.TRUE.equals(e.getTipoElemento().getPosicionable())){
					result = false;
					break;
				}
			}
		}catch(Exception ex){
			result = false;
		}
		return result;		
	}
	
	/**
	 * Verifica que haya suficientes posiciones en el modulo destino para ubicar los elementos a reposicionar.
	 * @param elementos
	 * @param moduloDestino
	 * @return
	 */
	public Boolean verificarSuficientesPosicionesModuloDestino(Collection<Elemento> elementos, Modulo moduloDestino){
		
		int verPorModulo = moduloDestino.getEstante().getGrupo().getVerticales().intValue() / moduloDestino.getEstante().getGrupo().getModulosVert().intValue();  
		int horPorModulo = moduloDestino.getEstante().getGrupo().getHorizontales().intValue() / moduloDestino.getEstante().getGrupo().getModulosHor().intValue();
		int posPorModulo = verPorModulo * horPorModulo;
		
		return elementos.size()<= posPorModulo;
		
	}
	
	/**
	 * verifica que el numero de elementos sea multiplo del numero de posiciones horizontales del modulo (reposicionamiento a modulo completo)
	 */
	public Boolean verificarReposicionamientoModuloCompleto(Collection<Elemento> elementos, Modulo modulo){
		Grupo grupo = modulo.getEstante().getGrupo();
		int posicionesHorizontales = grupo.getHorizontales() / grupo.getModulosHor();
		return ((long)elementos.size()%posicionesHorizontales) == 0;
	}
	
	/**
	 * verifica que todos los modulos del grupo al que pertenece modulo destino y que tengan la misma posicion, tengan el mismo tipo de elemento asignado
	 * @param tipoElemento
	 * @param moduloDestino
	 * @return
	 */
	public Boolean verificarTipoValidoParaModuloDestino(TipoElemento tipoElemento, Modulo moduloDestino , ElementoService elementoService, ClienteAsp clienteAsp){
		Boolean result = false;
		if(tipoElemento!=null && moduloDestino != null && elementoService!=null){
			result = elementoService.verificarTipoElementoValidoParaGrupoDeModulos(tipoElemento, moduloDestino, clienteAsp);
		}
		return result;
	}
	
	public Boolean verificarDepositoActualIgualDepositoDestino(Modulo moduloDestino, List<Elemento> elementosReposicionados){
		Boolean result = true;
		Deposito dep = moduloDestino.getEstante().getGrupo().getSeccion().getDeposito();
		for(Elemento e: elementosReposicionados){
			if(e.getDepositoActual()==null){
				e.setDepositoActual(dep);
			}
			if(!e.getDepositoActual().getId().equals(dep.getId())){
				result = false;
				break;
			}
		}
		return result;
	}
}

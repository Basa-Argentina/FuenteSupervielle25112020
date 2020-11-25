
package com.dividato.configuracionGeneral.validadores;

import static com.security.recursos.Configuracion.formatoFechaFormularios;

import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.security.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.bind.WebDataBinder;

import com.security.accesoDatos.configuraciongeneral.interfaz.ReferenciaService;
import com.security.modelo.administracion.ClienteAsp;
import com.security.modelo.configuraciongeneral.LoteReferencia;
import com.security.modelo.configuraciongeneral.Referencia;
import com.security.modelo.seguridad.User;

/**
 * 
 * @author FedeMz
 *
 */
@Component
public class LoteReferenciaValidator implements Validator{

	private ReferenciaService referenciaService;
	private static Logger logger=Logger.getLogger(LoteReferenciaValidator.class);
	
	@Autowired
	public void setReferenciaServices(ReferenciaService referenciaService) {
		this.referenciaService = referenciaService;
	}
		
	@Override
	public boolean supports(Class type) {
		return LoteReferencia.class.isAssignableFrom(type);
	}

	public void initDataBinder(WebDataBinder binder) {
		binder.setRequiredFields(new String[] {"fechaRegistro"});
		binder.registerCustomEditor(Date.class, "fechaRegistro",new CustomDateEditor(formatoFechaFormularios, true));
	}

	@Override
	public void validate(Object command, Errors errors) {
		LoteReferencia lote = (LoteReferencia) command;
		if(lote.getEmpresa()==null){
			errors.rejectValue("codigoEmpresa", "required");
		}
		if(lote.getSucursal()==null){
			errors.rejectValue("codigoSucursal", "required");
		}
		if(lote.getClienteEmp()==null){
			errors.rejectValue("codigoCliente", "required");
		}
		
		List<Referencia> lista = lote.getReferencias();
		String idReferencias = "";
		boolean primeraVez = true, saltea = false, entra = false;
		Integer cantRefe = new Integer(0);
		
		if(lista !=null && lista.size()>0){
			logger.error(obtenerUser().getPersona() + "--Entra a chequear la lista de referencias.");
			
			//for(Referencia ref : lista)
			for (Iterator<Referencia> iter = lista.iterator(); iter.hasNext();)
			{
				Referencia ref = iter.next();
				saltea = false;
				entra = false;
				if(ref.getLoteReferencia()!=null && ref.getLoteReferencia().getId()!=null 
						&& (lote.getId()==null 
								|| lote.getId().longValue()==0 
								|| lote.getId().longValue()!=ref.getLoteReferencia().getId().longValue()))
				{
					errors.rejectValue("referencias", "formularioLoteReferencia.error.referenciasCruzadas");
					logger.error("Error en referencias cruzadas");
					break;
				}
				
				if(lote.getModificadas()!=null && lote.getModificadas().size()>0)
					for(Referencia refMod : lote.getModificadas()){
						if(refMod.getId()!=null && ref.getElemento()!=null && refMod.getElemento().getId().longValue()==ref.getElemento().getId().longValue()){
							saltea = true;
							break;
						}
						if(refMod.getId()!=null && ref.getId()!=null && refMod.getId().longValue()==ref.getId().longValue()){
							entra = true;
							break;
						}
					}
				
				if(lote.getEliminadas()!=null && lote.getEliminadas().size()>0)
					for(Referencia refEli : lote.getEliminadas())
						if(refEli.getId()!=null && ref.getElemento()!=null && refEli.getElemento().getId().longValue()==ref.getElemento().getId().longValue()){
							saltea = true;
							break;
						}
				
				if(!saltea && (ref.getId()==null || entra)){
					if(primeraVez){
						idReferencias = ref.getElemento().getId().toString();
						primeraVez = false;
					}else{
						idReferencias += ","+ref.getElemento().getId().toString(); 
					}
				}
				
			}
			
			if(lote.getIndiceIndividual()){
				
				if(!idReferencias.equals("")){
					cantRefe = referenciaService.cantReferenciasExistenPorElementos(idReferencias, obtenerClienteAspUser().getId());
				}
				
				if(cantRefe.longValue() > 0){
					
					errors.rejectValue("referencias", "formularioLoteReferencia.error.referenciasDuplicadas");
					logger.error(obtenerUser().getPersona()+ "--Error, referencias ya cargadas en la base de datos");				
//					for(Referencia refe : lista)
//					{
//						boolean existe = false;
//						
//						if(lote.getModificadas()!=null && lote.getModificadas().size()>0)
//							for(Referencia refMod : lote.getModificadas())
//								if(refMod.getId()!=null && refMod.getId().longValue()==refe.getId().longValue()){
//									existe = true;
//									break;
//								}
//						
//						if(lote.getEliminadas()!=null && lote.getEliminadas().size()>0 && !existe)
//							for(Referencia refEli : lote.getEliminadas())
//								if(refEli.getId()!= null && refEli.getId().longValue()==refe.getId().longValue()){
//									existe = true;
//									break;
//								}
//						
//						if(!existe){
//							errors.rejectValue("referencias", "formularioLoteReferencia.error.referenciasDuplicadas");
//							logger.error(obtenerUser().getPersona()+ "--Error, referencias ya cargadas en la base de datos");
//							break;
//						}
//					}
				}
			}
			
			if(lote.getIndiceIndividual()){
				Referencia[] array = (Referencia[])lista.toArray(new Referencia[lista.size()]);
				String contenedor = array[0].getElementoContenedor().getCodigo();
				for(Referencia refe : lista){
					System.out.println(refe.getTexto1());
					if(!contenedor.equalsIgnoreCase(refe.getElementoContenedor().getCodigo())){
						errors.rejectValue("referencias", "formularioLoteReferencia.error.distintosContenedores");
						break;
					}
					System.out.println("OK");
				}
			}
			
		}else{
			errors.rejectValue("referencias", "formularioLoteReferencia.error.loteVacio");
		}
		
	}

	private User obtenerUser(){
		return ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
	}
	
	private ClienteAsp obtenerClienteAspUser(){
		return ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getCliente();
	}
}

package com.dividato.security.validadores;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.bind.WebDataBinder;

import com.security.accesoDatos.interfaz.CampoComparacion;
import com.security.accesoDatos.interfaz.GroupService;
import com.security.modelo.seguridad.Group;
/**
 * 
 * @author Gabriel Mainero
 * @modif Ezequiel Beccaria (05/05/2011)
 *
 */
@Component
public class GroupValidator implements Validator {
	
	private GroupService groupService;
	
	@Autowired
	public void setGroupService(GroupService groupService) {
		this.groupService = groupService;
	}
	/**
	 * Indica cual es la clase que este validador puede validar.
	 */
	@SuppressWarnings("unchecked")
	@Override
	public boolean supports(Class type) {
		return Group.class.isAssignableFrom(type);
	}

	/**
	 * Inicializa el WebDataBinder con los campos requeridos y las conversiones de tipos de datos.
	 * @param binder
	 */
	public void initDataBinder(WebDataBinder binder) {
		binder.setRequiredFields(new String[] {"groupName"});
	}
	/**
	 * Valida los valores de las propiedades del objeto.
	 */
	@Override
	public void validate(Object command, Errors errors) {
		
		Group group = (Group) command;
		Group group3 = null;
		
		//Valido que se alla seleccionado algun privilegio
		if(group.getGroupPrivileges()==null || "".equals(group.getGroupPrivileges()))
			errors.rejectValue("groupPrivileges", "error.group.noAsignoPrivilegio");
		
		if(group.getAccion().equals("MODIFICACION")){
			group3 = groupService.obtenerPorId(group.getId());
		}
		if(group3!=null){
			if(group3.getGroupName().equalsIgnoreCase(group.getGroupName()))
				return;
		}		
		//ArrayList<Group> lista = (ArrayList<Group>) groupService.listarPorGroupName(group.getGroupName());
		List<Group> lista = groupService.listarTodosFiltradoPorLista(
				new CampoComparacion("groupName",group.getGroupName()),
				new CampoComparacion("cliente",group.getCliente()));
		if(lista!=null && lista.size()>0){	
			errors.rejectValue("groupName", "error.group.nombreGrupoExistente");
		}		
	}	
			
}
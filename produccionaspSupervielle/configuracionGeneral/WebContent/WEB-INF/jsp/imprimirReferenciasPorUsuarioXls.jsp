<%@page import="com.dividato.configuracionGeneral.objectForms.ReferenciasPorUsuarioReport"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="display" uri="http://displaytag.sf.net" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fmt" uri="http://www.costapag.org/tags/format"%>
<%@ page import="java.util.*" %> 
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<% 
response.setContentType("application/vnd.ms-excel");
response.setHeader("Content-Disposition", "attachment; filename=referencias_por_usuario.xls");

// response.setContentType("application/application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
// response.setHeader("content-disposition", "attachment; filename=myfile.xlsx");

//	List<Object> listaNombre = (List<Object>) request.getAttribute("listaNombre");
	List<ReferenciasPorUsuarioReport> listado = (List<ReferenciasPorUsuarioReport>) request.getAttribute("listado");
	String usuario = (String) request.getAttribute("usuario");
	String fechaDesde = (String) request.getAttribute("fechaDesde");
	String fechaHasta = (String) request.getAttribute("fechaHasta");
	int cantidadRefCreadas = (Integer) request.getAttribute("cantidadRefCreadas");
	int cantidadRefModificadas = (Integer) request.getAttribute("cantidadRefModificadas");
%>
</head>
<body>


<table bgcolor="#FCFCFC">
	<tr>
		<td>Fecha Desde:</td>
		<td align="left"><strong><%=fechaDesde %> </strong> </td>
		<td>Fecha Hasta:</td>
		<td align="left"><strong><%=fechaHasta %></strong></td>
		<td></td>
		<td></td>
		<td></td>
	</tr>
	<tr>
		<td>Creadas:</td>
		<td align="left"><strong><%=cantidadRefCreadas %></strong></td>
		<td>Modificadas:</td>
		<td align="left"><strong><%=cantidadRefModificadas %></strong></td>
		<td></td>
		<td></td>
		<td></td>
	</tr>
	<tr>
		<td>Usuarios:</td>
		<td colspan="5"><%=usuario %> </td>
		<td></td>
	</tr>
	<tr>
		<td></td>
		<td></td>
		<td></td>
		<td></td>
		<td></td>
		<td></td>
		<td></td>
	</tr>
	<tr>
		<td></td>
		<td></td>
		<td></td>
		<td></td>
		<td></td>
		<td></td>
		<td></td>
	</tr>
	<tr>
		<td><strong>Cantidad</strong></td>
		<td><strong>Acción</strong></td>
		<td><strong>Código</strong></td>
		<td><strong>Descripción</strong></td>
		<td></td>
		<td></td>
		<td></td>
	</tr>
	<tr>
		<td></td>
		<td></td>
		<td></td>
		<td></td>
		<td></td>
		<td></td>
		<td></td>
	</tr>
	
	<%

	
	if(listado!= null) { // && listaNombre.size()>0){
		for(ReferenciasPorUsuarioReport l : listado){

	%>
	
	<tr>

		<td><%=l.getCantidad() %></td>
		<td><%=l.getAccion()%></td>
		<td><%=l.getCodigo()%></td>
		<td><%=l.getDescripcion()%></td>
		<td></td>
		<td></td>
		<td></td>

	</tr>
	<%
		}
	}
	
	%>
</table>

</body>
</html>
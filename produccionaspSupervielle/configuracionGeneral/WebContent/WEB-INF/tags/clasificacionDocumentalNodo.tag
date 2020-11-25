<%@ tag display-name="Mostrar Nodo Arbol" example="configuracionDocumentalNodo" description="Muestra un nodo del arbol"%>

<%@ taglib prefix="cstmTag" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<%@ attribute name="listaClasificacionDocumental" required="true" type="java.util.List" rtexprvalue="true" %>


	<c:forEach items="${listaClasificacionDocumental}" var="clasificacionDocumental">
	<li>
		<span class="txtArbol">
			<c:out value="${clasificacionDocumental.codigo}"/> - <c:out value="${clasificacionDocumental.nombre}"/>
		</span>
		<input type="hidden" id="hdn_id" value="${clasificacionDocumental.id}"/>
		<input type="hidden" id="hdn_nodo" value="${clasificacionDocumental.nodo}"/>
		<input type="hidden" id="hdn_codigo" value="${clasificacionDocumental.codigo}"/>
		<input type="hidden" id="hdn_nombre" value="${clasificacionDocumental.nombre}"/>
		<c:if test="${clasificacionDocumental.nodo == 'N'}">
			<ul>
				<cstmTag:clasificacionDocumentalNodo listaClasificacionDocumental="${clasificacionDocumental.listaHijosOrdenada}"/>
			</ul>	
		</c:if>
	</li> 
	</c:forEach>

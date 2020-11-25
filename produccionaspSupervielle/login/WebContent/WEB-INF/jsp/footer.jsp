<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.util.Date"%>
<%
String loc = org.springframework.web.servlet.support.RequestContextUtils.getLocaleResolver(request).resolveLocale(request).toString();
pageContext.setAttribute("loc",loc);
%>

<div style="width: 100%; float: left; padding-top: 20px;">	
	<table style="width: 1024px;">
		<tr>
			<td style="vertical-align: middle; text-align: center;" class="footer">
				©<%=(new SimpleDateFormat("yyyy")).format(new Date())%> Banco de Archivos S.A.
			</td>	
		</tr>
	</table>
</div>
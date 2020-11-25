<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
	String version = request.getHeader("User-Agent");
	boolean ie = version.indexOf("MSIE") >= 0;
	boolean ie8 = version.indexOf("MSIE 8") >=0;
	boolean ie9 = version.indexOf("MSIE 9") >=0;
	pageContext.setAttribute("ie", ie || ie8 || ie9);
	pageContext.setAttribute("ie9", ie9);
%>
<c:if test="${ie==true}">
	<link rel="stylesheet" href="css/screen_ie.css" type="text/css"/>
</c:if>
<c:if test="${ie==false}">
	<link rel="stylesheet" href="css/screen_ff.css" type="text/css"/>
</c:if>
<c:if test="${ie9==false}">
	<link rel="stylesheet" href="css/menu.css" type="text/css"/>
</c:if>
<c:if test="${ie9==true}">
	<link rel="stylesheet" href="css/menu_ie9.css" type="text/css"/>
</c:if>
<link rel="stylesheet" href="css/forms.css" type="text/css"/>
<link rel="stylesheet" href="css/displaytag.css" type="text/css"/>
<link rel="stylesheet" href="css/calendar.css" type="text/css"/>
<link rel="stylesheet" href="css/ini.css" type="text/css" />
<link rel="stylesheet" href="css/jquery.alerts.css" type="text/css" />
<link rel="stylesheet" href="css/tabs.css" type="text/css" />
<link rel="stylesheet" href="css/folders.css" type="text/css" />
<link rel="stylesheet" href="css/tooltip.css" type="text/css" />
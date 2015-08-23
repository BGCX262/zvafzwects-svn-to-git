<%@ include file="inc/before.jsp" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%-- Author: Markus Henn --%>

<h1>Automatische Weiterleitung</h1>
<div>Sie werden in 5 Sekunden auf Ihre Ausgangsseite weitergeleitet.<br />
Alternativ können sie folgenden Link verwenden:
	<a href="<c:out value="${redirect}" />"><c:out value="${redirect}" /></a>
</div>
<%@ include file="inc/after.jsp" %>
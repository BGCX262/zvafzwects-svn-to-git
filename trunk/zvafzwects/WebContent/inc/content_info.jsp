<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%-- Author: Christian Zoellner, Markus Henn --%>

<%-- Shows messages if available  --%>

<%-- Success --%>
<c:if test="${not empty successMsg}">
	<div id="success_message" class="message">
		<div class="content">
			<c:out value="${successMsg}" />
		</div>
	</div>
</c:if>

<%-- Info --%>
<c:if test="${not empty infoMsg}">
	<div id="info_message" class="message">
		<div class="topic">Hinweis:</div>
		<div class="content">
			<c:out value="${infoMsg}" />
		</div>
	</div>
</c:if>

<%--  Error --%>
<c:if test="${not empty errorMsg}">
	<div id="error_message" class="message">
		<div class="topic">Fehler:</div>
		<div class="content">
			<c:out value="${errorMsg}" />
		</div>
	</div>
</c:if>

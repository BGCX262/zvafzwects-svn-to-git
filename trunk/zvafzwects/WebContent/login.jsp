<%@ include file="inc/before.jsp" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%-- Author: Markus Henn --%>

<h1>Anmelden</h1>
<form id="login" action="user?action=login" method="post">
	<c:choose>
		<c:when test="${not empty redirectParameter}">
			<input type="hidden" name="redirectParameter" value="<c:out value='${redirectParameter}' />" />
		</c:when>
		<c:otherwise>
			<input type="hidden" name="redirectParameter" value="<c:out value='${header.referer}' default="" />" />
		</c:otherwise>
	</c:choose>
	<div class="form_input">
		<label for="login_email">E-Mail-Adresse: </label>
		<input id="login_email" name="login_email" type="text" value="" />
	</div>
	<div class="form_input">
		<label for="login_password">Passwort: </label>
		<input id="login_password" name="login_password" type="password" value="" />
	</div>
	<div class="form_submit">
		<input class="form_submit_button" type="submit" value="Anmelden" />
	</div>
</form>

<%@ include file="inc/after.jsp" %>
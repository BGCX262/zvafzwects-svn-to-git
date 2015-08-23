<%@ include file="inc/before.jsp"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%-- Author: Andreas Baur --%>

<h1>Adresse anlegen</h1>

<div id="create_address">
	<form action="user?action=create_address" method="post">
		<c:choose>
			<c:when test="${not empty redirect}" >
				<input type="hidden" name="redirectParameter" value="<c:out value='${redirect}' />" />
			</c:when>
			<c:otherwise>
				<input type="hidden" name="redirectParameter" value="<c:out value='${header.referer}' />" />
			</c:otherwise>
		</c:choose>
		<div class="form_input">
			<label for="address_street">Straße: </label> 
			<input id="address_street" name="address_street" type="text" value="" />
		</div>
		<div class="form_input">
			<label for="address_housenumber">Hausnummer: </label> 
			<input id="address_housenumber" name="address_housenumber" type="text" value="" />
		</div>
		<div class="form_input">	
			<label for="address_zipcode">PLZ: </label> 
			<input id="address_zipcode" name="address_zipcode" type="text" value="" />
		</div>
		<div class="form_input">
			<label for="address_city">Stadt: </label> 
			<input id="address_city" name="address_city" type="text" value="" />
		</div>
		<div class="form_submit">
			<input class="form_submit_button" type="submit" value="Adresse anlegen" />
		</div>
	</form>
</div>

<%@ include file="inc/after.jsp"%>
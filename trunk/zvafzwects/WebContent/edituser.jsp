<%@ include file="inc/before.jsp"%>

<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%-- Author: Jochen Pätzold, Andreas Baur --%>

<c:choose>
	<c:when test="${empty user}">
		<h1>Registrierung</h1>
	</c:when>
	<c:otherwise>
		<h1>Profil editieren</h1>
	</c:otherwise>
</c:choose>

<form id="edituser" action="<c:out value='${formAction}' />"
	method="post">

	<div class="form_input">
		<label for="user_surname">Nachname: </label> <input id="user_surname"
			name="user_surname" type="text"
			value="<c:out value='${user.surname}' />" />
	</div>
	<div class="form_input">
		<label for="user_forename">Vorname: </label> <input id="user_forename"
			name="user_forename" type="text"
			value="<c:out value='${user.forename}' />" />
	</div>
	<div class="form_input">
		<label for="user_date_of_birth">Geburtsdatum: </label> <input
			id="user_date_of_birth" name="user_date_of_birth" type="date"
			value="<c:out value='${user.dateOfBirthAsString}' />" />
	</div>
	<div class="form_input">
		<label for="user_gender">Geschlecht: </label> <input type="radio"
			name="user_gender" value="m"
			<c:if test="${not empty user && 'm' == user.gender.toString()}"> checked="checked" </c:if>>
		m &nbsp; <input type="radio" name="user_gender" value="w"
			<c:if test="${not empty user && 'w' == user.gender.toString()}"> checked="checked" </c:if>>
		w <br />
	</div>
	<div class="form_input">
		<label for="user_email">E-Mail-Adresse: </label> <input
			id="user_email" name="user_email" type="email"
			value="<c:out value='${user.email}' />" />
	</div>
	<c:if test="${not empty user}">
		<label>Passwortänderung ist optional</label>
		<div class="form_input">
			<label for="user_password_new">Neues Passwort: </label> <input
				id="user_password_new" name="user_password_new" type="password"
				value="" />
		</div>
		<div class="form_input">
			<label for="user_password_approve">Passwort wiederholen: </label> <input
				id="user_password_approve" name="user_password_approve"
				type="password" value="" />
		</div>
	</c:if>
	<div class="form_input">
		<c:choose>
			<c:when test="${empty user}">
				<label for="user_password">Passwort: </label>
			</c:when>
			<c:otherwise>
				<label for="user_password">Passwort eingeben, um Änderungen
					zu übernehmen:</label>
			</c:otherwise>
		</c:choose>
		<input id="user_password" name="user_password" type="password"
			value="" />
	</div>
	<div class="form_submit">
		<c:choose>
			<c:when test="${empty user}">
				<input class="form_submit_button" type="submit" value="Registrieren" />
			</c:when>
			<c:otherwise>
				<input class="form_submit_button" type="submit" value="Übernehmen" />
			</c:otherwise>
		</c:choose>
	</div>
</form>
<c:if test="${not empty user}">
				<h2>Adressen</h2>
				<c:forEach var="address" items="${user.addresses}">
					<div class="address">
						<c:out value="${address.street}" /> <c:out value="${address.houseNumber}" />,
						<c:out value="${address.zipcode}" /> <c:out value="${address.city}" />
					</div>
				</c:forEach>
	<div>
		<a href="user?action=display_create_address">weitere Adresse anlegen</a>
	</div>
</c:if>

<%@ include file="inc/after.jsp"%>
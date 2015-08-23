<%@ include file="inc/before.jsp"%>

<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%-- Author: Christian Zoellner, Andreas Baur --%>

<h1>Registrieren</h1>

<form id="register" action="user" method="post">
	<input name="action" value="register" type="hidden" />
	<div class="form_input">
		<label for="register_surname">Nachname: </label> <input
			id="register_surname" name="register_surname" type="text" value="" />
	</div>
	<div class="form_input">
		<label for="register_forename">Vorname: </label> <input
			id="register_forename" name="register_forename" type="text" value="" />
	</div>
	<div class="form_input">
		<label for="register_date_of_birth">Geburtsdatum: </label> <input
			id="register_date_of_birth" name="register_date_of_birth" type="date"
			value="" />
	</div>
	<div class="form_input">
		<label for="register_gender">Geschlecht: </label>
		<select id="register_gender" name="register_gender">
			<option value="m">männlich</option>
			<option value="w">weiblich</option>
		</select>
	</div>
	<div class="form_input">
		<label for="register_email">E-Mail-Adresse: </label> <input
			id="register_email" name="register_email" type="email" value="" />
	</div>
	<div class="form_input">
		<label for="register_password">Passwort: </label> <input
			id="register_password" name="register_password" type="password"
			value="" />
	</div>
	<div class="form_input">
		<label for="register_isadmin">Admin: </label> <input
			id="register_isadmin" name="register_isadmin" type="checkbox"
			value="" />
	</div>
	<div class="form_submit">
		<input class="form_submit_button" type="submit" value="Registrieren" />
	</div>
</form>
<%@ include file="inc/after.jsp"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<!-- header_account Anfang -->
<div id="header_account">
<%-- Loginformular (nur für nicht angemeldete Nutzer) --%>
<c:choose>
<c:when test="${ empty sessionScope.cook }">
	<form class="account" action="controller" method="post" id="form-login">
		<fieldset>
			<legend>Login</legend>
			<label for="username">Name</label>
			<input type="hidden" name="action" value="LoginCook" /> 
			<input id="username"  type="text" value="" name="username" size="19" maxlength="30" />
	
		
			<label for="password">Passwort</label>
			<input id="password" type="password" value="" name="password" size="19" maxlength="30" />
				
			
			<input class="accountbutton" type="submit" value="login" /> 
			<br />
			<br />
	
			<a href="/lokalhorst/controller?action=Redirect&amp;targetview=RegisterCook">Registrieren</a>
			<a style="margin-left:10px" href="/lokalhorst/controller?action=Redirect&amp;targetview=SecretName">Passwort vergessen</a>
		</fieldset>
	</form>
</c:when>

<%-- Logout (nur für angemeldete Nutzer) --%>
<c:otherwise> 
	<form class="account" action="controller" method="post" id="form-logout">
		<fieldset>
			<legend>Account</legend>
			<p>Angemeldet:  <c:out value="${ sessionScope.cook.cook_name }" default="" /></p>
			<c:choose>
			<c:when test="${ sessionScope.cook.is_admin and sessionScope.cook.cook_id == 1 }">
				<p>Rang: Meisterkoch</p>
			</c:when>
			<c:when test="${ sessionScope.cook.is_admin}">
				<p>Rang: Chefkoch</p>
			</c:when>
			<c:otherwise>
				<p>Rang: Hobbykoch</p>
			</c:otherwise>
			</c:choose>
			<br />
			<input type="hidden" name="action" value="LogoutCook" /> 
			<input type="submit" value="logout" /> 
		</fieldset>
	</form>
</c:otherwise>	
</c:choose>
</div>
<!-- header_account Ende -->
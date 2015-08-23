<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="de" lang="de">
	<head>
	<title>Kochrezeptplattform - Team Lokalhorst</title>
	<meta http-equiv="content-type" content="text/html;charset=utf-8" />
	<link rel="stylesheet" type="text/css" href="styles/global.css"	media="screen" />
	</head>
	<body>
		<!-- outer_box Anfang -->
		<div id="outer_box">
			<div id="header_font"><img src="styles/kochrezepte.png" width="338" height="36" alt="bild_kochrezepte" /></div>
	
			<!-- Kochmuetze -->
			<div id="togue"><img src="styles/togue.png" width="221" height="280" alt="bild_kochmuetze" /></div>
	
			<!-- box Anfang-->
			<div id="box">
			
				<!-- header Anfang -->
				<div id="header">
					<%-- Schnellsuche --%> 
					<jsp:directive.include file="/template/header_search.jsp" /> 
					
					<%-- Account: Login/Logout --%>
					<jsp:directive.include file="/template/header_account.jsp" />
				</div> <!-- header Ende --> 
				
				<%-- Navigationsleiste --%> 
				<jsp:directive.include file="/template/navbar.jsp" /> 
				
				<!-- Content Anfang -->
				<div id="content">
					<a></a> <%-- Ausgabe von Informations- und Fehlermeldung --%>
					<jsp:directive.include file="/template/content_info.jsp" />
					
					<h2>Registrierung</h2>
					<br />
					<br />
					
					<%-- action: Seite an die die Daten gehen --%> <%-- method: Art der Datenübermittlung --%>
					<form class="register" action="controller" method="post" id="form-register">
						<fieldset class="register">
							<legend class="register">Anmeldedaten</legend>
							<br />
							<p>Um sich auf unserer Seite registrieren zu können, müssen Sie unten stehende Felder ausfüllen</p>
							<br />
								
								<p>
									<label for="form-register-name">Name: </label><%-- Name der registriert werden soll --%>
									<input type="text" value="<c:out value="${ out.newName }" default= "" />" name="newName" size="30" maxlength="30" id="form-register-name" /> 5 bis 30 Zeichen
								</p>
								<p>
									<label for="form-register-password">Passwort: </label><%-- Passwort --%>
									<input type="password" value="<c:out value="${ out.newPassword }" default= "" />" name="newPassword" size="30" maxlength="30" id="form-register-password" /> 5 bis 30 Zeichen
								</p>
								<p>
									<label for="form-register-password-2">Wiederholung: </label><%-- Passwort-2 --%>
									<input type="password" value="<c:out value="${ out.newPassword_2 }" default= "" />" name="newPassword_2" size="30" maxlength="30" id="form-register-password-2" />
								</p>
								<p>
									<label for="form-register-question">Frage: </label><%-- Geheime Frage --%>
									<input type="text" value="<c:out value="${ out.newQuestion }" default= "" />" name="newQuestion" size="50" maxlength="100" id="form-register-question" /> 10 bis 100 Zeichen
								</p>
								<p>
									<label for="form-register-answer">Antwort: </label><%-- Antwort auf die Geheimfrage --%>
									<input type="text" value="<c:out value="${ out.newAnswer }" default= "" />" name="newAnswer" size="50" maxlength="100" id="form-register-answer" /> 10 bis 100Zeichen
								</p>
		
		
								<%--  Action-Parameter für den CommanBroker (hier: registerCook) --%> 
								<input type="hidden" name="action" value="RegisterCook" /> <%-- Button der die Registrierung auslöst --%>
								<input id="submit_register" type="submit" value="Registrieren" />
						</fieldset>
					</form>
				</div> <!-- Content Ende --> 
			
				<%-- Footer --%> 
				<jsp:directive.include file="/template/footer.jsp" />
			
			</div><!--Box Ende-->
		</div><!--outer_box Ende-->
	</body><!-- body Ende -->
</html><!-- HTML Ende -->
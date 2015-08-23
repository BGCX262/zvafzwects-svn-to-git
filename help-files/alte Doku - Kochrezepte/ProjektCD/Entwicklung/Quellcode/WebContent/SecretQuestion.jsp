<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd"> 
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<html  xmlns="http://www.w3.org/1999/xhtml" xml:lang="de" lang="de">
<head>
	<title>Kochrezeptplattform - Team Lokalhorst</title>
	<meta http-equiv="content-type" content="text/html;charset=utf-8" />
	<link rel="stylesheet" type="text/css" href="styles/global.css" media="screen" />
</head>
<body>
	<!-- outer_box Anfang -->
	<div id="outer_box">
		<div id="header_font">
			<img src="styles/kochrezepte.png" width="338" height="36" alt="bild_kochrezepte"/>
		</div>
	
		<!-- Kochmuetze -->
		<div id="togue">
			<img src="styles/togue.png" width="221" height="280" alt="bild_kochmuetze"/>
		</div>
	
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
				<%-- Hinweis- und/oder Fehlermeldungen --%>
				<jsp:directive.include file="/template/content_info.jsp" /> 
				<h2>Passwort vergessen</h2>
				<br />
				<br />
				<p>Das hätte ihnen aber nicht passieren dürfen ... dennoch können Sie Ihr Passwort ändern, wenn Sie
				Ihre geheime Frage richtig beantworten können.</p>
				
					<form class="secretquestion" action="controller" method="post" id="form-secretQuestion">
						<fieldset class="secretquestion">
							<p>Beantworten Sie die Frage und geben ein neues Passwort an.</p>
							<br />
							<p>
								Name: <c:out value="${ sessionScope.sec_cook.cook_name }" default= ""/>
							</p>
							<p>
								Frage: <c:out value="${ sessionScope.sec_cook.sec_question }" default= ""/>
							</p>
							<br />
							<p>
								<label for="form-secretQuestion-answer">Antwort: </label>
								<%-- Antwort die gesucht wird --%>
								<input type="text" value="<c:out value="${out.answer}" default= "" />" name="answer" size="50" maxlength="100" id="form-secretQuestion-answer" />
							</p>
							<p>
								<label for="form-secretQuestion-password">neues Passwort: </label>
								<%-- Neues Passwort --%>
								<input type="password" value="<c:out value="${out.password}" default= "" />" name="password" size="30" maxlength="30" id="form-secretQuestion-password" />
								5 bis 30 Zeichen
							</p>
							<p>
								<label for="form-secretQuestion-password-2">Wiederholung: </label>
								<%-- Wiederholgung des Passwortes --%>
								<input type="password" value="<c:out value="${out.password_2}" default= "" />" name="password_2" size="30" maxlength="30" id="form-secretQuestion-password-2" />
							</p>	
							
							<%--  Action-Parameter für den CommanBroker --%>
							<input type="hidden" name="action" value="SecretQuestion" />
							<input id="submit_secretquestion" type="submit" value="Passwort ändern" />
						</fieldset>
					</form>
			</div> <!-- Content Ende -->
			
			<%-- Footer --%>
			<jsp:directive.include file="/template/footer.jsp" />
			 
		</div> <!--Box Ende-->
	 </div><!--outer_box Ende-->
</body><!-- body Ende -->
</html><!-- HTML Ende -->
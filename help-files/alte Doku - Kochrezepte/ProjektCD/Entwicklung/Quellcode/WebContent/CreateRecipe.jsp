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
				
				<h2>Rezept anlegen</h2> 
				<form class="createrecipe" action="controller" method="post" id="form-createRecipe">
						<fieldset class="createrecipe">
							<p>Bitte geben Sie den Namen (min. 5 Zeichen) sowie eine kurze Beschreibung (min. 10 Zeichen) für das neue Rezept an.</p>
							<br />
					
							<label for="form-createRecipe-name">Name: </label><%-- Name des neuen Rezepts --%>
							<input type="text" value="<c:out value="${out.name}" default= "" />" name="name" size="30" maxlength="30" id="form-createRecipe-name" />
							<br />
							<label for="form-createRecipe-description">Beschreibung: </label><%-- Beschreibung des neuen Rezepts --%>
							<textarea  name="desc" cols="50" rows="4" id="form-createRecipe-description"><c:out value="${out.desc}" default= "" /></textarea>
														
							<%--  Action-Parameter für den CommanBroker --%>
							<input type="hidden" name="action" value="CreateRecipe" />
							<input id="submit_createrecipe" type="submit" value="anlegen" />
						</fieldset>
					</form>		
			</div> <!-- Content Ende -->
			
			<%-- Footer --%>
			<jsp:directive.include file="/template/footer.jsp" />

		</div> <!--Box Ende-->
	 </div><!--outer_box Ende-->
</body><!-- body Ende -->
</html><!-- HTML Ende -->
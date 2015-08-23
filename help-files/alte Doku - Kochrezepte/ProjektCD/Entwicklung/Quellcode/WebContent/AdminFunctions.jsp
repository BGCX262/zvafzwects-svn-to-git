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
				<h2>Administration</h2> 
				<br />
				<br />
				<br />
				<div id="cook_search">
					<h3>User bearbeiten</h3> 
					<hr />
					<br />
					<form class="cook_search" action="controller" method="post">
						<fieldset class="cook_search"> 
							<input type="text" value="Nutzername" name="search_term" size="25" maxlength="18" />
	    					<button name="action" type="submit" value="SearchCook">suchen</button>
						</fieldset> 
					</form>
				</div><!-- cook_search Ende -->
		
			</div> <!-- Content Ende -->
			
			<%-- Footer --%>
			<jsp:directive.include file="/template/footer.jsp" />

		</div> <!--Box Ende-->
	 </div><!--outer_box Ende-->
</body><!-- body Ende -->
</html><!-- HTML Ende -->
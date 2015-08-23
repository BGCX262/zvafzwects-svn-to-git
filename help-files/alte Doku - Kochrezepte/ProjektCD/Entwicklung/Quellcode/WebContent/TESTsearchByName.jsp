<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd"> 
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>


<html xmlns="http://www.w3.org/1999/xhtml">
<head>

<script type="text/javascript" src="http://ajax.googleapis.com/ajax/libs/jquery/1/jquery.min.js"></script> 
<link rel="stylesheet" type="text/css" href="styles/global.css" media="screen" />
<title>Kochrezeptplattform - Team Lokalhorst</title>
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
				<%@ include file="/template/header_search.jsp" %>			
				<%@ include file="/template/header_account.jsp" %>
			</div> <!-- header Ende -->
			
			
			
			<%-- Navigationsleiste --%>
			<jsp:directive.include file="/template/navbar.jsp" />
			
			
			
			<!-- Content Anfang -->
			<div id="content"> 
				<p>Testseite zum suchen nach Rezeptnamen</p>
				
			</div> <!-- Content Ende -->
			
			
			
			
			<p id="footer">Kochrezepteplattform "Lokalhorst" - Informatikprojekt WS 2010/2011 - FH Kaiserslautern</p> 
		</div> <!--Box Ende-->
	 </div><!--outer_box Ende-->
</body><!-- body Ende -->
</html><!-- HTML Ende -->
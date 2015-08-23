<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<html  xmlns="http://www.w3.org/1999/xhtml" xml:lang="de" lang="de">
<head>
	<title>Kochrezeptplattform - Team Lokalhorst</title>
	<meta http-equiv="content-type" content="text/html;charset=utf-8" />
	<link rel="stylesheet" type="text/css" href="styles/global.css" media="screen" />
	<script type="text/javascript" src="http://ajax.googleapis.com/ajax/libs/jquery/1/jquery.min.js"></script> 
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
				<a></a> <%-- Ausgabe von Informations- und Fehlermeldung --%>
				<jsp:directive.include file="/template/content_info.jsp" />
				
				
				<h2>Einkaufsliste</h2>
				
				
				<!-- Eintragsausgabe -->
				<c:forEach items="${out.buylist}" var="recipe">
						<br />
						<br />
      					<h3><c:out value="${recipe.r_name}"/></h3>
      					<hr />
      					<br />
      					<c:forEach items="${recipe.ingredients}" var="ingred">
      						<c:out value="${ingred.amount}"/>
      						<c:out value="${ingred.unit_name}"/>
      						<c:out value="${ingred.ingr_name}"/>
      						<br />
      					</c:forEach>			
      			</c:forEach>
      			<br />
			    <br />
      			<c:forEach items="${out.summation}" var="ueberschrift" begin="0" end="0">
      				<h3>Insgesamt</h3>
      				<hr />
      				<br />
			    </c:forEach>
			    <c:forEach items="${out.summation}" var="ingreds">
			   		<c:out value="${ingreds.value.amount} ${ingreds.value.unit_name} ${ingreds.value.ingr_name}" />
			    	<br />
			    </c:forEach>
			    <br />
			    <br />
			    <br />
			    <a href="/lokalhorst/controller?action=SaveBuyList"><c:out value="Einkaufsliste speichern"/></a>
			    <a href="/lokalhorst/controller?action=ClearBuyList"><c:out value="Einkaufsliste leeren"/></a>
				<br />
				<br />
				<%-- Druckcommand: param=2 entspricht Einkaufsliste ausdrucken --%>
				<a href="/lokalhorst/controller?action=PrintSite&amp;param=2" target="_blank"><c:out value="Druckansicht"/></a>
			</div> <!-- Content Ende -->
			
			<%-- Footer --%>
			<jsp:directive.include file="/template/footer.jsp" />

		</div> <!--Box Ende-->
	 </div><!--outer_box Ende-->
</body><!-- body Ende -->
</html><!-- HTML Ende -->
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd"> 

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<html  xmlns="http://www.w3.org/1999/xhtml" xml:lang="de" lang="de">
<head>
	<title>Kochrezeptplattform - Team Lokalhorst</title>
	<meta http-equiv="content-type" content="text/html;charset=utf-8" />
	<link rel="stylesheet" type="text/css" href="styles/global.css" media="screen" />
	<script type="text/javascript" src="http://ajax.googleapis.com/ajax/libs/jquery/1/jquery.min.js"></script> 
</head>
<body id="print">
				
				<h2>Einkaufsliste</h2>
				
				
				<!-- Eintragsausgabe -->
				
				<c:forEach items="${out.buylist}" var="recipe">
						<p><br /><br /></p>
      					<h3><c:out value="${recipe.r_name}"/></h3>
						
      					<c:forEach items="${recipe.ingredients}" var="ingred">
      						<p><c:out value="${ingred.amount}"/>
      							<c:out value="${ingred.unit_name}"/>
      							<c:out value="${ingred.ingr_name}"/></p>
      					</c:forEach>			
      			</c:forEach>
      			<p><br /><br /></p>
			 	
      			<c:forEach items="${out.summation}" var="ueberschrift" begin="0" end="0">
      				<h3>Insgesamt</h3>
			    </c:forEach>
			    <c:forEach items="${out.summation}" var="ingreds">
			   		<p><c:out value="${ingreds.value.amount} ${ingreds.value.unit_name} ${ingreds.value.ingr_name}" />
			    	<br /></p>
			    </c:forEach>
			    
			    
			
</body><!-- body Ende -->
</html><!-- HTML Ende -->
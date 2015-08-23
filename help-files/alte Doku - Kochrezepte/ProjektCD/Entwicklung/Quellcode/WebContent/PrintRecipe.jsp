<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd"> 
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page import="java.sql.DriverManager" %>
<%@ page import="java.util.LinkedList" %>
<%@ page import="de.lokalhorst.db.dto.*" %>
<%@ page import="de.lokalhorst.helper.*" %>
<%@ page import="de.lokalhorst.helper.RecipeHelper" %>
<%@ page import="java.sql.PreparedStatement" %>
<%@ page import="java.sql.ResultSet" %>
<%@ page import="java.sql.SQLException" %>
<%@ page import="java.util.Collection" %>
<%@ page import="java.util.LinkedList" %>
<%@ page import="java.util.Iterator" %>
<%@ page import="java.util.HashMap" %>


<html xmlns="http://www.w3.org/1999/xhtml">
<head>

<script type="text/javascript" src="http://ajax.googleapis.com/ajax/libs/jquery/1/jquery.min.js"></script> 
<link rel="stylesheet" type="text/css" href="styles/global.css" media="screen" />
<title>Kochrezeptplattform - Team Lokalhorst</title>
</head>
<body id="print">
	
				
				<!-- Anzeige des Rezeptes -->
						
						
	      				<h2><c:out value="${out.recipe.r_name}"/> (für <c:out value="${out.recipe.persons}"/> Personen)</h2>
	      				<p>	
	      					<br />
	      					<c:out value="${out.recipe.r_description}"/>
	      					<br />
	      					<br />
	      					<br />
      					</p>
      					<h3>Zutaten</h3>
      					
      					<p>	<c:forEach items="${out.recipe.ingredients}" var="ingred" varStatus="counter">
      							<c:out value="${ingred.amount}"/>
      							<c:out value="${ingred.unit_name}"/>
      							<c:out value="${ingred.ingr_name}"/>		
      							<br />
      						</c:forEach>
      						<br />
      						<br />
      						<br />
      					</p>
      					
      					<h3>Zubereitung</h3>
      					<p>	<c:forEach items="${out.recipe.prep_steps}" var="prepstep">
      							<c:out value="${prepstep.instruction}"/>
      							<br />
      						</c:forEach>	
      						<br />
      						<br />
      					</p>
      					
      					<h3>Übersicht</h3>
      					
      					<table>
      						<tr>
      							<td>
      								<p><b>Zubereitungszeit:</b></p>
      							</td>
      							<td>
      								<p><c:out value="${out.recipe.prep_time}"/>min.</p>
      							</td>
      						</tr>
      						<tr>
      							<td>
      								<p><b>Schwierigkeitsgrad:</b></p>
      							</td>
      							<td>
      								<p><c:out value="${out.recipe.difficulty}"/></p>
      							</td>
      						</tr>
      						<tr>
      							<td>
      								<p><b>Kategorie:</b></p>
      							</td>
      							<td>
      								<p><c:out value="${out.recipe.cat_name}"/></p>
      							</td>
      						</tr>
      					</table>
      		

</body><!-- body Ende -->
</html><!-- HTML Ende -->
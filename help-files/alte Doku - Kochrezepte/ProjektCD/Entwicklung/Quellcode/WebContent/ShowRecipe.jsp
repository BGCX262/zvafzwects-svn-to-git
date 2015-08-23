<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd"> 
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
				<a></a> <%-- Ausgabe von Informations- und Fehlermeldung --%>
				<jsp:directive.include file="/template/content_info.jsp" />
				
				
				
				
				<!-- Anzeige des Rezeptes -->
						
						
      					<h2><c:out value="${out.recipe.r_name}"/></h2>
      					<c:set var="recp_id_bl" value="${out.recipe.recp_id}" scope="session"/>
      					<c:set var="quantity" value="${out.recipe.persons}" scope="session"/>
      					<c:set var="recp_name_bl" value="${out.recipe.r_name}" scope="session" />
      					<hr />
      					<br />
      					<p><c:out value="${out.recipe.r_description}"/></p>
      					<br/><br /><br />
      					
      					<h3>Zutaten für <c:out value="${out.recipe.persons}"/> Personen</h3>
      					
      									
      					
      					
      				
      					<hr />
      					<br />
      					<p>	<c:forEach items="${out.recipe.ingredients}" var="ingred" varStatus="counter">
      							<c:out value="${ingred.amount}"/>
      							<c:out value="${ingred.unit_name}"/>
      							<c:out value="${ingred.ingr_name}"/>
      							<!-- Variable mit alle Zutaten als String zum Anhängen an den Add-Link -->
      							<c:set var="ingredients_tmp" value="${ingredients_tmp}${ingred.amount}_${ingred.unit_name}_${ingred.ingr_name}_" scope="page"/>
      							<br />
      						</c:forEach>
      						
      						<br />
      					</p>
      					
      					
      					<br /><br/><h3>Zubereitungsschritte</h3>
      					<hr />
      					<br />
      					<p>	<c:forEach items="${out.recipe.prep_steps}" var="prepstep">
      							<c:out value="${prepstep.instruction}"/>
      							<c:set var="prepsteps_tmp" value="${prepsteps_tmp}_${prepstep.instruction}" scope="page"/>
      							<br />
      						</c:forEach>
      						<br />
      					</p>
      					
      		
      					<br /><br/>
      					<h3>Übersicht</h3>
      					<hr />
      					<table class="recipeoverview">
      						<tr>
      							<td>
      								<p class="td_showrecipe"><b>Zubereitungszeit:</b></p>
      							</td>
      							<td class="showrecipe">
      								<p><c:out value="${out.recipe.prep_time}"/>min.</p>
      							</td>
      						</tr>
      						<tr>
      							<td>
      								<p class="td_showrecipe"><b>Schwierigkeitsgrad:</b></p>
      							</td>
      							<td class="showrecipe">
      								<p><c:out value="${out.recipe.difficulty}"/></p>
      							</td>
      						</tr>
      						<tr>
      							<td>
      								<p class="td_showrecipe"><b>Kategorie:</b></p>
      							</td>
      							<td class="showrecipe">
      								<p><c:out value="${out.recipe.cat_name}"/></p>
      							</td>
      						</tr>
      						<tr>
      							<td>
      								<p class="td_showrecipe"><b>hochgeladen am:</b></p>
      							</td>
      							<td class="showrecipe">
      								<p><c:out value="${out.recipe.date_applied}"/></p>
      							</td>
      						</tr>
      						<tr>
      							<td>
      								<p class="td_showrecipe"><b>zuletzt editiert:</b></p>
      							</td>
      							<td class="showrecipe">
      								<p><c:out value="${out.recipe.date_last_edited}"/></p>
      							</td>
      						</tr>
      						<tr>
      							<td>
      								<p class="td_showrecipe"><b>Verfasser:</b></p>
      							</td>
      							<td class="showrecipe">
      								<p><c:out value="${out.recipe.cook_name}"/></p>
      							</td>
      						</tr>
      					</table>
      					
      					<%-- Variable mit allen Rezeptattributen (ohne Zutaten und Bearbeitungsschritte--%>
      					<c:set var="recipe_tmp" value="${out.recipe.recp_id}_${out.recipe.r_name}_${out.recipe.r_description}_${out.recipe.persons}_${out.recipe.prep_time}_${out.recipe.difficulty}_${out.recipe.difficulty_id}_${out.recipe.cat_name}_${out.recipe.cat_id}_${out.recipe.date_applied}_${out.recipe.date_last_edited}_${out.recipe.cook_name}_${out.recipe.cook_id}_${out.recipe.is_admin}_${out.recipe.is_released}" scope="page"/>
      					
      					
      					
      					<div id="recipeoptions">
      						<c:url value="/controller" var="add_buylist_URL">
   								<c:param name="action" value="AddRecipeToBuyList" />
    							<c:param name="buylistentries" value="${ingredients_tmp}" />
							</c:url>
	      					<a href="<c:out value="${add_buylist_URL}"/>"><c:out value="Zutaten zur Einkaufsliste hinzufügen"/></a>
	      					<br />
	      					<br />
	      					<br />
	      					
	      					
	      					<!-- URL Codierung mittels c:url. Sonderzeichen werden Problemlos übernommen -->
	      					<c:url value="/controller" var="printURL">
   								<c:param name="action" value="PrintSite" />
   								<c:param name="param" value="1" />
    							<c:param name="recipeattributes" value="${recipe_tmp}" />
    							<c:param name="ingredients" value="${ingredients_tmp}" />
    							<c:param name="prepsteps" value="${prepsteps_tmp}" />
							</c:url>
							<a href="<c:out value="${printURL}"/>" target="_blank">Druckansicht</a>
							
							<br />
							<br />
							<br />
							<br />
							<p><b>Zutaten umrechen:</b></p>
							<br />
							<form action="controller" method="post">
	      						<fieldset style="border: none;"> 
	      							<input type="hidden" name="recipeattributes" value="${recipe_tmp}" />
	     							<input type="hidden" name="ingredients" value="${ingredients_tmp}" />
	      							<input type="hidden" name="prepsteps" value="${prepsteps_tmp}" />
	      							<input style="float:right margin-left:2px" type="text" value="${out.recipe.persons}" name="persons" size="1" maxlength="2" /> Personen
	      							<br />
	      							<br />
	      							<button name="action" type="submit" value="CalculateAmounts">umrechnen</button>
	      						</fieldset>
	      					</form>
						</div>
				
					
				
			</div> <!-- Content Ende -->
			
			
			
			<%-- Footer --%>
			<jsp:directive.include file="/template/footer.jsp" />
		</div> <!--Box Ende-->
	 </div><!--outer_box Ende-->
</body><!-- body Ende -->
</html><!-- HTML Ende -->
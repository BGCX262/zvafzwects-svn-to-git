<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd"> 
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page import="java.sql.DriverManager" %>
<%@ page import="java.util.LinkedList" %>
<%@ page import="de.lokalhorst.db.dto.*" %>
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
				<br/><br/>
				
				
				
				
				<!-- Ausgabe der Suchergebnisse -->
				<div  id="search_result">
				<h2>Meine Rezepte</h2><br/>
				<c:out value="${InfoMsg}"/>
				<br />				
				<br />
				<table>
					<tr>
							<th class="search_result">Rezeptname</th>
							<th class="search_result">Schwierigkeit</th>
							<th class="search_result">Datum</th>
							<th class="search_result">Kategorie</th>
							<th class="search_result">Status</th>
							<th class="search_result">Optionen</th>
					</tr>
					<c:forEach items="${out}" var="entry">	
      						<c:forEach items="${entry.value}" var="dto">
      							<tr class="search_result">
        							<td class="search_result">
										<a href="/lokalhorst/controller?action=ShowRecipe&amp;param=${dto.recp_id}"><c:out value="${dto.r_name}"/></a>
        							</td>
        							<td class="search_result">
        								<c:out value="${dto.difficulty}"/>
        							</td>
        							<td class="search_result">
        								<c:out value="${dto.date_applied}"/>
        							</td>
        							<td class="search_result">
        								<c:out value="${dto.cat_name}"/>	
        							</td>
        							<td class="search_result">
        								<c:choose>
        									<c:when test="${dto.is_released}">
        										<p>freigegeben</p>	
        									</c:when>
        									<c:otherwise>
        										<p>nicht freigegeben</p>	
        									</c:otherwise>
        								</c:choose>
        							</td>
        							<td>
        								<a href="/lokalhorst/controller?action=EditRecipe&amp;recp_id=${dto.recp_id}">bearbeiten</a>
        							</td>
        						</tr>
    						</c:forEach>
					</c:forEach>
					
				</table>
				</div>
			</div> <!-- Content Ende -->
			
			
			
			<p id="footer">Kochrezepteplattform "Lokalhorst" - Informatikprojekt WS 2010/2011 - FH Kaiserslautern</p> 
		</div> <!--Box Ende-->
	 </div><!--outer_box Ende-->
</body><!-- body Ende -->
</html><!-- HTML Ende -->
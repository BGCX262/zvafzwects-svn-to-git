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
				<h2>gefundene User</h2><br/>
				<c:out value="${InfoMsg}"/>
				<br />				
				<br />
				<table>
					<tr>
							<th class="search_result">Name</th>
							<th class="search_result">ID</th>
							<th class="search_result">Adminstatus</th>
							<th class="search_result">Optionen</th>
					</tr>
					<c:forEach items="${out}" var="entry">	
      						<c:forEach items="${entry.value}" var="dto">
      							<tr class="search_result">
        							<td class="search_result">
										<c:out value="${dto.cook_name}"/>
        							</td>
        							<td class="search_result">
        								<c:out value="${dto.cook_id}"/>
        							</td>
        							<c:choose>
        								<%-- den eigenen Adminstatus und den des Chef Koches darf man nicht ändern --%>
        								<c:when test="${dto.cook_id==sessionScope.cook.cook_id || dto.cook_id==1}">
        									<td class="search_result">
        										<c:choose>
	        										<c:when test="${dto.is_admin}">
	        											<p>Admin</p>
	        										</c:when>
	        										<c:otherwise>
	        											<p>keine Admin</p>
	        										</c:otherwise>
        										</c:choose>
        									</td>
        								</c:when>
        								<c:otherwise>
        									<td class="search_result">
        										<form action="controller" method="post">
        											<fieldset class="search_result">
     												<input type="hidden" value="${dto.is_admin}" name="adminstatus_old"/>
        											
	        										<select name="adminstatus">
	        											<c:choose>
											     			 <c:when test="${dto.is_admin}">
											      				<option value="true">Admin</option>
											  					<option value="false">kein Admin</option>
											      			</c:when>
							
											      			<c:otherwise>
											       				<option value="false">kein Admin</option>
																<option value="true">Admin</option>
											     		 	</c:otherwise>
											    		</c:choose>	
											    		
													</select>
													<input type="hidden" value="${dto.cook_id}" name="cook_id"/>
													<input type="hidden" value="${dto.cook_name}" name="cook_name"/>
													<button name="action" type="submit" value="EditCook">speichern</button>
													</fieldset>
												</form>
        									</td>
        								</c:otherwise>
        							</c:choose>  
        							
        							
        							<c:choose>
	        								<%-- den eigenen Account und den des Chefkochs darf man nicht löschen --%>
	        								<c:when test="${dto.cook_id==sessionScope.cook.cook_id || dto.cook_id==1}">
	        									<td class="search_result">
	        										<p>nicht verfügbar</p>
	        									</td>
	        								</c:when>
	        								<c:otherwise>
	        									<td class="search_result">
	        										<form action="controller" method="post">
	        											<fieldset class="search_result">
															<input type="hidden" value="${dto.cook_id}" name="cook_id"/>
															<input type="hidden" value="${dto.cook_name}" name="cook_name"/>
															<button name="action" type="submit" value="DeleteCook">löschen</button>
														</fieldset>
													</form>
	        									</td>
	        								</c:otherwise>
	        							</c:choose>     							
        						</tr>
    						</c:forEach>
					</c:forEach>
					
				</table>
				</div>
			</div> <!-- Content Ende -->
			
			
			
			<%-- Footer --%>
			<jsp:directive.include file="/template/footer.jsp" />
		</div> <!--Box Ende-->
	 </div><!--outer_box Ende-->
</body><!-- body Ende -->
</html><!-- HTML Ende -->
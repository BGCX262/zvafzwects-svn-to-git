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
				
				<h2>Rezept bearbeiten</h2>
				<br />
				<br />
				<h3>Name: "<c:out value="${ sessionScope.recipe.r_name }" default="" />"</h3>
				<br />
				
				<form class="editrecipe" action="controller" method="post" id="form-editRecipe">
				
					<fieldset class="editrecipe">
						<legend>Speichern &amp; Veröffentlichen</legend>
						<button name="edit" value="6">Speichern</button>
						<br />
						<br />
						<label for="form-edit-release" >veröffentlichen:</label>
						<input type="checkbox" name="release" value="true" <c:if test="${ sessionScope.release }"> checked="checked" </c:if> id="form-edit-release" />
					</fieldset>
					
					<p><br /><br /></p>
					
					<%-- Rezeptbeschreibung --%>
					<fieldset class="editrecipe">
						<legend>Beschreibung</legend>
						<label for="form-edit-desc">Beschreibung:</label>
						<textarea name="desc" cols="40" rows="3" id="form-edit-desc"><c:out value="${ sessionScope.recipe.r_description }" default="" /></textarea>
						<br />
						<p class="maxsymbols">max. 300 Zeichen</p>
					</fieldset>
						
					<p><br /><br /></p>
					
					<%-- Dauer / Schwierigkeit / Kategorie --%>
					<fieldset class="editrecipe">
						<legend>Dauer, Schwierigkeit &amp; Kategorie</legend>
						<label for="form-edit-prep_time" >Dauer in min.:</label>
						<input type="text" name="prep_time" value='<c:out value="${ sessionScope.recipe.prep_time }" default="" />' size="4" maxlength="4" id="form-edit-prep_time" />
						<br />
						<br />
						<label for="form-edit-difficulty" >Schwierigkeit:</label>
						<select name="difficulty" id="form-edit-difficulty">
							<c:forEach items="${ sessionScope.difficulties }" var="diff">
								<option	<c:if test='${ diff.diff_level == sessionScope.recipe.difficulty_id }'>selected="selected"</c:if> value="${ diff.diff_level }"><c:out value="${ diff.description }" default="" /></option>
							</c:forEach>
						</select>
						<br />
						<br />
						<label for="form-edit-cat" >Kategorie:</label>
						<select name="category" id="form-edit-cat">
							<c:forEach items="${ sessionScope.categories }" var="cat">
								<option <c:if test='${ cat.cat_id == sessionScope.recipe.cat_id }'>selected="selected"</c:if> value="${ cat.cat_id }"><c:out value="${ cat.cat_name }" default="" /></option>
							</c:forEach>
						</select>
					</fieldset>	
					
					<p><br /><br /></p>	
						
					<%-- Zutaten --%>
					<fieldset class="editrecipe">
						<legend>Zutatenverwaltung</legend>
						<p><b>Zutaten</b></p>
						<p>Folgende Zutaten werden bereits verwendet. Sie können unten weitere hinzufügen!</p>
						<br />
						<ol>
							<c:forEach items="${ sessionScope.recipe.ingredients }" var="ingr">
							<li>
								<label class="ingr_label" for="ingr_id_${ ingr.ingr_id }"></label>
								<input type="radio" name="ingr_id" id="ingr_id_${ ingr.ingr_id }" value="<c:out value='${ ingr.ingr_id }' />" /> <c:out value="${ ingr.amount }" /> <c:out value="${ ingr.unit_name }" /> <c:out value="${ ingr.ingr_name }" />
								
							</li>
							</c:forEach>
						</ol>
						<br />
						<button name="edit" value="3">entfernen</button>
						<br />
						<hr />
						<p><b>Zutat hinzufügen</b></p>
						<p>Bitte geben sie die Menge als Ganzzahl an, bezogen auf 4 Personen.</p>
						<br />
						<label for="form-edit-addAmount" >Menge:</label>
						<input type="text" name="addAmount" value="" size="4" maxlength="4" id="form-edit-addAmount"/>
						<br />
						<br />
						<label for="form-edit-addIngr" >Zutat:</label>
						<select name="addIngr" id="form-edit-addIngr">
							<c:forEach items="${ sessionScope.ingredients }" var="addIngr">
								<option value="${ addIngr.ingr_id }"><c:out value="${ addIngr.ingr_name }" /> [<c:out value="${ addIngr.unit_name }" />]</option>
							</c:forEach>
						</select>
						<button name="edit" value="4">hinzufügen</button>
						<br />
						<hr />
						
						<%-- neue Zutat anlegen --%>
						<p><b>neue Zutat anlegen</b></p>
						<p>Um einen einen neue Zutat anzulegen geben Sie einen Namen an und wählen eine Einheit.</p>
						<br />
						<label for="form-edit-newIngr" >Name:</label>
						<input type="text" name="newIngr" value='<c:out value="${ newIngr }" default="" />' size="20" maxlength="30" id="form-edit-newIngr"/>
						<br />
						<br />
						<label for="form-edit-newUnit" >Einheit:</label>
						<select name="newUnit" id="form-edit-newUnit">
							<c:forEach items="${ sessionScope.units }" var="unit">
								<option value="${ unit.unit_id }">${ unit.u_name }</option>
							</c:forEach>
						</select>																	
						<button name="edit" value="5">anlegen</button>	
					</fieldset>
					
					<p><br /><br /></p>
					
					<%-- Zubereitungsschritte --%>
					<fieldset class="prepstep">
						<legend class="prep">Zubereitung</legend>	
						<p>Mit 'neu' legen Sie einen Zubereitungsschritt an, der bis zu 300 Zeichen lang sein kann. Wenn Sie 'löschen' wählen, wird der letzte Schritt entfernt</p>
						<br />					
						<fieldset class="prepstep_inner">
							<c:forEach items="${ sessionScope.recipe.prep_steps }" var="step">
								<label for="prepstep_${ step.prep_step }">Schritt <c:out value="${ step.prep_step }" default="" />:</label>
								<textarea name="prepstep_${ step.prep_step }" id="prepstep_${ step.prep_step }" cols="40" rows="3" ><c:out value="${ step.instruction }" default="" /></textarea>
								<p class="maxsymbols">max. 300 Zeichen</p>
								<br />
								<hr />
								<br />
							</c:forEach>
						</fieldset>
						<%-- neuen Schritte hinzufügen --%>
						<input type="hidden" name="action" value="EditRecipe" />
						<input type="hidden" name="recp_id" value="${ sessionScope.recipe.recp_id }" />
						<button name="edit" value="2">löschen</button>
						<button name="edit" value="1">neu</button>
						<br />
						<br />
					</fieldset>
				</form>	
			</div> <!-- Content Ende -->
			
			<%-- Footer --%>
			<jsp:directive.include file="/template/footer.jsp" />

		</div> <!--Box Ende-->
	 </div><!--outer_box Ende-->
</body><!-- body Ende -->
</html><!-- HTML Ende -->
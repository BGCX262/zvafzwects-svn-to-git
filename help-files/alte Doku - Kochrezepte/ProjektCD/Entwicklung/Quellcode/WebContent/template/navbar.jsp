<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<!-- Navbar Anfang -->
<div id="navigation">
<ul>
	<%-- Menüeintrage für alle Benutzer --%>
	<li class="navigation"><a href="/lokalhorst/controller">Home</a></li>
	<li class="navigation"><a href="/lokalhorst/controller?action=ShowBuyList">Einkaufsliste</a></li>
	<%-- Menüeintrage für registrierte Benutzer --%>
	<c:if test="${not empty sessionScope.cook }">
		<li class="navigation"><a href="/lokalhorst/controller?action=SearchOwnRecipe">Meine Rezepte</a></li>
		<li class="navigation"><a href="/lokalhorst/controller?action=Redirect&amp;targetview=CreateRecipe">Rezept anlegen</a></li>
		<%-- Menüeinträge für Admins --%>
		<c:if test="${ sessionScope.cook.is_admin }">
			<li class="navigation"><a href="/lokalhorst/controller?action=Redirect&amp;targetview=AdminFunctions">Administration</a></li>
		</c:if>
	</c:if>
</ul>
<!-- Navbar Ende --></div>
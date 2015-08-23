<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<%-- Anzeige der Meldungen falls vorhanden  --%>

<!-- Hinweise -->
<c:if test='${ (not empty InfoMsg) and (InfoMsg != "" ) }'>
	<div id="InfoMsg">
		<h3>Hinweis:</h3>
	<p><c:out value="${InfoMsg}" /></p>
	</div>
</c:if>

<!--  Fehler -->
<c:if test='${ (not empty ErrorMsg) and (ErrorMsg != "" ) }'>
	<div id="ErrorMsg">
		<h3 class="error">Fehler:</h3>
	<p class="error"><c:out value="${ErrorMsg}" /></p>
	</div>
</c:if>
	
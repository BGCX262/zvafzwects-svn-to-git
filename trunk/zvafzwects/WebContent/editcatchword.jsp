<%@ include file="inc/before.jsp"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%-- Author: Andreas Baur --%>

<c:choose>
	<c:when test="${empty catchword}">	
		<h1>Neues Schlagwort anlegen</h1>
	</c:when>
	<c:otherwise>
		<h1>Schlagwort umbenennen</h1>
	</c:otherwise>
</c:choose>

<div id="create_catchword">
	<form action="<c:out value='${formAction}' />" method="post">
		<div class="form_input">
			<label for="catchword_name">Schlagwort: </label> <input
				id="catchword_name" name="catchword_name" type="text" value="<c:out value='${catchword.catchwordName}' />" />
		</div>
		<div class="form_submit">
			<c:choose>
			<c:when test="${empty catchword}">	
				<input class="form_submit_button" type="submit" value="Schlagwort anlegen" />
			</c:when>
			<c:otherwise>
				<input class="form_submit_button" type="submit" value="Schlagwort umbenennen" />
			</c:otherwise>
		</c:choose>
		</div>
	</form>
	
	<h2>Existierende Schlagworte:</h2>
	<div class="catchwords">
		<c:forEach var="catchword" items="${catchwords}">
			<div class="album_catchword">
				<a class="album_catchword" href="user?action=search&amp;album_catchwords[]=${catchword.catchwordId}"><c:out value="${catchword.catchwordName}" /></a>
				<a class="album_catchword_edit" href="admin?action=display_edit_catchword&amp;display_catchword_id=${catchword.catchwordId}">umbenennen</a>
				<a class="album_catchword_delete" href="admin?action=delete_catchword&amp;catchword_id=${catchword.catchwordId}">löschen</a>
			</div>
		</c:forEach>
	</div>
</div>

<%@ include file="inc/after.jsp"%>
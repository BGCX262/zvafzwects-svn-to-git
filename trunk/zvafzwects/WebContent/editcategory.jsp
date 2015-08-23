<%@ include file="inc/before.jsp"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%-- Author: Andreas Baur --%>

<c:choose>
	<c:when test="${empty category}">	
		<h1>Neue Kategorie anlegen</h1>
	</c:when>
	<c:otherwise>
		<h1>Kategorie umbenennen</h1>
	</c:otherwise>
</c:choose>

<form id="create_category" action="<c:out value='${formAction}' />" method="post">
	<div class="form_input">
		<label for="category_name">Kategorie: </label> <input
			id="category_name" name="category_name" type="text" value="<c:out value='${category.categoryName}' />" />
	</div>
	<div class="form_submit">
		<c:choose>
			<c:when test="${empty category}">	
				<input class="form_submit_button" type="submit" value="Kategorie anlegen" />
			</c:when>
			<c:otherwise>
				<input class="form_submit_button" type="submit" value="Kategorie umbenennen" />
			</c:otherwise>
		</c:choose>
	</div>
</form>

<h2>Existierende Kategorien:</h2>
<div class="categories">
	<c:forEach var="category" items="${categories}">
		<div class="album_category">
			<a class="album_category" href="user?action=search&amp;album_category=${category.categoryId}"><c:out value="${category.categoryName}" /></a>
			<a class="album_category_edit" href="admin?action=display_edit_category&amp;display_category_id=${category.categoryId}">umbenennen</a>
			<a class="album_category_delete" href="admin?action=delete_category&amp;category_id=${category.categoryId}">löschen</a>
		</div>
	</c:forEach>
</div>

<%@ include file="inc/after.jsp"%>
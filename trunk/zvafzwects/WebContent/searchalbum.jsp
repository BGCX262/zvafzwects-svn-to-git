<%@ include file="inc/before.jsp" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%-- Author: Markus Henn --%>

<h1>Suche</h1>

<form id="searchform" action="user" method="get">
	<input name="action" value="search" type="hidden" />
	<div class="form_input">
		<label for="album_title">Titel: </label>
		<input id="album_title" name="album_title" type="text" value="" />
	</div>
	<div class="form_input">
		<label for="album_interpreter">Interpret: </label>
		<input id="album_interpreter" name="album_interpreter" type="text" value="" />
	</div>
	<div class="form_input">
		<label for="album_category">Kategorie: </label>
		<select id="album_category" name="album_category">
			<option value="">---</option>
			<c:forEach items="${categories}" var="category">
				<option value="<c:out value='${category.categoryId}' />"><c:out value="${category.categoryName}" /></option>
			</c:forEach>
		</select>
	</div>
	<div class="form_input">
		<label for="album_catchword_1">Schlagworte: </label>
		<select id="album_catchword_1" name="album_catchwords[]">
			<option value="">---</option>
			<c:forEach items="${catchwords}" var="catchword">
				<option value="<c:out value='${catchword.catchwordId}' />"><c:out value="${catchword.catchwordName}" /></option>
			</c:forEach>
		</select>
		<select id="album_catchword_2" name="album_catchwords[]">
			<option value="">---</option>
			<c:forEach items="${catchwords}" var="catchword">
				<option value="<c:out value='${catchword.catchwordId}' />"><c:out value="${catchword.catchwordName}" /></option>
			</c:forEach>
		</select>
		<select id="album_catchword_3" name="album_catchwords[]">
			<option value="">---</option>
			<c:forEach items="${catchwords}" var="catchword">
				<option value="<c:out value='${catchword.catchwordId}' />"><c:out value="${catchword.catchwordName}" /></option>
			</c:forEach>
		</select>
		<select id="album_catchword_4" name="album_catchwords[]">
			<option value="">---</option>
			<c:forEach items="${catchwords}" var="catchword">
				<option value="<c:out value='${catchword.catchwordId}' />"><c:out value="${catchword.catchwordName}" /></option>
			</c:forEach>
		</select>
	</div>
	<div class="form_submit">
		<input class="form_submit_button" type="submit" value="Suche" />
	</div>
</form>

<%@ include file="inc/after.jsp" %>
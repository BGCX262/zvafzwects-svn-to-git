<%@ include file="inc/before.jsp" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://zvafzwects/myfn" prefix="myfn" %>
<%-- Author: Markus Henn, Jochen Pätzold --%>

<c:choose>
	<c:when test="${album.albumId > -1}">
		<h1>Album editieren</h1>
	</c:when>
	<c:otherwise>
		<h1>Neues Album hinzufügen</h1>
	</c:otherwise>
</c:choose>

<form id="editalbum" action="<c:out value='${formAction}' />" method="post" enctype="multipart/form-data">
	<div class="form_input title">
		<label for="album_title">Titel: </label>
		<input id="album_title" name="album_title" type="text" value="<c:out value='${album.title}' />" />
	</div>
	<div class="form_input interpreter">
		<label for="album_interpreter">Interpret: </label>
		<input id="album_interpreter" name="album_interpreter" type="text" value="<c:out value='${album.interpreter}' />" />
	</div>
	<div class="form_input category">
		<label for="album_category">Kategorie: </label>
		<select id="album_category" name="album_category">
			<option value="">Keine Kategorie</option>
			<c:forEach var="category" items="${categories}">
				<option value="${category.categoryId}"<c:if test='${(album.category != null) && (album.category.categoryId == category.categoryId)}'> selected="selected"</c:if>>
					<c:out value="${category.categoryName}" />
				</option>
			</c:forEach>
		</select> 
	</div>
	<div class="form_input catchwords">
		<label for="album_catchwords">Schlagworte: </label>
		<select id="album_catchwords" name="album_catchwords[]" multiple="multiple">
			<c:forEach var="catchword" items="${catchwords}">
				<option value="${catchword.catchwordId}"<c:if test='${(album.catchwords != null) && myfn:contains(album.catchwords, catchword)}'> selected="selected"</c:if>><c:out value="${catchword.catchwordName}" /></option>
			</c:forEach>
		</select> 
	</div>
	<div class="form_input releaseyear">
		<label for="album_releaseyear">Erscheinungsjahr: </label>
		<input id="album_releaseyear" name="album_releaseyear" type="text" value="<c:out value='${album.releaseYear}' />" />
	</div>
	<div class="form_input label">
		<label for="album_label">Label: </label>
		<input id="album_label" name="album_label" type="text" value="<c:out value='${album.label}' />" />
	</div>
	<c:if test="${album.cover != null}">
		<div class="album_cover_area">
			<img class="album_cover" src="cover?album_id=${album.albumId}" alt="album cover" />
		</div>
		<div class="form_input cover_delete">
			<label for="album_cover_delete">Cover entfernen: </label>
			<input id="album_cover_delete" name="album_cover_delete" type="checkbox" value="" />
		</div>
	</c:if>
	<div class="form_input cover">
			<label for="album_cover">Cover <c:if test="${album.cover != null}">ersetzen</c:if>: </label>
		<input id="album_cover" name="album_cover" type="file" value="" accept="image/*" />
	</div>
	<div class="form_input price">
		<label for="album_price">Preis: </label>
		<input id="album_price" name="album_price" type="text" value="<c:out value='${album.price}' />" /> €
	</div>
	<div class="form_input stock">
		<label for="album_stock">Stückzahl auf Lager: </label>
		<input id="album_stock" name="album_stock" type="text" value="<c:out value='${album.stock}' />" />
	</div>
	<c:if test="${!(album.albumId > -1)}">
		<div id="editalbum_cds">
			<noscript>
				Zum Hinzufügen von CDs (Tracks) wird JavaScript vorausgesetzt! JavaScript scheint in Ihrem Browser derzeit deaktiviert zu sein bzw. nicht unterstützt zu werden!
			</noscript>
		</div>
		<div>
			<button onclick="addNewCdInput(document.getElementById('editalbum_cds')); return false;">Weitere CD hinzufügen…</button>
		</div>
	</c:if>
	<div class="form_submit">
		<c:choose>
			<c:when test="${album.albumId > -1}">
				<input class="form_submit_button" type="submit" value="Editierte Albumdaten übernehmen" />
			</c:when>
			<c:otherwise>
				<input class="form_submit_button" type="submit" value="Album anlegen" />
			</c:otherwise>
		</c:choose>
	</div>
</form>

<%@ include file="inc/after.jsp" %>
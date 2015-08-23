<%@ include file="inc/before.jsp" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%-- Author: Markus Henn --%>

<h1>Suchergebnisse</h1>

<div id="searchresults">
	gefundene Ergebnisse: <c:out value="${fn:length(alba)}" />

	<c:forEach var="album" items="${alba}">
		<div class="album">
			<h2 class="album_title"><a href="user?action=display_album&album_id=${album.albumId}"><c:out value="${album.title}" /></a></h2>
			<div class="album_interpreter">von <a href="user?action=search&album_interpreter=<c:out value='${album.interpreter}' />"><c:out value="${album.interpreter}" /></a></div>
			<div class="album_cover_area">
				<a href="user?action=display_album&album_id=${album.albumId}">
					<c:choose>
						<c:when test="${album.cover != null}">
							<img class="album_cover" src="cover?album_id=${album.albumId}" alt="album cover" />
						</c:when>
						<c:otherwise>
							<img class="album_cover" src="https://upload.wikimedia.org/wikipedia/commons/d/d7/No_Cover_.jpg" alt="album cover" />
						</c:otherwise>
					</c:choose>
				</a>
			</div>
			<div class="album_label">erschienen bei: <c:out value="${album.label}" /></div>
			<div class="album_releaseyear">Erscheinungsjahr: <c:out value="${album.releaseYear}" /></div>
			<div class="album_stock">noch <c:out value="${album.stock}" /> Stück auf Lager</div>
			<div class="album_numberofcds"><c:out value="${album.numberOfCds}" /> CD(s)</div>
			<div class="album_price"><c:out value="${album.price}" /> €</div>
			
			<c:if test="${not empty album.category}">
				Category:
				<div class="album_category">
					<a href="user?action=search&album_category=${album.category.categoryId}"><c:out value="${album.category.categoryName}" /></a>
				</div>
			</c:if>
			<c:if test="${not empty album.catchwords}">
				Catchwords:
				<div class="album_catchwords">
					<c:forEach var="catchword" items="${album.catchwords}">
						<a class="album_catchword" href="user?action=search&album_catchwords[]=${catchword.catchwordId}"><c:out value="${catchword.catchwordName}" /></a>
					</c:forEach>	
				</div>
			</c:if>
			
			<c:if test="${user.isAdmin}">
				<div class="admin_functions_area">
					<a href="admin?action=display_edit_album&album_id=${album.albumId}">Album editieren</a>
				</div>
			</c:if>
		</div>
	</c:forEach>
</div>

<%@ include file="inc/after.jsp" %>
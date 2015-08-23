<%@ include file="inc/before.jsp" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%-- Author: Markus Henn --%>

<h1 class="album_title"><c:out value="${album.title}" /></h1>
<%--<div class="album_interpreter">von: <a href="user?action=search&amp;album_interpreter=<%= java.net.URLEncoder.encode(((DBAlbum)request.getAttribute("album")).getInterpreter(), "utf-8") %>"><c:out value="${album.interpreter}" /></a></div>--%>
<div class="album_interpreter">von: <a href="user?action=search&amp;album_interpreter=<c:out value="${album.interpreter}" />"><c:out value="${album.interpreter}" /></a></div>

<div class="album_cover_area">
	<c:choose>
		<c:when test="${not empty album.cover}">
			<img class="album_cover" src="cover?album_id=${album.albumId}" alt="album cover" />
		</c:when>
		<c:otherwise>
			<img class="album_cover" src="https://upload.wikimedia.org/wikipedia/commons/d/d7/No_Cover_.jpg" alt="album cover" />
		</c:otherwise>
	</c:choose>
</div>

<div class="album_label">erschienen bei: <c:out value="${album.label}" /></div>
<div class="album_releaseyear">Erscheinungsjahr: <c:out value="${album.releaseYear}" /></div>
<div class="album_stock">noch <c:out value="${album.stock}" /> Stück auf Lager</div>
<div class="album_numberofcds"><c:out value="${album.numberOfCds}" /> CD(s)</div>
<div class="album_price"><c:out value="${album.priceAsString}" /> €</div>

<c:if test="${not empty album.category}">
	Kategorie:
	<div class="album_category">
		<a href="user?action=search&amp;album_category=${album.category.categoryId}"><c:out value="${album.category.categoryName}" /></a>
	</div>
</c:if>

<c:if test="${not empty album.catchwords}">
	Schlagwörter:
	<div class="album_catchwords">
		<c:forEach var="catchword" items="${album.catchwords}">
			<a class="album_catchword" href="user?action=search&amp;album_catchwords[]=${catchword.catchwordId}"><c:out value="${catchword.catchwordName}" /></a>
		</c:forEach>	
	</div>
</c:if>

<c:forEach var="cd" items="${album.cds}">
	<div class="cd">
		CD <c:out value="${cd.cdNumber}" />
		<c:forEach var="track" items="${cd.tracks}">
			<div class="track">
				<div class="track_number"><c:out value="${track.trackNumber}" />.</div>
				<div class="track_preview">
					<%--
					<form action="mp3stream" method="get">
						<input name="album_id" type="hidden" value="${album.albumId}" />
						<input name="cd_number" type="hidden" value="${cd.cdNumber}" />
						<input name="track_number" type="hidden" value="${track.trackNumber}" />
						<input type="submit" value="anhören" />
					</form>
					--%>
					<%--<a href="mp3stream?album_id=${album.albumId}&cd_number=${cd.cdNumber}&track_number=${track.trackNumber}">anhören</a>--%>
					<%--<embed src="audioplay/audioplay.swf?repeat=1&amp;buttondir=audioplay/buttons/classic_small&amp;file=MP3Stream%3Falbum_id%3D${album.albumId}%26cd_number%3D${cd.cdNumber}%26track_number%3D${track.trackNumber}" type="application/x-shockwave-flash"></embed>--%>
					<object type="application/x-shockwave-flash" data="audioplay/audioplay.swf?repeat=1&amp;buttondir=audioplay/buttons/classic_small&amp;fadeindur=1000&amp;file=mp3stream%3Falbum_id%3D${album.albumId}%26cd_number%3D${cd.cdNumber}%26track_number%3D${track.trackNumber}">
					</object>
				</div>
				<div class="track_title"><c:out value="${track.title}" /></div>
			</div>
		</c:forEach>
	</div>
</c:forEach>

<div>
	<form action="user?action=order_item&album_id=${album.albumId}" method="post">
		<input id="order_item_amount" name="order_item_amount" type="number" min="1" max="${album.stock}" value="1" />
		<label for="order_item_amount">Stück</label>
		<input type="submit" value="in den Warenkorb legen!" />
	</form>
</div>

<c:if test="${user.isAdmin}">
	<div class="admin_functions_area">
		<a href="admin?action=display_edit_album&amp;album_id=${album.albumId}">Album editieren</a>
	</div>
</c:if>

<%@ include file="inc/after.jsp" %>
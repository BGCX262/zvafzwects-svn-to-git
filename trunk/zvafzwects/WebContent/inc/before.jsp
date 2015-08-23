<?xml version="1.0" encoding="UTF-8" ?>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<%-- Author: Markus Henn --%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
	<c:choose>
		<c:when test="${not empty redirect}">
			<meta http-equiv="refresh" content="5; URL=<c:out value='${redirect}' />" />
		</c:when>
	</c:choose>
	<link rel="stylesheet" type="text/css" href="inc/style.css" />
	<script type="text/javascript" src="inc/editalbum.js"></script>
	<script type="text/javascript" src="inc/order.js"></script>
	<title>ZVAFZWECTS</title>
</head>
<body id="top">
	<table id="frame">
		<tr id="header">
			<td id="logo"><a href="index.jsp">Home</a></td>
			<td id="topic">ZVAFZWECTS</td>
			<td id="user_status">
				<c:choose>
					<c:when test="${empty user}">
						Sie sind nicht angemeldet. Jetzt <a href="user?action=login">anmelden</a>
						oder <a href="user?action=display_register_user">registrieren</a>!
					</c:when>
					<c:otherwise>
					Angemeldet als <c:out value="${user.email}" default="" />
						<c:if test="${user.isAdmin}">
							<br />Admin
						</c:if>
						<br /><a href="user?action=logout">Logout</a>
						<br /><a href="user?action=display_edit_user">Benutzerdaten editieren</a>
					</c:otherwise>
				</c:choose>
			</td>
		</tr>
		<tr>
			<td id="left">
				<div id="menu">
					<a href="user?action=search">Alle anzeigen</a><br />
					<a href="user?action=display_search">Album suchen</a><br />
					<c:if test="${user.isAdmin}">
						<a href="admin?action=display_new_album">Album hinzufügen</a><br />
						<a href="admin?action=display_create_category">Kategorien verwalten</a><br />
						<a href="admin?action=display_create_catchword">Schlagwörter verwalten</a><br />
					</c:if>
					<div class="top_link">
						<a href="#top">Zurück nach oben</a>
					</div>
				</div>
			</td>
			<td id="main">
<%@ include file="content_info.jsp" %>
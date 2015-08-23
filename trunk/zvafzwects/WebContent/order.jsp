<%@ include file="inc/before.jsp"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%-- Author: Jochen Pätzold, Markus Henn --%>

<h1>Bestellung</h1>

<form id="sendorder" action="user?action=submit_order" method="post">
	<div class="order">
		<table class="order_items">
			<c:forEach var="orderItem" items="${shoppingCart.orderItems}" varStatus="status">
				<tr class="order_item" id="order_item_${status.index}">
					<td class="amount">
						<input type="number" min="1" max="${orderItem.album.stock}" name="order_item_${status.index}_amount" value="<c:out value="${orderItem.amount}" />" onchange="updateOrderItem(${status.index})" />x
					</td>
					<td class="title">
						<a href="user?action=display_album&amp;album_id=${orderItem.album.albumId}"><c:out value="${orderItem.album.title}" /></a>
						<c:if test="${orderItem.amount > 1}">
							(á <c:out value="${orderItem.price}" /> €)
						</c:if>
					</td>
					<td class="price" id="order_item_${status.index}_total_price"><c:out value="${orderItem.totalPrice}" /> €</td>
					<!--
					<td><input type="button" value="Entfernen" onclick="removeOrderItem(${status.index})" /></td>
					-->
				</tr>
			</c:forEach>
			<tr class="total">
				<td colspan="2"> </td>
				<td class="price" id="order_total_price"><c:out value="${shoppingCart.total}"/> €</td>
			</tr>
		</table>
		<div class="form_submit">
			<input type="hidden" name="confirmation_needed" id="edit_order_hiddeninput" />
			<input class="form_submit_button" type="submit" name="edit_order" id="edit_order_button" value="Aktualisieren" />
		</div> 
	
		<fieldset class="addresses" id="invoice_addresses">
			<legend>Rechnungsaddresse wählen:</legend>
			<c:forEach var="address" items="${user.addresses}">
				<div>
					<input type="radio" id="invoice_address_${address.addressId}" name="invoiceAddress"
						value="${address.addressId}" checked="checked" />
					<label for="invoice_address_${address.addressId}">
						<c:out value="${address.street}" /> <c:out value="${address.houseNumber}" />,
						<c:out value="${address.zipcode}" /> <c:out value="${address.city}" />
					</label>
				</div>
			</c:forEach>
		</fieldset>

		<fieldset class="addresses" id="deliver_addresses">
			<legend>Lieferadresse wählen:</legend>
			<c:forEach var="address" items="${user.addresses}">
				<div>
					<input type="radio" id="deliver_address_${address.addressId}" name="deliverAddress"
						value="${address.addressId}" checked="checked" />
					<label for="deliver_address_${address.addressId}">
						<c:out value="${address.street}" /> <c:out value="${address.houseNumber}" />,
						<c:out value="${address.zipcode}" /> <c:out value="${address.city}" />
					</label>
				</div>
			</c:forEach>
		</fieldset>
	</div>
	
	<div class="form_submit">
		<input class="form_submit_button" type="submit" name="complete_order" value="Bestellen" />
	</div>
</form>
	<%@ include file="inc/after.jsp"%>
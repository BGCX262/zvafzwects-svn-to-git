<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%-- Author: Markus Henn --%>
			</td>
			<td>
				<div id="shopping_cart">
					<div class="topic">Warenkorb</div>
					<%--<c:out value="${fn:length(shoppingCart.orderItems)}" default="0" /> verschiedene Produkte(e)--%>
					<c:choose>
						<c:when test="${empty shoppingCart.orderItems}">
							<div class="order_item_count">leer</div>
						</c:when>
						<c:otherwise>
							<table class="order_items">
								<c:forEach var="orderItem" items="${shoppingCart.orderItems}">
									<tr class="order_item">
										<td class="amount"><c:out value="${orderItem.amount}" />x</td>
										<td class="title">
											<a href="user?action=display_album&amp;album_id=${orderItem.album.albumId}"><c:out value="${orderItem.album.title}" /></a>
										</td>
										<td class="price"><c:out value="${orderItem.totalPrice}" /> €</td>
									</tr>
								</c:forEach>
								<tr class="total">
									<td colspan="2"> </td>
									<td class="price"><c:out value="${shoppingCart.total}"/> €</td>
								</tr>
							</table>
							<a href="user?action=order_shoppingcart">Bestellung abschließen</a>
						</c:otherwise>
					</c:choose>
				</div>
			</td>
		</tr>
	</table>
</body>
</html>
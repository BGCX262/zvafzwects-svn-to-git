/* Author: Markus Henn */

addLoadEvent(function() {
	var orderElement = document.getElementById('sendorder');
	if (orderElement == null) {
		return;
	}
	// initialize javascript needed functions/elements/attributes
});

function removeOrderItem(id) {
	var orderItemElement = document.getElementById('order_item_' + id);
	if (orderItemElement == null) {
		return;
	}
	// TODO: ändern :-)
	orderItemElement.style.visibility = 'hidden';
	
	var orderTotalPriceElement = document.getElementById('order_total_price');
	if (orderTotalPriceElement != null) {
		orderTotalPriceElement.firstChild.data = '(N/A)';
	}
}

function orderItemsIsEmpty() {
	// TODO
	return true;
}

function updateOrderItem(id) {
	var orderItemElement = document.getElementById('order_item_' + id);
	if (orderItemElement == null) {
		return;
	}
	// TODO: ändern :-)
	var orderItemTotalPriceElement = document.getElementById('order_item_' + id + '_total_price');
	if (orderItemTotalPriceElement != null) {
		orderItemTotalPriceElement.firstChild.data = '(N/A)';
	}
	var orderTotalPriceElement = document.getElementById('order_total_price');
	if (orderTotalPriceElement != null) {
		orderTotalPriceElement.firstChild.data = '(N/A)';
	}
	// unhide edit order button
	/*
	var editOrderButtonElement = document.getElementById('edit_order_button');
	if (editOrderButtonElement != null) {
		editOrderButtonElement.style.visibility = 'visible';
	}
	*/
	// set a hidden element, so we know, that something changed when submitting
	document.getElementById('edit_order_hiddeninput').setAttribute('value', 'true');
}


/**
 * http://simonwillison.net/2004/May/26/addLoadEvent/
 */
function addLoadEvent(func) {
	var oldonload = window.onload;
	if (typeof window.onload != 'function') {
		window.onload = func;
	} else {
		window.onload = function() {
			if (oldonload) {
				oldonload();
			}
			func();
		};
	}
}
package de.fhkl.dbsm.zvafzwects.model.db;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Andreas Baur, Jochen Pätzold, Markus Henn
 * @version 3.1
 */
public class DBOrder {
	private int orderId;
	private DBUser user;
	private final List<DBOrderItem> orderItems;
	private DBAddress invoiceAddress;
	private DBAddress deliverAddress;
	private String date;

	public DBOrder() {
		super();
		this.user = null;
		this.orderItems = new ArrayList<DBOrderItem>();
		this.invoiceAddress = null;
		this.deliverAddress = null;
		this.date = null;
	}

	public int getOrderId() {
		return orderId;
	}

	public void setOrderId(int orderId) {
		this.orderId = orderId;
	}

	public DBUser getUser() {
		return user;
	}

	public void setUser(DBUser user) {
		this.user = user;
	}

	public List<DBOrderItem> getOrderItems() {
		return orderItems;
	}

	protected void addOrderItem(DBOrderItem orderItem) {
		orderItems.add(orderItem);
	}

	public boolean contains(DBOrderItem orderItem) {
		return orderItems.contains(orderItem);
	}

	public BigDecimal getTotal() {
		BigDecimal total = new BigDecimal(0.00);
		for (DBOrderItem orderItem : orderItems) {
			total = total.add(orderItem.getTotalPrice());
		}
		total = total.setScale(2, RoundingMode.HALF_UP);
		return total;
	}

	public DBAddress getInvoiceAddress() {
		return invoiceAddress;
	}

	public void setInvoiceAddress(DBAddress invoiceAddress) {
		this.invoiceAddress = invoiceAddress;
	}

	public DBAddress getDeliverAddress() {
		return deliverAddress;
	}

	public void setDeliverAddress(DBAddress deliverAddress) {
		this.deliverAddress = deliverAddress;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

}

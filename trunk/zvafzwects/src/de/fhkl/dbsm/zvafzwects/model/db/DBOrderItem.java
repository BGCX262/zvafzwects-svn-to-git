package de.fhkl.dbsm.zvafzwects.model.db;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * @author Andreas Baur, Jochen Pätzold, Markus Henn
 * @version 3
 */
public class DBOrderItem {
	private DBOrder order;
	private DBAlbum album;
	private int amount;
	private BigDecimal price;

	public DBOrderItem() {
		super();
		this.price = new BigDecimal(0.00).setScale(2, RoundingMode.HALF_UP);
	}

	public DBOrderItem(DBOrder order, DBAlbum album, int amount,
			BigDecimal price)
			throws IllegalArgumentException {
		super();
		this.album = album;
		this.amount = amount;
		setPrice(price);
		this.setOrder(order);
	}		

	public DBOrder getOrder() {
		return order;
	}

	public void setOrder(DBOrder order) throws IllegalArgumentException {
		if (order == null) {
			throw new IllegalArgumentException("order cannot be null");
		}
		this.order = order;	
		this.order.addOrderItem(this);
	}

	public DBAlbum getAlbum() {
		return album;
	}

	public void setAlbum(DBAlbum album) {
		this.album = album;
	}

	public int getAmount() {
		return amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = BigDecimal.valueOf(price.doubleValue()).setScale(2,
				RoundingMode.HALF_UP);
	}

	/**
	 * Calculate the total price of this orderitem, i.e. price * amount.
	 * @return
	 */
	public BigDecimal getTotalPrice() {
		BigDecimal amountDecimal = BigDecimal.valueOf(amount);
		return price.multiply(amountDecimal).setScale(2, RoundingMode.HALF_UP);
	}

}

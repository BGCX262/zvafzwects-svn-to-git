package de.fhkl.dbsm.zvafzwects.model.db;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author Andreas Baur, Jochen Pätzold
 */
public class DBUser {
	private int userId;
	private String surname;
	private String forename;
	private String email;
	private String password;
	private Date dateOfBirth;
	private char gender;
	private List<DBAddress> addresses;
	private List<DBOrder> orders;
	private boolean isAdmin;

	public DBUser() {
		super();
		addresses = new ArrayList<DBAddress>();
		orders = new ArrayList<DBOrder>();
	}

	public DBUser(String surname, String forename, String email,
			String password, Date dateOfBirth, char gender, boolean isAdmin) {
		super();
		this.surname = surname;
		this.forename = forename;
		this.email = email;
		this.password = password;
		this.dateOfBirth = dateOfBirth;
		this.gender = gender;
		this.isAdmin = isAdmin;
		addresses = new ArrayList<DBAddress>();
		orders = new ArrayList<DBOrder>();
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getSurname() {
		return surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

	public String getForename() {
		return forename;
	}

	public void setForename(String forename) {
		this.forename = forename;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Date getDateOfBirth() {
		return dateOfBirth;
	}

	public String getDateOfBirthAsString() {
		if (dateOfBirth != null) {
			DateFormat formatter = new SimpleDateFormat("dd.MM.yyyy");
			return formatter.format(dateOfBirth);
		}
		return "";
	}

	public void setDateOfBirth(Date dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}
	

	public char getGender() {
		return gender;
	}

	public void setGender(char gender) {
		this.gender = gender;
	}

	public List<DBAddress> getAddresses() {
		return addresses;
	}

	public void setAddresses(List<DBAddress> addresses) {
		this.addresses = addresses;
	}

	public void addAddress(DBAddress address) {
		this.addresses.add(address);
	}

	public void removeAddress(DBAddress address) {
		if (addresses.isEmpty()) {
			return;
		}
		addresses.remove(address);
	}

	public List<DBOrder> getOrders() {
		return this.orders;
	}

	public void setOrders(List<DBOrder> orders) {
		this.orders = orders;
	}

	public void addOrder(DBOrder order) {
		this.orders.add(order);
	}

	public boolean getIsAdmin() {
		return this.isAdmin;
	}

	public void setIsAdmin(boolean isAdmin) {
		this.isAdmin = isAdmin;
	}

	public boolean addressExists(DBAddress address) {
		for (DBAddress addr : addresses) {
			if (addr.equals(address))
				return true;
		}
		return false;
	}
}

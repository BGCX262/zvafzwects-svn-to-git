package de.fhkl.dbsm.zvafzwects.servlet;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.db4o.ObjectContainer;

import de.fhkl.dbsm.zvafzwects.model.dao.DAOAddress;
import de.fhkl.dbsm.zvafzwects.model.dao.DAOAlbum;
import de.fhkl.dbsm.zvafzwects.model.dao.DAOCatchword;
import de.fhkl.dbsm.zvafzwects.model.dao.DAOCategory;
import de.fhkl.dbsm.zvafzwects.model.dao.DAOOrder;
import de.fhkl.dbsm.zvafzwects.model.dao.DAOUser;
import de.fhkl.dbsm.zvafzwects.model.dao.exception.DatabaseObjectNotFoundException;
import de.fhkl.dbsm.zvafzwects.model.dao.exception.GeneralDatabaseException;
import de.fhkl.dbsm.zvafzwects.model.dao.exception.UniqueFieldValueConstraintViolationException;
import de.fhkl.dbsm.zvafzwects.model.db.DBAddress;
import de.fhkl.dbsm.zvafzwects.model.db.DBAlbum;
import de.fhkl.dbsm.zvafzwects.model.db.DBCatchword;
import de.fhkl.dbsm.zvafzwects.model.db.DBCategory;
import de.fhkl.dbsm.zvafzwects.model.db.DBOrder;
import de.fhkl.dbsm.zvafzwects.model.db.DBOrderItem;
import de.fhkl.dbsm.zvafzwects.model.db.DBUser;
import de.fhkl.dbsm.zvafzwects.utils.hash.Whirlpool;

/**
 * Servlet implementation class UserServlet
 * 
 * @author Andreas Baur, Markus Henn, Jochen Pätzold
 * @version 2
 * @category servlet
 */
@WebServlet(description = "functions available for all users", urlPatterns = { "/user" })
public class UserServlet extends GeneralServlet {
	private static final long serialVersionUID = 1L;

	/*
	 * (non-Javadoc)
	 * 
	 * @see servlet.GeneralServlet#doGet(javax.servlet.http.HttpServletRequest,
	 * javax.servlet.http.HttpServletResponse, com.db4o.ObjectContainer)
	 */
	@Override
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response, ObjectContainer connection)
			throws ServletException, IOException, GeneralDatabaseException {

		String action = getRequestActionParameter(request);

		if (action == null) {
			setErrorMessage(request, "404: Kein Service spezifiziert!");
			request.getRequestDispatcher("error.jsp")
					.forward(request, response);
			return;
		}

		if ("search".compareToIgnoreCase(action) == 0) {
			searchAlba(request, response, connection);
		} else if ("display_album".compareToIgnoreCase(action) == 0) {
			displayAlbum(request, response, connection);
		} else if ("display_search".compareToIgnoreCase(action) == 0) {
			displaySearch(request, response, connection);
		} else if ("login".compareToIgnoreCase(action) == 0) {
			displayLoginUser(request, response);
		} else if ("logout".compareToIgnoreCase(action) == 0) {
			logoutUser(request, response);
		} else if ("display_register_user".compareToIgnoreCase(action) == 0) {
			displayRegisterUser(request, response);
		} else if ("order_shoppingcart".compareToIgnoreCase(action) == 0) {
			displayOrderShoppingCart(request, response, connection);
		} else if ("display_edit_user".compareToIgnoreCase(action) == 0) {
			displayEditUser(request, response);
		} else if ("display_create_address".compareToIgnoreCase(action) == 0) {
			displayCreateAddress(request, response);
		} else {
			setErrorMessage(request, "404: Service '" + action
					+ "' nicht gefunden!");
			request.getRequestDispatcher("error.jsp")
					.forward(request, response);
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see servlet.GeneralServlet#doPost(javax.servlet.http.HttpServletRequest,
	 * javax.servlet.http.HttpServletResponse, com.db4o.ObjectContainer)
	 */
	@Override
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response, ObjectContainer connection)
			throws ServletException, IOException, GeneralDatabaseException {
		String action = getRequestActionParameter(request);

		if (action == null) {
			setErrorMessage(request, "404: Kein Service spezifiziert!");
			request.getRequestDispatcher("error.jsp")
					.forward(request, response);
			return;
		}

		if ("login".compareToIgnoreCase(action) == 0) {
			loginUser(request, response, connection);
		} else if ("register_user".compareToIgnoreCase(action) == 0) {
			registerUser(request, response, connection);
		} else if ("order_item".compareToIgnoreCase(action) == 0) {
			orderItem(request, response, connection);
		} else if ("create_address".compareToIgnoreCase(action) == 0) {
			createAddress(request, response, connection);
		} else if ("edit_user".compareToIgnoreCase(action) == 0) {
			editUser(request, response, connection);
		} else if ("submit_order".compareToIgnoreCase(action) == 0) {
			submitOrder(request, response, connection);
		} else {
			setErrorMessage(request, "404: Service '" + action
					+ "' nicht gefunden!");
			request.getRequestDispatcher("error.jsp")
					.forward(request, response);
		}
	}

	/**
	 * Opens the index page
	 * 
	 * @param request the http servlet request object
	 * @param response the http servlet response object
	 * @throws ServletException
	 * @throws IOException
	 */
	private void displayIndex(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		request.getRequestDispatcher("index.jsp").forward(request, response);
	}

	/**
	 * Submits an order and updates the alba in the order with the new stock.
	 * 
	 * @param request the http servlet request object
	 * @param response the http servlet response object
	 * @param connection the object container
	 * @throws GeneralDatabaseException if the database is read only, closed or
	 *             a {@link com.db4o.ext.Db4oIOException Db4oIOException} is
	 *             raised
	 * @throws ServletException
	 * @throws IOException
	 */
	private void submitOrder(HttpServletRequest request,
			HttpServletResponse response, ObjectContainer connection)
			throws GeneralDatabaseException, ServletException, IOException {
		DBAddress invoiceAddress, deliverAddress;

		// user changed order items (amount)
		String confirmationNeededParameter = request
				.getParameter("confirmation_needed");
		// complete order button (set, if this button was used to submit form)
		String completeOrderParameter = request.getParameter("complete_order");

		// don't complete order when user changed some order details
		// or if he didn't click the "complete order" button
		if (completeOrderParameter == null
				|| "true".equals(confirmationNeededParameter)) {
			DBOrder order = getShoppingCart(request);
			List<DBOrderItem> orderItems = order.getOrderItems();
			try {
				for (int i = 0; true; i++) {
					String orderItemAmountString = request
							.getParameter("order_item_" + i + "_amount");
					if (orderItemAmountString == null) {
						break;
					}
					int orderItemAmount = Integer
							.parseInt(orderItemAmountString);
					if (orderItemAmount <= 0) {
						throw new IllegalArgumentException("amount must be > 0");
					}
					DBOrderItem orderItem = orderItems.get(i);
					if (orderItemAmount > orderItem.getAlbum().getStock()) {
						throw new IllegalArgumentException(
								"amount must be <= stock");
					}
					orderItem.setAmount(orderItemAmount);
				}
				// the update button was used explicitely
				if (completeOrderParameter == null) {
					setSuccessMessage(request,
							"Ihre Bestellung wurde erfolgreich aktualisiert.");
				}
				// the complete order process was interrupted
				else {
					setInfoMessage(
							request,
							"Ihre Bestellung wurde aktualisiert, überprüfen Sie die aktualisierte Bestellung!");
				}
			} catch (NumberFormatException e) {
				setErrorMessage(request,
						"Bei der Aktualisierung Ihrer Bestellung ist ein Fehler aufgetreten! Eine Mengenangabe war ungültig!");
			} catch (IllegalArgumentException e) {
				String detailedErrorMsg = " Eine Mengenangabe war ungültig!";
				if ("amount must be > 0".equals(e.getMessage())) {
					detailedErrorMsg += " Die Menge muss eine positive Zahl sein!";
				} else if ("amount must be <= stock".equals(e.getMessage())) {
					detailedErrorMsg += " Die gewählte Menge ist nicht mehr auf Lager!";

				}
				setErrorMessage(request,
						"Bei der Aktualisierung Ihrer Bestellung ist ein Fehler aufgetreten!"
								+ detailedErrorMsg);
			} catch (IndexOutOfBoundsException e) {
				setErrorMessage(request,
						"Bei der Aktualisierung Ihrer Bestellung ist ein Fehler aufgetreten!");
			}
			displayOrderShoppingCart(request, response, connection);
			return;
		}

		// submit order
		int invoiceAddressId = Integer.parseInt(request
				.getParameter("invoiceAddress"));
		int deliverAddressId = Integer.parseInt(request
				.getParameter("deliverAddress"));

		try {
			invoiceAddress = DAOAddress.getAddressById(connection,
					invoiceAddressId);
			deliverAddress = DAOAddress.getAddressById(connection,
					deliverAddressId);

			DBOrder order = getShoppingCart(request);
			order.setDeliverAddress(deliverAddress);
			order.setInvoiceAddress(invoiceAddress);

			DAOOrder.insertOrder(connection, order);

			setSessionAttribute(request, "shoppingCart", null);

			setSuccessMessage(request, "Bestellung erfolgreich aufgegeben.");
		} catch (DatabaseObjectNotFoundException e) {
			setErrorMessage(request,
					"Datenbankfehler. Bitte versuchen Sie es später erneut.");
		}
		// dispatch in both, error case and success case.
		displayIndex(request, response);
	}

	/**
	 * Opens the login page
	 * 
	 * @param request the http servlet request object
	 * @param response the http servlet response object
	 * @throws ServletException
	 * @throws IOException
	 */
	private void displayLoginUser(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		request.getRequestDispatcher("login.jsp").forward(request, response);
	}

	/**
	 * Opens the register user page
	 * 
	 * @param request the http servlet request object
	 * @param response the http servlet response object
	 * @throws ServletException
	 * @throws IOException
	 */
	private void displayRegisterUser(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		request.setAttribute("formAction", "user?action=register_user");
		request.getRequestDispatcher("edituser.jsp").forward(request, response);
	}

	/**
	 * Opens the edit user page
	 * 
	 * @param request the http servlet request object
	 * @param response the http servlet response object
	 * @throws ServletException
	 * @throws IOException
	 */
	private void displayEditUser(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		request.setAttribute("formAction", "user?action=edit_user");
		request.getRequestDispatcher("edituser.jsp").forward(request, response);
	}

	/**
	 * Method for registering an user
	 * 
	 * @param request the http servlet request object
	 * @param response the http servlet response object
	 * @param connection the object container
	 * @throws IOException
	 * @throws ServletException
	 * @throws GeneralDatabaseException if the database is read only, closed or
	 *             a {@link com.db4o.ext.Db4oIOException Db4oIOException} is
	 *             raised
	 */
	private void registerUser(HttpServletRequest request,
			HttpServletResponse response, ObjectContainer connection)
			throws ServletException, IOException, GeneralDatabaseException {
		// retrieve parameters from jsp
		String userSurname = request.getParameter("user_surname");
		String userForename = request.getParameter("user_forename");
		String inputGender = request.getParameter("user_gender");
		char userGender = ' ';

		// check if a gender radio button is checked
		if ("m".equalsIgnoreCase(inputGender)
				|| "w".equalsIgnoreCase(inputGender))
			userGender = request.getParameter("user_gender").charAt(0);

		String userDate = request.getParameter("user_date_of_birth");
		String userEmail = request.getParameter("user_email");
		String userPassword = request.getParameter("user_password");

		// sets the flag to false if isadmin checkbox is unchecked, else true
		boolean userIsAdmin = (request.getParameter("user_isadmin") == null) ? false
				: true;

		if ("".equalsIgnoreCase(userSurname)
				|| "".equalsIgnoreCase(userForename)
				|| "".equalsIgnoreCase(userDate)
				|| "".equalsIgnoreCase(userEmail)
				|| "".equalsIgnoreCase(userPassword)) {
			setErrorMessage(request, "Alle Felder sind Pflichtfelder!");
			request.getRequestDispatcher("edituser.jsp").forward(request,
					response);
			return;
		}

		try {
			// hash the password before storing the user object
			userPassword = Whirlpool.hash(userPassword);

			DateFormat formatter = new SimpleDateFormat("dd.MM.yyyy");
			Date date = formatter.parse(userDate);

			DBUser user = new DBUser(userSurname, userForename, userEmail,
					userPassword, date, userGender, userIsAdmin);
			DAOUser.insertUser(connection, user);

			setSuccessMessage(request,
					"Registrierung erfolgreich. Sie sind jetzt eingeloggt.");
			// login user (save user data in session)
			setUser(request, user);
			displayIndex(request, response);

		} catch (UniqueFieldValueConstraintViolationException ex) {
			setErrorMessage(request,
					"Es existiert bereits ein Benutzer mit dieser Email.");
			displayEditUser(request, response);
		} catch (IllegalArgumentException e) {
			setErrorMessage(request,
					"Es kann kein null-Objekt gespeichert werden.");
			displayEditUser(request, response);
		} catch (ParseException e) {
			setInfoMessage(request,
					"Das Datum muss in folgendem Format angegeben werden: \"DD.MM.YYYY\"");
			displayEditUser(request, response);
		}
		return;
	}

	/**
	 * Method for editing an user
	 * 
	 * @param request the http servlet request object
	 * @param response the http servlet response object
	 * @param connection the object container
	 * @throws IOException
	 * @throws ServletException
	 * @throws GeneralDatabaseException if the database is read only, closed or
	 *             a {@link com.db4o.ext.Db4oIOException Db4oIOException} is
	 *             raised
	 */
	private void editUser(HttpServletRequest request,
			HttpServletResponse response, ObjectContainer connection)
			throws ServletException, IOException, GeneralDatabaseException {
		// check if user is logged in
		if (getUser(request) == null) {
			setErrorMessage(request,
					"Sie müssen eingeloggt sein, um Ihre Daten zu ändern.");
			displayLoginUser(request, response);
		}

		// retrieve parameters from jsp
		String userSurname = request.getParameter("user_surname");
		String userForename = request.getParameter("user_forename");
		String inputGender = request.getParameter("user_gender");
		char userGender = ' ';

		// check if a gender radio button is checked
		if ("m".equalsIgnoreCase(inputGender)
				|| "w".equalsIgnoreCase(inputGender))
			userGender = request.getParameter("user_gender").charAt(0);

		String userDate = request.getParameter("user_date_of_birth");
		String userPassword = request.getParameter("user_password");

		if ("".equalsIgnoreCase(userSurname)
				|| "".equalsIgnoreCase(userForename)
				|| "".equalsIgnoreCase(userDate)) {
			setInfoMessage(request,
					"Nachname, Vorname und Geburtsdatum sind Pflichtfelder!");
			displayEditUser(request, response);
			return;
		}

		String userPasswordNew = request.getParameter("user_password_new");
		String userPasswordApprove = request
				.getParameter("user_password_approve");

		if ((!"".equals(userPasswordNew) || !"".equals(userPasswordApprove))
				&& !userPasswordNew.equals(userPasswordApprove)) {
			setInfoMessage(request, "Passwörter stimmen nicht überein.");
			displayEditUser(request, response);
			return;
		}

		try {
			DBUser user = DAOUser.getUserById(connection, getUser(request)
					.getUserId());

			// hash the password before comparing the password in the user
			// object
			if ("".equals(userPassword)) {
				setInfoMessage(request, "Bitte Passwort eingeben.");
				displayEditUser(request, response);
				return;
			}

			if (!user.getPassword().equals(Whirlpool.hash(userPassword))) {
				setInfoMessage(request,
						"Falsches Passwort. Bitte erneut eingeben.");
				displayEditUser(request, response);
				return;
			}

			DateFormat formatter = new SimpleDateFormat("dd.MM.yyyy");
			Date date = formatter.parse(userDate);
			
			user.setSurname(userSurname);
			user.setForename(userForename);
			user.setDateOfBirth(date);
			user.setGender(userGender);

			if (!"".equals(userPasswordNew))
				user.setPassword(Whirlpool.hash(userPasswordNew));

			DAOUser.updateUser(connection, user);

			setSuccessMessage(request, "Änderungen erfolgreich übernommen.");
			setUser(request, user);
			displayIndex(request, response);
		} catch (IllegalArgumentException e) {
			setErrorMessage(request,
					"Es kann kein null-Objekt gespeichert werden.");
			displayEditUser(request, response);
		} catch (DatabaseObjectNotFoundException e) {
			setErrorMessage(request,
					"Datenbankfehler. Bitte versuchen Sie es später erneut.");
			displayIndex(request, response);
		} catch (UniqueFieldValueConstraintViolationException e) {
			setInfoMessage(request, "Email wird bereits von einem anderen Benutzer verwendet.");
			displayEditUser(request, response);
		} catch (ParseException e) {
			setInfoMessage(request,
					"Das Datum muss in folgendem Format angegeben werden: \"DD.MM.YYYY\"");
			displayEditUser(request, response);
		}
		return;
	}

	/**
	 * Method for login as an user
	 * 
	 * @param request the http servlet request object
	 * @param response the http servlet response object
	 * @param connection the object container
	 * @throws ServletException
	 * @throws IOException
	 * @throws GeneralDatabaseException if the database is read only, closed or
	 *             a {@link com.db4o.ext.Db4oIOException Db4oIOException} is
	 *             raised
	 */
	private void loginUser(HttpServletRequest request,
			HttpServletResponse response, ObjectContainer connection)
			throws ServletException, IOException, GeneralDatabaseException {
		// retrieve parameters from jsp
		String userEmail = request.getParameter("login_email");
		String userPassword = request.getParameter("login_password");

		if ((userEmail == null || "".equalsIgnoreCase(userEmail))
				|| (userPassword == null || "".equalsIgnoreCase(userPassword))) {
			setErrorMessage(request, "Bitte geben Sie Email UND Passwort ein.");

			displayLoginUser(request, response);
			return;
		}

		try {
			// hash the user input before checking with the db entry
			userPassword = Whirlpool.hash(userPassword);

			DBUser user = DAOUser.getUserByCredentials(connection, userEmail,
					userPassword);

			setUser(request, user);
			setSuccessMessage(request, "Login erfolgreich.");

			redirectTo(request, response,
					request.getParameter("redirectParameter"));

		} catch (DatabaseObjectNotFoundException e) {
			setErrorMessage(request,
					"Es konnte kein Benutzer mit den eingegebenen Daten gefunden werden.");

			displayLoginUser(request, response);
		}

		return;
	}

	/**
	 * Method for logout of the user
	 * 
	 * @param request the http servlet request object
	 * @param response the http servlet response object
	 * @throws IOException
	 * @throws ServletException
	 */
	private void logoutUser(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// set the user in the session to null
		setUser(request, null);
		setSessionAttribute(request, "shoppingCart", null);
		setSuccessMessage(request, "Sie wurden erfolgreich ausgelogged.");
		displayIndex(request, response);
		return;
	}

	/**
	 * Method for displaying an album
	 * 
	 * @param request the http servlet request object
	 * @param response the http servlet response object
	 * @param connection the object container
	 * @throws IOException
	 * @throws ServletException
	 * @throws GeneralDatabaseException if the database is read only, closed or
	 *             a {@link com.db4o.ext.Db4oIOException Db4oIOException} is
	 *             raised
	 */
	private void displayAlbum(HttpServletRequest request,
			HttpServletResponse response, ObjectContainer connection)
			throws ServletException, IOException, GeneralDatabaseException {
		// retrieve the album id
		String albumIdString = request.getParameter("album_id");

		try {
			int albumId = Integer.parseInt(albumIdString);

			DBAlbum album = DAOAlbum.getAlbumByID(connection, albumId);

			request.setAttribute("album", album);
			request.getRequestDispatcher("viewalbum.jsp").forward(request,
					response);
		} catch (NumberFormatException ex) {
			setErrorMessage(request, "Ungültige Album ID.");
			displayIndex(request, response);
		} catch (DatabaseObjectNotFoundException e) {
			setErrorMessage(request,
					"Es konnte kein Album mit der gesuchten ID gefunden werden.");
			displayIndex(request, response);
		}

		return;
	}

	/**
	 * Method for searching alba
	 * 
	 * @param request the http servlet request object
	 * @param response the http servlet response object
	 * @param connection the object container
	 * @throws ServletException
	 * @throws IOException
	 * @throws GeneralDatabaseException if the database is read only, closed or
	 *             a {@link com.db4o.ext.Db4oIOException Db4oIOException} is
	 *             raised
	 */
	private void searchAlba(HttpServletRequest request,
			HttpServletResponse response, ObjectContainer connection)
			throws ServletException, IOException, GeneralDatabaseException {
		// initialize template objects
		DBCategory category = null;
		List<DBCatchword> catchwords = null;

		// retrieve the parameters
		String albumTitle = request.getParameter("album_title");
		String albumInterpreter = request.getParameter("album_interpreter");
		String albumCategoryIdString = request.getParameter("album_category");

		String[] albumCatchwordIdStrings = request
				.getParameterValues("album_catchwords[]");

		// handle the category
		if (albumCategoryIdString != null && !"".equals(albumCategoryIdString)) {
			try {

				int categoryId = Integer.parseInt(albumCategoryIdString);
				category = DAOCategory.getCategoryById(connection, categoryId);
			} catch (NumberFormatException ex) {
				setErrorMessage(request, "Ungültige Kategorie ID.");
				request.getRequestDispatcher("search.jsp").forward(request,
						response);
				return;
			} catch (DatabaseObjectNotFoundException e) {
				setErrorMessage(request,
						"Es konnte keine Kategorie mit der gesuchten ID gefunden werden.");
				request.getRequestDispatcher("search.jsp").forward(request,
						response);
				return;
			}
		}

		// handle the catchwords
		if (albumCatchwordIdStrings != null
				&& (albumCatchwordIdStrings.length > 0)) {
			try {

				catchwords = new ArrayList<DBCatchword>();
				for (String albumCatchwordIdString : albumCatchwordIdStrings) {
					if (albumCatchwordIdString != null
							&& !"".equals(albumCatchwordIdString)) {
						int catchwordId = Integer
								.parseInt(albumCatchwordIdString);
						catchwords.add(DAOCatchword.getCatchwordById(
								connection, catchwordId));
					}
				}
			} catch (DatabaseObjectNotFoundException e) {
				setErrorMessage(request,
						"Es konnte kein Schlagwort mit der gesuchten ID gefunden werden.");
				request.getRequestDispatcher("search.jsp").forward(request,
						response);
				return;
			}
		}

		// search for alba
		List<DBAlbum> alba = DAOAlbum.getAlbumBySearch(connection, catchwords,
				category, albumTitle, albumInterpreter);

		request.setAttribute("alba", alba);

		request.getRequestDispatcher("showsearchresults.jsp").forward(request,
				response);
		return;
	}

	/**
	 * Method for getting the categories and catchwords for the search
	 * 
	 * @param request the http servlet request object
	 * @param response the http servlet response object
	 * @param connection the object container
	 * @throws ServletException
	 * @throws IOException
	 * @throws GeneralDatabaseException if the database is read only, closed or
	 *             a {@link com.db4o.ext.Db4oIOException Db4oIOException} is
	 *             raised
	 */
	private void displaySearch(HttpServletRequest request,
			HttpServletResponse response, ObjectContainer connection)
			throws ServletException, IOException, GeneralDatabaseException {
		List<DBCategory> allCategories = getAllCategoriesInUse(request,
				connection);
		List<DBCatchword> allCatchwords = getAllCatchwordsInUse(request,
				connection);

		request.setAttribute("categories", allCategories);
		request.setAttribute("catchwords", allCatchwords);
		request.getRequestDispatcher("searchalbum.jsp").forward(request,
				response);
	}

	/**
	 * Orders the item specified by request parameter
	 * 
	 * @param request the http servlet request object
	 * @param response the http servlet response object
	 * @param connection the object container
	 * @throws IOException
	 * @throws GeneralDatabaseException if the database is read only, closed or
	 *             a {@link com.db4o.ext.Db4oIOException Db4oIOException} is
	 *             raised
	 * @throws ServletException
	 */
	private void orderItem(HttpServletRequest request,
			HttpServletResponse response, ObjectContainer connection)
			throws IOException, GeneralDatabaseException, ServletException {
		try {
			String amountString = request.getParameter("order_item_amount");
			int amount = Integer.parseInt(amountString);

			String albumIdString = request.getParameter("album_id");
			int albumId = Integer.parseInt(albumIdString);
			DBAlbum album = DAOAlbum.getAlbumByID(connection, albumId);

			if (amount <= 0) {
				setInfoMessage(request, "Anzahl muss größer 0 sein.");
				displayAlbum(request, response, connection);
				return;
			}

			if (amount > album.getStock()) {
				setInfoMessage(request,
						"So viele Alben sind nicht mehr vorhanden.");
				displayAlbum(request, response, connection);
				return;
			}

			DBOrder shoppingCart = getShoppingCart(request);

			// check if album is already in shoopingcart
			// if true, increase the amount of the album in the shoppingcart
			for (DBOrderItem item : shoppingCart.getOrderItems()) {
				if (item.getAlbum().getAlbumId() == album.getAlbumId()) {
					if (item.getAmount() + amount > album.getStock()) {
						setInfoMessage(request,
								"So viele Alben sind nicht mehr vorhanden.");
						displayAlbum(request, response, connection);
						return;
					}

					item.setAmount(item.getAmount() + amount);
					displayAlbum(request, response, connection);
					return;
				}
			}

			new DBOrderItem(shoppingCart, album, amount, album.getPrice());
			setSessionAttribute(request, "shoppingCart", shoppingCart);

			displayAlbum(request, response, connection);
		} catch (NumberFormatException e) {
			setInfoMessage(request, "Stückzahl muss eine Zahl sein!");
			displayAlbum(request, response, connection);
		} catch (DatabaseObjectNotFoundException e) {
			setErrorMessage(request, "Album konnte nicht gefunden werden.");
			displayIndex(request, response);
		}
	}

	/**
	 * Method for getting all categories in the database which are currently in
	 * use
	 * 
	 * @param request the request object
	 * @param connection database connection object
	 * @return a list of all existing categories
	 * @throws GeneralDatabaseException if database is closed or if
	 *             {@link com.db4o.ext.Db4oIOException Db4oIOException} was
	 *             raised
	 */
	protected final static List<DBCategory> getAllCategoriesInUse(
			HttpServletRequest request, ObjectContainer connection)
			throws GeneralDatabaseException {
		return DAOCategory.getAllCategoriesInUse(connection);
	}

	/**
	 * Method for getting all catchwords in the database which are currently in
	 * use
	 * 
	 * @param request the request object
	 * @param connection database connection object
	 * @return a list of all existing catchwords
	 * @throws GeneralDatabaseException if database is closed or if
	 *             {@link com.db4o.ext.Db4oIOException Db4oIOException} was
	 *             raised
	 */
	protected final static List<DBCatchword> getAllCatchwordsInUse(
			HttpServletRequest request, ObjectContainer connection)
			throws GeneralDatabaseException {
		return DAOCatchword.getAllCatchwordsInUse(connection);
	}

	/**
	 * Gets the shoppingcart from the session or, if none existed, creates a new
	 * one
	 * 
	 * @param request the request object
	 * @return the current shopping cart, saved in the session
	 */
	private static DBOrder getShoppingCart(HttpServletRequest request) {
		DBOrder shoppingCart = (DBOrder) getSessionAttribute(request,
				"shoppingCart");
		if (shoppingCart == null) {
			shoppingCart = new DBOrder();
		}
		return shoppingCart;
	}

	/**
	 * Orders the current shopping cart, saved in the session
	 * 
	 * @param request the http servlet request object
	 * @param response the http servlet response object
	 * @param connection the object container
	 * @throws IOException
	 * @throws ServletException
	 */
	private void displayOrderShoppingCart(HttpServletRequest request,
			HttpServletResponse response, ObjectContainer connection)
			throws ServletException, IOException {
		// check if user is logged in
		DBUser user = getUser(request);
		if (user == null) {
			setInfoMessage(request,
					"Um die Bestellung abzuschließen, müssen Sie eingeloggt sein.");
			displayLoginUser(request, response);
			return;
		} else if (user.getAddresses().isEmpty()) {
			setInfoMessage(request, "Sie haben noch keine Adresse angelegt.");
			displayCreateAddress(request, response);
			return;
		} else {
			List<DBAddress> addresses = user.getAddresses();
			request.setAttribute("addresses", addresses);
			request.getRequestDispatcher("order.jsp")
					.forward(request, response);
		}
	}

	/**
	 * Opens the page to create an address
	 * 
	 * @param request the http servlet request object
	 * @param response the http servlet response object
	 * @param connection the object container
	 * @throws IOException
	 * @throws ServletException
	 */
	private void displayCreateAddress(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		request.getRequestDispatcher("createaddress.jsp").forward(request,
				response);
	}

	/**
	 * Creates the address specified by the request parameters
	 * 
	 * @param request the http servlet request object
	 * @param response the http servlet response object
	 * @param connection the object container
	 * @throws IOException
	 * @throws ServletException
	 * @throws GeneralDatabaseException if the database is read only, closed or
	 *             a {@link com.db4o.ext.Db4oIOException Db4oIOException} is
	 *             raised
	 */
	private void createAddress(HttpServletRequest request,
			HttpServletResponse response, ObjectContainer connection)
			throws ServletException, IOException, GeneralDatabaseException {
		try {
			String street = request.getParameter("address_street");
			String housenumber = request.getParameter("address_housenumber");
			String city = request.getParameter("address_city");
			String zipcode = request.getParameter("address_zipcode");
			// check if all fields are set
			if ("".equalsIgnoreCase(street) || "".equalsIgnoreCase(housenumber)
					|| "".equalsIgnoreCase(city)
					|| "".equalsIgnoreCase(zipcode)) {
				setInfoMessage(request, "Bitte alle Felder ausfüllen.");
				displayCreateAddress(request, response);
				return;
			}
			// build address object
			DBAddress address = new DBAddress(street, housenumber, city, zipcode);
			DBUser user = getUser(request);
			// no user logged in?
			if (user == null) {
				setInfoMessage(request,
						"Sie müssen eingeloggt sein, um eine Adresse anlegen zu können.");
				displayLoginUser(request, response);
				return;
			}
			// retrieve user from database (session does not work for an update)
			user = DAOUser.getUserById(connection, user.getUserId());

			// check if address already exists
			if (user.addressExists(address)) {
				setInfoMessage(request, "Diese Adresse existiert bereits!");
				displayCreateAddress(request, response);
			}
			// add address to the user and update the object
			DAOAddress.insertAddress(connection, address);
			user.addAddress(address);
			DAOUser.updateUser(connection, user);

			// update user object in session
			setUser(request, user);

			setSuccessMessage(request,
					"Ihre neue Adresse wurde erfolgreich angelegt.");
			redirectTo(request, response,
					request.getParameter("redirectParameter"));

		} catch (IllegalArgumentException e) {
			setErrorMessage(request,
					"Es kann kein null-Objekt gespeichert werden.");
			displayCreateAddress(request, response);
		} catch (DatabaseObjectNotFoundException e) {
			setErrorMessage(request,
					"Datenbankfehler. Bitte versuchen Sie es später erneut.");
			displayIndex(request, response);
		} catch (UniqueFieldValueConstraintViolationException e) {
			setInfoMessage(request,
					"Oh, dies sollte eigentlich nicht vorkommen.");
			e.printStackTrace();
			displayCreateAddress(request, response);
		}
	}

	/**
	 * Can be used to redirect to the specified url. <b>Currently OUT OF ORDER
	 * because redirect problems with POST action of referrers.</b>
	 * 
	 * @param request the current request object
	 * @param response the current response object
	 * @param redirectUrl the url, which is targeted in a redirect
	 * @throws ServletException if the target resource throws this exception
	 * @throws IOException if the target resource throws this exception
	 */
	private void redirectTo(HttpServletRequest request,
			HttpServletResponse response, String redirectUrl)
			throws ServletException, IOException {
		/*
		 * Problems with post actions in referrer, so we don't use the referrer
		 * request.setAttribute("redirect",
		 * request.getParameter("redirectParameter"));
		 * request.getRequestDispatcher("redirect.jsp").forward(request,
		 * response);
		 */
		// redirect to the index page instead
		displayIndex(request, response);
	}
}

package de.fhkl.dbsm.zvafzwects.servlet;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.DataFormatException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import com.db4o.ObjectContainer;

import de.fhkl.dbsm.zvafzwects.model.adapter.InputDataAlbum;
import de.fhkl.dbsm.zvafzwects.model.dao.DAOAlbum;
import de.fhkl.dbsm.zvafzwects.model.dao.DAOCatchword;
import de.fhkl.dbsm.zvafzwects.model.dao.DAOCategory;
import de.fhkl.dbsm.zvafzwects.model.dao.exception.DatabaseObjectNotFoundException;
import de.fhkl.dbsm.zvafzwects.model.dao.exception.GeneralDatabaseException;
import de.fhkl.dbsm.zvafzwects.model.dao.exception.UniqueFieldValueConstraintViolationException;
import de.fhkl.dbsm.zvafzwects.model.db.DBAlbum;
import de.fhkl.dbsm.zvafzwects.model.db.DBCatchword;
import de.fhkl.dbsm.zvafzwects.model.db.DBCategory;
import de.fhkl.dbsm.zvafzwects.model.db.DBUser;

/**
 * Servlet implementation class AdminServlet
 * 
 * @author Andreas Baur, Markus Henn
 */
@WebServlet(description = "functions only available for logged in system administrators", urlPatterns = { "/admin" })
public class AdminServlet extends GeneralServlet {
	private static final long serialVersionUID = 1L;

	private File tempFilePath;

	/*
	 * (non-Javadoc)
	 * 
	 * @see servlet.GeneralServlet#init()
	 */
	@Override
	public void init() throws ServletException {
		super.init();
		this.tempFilePath = (File) getServletContext().getAttribute(
				"javax.servlet.context.tempdir");
	}

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

		if (!isAdminInSession(request)) {
			setErrorMessage(
					request,
					"403: Um diese Funktionalität nutzen zu können, müssen Sie als Administrator eingeloggt sein!");
			request.getRequestDispatcher("error.jsp")
					.forward(request, response);
			return;
		}

		String action = getRequestActionParameter(request);

		if (action == null) {
			setErrorMessage(request, "404: Kein Service spezifiziert!");
			request.getRequestDispatcher("error.jsp")
					.forward(request, response);
			return;
		}

		if ("display_edit_album".compareToIgnoreCase(action) == 0) {
			displayEditAlbum(request, response, connection);
		} else if ("display_new_album".compareToIgnoreCase(action) == 0) {
			displayEditAlbum(request, response, connection);
		} else if ("display_create_category".compareToIgnoreCase(action) == 0) {
			displayEditCategory(request, response, connection);
		} else if ("display_create_catchword".compareToIgnoreCase(action) == 0) {
			displayEditCatchword(request, response, connection);
		} else if ("display_edit_category".compareToIgnoreCase(action) == 0) {
			displayEditCategory(request, response, connection);
		} else if ("display_edit_catchword".compareToIgnoreCase(action) == 0) {
			displayEditCatchword(request, response, connection);
		} else if ("delete_category".compareToIgnoreCase(action) == 0) {
			deleteCategory(request, response, connection);
		} else if ("delete_catchword".compareToIgnoreCase(action) == 0) {
			deleteCatchword(request, response, connection);
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

		if (!isAdminInSession(request)) {
			setErrorMessage(
					request,
					"403: Um diese Funktionalität zu nutzen, müssen Sie als Administrator eingeloggt sein.");
			request.getRequestDispatcher("error.jsp")
					.forward(request, response);
			return;
		}

		String action = getRequestActionParameter(request);

		if (action == null) {
			setErrorMessage(request, "404: Kein Service spezifiziert!");
			request.getRequestDispatcher("error.jsp")
					.forward(request, response);
			return;
		}

		if ("create_category".compareToIgnoreCase(action) == 0) {
			createCategory(request, response, connection);
		} else if ("create_catchword".compareToIgnoreCase(action) == 0) {
			createCatchword(request, response, connection);
		} else if ("create_album".compareToIgnoreCase(action) == 0) {
			createAlbum(request, response, connection);
		} else if ("edit_album".compareToIgnoreCase(action) == 0) {
			editAlbum(request, response, connection);
		} else if ("edit_category".compareToIgnoreCase(action) == 0) {
			editCategory(request, response, connection);
		} else if ("edit_catchword".compareToIgnoreCase(action) == 0) {
			editCatchword(request, response, connection);
		} else {
			setErrorMessage(request, "404: Service '" + action
					+ "' nicht gefunden!");
			request.getRequestDispatcher("error.jsp")
					.forward(request, response);
		}
	}

	/**
	 * Opens the page to edit or create an album
	 * 
	 * @param request the http servlet request object
	 * @param response the http servlet response object
	 * @param connection the object container
	 * @throws ServletException
	 * @throws IOException
	 * @throws GeneralDatabaseException if database is closed or if
	 *             {@link com.db4o.ext.Db4oIOException Db4oIOException} was
	 *             raised
	 */
	private static void displayEditAlbum(HttpServletRequest request,
			HttpServletResponse response, ObjectContainer connection)
			throws ServletException, IOException, GeneralDatabaseException {
		String albumIdString = request.getParameter("album_id");

		// album id given? use it (else new album)
		if (albumIdString != null) {
			try {
				int albumId = Integer.parseInt(albumIdString);

				DBAlbum album = DAOAlbum.getAlbumByID(connection, albumId);

				request.setAttribute("album", album);
				request.setAttribute("formAction",
						"admin?action=edit_album&album_id=" + albumId);
			} catch (NumberFormatException ex) {
				// album id is no number
				setErrorMessage(request, "404: Ungültige Album-ID '"
						+ albumIdString + "'!");
				request.getRequestDispatcher("error.jsp").forward(request,
						response);
				return;
			} catch (DatabaseObjectNotFoundException ex) {
				// no album with the given id exists
				setErrorMessage(request,
						"404: Es existiert kein Album mit der ID '"
								+ albumIdString + "'!");
				request.getRequestDispatcher("error.jsp").forward(request,
						response);
				return;
			}

		} else {
			request.setAttribute("formAction", "admin?action=create_album");
		}

		List<DBCategory> allCategories = getAllCategories(request, connection);
		List<DBCatchword> allCatchwords = getAllCatchwords(request, connection);

		request.setAttribute("categories", allCategories);
		request.setAttribute("catchwords", allCatchwords);
		request.getRequestDispatcher("editalbum.jsp")
				.forward(request, response);
	}

	/**
	 * Opens a page to create or rename a category
	 * 
	 * @param request the http servlet request object
	 * @param response the http servlet response object
	 * @param connection the object container
	 * @throws GeneralDatabaseException if database is closed or if
	 *             {@link com.db4o.ext.Db4oIOException Db4oIOException} was
	 *             raised
	 */
	private static void displayEditCategory(HttpServletRequest request,
			HttpServletResponse response, ObjectContainer connection)
			throws GeneralDatabaseException, ServletException, IOException {
		String categoryIdString = request.getParameter("display_category_id");

		// is the category id given edit else create
		if (categoryIdString != null) {
			try {
				int categoryId = Integer.parseInt(categoryIdString);

				DBCategory category = DAOCategory.getCategoryById(connection,
						categoryId);

				request.setAttribute("category", category);
				request.setAttribute("formAction",
						"admin?action=edit_category&category_id=" + categoryId);
			} catch (NumberFormatException e) {
				// category id is not a number
				setErrorMessage(request, "Fehler beim parsen der Kategorie-ID");
			} catch (DatabaseObjectNotFoundException e) {
				setErrorMessage(request,
						"404: Es existiert keine Kategorie mit der ID '"
								+ categoryIdString + "'!");
			}
		} else {
			request.setAttribute("formAction", "admin?action=create_category");
		}

		List<DBCategory> allCategories = getAllCategories(request, connection);
		request.setAttribute("categories", allCategories);

		request.getRequestDispatcher("editcategory.jsp").forward(request,
				response);
	}

	/**
	 * Opens the page to create or rename a catchword
	 * 
	 * @param request the http servlet request object
	 * @param response the http servlet response object
	 * @param connection the object container
	 * @throws IOException
	 * @throws ServletException
	 * @throws GeneralDatabaseException if database is closed or if
	 *             {@link com.db4o.ext.Db4oIOException Db4oIOException} was
	 *             raised
	 */
	private static void displayEditCatchword(HttpServletRequest request,
			HttpServletResponse response, ObjectContainer connection)
			throws GeneralDatabaseException, ServletException, IOException {
		String catchwordIdString = request.getParameter("display_catchword_id");

		// is the catchword id given edit else create
		if (catchwordIdString != null) {
			try {
				int catchwordId = Integer.parseInt(catchwordIdString);

				DBCatchword catchword = DAOCatchword.getCatchwordById(
						connection, catchwordId);

				request.setAttribute("catchword", catchword);
				request.setAttribute("formAction",
						"admin?action=edit_catchword&catchword_id="
								+ catchwordId);
			} catch (NumberFormatException e) {
				// catchword id is not a number
				setErrorMessage(request, "Fehler beim Parsen der Schlagwort-ID");
			} catch (DatabaseObjectNotFoundException e) {
				setErrorMessage(request,
						"404: Es existiert kein Schlagwort mit der ID '"
								+ catchwordIdString + "'!");
			}
		} else {
			request.setAttribute("formAction", "admin?action=create_catchword");
		}

		List<DBCatchword> allCatchwords = getAllCatchwords(request, connection);
		request.setAttribute("catchwords", allCatchwords);

		request.getRequestDispatcher("editcatchword.jsp").forward(request,
				response);
	}

	/**
	 * Creates the category specified by the request parameter
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
	private static void createCategory(HttpServletRequest request,
			HttpServletResponse response, ObjectContainer connection)
			throws ServletException, IOException, GeneralDatabaseException {
		String categoryName = request.getParameter("category_name");

		// no category given -> error, let the user try again
		if (categoryName == null || categoryName == "") {
			setInfoMessage(request,
					"Konnte Kategorie nicht anlegen, da kein Name angegeben wurde!");
			displayEditCategory(request, response, connection);
			return;
		}

		try {
			DAOCategory
					.insertCategory(connection, new DBCategory(categoryName));

			setSuccessMessage(request, "Die Kategorie '" + categoryName
					+ "' wurde erfolgreich angelegt!");
		} catch (UniqueFieldValueConstraintViolationException ex) {
			// category already exists in the database -> error, let the user
			// try again
			setInfoMessage(request,
					"Konnte Kategorie nicht anlegen, da bereits eine Kategorie mit dem Namen '"
							+ categoryName + "' existiert!");
		}

		displayEditCategory(request, response, connection);
	}

	/**
	 * Creates the catchword specified by the request parameter
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
	private static void createCatchword(HttpServletRequest request,
			HttpServletResponse response, ObjectContainer connection)
			throws ServletException, IOException, GeneralDatabaseException {
		String catchwordName = request.getParameter("catchword_name");

		// no catchword given -> error, let the user try again
		if (catchwordName == null || catchwordName == "") {
			setInfoMessage(request,
					"Konnte Schlagwort nicht anlegen, da kein Name angegeben wurde!");
			displayEditCatchword(request, response, connection);
			return;
		}

		try {
			DAOCatchword.insertCatchword(connection, new DBCatchword(
					catchwordName));

			setSuccessMessage(request, "Das Schlagwort '" + catchwordName
					+ "' wurde erfolgreich angelegt!");
		} catch (UniqueFieldValueConstraintViolationException ex) {
			// catchword already exists in the database -> error, let the user
			// try again
			setInfoMessage(request,
					"Konnte Schlagwort nicht anlegen, da bereits ein Stichwort mit dem Namen '"
							+ catchwordName + "' existiert!");
		}

		displayEditCatchword(request, response, connection);
	}

	/**
	 * Creates the album with the data specified by the request parameters
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
	private void createAlbum(HttpServletRequest request,
			HttpServletResponse response, ObjectContainer connection)
			throws GeneralDatabaseException, ServletException, IOException {
		DBAlbum album = new DBAlbum(null, null, new BigDecimal(0.00), null, 0,
				null, null);
		album.setAlbumId(-1);

		if (readAlbumData(request, response, connection, album)) {
			DAOAlbum.insertAlbum(connection, album);
			setSuccessMessage(request, "Album wurde erfolgreich angelegt!");
			request.setAttribute("album", album);
			request.getRequestDispatcher("viewalbum.jsp").forward(request,
					response);
		}
	}

	/**
	 * Edits the album with the data specified by the request parameters
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
	private void editAlbum(HttpServletRequest request,
			HttpServletResponse response, ObjectContainer connection)
			throws ServletException, IOException, GeneralDatabaseException {
		String albumIdString = request.getParameter("album_id");

		try {
			int albumId = Integer.parseInt(albumIdString);
			DBAlbum album = DAOAlbum.getAlbumByID(connection, albumId);

			if (readAlbumData(request, response, connection, album)) {
				DAOAlbum.updateAlbum(connection, album);
				request.setAttribute("album", album);
				setSuccessMessage(request, "Album wurde erfolgreich editiert!");
				request.getRequestDispatcher("viewalbum.jsp").forward(request,
						response);
				return;
			}

		} catch (NumberFormatException ex) {
			// album id is no number
			setErrorMessage(request, "404: Ungültige Album-ID '"
					+ albumIdString + "'!");
			request.getRequestDispatcher("error.jsp")
					.forward(request, response);
			return;
		} catch (DatabaseObjectNotFoundException ex) {
			// no album with the given id exists
			setErrorMessage(request,
					"404: Es existiert kein Album mit der ID '" + albumIdString
							+ "'!");
			request.getRequestDispatcher("error.jsp")
					.forward(request, response);
			return;
		}
	}

	/**
	 * Reads the album data from the request
	 * 
	 * @param request the http servlet request object
	 * @param response the http servlet response object
	 * @param connection the object container
	 * @param album the album to fill with the data
	 * @return true, if reading was successful and valid, else false
	 * @throws IOException
	 * @throws ServletException
	 * @throws GeneralDatabaseException if the database is read only, closed or
	 *             a {@link com.db4o.ext.Db4oIOException Db4oIOException} is
	 *             raised
	 */
	private boolean readAlbumData(HttpServletRequest request,
			HttpServletResponse response, ObjectContainer connection,
			DBAlbum album) throws GeneralDatabaseException, ServletException,
			IOException {
		InputDataAlbum inputDataAlbum = new InputDataAlbum(album, connection);

		// use character encoding of the request
		String charEncoding = request.getCharacterEncoding();
		assert (charEncoding != null);

		// upload tracks
		FileItemFactory factory = new DiskFileItemFactory();
		ServletFileUpload upload = new ServletFileUpload(factory);
		upload.setHeaderEncoding(charEncoding);

		try {
			// upload data list
			@SuppressWarnings("unchecked")
			List<FileItem> parameters = upload.parseRequest(request);

			// iterate form parameters
			for (FileItem element : parameters) {
				setAlbumAttribute(inputDataAlbum, element, charEncoding);
			}

			inputDataAlbum.finishInput();
		} catch (FileUploadException ex) {
			setErrorMessage(request,
					"Allgemeiner Fehler beim Parsen des HTTP-Requests!");
			request.setAttribute("album", album);
			displayEditAlbum(request, response, connection);
			return false;
		} catch (DataFormatException e) {
			// e.printStackTrace();

			setInfoMessage(request,
					inputDataAlbum.getErrorMessagesAsString("\n"));
			request.setAttribute("album", album);
			displayEditAlbum(request, response, connection);
			return false;
		}
		return true;
	}

	/**
	 * Sets the album attribute to the value specified by the request parameter
	 * 
	 * @param album the album
	 * @param requestParameter the parameter list with album attributes
	 * @param charEncoding the character encoding
	 * @throws GeneralDatabaseException if the database is read only, closed or
	 *             a {@link com.db4o.ext.Db4oIOException Db4oIOException} is
	 *             raised
	 */
	private void setAlbumAttribute(InputDataAlbum album,
			FileItem requestParameter, String charEncoding)
			throws GeneralDatabaseException {
		String formFieldName = requestParameter.getFieldName();
		// requestParameter is text
		if (requestParameter.isFormField()) {
			try {
				String value = requestParameter.getString(charEncoding);

				if ("album_title".equals(formFieldName)) {
					album.setTitle(value);
				} else if ("album_interpreter".equals(formFieldName)) {
					album.setInterpreter(value);
				} else if ("album_price".equals(formFieldName)) {
					album.setPrice(value);
				} else if ("album_label".equals(formFieldName)) {
					album.setLabel(value);
				} else if ("album_stock".equals(formFieldName)) {
					album.setStock(value);
				} else if ("album_releaseyear".equals(formFieldName)) {
					album.setReleaseYear(value);
				} else if ("album_category".equals(formFieldName)) {
					album.setCategory(value);
				} else if ("album_catchwords[]".equals(formFieldName)) {
					album.addCatchword(value);
				} else if ("album_cover_delete".equals(formFieldName)) {
					album.removeCover();
				} else {
					// else: unknown field
					System.err.println("Unknown field: " + formFieldName);
				}
			} catch (UnsupportedEncodingException e) {
				// should never happen
				e.printStackTrace();
			}
		}
		// requestParameter is file
		else {

			// ignore file input field, if no filename was specified
			if ("".equals(requestParameter.getName())) {
				// System.out.println("Ignoring empty value of file request parameter: "
				// formFieldName);
				return;
			}

			// Cover
			if ("album_cover".equals(formFieldName)) {
				album.setCover(requestParameter);
			}
			// Tracks v2.0 (not in use yet)
			/*
			 * else if ("tracks[]".equals(formFieldName)) { File tempTrackFile =
			 * getTempFilePath(requestParameter);
			 * album.addTrack(requestParameter, tempTrackFile, 1); }
			 */
			// Tracks v1.0
			else {
				String trackFieldNameRegex = "cd_(\\d+)_tracks\\[\\]";
				Pattern trackFieldNamePattern = Pattern
						.compile(trackFieldNameRegex);
				Matcher trackFieldNameMatcher = trackFieldNamePattern
						.matcher(formFieldName);
				if (trackFieldNameMatcher.find()) {
					File tempTrackFile = getTempFilePath(requestParameter);
					String cdNumberString = trackFieldNameMatcher.group(1);
					int cdNumber = Integer.parseInt(cdNumberString);
					album.addTrack(requestParameter, tempTrackFile, cdNumber);
				} else {
					// else: unknown field
					System.err.println("Unknown field: " + formFieldName);
				}
			}
		}

	}

	/**
	 * Gets the temporary path of a fileitem
	 * 
	 * @param fileItem the file item
	 * @return the temporary file path of the given fileItem
	 */
	private File getTempFilePath(FileItem fileItem) {

		String remoteFileName = fileItem.getName();

		String tempFileName;
		// remoteFileName with path (as defined by opera browsers)
		if (remoteFileName.lastIndexOf("\\") >= 0) {
			tempFileName = this.tempFilePath
					+ remoteFileName
							.substring(remoteFileName.lastIndexOf("\\"));
		} else {
			// remoteFileName without a path (as defined by most of the other
			// browsers)
			tempFileName = tempFilePath + remoteFileName;
		}
		return new File(tempFileName);
	}

	/**
	 * Checks if the user (if logged in) is an admin
	 * 
	 * @param request the request object
	 * @return true, if the current user is admin, else false
	 */
	private static boolean isAdminInSession(HttpServletRequest request) {
		DBUser user = getUser(request);
		if (user != null) {
			return user.getIsAdmin();
		}
		return false;
	}

	/**
	 * Gets a list of all existing catchwords in the database
	 * 
	 * @param request the request object
	 * @param connection database connection object
	 * @return a list of all existing catchwords
	 * @throws GeneralDatabaseException if database is closed or if
	 *             {@link com.db4o.ext.Db4oIOException Db4oIOException} was
	 *             raised
	 */
	protected final static List<DBCatchword> getAllCatchwords(
			HttpServletRequest request, ObjectContainer connection)
			throws GeneralDatabaseException {
		return DAOCatchword.getAllCatchwords(connection);
	}

	/**
	 * Gets a list of all existing categories in the database
	 * 
	 * @param request the request object
	 * @param connection database connection object
	 * @return a list of all existing categories
	 * @throws GeneralDatabaseException if database is closed or if
	 *             {@link com.db4o.ext.Db4oIOException Db4oIOException} was
	 *             raised
	 */
	protected final static List<DBCategory> getAllCategories(
			HttpServletRequest request, ObjectContainer connection)
			throws GeneralDatabaseException {
		return DAOCategory.getAllCategories(connection);
	}

	/**
	 * Renames a category
	 * 
	 * @param request the http servlet request object
	 * @param response the http servlet response object
	 * @param connection the object container
	 * @throws GeneralDatabaseException if the database is read only, closed or
	 *             a {@link com.db4o.ext.Db4oIOException Db4oIOException} is
	 *             raised
	 */
	private void editCategory(HttpServletRequest request,
			HttpServletResponse response, ObjectContainer connection)
			throws GeneralDatabaseException, ServletException, IOException {
		// retrieve id and new name from the jsp
		String categoryIdString = request.getParameter("category_id");
		String categoryName = request.getParameter("category_name");

		try {
			int categoryId = Integer.parseInt(categoryIdString);

			DBCategory category = DAOCategory.getCategoryById(connection,
					categoryId);

			category.setCategoryName(categoryName);
			DAOCategory.updateCategory(connection, category);

			setSuccessMessage(request, "Kategorie erfolgreich umbenannt.");
		} catch (NumberFormatException e) {
			// should never happen
			setErrorMessage(request, "Fehler beim Parsen der Kategorie-ID");
		} catch (DatabaseObjectNotFoundException e) {
			setErrorMessage(request,
					"404: Es existiert keine Kategorie mit der ID '"
							+ categoryIdString + "'!");
		}
		request.setAttribute("category_id", null);
		displayEditCategory(request, response, connection);
	}

	/**
	 * Renames a catchword
	 * 
	 * @param request the http servlet request object
	 * @param response the http servlet response object
	 * @param connection the object container
	 * @throws GeneralDatabaseException if the database is read only, closed or
	 *             a {@link com.db4o.ext.Db4oIOException Db4oIOException} is
	 *             raised
	 */
	private void editCatchword(HttpServletRequest request,
			HttpServletResponse response, ObjectContainer connection)
			throws GeneralDatabaseException, ServletException, IOException {
		// retrieve id and new name from the jsp
		String catchwordIdString = request.getParameter("catchword_id");
		String catchwordName = request.getParameter("catchword_name");

		try {
			int catchwordId = Integer.parseInt(catchwordIdString);

			DBCatchword catchword = DAOCatchword.getCatchwordById(connection,
					catchwordId);

			catchword.setCatchwordName(catchwordName);
			DAOCatchword.updateCatchword(connection, catchword);

			setSuccessMessage(request, "Schlagwort erfolgreich umbenannt.");
		} catch (NumberFormatException e) {
			// should never happen
			setErrorMessage(request, "Fehler beim Parsen der Schlagwort-ID");
		} catch (DatabaseObjectNotFoundException e) {
			setErrorMessage(request,
					"404: Es existiert keine Schlagwort mit der ID '"
							+ catchwordIdString + "'!");
		}
		request.setAttribute("catchword_id", null);
		displayEditCatchword(request, response, connection);
	}

	/**
	 * Deletes a category from the database
	 * 
	 * @param request the http servlet request object
	 * @param response the http servlet response object
	 * @param connection the object container
	 * @throws GeneralDatabaseException if the database is read only, closed or
	 *             a {@link com.db4o.ext.Db4oIOException Db4oIOException} is
	 *             raised
	 */
	private void deleteCategory(HttpServletRequest request,
			HttpServletResponse response, ObjectContainer connection)
			throws GeneralDatabaseException, ServletException, IOException {
		String categoryIdString = request.getParameter("category_id");
		int categoryId = Integer.parseInt(categoryIdString);
		try {
			DAOCategory.deleteCategory(connection, categoryId);
			setSuccessMessage(request, "Kategory erfolgreich entfernt.");
		} catch (DatabaseObjectNotFoundException e) {
			setErrorMessage(request, "Kategory konnte nicht gefunden werden.");
		}
		displayEditCategory(request, response, connection);
	}

	/**
	 * Deletes a catchword from the database
	 * 
	 * @param request the http servlet request object
	 * @param response the http servlet response object
	 * @param connection the object container
	 * @throws GeneralDatabaseException if the database is read only, closed or
	 *             a {@link com.db4o.ext.Db4oIOException Db4oIOException} is
	 *             raised
	 */
	private void deleteCatchword(HttpServletRequest request,
			HttpServletResponse response, ObjectContainer connection)
			throws GeneralDatabaseException, ServletException, IOException {
		String catchwordIdString = request.getParameter("catchword_id");
		int catchwordId = Integer.parseInt(catchwordIdString);
		try {
			DAOCatchword.deleteCatchword(connection, catchwordId);
			setSuccessMessage(request, "Schlagwort erfolgreich entfernt.");
		} catch (DatabaseObjectNotFoundException e) {
			setErrorMessage(request, "Schlagwort konnte nicht gefunden werden.");
		}
		displayEditCatchword(request, response, connection);
	}
}

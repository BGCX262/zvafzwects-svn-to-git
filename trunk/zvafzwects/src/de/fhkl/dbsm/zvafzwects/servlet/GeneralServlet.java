package de.fhkl.dbsm.zvafzwects.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


import com.db4o.ObjectContainer;
import com.db4o.cs.Db4oClientServer;
import com.db4o.cs.config.ClientConfiguration;
import com.db4o.ext.Db4oIOException;
import com.db4odoc.clientserver.pooling.ClientConnectionFactory;
import com.db4odoc.clientserver.pooling.ConnectionPool;

import de.fhkl.dbsm.zvafzwects.model.dao.exception.GeneralDatabaseException;
import de.fhkl.dbsm.zvafzwects.model.db.DBUser;
import de.fhkl.dbsm.zvafzwects.server.Configuration;
import de.fhkl.dbsm.zvafzwects.server.ServerInfo;

/**
 * Abstract Servlet class GeneralServlet. Should be used by all other Servlets
 * in this application.
 * 
 * @author Andreas Baur, Markus Henn
 */
public abstract class GeneralServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static ConnectionPool connectionPool;

	/*
	 * (non-Javadoc)
	 * 
	 * In any case, init() is guaranteed to be called before the servlet handles
	 * its first request. Used to create the connection pool, if it's not yet
	 * existing.
	 * 
	 * @see javax.servlet.GenericServlet#init()
	 */
	@Override
	public void init() throws ServletException {
		super.init();
		// generate ConnectionPool, if it doesn't exist yet
		if (connectionPool == null) {
			connectionPool = createConnectionPool();
		}
	}

	/**
	 * 
	 * @return a newly created ConnectionPool
	 */
	private ConnectionPool createConnectionPool() {
		return new ConnectionPool(new ClientConnectionFactory() {
			@Override
			public ObjectContainer connect() {
				// Open clients for the pool
				ClientConfiguration config = Configuration
						.getClientConfiguration();
				ObjectContainer client = Db4oClientServer.openClient(config,
						ServerInfo.HOST, ServerInfo.PORT, ServerInfo.USER,
						ServerInfo.PASS);
				// initialize sequences
				Configuration.initializeDB(client);
				return client;
			}
		});
	}

	/**
	 * 
	 * @return An open connection
	 * @throws GeneralDatabaseException if the connection to the database
	 *             couldn't be established
	 */
	private ObjectContainer getConnection() throws GeneralDatabaseException {
		assert (connectionPool != null);
		try {
			return connectionPool.acquire();
		} catch (Db4oIOException ex) {
			throw new GeneralDatabaseException(ex);
		}
	}

	/**
	 * 
	 * @param request the request object
	 * @return user, who is saved in the session (can be null)
	 * @throws IllegalArgumentException if request is null
	 */
	protected final static DBUser getUser(HttpServletRequest request)
			throws IllegalArgumentException {
		return (DBUser) getSessionAttribute(request, "user");
	}

	/**
	 * Saves the user in the session
	 * 
	 * @param request the request object
	 * @param user user object which should be saved in the session or null
	 * @throws IllegalArgumentException if request is null
	 * @throws IllegalStateException if this method is called on an invalidated
	 *             session
	 */
	protected final void setUser(HttpServletRequest request, DBUser user)
			throws IllegalArgumentException, IllegalStateException {
		setSessionAttribute(request, "user", user);
	}

	/**
	 * 
	 * @param request the request object
	 * @param message the success message
	 */
	protected final static void setSuccessMessage(HttpServletRequest request,
			String message) {
		request.setAttribute("successMsg", message);
	}

	/**
	 * 
	 * @param request the request object
	 * @param message the info message
	 */
	protected final static void setInfoMessage(HttpServletRequest request,
			String message) {
		request.setAttribute("infoMsg", message);
	}

	/**
	 * 
	 * @param request the request object
	 * @param message the error message
	 */
	protected final static void setErrorMessage(HttpServletRequest request,
			String message) {
		request.setAttribute("errorMsg", message);
	}

	/**
	 * 
	 * @param request the request object
	 * @return the action parameter specified in the request object
	 * @throws IllegalArgumentException if request is null
	 */
	protected final String getRequestActionParameter(HttpServletRequest request)
			throws IllegalArgumentException {
		return request.getParameter("action");
	}

	/**
	 * Convenience method to access attributes saved in the session
	 * 
	 * @param request
	 *            the request object
	 * @param attributeName
	 *            the name of the attribute
	 * @return the value of the given attribute or null if no object is bound
	 *         under the name.
	 * @throws IllegalArgumentException
	 *             if request or attribute name is null
	 */
	protected final static Object getSessionAttribute(
			HttpServletRequest request, String attributeName)
			throws IllegalArgumentException {
		if (request == null) {
			throw new IllegalArgumentException("request must not be null");
		}
		if (attributeName == null) {
			throw new IllegalArgumentException("attributeName must not be null");
		}
		HttpSession session = request.getSession(true);
		return session.getAttribute(attributeName);
	}

	/**
	 * Sets the given attribute of the session to the given value.
	 * 
	 * @param request the request object
	 * @param attributeName attribute name
	 * @param value attribute value
	 * @throws IllegalArgumentException if request or attribute name is null
	 * @throws IllegalStateException if this method is called on an invalidated
	 *             session
	 */
	protected final void setSessionAttribute(HttpServletRequest request,
			String attributeName, Object value)
			throws IllegalArgumentException, IllegalStateException {
		if (request == null) {
			throw new IllegalArgumentException("request must not be null");
		}
		if (attributeName == null) {
			throw new IllegalArgumentException("attributeName must not be null");
		}
		HttpSession session = request.getSession(true);
		session.setAttribute(attributeName, value);
	}

	/**
	 * Sets Character Encoding of request and response to UTF-8 and calls
	 * {@link #doGet(HttpServletRequest, HttpServletResponse, ObjectContainer)},
	 * which has to be implemented by the inheriting classes.
	 * 
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	@Override
	protected final void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// set UTF-8 encoding
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");

		try {
			ObjectContainer connection = getConnection();
			doGet(request, response, connection);
			connectionPool.release(connection);
		} catch (GeneralDatabaseException ex) {
			setErrorMessage(
					request,
					"Die Funktionalität steht aufgrund von Datenbank-Problemen vorübergehend nicht zur Verfügung. Bitte versuchen Sie es später noch einmal. Wir entschuldigen uns für die entstandenen Unannehmlichkeiten.");
			request.getRequestDispatcher("error.jsp")
					.forward(request, response);
		}
	}

	/**
	 * Same as
	 * {@link HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)}
	 * , but with a database connection
	 * 
	 * @param request
	 * @param response
	 * @param connection
	 * @throws IOException
	 * @throws ServletException
	 * @throws GeneralDatabaseException
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected abstract void doGet(HttpServletRequest request,
			HttpServletResponse response, ObjectContainer connection)
			throws ServletException, IOException, GeneralDatabaseException;

	/**
	 * Sets Character Encoding of request and response to UTF-8 and calls
	 * {@link #doPost(HttpServletRequest, HttpServletResponse, ObjectContainer)}
	 * , which has to be implemented by the inheriting classes.
	 * 
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	@Override
	protected final void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// set UTF-8 encoding
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");

		try {
			ObjectContainer connection = getConnection();
			doPost(request, response, connection);
			connectionPool.release(connection);
		} catch (GeneralDatabaseException ex) {
			setErrorMessage(
					request,
					"Die Funktionalität steht aufgrund von Datenbank-Problemen vorübergehend nicht zur Verfügung. Bitte versuchen Sie es später noch einmal. Wir entschuldigen uns für die entstandenen Unannehmlichkeiten.");
			request.getRequestDispatcher("error.jsp")
					.forward(request, response);
		}
	}

	/**
	 * Same as
	 * {@link HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)}
	 * , but with a database connection
	 * 
	 * @param request
	 * @param response
	 * @param connection
	 * @throws IOException
	 * @throws ServletException
	 * @throws GeneralDatabaseException
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected abstract void doPost(HttpServletRequest request,
			HttpServletResponse response, ObjectContainer connection)
			throws ServletException, IOException, GeneralDatabaseException;

}

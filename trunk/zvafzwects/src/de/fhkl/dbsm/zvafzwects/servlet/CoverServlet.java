package de.fhkl.dbsm.zvafzwects.servlet;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import com.db4o.ObjectContainer;

import de.fhkl.dbsm.zvafzwects.model.dao.DAOCover;
import de.fhkl.dbsm.zvafzwects.model.dao.exception.DatabaseObjectNotFoundException;
import de.fhkl.dbsm.zvafzwects.model.dao.exception.GeneralDatabaseException;
import de.fhkl.dbsm.zvafzwects.model.db.DBCover;

/**
 * Servlet implementation class Cover
 * 
 * @author Markus Henn
 */
@WebServlet({ "/cover" })
public class CoverServlet extends GeneralServlet {
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

		try {
			// retrieve album ID
			String albumIdString = request.getParameter("album_id");
			int albumId = Integer.parseInt(albumIdString);

			// cover cannot be null because DAOCover.getCoverByAlbumId throws
			// exception if no matching cover was found
			DBCover cover = DAOCover.getCoverByAlbumId(connection, albumId);

			BufferedImage coverImage = cover.getCover();

			// cover exists, but has no bufferedImage?
			if (coverImage == null) {
				response.sendError(HttpServletResponse.SC_NOT_FOUND);
				return;
			}

			response.setContentType("image/png");
			ServletOutputStream out = response.getOutputStream();
			// the following standard image format plugins are always
			// present: JPEG, PNG, GIF, BMP and WBMP
			String formatName = "png";
			ImageIO.write(coverImage, formatName, out);
			out.close();

		} catch (NumberFormatException e) {
			e.printStackTrace();
			response.sendError(HttpServletResponse.SC_NOT_FOUND);
		} catch (DatabaseObjectNotFoundException e) {
			e.printStackTrace();
			response.sendError(HttpServletResponse.SC_NOT_FOUND);
		} catch (GeneralDatabaseException e) {
			e.printStackTrace();
			response.sendError(HttpServletResponse.SC_SERVICE_UNAVAILABLE);
		} catch (Exception e) {
			e.printStackTrace();
			if (!response.isCommitted()) {
				response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			}
		}

	}

	@Override
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response, ObjectContainer connection)
			throws ServletException, IOException, GeneralDatabaseException {
		// should not be used, but if, redirect to doGet stuff
		doGet(request, response, connection);
	}

}

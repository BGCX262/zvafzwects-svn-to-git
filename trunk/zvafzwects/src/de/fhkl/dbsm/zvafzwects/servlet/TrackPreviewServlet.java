package de.fhkl.dbsm.zvafzwects.servlet;

import java.io.IOException;

import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import com.db4o.ObjectContainer;

import de.fhkl.dbsm.zvafzwects.model.dao.DAOAlbum;
import de.fhkl.dbsm.zvafzwects.model.dao.exception.DatabaseObjectNotFoundException;
import de.fhkl.dbsm.zvafzwects.model.dao.exception.GeneralDatabaseException;
import de.fhkl.dbsm.zvafzwects.model.db.DBAlbum;
import de.fhkl.dbsm.zvafzwects.model.db.DBCD;
import de.fhkl.dbsm.zvafzwects.model.db.DBTrack;

/**
 * @author Christian Zoellner, Andreas Baur, Markus Henn
 */
@WebServlet({ "/mp3stream" })
public class TrackPreviewServlet extends GeneralServlet {
	private static final long serialVersionUID = 1L;

	private static final int TRACK_ACTIVATION_DEPTH = 3;

	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response,
			ObjectContainer connection) throws IOException {

		try {
			int albumId = Integer.parseInt(request.getParameter("album_id"));
			int cdNumber = Integer.parseInt(request.getParameter("cd_number"));
			int trackNumber = Integer.parseInt(request
					.getParameter("track_number"));

			DBAlbum alb = DAOAlbum.getAlbumByID(connection, albumId);
			DBCD cd = alb.getCd(cdNumber);

			DBTrack track = cd.getTrack(trackNumber);

			// now we stream the mp3
			// looks weird, but the first getMp3Track gets the DBMP3Track object
			// and the second one the byte[]
			// for that purpose we need to activate the track object
			connection.activate(track, TRACK_ACTIVATION_DEPTH);
			byte[] mp3Bytes = track.getAudioFile().getAudioFile();

			// System.out.println("mp3Track: " + track.getTitle() + " (" +
			// alb.getTitle() + " by " + alb.getInterpreter() + ")");

			// calculate length
			long duration = track.getDuration();
			// try to use a 30s long part of the file
			int wantedDuration = 30;
			// wantedBytesCount for files without a duration set
			int wantedBytesCount = 524288;
			int bytesCount = mp3Bytes.length;
			// a duration is set for the track
			if (duration > 0) {
				// if the file has more than 30 seconds, use the duration to
				// calculate the approximate size of a 30s long part
				if (duration > wantedDuration) {
					double division = (double) duration
							/ (double) wantedDuration;
					bytesCount = (int) Math.round(bytesCount / division);
				}
			} else if (bytesCount > wantedBytesCount) {
				// use wantedBytesCount for files bigger than the
				// wantedBytesCount
				bytesCount = wantedBytesCount;
			}
			// take the middle part of the file
			int offset = (mp3Bytes.length - bytesCount) / 2;

			// set response headers/parameters
			response.setContentType("audio/mpeg");
			response.addHeader("Content-Disposition", "attachment; filename="
					+ track.getTitle());
			response.setContentLength(bytesCount);
			// set a low buffer size, so the stream starts immediately
			response.setBufferSize(20);

			// output
			ServletOutputStream outputStream = response.getOutputStream();
			outputStream.write(mp3Bytes, offset, bytesCount);
			outputStream.close();

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
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
	}

	@Override
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response, ObjectContainer connection)
			throws IOException {
		// should not be used, but if, redirect to doGet stuff
		doGet(request, response, connection);
	}
}
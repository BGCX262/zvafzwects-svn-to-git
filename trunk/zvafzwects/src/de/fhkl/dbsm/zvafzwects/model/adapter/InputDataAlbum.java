/**
 * 
 */
package de.fhkl.dbsm.zvafzwects.model.adapter;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.MathContext;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.zip.DataFormatException;

import javax.imageio.ImageIO;


import org.apache.commons.fileupload.FileItem;

import com.db4o.ObjectContainer;

import de.fhkl.dbsm.zvafzwects.model.dao.DAOCatchword;
import de.fhkl.dbsm.zvafzwects.model.dao.DAOCategory;
import de.fhkl.dbsm.zvafzwects.model.dao.exception.DatabaseObjectNotFoundException;
import de.fhkl.dbsm.zvafzwects.model.dao.exception.GeneralDatabaseException;
import de.fhkl.dbsm.zvafzwects.model.db.DBAlbum;
import de.fhkl.dbsm.zvafzwects.model.db.DBAudioFile;
import de.fhkl.dbsm.zvafzwects.model.db.DBCD;
import de.fhkl.dbsm.zvafzwects.model.db.DBCatchword;
import de.fhkl.dbsm.zvafzwects.model.db.DBCategory;
import de.fhkl.dbsm.zvafzwects.model.db.DBCover;
import de.fhkl.dbsm.zvafzwects.model.db.DBTrack;
import de.vdheide.mp3.FrameDamagedException;
import de.vdheide.mp3.ID3;
import de.vdheide.mp3.ID3v2DecompressionException;
import de.vdheide.mp3.ID3v2IllegalVersionException;
import de.vdheide.mp3.ID3v2WrongCRCException;
import de.vdheide.mp3.MP3File;
import de.vdheide.mp3.MP3Properties;
import de.vdheide.mp3.NoID3TagException;
import de.vdheide.mp3.NoMP3FrameException;

/**
 * Adapter used to validate input data for the album and convert them into an
 * DBAlbum object if possible
 * 
 * @author Markus Henn
 * 
 */
public class InputDataAlbum {
	// save messages of occuring errors in this list
	private final List<String> errorMessages = new ArrayList<String>();
	private final Set<DBCatchword> temporaryCatchwords = new HashSet<DBCatchword>();
	private final List<InputDataTrack> temporaryTracks = new ArrayList<InputDataTrack>();
	private final Map<Integer, Integer> cdTrackCount = new HashMap<Integer, Integer>();

	private final DBAlbum dbAlbum;
	private final ObjectContainer connection;

	/**
	 * Creates a new InputDataAlbum
	 * 
	 * @param album
	 *            the album object which should be updated (or a new one), but
	 *            has to be defined (!= null)
	 * @param connection
	 *            the object container
	 * @throws IllegalArgumentException
	 *             if album is null
	 */
	public InputDataAlbum(final DBAlbum album, final ObjectContainer connection)
			throws IllegalArgumentException {
		if (album == null) {
			throw new IllegalArgumentException("album must not be null");
		}
		this.dbAlbum = album;
		this.connection = connection;
	}

	/**
	 * Adds the Catchword with the given ID to the album
	 * 
	 * @param catchwordIdString
	 *            the catchword ID
	 * @throws GeneralDatabaseException
	 *             if database is closed or if Db4oIOException was raised
	 */
	public void addCatchword(String catchwordIdString)
			throws GeneralDatabaseException {
		try {
			int catchwordId = Integer.parseInt(catchwordIdString);
			DBCatchword catchword = DAOCatchword.getCatchwordById(
					this.connection, catchwordId);
			temporaryCatchwords.add(catchword);
		} catch (NumberFormatException ex) {
			// should not happen
			ex.printStackTrace();
			errorMessages.add("Die angegebene Schlagwort-ID '"
					+ catchwordIdString + "' ist keine gültige Zahl!");
		} catch (DatabaseObjectNotFoundException ex) {
			errorMessages.add("Das ausgewählte Schlagwort mit der ID '"
					+ catchwordIdString + "' ist nicht mehr verfügbar!");
		}

	}

	/**
	 * Returns the Error Messages as on String, uses given messageSeparator to
	 * concatenate the messages.
	 * 
	 * @param messageSeparator
	 * @return the List of error Messages as one string, concatenated by
	 *         messageSeparator
	 */
	public String getErrorMessagesAsString(String messageSeparator) {
		StringBuilder errorMessageStringBuilder = new StringBuilder();
		for (String errorMessage : errorMessages) {
			errorMessageStringBuilder.append(errorMessage);
			if (errorMessages.iterator().hasNext()) {
				errorMessageStringBuilder.append(messageSeparator);
			}
		}
		return errorMessageStringBuilder.toString();
	}

	/**
	 * finishes the input phase, if input was valid
	 * 
	 * @throws DataFormatException
	 *             if the input specified before was not completely valid
	 */
	public void finishInput() throws DataFormatException {
		if (!isValid()) {
			throw new DataFormatException();
		}
		this.dbAlbum.setCatchwords(temporaryCatchwords);
		transferTemporaryTracksToAlbum();
	}

	/**
	 * 
	 * @return true, if input was valid, else false
	 */
	private boolean isValid() {
		checkIfDefined("Titel", dbAlbum.getTitle());
		checkIfDefined("Interpret", dbAlbum.getInterpreter());
		return errorMessages.isEmpty();
	}

	/**
	 * Checks if the specified value is not null and not an empty String, else
	 * adds an error message.
	 * 
	 * @param attributeName
	 * @param attributeValue
	 */
	private void checkIfDefined(String attributeName, String attributeValue) {
		if (attributeValue == null || "".equals(attributeValue)) {
			errorMessages.add(attributeName + " muss angegeben werden!");
		}
	}

	/**
	 * Transfers the temporary saved tracks in the DBAlbum object
	 */
	private void transferTemporaryTracksToAlbum() {
		int cdNumber = 0;
		DBCD cd = null;
		for (InputDataTrack inputDataTrack : temporaryTracks) {
			assert (inputDataTrack.getTrackNumber() > 0);
			assert (inputDataTrack.getTrackNumber() >= cdNumber);

			if (inputDataTrack.getCdNumber() > cdNumber) {
				cdNumber++;
				cd = new DBCD(dbAlbum, cdNumber);
			}
			DBAudioFile audioFile = new DBAudioFile(
					inputDataTrack.getTrackBytes());
			new DBTrack(cd, inputDataTrack.getTrackNumber(),
					inputDataTrack.getTitle(), inputDataTrack.getDuration(),
					audioFile);
		}
	}

	/**
	 * 
	 * @param title
	 */
	public void setTitle(String title) {
		this.dbAlbum.setTitle(title);
	}

	/**
	 * 
	 * @param interpreter
	 */
	public void setInterpreter(String interpreter) {
		this.dbAlbum.setInterpreter(interpreter);
	}

	/**
	 * 
	 * @param label
	 */
	public void setLabel(String label) {
		this.dbAlbum.setLabel(label);
	}

	/**
	 * 
	 * @param releaseYearString
	 */
	public void setReleaseYear(String releaseYearString) {
		int releaseYear;
		try {
			if ("".equals(releaseYearString)) {
				releaseYear = 0;
			} else {
				releaseYear = Integer.parseInt(releaseYearString);
			}
			this.dbAlbum.setReleaseYear(releaseYear);
		} catch (NumberFormatException ex) {
			/*
			errorMessages.add("Das angegebene Erscheinungsjahr '"
					+ releaseYearString + "' ist keine gültige Zahl!");
			return;
			*/
		}
	}

	/**
	 * 
	 * @param priceString
	 */
	public void setPrice(String priceString) {
		priceString = priceString.replace(',', '.');
		try {
			BigDecimal price = new BigDecimal(priceString,
					MathContext.DECIMAL32);
			this.dbAlbum.setPrice(price);
		} catch (NumberFormatException ex) {
			/*
			errorMessages
					.add("Der angegebene Preis '"
							+ priceString
							+ "' hat keinen gültigen Wert! Beispiel eines gültigen Werts: '19,99'.");
			*/
		}
	}

	/**
	 * 
	 * @param stockString
	 */
	public void setStock(String stockString) {
		try {
			int stock = Integer.parseInt(stockString);
			this.dbAlbum.setStock(stock);
		} catch (NumberFormatException ex) {
			/*
			errorMessages.add("Die angegebene Stückzahl auf Lager '"
					+ stockString + "' ist keine gültige Zahl!");
			*/
		}
	}

	/**
	 * 
	 * @param categoryIdString
	 * @throws GeneralDatabaseException
	 *             if database is closed or if Db4oIOException was raised
	 */
	public void setCategory(String categoryIdString)
			throws GeneralDatabaseException {
		if ("".equals(categoryIdString)) {
			this.dbAlbum.setCategory(null);
			return;
		}
		try {
			int categoryId = Integer.parseInt(categoryIdString);
			DBCategory category = DAOCategory.getCategoryById(this.connection,
					categoryId);
			this.dbAlbum.setCategory(category);
		} catch (NumberFormatException ex) {
			// should not happen
			ex.printStackTrace();
			errorMessages.add("Die angegebene Kategorie-ID '"
					+ categoryIdString + "' ist keine gültige Zahl!");
		} catch (DatabaseObjectNotFoundException ex) {
			errorMessages.add("Die ausgewählte Kategorie mit der ID '"
					+ categoryIdString + "' ist nicht mehr verfügbar!");
		}
	}

	/**
	 * 
	 * @param coverFileItem
	 */
	public void setCover(FileItem coverFileItem) {
		if (coverFileItem == null) {
			throw new IllegalArgumentException("coverFileItem must not be null");
		}
		assert (!"".equals(coverFileItem.getName()));

		InputStream coverInputStream = null;
		try {
			coverInputStream = coverFileItem.getInputStream();
			setCover(coverInputStream);
		} catch (IOException e) {
			e.printStackTrace();
			errorMessages.add("Das Cover '" + coverFileItem.getName()
					+ "' konnte nicht gelesen werden!");
		} finally {
			if (coverInputStream != null) {
				try {
					coverInputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * 
	 * @param coverInputStream
	 * @throws IOException
	 *             if an error occurs during reading
	 */
	private void setCover(InputStream coverInputStream) throws IOException {
		BufferedImage coverImage = ImageIO.read(coverInputStream);
		DBCover cover = new DBCover(coverImage);
		this.dbAlbum.setCover(cover);
	}

	/**
	 * removes the cover
	 */
	public void removeCover() {
		this.dbAlbum.setCover(null);
	}

	/**
	 * adds the given track from the trackFileItem and uses the location
	 * tempTrackFile to work (write/read) with the given file.
	 * 
	 * @param trackFileItem
	 * @param tempTrackFile
	 * @param cdNumber
	 */
	public void addTrack(FileItem trackFileItem, File tempTrackFile,
			int cdNumber) {
		if (trackFileItem == null) {
			throw new IllegalArgumentException("trackFileItem must not be null");
		}
		assert (!"".equals(trackFileItem.getName()));

		try {
			byte[] trackBytes = trackFileItem.get();
			InputDataTrack inputDataTrack = new InputDataTrack(cdNumber,
					increaseCdTrackCount(cdNumber), trackFileItem.getName(), 0,
					trackBytes);
			// read missing album info (artist, title, release year) from first
			// track's id3 tags (first track, if temporaryTrack list is empty)
			readMp3Info(inputDataTrack, trackFileItem, tempTrackFile,
					temporaryTracks.isEmpty());

			temporaryTracks.add(inputDataTrack);
		} catch (Exception e) {
			e.printStackTrace();
			errorMessages.add("Der Track '" + trackFileItem.getName()
					+ "' konnte nicht gelesen werden!");
		}
	}

	/**
	 * Increases the count of tracks of the CD with the given cdNumber and
	 * returns it
	 * 
	 * @param cdNumber
	 * @return the increased count of tracks of the given CD
	 */
	private int increaseCdTrackCount(int cdNumber) {
		int newValue = getCdTrackCount(cdNumber) + 1;
		cdTrackCount.put(cdNumber, newValue);
		return newValue;
	}

	/**
	 * 
	 * @param cdNumber
	 * @return the current count of tracks in the CD with the given cdNumber
	 */
	private int getCdTrackCount(int cdNumber) {
		if (!cdTrackCount.containsKey(cdNumber)) {
			return 0;
		}
		return cdTrackCount.get(cdNumber);
	}

	/**
	 * Reads the mp3 id3 tag (title, duration) from the given track and saves
	 * the value in the inputDataTrack. If readAlbumInfo is true, and some album
	 * info (artist, title, release year, label) is not defined, it reads it
	 * from the mp3 id3 tag, too.
	 * 
	 * @param inputDataTrack
	 * @param trackFileItem
	 * @param tempTrackFile
	 *            possible temporary location to save the mp3 file to
	 * @param readAlbumInfo
	 *            true, if you want to fill out missing album info (artist,
	 *            title, release year) by the id3 tags
	 * @throws Exception
	 *             if an error occurs while trying to write the track to the
	 *             tempTrackFile location
	 */
	private void readMp3Info(InputDataTrack inputDataTrack,
			FileItem trackFileItem, File tempTrackFile, boolean readAlbumInfo)
			throws Exception {
		// tempTrackFile is just a description of a possible place to save the
		// file. The file is not yet existing there! Write it there!
		trackFileItem.write(tempTrackFile);
		try {
			// Use MP3Properties instead of MP3File, because initializing
			// MP3File also fails, if something is wrong with the id3(v2) tags
			MP3Properties mp3props = new MP3Properties(tempTrackFile);

			// set track duration
			inputDataTrack.setDuration(mp3props.getLength());

			try {
				MP3File mp3file = new MP3File(tempTrackFile.getAbsolutePath());

				// Title
				String titleString = mp3file.getTitle().getTextContent();
				System.out.println("Title: " + titleString);
				inputDataTrack.setTitle(titleString);

				// Track Number
				// We don't trust the id3 tags yet, so we don't use it for track
				// number and cd number
				/*
				String trackString = mp3file.getTrack().getTextContent();
				System.out.println("Track: " + trackString);
				*/
				// trackString format: <trackNumber>/<totalNumberOfTracks>, e.g.
				// "3/14"

				/*
				inputDataTrack.setTrackNumber(Integer.parseInt(trackString.substring(0, trackString.indexOf('/'))));
				*/

				// CD Number
				// We don't trust the id3 tags yet, so we don't use it for track
				// number and cd number
				/*
				String cdString = mp3file.getPartOfSet().getTextContent();
				System.out.println("CD: " + cdString);
				*/
				// cdString format: <cdNumber>/<totalNumberOfCDs>, e.g.
				// "3/14"
				/*
				inputDataTrack.setCdNumber(Integer.parseInt(cdString.substring(0, cdString.indexOf('/'))));
				*/

				// read album info if wanted
				if (readAlbumInfo) {
					// Album
					if (isNotDefined(dbAlbum.getTitle())) {
						String albumString = mp3file.getAlbum()
								.getTextContent();
						System.out.println("Album: " + albumString);
						dbAlbum.setTitle(albumString);
					}

					// Interpreter (Artist)
					if (isNotDefined(dbAlbum.getInterpreter())) {
						String artistString = mp3file.getArtist()
								.getTextContent();
						System.out.println("Artist: " + artistString);
						dbAlbum.setInterpreter(artistString);
					}
					
					// Release Year
					if (dbAlbum.getReleaseYear() == 0) {
						String releaseYearString = mp3file.getYear()
								.getTextContent();
						System.out.println("ReleaseYear: " + releaseYearString);
						try {
							dbAlbum.setReleaseYear(Integer.parseInt(releaseYearString));
						} catch (NumberFormatException ex) {
							System.out.println("Couldn't read ReleaseYear: "
									+ ex.getClass() + "(" + ex.getMessage()
									+ ")");
						}
					}
					
					// Publisher
					if (isNotDefined(dbAlbum.getLabel())) {
						String labelString = mp3file.getPublisher()
								.getTextContent();
						System.out.println("Publisher: " + labelString);
						dbAlbum.setLabel(labelString);
					}
					
					// Cover
					if (dbAlbum.getCover() == null) {
						byte[] coverBytes = mp3file.getPicture()
								.getBinaryContent();
						System.out.println("Cover: " + coverBytes);
						if (coverBytes != null) {
							setCover(new ByteArrayInputStream(coverBytes));
						}
					}
				}

				// fall back to ID3v1 tag info when ID3v2 exceptions occur
			} catch (ID3v2WrongCRCException e) {
				// System.out.println(e.getClass() + " occured (" +
				// e.getMessage() + "). Falling back to ID3v1...");
				readId3v1TagInfo(inputDataTrack, tempTrackFile, readAlbumInfo);
			} catch (ID3v2DecompressionException e) {
				// System.out.println("ID3v2DecompressionException occured (" +
				// e.getMessage() + "). Falling back to ID3v1...");
				readId3v1TagInfo(inputDataTrack, tempTrackFile, readAlbumInfo);
			} catch (ID3v2IllegalVersionException e) {
				// System.out.println(e.getClass() + " occured (" +
				// e.getMessage() + "). Falling back to ID3v1...");
				readId3v1TagInfo(inputDataTrack, tempTrackFile, readAlbumInfo);
			} catch (FrameDamagedException e) {
				// System.out.println("FrameDamagedException occured (" +
				// e.getMessage() + "). Falling back to ID3v1...");
				readId3v1TagInfo(inputDataTrack, tempTrackFile, readAlbumInfo);
			}

		} catch (IOException e) {
			e.printStackTrace();
		} catch (NoMP3FrameException e) {
			e.printStackTrace();
		}
	}

	/**
	 * just like {@link #readMp3Info(InputDataTrack, FileItem, File, boolean)},
	 * but only for ID3v1 tags.
	 * 
	 * @param inputDataTrack
	 * @param tempTrackFile
	 * @param readAlbumInfo
	 */
	private void readId3v1TagInfo(InputDataTrack inputDataTrack,
			File tempTrackFile, boolean readAlbumInfo) {
		ID3 id3 = new ID3(tempTrackFile);

		try {
			// Title
			inputDataTrack.setTitle(id3.getTitle());

			// Track Number
			System.out.println(id3.getTrack());
			// We don't trust the id3 tags yet, so we don't use it for track
			// number
			/*
			inputDataTrack.setTrackNumber(id3.getTrack());
			*/

			if (readAlbumInfo) {
				// Album
				if (isNotDefined(dbAlbum.getTitle())) {
					dbAlbum.setTitle(id3.getAlbum());
				}

				// Interpreter (Artist)
				if (isNotDefined(dbAlbum.getInterpreter())) {
					dbAlbum.setInterpreter(id3.getArtist());
				}

				// Release Year
				if (dbAlbum.getReleaseYear() == 0) {
					try {
						dbAlbum.setReleaseYear(Integer.parseInt(id3.getYear()));
					} catch (NumberFormatException ex) {
						System.out.println("Couldn't read ReleaseYear: "
								+ ex.getClass() + "(" + ex.getMessage() + ")");
					}
				}
			}

		} catch (NoID3TagException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * @param value
	 * @return true if the given value is not null and not an empty String (""),
	 *         else false.
	 */
	private boolean isNotDefined(String value) {
		return ((value == null) || "".equals(value));
	}
}

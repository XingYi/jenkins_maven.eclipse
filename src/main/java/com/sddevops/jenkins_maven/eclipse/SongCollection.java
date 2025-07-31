package com.sddevops.jenkins_maven.eclipse;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.json.JSONObject;

public class SongCollection {

	private static final Logger logger = Logger.getLogger(SongCollection.class.getName());
	private ArrayList<Song> songs = new ArrayList<>();
	private int capacity;

	public SongCollection() {
		this.capacity = 20;
	}

	public SongCollection(int capacity) {
		this.capacity = capacity;
	}

	public List<Song> getSongs() {
		return songs;
	}

	public void addSong(Song song) {
		if (songs.size() != capacity) {
			songs.add(song);
		}
	}

	public List<Song> sortSongsByTitle() {
		Collections.sort(songs, Song.titleComparator);
		return songs;
	}

	public List<Song> sortSongsBySongLength() {
		Collections.sort(songs, Song.songLengthComparator);
		return songs;
	}

	public Song findSongsById(String id) {
		for (Song s : songs) {
			if (s.getId().equals(id))
				return s;
		}
		return null;
	}

	public Song findSongByTitle(String title) {
		for (Song s : songs) {
			if (s.getTitle().equals(title))
				return s;
		}
		return null;
	}

//	protected String fetchSongJson() {
//		String urlString = "https://mocki.io/v1/36c94419-b141-4cfd-96fa-327f4872aca6";
//		try {
//			URL url = new URL(urlString); // Not actually deprecated in Java 8
//			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//			conn.setRequestMethod("GET");
//			if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
//				BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
//				StringBuilder response = new StringBuilder();
//				String inputLine;
//				while ((inputLine = in.readLine()) != null) {
//					response.append(inputLine);
//				}
//				in.close();
//				return response.toString();
//			}
//		} catch (Exception e) {
//			logger.log(Level.SEVERE, "An error occurred", e);
//		}
//		return null;
//	}

	protected String fetchSongJson() {
	    String urlString = "https://mocki.io/v1/36c94419-b141-4cfd-96fa-327f4872aca6";
	    HttpURLConnection conn = null;

	    try {
	        URL url = new URL(urlString);
	        conn = (HttpURLConnection) url.openConnection();
	        conn.setRequestMethod("GET");

	        // Set a User-Agent header (some APIs require this)
//	        conn.setRequestProperty("User-Agent", "Java-HttpClient");

	        int responseCode = conn.getResponseCode();
	        logger.info("HTTP response code: " + responseCode);

	        if (responseCode == HttpURLConnection.HTTP_OK) {
	            try (BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
	                StringBuilder response = new StringBuilder();
	                String inputLine;
	                while ((inputLine = in.readLine()) != null) {
	                    response.append(inputLine);
	                }
	                logger.info("API response: " + response);
	                return response.toString();
	            }
	        } else {
	            // Read error stream and log it
	            try (BufferedReader errReader = new BufferedReader(new InputStreamReader(conn.getErrorStream()))) {
	                StringBuilder errorResponse = new StringBuilder();
	                String line;
	                while ((line = errReader.readLine()) != null) {
	                    errorResponse.append(line);
	                }
	                logger.warning("API returned error: " + errorResponse);
	            }
	        }
	    } catch (Exception e) {
	        logger.log(Level.SEVERE, "Exception while fetching song JSON", e);
	    } finally {
	        if (conn != null) {
	            conn.disconnect();
	        }
	    }

	    return null;
	}

	
	public Song fetchSongOfTheDay() {
		try {
			String jsonStr = fetchSongJson();
			if (jsonStr == null)
				return null;

			JSONObject json = new JSONObject(jsonStr);
			Song song = new Song(json.getString("id"), json.getString("title"), json.getString("artiste"),
					json.getDouble("songLength"));

			if (song.getArtiste().equals("Taylor Swift")) {
				song.setArtiste("TS");
				this.addSong(song);
			} else if (song.getArtiste().equals("Bruno Mars")) {
				this.addSong(song);
			}

			return song;
		} catch (Exception e) {
			//logger.log(Level.SEVERE, "An error occurred", e); #NOTE: Cause built failure in Jenkins
			return null;
		}
	}
}

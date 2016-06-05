package com.edavtyan.materialplayer.app.models.artist;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.content.CursorLoader;

public class ArtistsProvider {
	/*
	 * Fields
	 */

	private final Context context;

	/*
	 * Constants
	 */

	private static final Uri URI = MediaStore.Audio.Artists.EXTERNAL_CONTENT_URI;
	private static final String[] PROJECTION = {
			MediaStore.Audio.Artists._ID,
			MediaStore.Audio.Artists.ARTIST,
			MediaStore.Audio.Artists.NUMBER_OF_ALBUMS,
			MediaStore.Audio.Artists.NUMBER_OF_TRACKS,
	};

	private static final int COLUMN_ID = 0;
	private static final int COLUMN_TITLE = 1;
	private static final int COLUMN_ALBUMS_COUNT = 2;
	private static final int COLUMN_TRACKS_COUNT = 3;

	/*
	 * Constructors
	 */

	public ArtistsProvider(Context context) {
		this.context = context;
	}

	/*
	 * Public methods
	 */

	public int getId(Cursor cursor) {
		return cursor.getInt(COLUMN_ID);
	}

	public String getTitle(Cursor cursor) {
		return cursor.getString(COLUMN_TITLE);
	}

	public int getAlbumsCount(Cursor cursor) {
		return cursor.getInt(COLUMN_ALBUMS_COUNT);
	}

	public int getTracksCount(Cursor cursor) {
		return cursor.getInt(COLUMN_TRACKS_COUNT);
	}

	//--

	public CursorLoader getAllArtistsLoader() {
		return new CursorLoader(
				context,
				URI,
				PROJECTION,
				null, null, null);
	}

	//--

	public Artist getArtistFromTitle(String title) {
		Cursor cursor = null;

		try {
			cursor = context.getContentResolver().query(
					URI, PROJECTION,
					MediaStore.Audio.Artists.ARTIST + "='" + title + "'",
					null, null);
			cursor.moveToFirst();

			Artist artist = new Artist();
			artist.setTitle(getTitle(cursor));
			artist.setAlbumsCount(getAlbumsCount(cursor));
			artist.setTracksCount(getTracksCount(cursor));
			return artist;
		} finally {
			if (cursor != null) cursor.close();
		}
	}
}

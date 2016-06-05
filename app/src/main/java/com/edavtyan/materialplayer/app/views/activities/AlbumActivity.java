package com.edavtyan.materialplayer.app.views.activities;

import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;

import com.bumptech.glide.DrawableRequestBuilder;
import com.bumptech.glide.Glide;
import com.edavtyan.materialplayer.app.R;
import com.edavtyan.materialplayer.app.adapters.AlbumTracksAdapter;
import com.edavtyan.materialplayer.app.adapters.TracksAdapter;
import com.edavtyan.materialplayer.app.lib.activities.CollapsingHeaderListActivity;
import com.edavtyan.materialplayer.app.lib.adapters.RecyclerViewCursorAdapter;
import com.edavtyan.materialplayer.app.models.columns.TrackColumns;
import com.edavtyan.materialplayer.app.models.data.Album;
import com.edavtyan.materialplayer.app.models.data.Track;
import com.edavtyan.materialplayer.app.models.providers.ArtProvider;
import com.edavtyan.materialplayer.app.models.providers.TracksProvider;

import java.io.File;

public class AlbumActivity extends CollapsingHeaderListActivity {
	public static final String EXTRA_ALBUM_ID = "album_id";

	private TracksAdapter tracksAdapter;
	private TracksProvider tracksProvider;

	/*
	 * AsyncTasks
	 */

	private class ImageLoadTask extends AsyncTask<Integer, Void, File> {
		@Override
		protected File doInBackground(Integer... albumIds) {
			Track track = tracksProvider.firstWithAlbumId(albumIds[0]);
			return ArtProvider.fromTrack(track);
		}

		@Override
		protected void onPostExecute(File artFile) {
			DrawableRequestBuilder artRequest = Glide.with(AlbumActivity.this)
					.load(artFile)
					.error(R.drawable.fallback_cover);

			artRequest.into(imageBackView);
			artRequest.into(imageView);
		}
	}

	/*
	 * CollapsingHeaderListActivity
	 */

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		tracksAdapter = new AlbumTracksAdapter(this, null);
		tracksProvider = new TracksProvider(this);
		super.onCreate(savedInstanceState);
		initToolbar(R.string.app_name);

		Album album = Album.fromId(getIntent().getIntExtra(EXTRA_ALBUM_ID, -1), this);

		titleView.setText(album.getTitle());
		String tracksCount = getResources().getQuantityString(
				R.plurals.tracks, album.getTracksCount(), album.getTracksCount());
		String albumInfo = getResources().getString(
				R.string.pattern_album_info, album.getArtistTitle(), tracksCount);
		infoView.setText(albumInfo);

		new ImageLoadTask().execute(album.getId());
	}

	@Override
	protected void onResume() {
		super.onResume();
		tracksAdapter.bindService();
	}

	@Override
	protected void onPause() {
		super.onPause();
		tracksAdapter.unbindService();
	}

	@Override
	public Loader<Cursor> getLoader() {
		int albumId = getIntent().getIntExtra(EXTRA_ALBUM_ID, 0);
		return new CursorLoader(
				this,
				TrackColumns.URI,
				TrackColumns.PROJECTION,
				TrackColumns.NAME_ALBUM_ID + "=" + albumId,
				null,
				TrackColumns.NAME_TRACK + " ASC");
	}

	@Override
	public RecyclerViewCursorAdapter getAdapter() {
		return tracksAdapter;
	}
}
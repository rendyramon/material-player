package com.edavtyan.materialplayer.components.albums.views.list;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.edavtyan.materialplayer.R;
import com.edavtyan.materialplayer.components.albums.models.Album;
import com.edavtyan.materialplayer.components.albums.models.AlbumDB;
import com.edavtyan.materialplayer.components.albums.views.detail.AlbumDetailActivity;
import com.edavtyan.materialplayer.components.tracks.Track;
import com.edavtyan.materialplayer.components.tracks.TracksProvider;
import com.edavtyan.materialplayer.lib.adapters.RecyclerServiceCursorAdapter;

import java.util.List;

public class AlbumsListAdapter extends RecyclerServiceCursorAdapter<AlbumsListViewHolder> {
	private final AlbumDB albumDB;
	private final TracksProvider tracksProvider;

	//---

	public AlbumsListAdapter(Context context, AlbumDB albumDB) {
		super(context, null);
		this.albumDB = albumDB;
		tracksProvider = new TracksProvider(context);
	}

	//---

	@Override
	public AlbumsListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		View view = LayoutInflater.from(context).inflate(R.layout.listitem_album, parent, false);
		AlbumsListViewHolder holder = new AlbumsListViewHolder(context, view);

		holder.setOnClickListener(itemView -> {
			Album album = albumDB.getAlbum(holder.getAdapterPosition());
			AlbumDetailActivity.startActivity(context, album.getId());
		});

		holder.setOnMenuItemClickListener(item -> {
			switch (item.getItemId()) {
			case R.id.menu_addToPlaylist:
				Album album = albumDB.getAlbum(holder.getAdapterPosition());
				List<Track> tracks = tracksProvider.getAllTracksWithAlbumId(album.getId());
				service.getQueue().addAll(tracks);

			default:
				return false;
			}
		});

		return holder;
	}

	@Override
	public void onBindViewHolder(AlbumsListViewHolder holder, int position) {
		super.onBindViewHolder(holder, position);
		Album album = albumDB.getAlbum(position);
		holder.setTitle(album.getTitle());
		holder.setInfo(album.getTracksCount(), album.getArtistTitle());
		holder.setArt(album.getArt());
	}

	@Override
	public void swapCursor(Cursor newCursor) {
		super.swapCursor(newCursor);
		albumDB.swapCursor(newCursor);
	}
}

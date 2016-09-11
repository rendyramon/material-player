package com.edavtyan.materialplayer.components.artist_detail;

import android.content.Context;

import com.edavtyan.materialplayer.components.album_all.AlbumListAdapter;
import com.edavtyan.materialplayer.db.AlbumDB;
import com.edavtyan.materialplayer.db.ArtistDB;
import com.edavtyan.materialplayer.db.TrackDB;
import com.edavtyan.materialplayer.lib.base.BaseFactory;

public class ArtistDetailFactory extends BaseFactory {
	private final Context context;
	private final String artistTitle;
	private ArtistDetailMvp.Model model;
	private ArtistDetailMvp.View view;
	private ArtistDetailMvp.Presenter presenter;
	private AlbumListAdapter adapter;

	public ArtistDetailFactory(Context context, ArtistDetailMvp.View view, String artistTitle) {
		super(context);
		this.context = context;
		this.view = view;
		this.artistTitle = artistTitle;
	}

	public ArtistDetailMvp.Model provideModel() {
		if (model == null) {
			AlbumDB albumDB = new AlbumDB(context);
			TrackDB trackDB = new TrackDB(context);
			ArtistDB artistDB = new ArtistDB(context);
			model = new ArtistDetailModel(context, artistDB, albumDB, trackDB, artistTitle);
		}

		return model;
	}

	public ArtistDetailMvp.View provideView() {
		return view;
	}

	public ArtistDetailMvp.Presenter providePresenter() {
		if (presenter == null) presenter = new ArtistDetailPresenter(provideModel(), provideView());
		return presenter;
	}

	public AlbumListAdapter provideAdapter() {
		if (adapter == null) adapter = new AlbumListAdapter(context, providePresenter());
		return adapter;
	}
}
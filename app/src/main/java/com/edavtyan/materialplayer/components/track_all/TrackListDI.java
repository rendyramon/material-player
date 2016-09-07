package com.edavtyan.materialplayer.components.track_all;

import android.content.Context;

import com.edavtyan.materialplayer.db.TrackDB;
import com.edavtyan.materialplayer.lib.base.BaseDI;

public class TrackListDI extends BaseDI {
	private final Context context;
	private final TrackListMvp.View view;
	private TrackListModel model;
	private TrackDB trackDB;
	private TrackListPresenter presenter;
	private TrackListAdapter adapter;

	public TrackListDI(Context context, TrackListMvp.View view) {
		super(context);
		this.context = context;
		this.view = view;
	}

	public TrackListMvp.Model provideModel() {
		if (model == null) model = new TrackListModel(context, provideTrackDB());
		return model;
	}

	public TrackDB provideTrackDB() {
		if (trackDB == null) trackDB = new TrackDB(context);
		return trackDB;
	}

	public TrackListMvp.View provideView() {
		return view;
	}

	public TrackListMvp.Presenter providePresenter() {
		if (presenter == null) presenter = new TrackListPresenter(provideView(), provideModel());
		return presenter;
	}

	public TrackListAdapter provideAdapter() {
		if (adapter == null) adapter = new TrackListAdapter(context, providePresenter());
		return adapter;
	}
}

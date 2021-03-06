package com.edavtyan.materialplayer.components.tracks;

import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.edavtyan.materialplayer.R;
import com.edavtyan.materialplayer.lib.decorators.DividerItemDecoration;
import com.edavtyan.materialplayer.utils.AppColors;

public class TracksListFragment extends Fragment
		implements LoaderManager.LoaderCallbacks<Cursor> {
	private TracksListAdapter tracksAdapter;
	private TrackDB trackDB;
	private AppColors appColors;

	/*
	 * Fragment
	 */

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		trackDB = new TrackDB(getActivity());
		tracksAdapter = new TracksListAdapter(getActivity(), trackDB);
	}

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater,
	                         @Nullable ViewGroup container,
	                         @Nullable Bundle savedInstanceState) {
		appColors = new AppColors(getActivity());

		View view = inflater.inflate(R.layout.fragment_list, container, false);

		RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.list);
		recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
		recyclerView.setAdapter(tracksAdapter);
		recyclerView.addItemDecoration(new DividerItemDecoration(appColors.divider));

		return view;
	}

	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		getLoaderManager().initLoader(0, null, this);
	}

	@Override
	public void onResume() {
		super.onResume();
		tracksAdapter.bindService();
	}

	@Override
	public void onPause() {
		super.onPause();
		tracksAdapter.unbindService();
	}

	/*
	 * LoaderCallbacks
	 */

	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle args) {
		return trackDB.getAllTracksLoader();
	}

	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
		tracksAdapter.swapCursor(data);
	}

	@Override
	public void onLoaderReset(Loader<Cursor> loader) {
		tracksAdapter.swapCursor(null);
	}
}

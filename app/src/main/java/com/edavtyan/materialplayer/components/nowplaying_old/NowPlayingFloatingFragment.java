package com.edavtyan.materialplayer.components.nowplaying_old;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.edavtyan.materialplayer.MusicPlayerService;
import com.edavtyan.materialplayer.R;
import com.edavtyan.materialplayer.components.nowplaying.NowPlayingActivity;
import com.edavtyan.materialplayer.components.tracks.Track;
import com.edavtyan.materialplayer.lib.fragments.ServiceFragment;
import com.edavtyan.materialplayer.utils.ArtProvider;
import com.wnafee.vector.MorphButton;

public class NowPlayingFloatingFragment
		extends ServiceFragment
		implements View.OnClickListener {
	private ImageView artView;
	private TextView titleView;
	private TextView infoView;
	private MorphButton controlView;
	private LinearLayout container;

	/*
	 * BroadcastReceivers
	 */

	private final BroadcastReceiver playReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			controlView.setState(MorphButton.MorphState.END, true);
		}
	};
	private final BroadcastReceiver pauseReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			controlView.setState(MorphButton.MorphState.START, true);
		}
	};
	private final BroadcastReceiver newTrackReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			syncDataWithService();
		}
	};

	/*
	 * Fragment
	 */

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup parent,
	                         Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_nowplaying_floating, parent, false);

		LinearLayout infoWrapper = (LinearLayout) view.findViewById(R.id.info_wrapper);
		infoWrapper.setOnClickListener(this);

		artView = (ImageView) view.findViewById(R.id.art);
		artView.setOnClickListener(this);

		titleView = (TextView) view.findViewById(R.id.title);
		titleView.setOnClickListener(this);

		infoView = (TextView) view.findViewById(R.id.info);

		controlView = (MorphButton) view.findViewById(R.id.play_pause);
		controlView.setOnClickListener(this);

		container = (LinearLayout) view.findViewById(R.id.container);

		return view;
	}

	@Override
	public void onStart() {
		super.onStart();

		if (isBound() && getService().getQueue().hasData()) {
			container.setVisibility(View.VISIBLE);
			syncDataWithService();
		} else {
			container.setVisibility(View.GONE);
		}
	}

	@Override
	public void onResume() {
		super.onResume();

		getActivity().registerReceiver(
				playReceiver,
				new IntentFilter(MusicPlayerService.SEND_PLAY));
		getActivity().registerReceiver(
				pauseReceiver,
				new IntentFilter(MusicPlayerService.SEND_PAUSE));
		getActivity().registerReceiver(
				newTrackReceiver,
				new IntentFilter(MusicPlayerService.SEND_NEW_TRACK));
	}

	@Override
	public void onPause() {
		super.onPause();

		getActivity().unregisterReceiver(playReceiver);
		getActivity().unregisterReceiver(pauseReceiver);
		getActivity().unregisterReceiver(newTrackReceiver);
	}

	@Override
	public void onServiceConnected() {
		if (getService().getQueue().hasData()) {
			container.setVisibility(View.VISIBLE);
			syncDataWithService();
		} else {
			container.setVisibility(View.GONE);
		}
	}

	/*
	 * View.OnClickListener
	 */

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.info_wrapper:
		case R.id.title:
		case R.id.info:
		case R.id.art:
			NowPlayingActivity.startActivity(getContext());
			break;

		case R.id.play_pause:
			getActivity().sendBroadcast(new Intent(MusicPlayerService.ACTION_PLAY_PAUSE));
			break;
		}
	}

	/*
	 * Private methods
	 */

	private void syncDataWithService() {
		Glide.with(getContext())
				.load(ArtProvider.fromTrack(getService().getQueue().getCurrentTrack()))
				.error(R.drawable.fallback_cover)
				.into(artView);

		Track track = getService().getQueue().getCurrentTrack();
		String trackInfo = getResources().getString(
				R.string.nowplaying_info_pattern,
				track.getArtistTitle(),
				track.getAlbumTitle());
		titleView.setText(getService().getQueue().getCurrentTrack().getTitle());
		infoView.setText(trackInfo);


		if (getService().getPlayer().isPlaying()) {
			controlView.setState(MorphButton.MorphState.END);
		} else {
			controlView.setState(MorphButton.MorphState.START);
		}
	}
}

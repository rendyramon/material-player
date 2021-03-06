package com.edavtyan.materialplayer.components.nowplaying.views;

import android.app.Activity;
import android.content.Context;
import android.widget.TextView;

import com.edavtyan.materialplayer.R;

public class NowPlayingInfo {
	private final Context context;
	private final TextView titleView;
	private final TextView infoView;

	public NowPlayingInfo(Activity activity) {
		this.context = activity;
		titleView = (TextView) activity.findViewById(R.id.title);
		infoView = (TextView) activity.findViewById(R.id.info);
	}

	public void setTitle(CharSequence title) {
		titleView.setText(title);
	}

	public void setInfo(CharSequence artistTitle, CharSequence albumTitle) {
		String info = context.getString(R.string.nowplaying_info_pattern, artistTitle, albumTitle);
		infoView.setText(info);
	}
}

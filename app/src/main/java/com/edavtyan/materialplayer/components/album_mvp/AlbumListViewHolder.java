package com.edavtyan.materialplayer.components.album_mvp;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.edavtyan.materialplayer.R;

import lombok.Getter;
import lombok.Setter;

public class AlbumListViewHolder
		extends RecyclerView.ViewHolder
		implements View.OnClickListener,
				   PopupMenu.OnMenuItemClickListener {

	private final Context context;
	private final TextView titleView;
	private final TextView infoView;
	private final ImageView artView;
	private final ImageButton menuButton;
	private final View itemView;
	private final PopupMenu popupMenu;
	private @Getter @Setter int albumId;
	private @Setter OnHolderClickListener onHolderClickListener;
	private @Setter OnHolderMenuItemClickListener onHolderMenuItemClickListener;

	public interface OnHolderClickListener {
		void onHolderClick(int albumId);
	}

	public interface OnHolderMenuItemClickListener {
		void onMenuAddToPlaylistClick(int albumId);
	}

	public AlbumListViewHolder(Context context, View itemView) {
		super(itemView);

		this.context = context;

		this.itemView = itemView;
		this.itemView.setOnClickListener(this);

		titleView = (TextView) itemView.findViewById(R.id.title);
		infoView = (TextView) itemView.findViewById(R.id.info);
		artView = (ImageView) itemView.findViewById(R.id.art);
		menuButton = (ImageButton) itemView.findViewById(R.id.menu);

		popupMenu = new PopupMenu(context, menuButton);
		popupMenu.inflate(R.menu.menu_track);
		popupMenu.setOnMenuItemClickListener(this);

		menuButton.setOnClickListener(view -> popupMenu.show());
	}

	public void setTitle(String title) {
		titleView.setText(title);
	}

	public String getTitle() {
		return titleView.getText().toString();
	}

	public void setInfo(int tracksCount, String artist) {
		Resources res = context.getResources();
		String tracksCountStr = res.getQuantityString(R.plurals.tracks, tracksCount, tracksCount);
		String info = res.getString(R.string.pattern_album_info, artist, tracksCountStr);
		infoView.setText(info);
	}

	public String getInfo() {
		return infoView.getText().toString();
	}

	public void setArt(String artPath) {
		Glide.with(context)
				.load(artPath)
				.error(R.drawable.fallback_cover_listitem)
				.into(artView);
	}

	public Drawable getArt() {
		return artView.getDrawable();
	}

	public void setOnClickListener(View.OnClickListener listener) {
		itemView.setOnClickListener(listener);
	}

	@Override
	public void onClick(View v) {
		if (onHolderClickListener != null) {
			onHolderClickListener.onHolderClick(albumId);
		}
	}

	@Override
	public boolean onMenuItemClick(MenuItem item) {
		if (onHolderMenuItemClickListener == null) return false;

		switch (item.getItemId()) {
		case R.id.menu_addToPlaylist:
			onHolderMenuItemClickListener.onMenuAddToPlaylistClick(albumId);
			return true;
		default:
			return false;
		}
	}
}
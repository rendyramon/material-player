package com.edavtyan.materialplayer.app.adapters;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.edavtyan.materialplayer.app.R;
import com.edavtyan.materialplayer.app.lib.adapters.RecyclerViewServiceAdapter;
import com.edavtyan.materialplayer.app.services.MusicPlayerService;

public class PlaylistAdapter
		extends RecyclerViewServiceAdapter<PlaylistAdapter.TrackViewHolder> {

	private final Context context;

	public PlaylistAdapter(Context context) {
		super(context);
		this.context = context;
	}

	/*
	 * BroadcastReceivers
	 */

	private BroadcastReceiver newTrackReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			notifyItemChanged(service.getPlayer().getCurrentTrackIndex());
			notifyItemChanged(service.getPlayer().getCurrentTrackIndex() - 1);
		}
	};

	/*
	 * ViewHolder
	 */

	public class TrackViewHolder
			extends RecyclerView.ViewHolder
			implements View.OnClickListener, PopupMenu.OnMenuItemClickListener {
		private final TextView titleView;
		private final TextView infoView;
		private final ImageButton menuButton;
		private final ImageView nowPlayingIcon;

		public TrackViewHolder(View itemView) {
			super(itemView);
			itemView.setOnClickListener(this);

			titleView = (TextView) itemView.findViewById(R.id.title);
			infoView = (TextView) itemView.findViewById(R.id.info);
			menuButton = (ImageButton) itemView.findViewById(R.id.menu);
			nowPlayingIcon = (ImageView) itemView.findViewById(R.id.nowPlaying);

			PopupMenu popupMenu = new PopupMenu(context, menuButton);
			popupMenu.inflate(R.menu.menu_queue);
			popupMenu.setOnMenuItemClickListener(this);
			menuButton.setOnClickListener(view -> popupMenu.show());
		}

		@Override
		public void onClick(View view) {
			if (!isBound) {
				return;
			}

			int oldPosition = service.getPlayer().getCurrentTrackIndex();
			service.getPlayer().setCurrentTrackIndex(getAdapterPosition());
			service.getPlayer().prepare();
			notifyItemChanged(oldPosition);
		}

		@Override
		public boolean onMenuItemClick(MenuItem menuItem) {
			switch (menuItem.getItemId()) {
			case R.id.menu_remove:
				service.getPlayer().getQueue().remove(getAdapterPosition());
				notifyItemRemoved(getAdapterPosition());
				return true;

			default:
				return false;
			}
		}
	}

	@Override
	public TrackViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		View view = LayoutInflater.from(context).inflate(R.layout.listitem_track, parent, false);
		return new TrackViewHolder(view);
	}

	@Override
	public void onBindViewHolder(TrackViewHolder holder, int position) {
		if (!isBound) return;
		holder.titleView.setText(service.getPlayer().getQueue().get(position).getTrackTitle());
		holder.infoView.setText(service.getPlayer().getQueue().get(position).getAlbumTitle());
		if (service.getPlayer().getCurrentTrackIndex() == holder.getLayoutPosition()) {
			holder.nowPlayingIcon.setVisibility(View.VISIBLE);
		} else {
			holder.nowPlayingIcon.setVisibility(View.GONE);
		}
	}

	@Override
	public int getItemCount() {
		if (!isBound) return 0;
		return service.getPlayer().getQueue().size();
	}

	/*
	 * Public methods
	 */

	public void registerReceivers() {
		context.registerReceiver(
				newTrackReceiver,
				new IntentFilter(MusicPlayerService.SEND_NEW_TRACK));
	}

	public void unregisterReceivers() {
		context.unregisterReceiver(newTrackReceiver);
	}
}
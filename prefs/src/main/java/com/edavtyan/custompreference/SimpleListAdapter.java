package com.edavtyan.custompreference;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import lombok.Setter;

public class SimpleListAdapter
		extends RecyclerView.Adapter<SimpleListViewHolder>
		implements SimpleListViewHolder.OnHolderClickListener {

	private final Context context;
	private final SimpleListModel model;
	private @Setter SimpleListViewHolder.OnHolderClickListener onHolderClickListener;

	public SimpleListAdapter(Context context, SimpleListModel model) {
		this.context = context;
		this.model = model;
	}

	@Override
	public SimpleListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		View view = LayoutInflater.from(context).inflate(R.layout.listitem_simple, parent, false);
		return new SimpleListViewHolder(view);
	}

	@Override
	public void onBindViewHolder(SimpleListViewHolder holder, int position) {
		holder.setTitle(model.getEntries().get(position).toString());
		holder.setChecked(model.getPrefSelectedAtIndex(position));
		holder.setValue(model.getValues().get(position));
		holder.setOnHolderClickListener(this);
	}

	@Override
	public int getItemCount() {
		return model.getEntries().size();
	}

	@Override
	public void onHolderClick(CharSequence value) {
		if (onHolderClickListener != null) {
			onHolderClickListener.onHolderClick(value);
		}
	}

}

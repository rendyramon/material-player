package com.example.custompreference;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.TextView;

import com.example.custompreference.utils.AttributeResolver;
import com.example.custompreference.utils.PixelConverter;

public class ColorSelectionPreference extends DialogPreference<ColorSelectionController>
		implements ColorSelectionView.OnColorSelectedListener {

	private ColorSelectionView colorSelectionView;
	private ColorCircleView colorView;


	public ColorSelectionPreference(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public ColorSelectionPreference(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}


	@Override
	protected void createDialogBuilder(AlertDialog.Builder builder) {
		int padding = PixelConverter.dpToPx(24);

		colorSelectionView = new ColorSelectionView(context, null);
		colorSelectionView.setPadding(padding, 0, padding, 0);
		colorSelectionView.setColors(controller.getEntries());
		colorSelectionView.rebuild();
		colorSelectionView.setSelectedColor(controller.getSelectedPrefIndex());
		colorSelectionView.setOnColorSelectedListener(this);
		builder.setView(colorSelectionView);
	}

	@Override
	protected ColorSelectionController createController(AttributeSet attrs) {
		return new ColorSelectionController(this, attrs);
	}

	@Override
	protected void createEntryView() {
		AttributeResolver attrs = new AttributeResolver(context);

		int height = attrs.getDimen(android.R.attr.listPreferredItemHeight);
		setMinimumHeight(height);

		int paddingLeft = attrs.getDimen(android.R.attr.listPreferredItemPaddingLeft);
		int paddingRight = attrs.getDimen(android.R.attr.listPreferredItemPaddingRight);
		setPadding(paddingLeft, 0, paddingRight, 0);

		Drawable background = attrs.getDrawableAttribute(android.R.attr.selectableItemBackground);
		setBackgroundDrawable(background);

		inflate(context, R.layout.entry_color, this);

		TextView titleView = (TextView) findViewById(R.id.title);
		titleView.setText(controller.getTitle());

		colorView = (ColorCircleView) findViewById(R.id.color);
		colorView.setColor(controller.getCurrentColor());
	}

	@Override
	public void onColorSelected(int color, int position) {
		controller.savePref(position);
		colorSelectionView.setSelectedColor(position);
		colorView.setColor(controller.getCurrentColor());
		closeDialog();
	}
}
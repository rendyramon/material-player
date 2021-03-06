package com.edavtyan.custompreference;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import com.edavtyan.custompreference.utils.PixelConverter;

import java.util.ArrayList;
import java.util.List;

import lombok.Setter;

public class ColorSelectionView extends LinearLayout implements View.OnClickListener {

	private final Context context;
	private List<ColorToggleSelectedView> colorViews;
	private @Setter List<Integer> colors;
	private @Setter int colorViewSize;
	private @Setter int spacing;
	private @Setter int colorsPerRow;
	private @Setter int minSpacing;
	private @Setter int totalWidth;
	private @Setter OnColorSelectedListener onColorSelectedListener;

	interface OnColorSelectedListener {
		void onColorSelected(int position);
	}

	public ColorSelectionView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		init();
	}

	public ColorSelectionView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		this.context = context;
		init();
	}

	public void rebuild() {
		initSizeAndSpacing();

		int fullRowsCount = colors.size() / colorsPerRow;
		for (int i = 0; i < fullRowsCount; i++) {
			LinearLayout row = createRow(i);
			for (int j = 0; j < colorsPerRow; j++) {
				row.addView(createColorView(colorsPerRow * i + j));
			}
			addView(row);
		}

		LinearLayout lastRow = createRow(fullRowsCount - 1);
		int colorsInLastRow = colors.size() % colorsPerRow;
		for (int i = 0; i < colorsInLastRow; i++) {
			lastRow.addView(createColorView(fullRowsCount * colorsPerRow + i));
		}
		addView(lastRow);
	}

	public void setSelectedColor(int position) {
		for (ColorToggleSelectedView colorView : colorViews) colorView.setChecked(false);
		colorViews.get(position).setChecked(true);
	}

	@Override
	public void onClick(View v) {
		if (onColorSelectedListener != null) {
			ColorToggleSelectedView colorView = (ColorToggleSelectedView) v;
			int color = colorView.getColor();
			onColorSelectedListener.onColorSelected(colors.indexOf(color));
		}
	}

	private void init() {
		setOrientation(VERTICAL);
		colors = new ArrayList<>();
		colorViews = new ArrayList<>();
		minSpacing = PixelConverter.dpToPx(12);
		colorViewSize = PixelConverter.dpToPx(36);
		totalWidth = PixelConverter.dpToPx(240);
	}

	private LinearLayout createRow(int index) {
		LinearLayout linearLayout = new LinearLayout(context, null);

		boolean isFirstRow = index == 0;
		if (isFirstRow) {
			linearLayout.setPadding(0, PixelConverter.dpToPx(20), 0, 0);
		} else {
			linearLayout.setPadding(0, spacing, 0, 0);
		}

		return linearLayout;
	}

	private ColorToggleSelectedView createColorView(int index) {
		LayoutParams params = new LayoutParams(colorViewSize, colorViewSize);

		boolean isFirstInRow = index % colorsPerRow == 0;
		if (!isFirstInRow) {
			params.setMargins(spacing, 0, 0, 0);
		}

		ColorToggleSelectedView colorView = new ColorToggleSelectedView(context, null);
		colorView.setLayoutParams(params);
		colorView.setColor(colors.get(index));
		colorView.setOnClickListener(this);
		colorViews.add(colorView);
		return colorView;
	}

	private void initSizeAndSpacing() {
		int width = totalWidth;
		colorsPerRow = 0;

		// first item has no spacing
		width -= colorViewSize;
		colorsPerRow++;

		while (width > (colorViewSize + minSpacing)) {
			colorsPerRow++;
			width -= colorViewSize + minSpacing;
		}

		int spaceForSpacing = totalWidth - colorsPerRow * colorViewSize;
		int spacesCount = colorsPerRow - 1;
		spacing = spaceForSpacing / spacesCount;
	}
}

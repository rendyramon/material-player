package com.edavtyan.custompreference;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.TextView;

import com.edavtyan.custompreference.utils.AttributeResolver;

public abstract class SummaryPreference<TController extends SummaryController>
		extends DialogPreference<TController> {

	protected final String initialSummary;
	protected TextView titleView;
	protected TextView summaryView;


	public SummaryPreference(Context context, AttributeSet attrs) {
		super(context, attrs);
		initialSummary = controller.getSummary().toString();
		updateEntry();
	}

	public SummaryPreference(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		initialSummary = controller.getSummary().toString();
		updateEntry();
	}


	public void updateEntry() {
		if (isInEditMode()) {
			summaryView.setText(initialSummary.replace("%s", controller.getDefaultValue()));
			return;
		}

		summaryView.setText(initialSummary.replace("%s", controller.getCurrentPreference()));
	}


	@Override
	protected void createEntryView() {
		AttributeResolver res = new AttributeResolver(context);

		int height = res.getDimen(R.attr.listPreferredItemHeight);
		int padding = res.getDimen(R.attr.listPreferredItemPaddingLeft);
		Drawable background = res.getDrawable(R.attr.selectableItemBackground);

		setMinimumHeight(height);
		setOrientation(VERTICAL);
		setBackgroundDrawable(background);
		setPadding(padding, 0, padding, 0);
		setGravity(Gravity.CENTER_VERTICAL);

		inflate(context, R.layout.entry_summary, this);

		titleView = (TextView) findViewById(R.id.title);
		titleView.setText(controller.getTitle());

		summaryView = (TextView) findViewById(R.id.summary);
		summaryView.setText(controller.getSummary());
	}
}
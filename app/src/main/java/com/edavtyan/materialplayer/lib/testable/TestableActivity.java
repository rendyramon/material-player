package com.edavtyan.materialplayer.lib.testable;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.edavtyan.materialplayer.lib.base.BaseToolbarActivity;

@SuppressLint("MissingSuperCall")
public abstract class TestableActivity extends BaseToolbarActivity {
	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		baseOnCreate(savedInstanceState);
	}

	@Override
	protected void onDestroy() {
		baseOnDestroy();
	}

	public void baseOnCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	public void baseOnDestroy() {
		super.onDestroy();
	}
}
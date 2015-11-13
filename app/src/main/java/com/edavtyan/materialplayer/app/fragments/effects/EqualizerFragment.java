package com.edavtyan.materialplayer.app.fragments.effects;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.edavtyan.materialplayer.app.R;
import com.edavtyan.materialplayer.app.fragments.base.ServiceFragment;
import com.edavtyan.materialplayer.app.music.effects.equalizer.Equalizer;
import com.edavtyan.materialplayer.app.views.EqualizerView;

public class EqualizerFragment
        extends ServiceFragment
        implements EqualizerView.OnBandChangedListener,
                   CompoundButton.OnCheckedChangeListener {
    private Equalizer equalizer;
    private EqualizerView equalizerView;
    private Switch equalizerSwitch;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_equalizer, container, false);

        equalizerView = (EqualizerView) view.findViewById(R.id.equalizer);
        equalizerView.setOnBandChangedListener(this);

        equalizerSwitch = (Switch) view.findViewById(R.id.equalizer_switch);
        equalizerSwitch.setOnCheckedChangeListener(this);

        return view;
    }

    @Override
    public void onServiceConnected() {
        equalizer = getService().getEqualizer();
        equalizerView.setGainLimit(equalizer.getGainLimit());
        equalizerView.setOnBandChangedListener(this);
        equalizerView.setBands(equalizer.getBandsCount(),
                equalizer.getFrequencies(), equalizer.getGains());
        equalizerSwitch.setChecked(equalizer.isEnabled());
    }

    /*
     * EqualizerView.OnBandChangedListener
     */

    @Override
    public void onBandChanged(int band, int gain) {
        equalizer.setBandGain(band, gain);
    }

    @Override
    public void onBandStopTracking() {
        equalizer.saveSettings();
    }

    /*
     * CompoundButton.OnCheckedChangeListener
     */

    @Override
    public void onCheckedChanged(CompoundButton button, boolean isChecked) {
        equalizer.setEnabled(isChecked);
    }
}

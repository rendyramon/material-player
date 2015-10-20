package com.edavtyan.materialplayer.app.fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;

import com.edavtyan.materialplayer.app.R;
import com.edavtyan.materialplayer.app.fragments.base.ServiceFragment;
import com.edavtyan.materialplayer.app.services.MusicPlayerService;

public class PlayerSeekbarFragment
        extends ServiceFragment
        implements SeekBar.OnSeekBarChangeListener {

    private SeekBar seekbar;
    private Handler handler;
    private final Runnable syncSeekbar = new Runnable() {
        @Override
        public void run() {
            seekbar.setProgress(getService().getPosition());
            handler.postDelayed(syncSeekbar, 1000);
        }
    };

    /*
     * BroadcastReceivers
     */

    private final BroadcastReceiver playReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            syncSeekbar.run();
        }
    };
    private final BroadcastReceiver pauseReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            handler.removeCallbacks(syncSeekbar);
        }
    };
    private final BroadcastReceiver newTrackReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            seekbar.setMax(getService().getDuration());
            syncSeekbar.run();
        }
    };

    /*
     * Fragment members
     */

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_seekbar, container);

        seekbar = (SeekBar) view.findViewById(R.id.seekbar);
        seekbar.setOnSeekBarChangeListener(this);
        handler = new Handler();

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Intent intent = new Intent(getActivity(), MusicPlayerService.class);
        getActivity().getApplicationContext().bindService(intent, this, Context.BIND_AUTO_CREATE);
    }

    @Override
    public void onResume() {
        getActivity().registerReceiver(
                playReceiver,
                new IntentFilter(MusicPlayerService.SEND_PLAY));
        getActivity().registerReceiver(
                pauseReceiver,
                new IntentFilter(MusicPlayerService.SEND_PAUSE));
        getActivity().registerReceiver(
                newTrackReceiver,
                new IntentFilter(MusicPlayerService.SEND_NEW_TRACK));
        super.onResume();
    }

    @Override
    public void onPause() {
        getActivity().unregisterReceiver(playReceiver);
        getActivity().unregisterReceiver(pauseReceiver);
        getActivity().unregisterReceiver(newTrackReceiver);
        super.onPause();
    }

    @Override
    public void onDestroy() {
        getActivity().getApplicationContext().unbindService(this);
        super.onDestroy();
    }

    @Override
    public void onServiceConnected() {
        seekbar.setMax(getService().getDuration());
        syncSeekbar.run();
    }

    /*
     * SeekBar.OnSeekBarChangeListener members
     */

    @Override
    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {}

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        handler.removeCallbacks(syncSeekbar);
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        getService().setPosition(seekBar.getProgress());
        syncSeekbar.run();
    }
}

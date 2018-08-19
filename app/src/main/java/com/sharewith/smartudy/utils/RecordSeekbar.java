package com.sharewith.smartudy.utils;

import android.content.Context;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.SeekBar;

import java.io.IOException;

/**
 * Created by Simjae on 2018-07-24.
 */

//SeekBar와 MediaRecorder를 연결
public class RecordSeekbar extends AsyncTask<Void,Integer,Void>{
    private SeekBar mSeekbar;
    private MediaPlayer mPlayer;
    private boolean isrun;
    private Context mContext;
    private Uri mUri;

    public RecordSeekbar(Context context,final Uri uri,SeekBar seekbar) {
        mContext = context;
        mUri = uri; // 녹음 파일 uri
        mSeekbar = seekbar;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        try {
            mPlayer = MediaPlayer.create(mContext, mUri);
            mSeekbar.setOnSeekBarChangeListener(listener);
            mSeekbar.setMax(mPlayer.getDuration());
            mPlayer.setLooping(false);//무한반복x
            mPlayer.start();
        }catch(Exception e){
            e.printStackTrace();
        }
    }


    @Override
    protected Void doInBackground(Void... voids) {
        while(mPlayer.isPlaying()) {
            int current = mPlayer.getCurrentPosition();
            publishProgress(current);
        }
        publishProgress(0);
        mPlayer.stop();
        mPlayer.release();
        return null;
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
        int current = values[0];
        mSeekbar.setProgress(current);
    }

    SeekBar.OnSeekBarChangeListener listener = new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
        }
        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
        }
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(fromUser)
                    mPlayer.seekTo(progress);
        }

    };

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);

    }

    @Override
    protected void onCancelled(Void aVoid) {
        super.onCancelled(aVoid);
    }

    @Override
    protected void onCancelled() {
        super.onCancelled();
    }

    public RecordSeekbar() {
        super();
    }

}

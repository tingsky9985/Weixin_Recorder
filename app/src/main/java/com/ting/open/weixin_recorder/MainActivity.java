package com.ting.open.weixin_recorder;

import android.app.Activity;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.ting.open.weixin_recorder.adapter.RecorderAdapter;
import com.ting.open.weixin_recorder.beans.Recorder;
import com.ting.open.weixin_recorder.utils.MediaManager;
import com.ting.open.weixin_recorder.view.AudioRecorderButton;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lt on 2016/8/6.
 */

/**
 * 主要内容：
 * 仿微信语音聊天
 *
 * 主要思想：
 * AudioRecorderButton
 *   state: STATE_NORMAL , STATE_RECORDING, STATE_WANT_TO_CANCEL
 * DialogManager
 *   Style: RECORDING, WANT_TO_CANCEL, TOO_SHORT
 *  AudioManager
 *    prepare();
 *    cancel();
 *    release();
 *    getVoiceLevel();
 *
 * 伪码：
 * class AudioRecorderButton{
 *     onTouchEvent(){
 *         DOWN:
 *            changeButtonState(STATE_RECORDING);
 *            LONGCLICK -> AudioManager.prepare() -> end prepared -> DialogManager.showDialog(RECORDING)
 *        MOVE:
 *           if(wantCancel(x,y)){
 *               DialogManager.showDialog(WANT_TO_CANCEL);
 *               changeButtonState(STATE_WANT_TO_CANCEL);
 *           }else{
 *               DialogManager.showDialog(RECORDING);
 *               changeButtonState(STATE_RECORDING);
 *           }
 *       UP:
 *          if(wantCancel == curState){
 *              AudioManager.cancel();
 *          }
 *          if(STATE_RECORDING == curState){
 *              AudioManager.reease();
 *              callbckToActivity(url,time);
 *          }
 *     }
 * }
 *
 */
public class MainActivity extends Activity {


    private ListView mListView;
    private ArrayAdapter<Recorder> mAdapter;
    private List<Recorder> mDatas = new ArrayList<Recorder>();

    private AudioRecorderButton mAudioRecorderButton;

    private View animaView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //初始化控件
        init();
    }

    /**
     * 初始化数据
     */
    private void init() {

        mListView = (ListView) findViewById(R.id.id_listview);
        mAudioRecorderButton = (AudioRecorderButton) findViewById(R.id.id_recorder_button);
        mAudioRecorderButton.setOnAudioFinishRecorderListener(new AudioRecorderButton.AudioFinishRecorderListener() {
            @Override
            public void onFinish(float seconds, String filePath) {
                Recorder recorder = new Recorder(seconds,filePath);
                mDatas.add(recorder);
                mAdapter.notifyDataSetChanged();
                mListView.setSelection(mDatas.size() - 1);
            }
        });

        mAdapter = new RecorderAdapter(this, mDatas);
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if(animaView != null){
                    animaView.setBackgroundResource(R.drawable.adj);
                    animaView = null;
                }
                //播放动画
                animaView = view.findViewById(R.id.id_recorder_anima);
                animaView.setBackgroundResource(R.drawable.play_anima);
                AnimationDrawable anima = (AnimationDrawable) animaView.getBackground();
                anima.start();
                //播放音频
                MediaManager.playSound(mDatas.get(position).filePath, new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        animaView.setBackgroundResource(R.drawable.adj);
                    }
                });
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        MediaManager.pause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        MediaManager.pause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MediaManager.release();
    }
}


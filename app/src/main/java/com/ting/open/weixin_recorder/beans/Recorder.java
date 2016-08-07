package com.ting.open.weixin_recorder.beans;

/**
 * Created by lt on 2016/8/7.
 */
public class Recorder {
    public float time;
    public String filePath;
    public Recorder(float time, String filePath){
        super();
        this.time = time;
        this.filePath = filePath;
    }

    public float getTime() {
        return time;
    }

    public void setTime(float time) {
        this.time = time;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }
}

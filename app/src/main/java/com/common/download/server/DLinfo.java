package com.common.download.server;

import com.mydownloader.cn.interfaces.IDListener;

/**
 * Created by Administrator on 2016/7/1.
 */
public class DLinfo {
    private String name;
    private  int downId;
    private String dir;
    private String downLoadUrl;
    private boolean isStop;
    private IDListener myIdIDListener;

    private int fileLenght;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getDownId() {
        return downId;
    }

    public void setDownId(int downId) {
        this.downId = downId;
    }

    public String getDir() {
        return dir;
    }

    public void setDir(String dir) {
        this.dir = dir;
    }

    public String getDownLoadUrl() {
        return downLoadUrl;
    }

    public void setDownLoadUrl(String downLoadUrl) {
        this.downLoadUrl = downLoadUrl;
    }

    public boolean isStop() {
        return isStop;
    }

    public void setStop(boolean stop) {
        isStop = stop;
    }

    public IDListener getMyIdIDListener() {
        return myIdIDListener;
    }

    public void setMyIdIDListener(IDListener myIdIDListener) {
        this.myIdIDListener = myIdIDListener;
    }

    public int getFileLenght() {
        return fileLenght;
    }

    public void setFileLenght(int fileLenght) {
        this.fileLenght = fileLenght;
    }
}

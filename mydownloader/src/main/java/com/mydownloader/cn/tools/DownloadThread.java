package com.mydownloader.cn.tools;

import java.io.File;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;

import android.util.Log;

public class DownloadThread extends Thread {
    private static final String TAG = "DownloadThread";
    private File saveFile;
    private URL downUrl;
    private int block;
    
    /* 下载开始位置  */
    private int threadId = -1;    
    private int downLength;
    private boolean finish = false;
    private FileDownloader downloader;



    public DownloadThread(FileDownloader downloader, URL downUrl, File saveFile, int block, int downLength, int threadId) {
        this.downUrl = downUrl;
        this.saveFile = saveFile;
        this.block = block;
        this.downloader = downloader;
        this.threadId = threadId;
        this.downLength = downLength;
    }
    
    @Override
    public void run() {
        if(downLength < block){//未下载完成
            try {
                HttpURLConnection http = (HttpURLConnection) downUrl.openConnection();
                http.setConnectTimeout(5 * 1000);
                http.setRequestMethod("GET");
                http.setRequestProperty("Accept", "image/gif, image/jpeg, image/pjpeg, image/pjpeg, application/x-shockwave-flash, application/xaml+xml, application/vnd.ms-xpsdocument, application/x-ms-xbap, application/x-ms-application, application/vnd.ms-excel, application/vnd.ms-powerpoint, application/msword, */*");
                http.setRequestProperty("Accept-Language", "zh-CN");
                http.setRequestProperty("Referer", downUrl.toString()); 
                http.setRequestProperty("Charset", "UTF-8");
                int startPos = block * (threadId - 1) + downLength;//开始位置
                int endPos = block * threadId -1;//结束位置
                if (threadId==downloader.getThreadSize())
                    endPos=downloader.getFileSize()-1;
                http.setRequestProperty("Range", "bytes=" + startPos + "-"+ endPos);//设置获取实体数据的范围
                http.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 5.2; Trident/4.0; .NET CLR 1.1.4322; .NET CLR 2.0.50727; .NET CLR 3.0.04506.30; .NET CLR 3.0.4506.2152; .NET CLR 3.5.30729)");
                http.setRequestProperty("Connection", "Keep-Alive");
                Llog.print(TAG,"Range---bytes=" + startPos + "-"+ endPos+"---downLength:"+downLength+"----block:"+block);
                InputStream inStream = http.getInputStream();
                byte[] buffer = new byte[2<<16];
                int offset = 0;

                RandomAccessFile threadfile = new RandomAccessFile(this.saveFile, "rwd");
                threadfile.seek(startPos);
//                (offset = inStream.read(buffer, 0, 2<<17)) != -1
                while ((offset = inStream.read(buffer, 0, 2<<16)) != -1) {
                    threadfile.write(buffer, 0, offset);
                    downLength +=offset;
                    downloader.append(offset);
//                    Llog.print(TAG,this.threadId+"：" + downLength + "----endPos："+ endPos);
                    downloader.update(this.threadId, downLength);
                    if (downloader.isStopDownload()) {
                        downloader.stop(this.threadId);
                        break;
                    }
                }
                threadfile.close();
                inStream.close();
                if (!downloader.isStopDownload()){

                    Llog.print(TAG,"Thread " + this.threadId + " download finish");
                    this.finish = true;
                }else{
                    Llog.print(TAG,"Thread " + this.threadId + " download Pause");
                }

            } catch (Exception e) {
                this.downLength = -1;
                Llog.print(TAG,"Thread "+ this.threadId+ ":finish:"+isFinish()+"\n"+ e);
            }
        }
    }
    

    
    /**
     * 下载是否完成
     * @return
     */
    public boolean isFinish() {
        return finish;
    }
    
    /**
     * 已经下载的内容大小
     * @return 如果返回值为-1,代表下载失败
     */
    public long getDownLength() {
        return downLength;
    }
}
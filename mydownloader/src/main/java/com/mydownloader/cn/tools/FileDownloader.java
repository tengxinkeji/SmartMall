package com.mydownloader.cn.tools;
import java.io.File;

import java.io.RandomAccessFile;

import java.net.HttpURLConnection;

import java.net.URL;

import java.util.HashMap;
import java.util.LinkedHashMap;

import java.util.Map;

import java.util.UUID;

import java.util.concurrent.ConcurrentHashMap;

import java.util.regex.Matcher;

import java.util.regex.Pattern;





import android.content.Context;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import com.mydownloader.cn.interfaces.IDListener;


public class FileDownloader {

    private static final String TAG = "FileDownloader";

    private Context context;

    public FileService fileService;

    private IDListener idListener;

    private boolean isStopDownload;

    /* 已下载文件长度 */

    private int downloadSize = 0;

    

    /* 原始文件长度 */

    private int fileSize = 0;

    

    /* 线程数 */

    private DownloadThread[] threads;

    

    /* 本地保存文件 */

    private File saveFile;

    

    /* 缓存各线程下载的长度*/

    private Map<Integer, Integer> data = new ConcurrentHashMap<Integer, Integer>();

    

    /* 每条线程下载的长度 */

    private int block;

    

    /* 下载路径  */

    private String downloadUrl;


    private long lastTime = System.currentTimeMillis();
    private long lastTotalProgress=0l;

    /**

     * 获取线程数

     */

    public int getThreadSize() {

        return threads.length;

    }

    

    /**

     * 获取文件大小

     * @return

     */

    public int getFileSize() {

        return fileSize;

    }

    

    /**

     * 累计已下载大小

     * @param size

     */

    protected synchronized void append(int size) {

        downloadSize += size;


    }

    

    /**

     * 更新指定线程最后下载的位置

     * @param threadId 线程id

     * @param pos 最后下载的位置

     */

    protected synchronized void update(int threadId, int pos) {

        this.data.put(threadId, pos);

        this.fileService.update(this.downloadUrl, this.data);

    }

    private Map<Integer,Boolean> downloadStop;
    public boolean isStopFinish=false;
    protected synchronized void stop(int threadId) {

        downloadStop.put(threadId,true);
        boolean misStopFinish=true;
        for (Boolean b : downloadStop.values()) {
            if (!b){
                misStopFinish=false;
                break;
            }
        }
        if (misStopFinish) {
            isStopFinish=true;
            idListener.onStop(this.downloadSize);
        }

    }

    public synchronized boolean isStopDownload() {
        return isStopDownload;
    }

    public synchronized void setStopDownload(boolean stopDownload) {
        isStopDownload = stopDownload;
    }

    public FileDownloader(Context context){
        this.context = context;
        fileService = new FileService(context);
    }


    /**

     *

     * @param downloadUrl 下载路径

     * @param fileSaveDir 文件保存目录

     * @param threadNum 下载线程数

     */



    public void init( String downloadUrl, File fileSaveDir, int threadNum,IDListener idListenerP) {


        try {


            this.idListener = idListenerP;

            this.isStopFinish=false;
            downloadStop=new HashMap<Integer,Boolean>();

            for(int i=1;i<=threadNum;i++){
                downloadStop.put(i,false);
            }

            this.downloadUrl = downloadUrl;

            URL url = new URL(this.downloadUrl);

            if(!fileSaveDir.exists()) fileSaveDir.mkdirs();

            this.threads = new DownloadThread[threadNum];

            idListener.onPrepare();

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setConnectTimeout(5*1000);

            conn.setRequestMethod("GET");

            conn.setRequestProperty("Accept", "image/gif, image/jpeg, image/pjpeg, image/pjpeg, application/x-shockwave-flash, application/xaml+xml, application/vnd.ms-xpsdocument, application/x-ms-xbap, application/x-ms-application, application/vnd.ms-excel, application/vnd.ms-powerpoint, application/msword, */*");

            conn.setRequestProperty("Accept-Language", "zh-CN");

            conn.setRequestProperty("Referer", downloadUrl); 

            conn.setRequestProperty("Charset", "UTF-8");

            conn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 5.2; Trident/4.0; .NET CLR 1.1.4322; .NET CLR 2.0.50727; .NET CLR 3.0.04506.30; .NET CLR 3.0.4506.2152; .NET CLR 3.5.30729)");

            conn.setRequestProperty("Connection", "Keep-Alive");

            conn.connect();

            printResponseHeader(conn);

            

            if (conn.getResponseCode()==200) {

                this.fileSize = conn.getContentLength();//根据响应获取文件大小

                if (this.fileSize <= 0) throw new RuntimeException("Unkown file size ");

                        

                String filename = getFileName(conn);//获取文件名称

                this.saveFile = new File(fileSaveDir, filename);//构建保存文件

                idListener.onStart(filename,downloadUrl,fileSize);

                Map<Integer, Integer> logdata = fileService.getData(downloadUrl);//获取下载记录

                

                if(logdata.size()>0){//如果存在下载记录

                    for(Map.Entry<Integer, Integer> entry : logdata.entrySet()) {

                        data.put(entry.getKey(), entry.getValue());//把各条线程已经下载的数据长度放入data中

                        Llog.print(TAG,"存在下载记录:"+filename);
                        Llog.print(TAG,"存在下载记录:"+entry.getKey()+":"+entry.getValue());

                    }
                }

                

                if(this.data.size()==this.threads.length){//下面计算所有线程已经下载的数据长度

                    for (int i = 0; i < this.threads.length; i++) {

                        this.downloadSize += this.data.get(i+1);

                    }
                    Llog.print(TAG,"已经下载的数据长度:"+downloadSize+"---文件大小："+getFileSize());
                    int progressResult=(int)((((float)  this.downloadSize)/((float) this.fileSize))*100);
                    idListener.onProgress(this.downloadSize,0,progressResult);

                }

                //计算每条线程下载的数据长度

                this.block = (this.fileSize % this.threads.length)==0? this.fileSize / this.threads.length : this.fileSize / this.threads.length + 1;

            }else{
                String err=this.downloadUrl+
                        "\nResponseCode:"+conn.getResponseCode()+
                        ":server no response ";
                idListener.onError(DLCons.Code.DOWNLOAD_err_no_response,err);
                throw new RuntimeException(err);

            }

        } catch (Exception e) {
            e.printStackTrace();
            String err=this.downloadUrl+
                    "\ndon't connection this url";
            idListener.onError(DLCons.Code.DOWNLOAD_err_url,err);
            throw new RuntimeException(err);

        }

    }

    

    /**

     * 获取文件名

     * @param conn

     * @return

     */

    private String getFileName(HttpURLConnection conn) {

        String contentDisposition=null;
        for (int i = 0;; i++) {

            String mine = conn.getHeaderField(i);

            if (mine == null) break;

            if("content-disposition".equals(conn.getHeaderFieldKey(i).toLowerCase())){

                Matcher m = Pattern.compile(".*filename=(.*)").matcher(mine.toLowerCase());

                if(m.find())
                    contentDisposition= m.group(1);

            }

        }

        String filename = DLUtil.obtainFileName(this.downloadUrl,contentDisposition,null);

       if (TextUtils.isEmpty(filename))
           filename = UUID.randomUUID()+ ".tmp";//默认取一个文件名
        Llog.print(TAG,"下载文件名："+filename);

        return filename;

    }

    

    /**

     *  开始下载文件

     * 监听下载数量的变化

     * @return 已下载文件大小

     * @throws Exception

     */

    public int download() throws Exception{

        try {

            RandomAccessFile randOut = new RandomAccessFile(this.saveFile, "rw");

            if(this.fileSize>0) randOut.setLength(this.fileSize);

            randOut.close();

            URL url = new URL(this.downloadUrl);

            

            if(this.data.size() != this.threads.length){

                this.data.clear();

                

                for (int i = 0; i < this.threads.length; i++) {

                    this.data.put(i+1, 0);//初始化每条线程已经下载的数据长度为0

                }

                idListener.onProgress(0,0,0);
            }

            

            for (int i = 0; i < this.threads.length; i++) {//开启线程进行下载

                int downLength = this.data.get(i+1);

                if(downLength < this.block && this.downloadSize<this.fileSize){//判断线程是否已经完成下载,否则继续下载    

                    this.threads[i] = new DownloadThread(this, url, this.saveFile, this.block, this.data.get(i+1), i+1);

                    this.threads[i].setPriority(7);

                    this.threads[i].start();

                }else{
                    downloadStop.put(i+1,true);
                    this.threads[i] = null;

                }

            }

            

            this.fileService.save(this.downloadUrl, this.data);

            boolean notFinish = true;//下载未完成

            while (notFinish) {// 循环判断所有线程是否完成下载



                notFinish = false;//假定全部线程下载完成

                for (int i = 0; i < this.threads.length; i++){

                    if (this.threads[i] != null && !this.threads[i].isFinish()) {//如果发现线程未完成下载

                        notFinish = true;//设置标志为下载没有完成

                        

                        if(this.threads[i].getDownLength() == -1){//如果下载失败,再重新下载

                            this.threads[i] = new DownloadThread(this, url, this.saveFile, this.block, this.data.get(i+1), i+1);

                            this.threads[i].setPriority(7);

                            this.threads[i].start();

                        }

                    }

                }

                if (isStopDownload()){
                    return this.downloadSize;
                }

                long currentTime = System.currentTimeMillis();
                if (currentTime - lastTime > 1000) {
                    long len= this.downloadSize-lastTotalProgress;
                    long userTime=currentTime - lastTime;
                    int downloadSpeed = (int)(len*1000/userTime)/1024;
                    int progressResult=(int)((((float) this.downloadSize)/((float) this.getFileSize()))*100);

                    idListener.onProgress(this.downloadSize,downloadSpeed,progressResult);//通知目前已经下载完成的数据长度

                    lastTime = currentTime;
                    lastTotalProgress=this.downloadSize;

                }

                Thread.sleep(1000);

            }

            idListener.onProgress(this.downloadSize,0,100);//通知目前已经下载完成的数据长度
            fileService.delete(this.downloadUrl);
            idListener.onFinish(saveFile);



        } catch (Exception e) {
            idListener.onError(DLCons.Code.DOWNLOAD_err_url,"file download fail");
            Llog.print(TAG,e.toString());

            throw new Exception("file download fail");

        }

        return this.downloadSize;

    }

    

    /**

     * 获取Http响应头字段

     * @param http

     * @return

     */

    public static Map<String, String> getHttpResponseHeader(HttpURLConnection http) {

        Map<String, String> header = new LinkedHashMap<String, String>();

        

        for (int i = 0;; i++) {

            String mine = http.getHeaderField(i);

            if (mine == null) break;

            header.put(http.getHeaderFieldKey(i), mine);

        }

        

        return header;

    }

    

    /**

     * 打印Http头字段

     * @param http

     */

    public static void printResponseHeader(HttpURLConnection http){

        Map<String, String> header = getHttpResponseHeader(http);

        

        for(Map.Entry<String, String> entry : header.entrySet()){

            String key = entry.getKey()!=null ? entry.getKey()+ ":" : "";

            Llog.print(TAG,key+ entry.getValue());

        }

    }





}
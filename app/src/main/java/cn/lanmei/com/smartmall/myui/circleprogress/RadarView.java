package cn.lanmei.com.smartmall.myui.circleprogress;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.SweepGradient;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;

import com.common.app.MyApplication;
import com.common.app.degexce.L;
import com.demo.smarthome.device.Dev;
import com.demo.smarthome.tools.IpTools;
import com.demo.smarthome.tools.StrTools;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

import cn.lanmei.com.smartmall.R;

/**
 * TODO: document your custom view class.
 */
public class RadarView extends View {
    private String TAG="RadarView";

    private TextPaint mPaint;
    protected float mCenterX;
    protected float mCenterY;
    private float cirAngle;
    private XChartCalc xChartCalc;
    private ScanListener scanListener;
    private boolean isDoneScan;

    private boolean isFindDev;
    static final int FIND_DEVID = 2;
    static final int DONE_SCAN = 3;
    static final int DEVID_Bind = 4;
    private boolean isStartUi=false;

    private Map<String,Boolean> scanIsDev;//设备是否已扫描；


    public void setScanListener(ScanListener scanListener) {
        this.scanListener = scanListener;
    }

    public interface ScanListener{
        public void startScan(RadarView radarView);
        public void scanDev(RadarView radarView,Dev dev);
        public void scanBindDev(RadarView radarView,Dev dev);
        public void scanDoneDev(RadarView radarView,boolean find);
    }

    public RadarView(Context context) {
        super(context);
        init(null, 0);
    }

    public RadarView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public RadarView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    private void init(AttributeSet attrs, int defStyle) {
        scanIsDev=new HashMap<>();
        // Load attributes
        xChartCalc=new XChartCalc();
        // Set up a default TextPaint object
        mPaint = new TextPaint();
//        mPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
//        mPaint.setTextAlign(Paint.Align.LEFT);
        mPaint.setColor(ContextCompat.getColor(getContext(),R.color.white));
        mPaint.setAntiAlias(true);

    }

    private void updateDimensions(int width, int height)
    {
        // Update center position
        mCenterX = width / 2.0f;
        mCenterY = height / 2.0f;
    }

    @Override
    protected synchronized void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);

        if (width > height)
            super.onMeasure(heightMeasureSpec, widthMeasureSpec);
        else
            super.onMeasure(widthMeasureSpec, widthMeasureSpec);

        updateDimensions(getWidth(), getHeight());
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh)
    {
        super.onSizeChanged(w, h, oldw, oldh);
        updateDimensions(w, h);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int paddingLeft = getPaddingLeft();
        int paddingTop = getPaddingTop();
        int paddingRight = getPaddingRight();
        int paddingBottom = getPaddingBottom();

        float mRadius = mCenterX - paddingTop - paddingBottom;
        canvas.translate(mCenterX,mCenterY);
//        L.MyLog(TAG,"isDoneScan:"+isDoneScan+"---isFindDev:"+isFindDev);
        if (isDoneScan&&!isFindDev){//扫描失败
            TextPaint mPaintErr = new TextPaint();
            mPaintErr.setAntiAlias(true);
            mPaintErr.setColor(Color.RED);
            mPaintErr.setStyle(Paint.Style.STROKE);
            canvas.drawCircle(0, 0, mRadius, mPaintErr);
            for (int i=0;i<4;i++){
                xChartCalc.CalcArcEndPointXY(0,0,mRadius,90*i+45);
                canvas.drawLine(0,0,xChartCalc.getPosX(),xChartCalc.getPosY(),mPaintErr);
            }
        }else{
            TextPaint mLinePaint = new TextPaint();
            mLinePaint.setAntiAlias(true);
            mLinePaint.setColor(ContextCompat.getColor(getContext(),R.color.white));
            canvas.drawLine(0,-mRadius,0,mRadius,mLinePaint);
            canvas.drawLine(-mRadius,0,mRadius,0,mLinePaint);

            int[] colors = {ContextCompat.getColor(getContext(),R.color.txtColor_bar),
                    ContextCompat.getColor(getContext(),R.color.white),
                    ContextCompat.getColor(getContext(),R.color.txtColor_bar),
                    ContextCompat.getColor(getContext(),R.color.white),
                    ContextCompat.getColor(getContext(),R.color.txtColor_bar)};
        /*设置paint的 style 为STROKE：空心*/
            mPaint.setStyle(Paint.Style.STROKE);

            SweepGradient sweepGradient = new SweepGradient(0, 0, colors, new float[]{0f,0.2f,0.5f,0.8f,1f});
            mPaint.setShader(sweepGradient);
            canvas.drawCircle(0, 0, mRadius, mPaint);

            float ring=(mRadius-20)*0.35f;
            sweepGradient = new SweepGradient(0, 0, colors, new float[]{0f,0.1f,0.4f,0.7f,1f});
            mPaint.setShader(sweepGradient);
            canvas.drawCircle(0, 0, mRadius-ring, mPaint);

            sweepGradient = new SweepGradient(0, 0, colors, new float[]{0f,0.3f,0.7f,0.9f,1f});
            mPaint.setShader(sweepGradient);
            canvas.drawCircle(0, 0, mRadius-ring*2, mPaint);

            canvas.save();
            canvas.rotate(cirAngle,0,0);
            mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
            SweepGradient sweepGradientScan = new SweepGradient(0, 0,
                    new int[]{ Color.TRANSPARENT,
                            Color.TRANSPARENT,
                            ContextCompat.getColor(getContext(),R.color.txtColor_bar)}, new float[]{0f,0.8f,1f});
            mPaint.setShader(sweepGradientScan);
            canvas.drawCircle(0, 0, mRadius, mPaint);
        }



    }

    /**/
    public void setScanAll(boolean scanAll) {
        if (scanAll){
            MyApplication.devScanClean();
        }

    }

    public void startScan(boolean isStart){
        new ScanThread().start();
        setScanAll(isStart);
        isDoneScan=false;
        scanDev();
    }

    public void startScan(String bssid, InetAddress inetAddress){
        new ScanThread().start();
        setScanAll(true);
        isDoneScan=false;
        scanDev(bssid,inetAddress);
    }

    public void stopScan(){
        stopUi();
    }



    private Handler mHandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 1:
                    invalidate();
                    break;
                case DONE_SCAN:
                    invalidate();
                    if (scanListener!=null)
                        scanListener.scanDoneDev(RadarView.this,isFindDev);
                    break;
                case FIND_DEVID:
                    initReciverDev((Dev) msg.obj);
                    break;
                case DEVID_Bind:
                    if (scanListener!=null)
                        scanListener.scanBindDev(RadarView.this,(Dev) msg.obj);
                    break;

            }

        }
    };

    private void initReciverDev(Dev dev){
       if (scanListener!=null)
            scanListener.scanDev(this,dev);
    }

    public synchronized boolean isStart() {
        return isStartUi;
    }

    public synchronized void startUi() {
        isStartUi = true;
    }

    public synchronized void stopUi() {
        isStartUi = false;
    }
    private class ScanThread extends Thread{
        public ScanThread() {
            startUi();
        }

        @Override
        public void run() {
            super.run();
            while (isStart()){
//                L.MyLog(TAG,currentThread().getName()+":"+isStart()+"");
                try {
                    cirAngle++;
                    if (cirAngle>360)
                        cirAngle=0;
                    mHandler.sendEmptyMessage(1);
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    e.printStackTrace();

                }
            }

        }
    }

    class StartUDPThread extends Thread {
        String ip = "";

        public StartUDPThread(String ip) {
            this.ip = ip;
        }

        public void run() {
            isFindDev=false;
            new UDPThread(ip, 0).start();
            /*for (int i = 1; i < 255; i++) {
                if (isDoneScan) {
                    mHandler.sendEmptyMessage(DONE_SCAN);
                    return;
                }

                new UDPThread(ip, i).start();

                try {
                    Thread.sleep(MyApplication.DEV_UDP_SEND_DELAY);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

            }*/


            return;



        }

    }

    class UDPThread extends Thread {
        String Hostip = "";
        String ip = "";
        int position;
        int port = MyApplication.DEV_UDP_SEND_PORT;


        public UDPThread(String ipStr, int i) {
            this.position=i;
            this.Hostip = ipStr;
            if (i==0){
                ip="255.255.255.255";
            }else if(i==-1){
                ip=ipStr;
            }else{
                byte[] addr = IpTools.getIpV4Byte(ipStr);
                if (addr.length == 4) {
                    addr[3] = (byte) (i);
                    ip = IpTools.getIpV4StringByByte(addr, 0);
                }
            }

        }

        public String echo(String msg) {
            return " adn echo:" + msg;
        }

        public void run() {
            DatagramSocket dSocket = null;
            String msg = "";
            byte[] buf = new byte[1024];
            DatagramPacket dp = new DatagramPacket(buf, 1024);
            InetAddress local = null;
            try {
                local = InetAddress.getByName(ip); // 本机测试
                // local = InetAddress.getLocalHost(); // 本机测试
                L.MyLog(TAG,"扫描:local" + local);
            } catch (UnknownHostException e) {
                e.printStackTrace();
            }
            try {
                dSocket = new DatagramSocket(); // 注意此处要先在配置文件里设置权限,否则会抛权限不足的异常
            } catch (SocketException e) {
                e.printStackTrace();
            }

            String localPort;
            localPort= dSocket.getLocalPort() + "";
//            localPort="8943";
            L.MyLog(TAG,"扫描:" + "Hostip:" + Hostip + "  ip:" + ip
                    + "   localPort:" + localPort);

            msg = "RPL:\"" + Hostip + "\",\"" + localPort + "\"";
//           L.MyLog(TAG, "扫描:" + msg);

            int msg_len = msg == null ? 0 : msg.getBytes().length;
            DatagramPacket dPacket = new DatagramPacket(msg.getBytes(),
                    msg_len, local, port);
            // String addr="";

            // addr = dPacket.getAddress().toString();

            // dPacket.getPort();
            // System.out.println("local:"+local);
            // dPacket.setData();
            try {

                // 发送设置为广播
                dSocket.setBroadcast(true);
                dSocket.setSoTimeout(10000);
                dSocket.send(dPacket);
                dSocket.receive(dp);
                String strInfo = new String(dp.getData(), 0, dp.getLength());
                L.MyLog(TAG,"扫描receive:"+strInfo);
                String str = strInfo;
                String[] tmp = str.split(":");
                for (String s : tmp) {
                    L.MyLog(TAG, "item1:" + s);
                }
                if (tmp.length >= 2) {
                    str = tmp[1];
                    tmp = str.split(",");
//                    for (String s : tmp) {
//                        L.MyLog(TAG, "item2:" + s);
//                    }

                    if (tmp.length>=5){
                        String idStr = tmp[0].replace('"', ' ').trim();
                        if (scanIsDev.containsKey(idStr)){//已扫描过
                            L.MyLog(TAG, idStr+":"+StrTools.StrHexLowToLong(idStr) + " : 已扫描");
                        }else{
                            scanIsDev.put(idStr,true);
                            Dev dev = MyApplication.getDevScanByIdHex(idStr);
                            boolean isNull=dev==null;
                            dev=new Dev();
                            String passStr = tmp[1].replace('"', ' ').trim();
                            dev.setIdHex(idStr);
                            dev.setDevType(tmp[4].replace('"', ' ').trim());

                            StrTools.StrHexLowToLong(idStr);
                            StrTools.StrHexLowToLong(passStr);
                            StrTools.StrHexHighToLong(idStr);
                            StrTools.StrHexHighToLong(passStr);
                            // int id = Int
                            dev.setId(StrTools.StrHexLowToLong(idStr) + "");

                            dev.setPass(StrTools.StrHexHighToLong(passStr) + "");
                            MyApplication.putDevScan(dev);

                            if (isNull){
                                isFindDev=true;
                                isDoneScan=true;
                                stopScan();
                                Message message = new Message();
                                message.what = FIND_DEVID;
                                message.obj=dev;
                                mHandler.sendMessage(message);
                            }else {
                                isFindDev=true;
                                isDoneScan=true;
                                stopScan();
                                Message message = new Message();
                                message.what = DEVID_Bind;
                                message.obj=dev;
                                mHandler.sendMessage(message);
                            }
                        }



                    }


                }

            } catch (IOException e) {
                e.printStackTrace();
            }finally {
                isDoneScan=true;
                mHandler.sendEmptyMessage(DONE_SCAN);
                stopScan();
                dSocket.close();
            }



        }
    }

    private void scanDev(){
        String ip = IpTools.getIp((WifiManager)getContext().getSystemService(Context.WIFI_SERVICE));
        if (ip.length() < 4) {
            ip = "192.168.1.255";
        }
        if (scanListener!=null){
            scanListener.startScan(this);
        }
        new StartUDPThread(ip).start();
    }

    private void scanDev(String bssid, InetAddress inetAddress){
        if (scanListener!=null){
            scanListener.startScan(this);
        }
        isFindDev=false;
        new UDPThread(inetAddress.getHostAddress(), -1).start();
    }

}

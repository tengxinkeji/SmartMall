package cn.lanmei.com.smartmall.dialog;

import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import com.common.app.MyApplication;
import com.common.app.StaticMethod;
import com.common.app.degexce.L;
import com.common.dialog.ProgressDialog_F;
import com.common.net.NetData;
import com.common.net.RequestParams;
import com.common.net.URLRequest;
import com.demo.smarthome.device.Dev;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;

import cn.lanmei.com.smartmall.R;
import cn.lanmei.com.smartmall.parse.ParserJson;
import cn.lanmei.com.smartmall.socket.WebSocketDevService;
import smartmall.socket.IBackService;


/**
 *扫描到设备
 */
@SuppressLint("ValidFragment")
public class DF_devices extends DialogFragment {
	private String TAG="DF_devices";
	private View view;

	private TextView txtMsg;
	private TextView txtPositive;
	private TextView txtScanNext;
	private boolean isShowNext=true;
	private String msgErr="";

	private Dev dev;
	private boolean isBindDev;
//	private SendMsgLinkedMap sendMsgMap;
	private IBackService iBackService;
	private ServiceConnection conn = new ServiceConnection() {

		@Override
		public void onServiceDisconnected(ComponentName name) {
			iBackService = null;
			L.MyLog(TAG,"onServiceDisconnected---iBackService="+iBackService);
		}

		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			iBackService = IBackService.Stub.asInterface(service);
			L.MyLog(TAG,"onServiceConnected---iBackService="+iBackService);
			if (iBackService!=null){
				try {
					iBackService.initServer(dev.getIdHex());

					if (iBackService!=null){
						try {
							Calendar cal = Calendar.getInstance();
							//当前时：HOUR_OF_DAY-24小时制；HOUR-12小时制
							int hour = cal.get(Calendar.HOUR_OF_DAY);
							//当前分
							int minute = cal.get(Calendar.MINUTE);
							//当前秒
							int second = cal.get(Calendar.SECOND);
							iBackService.sendMessage("time:" +hour+"." +minute+"." +second);
						} catch (RemoteException e) {
							e.printStackTrace();
						}
					}
					closeDF();
				} catch (RemoteException e) {
					e.printStackTrace();
				}
			}
		}
	};

	public DF_devices(Dev dev) {
		this.dev = dev;
//		sendMsgMap=new SendMsgLinkedMap(dev.getIdHex());


	}
	public DF_devices(Dev dev, boolean isBind) {
		this.dev = dev;
		this.isBindDev = isBind;
//		sendMsgMap=new SendMsgLinkedMap(dev.getIdHex());


	}
	private Intent mServiceIntent;
	private void initSocket(){
		mServiceIntent = new Intent(getActivity(), WebSocketDevService.class);
		getActivity().startService(mServiceIntent);
		getActivity().bindService(mServiceIntent, conn, Context.BIND_AUTO_CREATE);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState)
	{

		getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
		getDialog().setCanceledOnTouchOutside(false);

		view = inflater.inflate(R.layout.layout_dialog_scandev, container);

		txtMsg= (TextView) view.findViewById(R.id.txt_msg);
		txtScanNext= (TextView) view.findViewById(R.id.txt_next);
		txtScanNext.setVisibility(isShowNext?View.VISIBLE:View.GONE);
		String msg="名称："+ MyApplication.getDevType(Integer.parseInt(dev.getDevType()))
				+(isBindDev?"(已绑定)":"(未绑定)")
				+"\n"+"标识码："+dev.getId();
		txtMsg.setText(msg);
		txtPositive= (TextView) view.findViewById(R.id.txt_positive);
		if (isBindDev){
			txtPositive.setText("取消");
		}
		txtPositive.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
					if (isBindDev){
						dismiss();
					}else {
						requestBindDev();
					}

			}
		});

		txtScanNext.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (dfScanListener!=null)
					dfScanListener.scanNext();
				dismiss();
			}
		});
		return view;
	}


	@Override
	public void onResume() {
		super.onResume();
		getDialog().getWindow().setLayout(StaticMethod.dip2px(getActivity(),300), StaticMethod.dip2px(getActivity(),200));
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		if (dfScanListener!=null)
			dfScanListener.cancle();
		if (mServiceIntent!=null){
			getActivity().unbindService(conn);
			getActivity().stopService(mServiceIntent);
		}

	}

	ProgressDialog_F progressDialogF;
	private Handler mHandler=new Handler(){
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what){
				case 1:
					if (progressDialogF==null)
						progressDialogF=new ProgressDialog_F();
					progressDialogF.show(getActivity().getSupportFragmentManager(),"ProgressDialog_F");
					break;

				case 4://绑定成功
					if (progressDialogF!=null)
						progressDialogF.dismiss();
					StaticMethod.showInfo(getActivity(),"绑定成功");

					String msgStr="名称："+ MyApplication.getDevType(Integer.parseInt(dev.getDevType()))
							+(isBindDev?"(已绑定)":"(未绑定)")
							+"\n"+"标识码："+dev.getId();
					txtMsg.setText(msgStr);
					txtPositive.setEnabled(false);
					initSocket();
					break;
				case 5:
					StaticMethod.showInfo(getActivity(),msgErr);
					if (progressDialogF!=null)
						progressDialogF.dismiss();
					break;
			}
		}
	};

	private void closeDF(){
		dismiss();
	}
	private void requestBindDev(){
		new Thread(new Runnable() {
			@Override
			public void run() {
				mHandler.sendEmptyMessage(1);
				String uid= MyApplication.getInstance().getUid();
				RequestParams requestParams=new RequestParams(NetData.ACTION_Member_device);
				requestParams.addParam("uid", MyApplication.getInstance().getUid());
				requestParams.addParam("device_type",dev.getDevType());
				requestParams.addParam("device_no",dev.getIdHex());
				requestParams.addParam("device_id",dev.getId());
				requestParams.addParam("password",dev.getPass());
				requestParams.setBaseParser(new ParserJson());
				L.MyLog("df_AsyncTask:绑定设备：",requestParams.getRequestUrl()+"");
				JSONObject result= (JSONObject) URLRequest.requestByGet(requestParams);
				if (result==null){
					msgErr="提交失败";
					mHandler.sendEmptyMessage(5);
					return ;
				}
				if (result!=null){
					try {
						if (result.getInt("status")==1){
							isBindDev=true;
							mHandler.sendEmptyMessage(4);
						}else{
							msgErr="此设备已被绑定";
							mHandler.sendEmptyMessage(5);

						}
					} catch (JSONException e) {
						e.printStackTrace();
						msgErr="绑定失败";
						mHandler.sendEmptyMessage(5);

					}
				}else{
					msgErr="绑定失败";
					mHandler.sendEmptyMessage(5);
				}
			}
		}).start();


	}

	public void setShowNext(boolean showNext) {
		isShowNext = showNext;
		if (txtScanNext!=null)
			txtScanNext.setVisibility(isShowNext?View.VISIBLE:View.GONE);
	}

	DFScanListener dfScanListener;

	public void setDfScanListener(DFScanListener dfScanListener) {
		this.dfScanListener = dfScanListener;
	}

	public interface DFScanListener{
		public void cancle();
		public void scanNext();
	}

}

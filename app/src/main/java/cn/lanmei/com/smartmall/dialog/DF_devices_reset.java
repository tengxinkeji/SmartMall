package cn.lanmei.com.smartmall.dialog;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import com.common.app.StaticMethod;
import com.common.app.degexce.L;
import com.demo.smarthome.device.Dev;

import cn.lanmei.com.smartmall.R;
import cn.lanmei.com.smartmall.socket.SocketAction;
import cn.lanmei.com.smartmall.socket.WebSocketDevService;
import smartmall.socket.IBackService;

/**
 *重置设备
 */
@SuppressLint("ValidFragment")
public class DF_devices_reset extends DialogFragment{
	private String TAG="DF_devices_reset";
	private View view;
	private TextView txtTitle;
	private TextView txtMsg;
	private TextView txtLeft;
	private TextView txtRight;


	private Dev dev;
//	private SendMsgLinkedMap sendMsgMap;
	private IBackService iBackService;
	private MessageBackReciver mReciver;
	private LocalBroadcastManager mLocalBroadcastManager;
	boolean isOnline=false;
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
			try {
				iBackService.initServer(dev.getIdHex());
				iBackService.startSendHeartMessage(dev.getIdHex());
			} catch (RemoteException e) {
				e.printStackTrace();
			}
		}
	};

	public DF_devices_reset(Dev dev) {
		this.dev = dev;


	}
	private Intent mServiceIntent;
	private IntentFilter mIntentFilter;
	private void initSocket(){
		mLocalBroadcastManager = LocalBroadcastManager.getInstance(getActivity());
		mReciver = new MessageBackReciver();

		mServiceIntent = new Intent(getActivity(), WebSocketDevService.class);
//        mServiceIntent = new Intent(getActivity(), BackService.class);
		getActivity().startService(mServiceIntent);

		mIntentFilter = new IntentFilter();
		mIntentFilter.addAction(SocketAction.ACTION_online);
		mIntentFilter.addAction(SocketAction.ACTION_offline);
		mIntentFilter.addAction(SocketAction.LOGIN_ACTION);
		mIntentFilter.addAction(SocketAction.MESSAGE_ACTION);
		mLocalBroadcastManager.registerReceiver(mReciver, mIntentFilter);
		getActivity().bindService(mServiceIntent, conn, Context.BIND_AUTO_CREATE);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState)
	{

		getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
		getDialog().setCanceledOnTouchOutside(false);

		view = inflater.inflate(R.layout.layout_df_device_reset, container);
		txtTitle= (TextView) view.findViewById(R.id.title);
		txtMsg= (TextView) view.findViewById(R.id.txt_msg);
		txtLeft= (TextView) view.findViewById(R.id.txt_left);
		txtRight= (TextView) view.findViewById(R.id.txt_right);

		txtRight.setEnabled(false);

		txtLeft.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				dismiss();
			}
		});

		txtRight.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				deviceReset();
			}
		});
		initSocket();

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
		try {
			iBackService.removeSendHeartMessage();
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		if (mServiceIntent!=null){
			mLocalBroadcastManager.unregisterReceiver(mReciver);
			getActivity().unbindService(conn);
			getActivity().stopService(mServiceIntent);
		}

	}

	class MessageBackReciver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();

			switch (action) {
				case SocketAction.ACTION_online://
					L.MyLog(TAG,"热水器联网中...");
					isOnline=true;
					txtRight.setEnabled(true);
					if (startReset>=1){
						txtMsg.setText("重置中");
					}else {
						txtMsg.setText("热水器联网中...");
					}

					break;
				case SocketAction.ACTION_offline://
					L.MyLog(TAG,"热水器联网中断");
					isOnline=false;
					txtRight.setEnabled(false);
					if (startReset==2){
						txtMsg.setText("重置成功");
					}else {
						txtMsg.setText("热水器联网中断(正在连接)");
					}


					break;
				case SocketAction.LOGIN_ACTION://
					L.MyLog(TAG,"登录");
					break;
				case SocketAction.MESSAGE_ACTION://
					String message = intent.getStringExtra("message");
					parserReciverMsg(message);
					break;

			}
		};
	}

	private int startReset;

	/**重置设备*/
	public void deviceReset(){
		if (startReset>=1)
			return;
		if (iBackService!=null){
			try {
				startReset=1;
				txtMsg.setText("重置中");
				txtRight.setEnabled(false);
				iBackService.sendMessage("reset");
				startReset=2;
			} catch (RemoteException e) {
				e.printStackTrace();

			}

		}
	}

	private void parserReciverMsg(String message) {
		L.MyLog(TAG,message+"");
	}
}

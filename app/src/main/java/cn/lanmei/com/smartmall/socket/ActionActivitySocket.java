package cn.lanmei.com.smartmall.socket;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v4.content.LocalBroadcastManager;
import android.view.View;
import android.widget.Button;

import com.common.app.degexce.L;

import cn.lanmei.com.smartmall.R;
import cn.lanmei.com.smartmall.main.BaseActionActivity;
import smartmall.socket.IBackService;

public class ActionActivitySocket extends BaseActionActivity {
	private static final String TAG = "ActionActivitySocket";

	private Button txtSend;

	private IBackService iBackService;
	private ServiceConnection conn = new ServiceConnection() {

		@Override
		public void onServiceDisconnected(ComponentName name) {
			iBackService = null;

		}

		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			iBackService = IBackService.Stub.asInterface(service);
		}
	};


	private Intent mServiceIntent;

	@Override
	public void setTitle(String title) {

	}

	class MessageBackReciver extends BroadcastReceiver {



		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();

			if (action.equals(SocketAction.LOGIN_ACTION)) {
				L.MyLog(TAG,"登录");

			} else {
				String message = intent.getStringExtra("message");
				L.MyLog(TAG,"socket接收："+message);
			}
		};
	}

	private MessageBackReciver mReciver;

	private IntentFilter mIntentFilter;

	private LocalBroadcastManager mLocalBroadcastManager;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);


		mLocalBroadcastManager = LocalBroadcastManager.getInstance(this);
		mReciver = new MessageBackReciver();

		mServiceIntent = new Intent(this, BackService.class);
		startService(mServiceIntent);

		mIntentFilter = new IntentFilter();
		mIntentFilter.addAction(SocketAction.LOGIN_ACTION);
		mIntentFilter.addAction(SocketAction.MESSAGE_ACTION);
		mLocalBroadcastManager.registerReceiver(mReciver, mIntentFilter);
		bindService(mServiceIntent, conn, BIND_AUTO_CREATE);
	}

	@Override
	public void loadViewLayout() {
		setMContentView(R.layout.layout_socket);

	}

	@Override
	public void mfindViewById() {
		txtSend =(Button) findViewById(R.id.send);
		txtSend.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {



				try {
					String msg ="{\"TYPE\":1,\"ID\":\"123\",\"PW\":\"111111\"}";
					boolean isSend = iBackService.sendMessage(msg);//Send Content by socket
					L.MyLog(TAG,"send:"+(isSend ? "success" : "fail"));


				} catch (RemoteException e) {
					e.printStackTrace();
				}
			}
		});
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		unbindService(conn);
		stopService(mServiceIntent);
		mLocalBroadcastManager.unregisterReceiver(mReciver);
	}



}

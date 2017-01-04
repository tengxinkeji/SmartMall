package cn.lanmei.com.smartmall.dialog;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.DialogFragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;

import com.common.app.Common;
import com.common.app.MyApplication;
import com.common.app.SharePreferenceUtil;
import com.common.app.StaticMethod;
import com.common.app.degexce.L;
import com.common.net.NetData;
import com.common.net.NetWorkUtil;
import com.common.net.RequestParams;
import com.common.net.URLRequest;
import com.common.tools.AuthCode;

import org.json.JSONException;
import org.json.JSONObject;

import cn.lanmei.com.smartmall.parse.ParserJson;
import cn.lanmei.com.smartmall.R;

@SuppressLint("ValidFragment")
public class DF_device_del extends DialogFragment{
	private View view;

	private EditText inputCode;
	private TextView txtSendCode;
	private TextView txtOk;
	private String id;
	private String code;


	public DF_device_del(String id) {
		this.id = id;

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState)
	{
		getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
		getDialog().setCanceledOnTouchOutside(false);

		view = inflater.inflate(R.layout.layout_df_device_del, container);
		inputCode= (EditText) view.findViewById(R.id.input_code);
		txtSendCode= (TextView) view.findViewById(R.id.txt_send_code);
		txtOk= (TextView) view.findViewById(R.id.txt_ok);

		txtSendCode.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				sendCode();
			}
		});
		txtOk.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				refer();
			}
		});
		return view;
	}

	@Override
	public void onResume() {
		super.onResume();
		getDialog().getWindow().setLayout(StaticMethod.dip2px(getActivity(),300), StaticMethod.dip2px(getActivity(),220));
	}

	@Override
	public void onDestroy() {
		super.onDestroy();

	}
	String inputCodeStr="";
	private void refer(){
		inputCodeStr=inputCode.getText().toString();
		if (TextUtils.isEmpty(inputCodeStr)){
			StaticMethod.showInfo(getActivity(),"请输入验证码");
			return;
		}
		if (!code.equals(inputCodeStr)){
			StaticMethod.showInfo(getActivity(),"验证码错误，请重新输入");
			return;
		}
		txtOk.setEnabled(false);
		if (NetWorkUtil.netWorkConnection()){
			new Thread(new Runnable() {
				@Override
				public void run() {
					RequestParams requestParams=new RequestParams(NetData.ACTION_Member_device);
					requestParams.addParam("uid", MyApplication.getInstance().getUid());
					requestParams.addParam("del", 1);
					requestParams.addParam("id",id);
					requestParams.setBaseParser(new ParserJson());
					JSONObject jsonObject= (JSONObject) URLRequest.requestByGet(requestParams);
					Message msg = mHandler.obtainMessage();
					msg.what=1;
					msg.obj=jsonObject;
					mHandler.sendMessage(msg);
				}
			}).start();
		}

	}
	private Handler mHandler=new Handler(){
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if (msg.what==1){
				txtOk.setEnabled(true);
				JSONObject jsonObject= (JSONObject) msg.obj;
				if (jsonObject!=null){
					try {
						StaticMethod.showInfo(getActivity(),jsonObject.getString("info"));
						if (jsonObject.getInt("status")==1){
							if (delBindDeviceLisenter!=null)
								delBindDeviceLisenter.delBindDeviceLisenter(true);
							dismiss();

						}
					} catch (JSONException e) {
						e.printStackTrace();
						StaticMethod.showInfo(getActivity(),"解绑失败");
					}
				}else{
					StaticMethod.showInfo(getActivity(),"解绑失败");
				}
			}
		}
	};

	private void sendCode(){
		txtSendCode.setEnabled(false);
		new AsyncTask<Void,Integer,JSONObject>(){
			@Override
			protected void onPreExecute() {
				super.onPreExecute();
			}

			@Override
			protected JSONObject doInBackground(Void... params) {

				code= AuthCode.getInstance().createCode();
				RequestParams requestParams = new RequestParams(NetData.ACTION_send_sms);
				requestParams.addParam("phone", SharePreferenceUtil.getString(Common.User_phone,""));
				requestParams.addParam("content",code);
				L.MyLog("解绑：",SharePreferenceUtil.getString(Common.User_phone,"")+":"+code);
				requestParams.setBaseParser(new ParserJson());
				JSONObject result = (JSONObject) URLRequest.requestByGet(requestParams);
				for (int i=60;i>0;i--){
					publishProgress(i);
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				return result;

			}

			@Override
			protected void onProgressUpdate(Integer... values) {
				super.onProgressUpdate(values);
				if (values[0]<=1){
					txtSendCode.setText("发送验证码");
				}else{
					txtSendCode.setText(values[0]+"");
				}

			}

			@Override
			protected void onPostExecute(JSONObject jsonObject) {
				super.onPostExecute(jsonObject);
				txtSendCode.setEnabled(true);
				if (jsonObject==null){
					StaticMethod.showInfo(getActivity(),"发送失败");
					return;
				}
				try {
					StaticMethod.showInfo(getActivity(),jsonObject.getString("info"));

				} catch (JSONException e) {
					e.printStackTrace();
					StaticMethod.showInfo(getActivity(),"发送失败");
				}
			}
		}.execute();


	}

	private DelBindDeviceLisenter delBindDeviceLisenter;

	public void setDelBindDeviceLisenter(DelBindDeviceLisenter delBindDeviceLisenter) {
		this.delBindDeviceLisenter = delBindDeviceLisenter;
	}

	public interface  DelBindDeviceLisenter{
		public void delBindDeviceLisenter(boolean isSuccess);
	}


}

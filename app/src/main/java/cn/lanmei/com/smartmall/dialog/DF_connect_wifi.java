package cn.lanmei.com.smartmall.dialog;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.common.app.Common;
import com.common.app.StaticMethod;
import com.demo.smarthome.tools.StrTools;
import com.espressif.iot.esptouch.EsptouchResult;
import com.espressif.iot.esptouch.EsptouchTask;
import com.espressif.iot.esptouch.IEsptouchResult;
import com.espressif.iot.esptouch.IEsptouchTask;

import cn.lanmei.com.smartmall.R;
import cn.lanmei.com.smartmall.device.Activity_device_scan;

@SuppressLint("ValidFragment")
public class DF_connect_wifi extends DialogFragment{
	private View view;
	private ProgressBar progressBar;
	private TextView txtMsg;
	private TextView txtCancle;
	private View viewDivider;
	private TextView txtPositive;


	private String apSsid ;
	private String apPassword;
	private String apBssid;

	public DF_connect_wifi(String apSsid, String apPassword, String apBssid) {
		this.apSsid = apSsid;
		this.apPassword = apPassword;
		this.apBssid = apBssid;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState)
	{
		getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
		getDialog().setCanceledOnTouchOutside(false);
		WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();
		lp.alpha = 0.6f;
		getActivity().getWindow().setAttributes(lp);
		getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);

		view = inflater.inflate(R.layout.layout_dialog_msg, container);
		viewDivider= view.findViewById(R.id.divider);
		progressBar= (ProgressBar) view.findViewById(R.id.progressBar);
		txtMsg= (TextView) view.findViewById(R.id.txt_msg);
		txtPositive= (TextView) view.findViewById(R.id.txt_positive);
		txtCancle= (TextView) view.findViewById(R.id.txt_cancle);
		txtCancle.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (mEsptouchTask != null) {
					mEsptouchTask.interrupt();
				}
				esptouchResult=null;
				dismiss();
			}
		});

		txtPositive.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				bindDev();
			}
		});
		initConnect();
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
		WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();
		lp.alpha = 1.0f;
		getActivity().getWindow().setAttributes(lp);
		getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
	}
	EsptouchResult esptouchResult;
	private void bindDev(){
		if (esptouchResult==null)
			return;
		Bundle bundle=new Bundle();
		bundle.putSerializable(Common.KEY_bundle,esptouchResult);
		Intent toIntent=new Intent(getActivity(),Activity_device_scan.class);
		toIntent.putExtra(Common.KEY_bundle,bundle);
		getActivity().startActivity(toIntent);
		dismiss();
	}

	private IEsptouchTask mEsptouchTask;

	private void initConnect(){
		new AsyncTask<String, Void, IEsptouchResult>(){
			@Override
			protected void onPreExecute() {
				super.onPreExecute();
				txtPositive.setEnabled(false);
				esptouchResult=null;
				if (mEsptouchTask != null) {
					mEsptouchTask.interrupt();
				}
			}

			@Override
			protected IEsptouchResult doInBackground(String... params) {
				mEsptouchTask = new EsptouchTask(apSsid, apBssid, apPassword, true,getActivity());
				IEsptouchResult result = mEsptouchTask.executeForResult();
				return result;

			}

			@Override
			protected void onPostExecute(final IEsptouchResult iEsptouchResult) {
				super.onPostExecute(iEsptouchResult);
				progressBar.setVisibility(View.INVISIBLE);

				if (!iEsptouchResult.isCancelled()) {
					if (iEsptouchResult.isSuc()) {
						txtPositive.setVisibility(View.VISIBLE);
						txtMsg.setText("设备ID:"+ StrTools.StrHexLowToLong(iEsptouchResult.getBssid()));
						txtPositive.setText("绑定设备");
						esptouchResult= (EsptouchResult) iEsptouchResult;
						txtPositive.setEnabled(true);
					} else {
						txtMsg.setText("找不到设备或者设备已连接Wifi");
						viewDivider.setVisibility(View.GONE);
						txtPositive.setVisibility(View.GONE);
						txtCancle.setText("我知道了");

					}
				}


			}
		}.execute();
	}

}

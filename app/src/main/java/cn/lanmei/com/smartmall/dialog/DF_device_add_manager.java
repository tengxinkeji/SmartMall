package cn.lanmei.com.smartmall.dialog;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;

import com.common.app.MyApplication;
import com.common.app.StaticMethod;
import com.common.net.NetData;
import com.common.net.RequestParams;
import com.common.net.URLRequest;

import org.json.JSONException;
import org.json.JSONObject;

import cn.lanmei.com.smartmall.parse.ParserJson;
import cn.lanmei.com.smartmall.R;

@SuppressLint("ValidFragment")
public class DF_device_add_manager extends DialogFragment{
	private View view;

	private EditText inputUser;
	private TextView txtOk;
	private String id;

	public DF_device_add_manager(String id) {
		this.id = id;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState)
	{
		getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
		getDialog().setCanceledOnTouchOutside(false);

		view = inflater.inflate(R.layout.layout_df_device_add_manager, container);
		inputUser= (EditText) view.findViewById(R.id.input_user);
		txtOk= (TextView) view.findViewById(R.id.txt_ok);

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
		getDialog().getWindow().setLayout(StaticMethod.dip2px(getActivity(),300), StaticMethod.dip2px(getActivity(),200));
	}

	@Override
	public void onDestroy() {
		super.onDestroy();

	}
	String user="";
	private void refer(){
		user=inputUser.getText().toString();
		if (TextUtils.isEmpty(user)){
			StaticMethod.showInfo(getActivity(),"请输入平台账号");
			return;
		}
		txtOk.setEnabled(false);
		new AsyncTask<Void,Void,JSONObject>(){
			@Override
			protected void onPreExecute() {
				super.onPreExecute();
			}

			@Override
			protected JSONObject doInBackground(Void... params) {
				RequestParams requestParams=new RequestParams(NetData.ACTION_Member_device);
				requestParams.addParam("uid", MyApplication.getInstance().getUid());
				requestParams.addParam("phone", user);
				requestParams.addParam("id", id);
				requestParams.setBaseParser(new ParserJson());

				JSONObject jsonObject= (JSONObject) URLRequest.requestByGet(requestParams);

				return jsonObject;
			}

			@Override
			protected void onPostExecute(JSONObject jsonObject) {
				super.onPostExecute(jsonObject);
				txtOk.setEnabled(true);
				if (jsonObject!=null){
					try {
						StaticMethod.showInfo(getActivity(),jsonObject.getString("info"));
						if (jsonObject.getInt("status")==1){
							if (addManagerLisenter!=null)
								addManagerLisenter.addManagerLisenter(true);
							dismiss();
						}
					} catch (JSONException e) {
						e.printStackTrace();
						StaticMethod.showInfo(getActivity(),"添加失败");
					}
				}else{
					StaticMethod.showInfo(getActivity(),"添加失败");
				}
			}
		}.execute();
	}

	private AddManagerLisenter addManagerLisenter;

	public void setAddManagerLisenter(AddManagerLisenter addManagerLisenter) {
		this.addManagerLisenter = addManagerLisenter;
	}

	public interface  AddManagerLisenter{
		public void addManagerLisenter(boolean isSuccess);
	}


}

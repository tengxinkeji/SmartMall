package cn.lanmei.com.smartmall.dialog;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import cn.lanmei.com.smartmall.R;
import cn.lanmei.com.smartmall.myui.circleprogress.OnSeekCircleChangeListener;
import cn.lanmei.com.smartmall.myui.circleprogress.SeekHour;
import cn.lanmei.com.smartmall.myui.circleprogress.SeekMinute;

@SuppressLint("ValidFragment")
public class DF_select_time extends DialogFragment{
	private View view;
	private TextView txtHour;
	private TextView txtMinute;
	private TextView txtCancle;
	private TextView txtSure;
	private SeekHour seekHour;
	private SeekMinute seekMinute;

	private int hour,minute;

	public DF_select_time(DiolagTimeListener diolagTimeListener) {
		this.diolagTimeListener = diolagTimeListener;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState)
	{
		getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
		getDialog().setCanceledOnTouchOutside(false);

		view = inflater.inflate(R.layout.layout_select_time, container);
		txtCancle= (TextView) view.findViewById(R.id.time_cancle);
		txtSure= (TextView) view.findViewById(R.id.time_sure);
		txtHour= (TextView) view.findViewById(R.id.time_hour);
		txtMinute= (TextView) view.findViewById(R.id.time_minute);
		seekHour= (SeekHour) view.findViewById(R.id.seek_time_hour);
		seekMinute= (SeekMinute) view.findViewById(R.id.seek_time_minute);

		hour=7;
		minute=6;
		seekHour.setProgress(hour);
		seekMinute.setProgress(minute);
		txtHour.setText(hour+"");
		txtMinute.setText(minute+"");

		txtHour.setTextColor(ContextCompat.getColor(getContext(),R.color.white));
		txtMinute.setTextColor(ContextCompat.getColor(getContext(),R.color.txtColor_txt));
		txtHour.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				seekMinute.setVisibility(View.GONE);
				seekHour.setVisibility(View.VISIBLE);
				txtHour.setTextColor(ContextCompat.getColor(getContext(),R.color.white));
				txtMinute.setTextColor(ContextCompat.getColor(getContext(),R.color.txtColor_txt));
			}
		});

		txtMinute.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				seekHour.setVisibility(View.GONE);
				seekMinute.setVisibility(View.VISIBLE);
				txtMinute.setTextColor(ContextCompat.getColor(getContext(),R.color.white));
				txtHour.setTextColor(ContextCompat.getColor(getContext(),R.color.txtColor_txt));
			}
		});

		txtCancle.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				dismiss();
			}
		});
		txtSure.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (diolagTimeListener!=null)
					diolagTimeListener.onDialogTimeDismiss(hour,minute);
				dismiss();
			}
		});

		seekHour.setOnSeekCircleChangeListener(new OnSeekCircleChangeListener<SeekHour>() {

			@Override
			public void onProgressChanged(SeekHour seekView, int progress, boolean fromUser) {
				hour=progress;
				txtHour.setText(hour+"");
			}

			@Override
			public void onStartTrackingTouch(SeekHour seekView) {

			}

			@Override
			public void onStopTrackingTouch(SeekHour seekView) {
				hour=seekView.getProgress();
				txtHour.setText(hour+"");
				seekHour.setVisibility(View.GONE);
				seekMinute.setVisibility(View.VISIBLE);
//				L.MyLog(TAG,"onStopTrackingTouch:"+seekView.getProgress()+"");
			}
		});

		seekMinute.setOnSeekCircleChangeListener(
				new OnSeekCircleChangeListener<SeekMinute>() {
					@Override
					public void onProgressChanged(SeekMinute seekView, int progress, boolean fromUser) {
						minute=progress;
						txtMinute.setText(minute+"");
					}

					@Override
					public void onStartTrackingTouch(SeekMinute seekView) {

					}

					@Override
					public void onStopTrackingTouch(SeekMinute seekView) {
						minute=seekView.getProgress();
						txtMinute.setText(minute+"");
					}
				});

		return view;
	}

	@Override
	public void onResume() {
		super.onResume();
//		getDialog().getWindow().setLayout(StaticMethod.dip2px(getActivity(),300), StaticMethod.dip2px(getActivity(),200));
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	private DiolagTimeListener diolagTimeListener;

	public void setDiolagTimeListener(DiolagTimeListener diolagTimeListener) {
		this.diolagTimeListener = diolagTimeListener;
	}

	public interface DiolagTimeListener{
		public void onDialogTimeDismiss(int hour,int minute);
	}

}

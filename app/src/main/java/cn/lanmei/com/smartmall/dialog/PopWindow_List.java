package cn.lanmei.com.smartmall.dialog;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;

import com.common.app.StaticMethod;

import cn.lanmei.com.smartmall.R;

public class PopWindow_List<T extends BaseAdapter> extends PopupWindow {
	private View conentView;
	private PopWindowItemClick onPopWindowItemClick;

	public PopWindow_List(Context context, T adapter, PopWindowItemClick popWindowItemClick){
		int[] wm=StaticMethod.wmWidthHeight(context);
		int h =wm[1];
		int w = wm[0];
		initPop(context,adapter,popWindowItemClick,w/3);
	}

	public PopWindow_List(Context context, T adapter, PopWindowItemClick popWindowItemClick,int width) {
		initPop(context,adapter,popWindowItemClick,width);
	}

	void initPop(Context context, T adapter, PopWindowItemClick popWindowItemClick,int width){
		this.onPopWindowItemClick=popWindowItemClick;
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		conentView = inflater.inflate(R.layout.layout_list, null);

		// 设置SelectPicPopupWindow的View
		this.setContentView(conentView);
		// 设置SelectPicPopupWindow弹出窗体的宽
		this.setWidth(width);
		// 设置SelectPicPopupWindow弹出窗体的高
		this.setHeight(LayoutParams.WRAP_CONTENT);
//		this.setHeight((int) (width*1.4f));
		// 设置SelectPicPopupWindow弹出窗体可点击
		this.setFocusable(true);
		this.setOutsideTouchable(true);
		// 刷新状态
		this.update();
		// 实例化一个ColorDrawable颜色为半透明
		ColorDrawable dw = new ColorDrawable(0000000000);
		// 点back键和其他地方使其消失,设置了这个才能触发OnDismisslistener ，设置其他控件变化等操作
		this.setBackgroundDrawable(dw);
		// mPopupWindow.setAnimationStyle(android.R.style.Animation_Dialog);
		// 设置SelectPicPopupWindow弹出窗体动画效果
//		this.setAnimationStyle(R.style.AnimationPreview);
		ListView listView= (ListView) conentView.findViewById(R.id.list);
		listView.setAdapter(adapter);
		listView.setDivider(new ColorDrawable(ContextCompat.getColor(context,R.color.divider)));
		listView.setDividerHeight(1);
		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				if (onPopWindowItemClick!=null)
					onPopWindowItemClick.onPopWindowItemClick(position);
				dismiss();
			}
		});
	}

	public void showPopupWindow(View parent) {
		if (!this.isShowing()) {
//			this.showAsDropDown(parent, parent.getLayoutParams().width / 2, 18);
			this.showAsDropDown(parent);
		} else {
			this.dismiss();
		}
	}



	public interface PopWindowItemClick{
		public void onPopWindowItemClick(int position);
	}
}


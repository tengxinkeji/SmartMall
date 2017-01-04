package com.city;
import java.util.ArrayList;


import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;



/**
 * 级联菜单碎片
 * @author LILIN
 * 上午11:01:04
 */
public class CascadingMenuFragment extends Fragment {

	private CascadingMenuView cascadingMenuView;
	private ArrayList<Area> areas=null;
    //提供给外的接口
	private CascadingMenuViewOnSelectListener menuViewOnSelectListener;
	private static CascadingMenuFragment instance=null;
	//单例模式
	public static CascadingMenuFragment getInstance(){
		if(instance==null){
			instance=new CascadingMenuFragment();
		}
		return instance;
	}
	
	public void setMenuItems(ArrayList<Area> areas) {
		this.areas = areas;
	}
	
	
	public void setMenuViewOnSelectListener(
			CascadingMenuViewOnSelectListener menuViewOnSelectListener) {
		this.menuViewOnSelectListener = menuViewOnSelectListener;
	}
     
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//实例化级联菜单
		cascadingMenuView=new CascadingMenuView(getActivity(),areas);
		//设置回调接口
		cascadingMenuView.setCascadingMenuViewOnSelectListener(new MCascadingMenuViewOnSelectListener());
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return cascadingMenuView;
	}
	 //级联菜单选择回调接口
		class MCascadingMenuViewOnSelectListener implements CascadingMenuViewOnSelectListener{



		 @Override
		 public void getValue(Area area_1, Area area_2, Area area_3) {
			 if(menuViewOnSelectListener!=null){
				 menuViewOnSelectListener.getValue(area_1,area_2,area_3);
			 }
		 }
	 }
}

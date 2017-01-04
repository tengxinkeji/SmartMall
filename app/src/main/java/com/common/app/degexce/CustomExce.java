package com.common.app.degexce;

import android.util.Log;

public class CustomExce extends Exception {

	/**
	 * @deprecated 自定义异常
	 */
	private static final long serialVersionUID = -7789209208148107204L;
	
	public CustomExce(){
		Log.i("CustomExce--->", "自定义异常");
	}
	
	public CustomExce(String err){
		Log.i("CustomExce--->err", err);
		
	}
	

}

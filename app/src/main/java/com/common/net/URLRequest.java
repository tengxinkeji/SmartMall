package com.common.net;

import com.common.app.degexce.L;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;


public class URLRequest  {
private static String TAG="URLRequest";

	// Get方式请求
	public static String requestByGet(String urlStr)  {
		L.MyLog("url:", urlStr);

		try {
			// 新建一个URL对象
			URL url = new URL(urlStr);
			// 打开一个HttpURLConnection连接
			HttpURLConnection urlConn;
			urlConn = (HttpURLConnection) url.openConnection();
			// 设置连接超时时间
			urlConn.setConnectTimeout(10 * 1000);
			urlConn.setReadTimeout(10*1000);
			// 请求不能使用缓存
			urlConn.setUseCaches(false);
//			urlConn.setDoInput(true);
//			urlConn.setDoOutput(true);
			// 开始连接
			urlConn.connect();
			// 判断请求是否成功
			L.MyLog(TAG,"ResponseCode:"+urlConn.getResponseCode()+"");
			if (urlConn.getResponseCode() == HttpURLConnection.HTTP_OK) {
				// 获取返回的数据
				return convertStreamToString(urlConn.getInputStream()).toString();

			}
			// 关闭连接
			urlConn.disconnect();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

		}
		return null;
	}
	
	// Get方式请求  
	public static Object requestByGet(RequestParams requestParams )  {
		L.MyLog("url:", requestParams.getRequestUrl());
		try {
			// 新建一个URL对象
			URL url = new URL(requestParams.getRequestUrl());
			// 打开一个HttpURLConnection连接
			HttpURLConnection urlConn;
			urlConn = (HttpURLConnection) url.openConnection();
			// 设置连接超时时间
			urlConn.setConnectTimeout(10 * 1000);
			urlConn.setReadTimeout(10*1000);
			// 请求不能使用缓存
			urlConn.setUseCaches(false);
//			urlConn.setDoInput(true);
//			urlConn.setDoOutput(true);
			// 开始连接
			urlConn.connect();
			// 判断请求是否成功
			L.MyLog(TAG,"ResponseCode:"+urlConn.getResponseCode()+"");
			if (urlConn.getResponseCode() == HttpURLConnection.HTTP_OK) {
				// 获取返回的数据
				String data = convertStreamToString(urlConn.getInputStream()).toString();
				return requestParams.baseParser.parserJson(data);
			}
			// 关闭连接
			urlConn.disconnect();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

		}
		return null;
	}


	// Post方式请求
	public static Object requestByPost(RequestParams requestParams) {

//		String path = "https://reg.163.com/logins.jsp";
		String path =requestParams.getRequestUrl();
		L.MyLog("url---post:",path);
		// 新建一个URL对象
		URL url;
		try {
			// 请求的参数转换为byte数组

			String postData = requestParams.getParams();
			L.MyLog("url---postData:",postData);
			url = new URL(path);
			// 打开一个HttpURLConnection连接
			HttpURLConnection urlConn = (HttpURLConnection) url.openConnection();
			//设置参数
			urlConn.setDoOutput(true);   //需要输出
			urlConn.setDoInput(true);   //需要输入
			urlConn.setUseCaches(false);  //不允许缓存
			urlConn.setRequestMethod("POST");   //设置POST方式连接
			//设置请求属性
			urlConn.setRequestProperty("accept", "*/*");
//			urlConn.setRequestProperty("Content-Type", "application/json");
//			urlConn.setRequestProperty("Content-Type", "application/octet-stream");
			urlConn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			urlConn.setRequestProperty("Connection", "Keep-Alive");// 维持长连接
			urlConn.setRequestProperty("Charset", NetData.charsetName);

			//连接,也可以不用明文connect，使用下面的httpConn.getOutputStream()会自动connect
			urlConn.connect();
			//建立输入流，向指向的URL传入参数
			DataOutputStream dos=new DataOutputStream(urlConn.getOutputStream());
			dos.writeBytes(postData);
			dos.flush();
			dos.close();
			// 判断请求是否成功
			L.MyLog(TAG,"ResponseCode:"+urlConn.getResponseCode()+"");
			if (urlConn.getResponseCode() == HttpURLConnection.HTTP_OK) {
				// 获取返回的数据
				String data = convertStreamToString(urlConn.getInputStream()).toString();
				return requestParams.baseParser.parserJson(data);

			}
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

		} catch (ProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

		}

		return null;
	}



	public static StringBuffer convertStreamToString(InputStream is) {

		   BufferedReader reader = new BufferedReader(new InputStreamReader(is));

		        StringBuffer sb = new StringBuffer();

		        String line = null;

		        try {   

		            while ((line = reader.readLine()) != null) {   

		                sb.append(line);   

		            }   

		        } catch (IOException e) {

		            e.printStackTrace();   

		        } finally {   

		            try {   

		                is.close();   

		            } catch (IOException e) {

		                e.printStackTrace();   

		            }   

		        }
		    

		        return sb;

		    } 

}

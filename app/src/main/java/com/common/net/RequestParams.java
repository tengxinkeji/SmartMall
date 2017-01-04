package com.common.net;


import com.common.tools.MD5;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by Administrator on 2015/8/24.
 */
public class RequestParams implements Serializable {

    private static final long serialVersionUID = 4300401889425315130L;

    public String path;
    public Map<String,Object> params;
    public BaseParser<?> baseParser;
    private boolean isPost;

    /**
     * @param path url路径
     *
     * */
    public RequestParams(String path) {
        this.path = path;
        params=new HashMap<String,Object>();
        isPost=false;
    }

    public boolean isPost() {
        return isPost;
    }

    public void setPost(boolean post) {
        isPost = post;
    }

    public void setBaseParser(BaseParser<?> baseParser) {
        this.baseParser = baseParser;
    }

    public void addParam(String key, Object value){
        params.put(key,value);
    }

    public String getParams() {
        if (params==null||params.size()<1){
            return null;
        }else{
            Set<Map.Entry<String, Object>> entrySet = params.entrySet();
            StringBuffer urlStr = new StringBuffer();
            for(Map.Entry<String, Object> item:entrySet){
                urlStr.append("&");
                try {
                    urlStr.append(item.getKey()+"="+URLEncoder.encode(item.getValue().toString(), NetData.charsetName));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
            return urlStr.toString();
        }

    }



    public void setParams(Map<String,Object> params) {
        this.params = params;
    }



    public String getRequestUrl(){
        StringBuffer urlStr = new StringBuffer();
        urlStr.append(NetData.HOST+NetData.PATH+path);
        urlStr.append(NetData.KEY_key+"="+ MD5.getMD5(NetData.KEY_Value));
        if (isPost()){
            if (params.containsKey("uid"))
                urlStr.append("&uid="+ params.get("uid"));
            return urlStr.toString();
        }else {

            String params=getParams();
            if (params!=null)
                urlStr.append(params);
            return urlStr.toString();
        }


    }


}

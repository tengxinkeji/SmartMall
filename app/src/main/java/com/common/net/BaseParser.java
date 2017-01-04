package com.common.net;

import org.json.JSONException;

import java.io.InputStream;

/**
 * Created by Administrator on 2016/7/26.
 */
public abstract class BaseParser<T> {
    public abstract T parserJson(String responseString);
}

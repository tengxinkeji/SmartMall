package com.common.myinterface;

/**
 * Created by Administrator on 2016/8/31.
 */

public class SimpleDataCallBack<T,A> implements DataCallBack<T> {
    A obj;

    public SimpleDataCallBack(A obj) {
        this.obj = obj;
    }

    public void onPre(A obj) {
    }


    public void processData(A obj, T parserData) {

    }


    public void onComplete(A obj) {

    }


    @Override
    public void onPre() {
        onPre(obj);
    }

    @Override
    public void processData(T parserData) {
        processData(obj,parserData);
    }

    @Override
    public void onComplete() {
        onComplete(obj);
    }
}

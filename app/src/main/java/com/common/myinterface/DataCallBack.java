package com.common.myinterface;

public interface DataCallBack<T>{
        public void onPre();
        public void processData(T parserData) ;
        public void onComplete();
    }
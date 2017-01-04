package com.common.app.sd;

import com.common.app.Common;

/**
 * Created by Administrator on 2015/12/17.
 */
public enum Enum_Dir {
    // 利用构造函数传参
    DIR_root(Common.DIR_root),
    DIR_img(Common.DIR_img),
    DIR_video(Common.DIR_video),
    DIR_apk(Common.DIR_apk);

    // 定义私有变量
    private String value;

    // 构造函数，枚举类型只能为私有
    private Enum_Dir(String _value) {
        this.value = _value;
    }

    @Override
    public String toString() {
        return String.valueOf(this.value);// 转换到字符串
    }

    public String value() {
        return this.value;
    }
}

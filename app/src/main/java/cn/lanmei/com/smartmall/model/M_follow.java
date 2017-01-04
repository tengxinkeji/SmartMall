package cn.lanmei.com.smartmall.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/11/1.
 */

public class M_follow {
    private M_merchant mMerchant;
    private List<M_Goods> mGoodsList;

    public M_follow() {
        this.mMerchant = new M_merchant();
        this.mGoodsList = new ArrayList<>();
    }

    public M_merchant getmMerchant() {
        return mMerchant;
    }

    public void setmMerchant(M_merchant mMerchant) {
        this.mMerchant = mMerchant;
    }

    public List<M_Goods> getmGoodsList() {
        return mGoodsList;
    }

    public void setmGoodsList(List<M_Goods> mGoodsList) {
        this.mGoodsList = mGoodsList;
    }
}

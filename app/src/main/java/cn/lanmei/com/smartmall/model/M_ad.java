package cn.lanmei.com.smartmall.model;

import java.util.List;

/**
 * Created by Administrator on 2016/9/20.
 */

public class M_ad {
    private List<Integer> adImgId;
    private List<String> adImgUrl;
    private List<M_ad_item> ads;

    public M_ad(List<Integer> adImgId, List<String> adImgUrl,List<M_ad_item> ads) {
        this.adImgId = adImgId;
        this.adImgUrl = adImgUrl;
        this.ads = ads;
    }

    public List<String> getAdImgUrl() {
        return adImgUrl;
    }

    public void setAdImgUrl(List<String> adImgUrl) {
        this.adImgUrl = adImgUrl;
    }

    public List<Integer> getAdImgId() {
        return adImgId;
    }

    public void setAdImgId(List<Integer> adImgId) {
        this.adImgId = adImgId;
    }

    public List<M_ad_item> getAds() {
        return ads;
    }

    public void setAds(List<M_ad_item> ads) {
        this.ads = ads;
    }
}

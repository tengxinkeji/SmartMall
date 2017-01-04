package cn.lanmei.com.smartmall.model;

import java.util.List;

/**
 * Created by Administrator on 2016/5/24.
 */
public class StructorHotelType {

    /**
     * status : 1
     * info :
     * data : [{"id":"1","pid":"0","title":"商务旅店","sort":"3","status":"1"},{"id":"2","pid":"0","title":"主题旅店","sort":"2","status":"1"},{"id":"3","pid":"0","title":"家庭旅店","sort":"1","status":"1"}]
     */

    private int status;
    private String info;
    /**
     * id : 1
     * pid : 0
     * title : 商务旅店
     * sort : 3
     * status : 1
     */

    private List<DataBean> data;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        private String id;
        private String pid;
        private String title;
        private String sort;
        private String status;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getPid() {
            return pid;
        }

        public void setPid(String pid) {
            this.pid = pid;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getSort() {
            return sort;
        }

        public void setSort(String sort) {
            this.sort = sort;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }
    }
}

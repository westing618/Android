package com.ztd.yyb.bean.beanMess;

import java.util.List;

/**
 * Created by Administrator on 2017/4/9 0009.
 */

public class DataMess {


    /**
     * result : true
     * data : {"noticearray":[{"ygbmstitle":"放大","ygbmscreatetime":"2017-4-06 16:17:22","ygbmscontent":"奋斗史","ygbmid":0,"ygbmsoperator":"admin","ygbmstype":"1","ygbmsid":15,"ygbmsurl":""},{"ygbmstitle":"水电费","ygbmscreatetime":"2017-3-28 10:58:25","ygbmscontent":"第三方","ygbmid":0,"ygbmsoperator":"admin","ygbmstype":"1","ygbmsid":13,"ygbmsurl":""},{"ygbmstitle":"gtre","ygbmscreatetime":"2017-3-22 15:16:10","ygbmscontent":"tewtr5","ygbmid":0,"ygbmsoperator":"admin","ygbmstype":"1","ygbmsid":12,"ygbmsurl":""},{"ygbmstitle":"e3w","ygbmscreatetime":"2017-3-22 15:15:58","ygbmscontent":"rwerewrf","ygbmid":0,"ygbmsoperator":"admin","ygbmstype":"1","ygbmsid":11,"ygbmsurl":""},{"ygbmstitle":"1","ygbmscreatetime":"2017-3-20 09:37:12","ygbmscontent":"1","ygbmid":0,"ygbmsoperator":"admin","ygbmstype":"1","ygbmsid":10,"ygbmsurl":""},{"ygbmstitle":"fgrw","ygbmscreatetime":"2017-3-17 18:08:51","ygbmscontent":"gvf","ygbmid":0,"ygbmsoperator":"admin","ygbmstype":"1","ygbmsid":9,"ygbmsurl":""},{"ygbmstitle":"系统消息","ygbmscreatetime":"2017-3-17 18:08:29","ygbmscontent":"3","ygbmid":0,"ygbmsoperator":"admin","ygbmstype":"1","ygbmsid":6,"ygbmsurl":""},{"ygbmstitle":"t43","ygbmscreatetime":"2017-3-17 18:07:34","ygbmscontent":"rgeyhr","ygbmid":0,"ygbmsoperator":"admin","ygbmstype":"1","ygbmsid":8,"ygbmsurl":""}],"success":"1"}
     * msg : 获取成功
     */

    private String result;
    private DataEntity data;
    private String msg;

    public void setResult(String result) {
        this.result = result;
    }

    public void setData(DataEntity data) {
        this.data = data;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getResult() {
        return result;
    }

    public DataEntity getData() {
        return data;
    }

    public String getMsg() {
        return msg;
    }

    public static class DataEntity {
        /**
         * noticearray : [{"ygbmstitle":"放大","ygbmscreatetime":"2017-4-06 16:17:22","ygbmscontent":"奋斗史","ygbmid":0,"ygbmsoperator":"admin","ygbmstype":"1","ygbmsid":15,"ygbmsurl":""},{"ygbmstitle":"水电费","ygbmscreatetime":"2017-3-28 10:58:25","ygbmscontent":"第三方","ygbmid":0,"ygbmsoperator":"admin","ygbmstype":"1","ygbmsid":13,"ygbmsurl":""},{"ygbmstitle":"gtre","ygbmscreatetime":"2017-3-22 15:16:10","ygbmscontent":"tewtr5","ygbmid":0,"ygbmsoperator":"admin","ygbmstype":"1","ygbmsid":12,"ygbmsurl":""},{"ygbmstitle":"e3w","ygbmscreatetime":"2017-3-22 15:15:58","ygbmscontent":"rwerewrf","ygbmid":0,"ygbmsoperator":"admin","ygbmstype":"1","ygbmsid":11,"ygbmsurl":""},{"ygbmstitle":"1","ygbmscreatetime":"2017-3-20 09:37:12","ygbmscontent":"1","ygbmid":0,"ygbmsoperator":"admin","ygbmstype":"1","ygbmsid":10,"ygbmsurl":""},{"ygbmstitle":"fgrw","ygbmscreatetime":"2017-3-17 18:08:51","ygbmscontent":"gvf","ygbmid":0,"ygbmsoperator":"admin","ygbmstype":"1","ygbmsid":9,"ygbmsurl":""},{"ygbmstitle":"系统消息","ygbmscreatetime":"2017-3-17 18:08:29","ygbmscontent":"3","ygbmid":0,"ygbmsoperator":"admin","ygbmstype":"1","ygbmsid":6,"ygbmsurl":""},{"ygbmstitle":"t43","ygbmscreatetime":"2017-3-17 18:07:34","ygbmscontent":"rgeyhr","ygbmid":0,"ygbmsoperator":"admin","ygbmstype":"1","ygbmsid":8,"ygbmsurl":""}]
         * success : 1
         */

        private String success;
        private List<NoticearrayEntity> noticearray;

        public void setSuccess(String success) {
            this.success = success;
        }

        public void setNoticearray(List<NoticearrayEntity> noticearray) {
            this.noticearray = noticearray;
        }

        public String getSuccess() {
            return success;
        }

        public List<NoticearrayEntity> getNoticearray() {
            return noticearray;
        }

        public static class NoticearrayEntity {
            /**
             * ygbmstitle : 放大
             * ygbmscreatetime : 2017-4-06 16:17:22
             * ygbmscontent : 奋斗史
             * ygbmid : 0
             * ygbmsoperator : admin
             * ygbmstype : 1
             * ygbmsid : 15
             * ygbmsurl :
             */

            private String ygbmstitle;
            private String ygbmscreatetime;
            private String ygbmscontent;
            private String ygbmid;
            private String ygbmsoperator;
            private String ygbmstype;
            private String ygbmsid;
            private String ygbmsurl;

            public void setYgbmstitle(String ygbmstitle) {
                this.ygbmstitle = ygbmstitle;
            }

            public void setYgbmscreatetime(String ygbmscreatetime) {
                this.ygbmscreatetime = ygbmscreatetime;
            }

            public void setYgbmscontent(String ygbmscontent) {
                this.ygbmscontent = ygbmscontent;
            }

            public void setYgbmid(String ygbmid) {
                this.ygbmid = ygbmid;
            }

            public void setYgbmsoperator(String ygbmsoperator) {
                this.ygbmsoperator = ygbmsoperator;
            }

            public void setYgbmstype(String ygbmstype) {
                this.ygbmstype = ygbmstype;
            }

            public void setYgbmsid(String ygbmsid) {
                this.ygbmsid = ygbmsid;
            }

            public void setYgbmsurl(String ygbmsurl) {
                this.ygbmsurl = ygbmsurl;
            }

            public String getYgbmstitle() {
                return ygbmstitle;
            }

            public String getYgbmscreatetime() {
                return ygbmscreatetime;
            }

            public String getYgbmscontent() {
                return ygbmscontent;
            }

            public String getYgbmid() {
                return ygbmid;
            }

            public String getYgbmsoperator() {
                return ygbmsoperator;
            }

            public String getYgbmstype() {
                return ygbmstype;
            }

            public String getYgbmsid() {
                return ygbmsid;
            }

            public String getYgbmsurl() {
                return ygbmsurl;
            }
        }
    }
}

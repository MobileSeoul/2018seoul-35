package tw.sspr.seoulsafepublicrestroom.ResponseBody;

import java.util.Date;

public class ReportGET {
    private String report_id;
    private String restroom_id;
    private String writer;
    private String msg;
    private String img;
    private String updatedate;

    public String getReport_id() {
        return report_id;
    }

    public void setReport_id(String report_id) {
        this.report_id = report_id;
    }

    public String getRestroom_id() {
        return restroom_id;
    }

    public void setRestroom_id(String restroom_id) {
        this.restroom_id = restroom_id;
    }

    public String getWriter() {
        return writer;
    }

    public void setWriter(String writer) {
        this.writer = writer;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getUpdatedate() {
        return updatedate;
    }

    public void setUpdatedate(String updatedate) {
        this.updatedate = updatedate;
    }
}

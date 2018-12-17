package tw.sspr.seoulsafepublicrestroom.Object;

import java.text.SimpleDateFormat;
import java.util.Date;

import tw.sspr.seoulsafepublicrestroom.ResponseBody.ReportGET;

public class ReportItem {
    private String report_id;
    private String restroom_id;
    private String writer;
    private String msg;
    private String img;
    private String updatedate;

    public ReportItem(ReportGET get){
        this.report_id = get.getReport_id();
        this.restroom_id = get.getRestroom_id();
        this.writer = get.getWriter();
        this.msg = get.getMsg();
        this.img = get.getImg();

        Date date = new Date(Long.parseLong(get.getUpdatedate().trim()));
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        this.updatedate = sdf.format(date);
    }

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

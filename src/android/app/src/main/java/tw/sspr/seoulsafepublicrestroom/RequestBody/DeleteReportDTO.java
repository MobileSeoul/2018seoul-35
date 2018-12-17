package tw.sspr.seoulsafepublicrestroom.RequestBody;

import java.text.SimpleDateFormat;
import java.util.Date;

import tw.sspr.seoulsafepublicrestroom.ResponseBody.ReportGET;

public class DeleteReportDTO {
    private String report_id;
    private String pwd;

    public String getReport_id() {
        return report_id;
    }

    public void setReport_id(String report_id) {
        this.report_id = report_id;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }
}

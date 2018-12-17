package com.sspr.domain;

import java.util.Date;

public class ReportVO {
	private String report_id;
	private String restroom_id;
	private String writer;
	private String pwd;
	private String msg;
	private String img;
	private Date updatedate;
	
	
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
	public String getPwd() {
		return pwd;
	}
	public void setPwd(String pwd) {
		this.pwd = pwd;
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
	public Date getUpdatedate() {
		return updatedate;
	}
	public void setUpdatedate(Date updatedate) {
		this.updatedate = updatedate;
	}
	
	
}

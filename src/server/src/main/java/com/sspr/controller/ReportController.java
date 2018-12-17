package com.sspr.controller;

import java.util.List;

import javax.annotation.Resource;
import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.sspr.domain.ReportVO;
import com.sspr.service.ReportService;
import com.sspr.service.RestroomService;
import com.sspr.util.FileManager;
import com.sspr.util.UploadFileUtils;

@RestController
@RequestMapping("report")
public class ReportController {

	private static final String SERVER_STORAGE_URL = "http://devsim.cafe24.com/resources/upload";

	@Resource(name = "uploadPath")
	private String uploadPath;

	@Inject
	private RestroomService memberService;

	@Inject
	private ReportService reportService;

	private static final Logger logger = LoggerFactory.getLogger(HomeController.class);


	@RequestMapping(value="/add", method= RequestMethod.POST,
			headers = ("content-type=multipart/*"))
	public String uploadForm(@RequestParam("file") MultipartFile file,
			String restroom_id,
			String writer,
			String pwd,
			String msg) throws Exception{
		logger.info("originalName : " + file.getOriginalFilename());
		logger.info("size: " + file.getSize());
		logger.info("contentType: " + file.getContentType());

		String url = UploadFileUtils.uploadFile(uploadPath,
				file.getOriginalFilename(),
				file.getBytes());

		System.out.println(file.getOriginalFilename());
		System.out.println(uploadPath + url);
		System.out.println(restroom_id +"|" +writer+"|" +pwd+"|" +msg);

		ReportVO report = new ReportVO();
		report.setRestroom_id(restroom_id);
		report.setWriter(writer);
		report.setPwd(pwd);
		report.setMsg(msg);

		report.setImg(SERVER_STORAGE_URL+url);

		reportService.insert(report);

		return file.getOriginalFilename();
	}

	@RequestMapping(value="get", method=RequestMethod.GET)
	public List<ReportVO> readReports(@RequestParam String restroom_id){
		List<ReportVO> results = reportService.readReports(restroom_id);
		System.out.println("size : " + results.size());
		return results;
	}

	@RequestMapping(value="delete", method=RequestMethod.POST)
	public void deleteReport(@RequestBody ReportVO vo){
		ReportVO item = reportService.readReport(vo.getReport_id());
		if(item != null){
			FileManager.delete(uploadPath, item.getImg());
			reportService.delete(vo);
		}
	}


}

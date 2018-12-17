package com.sspr.service;

import java.util.List;

import javax.inject.Inject;

import org.springframework.stereotype.Service;

import com.sspr.domain.ReportVO;
import com.sspr.persistence.ReportDAO;

@Service
public class ReportServiceImpl implements ReportService{

	@Inject
	private ReportDAO dao;

	@Override
	public void insert(ReportVO vo) {
		// TODO Auto-generated method stub
		dao.insertReport(vo);
	}

	@Override
	public List<ReportVO> readReports(String restroom_id) {
		// TODO Auto-generated method stub
		return dao.readReports(restroom_id);
	}
	

	@Override
	public ReportVO readReport(String report_id) {
		// TODO Auto-generated method stub
		return dao.readReport(report_id);
	}

	@Override
	public void delete(ReportVO vo) {
		// TODO Auto-generated method stub
		dao.deleteReport(vo);
	}
	
}

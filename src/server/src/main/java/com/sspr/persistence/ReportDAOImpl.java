package com.sspr.persistence;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import com.sspr.domain.ReportVO;

@Repository
public class ReportDAOImpl implements ReportDAO{
	
	@Inject
	private SqlSession sqlSession;
	
	private static final String namespace =
			"com.sspr.mappers.ReportMapper";
	
	@Override
	public void insertReport(ReportVO vo) {
		// TODO Auto-generated method stub
		sqlSession.insert(namespace + ".insertReport",vo);
		
	}

	@Override
	public List<ReportVO> readReports(String restroom_id) {
		// TODO Auto-generated method stub
		return sqlSession.selectList(namespace + ".readReports", restroom_id);
	}
	
	

	@Override
	public ReportVO readReport(String report_id) {
		// TODO Auto-generated method stub
		return sqlSession.selectOne(namespace + ".readReport", report_id);
	}

	@Override
	public void deleteReport(ReportVO vo) {
		// TODO Auto-generated method stub
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("report_id", vo.getReport_id());
		map.put("pwd", vo.getPwd());
		sqlSession.delete(namespace + ".deleteReport", map);
	}
	
	

}

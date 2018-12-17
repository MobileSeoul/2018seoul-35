package com.sspr.persistence;

import javax.inject.Inject;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import com.sspr.domain.RestroomVO;

@Repository
public class RestroomDAOImpl implements RestroomDAO{
	
	@Inject
	private SqlSession sqlSession;
	
	private static final String namespace =
			"com.sspr.mappers.RestroomMapper";
	
	@Override
	public RestroomVO readRestroom(String id) {
		// TODO Auto-generated method stub
		RestroomVO vo = sqlSession.selectOne(namespace+".readRestrrom",id);
		return vo;
	}

	@Override
	public void insertRestroom(RestroomVO vo) {
		// TODO Auto-generated method stub
		sqlSession.insert(namespace+".insertRestroom",vo);
		
	}

	@Override
	public void updateRestroom(RestroomVO vo) {
		// TODO Auto-generated method stub
		sqlSession.update(namespace+".updateRestroom",vo);
	}

}

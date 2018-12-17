package com.sspr.service;

import javax.inject.Inject;

import org.springframework.stereotype.Service;

import com.sspr.domain.RestroomVO;
import com.sspr.persistence.RestroomDAO;

@Service
public class RestroomServiceImpl implements RestroomService{

	@Inject
	private RestroomDAO dao;
	
	@Override
	public RestroomVO read(String id) {
		// TODO Auto-generated method stub
		return dao.readRestroom(id);
	}

	@Override
	public void insert(RestroomVO vo) {
		// TODO Auto-generated method stub
		dao.insertRestroom(vo);
	}

	@Override
	public void update(RestroomVO vo) {
		// TODO Auto-generated method stub
		dao.updateRestroom(vo);
	}
	
}

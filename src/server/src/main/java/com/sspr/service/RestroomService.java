package com.sspr.service;

import com.sspr.domain.RestroomVO;

public interface RestroomService {
	public RestroomVO read(String id);
	public void insert(RestroomVO vo);
	public void update(RestroomVO vo);
}

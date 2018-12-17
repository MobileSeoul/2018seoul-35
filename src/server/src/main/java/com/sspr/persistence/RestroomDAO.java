package com.sspr.persistence;

import com.sspr.domain.RestroomVO;

public interface RestroomDAO {
	public RestroomVO readRestroom(String id);
	public void insertRestroom(RestroomVO vo);
	public void updateRestroom(RestroomVO vo);
}

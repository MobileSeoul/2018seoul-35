package com.sspr.controller;

import javax.inject.Inject;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sspr.domain.RestroomVO;
import com.sspr.service.RestroomService;

@RestController
@RequestMapping("restroom")
public class RestroomController {
	
	@Inject
	private RestroomService memberService;
	
	@RequestMapping(value="update", method=RequestMethod.POST)
	public void updateRestroom(@RequestBody RestroomVO input){
		RestroomVO vo = memberService.read(input.getId());
		if(vo == null){
			memberService.insert(input);
		}else{
			memberService.update(input);
		}
	}
	
	@RequestMapping(value="get", method=RequestMethod.GET)
	public RestroomVO readRestroom(@RequestParam String id){
		RestroomVO vo = memberService.read(id);
		if(vo == null){
			vo = new RestroomVO();
			vo.setId(id);
			vo.setState("¾ÈÀü");
			memberService.insert(vo);
			vo = memberService.read(id);
		}
		return vo;
	}
	
	
}

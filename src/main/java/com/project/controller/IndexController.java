package com.project.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.project.common.util.CommonUtil;

@Controller
@RequestMapping("/")
public class IndexController {
	
	/**
	 * 첫 화면으로 이동한다.
	 * @param memberVo
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/index")
	public String index(Model model, HttpServletRequest request, HttpServletResponse response) {
		
		//세션에 로그인 정보가 있으면 데이터를 화면에 반환한다.
		if(!CommonUtil.getSessionValue(request, "S_MBER_ID").equals("")) {
			
			model.addAttribute("mberNm", CommonUtil.getSessionValue(request, "S_MBER_NM"));
			
		}
		
		//첫 화면으로 이동한다.
		return "index";
		
	}
	
}

package com.project.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.project.dao.MemberRepository;
import com.project.service.MemberService;
import com.project.vo.MemberVo;

@Controller
@RequestMapping("/member")
public class MemberController {
	
	@Autowired
    MemberService memberService;
	
	@Autowired
    MemberRepository memberRepository;
	
	/**
	 * 회원가입 화면으로 이동한다.
	 * @param memberVo
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/memberJoin")
	public String memberJoin(MemberVo memberVo, Model model, HttpServletRequest request, HttpServletResponse response) {
		
		//회원가입 화면으로 이동한다.
		return "member/memberJoin";
		
	}
	
	/**
	 * 회원가입 처리를 실행하고 결과를 반환한다.
	 * @param memberVo
	 * @param model
	 * @param request
	 * @param response
	 * @return resultCode : >>>
	 * -1 : 오류
	 * 99 : 처리 성공
	 * @return resultMessage : >>>
	 * resultCode가 99 이외일 경우 결과메시지
	 */
	@ResponseBody
	@RequestMapping("/memberJoinAction")
	public MemberVo memberJoinAction(MemberVo memberVo, Model model, HttpServletRequest request, HttpServletResponse response) {
		
		MemberVo voReturn = new MemberVo();
		
		try {
			
			//회원정보 검증 및 저장처리를 수행한다.
			voReturn = memberService.saveMemberJoinData(memberVo);
			
		} catch(Exception e) {
			
			voReturn.setResultCode("-1");
			voReturn.setResultMessage("처리 중 오류가 발생하였습니다.");
			e.printStackTrace();
			
		}
		
		return voReturn;
		
	}
	
	/**
	 * 아이디 중복여부를 체크하고 결과를 반환한다.
	 * @param memberVo
	 * @param model
	 * @param request
	 * @param response
	 * @return resultCode : >>>
	 * -1 : 오류
	 * 1 : 중복아이디 존재
	 * 99 : 중복아이디 없음
	 */
	@ResponseBody
	@RequestMapping("/checkMberIdExist")
	public MemberVo checkMberIdExist(MemberVo memberVo, Model model, HttpServletRequest request, HttpServletResponse response) {
		
		MemberVo voReturn = new MemberVo();
		
		try {
			
			String mberId = memberVo.getMberId();
			
			//db를 통해 입력한 아이디의 중복여부를 확인한다.
			MemberVo voTemp = memberRepository.findByMberId(mberId);
			if(voTemp == null) {
				
				voReturn.setResultCode("99");
				
			} else {
				
				voReturn.setResultCode("1");
				
			}
			
		} catch(Exception e) {
			
			voReturn.setResultCode("-1");
			e.printStackTrace();
			
		}
		
		return voReturn;
		
	}
	
	
	
	/**
	 * 로그인을 실행하고 결과를 반환한다.
	 * @param memberVo
	 * @param model
	 * @param request
	 * @param response
	 * @return resultCode : >>>
	 * -1 : 오류
	 * 1 : 회원정보 없음
	 * 99 : 로그인 성공
	 * @return resultMessage : >>>
	 * resultCode가 99 이외일 경우 결과메시지
	 */
	@ResponseBody
	@RequestMapping(value="/memberLoginAction")
	public MemberVo memberLoginAction(MemberVo memberVo, Model model, HttpServletRequest request, HttpServletResponse response) {
		
		// 응답헤더 지정
		HttpHeaders resHeaders = new HttpHeaders();
		resHeaders.add("Content-Type", "application/json;charset=UTF-8");
	    
		MemberVo voReturn = new MemberVo();
		
		try {
			
			voReturn = memberService.getLoginInfo(memberVo, request);
			
		} catch(Exception e) {
			
			voReturn.setResultCode("-1");
			voReturn.setResultMessage("처리 중 오류가 발생하였습니다.");
			e.printStackTrace();
			
		}
		
		return voReturn;
		
	}
	
	/**
	 * 로그아웃을 실행하고 결과를 반환한다.
	 * @param memberVo
	 * @param model
	 * @param request
	 * @param response
	 * @return resultCode : >>>
	 * -1 : 오류
	 * 99 : 로그아웃 성공
	 * @return resultMessage : >>>
	 * resultCode가 99 이외일 경우 결과메시지
	 */
	@ResponseBody
	@RequestMapping("/memberLogoutAction")
	public MemberVo memberLogoutAction(MemberVo memberVo, Model model, HttpServletRequest request, HttpServletResponse response) {
		
		MemberVo voReturn = new MemberVo();
		
		try {
			
			//세션을 만료 처리하고 결과를 반환한다.
			request.getSession().invalidate();
			voReturn.setResultCode("99");
			
		} catch(Exception e) {
			
			voReturn.setResultCode("-1");
			voReturn.setResultMessage("처리 중 오류가 발생하였습니다.");
			e.printStackTrace();
			
		}
		
		return voReturn;
		
	}
	
}

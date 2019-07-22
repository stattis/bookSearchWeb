package com.project.controller;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.project.common.util.CommonUtil;
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
			
			//회원정보 저장처리 전 데이터 검증을 수행한다.
			memberService.validateMemberJoinData(memberVo);
			if(CommonUtil.nullToBlank(memberVo.getResultCode()).equals("-1")) {
				
				//결과 데이터 설정
				voReturn.setResultCode(memberVo.getResultCode());
				voReturn.setResultMessage(memberVo.getResultMessage());
				
			} else {
				
				//데이터 검증결과가 문제없으면 저장처리 수행
				
				//비밀번호 Hash암호화 수행
				String strHashPassword = CommonUtil.getSHA512(memberVo.getMberPw());
				memberVo.setMberPw(strHashPassword);
				
				//등록일자 생성
				memberVo.setRegDt(new Date());
				
				memberRepository.save(memberVo);
				
				voReturn.setResultCode("99");
				
			}
			
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
	@RequestMapping("/memberLoginAction")
	public MemberVo memberLoginAction(MemberVo memberVo, Model model, HttpServletRequest request, HttpServletResponse response) {
		
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

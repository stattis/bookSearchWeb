package com.project.controller;

import javax.servlet.http.HttpServletRequest;

import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.project.service.MemberService;
import com.project.vo.MemberVo;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MemberControllerTests {
	
	@Autowired
	MemberService memberService;
	
	@Autowired
	HttpServletRequest request;
	
	/**
	 * 회원가입 테스트
	 * @throws Exception
	 */
	@Test
	public void memberJoinActionTest() throws Exception {
		
		//패러미터 값 설정
		MemberVo memberVo = new MemberVo();
		memberVo.setMberId("kakaotester1");
		memberVo.setMberNm("테스터1");
		memberVo.setMberPw("kakaotester1");
		memberVo.setMberPwConfirm("kakaotester1");
		
		//결과값 반환
		MemberVo voReturn = memberService.saveMemberJoinData(memberVo);
		
		//결과값이 성공(99)가 맞는지 체크한다.
		System.out.println("회원가입 오류메시지 : " + voReturn.getResultMessage());
		Assert.assertThat(voReturn.getResultCode(), Matchers.is("99"));
		
	}
	
	/**
	 * 로그인 테스트
	 * @throws Exception
	 */
	@Test
	public void memberLoginActionTest() throws Exception {
		
		//패러미터 값 설정
		MemberVo memberVo = new MemberVo();
		memberVo.setMberId("kakaotester1");
		memberVo.setMberPw("kakaotester1");
		
		//결과값 반환
		MemberVo voReturn = memberService.getLoginInfo(memberVo, request);
		
		//결과값이 성공(99)가 맞는지 체크한다.
		System.out.println("로그인 오류메시지 : " + voReturn.getResultMessage());
		Assert.assertThat(voReturn.getResultCode(), Matchers.is("99"));
		
	}
	
}

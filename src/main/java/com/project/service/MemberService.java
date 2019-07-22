package com.project.service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.common.util.CommonUtil;
import com.project.dao.MemberRepository;
import com.project.vo.MemberVo;

@Service
public class MemberService {
	
	@Autowired
    MemberRepository memberRepository;
	
	/**
	 * 회원가입 데이터에 대한 검증과 병합을 수행한다.
	 * @param memberVo
	 */
	public void validateMemberJoinData(MemberVo memberVo) {
		
		//아이디와 비밀번호에 대한 적합성 검증 정규식
		Pattern testId = Pattern.compile("[ㄱ-ㅎ|ㅏ-ㅣ|가-힣]");
		Pattern testPw = Pattern.compile("^(?=.*[a-zA-Z])(?=.*[0-9]).{5,}$");
		
		//입력한 아이디에 대한 빈값여부 검증
		if(CommonUtil.nullToBlank(memberVo.getMberId()).equals("")) {
			
			memberVo.setResultCode("-1");
			memberVo.setResultMessage("사용하실 회원 아이디를 입력해주세요.");
			return;
			
		}
		
		//아이디 길이(50자 이상)에 대한 검증
		if(memberVo.getMberId().length() < 5 || memberVo.getMberId().length() > 50) {
			
			memberVo.setResultCode("-1");
			memberVo.setResultMessage("회원 아이디를 5자 이상, 50자 이하로 입력해주세요.");
			return;
			
		}
		
		//아이디를 한글만 입력했는지 검증
		Matcher matchId = testId.matcher(memberVo.getMberId());
		if(matchId.find()) {
			
			memberVo.setResultCode("-1");
			memberVo.setResultMessage("회원 아이디에는 한글을 사용할 수 없습니다.");
			return;
			
		}
		
		//아이디 중복여부 검증
		MemberVo voTemp = memberRepository.findByMberId(memberVo.getMberId());
		if(voTemp != null) {
			
			memberVo.setResultCode("-1");
			memberVo.setResultMessage("이미 사용중인 아이디입니다. 다른 아이디를 입력해주세요.");
			return;
			
		}
		
		//입력한 이름에 대한 빈값여부 검증
		if(CommonUtil.nullToBlank(memberVo.getMberNm()).equals("")) {
			
			memberVo.setResultCode("-1");
			memberVo.setResultMessage("사용하실 회원 이름을 입력해주세요.");
			return;
			
		}
		
		//입력한 비밀번호에 대한 빈값여부 검증
		if(CommonUtil.nullToBlank(memberVo.getMberPw()).equals("")) {
			
			memberVo.setResultCode("-1");
			memberVo.setResultMessage("비밀번호를 입력해주세요.");
			return;
			
		}
		
		//입력한 비밀번호에 대한 길이 및 적합여부 검증
		Matcher matchPw = testPw.matcher(memberVo.getMberPw());
		if(!matchPw.find()) {
			
			memberVo.setResultCode("-1");
			memberVo.setResultMessage("입력하신 비밀번호는 사용하기에 적합하지 않습니다. 영어, 숫자를 포함하여 5자리 이상 입력해주세요.");
			return;
			
		}
		
		//비밀번호 확인값 일치여부 검증
		if(!CommonUtil.nullToBlank(memberVo.getMberPw()).equals(CommonUtil.nullToBlank(memberVo.getMberPwConfirm()))) {
			
			memberVo.setResultCode("-1");
			memberVo.setResultMessage("입력하신 비밀번호 확인값이 비밀번호와 일치하지 않습니다.");
			return;
			
		}
		
	}
	
	
	/**
	 * 회원정보를 조회하여 로그인 처리한다.
	 * @param memberVo
	 * @return
	 */
	public MemberVo getLoginInfo(MemberVo memberVo, HttpServletRequest request) {
		
		MemberVo voReturn = new MemberVo();
		
		//입력받은 아이디로 회원정보를 조회한다.
		MemberVo voTemp = memberRepository.findByMberId(memberVo.getMberId());
		if(voTemp == null) {
			
			//아이디로 조회한 회원정보가 없을 경우
			
			voReturn.setResultCode("1");
			voReturn.setResultMessage("아이디 또는 비밀번호를 확인해주세요.");
			
		} else {
			
			//입력받은 비밀번호 Hash암호화 수행
			String strHashPassword = CommonUtil.getSHA512(memberVo.getMberPw());
			
			//실제 회원정보의 비밀번호 hash값과 같은지 확인한다.
			if(voTemp.getMberPw().equals(strHashPassword)) {
				
				//로그인 성공
				
				//세션에 로그인 회원 정보를 기록한다.
				CommonUtil.setSessionValue(request, "S_MBER_ID", voTemp.getMberId());
				CommonUtil.setSessionValue(request, "S_MBER_NM", voTemp.getMberNm());
				
				voReturn.setResultCode("99");
				voReturn.setMberNm(voTemp.getMberNm());
				
			} else {
				
				//비밀번호가 틀렸을 경우
				
				voReturn.setResultCode("1");
				voReturn.setResultMessage("아이디 또는 비밀번호를 확인해주세요.");
				
			}
			
		}
		
		return voReturn;
		
	}
	
}

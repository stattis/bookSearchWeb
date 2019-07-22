package com.project.vo;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * 회원 데이터를 송수신하는 Vo클래스
 * @author 박정환
 *
 */
@Entity
@Table(name = "TB_MEMBER")
public class MemberVo extends CommonVo {
	
	/**********************
	 * 회원 데이터 관련 필드 명시
	 **********************/
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long seq;				//시퀀스
	
	@Column(length = 50)
	private String mberId;			//회원아이디
	
	@Column(length = 50)
	private String mberNm;			//회원이름
	
	@Column(length = 128)
	private String mberPw;			//회원비밀번호
	
	@Transient
	private String mberPwConfirm;	//회원비밀번호 확인입력값
	
	@Column
	private Date regDt;				//등록일시
	
	/**********************************
	 * 회원 데이터 관련 필드의 get/set 메소드
	 * 가독성을 위하여 필드의 순서와 일치 필요
	 **********************************/
	public Long getSeq() {
		return seq;
	}
	public void setSeq(Long seq) {
		this.seq = seq;
	}
	
	public String getMberId() {
		return mberId;
	}
	public void setMberId(String mberId) {
		this.mberId = mberId;
	}
	
	public String getMberNm() {
		return mberNm;
	}
	public void setMberNm(String mberNm) {
		this.mberNm = mberNm;
	}
	
	public String getMberPw() {
		return mberPw;
	}
	public void setMberPw(String mberPw) {
		this.mberPw = mberPw;
	}
	
	public String getMberPwConfirm() {
		return mberPwConfirm;
	}
	public void setMberPwConfirm(String mberPwConfirm) {
		this.mberPwConfirm = mberPwConfirm;
	}
	
	public Date getRegDt() {
		return regDt;
	}
	public void setRegDt(Date regDt) {
		this.regDt = regDt;
	}
	
}

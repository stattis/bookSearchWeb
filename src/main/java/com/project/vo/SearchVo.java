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
 * 도서 데이터를 송수신하는 Vo클래스
 * @author 박정환
 *
 */
@Entity
@Table(name = "TB_SEARCH_LOG")
public class SearchVo extends CommonVo {
	
	/**********************
	 * 관련 필드 명시
	 **********************/
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long seq;				//시퀀스
	
	@Column(length = 50)
	private String mberId;			//회원아이디
	
	@Column(length = 500)
	private String keyword;			//검색어
	
	@Transient
	private String cnt;
	
	@Column
	private Date regDt;				//등록일시
	
	@Transient
	private String regDtText;		//등록일시
	
	/**********************************
	 * 관련 필드의 get/set 메소드
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
	
	public String getKeyword() {
		return keyword;
	}
	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}
	
	public String getCnt() {
		return cnt;
	}
	public void setCnt(String cnt) {
		this.cnt = cnt;
	}
	
	public Date getRegDt() {
		return regDt;
	}
	public void setRegDt(Date regDt) {
		this.regDt = regDt;
	}
	
	public String getRegDtText() {
		return regDtText;
	}
	public void setRegDtText(String regDtText) {
		this.regDtText = regDtText;
	}
	
}

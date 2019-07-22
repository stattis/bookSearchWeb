package com.project.vo;

/**
 * 공통 Vo클래스
 * @author 박정환
 *
 */
public class CommonVo {
	
	/**********************
	 * 공통 필드 명시
	 **********************/
	private String resultCode;		//결과코드
	private String resultMessage;	//결과메시지
	
	private String pageNo;
	
	/**********************************
	 * 필드의 get/set 메소드
	 * 가독성을 위하여 필드의 순서와 일치 필요
	 **********************************/
	public String getResultCode() {
		return resultCode;
	}
	public void setResultCode(String resultCode) {
		this.resultCode = resultCode;
	}
	
	public String getResultMessage() {
		return resultMessage;
	}
	public void setResultMessage(String resultMessage) {
		this.resultMessage = resultMessage;
	}
	
	public String getPageNo() {
		return pageNo;
	}
	public void setPageNo(String pageNo) {
		this.pageNo = pageNo;
	}
	
}

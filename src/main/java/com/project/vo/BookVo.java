package com.project.vo;

import javax.persistence.Transient;

/**
 * 도서 데이터를 송수신하는 Vo클래스
 * @author 박정환
 *
 */
public class BookVo extends CommonVo {
	
	/**********************
	 * 도서 데이터 관련 필드 명시
	 **********************/
	@Transient
	private String keyword;		//검색어
	
	@Transient
	private String keykind;		//검색종류
	
	@Transient
	private String isbn;		//isbn
	
	/**********************************
	 * 도서 데이터 관련 필드의 get/set 메소드
	 * 가독성을 위하여 필드의 순서와 일치 필요
	 **********************************/
	public String getKeyword() {
		return keyword;
	}
	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}
	
	public String getKeykind() {
		return keykind;
	}
	public void setKeykind(String keykind) {
		this.keykind = keykind;
	}
	
	public String getIsbn() {
		return isbn;
	}
	public void setIsbn(String isbn) {
		this.isbn = isbn;
	}
	
}

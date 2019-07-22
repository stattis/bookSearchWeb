package com.project.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.project.vo.SearchVo;

@Repository
public interface SearchRepository extends JpaRepository<SearchVo, Long> {
	
	//save 등의 기본 함수는 JpaRepository에서 지원하는 함수를 이용한다.
	
}
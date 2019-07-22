package com.project.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.project.vo.MemberVo;

@Repository
public interface MemberRepository extends JpaRepository<MemberVo, Long> {
	
	//save 등의 기본 함수는 JpaRepository에서 지원하는 함수를 이용한다.
	
	//회원아이디로 정보를 조회한다.
	public MemberVo findByMberId(String mberId);
	
}
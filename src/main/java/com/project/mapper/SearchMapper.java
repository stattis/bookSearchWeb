package com.project.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.project.vo.SearchVo;

@Mapper
public interface SearchMapper {
	
	List<SearchVo> selectMyHistory(SearchVo searchVo);
	
	List<SearchVo> selectFavoriteKeyword(SearchVo searchVo);
	
}

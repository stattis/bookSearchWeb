package com.project.controller;

import java.net.URLEncoder;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.project.common.util.CommonUtil;
import com.project.dao.SearchRepository;
import com.project.mapper.SearchMapper;
import com.project.vo.BookVo;
import com.project.vo.SearchVo;

import java.util.Iterator;

@Controller
@RequestMapping("/book")
public class BookController {
	
	@Autowired
    SearchRepository searchRepository;
	
	@Autowired
    SearchMapper searchMapper;
	
	/**
	 * 도서검색 화면으로 이동한다.
	 * @param memberVo
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/bookSearch")
	public String bookSearch(Model model, HttpServletRequest request, HttpServletResponse response) {
		
		//도서검색 화면으로 이동한다.
		return "book/bookSearch";
		
	}
	
	/**
	 * 도서검색을 실행한다.
	 * @param memberVo
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/bookSearchAjax")
	public String bookSearchAjax(BookVo bookVo, Model model, HttpServletRequest request, HttpServletRequest response) {
		
		JSONObject returnJson = new JSONObject();
		
		try {
			
			String strApiKey = "0121d6dddfb9de588695872ab56b5418";		//도서 검색 호출 api키
			String strUrl = "https://dapi.kakao.com/v3/search/book";	//rest api 주소
			
			//검색어를 api 주소에 조합한다.
			if(!CommonUtil.nullToBlank(bookVo.getKeyword()).equals("")) {
				
				strUrl += "?query=" + URLEncoder.encode(bookVo.getKeyword(), "UTF-8");
				
			}
			
			//검색유형을 api 주소에 조합한다.
			if(!CommonUtil.nullToBlank(bookVo.getKeykind()).equals("")) {
				
				strUrl += "&target=" + bookVo.getKeykind();
				
			}
			
			//현재 페이지 번호를 api 주소에 조합한다.
			strUrl += "&page=" + bookVo.getPageNo();
			
			//Rest Api를 호출한다.
			returnJson = CommonUtil.getRestApi(strUrl, strApiKey);
			
			//Rest api의 호출결과가 정상이 아니면 네이버의 도서검색 서비스를 이용한다.
			if(!CommonUtil.nullToBlank(returnJson.get("resultCode")).equals("99")) {
				
				strUrl = "https://openapi.naver.com/v1/search/book.xml";	//rest api 주소
				
				//검색어를 api 주소에 조합한다.
				if(!CommonUtil.nullToBlank(bookVo.getKeyword()).equals("")) {
					
					strUrl += "?query=" + URLEncoder.encode(bookVo.getKeyword(), "UTF-8");
					
				}
				
				//현재 페이지 번호를 api 주소에 조합한다.
				strUrl += "&start=" + bookVo.getPageNo();
				
				String strClientId = "lErIV3vv5ELlmfMKYw_4";
				String strClientSecret = "zTJWNHmBga";
				returnJson = CommonUtil.getRestApiForNaver(strUrl, strClientId, strClientSecret);
				
			}
			
			//검색기록을 로그에 저장한다.
			try {
				
				SearchVo searchVo = new SearchVo();
				
				if(CommonUtil.getSessionValue(request, "S_MBER_ID").equals("")) {
					
					searchVo.setMberId("guest");
					
				} else {
					
					searchVo.setMberId(CommonUtil.getSessionValue(request, "S_MBER_ID"));
					
				}
				searchVo.setKeyword(bookVo.getKeyword());
				searchVo.setRegDt(new Date());
				
				searchRepository.save(searchVo);
				
			} catch(Exception e) {
				
				e.printStackTrace();
				
			}
			
			
		} catch(Exception e) {
			
			e.printStackTrace();
			
			returnJson.put("resultCode", "-1");
			
		}
		
		return returnJson.toString();
		
	}
	
	/**
	 * 도서 상세검색을 실행한다.
	 * @param memberVo
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/bookSearchDetailAjax")
	public String bookSearchDetailAjax(BookVo bookVo, Model model, HttpServletRequest request, HttpServletRequest response) {
		
		JSONObject returnJson = new JSONObject();
		
		try {
			
			String strApiKey = "0121d6dddfb9de588695872ab56b5418";		//도서 검색 호출 api키
			String strUrl = "https://dapi.kakao.com/v3/search/book";	//rest api 주소
			
			//상세검색인 경우 isbn 값을 전달한다.
			String[] arrIsbn = null;
			String strIsbn = "";
			if(!CommonUtil.nullToBlank(bookVo.getIsbn()).equals("")) {
				
				//전달된 isbn 값을 공백 기준으로 분리하여 string 배열로 생성한다.
				arrIsbn = bookVo.getIsbn().split(" ");
				
				//isbn 값이 앞자리가 공백인 경우 강제로 두번째 자리를 쓰도록 예외 처리한다.
				if(arrIsbn.length == 2 && CommonUtil.nullToBlank(arrIsbn[0]).equals("")) {
					
					strIsbn = arrIsbn[1];
					
				} else {
					
					strIsbn = arrIsbn[0];
					
				}
				
			}
			
			strUrl += "?target=isbn&query=" + strIsbn;
			
			//Rest Api를 호출한다.
			returnJson = CommonUtil.getRestApi(strUrl, strApiKey);
			
		} catch(Exception e) {
			
			e.printStackTrace();
			
			returnJson.put("resultCode", "-1");
			
		}
		
		return returnJson.toString();
		
	}
	
	/**
	 * 도서 상세검색을 실행한다.
	 * 카카오 장애발생시 임시로 호출하는 naver api 호출용
	 * @param memberVo
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/bookSearchDetailAjaxForNaver")
	public String bookSearchDetailAjaxForNaver(BookVo bookVo, Model model, HttpServletRequest request, HttpServletRequest response) {
		
		JSONObject returnJson = new JSONObject();
		
		try {
			
			String strClientId = "lErIV3vv5ELlmfMKYw_4";
			String strClientSecret = "zTJWNHmBga";
			
			String strUrl = "https://openapi.naver.com/v1/search/book_adv.xml";	//rest api 주소
			
			//상세검색인 경우 isbn 값을 전달한다.
			String[] arrIsbn = null;
			String strIsbn = "";
			if(!CommonUtil.nullToBlank(bookVo.getIsbn()).equals("")) {
				
				//전달된 isbn 값을 공백 기준으로 분리하여 string 배열로 생성한다.
				arrIsbn = bookVo.getIsbn().split(" ");
				
				//isbn 값이 앞자리가 공백인 경우 강제로 두번째 자리를 쓰도록 예외 처리한다.
				if(arrIsbn.length == 2 && CommonUtil.nullToBlank(arrIsbn[0]).equals("")) {
					
					strIsbn = arrIsbn[1];
					
				} else {
					
					strIsbn = arrIsbn[0];
					
				}
				
			}
			
			strUrl += "?d_isbn=" + strIsbn;
			
			returnJson = CommonUtil.getRestApiForNaver(strUrl, strClientId, strClientSecret);
			
		} catch(Exception e) {
			
			e.printStackTrace();
			
			returnJson.put("resultCode", "-1");
			
		}
		
		return returnJson.toString();
		
	}
	
	/**
	 * 내 도서검색 목록을 조회한다.
	 * @param memberVo
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/myBookSearchListAjax")
	public String myBookSearchListAjax(BookVo bookVo, Model model, HttpServletRequest request, HttpServletRequest response) {
		
		JSONObject returnJson = new JSONObject();
		
		try {
			
			if(!CommonUtil.getSessionValue(request, "S_MBER_ID").equals("")) {
				
				//로그인한 경우, 검색 히스토리를 조회한다.
				SearchVo searchVo = new SearchVo();
				searchVo.setMberId(CommonUtil.getSessionValue(request, "S_MBER_ID"));
				
				List<SearchVo> listHistory = searchMapper.selectMyHistory(searchVo);
				
				/* 조회결과를 json 형태로 변환한다.*/
				JSONArray jsonArrayTemp = new JSONArray();
				JSONObject jsonObjectTemp = null;
				
				Iterator iterator = listHistory.iterator();
				while(iterator.hasNext()) {
					
					SearchVo voTemp = (SearchVo)iterator.next();
					
					jsonObjectTemp = new JSONObject();
					jsonObjectTemp.put("keyword", voTemp.getKeyword());
					jsonObjectTemp.put("regDtText", voTemp.getRegDtText());
					
					jsonArrayTemp.add(jsonObjectTemp);
					
				}
				
				returnJson.put("list", jsonArrayTemp);
				
			}
			
		} catch(Exception e) {
			
			e.printStackTrace();
			
		}
		
		return returnJson.toString();
		
	}
	
	/**
	 * 인기 검색어 목록을 조회한다.
	 * @param memberVo
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/favoriteKeywordListAjax")
	public String favoriteKeywordListAjax(BookVo bookVo, Model model, HttpServletRequest request, HttpServletRequest response) {
		
		JSONObject returnJson = new JSONObject();
		
		try {
			
			//인기 검색어를 조회한다.
			SearchVo searchVo = new SearchVo();
			List<SearchVo> listHistory = searchMapper.selectFavoriteKeyword(searchVo);
			
			/* 조회결과를 json 형태로 변환한다.*/
			JSONArray jsonArrayTemp = new JSONArray();
			JSONObject jsonObjectTemp = null;
			
			Iterator iterator = listHistory.iterator();
			while(iterator.hasNext()) {
				
				SearchVo voTemp = (SearchVo)iterator.next();
				
				jsonObjectTemp = new JSONObject();
				jsonObjectTemp.put("keyword", voTemp.getKeyword());
				jsonObjectTemp.put("cnt", voTemp.getCnt());
				
				jsonArrayTemp.add(jsonObjectTemp);
				
			}
			
			returnJson.put("list", jsonArrayTemp);
				
		} catch(Exception e) {
			
			e.printStackTrace();
			
		}
		
		return returnJson.toString();
		
	}
	
}

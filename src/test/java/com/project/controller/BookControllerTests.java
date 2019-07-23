package com.project.controller;

import javax.servlet.http.HttpServletRequest;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@RunWith(SpringRunner.class)
@SpringBootTest
public class BookControllerTests {
	
	@Autowired
    private MockMvc mvc;
	
	@MockBean
	BookController bookController;
	
	@Autowired
	HttpServletRequest request;
	
	/**
	 * 도서검색 api 실행 테스트
	 * @throws Exception
	 */
	@Test
	public void bookSearchAjaxTest() throws Exception {
		
		final ResultActions actualResult;
		actualResult = mvc.perform(MockMvcRequestBuilders.get("/book/bookSearchAjax/").contentType(MediaType.APPLICATION_JSON_UTF8).accept(MediaType.APPLICATION_JSON_UTF8));
		
	}
	
}

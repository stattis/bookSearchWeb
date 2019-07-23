<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<!DOCTYPE html>


<html lang="ko">
	<head>
		<meta charset="UTF-8" />
		<meta http-equiv="X-UA-Compatible" content="ie=edge" />
		
		<link rel="stylesheet" href="/css/default.css">
		
		<script src="/js/jquery-3.4.1.min.js"></script>
		<script src="/js/common.js"></script>
		<script>
			
			//화면 초기 로딩시 실행
			$(document).ready(function() {
				
				//검색 버튼 클릭시 실행
				$("#btnSearch").bind('click', function(e) {
					
					searchData();
					
				});
				
				$("#keyword").bind('keydown', function(e) {
					
					if(e.keyCode == 13) {
						
						searchData();
						
					}
					
				});
				
				//내 검색기록 보기
				$("#btnMyList").bind('click', function(e) {
					
					if('${S_MBER_ID}' == "") {
						
						alert("회원가입 및 로그인 후 이용 가능합니다.");
						return;
						
					}
					
					searchMyList();
					
				});
				
				//인기검색어를 조회한다.
				getFavoriteKeyword();
				
			});
			
			//도서 검색을 실행한다.
			function searchData(pageNo) {
				
				if($("#keyword").val().trim() == "") {
					
					alert("검색어를 입력해주세요.");
					$("#keyword").focus();
					return;
					
				}
				
				if(typeof(pageNo) == "undefined" || pageNo == "") {
					
					pageNo = 1;
					
				}
				
				//도서 상세검색값 초기화
				$("#isbn").val('');

				//페이지 번호 설정
				$("#pageNo").val(pageNo);
				
				var strUrl = "/book/bookSearchAjax";
				var strParam = $("#frm").serialize();
				
				var data = getAjaxData(strUrl, strParam);
				
				if(data.resultCode != "-1") {
					
					//검색결과를 받아 html영역을 생성한다.
					if(data.apiType == "kakao") {
						
						createSearchResultForKakao(data);
						
					} else if(data.apiType == "naver") {
						
						createSearchResultForNaver(data);
						
					}
					
				} else {
					
					alert("조회 중 오류가 발생했습니다. 잠시 후 다시 시도해주세요.");
					return;
					
				}
				
			}
			
			/******************************
			 * 도서 검색결과를 html로 생성한다.
			*******************************/
			function createSearchResultForKakao(data) {
				
				var documents = data.documents;		//검색결과에서 본문 영역을 별도로 분리한다.
				
				var strHtml = "";
					strHtml += "<p>총 검색건수 : " + data.meta.total_count + "건</p>";
					
					strHtml += "<table class=\"result_table\">";
					strHtml += "	<tr>";
					strHtml += "		<th class=\"result_width1\">";
					strHtml += "			제목";
					strHtml += "		</th>";
					strHtml += "		<th class=\"result_width2\">";
					strHtml += "			출판사";
					strHtml += "		</th>";
					strHtml += "		<th class=\"result_width3\">";
					strHtml += "			상세보기";
					strHtml += "		</th>";
					strHtml += "	</tr>";
				
				for(var i=0; i<documents.length; i++) {
					
					strHtml += "<tr>";
					strHtml += "	<td>";
					strHtml += documents[i].title;
					strHtml += "	</td>";
					strHtml += "	<td>";
					strHtml += documents[i].publisher;
					strHtml += "	</td>";
					strHtml += "	<td>";
					strHtml += "<a href=\"#\" onclick=\"javascript:viewDetail('" + documents[i].isbn + "');return false;\">상세보기</a>";
					strHtml += "	</td>";
					strHtml += "</tr>";
					
				}
				
				if(data.meta.total_count == 0) {
					
					strHtml += "<tr>";
					strHtml += "	<td colspan=\"3\">";
					strHtml += "		검색결과가 없습니다.";
					strHtml += "	</td>";
					strHtml += "</tr>";
					
				}
				
				strHtml += "</table>";
				
				if(data.meta.total_count != 0) {
					
					//페이징 영역 생성
					strHtml += createPaging(data.meta.total_count, 10, 10, $("#pageNo").val());
					
				}
				
				$("#dvSearch").html(strHtml);
				
				//상세 영역을 숨김 처리한다.
				$("#dvSearchDetail").hide();
				
			}

			
			/******************************
			 * 도서 검색결과를 html로 생성한다.
			 * 카카오 장애발생시 임시로 호출하는 naver api
			*******************************/
			function createSearchResultForNaver(data) {
				
				var documents = data.rss.channel.item;		//검색결과에서 본문 영역을 별도로 분리한다.
				
				var strHtml = "";
					strHtml += "<p>총 검색건수 : " + data.rss.channel.total + "건</p>";
					
					strHtml += "<table class=\"result_table\">";
					strHtml += "	<tr>";
					strHtml += "		<th class=\"result_width1\">";
					strHtml += "			제목";
					strHtml += "		</th>";
					strHtml += "		<th class=\"result_width2\">";
					strHtml += "			출판사";
					strHtml += "		</th>";
					strHtml += "		<th class=\"result_width3\">";
					strHtml += "			상세보기";
					strHtml += "		</th>";
					strHtml += "	</tr>";
				
				for(var i=0; i<documents.length; i++) {
					
					strHtml += "<tr>";
					strHtml += "	<td>";
					strHtml += documents[i].title;
					strHtml += "	</td>";
					strHtml += "	<td>";
					strHtml += documents[i].publisher;
					strHtml += "	</td>";
					strHtml += "	<td>";
					strHtml += "<a href=\"#\" onclick=\"javascript:viewDetailForNaver('" + documents[i].isbn + "');return false;\">상세보기</a>";
					strHtml += "	</td>";
					strHtml += "</tr>";
					
				}
				
				if(data.rss.channel.total == 0) {
					
					strHtml += "<tr>";
					strHtml += "	<td colspan=\"3\">";
					strHtml += "		검색결과가 없습니다.";
					strHtml += "	</td>";
					strHtml += "</tr>";
					
				}
				
				strHtml += "</table>";
				
				if(data.rss.channel.total != 0) {
					
					//페이징 영역 생성
					strHtml += createPaging(data.rss.channel.total, 10, 10, $("#pageNo").val());
					
				}
				
				$("#dvSearch").html(strHtml);

				//상세 영역을 숨김 처리한다.
				$("#dvSearchDetail").hide();
				
			}
			
			/******************************
			 * 도서 상세검색결과를 html로 생성한다.
			*******************************/
			function viewDetail(strIsbn) {
				
				//도서 상세검색값 설정
				$("#isbn").val(strIsbn);
				
				var strUrl = "/book/bookSearchDetailAjax";
				var strParam = $("#frm").serialize();
				
				var data = getAjaxData(strUrl, strParam);
				
				if(data.resultCode != "-1") {
					
					var documents = data.documents;		//검색결과에서 본문 영역을 별도로 분리한다.
					
					//검색결과를 받아 html영역을 생성한다.
					var strHtml = "";
						strHtml += "<h4>도서 상세보기</h4>";
						strHtml += "<p>";
						strHtml += "	<img src=\"" + documents[0].thumbnail + "\" />";
						strHtml += "</p>";
						strHtml += "<p>";
						strHtml += "	ISBN : " + documents[0].isbn;
						strHtml += "</p>";
						strHtml += "<p>";
						strHtml += "	제목 : " + documents[0].title;
						strHtml += "</p>";
						strHtml += "<p>";
						strHtml += "	저자 : " + documents[0].authors;
						strHtml += "</p>";
						strHtml += "<p>";
						strHtml += "	출판사 : " + documents[0].publisher;
						strHtml += "</p>";
						strHtml += "<p>";
						strHtml += "	출판일 : " + documents[0].datetime;
						strHtml += "</p>";
						strHtml += "<p>";
						strHtml += "	정가 : " + setComma(documents[0].price) + "원";
						strHtml += "</p>";
						strHtml += "<p>";
						strHtml += "	판매가 : <span class=\"text-blue\">" + setComma(documents[0].sale_price) + "</span>원";
						strHtml += "</p>";
						strHtml += "<p>";
						strHtml += "	<span style=\"font-weight:bold;\">책 소개</span>";
						strHtml += "	<br />";
						strHtml += "	<span>";
						strHtml += "" + documents[0].contents;
						strHtml += "	</span>";
						strHtml += "</p>";
					
					$("#dvSearchDetail").html(strHtml);
					$("#dvSearchDetail").show();
					$("#dvSearchDetail").attr("tabindex", -1).focus();
					
				} else {
					
					alert("조회 중 오류가 발생했습니다. 잠시 후 다시 시도해주세요.");
					return;
					
				}
				
			}
			
			/******************************
			 * 도서 상세검색결과를 html로 생성한다.
			 * 카카오 장애발생시 임시로 호출하는 naver api
			*******************************/
			function viewDetailForNaver(strIsbn) {
				
				//도서 상세검색값 설정
				$("#isbn").val(strIsbn);
				
				var strUrl = "/book/bookSearchDetailAjaxForNaver";
				var strParam = $("#frm").serialize();
				
				var data = getAjaxData(strUrl, strParam);
				
				if(data.resultCode != "-1") {
					
					var documents = data.rss.channel.item;		//검색결과에서 본문 영역을 별도로 분리한다.
					
					//검색결과를 받아 html영역을 생성한다.
					var strHtml = "";
						strHtml += "<h4>도서 상세보기</h4>";
						strHtml += "<p>";
						strHtml += "	<img src=\"" + documents.image + "\" />";
						strHtml += "</p>";
						strHtml += "<p>";
						strHtml += "	ISBN : " + documents.isbn;
						strHtml += "</p>";
						strHtml += "<p>";
						strHtml += "	제목 : " + documents.title;
						strHtml += "</p>";
						strHtml += "<p>";
						strHtml += "	저자 : " + documents.author;
						strHtml += "</p>";
						strHtml += "<p>";
						strHtml += "	출판사 : " + documents.publisher;
						strHtml += "</p>";
						strHtml += "<p>";
						strHtml += "	출판일 : " + documents.pubdate;
						strHtml += "</p>";
						strHtml += "<p>";
						strHtml += "	정가 : " + setComma(documents.price) + "원";
						strHtml += "</p>";
						strHtml += "<p>";
						strHtml += "	판매가 : <span class=\"text-blue\">" + setComma(documents.discount) + "</span>원";
						strHtml += "</p>";
						strHtml += "<p>";
						strHtml += "	<span style=\"font-weight:bold;\">책 소개</span>";
						strHtml += "	<br />";
						strHtml += "	<span>";
						strHtml += "" + documents.description;
						strHtml += "	</span>";
						strHtml += "</p>";
					
					$("#dvSearchDetail").html(strHtml);
					$("#dvSearchDetail").show();
					$("#dvSearchDetail").attr("tabindex", -1).focus();
					
				} else {
					
					alert("조회 중 오류가 발생했습니다. 잠시 후 다시 시도해주세요.");
					return;
					
				}
				
			}
			
			//내 검색 목록을 조회한다.
			function searchMyList() {
				
				var strUrl = "/book/myBookSearchListAjax";
				var strParam = "";
				
				var data = getAjaxData(strUrl, strParam);
				
				//검색결과를 받아 html영역을 생성한다.
				var list = data.list;

				var strHtml = "";
					strHtml += "<h4>내 검색 목록(<a href=\"#\" onclick=\"javascript:closeMyList();return false;\">[닫기]</a>)</h4>";
					
					strHtml += "<table style=\"width:500px;\">";
					strHtml += "	<tr>";
					strHtml += "		<th class=\"result_width1\">";
					strHtml += "			검색어";
					strHtml += "		</th>";
					strHtml += "		<th class=\"result_width2\">";
					strHtml += "			검색일시";
					strHtml += "		</th>";
					strHtml += "	</tr>";
				
				for(var i=0; i<list.length; i++) {
					
					strHtml += "<tr>";
					strHtml += "	<td>";
					strHtml += list[i].keyword;
					strHtml += "	</td>";
					strHtml += "	<td>";
					strHtml += list[i].regDtText;
					strHtml += "	</td>";
					strHtml += "</tr>";
					
				}
				
				if(list.length == 0) {
					
					strHtml += "<tr>";
					strHtml += "	<td colspan=\"2\">";
					strHtml += "		검색 목록이 없습니다.";
					strHtml += "	</td>";
					strHtml += "</tr>";
					
				}
				
				strHtml += "</table>";
				
				$("#dvMyList").html(strHtml);
				$("#dvMyList").show();
				
				
			}
			
			//내 검색목록을 닫는다.
			function closeMyList() {
				
				$("#dvMyList").hide();
				
			}

			//인기검색어를 조회한다.
			function getFavoriteKeyword() {
				
				var strUrl = "/book/favoriteKeywordListAjax";
				var strParam = "";
				
				var data = getAjaxData(strUrl, strParam);
				
				//검색결과를 받아 html영역을 생성한다.
				var list = data.list;

				var strHtml = "";
					strHtml += "인기 검색어 : ";
				
				for(var i=0; i<list.length; i++) {
					
					strHtml += "<span style=\"margin-right:15px;\">";
					strHtml += "<a href=\"#\" onclick=\"javascript:bindSearch('" + list[i].keyword + "');return false;\">";
					strHtml += list[i].keyword;
					strHtml += "(";
					strHtml += list[i].cnt;
					strHtml += ")";
					strHtml += "</span>";
					
				}
				
				$("#dvFavoriteKeyword").html(strHtml);
				
			}
			
			//선택한 인기검색어를 검색어에 대입 후 조회한다.
			function bindSearch(keyword) {
				
				$("#keyword").val(keyword);
				searchData();
				
			}
			
		</script>
		
	</head>
	<body>
		
		<form id="frm" name="frm" method="post" action="" onsubmit="javascript:return false;">
			
			<input type="hidden" id="pageNo" name="pageNo" value="" />
			<input type="hidden" id="isbn" name="isbn" value="" /> <!-- 도서 상세검색값 -->
			
			<h2>도서 검색 화면입니다.</h2>
			
			<div>
				<select id="keykind" name="keykind">
					<option value="">전체검색</option>
					<option value="title">제목</option>
					<option value="isbn">isbn</option>
					<option value="publisher">출판사</option>
					<option value="person">인명</option>
				</select>
				<input type="text" id="keyword" name="keyword" value="" />
				<input type="button" id="btnSearch" value="검색" />
				<input type="button" id="btnMyList" value="내 검색기록 보기" />
			</div>
			
			<div id="dvFavoriteKeyword">
				
			</div>
			
			<div id="dvMyList" class="book_detail" style="display:none;">
				
			</div>
			
			<div id="dvSearch" style="margin-top:20px;">
				<table class="result_table">
					<tr>
						<th class="result_width1">
							제목
						</th>
						<th class="result_width2">
							저자
						</th>
						<th class="result_width3">
							상세보기
						</th>
					</tr>
					<tr>
						<td colspan="3">
							검색어를 입력해주세요.
						</td>
					</tr>
				</table>
			</div>
			<div id="dvSearchDetail" class="book_detail" style="display:none;">
				
			</div>
			
		</form>
		
	</body>
</html>
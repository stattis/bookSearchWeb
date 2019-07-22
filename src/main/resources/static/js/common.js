
/**
 * Ajax로 데이터를 호출한다.
 * @param strUrl
 * @param strParam
 * @returns
 */
function getAjaxData(strUrl, strParam) {
	
	var resultJson = "";
	
	$.ajax({
		type : "POST"
		,dataType : "json"
		,async: false
		,url : strUrl
		,data : strParam
		,success : function (data, textStatus) {
			
			resultJson = data;
			
		}
	});
	
	return resultJson;
	
}

function setComma(strNumber) {
	
	var reg = /(^[+-]?\d+)(\d{3})/;
	strNumber += '';
	while (reg.test(strNumber)) {
		
		strNumber = strNumber.replace(reg, '$1' + ',' + '$2');
		
	}
	
	return strNumber;
	
}


/**
 * 페이징 영역 소스를 만들어 반환한다.
 * @param totCnt
 * @param pageLimit
 * @param pageBlock
 * @param pageNo
 * @returns
 */
function createPaging(totCnt, pageLimit, pageBlock, pageNo) {
	
	totCnt = parseInt(totCnt);
	pageLimit = parseInt(pageLimit);
	pageBlock = parseInt(pageBlock);
	pageNo = parseInt(pageNo);
	
	var returnHtml = "";
	
	//전체 페이지 개수
	var lastPageNum = parseInt(((totCnt - 1) / pageLimit) + 1);

	//화면에 보여질 시작 페이지 번호
	var startPageNum = parseInt((parseInt(((pageNo - 1) / pageBlock)) * pageBlock) + 1);
	
	//화면에 보여질 종료 페이지 번호
	var endPageNum = parseInt(startPageNum + pageBlock - 1);
	
	//종료 페이지 범위 처리
	if(endPageNum > lastPageNum) {
		
		endPageNum = lastPageNum;
		
	}
	
	//이전 페이지 그룹
	var prevPageGroup = 1;
	
	//다음 페이지 그룹
	var nextPageGroup = lastPageNum;
	
	if(startPageNum - pageBlock < 1) {
		
		prevPageGroup = 1;
		
	} else {
		
		prevPageGroup = startPageNum - pageBlock;
		if(prevPageGroup <= 0) {
			
			prevPageGroup = 1;
		}
		
	}
	
	if(endPageNum + 1 > lastPageNum) {
		
		nextPageGroup = lastPageNum;
		
	} else {
		
		nextPageGroup = endPageNum + 1;
		
	}
	
	returnHtml += "<div class=\"paging\">";
	
	//이전 페이지 그룹으로 이동
	returnHtml += "<div class=\"ctrl\"><a href=\"javascript:searchData('" + prevPageGroup + "');\" class=\"first\">&lt;</a></div>";
	
	returnHtml += "<ol>";
	
	//페이지 번호 표시
	for(var i=startPageNum; i<=endPageNum; i++) {
		
		if(i == pageNo) {
			
			returnHtml += "<li class=\"on\"><strong>" + i + "</strong></li>";
			
		} else {
			
			returnHtml += "<li><a href=\"javascript:searchData('" + i + "');\">" + i + "</a></li>";
			
		}
		
	}
	
	returnHtml += "</ol>";
	
	//다음 페이지 그룹으로 이동
	returnHtml += "<div class=\"ctrl\"><a href=\"javascript:searchData('" + nextPageGroup + "');\" class=\"last\">&gt;</a></div>";
	
	returnHtml += "</div>";
	
	return returnHtml;
	
}

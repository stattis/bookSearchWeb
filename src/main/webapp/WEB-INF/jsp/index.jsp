<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<!DOCTYPE html>


<html lang="ko">
	<head>
		<meta charset="UTF-8" />
		<meta http-equiv="X-UA-Compatible" content="ie=edge" />
		
		<script src="/js/jquery-3.4.1.min.js"></script>
		<script src="/js/common.js"></script>
		<script>
			
			//화면 초기 로딩시 실행
			$(document).ready(function() {
				
				//로그인된 회원정보가 있을 경우 로그인 영역을 숨김 처리한다.
				if('${mberNm}' != "") {
					
					setLoginArea('${mberNm}');
					
				}
				
				//로그인 버튼 클릭이벤트 등록
				$("#btnLogin").bind('click', function(e) {
					
					login();
					
				});
				
				//로그아웃 버튼 클릭이벤트 등록
				$("#btnLogout").bind('click', function(e) {
					
					logout();
					
				});
				
				//회원가입 버튼 클릭이벤트 등록
				$("#btnMemberJoin").bind('click', function(e) {
					
					location.href = "/member/memberJoin";
					
				});

				//도서 검색 버튼 클릭이벤트 등록
				$("#btnBookSearch").bind('click', function(e) {
					
					location.href = "/book/bookSearch";
					
				});
				
				//비밀번호 입력 후 엔터키 누르면 로그인 실행
				$("#mberPw").bind('keydown', function(e) {
					
					if(e.keyCode == 13) {
						
						login();
						
					}
					
				});
				
			});
			
			//로그인을 실행한다.
			function login() {
				
				if(validateData()) {
					
					//로그인 처리
					var strUrl = "/member/memberLoginAction";
					var strParam = $("#frm").serialize();
					
					var result = getAjaxData(strUrl, strParam);
					if(result.resultCode == "99") {
						
						//로그인 영역 처리
						setLoginArea(result.mberNm);
						
					} else {
						
						alert(result.resultMessage);
						return;
						
					}
					
				}
				
			}

			//로그아웃을 실행한다.
			function logout() {
				
				if(confirm("로그아웃 하시겠습니까?")) {
					
					//로그아웃 처리
					var strUrl = "/member/memberLogoutAction";
					var strParam = "";
					
					var result = getAjaxData(strUrl, strParam);
					if(result.resultCode == "99") {
						
						//로그인 영역 처리
						setLoginArea('');
						
					} else {
						
						alert(result.resultMessage);
						return;
						
					}
					
				}
				
			}
			
			//데이터를 검증 처리한다.
			function validateData() {
				
				//입력한 아이디에 대한 빈값여부 검증
				if($("#mberId").val().trim() == "") {
					
					alert("아이디를 입력해주세요.");
					$("#mberId").focus();
					return false;
					
				}

				//입력한 비밀번호에 대한 빈값여부 검증
				if($("#mberPw").val().trim() == "") {
					
					alert("비밀번호를 입력해주세요.");
					$("#mberPw").focus();
					return false;
					
				}
				
				return true;
				
			}
			
			//로그인 영역에 대한 처리
			function setLoginArea(mberNm) {
				
				if(mberNm == "") {
					
					//로그인된 회원정보가 없을 경우
					
					$("#pNonLogin").show();
					$("#pLogin").hide();
					$("#spMberNm").html("");
					
				} else {
					
					//로그인된 회원정보가 있을 경우
					
					$("#pNonLogin").hide();
					$("#pLogin").show();
					$("#spMberNm").html(mberNm);
					
				}
				
			}
			
		</script>
		
	</head>
	<body>
		
		<form id="frm" name="frm" method="post" action="">
			
			<h2>도서 검색 서비스 페이지입니다.</h2>
			
			회원가입 후 마음에 드는 책을 검색해 보세요!
			
			<p id="pNonLogin">
				아이디 : <input type="text" id="mberId" name="mberId" maxlength="50" value="" style="width:150px;" />
				비밀번호 : <input type="password" id="mberPw" name="mberPw" value="" style="width:150px;" />
				<input type="button" id="btnLogin" value="로그인" />
			</p>
			<p id="pLogin" style="display:none;">
				<span id="spMberNm"></span> 님, 환영합니다.
				<input type="button" id="btnLogout" value="로그아웃" />
			</p>
			
			<p>
				<input type="button" id="btnMemberJoin" value="회원가입" />
				<input type="button" id="btnBookSearch" value="도서 검색" />
			</p>
			
		</form>
		
	</body>
</html>
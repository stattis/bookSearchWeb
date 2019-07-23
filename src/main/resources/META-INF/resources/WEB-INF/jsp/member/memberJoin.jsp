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

				//회원가입 버튼 클릭이벤트 등록
				$("#btnJoin").bind('click', function(e) {
					
					joinMember();
					
				});

				//첫 화면으로 돌아가기
				$("#btnIndex").bind('click', function(e) {
					
					location.href = "/index";
					
				});
				
			});
			
			//회원가입을 실행한다.
			function joinMember() {
				
				if(validateData()) {
					
					//회원정보 저장처리
					var strUrl = "/member/memberJoinAction";
					var strParam = $("#frm").serialize();
					
					var result = getAjaxData(strUrl, strParam);
					if(result.resultCode == "99") {
						
						alert("회원가입이 정상적으로 이루어졌습니다.\n책 검색 서비스를 이용하실 수 있습니다.");
						location.href = "/index";
						
					} else {
						
						alert(result.resultMessage);
						return;
						
					}
					
				}
				
			}
			
			//데이터를 검증 처리한다.
			function validateData() {
				
				//아이디와 비밀번호에 대한 적합성 검증 정규식
				var testId = /[ㄱ-ㅎ|ㅏ-ㅣ|가-힣]/;
				var testPw = /^(?=.*[a-zA-Z])(?=.*[0-9]).{5,}$/;
				
				//입력한 아이디에 대한 빈값여부 검증
				if($("#mberId").val().trim() == "") {
					
					alert("사용하실 회원 아이디를 입력해주세요.");
					$("#mberId").focus();
					return false;
					
				}
				
				//아이디 길이(50자 이상)에 대한 검증
				if($("#mberId").val().length < 5 || $("#mberId").val().length > 50) {
					
					alert("회원 아이디를 5자 이상, 50자 이하로 입력해주세요.");
					$("#mberId").focus();
					return false;
					
				}
				
				//아이디를 한글만 입력했는지 검증
				if(testId.test($("#mberId").val())) {
					
					alert("회원 아이디에는 한글을 사용할 수 없습니다.");
					$("#mberId").focus();
					return false;
					
				}
				
				//아이디 중복여부 검증
				var strUrl = "/member/checkMberIdExist";
				var strParam = "mberId=" + $("#mberId").val();
				
				var result = getAjaxData(strUrl, strParam);
				if(result.resultCode == "1") {
					
					alert("이미 사용중인 아이디입니다. 다른 아이디를 입력해주세요.");
					$("#mberId").focus();
					return false;
					
				}
						
				//입력한 이름에 대한 빈값여부 검증
				if($("#mberNm").val().trim() == "") {
					
					alert("사용하실 회원 이름을 입력해주세요.");
					$("#mberNm").focus();
					return false;
					
				}
				
				//입력한 비밀번호에 대한 빈값여부 검증
				if($("#mberPw").val().trim() == "") {
					
					alert("비밀번호를 입력해주세요.");
					$("#mberPw").focus();
					return false;
					
				}
				
				//입력한 비밀번호에 대한 길이 및 적합여부 검증
				if(!testPw.test($("#mberPw").val())) {
					
					alert("입력하신 비밀번호는 사용하기에 적합하지 않습니다.\n영어, 숫자를 포함하여 5자리 이상 입력해주세요.");
					$("#mberPw").focus();
					return false;
					
				}
				
				//비밀번호 확인값 일치여부 검증
				if($("#mberPw").val().trim() != $("#mberPwConfirm").val().trim()) {
					
					alert("입력하신 비밀번호 확인값이 비밀번호와 일치하지 않습니다.");
					$("#mberPwConfirm").focus();
					return false;
					
				}
				
				return true;
				
			}
			
		</script>
		
	</head>
	<body>
		
		<form id="frm" name="frm" method="post" action="">
			
			<h2>회원 가입 화면입니다.</h2>
			
			<table>
				<tr>
					<td>
						아이디
					</td>
					<td>
						<input type="text" id="mberId" name="mberId" maxlength="50" value="" />
					</td>
				</tr>
				<tr>
					<td>
						이름
					</td>
					<td>
						<input type="text" id="mberNm" name="mberNm" style="ime-mode:active;" value="" />
					</td>
				</tr>
				<tr>
					<td>
						비밀번호
					</td>
					<td>
						<input type="password" id="mberPw" name="mberPw" value="" />
					</td>
				</tr>
				<tr>
					<td>
						비밀번호 확인
					</td>
					<td>
						<input type="password" id="mberPwConfirm" name="mberPwConfirm" value="" />
					</td>
				</tr>
			</table>
			
			<input type="button" id="btnJoin" value="회원가입" />
			<input type="button" id="btnIndex" value="첫화면으로 돌아가기" />
			
		</form>
		
	</body>
</html>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<%@ include file="/WEB-INF/views/include/head.jsp" %>
<style type="text/css">
body{
	width:100%;
	height :100vh;
	display:flex;
	justify-content: center;
	align-items: center;
}
h1{
	text-align: center;
}
#userId{
	width:230px;
}
.tit{
   display:block; 
   width:150px;
}
.valid_msg{
	color:red;   
	font-size:13px;
	display:block;
}
.input{
	width:300px;
}
</style>
</head>
<body>
	
    <form action="/member/join" method="post" id="frm_join">
    <h1>회원 가입 양식</h1>
     <table>
        <tr>
           <td>ID : </td>
           <td>
              <input class="input" type="text" name="userId" id="userId" size="10" placeholder="아이디를 입력하세요"
              <c:if test="${not empty param.err and empty joinValid.userId}">
              	value = "${joinForm.userId}"
              </c:if>
              required/>
              <button type="button" id="btnIdCheck">중복확인</button>
              <span id="idCheck" class="valid_msg" >
	              <c:if test="${not empty param.err and not empty joinValid.userId}">
	              	이미 존재하는 아이디 입니다.
	              </c:if>
	          </span>
              
              
           </td>
        </tr>
        <tr>
           <td>PASSWORD : </td>
           <td>
              <input class="input" type="password" name="password" id="password" placeholder="패스워드를 입력하세요" 
              <c:if test="${not empty param.err and empty joinValid.password}">
              	value = "${joinForm.password}"
              </c:if>
              required/>
              <span id="pwCheck" class="valid_msg">
              	<c:if test="${not empty param.err and not empty joinValid.password}">
	              	비밀번호는 영문자, 숫자, 특수문자 조합 8자 이상이어야 합니다.
	            </c:if>
              </span>
           </td>
        </tr>
        <tr>
           <td>휴대폰번호 : </td>
           <td>
              <input class="input" type="tel" name="tell" id="tell" placeholder="숫자만 입력하세요" 
              <c:if test="${not empty param.err and empty joinValid.tell}">
              	value = "${joinForm.tell}"
              </c:if>
              required/>
           	  <span id="tellCheck" class="valid_msg">
           	    <c:if test="${not empty param.err and not empty joinValid.tell}">
	              	숫자 9~11자를 입력하세요.
	            </c:if>
           	  </span>
           </td>
        </tr>
        <tr>
           <td>email : </td>
           <td>
              <input class="input" type="email" name="email"
              <c:if test="${empty joinValid.email}">
	              	value = "${joinForm.email}"
	          </c:if>
              required/>

           </td>
        </tr>
        <tr>
           <td>
              <input type="submit" value="가입" />
              <input type="reset" value="취소" />
           </td>
       </tr>
  	 </table>
   </form>
   
   <script type="text/javascript" src="/resources/js/member/joinForm.js"></script>

</body>
</html>
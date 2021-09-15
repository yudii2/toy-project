<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<%@ include file="/WEB-INF/views/include/head.jsp" %>
<style type="text/css">
h1{
	text-align:center;
	font-size:18px;
}
h2{
	display:inline-block;
}
a{
	text-decoration: none;
	font-size:14px;
}
</style>
</head>

<body>
<h1>PCLASS TOY PROJECT</h1>

<%-- 인증정보가 없다면 로그아웃 상태 --%>
<c:if test="${empty authentication}">
	<h2><a href='/member/login-form'>LOGIN</a></h2>
	<h2><a href='/member/join-form'>JOIN</a></h2>
</c:if>
<%-- 인증정보가 있다면 로그인 상태 --%>
<c:if test="${not empty authentication}">
	<h2><a href='/member/logout'>LOGOUT</a></h2>
	<h2><a href='/member/mypage'>마이페이지</a></h2>
	<h2><a href='/board/board-form'>게시글쓰기</a></h2>
</c:if>



</body>
</html>
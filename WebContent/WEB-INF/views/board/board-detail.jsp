<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>

<%@ include file="/WEB-INF/views/include/head.jsp" %>
<link rel="stylesheet" href="/resources/css/board/board-detail.css">

</head>
<body>

<div class="content">
	<h2 class="tit">게시판</h2>
	<div class="info">
		<span>번호 : <c:out value='${datas.board.bdIdx}'/></span>
		<span>제목 : <c:out value='${datas.board.title}'/></span>
		<span>등록일 : <c:out value='${datas.board.regDate}'/></span>
		<span>작성자 : <c:out value='${datas.board.userId}'/></span>
	</div>
	<div class="info file_info">
		<ol>
			<c:forEach items='${datas.files}' var="file">
				<!-- 사용자에게 파일 다운로드를 요청할 url -->
				<li><a href='${file.downloadURL}'>${file.originFileName}</a></li>
			</c:forEach>
		</ol>
	</div>
		
	<div class="article_content">
		<pre><c:out value='${datas.board.content}'/></pre>
	</div>
	<div class="bg">
		<img src="http://localhost:7070/file/2021/9/10/9234567b-2df3-4ee1-a6c1-c0c86f77cc54"/>
	</div>


</div>













</body>
</html>
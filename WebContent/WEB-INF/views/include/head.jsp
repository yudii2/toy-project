<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%-- el태그 활용 --%>
<c:set var="contextPath" value="${pageContext.request.contextPath}"/>
<meta charset="UTF-8">
<title>Insert title here</title>

<%-- 이전에 표현태그를 활용해 등록했던 방식
	String contextPath = request.getContextPath();
--%>

<link rel="stylesheet" href="${contextPath}/resources/css/all.css">
<script type="text/javascript" src="${contextPath}/resources/js/webUtil.js"></script>
    
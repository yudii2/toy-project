package com.kh.toy.common.code;

public enum ErrorCode {
	
	DATABASE_ACCESS_ERROR("데이터베이스와 통신 중 에러가 발생하였습니다."),
	FAILED_VALIDATE_ERROR("데이터 양식이 적합하지 않습니다."),
	FAILED_MAILING_ERROR("이메일 발송 중 에러가 발생하였습니다."),
	HTTP_CONNECT_ERROR("HTTP통신 중 에러가 발생하였습니다."),
	AUTHENTICATION_EXPIRED_ERROR("유효하지 않은 인증입니다."),
	UNAUTHORIZED_PAGE("접근 권한이 없는 페이지입니다."),
	REDIRECT_TO_LOGIN_PAGE_NO_MSG("","/member/login-form"),		//HandleableException.java 오버로딩된 생성자
	FAILED_FILE_UPLOAD_ERROR("파일업로드에 실패하였습니다.");
	
	
	public final String MSG;
	public final String URL;

	ErrorCode(String msg){
		this.MSG = msg;
		this.URL = "/index";	//생성자로써 상수들을 초기화해줘야 함
	}
	
	ErrorCode(String msg,String url){
		this.MSG = msg;
		this.URL = url;
	}	
}

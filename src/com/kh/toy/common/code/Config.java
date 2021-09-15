package com.kh.toy.common.code;

public enum Config {

	//DOMAIN("http://www.littleCarrest.com"),
	DOMAIN("http://localhost:7070"),
	SMTP_AUTHENTICATION_ID("qleen513@gmail.com"),
	SMTP_AUTHENTICATION_PASSWORD("Bmw135798@"),
	OFFICIAL_MAIL("qleen513@gmail.com"),
	//UPLOAD_PATH("C:\\CODE\\upload\\");//운영서버용
	FILE_UPLOAD_PATH("C:\\CODE\\upload\\"),	//개발서버용
	IMG_UPLOAD_PATH("");	//이미지 저장경로(브라우저 출력)	
	
	
	public final String DESC;
	
	private Config(String desc) {
		this.DESC = desc;
	}
}

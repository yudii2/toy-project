package com.kh.toy.common.exception;

import com.kh.toy.common.code.ErrorCode;

public class HandleableException extends RuntimeException{

	private static final long serialVersionUID = 7847451612288681161L;
	
	public ErrorCode error;
	
	//log를 남기지 않고 사용자에게 알림메시지만 전달하는 용도의 생성자
	public HandleableException(ErrorCode error) {
		this.error = error;
		//stackTrace를 비워준다.
		this.setStackTrace(new StackTraceElement[0]);
	}
	
	public HandleableException(ErrorCode error,Exception e) {
		this.error = error;
		e.printStackTrace();
		this.setStackTrace(new StackTraceElement[0]);
	}

}

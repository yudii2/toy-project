package com.kh.toy.member.model.dto;

//Builder패턴 학습용 클래스
public class User {
	
	//객체 생성 패턴
	//객체의 속성을 초기화하고 생성하는 디자인패턴
	
	//	1. 점층적 생성자 패턴
	//	생성자의 매개변수를 통해 객체의 속성을 초기화하고 생성하는 패턴
	//	단점 : 코드만 보고 생성자의 매개변수가 어떤 객체를 초기화하는지 알기 어렵다
	//			즉, 가독성이 좋지 않다.
	
	//	2. 자바빈 패턴 : getter/setter
	//	단점 : public 메서드인 setter를 통해 속성을 초기화하기 때문에, 변경불가능한 객체(immutable)를 만들 수 없다.
	
	
	
	private String userId;
	private String password;
	private String email;
	private String tell;
	
	
	
}

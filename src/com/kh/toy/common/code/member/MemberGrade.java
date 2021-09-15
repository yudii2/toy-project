package com.kh.toy.common.code.member;

//enum(enumerated type) : 열거형
//서로 연관된 상수들의 집합
//서로 연관된 상수들을 하나의 묶음으로 다루기 위한 enum만의 문법적 형식과 메서드를 제공해준다.
public enum MemberGrade {
	
	//회원등급코드가 ME00이면 등급명은 "일반"이면서 연장가능횟수 1회
	//ENUM을 활용하면 연관된 것을 하나의 묶음으로 처리할 수 있는 편리한 이점이 있다.

	//내부적으로 enum은 class이다.
	//ME00("일반",1) -> public static final MemberGrade ME00 = new MemberGrade("일반",1);
	//이름이 ME00인 인스턴스를 만들어주는 것.
	//클래스를 인스턴스화 할 수 있도록 지원해주는 ENUM
	//public 이기 때문에 어디에서나 접근이 가능하고, static이기 때문에 언제나 접근이 가능한 인스턴스에
	//등급명과 연장횟수를 저장해두고 getter를 통해서 반환받아 사용한다.
	ME00("일반","user", 1),
	ME01("성실","user", 2),
	ME02("우수","user", 3),
	ME03("vip","user", 4),
	
	AD00("super", "admin"),		
	AD01("member", "admin"),	//회원관리 권한
	AD02("board", "admin");		//게시판관리 권한
	
	public final String LEVEL;
	public final String ROLE;
	public final int EXTENSIBLE_CNT;
	
	MemberGrade(String level, String role) {
		this.LEVEL = level;
		this.ROLE = role;
		this.EXTENSIBLE_CNT = 9999;
	}
	
	private MemberGrade(String level, String role, int extensibleCnt) {
		this.LEVEL = level;
		this.ROLE = role;
		this.EXTENSIBLE_CNT = extensibleCnt;
	}


	
}

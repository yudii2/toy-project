package com.kh.toy.member.validator;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;

import com.kh.toy.member.model.service.MemberService;

//filter에서 호출될 회원가입 유효성 검증용 클래스
public class JoinForm {
	
	private String userId;
	private String password;
	private String email;
	private String tell;
	HttpServletRequest request;
	
	private MemberService memberService = new MemberService();	//아이디가 기존회원 아이디와 중복되는지 확인 위함
	
	private Map<String, String> failedValidation = new HashMap<String, String>();	//validation 실패한 값 저장

	public JoinForm(ServletRequest request) {
		this.request = (HttpServletRequest)request;
		this.userId = request.getParameter("userId");
		this.password = request.getParameter("password");
		this.email = request.getParameter("email");
		this.tell = request.getParameter("tell");
	}
	
	
	
	public String getUserId() {
		return userId;
	}



	public String getPassword() {
		return password;
	}



	public String getEmail() {
		return email;
	}



	public String getTell() {
		return tell;
	}



	public boolean test() {
		boolean isFailed = false;
				
		//유효성 검사
		//	1. 사용자 아이디가 DB에 이미 존재하는지 확인 
		if(memberService.selectMemberById(userId) != null || userId.equals("")) {
			failedValidation.put("userId",userId);
			isFailed = true;
		}
		
		//	2. 비밀번호 : 영문자, 숫자, 특수문자 조합 8자리 이상 
		if(!Pattern.matches("(?=.*[a-zA-Z])(?=.*[0-9])(?=.*[^a-zA-Zㄱ-힣0-9]).{8,}", password)) {
			failedValidation.put("password", password);
			isFailed = true;
		}
		
		//	3. 전화번호 : 숫자로만 이루어져 있는지
		if(!Pattern.matches("\\d{9,11}", tell)) {
			failedValidation.put("tell", tell);
			isFailed = true;
		}
		
		
		
		//입력된 내용들을 그대로 두되, 유효성검사를 통과하지 못한 input은 값을 삭제하고 focusing
		if(isFailed) {
			request.getSession().setAttribute("joinValid", failedValidation);
			request.getSession().setAttribute("joinForm", this);	//join-form자체(입력된 내용 포함)를 저장
			return false;
		}else {
			request.getSession().removeAttribute("joinForm");
			request.getSession().removeAttribute("joinValid");
			return true;
		}
	}
	
}

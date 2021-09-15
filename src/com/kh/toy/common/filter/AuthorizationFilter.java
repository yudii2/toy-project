package com.kh.toy.common.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.kh.toy.common.code.ErrorCode;
import com.kh.toy.common.code.member.MemberGrade;
import com.kh.toy.common.exception.HandleableException;
import com.kh.toy.member.model.dto.Member;
import com.kh.toy.member.validator.JoinForm;

//권한 부여 필터
public class AuthorizationFilter implements Filter {

    /**
     * Default constructor. 
     */
    public AuthorizationFilter() {
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see Filter#destroy()
	 */
	public void destroy() {
		// TODO Auto-generated method stub
	}

	/**
	 * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
	 */
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest httpRequest = (HttpServletRequest) request;		
		HttpServletResponse httpResponse = (HttpServletResponse) response;
		
		String[] uriArr = httpRequest.getRequestURI().split("/");
	
		
		if(uriArr.length != 0) {
			switch (uriArr[1]) {
				case "member":
					memberAuthorize(httpRequest, httpResponse, uriArr);
					break;
				case "admin":
					adminAuthorize(httpRequest, httpResponse, uriArr);
					break;
				case "board":
					boardAuthorize(httpRequest, httpResponse, uriArr);
					break;
				default:
					break;
			}
		}
		
		chain.doFilter(request, response);
		
	}

	private void boardAuthorize(HttpServletRequest httpRequest, HttpServletResponse httpResponse, String[] uriArr) {
		
		HttpSession session = httpRequest.getSession();
		Member member = (Member) session.getAttribute("authentication");
		
		
		//비회원일 때 게시글 접근을 금지
		switch (uriArr[2]) {
		case "board-form":
			if(member == null) {
				throw new HandleableException(ErrorCode.UNAUTHORIZED_PAGE);
			}
			break;
		case "upload":
			if(member == null) {
				throw new HandleableException(ErrorCode.UNAUTHORIZED_PAGE);
			}
			break;
		default:
			break;
		}
		
	}

	private void adminAuthorize(HttpServletRequest httpRequest, HttpServletResponse httpResponse, String[] uriArr) {
		
		Member member = (Member) httpRequest.getSession().getAttribute("authentication");
		MemberGrade adminGrade = MemberGrade.valueOf(member.getGrade());
		
		//넘어온 인증정보가 관리자인지 사용자인지 판단
		if(member == null || adminGrade == null) {
			throw new HandleableException(ErrorCode.UNAUTHORIZED_PAGE);
		}
		
		//super관리자는 통과
		if(adminGrade.LEVEL.equals("super")) return;
		
		switch (uriArr[2]) {
		case "member":
			//회원과 관련된 관리를 수행하는 admin의 등급은 AD01
			if(!adminGrade.LEVEL.equals("member")) {
				throw new HandleableException(ErrorCode.UNAUTHORIZED_PAGE);
			}
			break;
		case "board":
			//게시판과 관련된 관리를 수행하는 admin의 등급은 AD02
			if(!adminGrade.LEVEL.equals("board")) {
				throw new HandleableException(ErrorCode.UNAUTHORIZED_PAGE);

			}
			break;
		default:
			break;
		}
		
		
	}

	private void memberAuthorize(HttpServletRequest httpRequest, HttpServletResponse httpResponse, String[] uriArr) throws ServletException, IOException {

		switch (uriArr[2]) {
		case "join-impl":
			String serverToken = httpRequest.getParameter("persist-token");
			String clientToken = httpRequest.getParameter("persist-token");
			
			if(serverToken == null || !serverToken.equals(clientToken)) {
				//같지않으면,
				throw new HandleableException(ErrorCode.AUTHENTICATION_EXPIRED_ERROR);
			}
			break;
		case "mypage":
			//에러메시지 없이 바로 로그인 화면으로 이동
			if(httpRequest.getSession().getAttribute("authentication") == null) {
				throw new HandleableException(ErrorCode.REDIRECT_TO_LOGIN_PAGE_NO_MSG);
			}
		default:
			break;
		}
		
	}

	private String memberValidation(HttpServletRequest httpRequest, HttpServletResponse httpResponse, String[] uriArr) throws IOException {
		
		String redirectURI = null;
		switch (uriArr[2]) {
		case "join":
			JoinForm joinForm = new JoinForm(httpRequest);
			if(!joinForm.test()) {
				redirectURI = "/member/join-form?err=1";	//err파라미터 전달(왜? 이때만 validation출력)
			}break;
		default:
			break;
		}
		return redirectURI;
		
	}

	/**
	 * @see Filter#init(FilterConfig)
	 */
	public void init(FilterConfig fConfig) throws ServletException {
		// TODO Auto-generated method stub
	}

}

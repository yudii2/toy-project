package com.kh.toy.common.filter;

import java.io.IOException;
import java.util.Arrays;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.kh.toy.common.code.ErrorCode;
import com.kh.toy.common.exception.HandleableException;
import com.kh.toy.member.validator.JoinForm;

public class ValidatorFilter implements Filter {

    /**
     * Default constructor. 
     */
    public ValidatorFilter() {
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
		
		System.out.println(Arrays.toString(uriArr));
		//1번 인덱스에 따라 각기 다르게 (validating설정)???
		
		if(uriArr.length != 0) {
			
			//수정, 추가, 삭제의 경우 DB에 영향 => validating 필요
			//	ex) 회원가입, 탈퇴, 게시글 수정, 삭제
			String redirectURI = null;

			switch (uriArr[1]) {
				case "member":
					redirectURI = memberValidation(httpRequest, httpResponse, uriArr);
					break;

				default:
					break;
			}
			if(redirectURI != null) {
				httpResponse.sendRedirect(redirectURI);
				return;
			}
		}
		
		chain.doFilter(request, response);
		
	}

	private String memberValidation(HttpServletRequest httpRequest, HttpServletResponse httpResponse, String[] uriArr) throws IOException {
		
		String redirectURI = null;
		
		switch (uriArr[2]) {
		case "join":
			JoinForm joinForm = new JoinForm(httpRequest);
			if(!joinForm.test()) {
				redirectURI = "/member/join-form?err=1";	//err파라미터 전달(왜? 이때만 validation출력)
			}break;
		case "join-impl":
			String persistToken = httpRequest.getParameter("persist-token");
			if(!persistToken.equals(httpRequest.getSession().getAttribute("persist-token"))) {
				//같지않으면,
				throw new HandleableException(ErrorCode.AUTHENTICATION_EXPIRED_ERROR);
			}
			break;
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

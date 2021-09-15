package com.kh.toy.common.filter;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;



//요청의 끝 지점인 servlet이 호출되기 전 호출되는 filter
public class EncodingFilter implements Filter {

    public EncodingFilter() {
        // TODO Auto-generated constructor stub
    }


    //response 인코딩은 지정하지 않은 이유 : jsp에서 자동적으로 처리해주기 때문
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		request.setCharacterEncoding("UTF-8");
		
		//다음 filter에게 request, response객체를 전달
		//마지막 filter였다면 Servlet객체에게 request,response객체를 전달
		chain.doFilter(request, response);
	}
	

	public void destroy() {
		// TODO Auto-generated method stub
	}


	public void init(FilterConfig fConfig) throws ServletException {
		// TODO Auto-generated method stub
	}

}

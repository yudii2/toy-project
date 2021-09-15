package com.kh.toy.common.filter;

import java.io.IOException;
import java.net.URLEncoder;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public class FileFilter implements Filter {

    /**
     * Default constructor. 
     */
    public FileFilter() {
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
		HttpServletResponse httpResponse = (HttpServletResponse) response;
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		
		String originFileName = httpRequest.getParameter("originName");
		
		if(originFileName != null) {
			//encoding처리할 경우, board-detail.jsp <img>태그에서 originName을 넘겨주지 않을 때, nullpointerException발생
			originFileName = URLEncoder.encode(httpRequest.getParameter("originName"),"UTF-8");	
			httpResponse.setHeader("content-Disposition", "attachment; filename=" + originFileName);
		}
		
		//originFileName 값이 null일 경우, dofilter로 넘겨 처리하면 정상적으로 img를 브라우저에 띄울 수 있다.
		chain.doFilter(request, response);
	}

	/**
	 * @see Filter#init(FilterConfig)
	 */
	public void init(FilterConfig fConfig) throws ServletException {
		// TODO Auto-generated method stub
	}

}

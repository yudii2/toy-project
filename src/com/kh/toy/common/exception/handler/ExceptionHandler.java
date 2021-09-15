package com.kh.toy.common.exception.handler;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.kh.toy.common.exception.HandleableException;

//CUSTOM Exception 발생시
//servlet container에 의해 요청이 재지정 될 servlet

@WebServlet("/exception-handler")
public class ExceptionHandler extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ExceptionHandler() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		//서블릿 컨테이너는 HandleableException이 발생하면 요청을 HandleableException으로 요청 재지정
		//이 때, ExceptionHandler의 서비스 메서드로 넘겨주는 request객체에 발생한 예외 객체(e)를
		//request 속성(javax.servlet.error.exception)에 e객체를 같이 넘겨주어야 한다.
		
		HandleableException e = (HandleableException)request.getAttribute("javax.servlet.error.exception");
		
		System.out.println("DataAccessException이 발생하여 ExeptionHandler가 호출됩니다");
		request.setAttribute("msg", e.error.MSG);
		//request.setAttribute("back", "1");	//뒤로가기
		request.setAttribute("url", e.error.URL);	//index.jsp 페이지로 이동
		request.getRequestDispatcher("WEB-INF/views/common/result.jsp").forward(request, response);
	
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}

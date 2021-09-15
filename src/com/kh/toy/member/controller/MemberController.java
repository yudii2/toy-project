package com.kh.toy.member.controller;

import java.io.IOException;
import java.sql.Array;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.UUID;
import java.util.regex.Pattern;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.MimeMessage;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.kh.toy.common.code.ErrorCode;
import com.kh.toy.common.exception.HandleableException;
import com.kh.toy.common.exception.PageNotFoundException;
import com.kh.toy.common.http.HttpConnector;
import com.kh.toy.common.mail.MailSender;
import com.kh.toy.member.model.dto.Member;
import com.kh.toy.member.model.service.MemberService;

/**
 * Servlet implementation class MemberController
 */
@WebServlet("/member/*")
public class MemberController extends HttpServlet {
	
	MemberService memberService = new MemberService();
	
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public MemberController() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String[] uriArr = request.getRequestURI().split("/");
		switch (uriArr[uriArr.length - 1]) {
		case "login-form":
			loginForm(request,response);
			break;
		case "login":
			login(request,response);
			break;
		case "join-form":
			joinForm(request,response);
			break;
		case "join":
			join(request,response);
			break;
		case "join-impl":
			joinImpl(request,response);
			break;
		case "id-check":
			checkID(request,response);
			break;
		case "logout":
			logout(request,response);
			break;
		case "mypage":
			mypage(request,response);
			break;
		default:throw new PageNotFoundException();		//우리가 만든 url이 아닌 다른 url로 이동할 경우 특정 exception(새로 custom exception만듦)발생시킴

		}
		
		
		
	}
	private void mypage(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.getRequestDispatcher("/member/mypage").forward(request, response);
		
	}

	private void logout(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.getSession().removeAttribute("authenticaion");
		response.sendRedirect("/");
		
	}

	private void joinForm(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.getRequestDispatcher("/member/join-form").forward(request, response);
		
	}
	
	private void join(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String userId = request.getParameter("userId");
		String password = request.getParameter("password");
		String tell = request.getParameter("tell");
		String email = request.getParameter("email");
		
		Member member = new Member();
		member.setUserId(userId);
		member.setPassword(password);
		member.setTell(tell);
		member.setEmail(email);
		
		String persistToken = UUID.randomUUID().toString();
		request.getSession().setAttribute("persistUser", member);
		request.getSession().setAttribute("persist-token",persistToken);	//랜덤으로 unique Id를 생성해줌
		
		memberService.authenticateEmail(member,persistToken);	//우리가 client이면서 서버이기 때문에 session을 저장할 수 없다. 서비스단에 데이터 직접 저장
		
		request.setAttribute("msg", "회원가입을 위한 이메일이 발송되었습니다.");
		request.setAttribute("url", "/member/login-form");
		request.getRequestDispatcher("/common/result").forward(request, response);
		
//		String email = request.getParameter("email");
//		MailSender mailSender = new MailSender();
//		mailSender.sendEmail(email, "회원가입을 축하합니다.", "<h1>OO님 반가워요! 환영합니다.</h1>");
		
	}

	//사용자가 join-form 에서 전송버튼을 누르면 join으로 post요청함 > 이메일jsp에서 회원가입 완료 버튼 클릭 
	//	> <a href=".../join-impl?persist..">을 통해 joinImpl(이메일인증절차) 메서드 호출
	private void joinImpl(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		HttpSession session = request.getSession();
		memberService.insertMember((Member) session.getAttribute("persistUser"));
		
		//같은 persistUser값이 두 번 DB에 입력되지 않도록 사용자 정보와 인증을 만료시킴
		session.removeAttribute("persistUser");
		session.removeAttribute("persist-token");
		response.sendRedirect("/member/login-form");
		
		
	}
	

	private void checkID(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//사용자가 보낸 아이디를 받아
		String userId = request.getParameter("userId");
		//기존 멤버에 아이디가 존재하는지 비교
		Member member = memberService.selectMemberById(userId);
		// *** response.sendRedirect를 사용할 수 없는 이유
		//		fetch를 통해 값을 비동기적으로 받아와야 한다.(html을 유지하면서 값을 저장하기 위함)
		//		fetch는 자바스크립트, 지금 우리는 내부 서버와 통신하고 있는데 자바스크립트는 이를 인지하지 못한다
		//		따라서, fetch를 활용하기 위해서는 getWriter()메서드를 활용해 브라우저에서 인지할 수 있도록 코드를 짜야한다.
		if(member == null) {
			response.getWriter().print("available");
		}else {
			response.getWriter().print("disable");
		}
		
	}


	



	private void login(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String userId = request.getParameter("userId");
		String password = request.getParameter("password");
		
		Member member = null;	//이 때, member는 String이 아니므로 공백("")으로 초기화할 수 없다!
		
		//1. 시스템에서 문제가 생겨서 (ex. DB가 뻗었다던가, 외부 api서버가 죽었다던가 등)
		//	 예외가 발생하는 경우 =>  service단에서 예외처리(catch구문) 진행
		member = memberService.memberAuthenticate(userId,password);		//일치하는 회원이 없다면 초기값 null을 반환
		
//		
//		try {
//			member = memberService.memberAuthenticate(userId,password);
//			
//		}catch(Exception e){
//			request.setAttribute("msg", "회원정보를 조회하는 도중 예외가 발생했습니다.");
//			//request.setAttribute("back", "1");	//뒤로가기
//			request.setAttribute("url", "/index");	//index.jsp 페이지로 이동
//			request.getRequestDispatcher("/error/result").forward(request, response);
//			return;
//		}
		
		
		
		//2. 사용자가 잘못된 아이디나 비밀번호를 입력한 경우, 
		//	 내부적인 문제x 사용자에게 아이디나 비밀번호가 틀렸음을 알림, login-form으로 redirect
		if(member == null) {
			response.sendRedirect("/member/login-form?err=1");
			return;
		}
		//성공적으로 전달
		request.getSession().setAttribute("authentication", member);
		response.sendRedirect("/index");
	}
	
	
	

	private void loginForm(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.getRequestDispatcher("/member/login").forward(request, response);
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}

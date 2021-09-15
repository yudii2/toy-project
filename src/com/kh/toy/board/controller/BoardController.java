package com.kh.toy.board.controller;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.kh.toy.board.model.dto.Board;
import com.kh.toy.board.model.service.BoardService;
import com.kh.toy.common.exception.PageNotFoundException;
import com.kh.toy.common.file.FileDTO;
import com.kh.toy.common.file.FileUtil;
import com.kh.toy.common.file.MultiPartParams;
import com.kh.toy.member.model.dto.Member;
import com.oreilly.servlet.MultipartRequest;

/**
 * Servlet implementation class BoardController
 */
@WebServlet("/board/*")
public class BoardController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	private BoardService boardService = new BoardService();
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public BoardController() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String[] uriArr = request.getRequestURI().split("/");
		
		switch (uriArr[uriArr.length-1]) {
		case "board-form":
			boardForm(request,response);
			break;
		case "upload":
			upload(request,response);
			break;
		case "board-detail":
			boardDetail(request,response);
			break;
		default:throw new PageNotFoundException();
		}
	}

	private void boardDetail(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		//게시글 상세데이터 불러오기 by게시글 식별자 필요(bdIdx)
		String bdIdx = request.getParameter("bdIdx");
		
		//식별자에 따른 게시글 조회 service단에 요청
		Map<String, Object> datas = boardService.selectBoardDetail(bdIdx);
		
		request.setAttribute("datas", datas);	//datas Map데이터 안의 board key를 가지는 value조회 -> jsp에서 조회
		
		request.getRequestDispatcher("/board/board-detail").forward(request, response);
		
		
		
		
		
	}

	private void upload(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		//multipart-form data는 mime-type이 form-urlencoded방식이 아니기 때문에 request getParameter메서드를 사용할 수 없다.
		//multipart parser를 사용해보자
		/*
		 * MultipartRequest multipartRequest = new MultipartRequest(request,"C:\\CODE\\upload\\"); 
		 * String title = multipartRequest.getParameter("title");
		 * String content = multipartRequest.getParameter("content");
		 * 
		 * System.out.println("제목 : " + title); System.out.println("본문 : " + content);
		 */
		
		FileUtil util = new FileUtil();
		MultiPartParams params = util.fileUpload(request);
		
		//로그인한 사용자만 board작성 가능
		Member member = (Member) request.getSession().getAttribute("authentication");
		
		//게시물 저장을 위해 boardDTO 생성
		Board board = new Board();
		board.setUserId(member.getUserId());	//세션에 인증절차 통과한 사용자 정보 등록
		board.setTitle(params.getParameter("title"));
		board.setContent(params.getParameter("content"));
		
		//file정보 가져오기
		List<FileDTO> fileDTOs = params.getFilesInfo();
		
//		for(fileDTO fileDTO : fileDTOs) {
//			System.out.println(fileDTO);
//		}
		
		boardService.insertBoard(board, fileDTOs);
		response.sendRedirect("/");	//index
		
		
//		String[] titles = params.getParameterValues("title");
//		System.out.println(Arrays.toString(titles));


		
	}

	private void boardForm(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.getRequestDispatcher("/board/board-form").forward(request, response);
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}

package test.http;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.kh.toy.common.exception.PageNotFoundException;
import com.kh.toy.common.http.HttpConnector;

/**
 * Servlet implementation class HttpConnectorTest
 */
@WebServlet("/test/http/*")
public class HttpConnectorTest extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public HttpConnectorTest() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		String[] uriArr = request.getRequestURI().split("/");
		switch (uriArr[uriArr.length - 1]) {
		case "test-kakao":
			testKakaoAPI();
			break;
		case "test-naver":
			testNaverAPI();
			break;
		default:throw new PageNotFoundException();		//우리가 만든 url이 아닌 다른 url로 이동할 경우 특정 exception(새로 custom exception만듦)발생시킴

		}		
	}
	
	
	private void testNaverAPI() {
		
		Gson gson = new Gson();
		
		Map<String,Object> body = new LinkedHashMap<String, Object>();
		List<Map<String,Object>> keywordGroups = new ArrayList<Map<String,Object>>();
		Map<String,Object> keywordGroup = new LinkedHashMap<String, Object>();
		List<String> keywords = new ArrayList<String>();
		
		keywords.add("GIT");
		keywords.add("GITHUB");
		
		keywordGroup.put("groupName","자바");
		keywordGroup.put("keywords",keywords);
		
		keywordGroups.add(keywordGroup);
		
		//초기화
		keywordGroup = new LinkedHashMap<String, Object>();
		keywords = new ArrayList<String>();
		
		
		keywords.add("DJango");
		keywords.add("디장고");
		
		keywordGroup.put("groupName","파이썬");
		keywordGroup.put("keywords",keywords);
		
		keywordGroups.add(keywordGroup);
		
		body.put("startDate", "2020-01-01");
		body.put("endDate", "2021-08-30");
		body.put("timeUnit", "month");
		body.put("keywordGroups", keywordGroups);
		
		String[] ages = {"3","4","5","6"};
		body.put("ages", ages);
		
		String jsonDatas = gson.toJson(body);
		System.out.println(jsonDatas);
		
		HttpConnector conn = new HttpConnector();
		Map<String,String> headers = new HashMap<String, String>();
		headers.put("X-Naver-Client-Id", "wsMhNO6MzI08n5rHAfvg");
		headers.put("X-Naver-Client-Secret", "qkq2ZttrzH");
		headers.put("Content-Type", "application/json; charset=UTF-8");
		
		String responseBody = conn.post("https://openapi.naver.com/v1/datalab/search", headers, jsonDatas);
		System.out.println(responseBody);
	}

	//@Test
	private void testKakaoAPI() {
		HttpConnector conn = new HttpConnector();
		
		Map<String,String> params = new HashMap<String, String>();
		params.put("query", "자바");
		String queryString = conn.urlEncodedForm(params);
		
		String url = "https://dapi.kakao.com/v3/search/book?"+queryString;
		Map<String,String> headers = new HashMap<String,String>();
		headers.put("Authorization", "KakaoAK 02235aa3e05dc625e3390ddd831ad7de");
		
				
		/*
		 * json Object => Map 
		 * json Array => ArrayList
		 * json String => String
		 * json Number => double
		 * json null => null
		 * json boolean => boolean
		 * 
		 */		
		

		//JSON 파싱 라이브러리(GSON) 사용 -> 우리가 원하는 데이터로 가공

		//	1. Map-casting 방식
//		Map<String,Object> datas = gson.fromJson(response, Map.class);
//		List<Map<String,Object>> documents = (List<Map<String, Object>>) datas.get("documents");
//		
//		for (Map<String, Object> doc : documents) {
//			for (String key : doc.keySet()) {
//				System.out.println(key + " : " + doc.get(key));
//			}
//			
//			System.out.println("===================================");
//		}
		
		
		
		//String response = conn.get(url, headers);
		//Gson gson = new Gson(); // Or use new GsonBuilder().create();
		

		//	2. JsonElement 방식
		JsonObject datas = conn.getAsJson(url, headers).getAsJsonObject();
		//System.out.println(datas);
		//datas.get("documents").getAsJsonArray();	//jsonElement객체를 jsonArray로 받아
		
		for (JsonElement e : datas.getAsJsonArray("documents")) {
			//System.out.println(e);
			String author = e.getAsJsonObject().get("authors").getAsString();
			String title = e.getAsJsonObject().get("title").getAsString();
			
			System.out.println("작가 : " + author);
			System.out.println("제목 : " + title);
			
		}
	}


	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}

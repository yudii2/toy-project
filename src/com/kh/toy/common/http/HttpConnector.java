package com.kh.toy.common.http;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.kh.toy.common.code.ErrorCode;
import com.kh.toy.common.exception.HandleableException;

public class HttpConnector {
	
	private Gson gson = new Gson(); // Or use new GsonBuilder().create();

	public String get(String url) {
		
		String responseBody = "";

		try {
			HttpURLConnection conn = getConnection(url, "GET");
			responseBody = getResponseBody(conn);
			
		}catch (Exception e) {
			throw new HandleableException(ErrorCode.HTTP_CONNECT_ERROR,e);
		}

		return responseBody;
	
	}

	public String get(String url, Map<String,String> headers) {
		
		String responseBody = "";

		try {
			HttpURLConnection conn = getConnection(url, "GET");
			setHeaders(headers, conn);
			responseBody = getResponseBody(conn);
			
		}catch (Exception e) {
			throw new HandleableException(ErrorCode.HTTP_CONNECT_ERROR,e);
		}

		return responseBody;
	
	}

	
	public JsonElement getAsJson(String url, Map<String,String> headers) {
		
		String responseBody = "";
		JsonElement datas = null;

		try {
			HttpURLConnection conn = getConnection(url, "GET");
			setHeaders(headers, conn);
			responseBody = getResponseBody(conn);
			datas = gson.fromJson(responseBody, JsonElement.class);
			
		}catch (Exception e) {
			throw new HandleableException(ErrorCode.HTTP_CONNECT_ERROR,e);
		}

		return datas;
	
	}
	
	//요청을 보낼 때 outputStream으로 데이터를 써주도록 처리
	public String post(String url, Map<String,String> headers, String body) {
		
		String responseBody = "";
		
		try {
			HttpURLConnection conn = getConnection(url, "POST");
			setHeaders(headers, conn);
			setBody(body, conn);
			responseBody = getResponseBody(conn);
			
		} catch (IOException e) {
			throw new HandleableException(ErrorCode.HTTP_CONNECT_ERROR,e);
		}
		
		return responseBody;
	}
	
	public JsonElement postAsJson(String url, Map<String,String> headers) {
		
		String responseBody = "";
		JsonElement datas = null;

		try {
			HttpURLConnection conn = getConnection(url, "GET");
			setHeaders(headers, conn);
			responseBody = getResponseBody(conn);
			datas = gson.fromJson(responseBody, JsonElement.class);
			
		}catch (Exception e) {
			throw new HandleableException(ErrorCode.HTTP_CONNECT_ERROR,e);
		}

		return datas;
	
	}
	
	
	private HttpURLConnection getConnection(String url, String method) throws IOException {
		URL u = new URL(url);
		HttpURLConnection conn = (HttpURLConnection) u.openConnection();
		conn.setRequestMethod(method);
		
		//POST방식일 경우 HttpURLConnection의 출력스트림 사용여부를 true로 지정
		if(method.equals("POST")) {
			conn.setDoOutput(true);
		}
		
		
		conn.setConnectTimeout(5000);
		conn.setReadTimeout(5000);
		return conn;
	
	}
	
	private String getResponseBody(HttpURLConnection conn) throws IOException {
		
		StringBuffer responseBody = new StringBuffer();
		
		try(BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));){
			String line = null;
			
			//readLine : 문자열 결합이 덜함
			while((line = br.readLine()) != null) {
				responseBody.append(line);
			}
			
		}
		return responseBody.toString();
	}
	
	private void setHeaders(Map<String,String> headers, HttpURLConnection conn) {
		
		for (String key : headers.keySet()) {
			conn.setRequestProperty(key, headers.get(key));
		}
	}
	
	private void setBody(String body, HttpURLConnection conn) throws IOException {
		
		try(BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()));){
			bw.write(body);
			bw.flush();
		}
	}
	
	
	//인코딩 formatting 메서드
	public String urlEncodedForm(Map<String,String> params) {
		String res = "";
		

		try {
			for (String key : params.keySet()) {
				res += "&" + URLEncoder.encode(key, "UTF-8") + "=" + URLEncoder.encode(params.get(key), "UTF-8");
			}
			
			if(res.length() > 0) {
				res = res.substring(1);
			}
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return res;
		
	}
}


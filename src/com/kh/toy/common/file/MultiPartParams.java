package com.kh.toy.common.file;

import java.util.List;
import java.util.Map;



public class MultiPartParams {

	private Map<String, List> params;
	

	
	public MultiPartParams(Map<String, List> params) {
		this.params = params;
	}
	
	public String getParameter(String paramName) {
//		String param = "";
//		Part part = null;
//		ParamPart paramPart = (ParamPart) part;
//		paramPart.getName();
//		return param;
		if(paramName.equals("com.kh.toy.files")) {
			throw new RuntimeException("com.kh.toy.files는 사용할 수 없는 파라미터 명입니다.");
		}
		return (String) params.get(paramName).get(0);
	}
	
	public String[] getParameterValues(String paramName) {
		
		if(paramName.equals("com.kh.toy.files")) {
			throw new RuntimeException("com.kh.toy.files는 사용할 수 없는 파라미터 명입니다.");
		}
		
		List<String> res = params.get(paramName);	//Map<String,List>에서 Map.get(key)메서드를 활용해 value값인 List를 받아옴 	
		
		return res.toArray(new String[res.size()]);	//List.toArray메서드를 활용해 Object[]배열 객체로 변환 -> toArray(T[] t) 메서드를 활용해 String배열 객체를 생성하여 매개변수로 전달
	}

	public List<FileDTO> getFilesInfo() {
		
		return params.get("com.kh.toy.files");
	}
	

}

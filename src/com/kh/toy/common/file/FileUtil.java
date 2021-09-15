package com.kh.toy.common.file;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import com.kh.toy.common.code.Config;
import com.kh.toy.common.code.ErrorCode;
import com.kh.toy.common.exception.HandleableException;
import com.oreilly.servlet.multipart.FilePart;
import com.oreilly.servlet.multipart.MultipartParser;
import com.oreilly.servlet.multipart.ParamPart;
import com.oreilly.servlet.multipart.Part;

public class FileUtil {
	
	private static final int MAX_SIZE = 1024*1024*10;
	
	//multipart 요청 도착 
	//	-> multipartParser를 사용해, 파일업로드 + 요청파라미터 저장 + FileDTO 생성

	//요청파라미터와 fileDTO의 각 다른 리턴타입을 담기 위해 return type = Map
	//fileDTO에는 예를들어 hobby라는 파라미터에 [축구,야구,농구..]처럼 여러개의 값이 담길 수 있으므로 List로 설정
	public MultiPartParams fileUpload(HttpServletRequest request){
		
		
		//처음엔 Map으로 뿌릴 거지만 더 재사용성을 높이기 위해 multipartRequest객체를 만들면 좋을 것.
		Map<String, List> res = new HashMap<String, List>();
		List<FileDTO> fileDTOs = new ArrayList<FileDTO>();
		
		
		try {
			MultipartParser parser = new MultipartParser(request,MAX_SIZE);
			parser.setEncoding("UTF-8");
			Part part = null;
			
			while((part = parser.readNextPart()) != null) {


				//input type=file 요소가 존재하면, 사용자가 파일을 첨부하지 않았더라도
				//빈 FilePart 객체가 넘어온다. 단, 파일을 첨부하지 않았기 때문에 getFileName 메서드에서 Null이 반환된다.
				if(part.isFile()) {
					FilePart filePart = (FilePart) part;
					if(filePart.getFileName() != null) {
						//System.out.println("파일이름" + filePart.getFileName());
						//System.out.println("파일경로" + filePart.getFilePath());
						//System.out.println("contentType" + filePart.getContentType());
						//	결론 : 파일경로는 파일이름과 동일값을 출력한다.
						
						//	3. FileDTO 생성
						FileDTO fileDTO = createFileDTO(filePart);

						filePart.writeTo(new File(getSavePath() + fileDTO.getRenameFileName()));	//파일저장
											
						//	4. FileDTO를 fileDTOs에 저장
						fileDTOs.add(fileDTO);
						
					}


				}else {
					ParamPart paramPart = (ParamPart) part;
						//System.out.println("파람이름 : " + paramPart.getName());
						//System.out.println("파람값 : " + paramPart.getStringValue());
						//	결론 : 요청파라미터(title,content)는 리턴해야 하므로 List타입으로 받아와 저장
					setParameterMap(paramPart, res);
				
				}
			}
		
			res.put("com.kh.toy.files", fileDTOs);	//res안에 파라미터와 file정보 모두 저장
			//System.out.println(res);
				
		} catch (IOException e) {
			throw new HandleableException(ErrorCode.FAILED_FILE_UPLOAD_ERROR);
		}
		
		
		
		MultiPartParams params = new MultiPartParams(res);		
		return params;
	}
	
	
	//경로 설정 메서드
	private String getSavePath() {
		
		//	2. 저장경로를 웹어플리케이션 외부로 지정
		//	   저장경로를 "외부경로 + /연/월/일" 형태로 작성
		String subPath = getSubPath();
		String savePath = Config.FILE_UPLOAD_PATH.DESC + subPath;
		
		File dir = new File(savePath);
		if(!dir.exists()) {
			dir.mkdirs();
		}
		
		return savePath;
	}
	
	private String getSubPath() {
		Calendar today = Calendar.getInstance();
		int year = today.get(Calendar.YEAR);
		int month = today.get(Calendar.MONTH) + 1;
		int date = today.get(Calendar.DATE);
		
		return year + "\\" + month + "\\" + date + "\\";

	}
	
	//파일 저장 메서드
	private FileDTO createFileDTO(FilePart filePart) {
		FileDTO fileDTO = new FileDTO();
//		1. UNIQUE한 파일명 생성
		String renameFileName = UUID.randomUUID().toString();
//		2. 파일 경로 생성
		String savePath = getSubPath();
		
		fileDTO.setOriginFileName(filePart.getFileName());
		fileDTO.setRenameFileName(renameFileName);
		fileDTO.setSavePath(savePath);
		return fileDTO;
	}
	
	
	private void setParameterMap(ParamPart paramPart, Map<String, List> res) throws UnsupportedEncodingException {
		//	2. 해당 파라미터명으로 기존 파라미터값이 존재하는 경우
		if(res.containsKey(paramPart.getName())) {
			//getName(key)으로 가져온 value 자리에 paramPart value를 저장
			res.get(paramPart.getName()).add(paramPart.getStringValue());
		}else {
		//	1. 해당 파라미터명으로 처음 파라미터값이 저장되는 경우
			List<String> params = new ArrayList<String>();
			params.add(paramPart.getStringValue());
			res.put(paramPart.getName(), params);
		}	
	}
	
	
	
	
	
	
	
}

package com.estimulo.base.controller;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Enumeration;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.estimulo.common.servlet.ModelAndView;
import com.estimulo.common.servlet.controller.MultiActionController;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.oreilly.servlet.MultipartRequest;
import com.oreilly.servlet.multipart.DefaultFileRenamePolicy;

public class ImgFileController extends MultiActionController {

	// SLF4J logger
	private static Logger logger = LoggerFactory.getLogger(ImgFileController.class);

	// GSON 라이브러리
	private static Gson gson = new GsonBuilder().serializeNulls().create(); // 속성값이 null 인 속성도 JSON 변환

	// 톰캣 서버의 이미지 저장 경로
	private static String serverUploadFolderPath = "ImgServer\\empPhoto\\";

	// 이클립스 워크스페이스의 이미지 저장경로 => 반드시 Full Path 로 지정할 것!
	private static String workspaceUploadFolderPath = "C:\\Users\\leesin-heon\\eclipse-workspace\\SKYBLUE\\WebContent\\ImgServer\\empPhoto\\";

	public ModelAndView imgFileUpload(HttpServletRequest request, HttpServletResponse response) {

		if (logger.isDebugEnabled()) {
			logger.debug("ImgFileController : imgFileupload 시작");
		}

		response.setCharacterEncoding("utf-8");
		response.setContentType("application/json; charset=UTF-8");

		HashMap<String, Object> map = new HashMap<>();
		PrintWriter out = null;

		// 한번에 올릴 수 있는 파일 용량 : 20Mbyte 제한
		int maxSize = 1024 * 1024 * 20;

		// 톰캣에 배포된 프로젝트 경로 => (예) C:/tomcat/webapps/JHlogistics/
		String root = request.getSession().getServletContext().getRealPath("/");

		// 파일 업로드 경로 : C:/tomcat/webapps/JHlogistics/ImgServer\empPhoto/
		String uploadPath = root + serverUploadFolderPath;

		// 인코딩 타입
		String encType = "utf-8";

		MultipartRequest multi = null;

		try {

			out = response.getWriter();

			// request , 파일저장경로 , 최대용량 , 인코딩 타입 , 중복파일명에 대한 기본 정책
			// multi 객체 생성시 바로 파일이 업로드됨

			// 톰캣 프로젝트에 저장
			multi = new MultipartRequest(request, uploadPath, maxSize, encType, new DefaultFileRenamePolicy());

			@SuppressWarnings("rawtypes")
			Enumeration files = multi.getFileNames(); // 전송한 전체 파일 이름들을 가져옴

			while (files.hasMoreElements()) {

				// 폼에서 file 태그의 이름
				String name = (String) files.nextElement();

				// 기존 업로드 폴더에 똑같은 파일이 있으면 현재 올리는 파일 이름은 바뀜 (중복 정책)
				// 파일명이 중복되는 경우, 기존 이름
				String originalName = multi.getOriginalFileName(name);

				// 파일명이 중복될 경우 중복 정책에 의해 뒤에 1,2,3 처럼 붙어 unique하게 파일명을 생성
				// 이때 생성된 이름이 storedFileName
				String storedFileName = multi.getFilesystemName(name);

				// 전송된 파일의 타입 : MIME 타입 ( 기계어, image, HTML, text , ... )
				String fileType = multi.getContentType(name);

				// name 에 해당하는 실제 파일을 가져옴
				File file = multi.getFile(name);

				if (file != null) {

					// 그 파일 객체의 크기
					long fileSize = file.length();

					System.out.println("< 파일 업로드 결과 >");
					System.out.println("   @ 파일 태그 파라미터 이름 : " + name);
					System.out.println("   @ 원본 파일이름 : " + originalName);
					System.out.println("   @ 실제 저장 이름 : " + storedFileName);
					System.out.println("   @ 파일 타입 : " + fileType);
					System.out.println("   @ 크기 : " + fileSize + " byte");

					Path from = Paths.get(uploadPath + storedFileName);
					System.out.println("   @ 서버 저장경로 : " + from.toString());

					Path to = Paths.get(workspaceUploadFolderPath + storedFileName);

					// 톰캣 서버에 업로드된 파일을 워크스페이스의 업로드 폴더에 복사
					Files.copy(from, to, StandardCopyOption.REPLACE_EXISTING);
					System.out.println("   @ 워크스페이스 저장경로 : " + to.toString());

					map.put("ImgUrl", "/" + serverUploadFolderPath + storedFileName);
					map.put("errorCode", 1);
					map.put("errorMsg", "성공");

				}

			}

		} catch (IOException e1) {
			e1.printStackTrace();
			map.put("errorCode", -1);
			map.put("errorMsg", e1.getMessage());

		} catch (Exception e2) {
			e2.printStackTrace();
			map.put("errorCode", -2);
			map.put("errorMsg", e2.getMessage());
		} finally {
			out.println(gson.toJson(map));
			out.close();
		}

		if (logger.isDebugEnabled()) {
			logger.debug("ImgFileController : imgFileupload 끝");
		}

		return null;
	}
}

package com.estimulo.common.servlet.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.estimulo.common.servlet.ModelAndView;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class ApiExplorer extends AbstractController {

	@Override
	public ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response) {
		HashMap<String, Object> map = new HashMap<>();
        BufferedReader br = null;
        PrintWriter out = null;
        String result=null;
				// gson 라이브러리
				Gson gson = new GsonBuilder().serializeNulls().create(); // 속성값이 null 인 속성도 json 변환
				  try{            
			            out = response.getWriter();
			            String urlstr = "http://apis.data.go.kr/B552061/roadDgdgrHighway/getRestRoadDgdgrHighway?ServiceKey=FxgeH5N%2FHDozcPfFC8zdG9fVrYGzmQn04x3YJlR28B8%2F7xILof5wxSq%2FwdBrHjWRbaCcQuxcMIWOSctuw3dpMw%3D%3D&frwyNm=%EA%B2%BD%EB%B6%80%EA%B3%A0%EC%86%8D%EB%8F%84%EB%A1%9C&frwySctnNm=%ED%8C%90%EA%B5%90IC-%EC%8B%A0%EA%B0%88JC&vhctyCd=01&type=json&numOfRows=10&pageNo=1";
			            		
			            		
			            		
			            URL url = new URL(urlstr);
			            System.out.println("url = "+url);
			            HttpURLConnection urlconnection = (HttpURLConnection) url.openConnection();
			            urlconnection.setRequestMethod("GET");
			            br = new BufferedReader(new InputStreamReader(urlconnection.getInputStream(),"UTF-8"));
			             result ="";
			            String line;
			            while((line=br.readLine()) != null) {
			                result = result+line;
			            }
			            map.put("gridRowJson", result);
			            map.put("error_code", 0);	
			        	map.put("error_msg", "성공");
System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@"+result);
			            out.println(gson.toJson(map));
		}catch (UnsupportedEncodingException e) {
	map.put("error-code", -1);	
	map.put("error-msg", "내부서버오류");
			e.printStackTrace();
		}catch(IOException e) {
		map.put("error-code", -1);	
		map.put("error-msg", "내부서버오류");	
		e.printStackTrace();
		}catch(Exception e) {
			map.put("error-code", -1);	
			map.put("error-msg", "내부서버오류");	
			e.printStackTrace();

		}finally { 
			map.put("error-code", 0);	
			map.put("error-msg", "성공");
            
		}
		
		return null;
	}

}

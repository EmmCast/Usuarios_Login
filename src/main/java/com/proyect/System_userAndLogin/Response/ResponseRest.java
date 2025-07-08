package com.proyect.System_userAndLogin.Response;

import java.util.ArrayList;
import java.util.HashMap;


public class ResponseRest {

	private ArrayList<HashMap<String, String>> metdata = new ArrayList<>();
	
	public ArrayList<HashMap<String, String>> getMetdata() {
		return metdata;
	}

	public void setMetdata(String type,String code, String date) {
		
		HashMap<String, String> map = new HashMap<String, String>();
		
		map.put("type", type);
		map.put("code", code);
		map.put("date", date);
		
		metdata.add(map);
	}

}

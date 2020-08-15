package com.github.miSkYle.mcbbsshearcher.utils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.LinkedList;

import org.junit.Test;

public class HttpTools {
	@Test
	public void a(){
		System.out.println(getWebData("http://www.mcbbs.net/forum.php?mod=forumdisplay&fid=44&filter=sortall&sortall=1&page=1"));
	}

	public static LinkedList<String> getWebData(String urlString){
		if(!(urlString.contains("http://") || urlString.contains("https://")))urlString = "https://"+urlString;
		if(urlString.contains("http://"))urlString = urlString.replace("http://", "https://");
		System.out.println(urlString);
		LinkedList<String> datas = new LinkedList<>();
		URL url = null;
		HttpURLConnection urlCon = null;
		BufferedReader br = null;
		
		try{
			url = new URL(urlString);
			urlCon = (HttpURLConnection) url.openConnection();
			urlCon.setDoInput(true);  
			urlCon.setRequestMethod("GET");  
			urlCon.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
			br = new BufferedReader(new InputStreamReader(urlCon.getInputStream(), "utf-8"));
			String data = br.readLine();
			while(data!=null){
				datas.add(data);
				data = br.readLine();
			}
			br.close();
			urlCon.disconnect();
		}catch (Exception e) {
			e.printStackTrace();
		}
		return datas;
	}
	
	
	public static String getWebDataString(String urlString){
		if(!(urlString.contains("http://") || urlString.contains("https://")))urlString = "https://"+urlString;
		if(urlString.contains("http://"))urlString = urlString.replace("http://", "https://");
		String datas = "";
		URL url = null;
		HttpURLConnection urlCon = null;
		BufferedReader br = null;
		
		try{
			url = new URL(urlString);
			urlCon = (HttpURLConnection) url.openConnection();
			urlCon.setDoInput(true);  
			urlCon.setRequestMethod("GET");  
			urlCon.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
			br = new BufferedReader(new InputStreamReader(urlCon.getInputStream(), "utf-8"));
			String data = br.readLine();
			while(data!=null){
				datas += data;
				data = br.readLine();
			}
			br.close();
			urlCon.disconnect();
		}catch (Exception e) {
			
		}
		return datas;
	}
}

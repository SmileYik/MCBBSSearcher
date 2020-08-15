package com.github.miSkYle.mcbbsshearcher.data;

import java.util.LinkedList;

import com.github.miSkYle.mcbbsshearcher.utils.HttpTools;

public class TopForum {
	public static String indexSplit = "&index";
	private String name;
	private String urlString;
	private LinkedList<SubForum> subs = new LinkedList<>();
	
	public TopForum(String name, String urlString) {
		this.name = name;
		this.urlString = urlString;
		
		LinkedList<String> datas = HttpTools.getWebData(urlString);
		for(String line : datas){
			if(line.contains("<h2><a href=\"")){
				String temp = line.replace("<h2><a href=\"", "");
				String url = "http://www.mcbbs.net/"+temp.split("1")[0]+indexSplit+".html";
				String name2 = temp.split(">")[1].split("<")[0];
				//if(url.contains("分区版主"))continue;
				if(!url.contains("http://www.mcbbs.net/forum-"))continue;
				subs.add(new SubForum(name2, url));
			}
			continue;
		}
	}
	
	public TopForum(String name, String urlString, LinkedList<SubForum> subs) {
		super();
		this.name = name; 
		this.urlString = urlString;
		this.subs = subs;
	}

	public static LinkedList<TopForum> getTopForums(){
		LinkedList<TopForum> topForums = new LinkedList<>();
		LinkedList<String> datas = HttpTools.getWebData("http://www.mcbbs.net/forum.php");
		for(String line : datas){
			if(line.contains("forum.php?gid=")){
				String temp1 = line.split("gid=")[1];
				String id = temp1.split("\"")[0];
				String temp2 = temp1.split("\">")[1];
				String name = temp2.split("<")[0];
				topForums.add(new TopForum(name.replace("amp;", ""), "http://www.mcbbs.net/forum.php?gid="+id));
			}
			continue;
		}
		return topForums;
	}
	
	public SubForum getSubForum(String name){
		if(name!=null)
			for(SubForum sub : subs)
				if(sub.getName().equals(name))return sub;
		return null;
	}

	public String getName() {
		return name;
	}

	public String getUrlString() {
		return urlString;
	}

	public LinkedList<SubForum> getSubs() {
		return subs;
	}
	
	
	
}

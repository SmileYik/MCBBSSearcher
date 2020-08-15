package com.github.miSkYle.mcbbsshearcher.data;

import java.util.LinkedList;

import com.github.miSkYle.mcbbsshearcher.Data;
import com.github.miSkYle.mcbbsshearcher.McbbsShearcher;
import com.github.miSkYle.mcbbsshearcher.utils.HttpTools;

public class SubForum {
	//http://www.mcbbs.net/forum.php?mod=forumdisplay&fid=138&page=20
	//public static String fidUrl = "http://www.mcbbs.net/forum.php?mod=forumdisplay&fid=%fid%&page="+indexSplit;
	public static String indexSplit = "&index";
	
	private String fid;
	private String name;
	private String urlString;
	private LinkedList<Page> pages = new LinkedList<>();
	
	
	public SubForum(String fid, String name, String urlString, int size) {
		super();
		this.fid = fid;
		this.name = name;
		this.urlString = urlString;
		this.size = size;
	}
	private int size;
	public SubForum(String name, String urlString) {
		super();
		this.name = name;
		
		for(String line : HttpTools.getWebData(urlString.replace(indexSplit,"1"))){
			if(line.contains("srhfid")){
				this.fid = line.replaceAll("[^0-9]", "");
				urlString = this.urlString =  "http://www.mcbbs.net/forum.php?mod=forumdisplay&fid="+fid+"&filter=sortall&sortall=1&page="+indexSplit;
				break;
			}
		}
		
		try {
			String temp = HttpTools.getWebDataString(this.urlString).split("class=\"last\">... ")[1].split("</a>")[0];
			this.size = Integer.parseInt(temp.replaceAll("[^0-9]", ""));			
		} catch (Exception e) {
			size = -1;
		}
	}
	
	public void updatePages(){
		for(int i=1;i<=size;i++){
			pages.add(Page.createPage(urlString, i));				
			if(McbbsShearcher.isStopUpdate())break;
		}
		if(Data.saveUpdate)
			try {
				Forum.saveAllData();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
				McbbsShearcher.fourmItemSizeInfo.setText("Save Successed!");
	}
	
	public String getName() {
		return name;
	}
	public String getUrlString() {
		return urlString;
	}
	public int getSize() {
		return size;
	}
	public String getFid() {
		return fid;
	}
	public LinkedList<Page> getPages() {
		return pages;
	}
}

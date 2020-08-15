package com.github.miSkYle.mcbbsshearcher.data;

import java.util.LinkedList;

import com.github.miSkYle.mcbbsshearcher.McbbsShearcher;
import com.github.miSkYle.mcbbsshearcher.utils.HttpTools;

public class Page {
	private int page;
	private LinkedList<Item> item = new LinkedList<>();
	
	public Page(int page, LinkedList<Item> item) {
		super();
		this.page = page;
		this.item = item;
	}

	/*
	 * public static Page createPage(String urlString,int page){ urlString =
	 * urlString.replace(SubForum.indexSplit, page+""); LinkedList<Item> item = new
	 * LinkedList<>(); int index = 0; for(String line :
	 * HttpTools.getWebData(urlString)){
	 * if(line.contains("atarget(this)")&&line.contains("</em> <a href=\"")){ String
	 * temp = line.split("</em> <a href=\"")[1]; String title =
	 * temp.split(">")[1].split("<")[0].replace("amp;", ""); String url =
	 * temp.split("\"")[0]; item.add(new Item(index++, title.replace("amp;", ""),
	 * "https://www.mcbbs.net/"+url.replace("amp;", "")));
	 * McbbsShearcher.changeProgressBar(1); if(McbbsShearcher.isStopUpdate())return
	 * new Page(page, item); } } return new Page(page, item); }
	 */
	
	public static Page createPage(String urlString,int page){
		urlString = urlString.replace(SubForum.indexSplit, page+"");
		LinkedList<Item> items = new LinkedList<>();
		
		LinkedList<String> lines = HttpTools.getWebData(urlString);
		
		int itemIndex = 0;
		int index = 0;
		int start = -1;
		for(String line : lines) {
			if(line.contains("<tbody id=\"normalthread")) {
				start = index;
			}else if(line.contains("</tbody>")&&start!=-1) {
				
				Item item = new Item();
				
				for(int i = start;i<=index;i++) {
					String subLine = lines.get(i);
					if(subLine.contains("atarget(this)")&&subLine.contains("</em> <a href=\"")){
						String temp = subLine.split("</em> <a href=\"")[1];
						String title = temp.split(">")[1].split("<")[0].replace("amp;", "");
						String url = "https://www.mcbbs.net/"+temp.split("\"")[0];
						item.setIndex(itemIndex++);
						item.setTitle(title);
						item.setUrlString(url);
					}else if(subLine.contains("#lastpost")&&subLine.contains("</em>")) {//reply time
						String preLine = lines.get(i-1);
						String replyer;
						String replyTime;
						if(subLine.contains("><span title=")) {
							replyTime = subLine.split("><span title=\"")[1].split("\"")[0];
						}else {
							replyTime = subLine.split("\">")[1].split("<")[0];
						}
						if(preLine.contains("匿名")) {
							replyer = "匿名";
						}else {
							replyer = preLine.split("\">")[1].split("<")[0];
						}
						item.setReplayer(replyer);
						item.setReplyTime(replyTime);
					}else if(subLine.contains("uid=")) {//auther
						String auther;
						String nextLine = lines.get(i+1);
						String postTime;
						if(subLine.contains("匿名")) {
							auther = "匿名";
						}else {
							auther = subLine.split("\">")[1].split("<")[0];
						}
						if(nextLine.contains("><span title=")) {
							postTime = nextLine.split("><span title=\"")[1].split("\"")[0];
						}else {
							postTime = nextLine.split("<em><span>")[1].split("<")[0];
						}
						item.setAuther(auther);
						item.setPostTime(postTime);
					}else if(subLine.contains("</em></td>")) {//view/reply
						long reply = Long.parseLong(subLine.split("\"xi2\">")[1].split("<")[0]);
						long view = Long.parseLong(subLine.split("<em>")[1].split("<")[0]);
						item.setReply(reply);
						item.setView(view);
					}
				}
				items.add(item);
				System.out.println(item);
				McbbsShearcher.changeProgressBar(1);
				if(McbbsShearcher.isStopUpdate())return new Page(page, items);
				start = -1;
			}
			index++;
		}
		return new Page(page, items);
	}

	public int getPage() {
		return page;
	}

	public LinkedList<Item> getItem() {
		return item;
	}
	
	
}

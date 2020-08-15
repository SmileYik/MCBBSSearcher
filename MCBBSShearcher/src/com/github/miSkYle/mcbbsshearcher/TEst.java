package com.github.miSkYle.mcbbsshearcher;

import java.util.LinkedList;

import org.junit.Test;

import com.github.miSkYle.mcbbsshearcher.data.Item;
import com.github.miSkYle.mcbbsshearcher.utils.HttpTools;

public class TEst {
	@Test
	public void test(){
		/*
		 * String dateString = "2019-12-4 9:07"; String dateFormat = "yyyy-MM-dd HH:mm";
		 * SimpleDateFormat format = new SimpleDateFormat(dateFormat); long time1 =
		 * format.parse(dateString).getTime(); Date time2 = new Date(time1);
		 * System.out.println(time2.toString());
		 */
		a();
		
		//System.out.println(HttpTools.getWebDataString("https://www.mcbbs.net/forum-development-1.html"));
		
	}
	
	public void a() {
		LinkedList<String> lines = HttpTools.getWebData("https://www.mcbbs.net/forum-development-1.html");
		System.out.println(lines);
		
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
						item.setIndex(index);
						item.setTitle(title);
						item.setUrlString(url);
					}else if(subLine.contains("#lastpost")&&subLine.contains("</em>")) {//reply time
						String preLine = lines.get(i-1);
						String replyer = preLine.split("\">")[1].split("<")[0];
						String replyTime;
						if(subLine.contains("><span title=")) {
							replyTime = subLine.split("><span title=\"")[1].split("\"")[0];
						}else {
							replyTime = subLine.split("\">")[1].split("<")[0];
						}
						item.setReplayer(replyer);
						item.setReplyTime(replyTime);
					}else if(subLine.contains("uid=")) {//auther
						String auther = subLine.split("\">")[1].split("<")[0];
						String nextLine = lines.get(i+1);
						String postTime;
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
				System.out.println(item.toString());
				start = -1;
			}
			index++;
		}
	}
}

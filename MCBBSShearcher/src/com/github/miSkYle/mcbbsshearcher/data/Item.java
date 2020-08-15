package com.github.miSkYle.mcbbsshearcher.data;

import java.text.ParseException;
import java.text.SimpleDateFormat;

public class Item {
	private int index;
	private String title;
	private String urlString;
	private long postTime;
	private String auther;
	private long replyTime;
	private String replayer;
	private long view;
	private long reply;
	
	public static final String postTimeFormat = "yyyy-MM-dd";
	public static final String replyTimeFormat = "yyyy-MM-dd HH:mm";
	
	public Item() {
		
	}
	
	public Item(int index, String title, String urlString) {
		super();
		this.index = index;
		this.title = title;
		this.urlString = urlString;
	}
	
	public Item(int index, String title, String urlString, long postTime, String auther, long replyTime,
			String replayer, long view, long reply) {
		super();
		this.index = index;
		this.title = title;
		this.urlString = urlString;
		this.postTime = postTime;
		this.auther = auther;
		this.replyTime = replyTime;
		this.replayer = replayer;
		this.view = view;
		this.reply = reply;
	}
	
	public Item(int index, String title, String urlString, String postTime, String auther, String replyTime,
			String replayer, long view, long reply) {
		super();
		this.index = index;
		this.title = title;
		this.urlString = urlString;
		this.auther = auther;
		this.replayer = replayer;
		this.view = view;
		this.reply = reply;
		try {
			this.postTime = new SimpleDateFormat(postTimeFormat).parse(postTime).getTime();
			this.replyTime = new SimpleDateFormat(replyTimeFormat).parse(replyTimeFormat).getTime();
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
	

	@Override
	public String toString() {
		return "Item [index=" + index + ", title=" + title + ", urlString=" + urlString + ", postTime=" + postTime
				+ ", auther=" + auther + ", replyTime=" + replyTime + ", replayer=" + replayer + ", view=" + view
				+ ", reply=" + reply 
				+ "]";
	}

	public int getIndex() {
		return index;
	}
	public String getTitle() {
		if(title == null) return "";
		return title.replace("amp;", "");
	}
	public String getUrlString() {
		if(urlString == null) return "";
		return urlString.replace("amp;", "");
	}

	public long getPostTime() {
		return postTime;
	}
	
	public String getPostTimeString() {
		return new SimpleDateFormat(postTimeFormat).format(postTime);
	}

	public void setPostTime(long postTime) {
		this.postTime = postTime;
	}
	
	public void setPostTime(String postTime) {
		try {
			this.postTime = new SimpleDateFormat(postTimeFormat).parse(postTime).getTime();
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}

	public String getAuther() {
		return auther;
	}

	public void setAuther(String auther) {
		this.auther = auther;
	}

	public long getReplyTime() {
		return replyTime;
	}
	
	public String getReplyTimeString() {
		return new SimpleDateFormat(replyTimeFormat).format(replyTime);
	}

	public void setReplyTime(long replyTime) {
		this.replyTime = replyTime;
	}
	
	public void setReplyTime(String replyTime) {
		try {
			this.replyTime = new SimpleDateFormat(replyTimeFormat).parse(replyTime).getTime();
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}

	public String getReplayer() {
		return replayer;
	}

	public void setReplayer(String replayer) {
		this.replayer = replayer;
	}

	public long getView() {
		return view;
	}

	public void setView(long view) {
		this.view = view;
	}

	public long getReply() {
		return reply;
	}

	public void setReply(long reply) {
		this.reply = reply;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setUrlString(String urlString) {
		this.urlString = urlString;
	}
}

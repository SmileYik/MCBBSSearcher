package com.github.miSkYle.mcbbsshearcher.data;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.Properties;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JOptionPane;

import org.junit.Test;

import com.github.miSkYle.mcbbsshearcher.Data;

public class Forum {
	
	public static LinkedList<TopForum> topForum = new LinkedList<>();
	public static File dataDir = new File("./McbbsShearcher/");
	public static boolean over = false;
	
	private static TopForum system;
	
	public static void init() throws Exception{
		
		if(!dataDir.exists()){
			dataDir.mkdirs();
			 Timer timer = new Timer();// 实例化Timer类   
		        timer.schedule(new TimerTask() {  
		            public void run() {
		            	topForum = TopForum.getTopForums();
		            	try {
							saveTSForumsData();
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
		        		if(new File(dataDir+"/system.suniform").exists()){
		        			try {
								system = loadTopForumData("system");
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
		        		}else{
		        			LinkedList<SubForum> subForums = new LinkedList<>();
		        			subForums.add(new SubForum("null", "历史记录", "null", 1));
		        			system = new TopForum("System", "null", subForums);
		        		}
		            	JOptionPane.showMessageDialog(null, "正在初始化完成!");
		            	over = true;
		            }  
		        }, 50);
			JOptionPane.showMessageDialog(null, "正在初始化程序...请稍后...");
		}else if(new File(dataDir+"/ForumsDatas.suniform").exists()){
			topForum = loadAllData();	
			if(new File(dataDir+"/system.suniform").exists()){
				system = loadTopForumData("system");
			}else{
				LinkedList<SubForum> subForums = new LinkedList<>();
				subForums.add(new SubForum("null", "历史记录", "null", 1));
				system = new TopForum("System", "null", subForums);
			}
			over = true;
		}else if(new File(dataDir+"/TSForumsData.suniform").exists()){
			topForum = loadTSForumsData();	
			if(new File(dataDir+"/system.suniform").exists()){
				system = loadTopForumData("system");
			}else{
				LinkedList<SubForum> subForums = new LinkedList<>();
				subForums.add(new SubForum("null", "历史记录", "null", 1));
				system = new TopForum("System", "null", subForums);
			}
			over = true;
		}
		
		
	}
	
	public static TopForum getTopForum(String name){
		if(system.getName().equals(name))
			return system;
		else if(name!=null)
			for(TopForum top : topForum)
				if(top.getName().equals(name))return top;
		return null;
	}
	
	@Test
	public void a() throws Exception{
/*		topForum = TopForum.getTopForums();
		try {
			saveTSForumsData();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		topForum = loadAllData();
		//loadTSForumsData();
		for(TopForum topForum : topForum){
			System.out.println(topForum.getName()+": "+topForum.getUrlString());
			for(SubForum sub : topForum.getSubs()){
				System.out.println("  "+sub.getName()+"(size: "+sub.getSize()+"): "+sub.getUrlString());
				for(Page page:sub.getPages())
					for(Item item : page.getItem()){
						System.out.println("    "+item.getTitle()+"): "+item.getUrlString());
					}
			}
		}
		//saveAllData();
	}
	
	@SuppressWarnings("deprecation")
	public static void saveAllData()throws Exception{
		File tSForumsDataFile = new File(dataDir+"/ForumsDatas.suniform");
		if(!tSForumsDataFile.exists())tSForumsDataFile.createNewFile();
		else{
			tSForumsDataFile.delete();
			tSForumsDataFile.createNewFile();
		}
		Properties prop = new Properties();
		prop.load(new BufferedReader(new InputStreamReader(new FileInputStream(tSForumsDataFile),"utf8")));
		String topForum = "";
		for(TopForum top : Forum.topForum){
			String subForum = "";
			topForum+=top.getName()+"\n";
			prop.setProperty(top.getName()+".name", top.getName());
			prop.setProperty(top.getName()+".url", top.getUrlString());
			for(SubForum sub : top.getSubs()){
				subForum+=sub.getName()+"\n";
				if(sub.getName()!=null)prop.setProperty(top.getName()+"."+sub.getName()+".name", sub.getName());
				if(sub.getUrlString()!=null)prop.setProperty(top.getName()+"."+sub.getName()+".url", sub.getUrlString());
				prop.setProperty(top.getName()+"."+sub.getName()+".size", sub.getSize()+"");
				if(sub.getFid()!=null)prop.setProperty(top.getName()+"."+sub.getName()+".fid", sub.getFid());
				for(Page page : sub.getPages()){
					for(Item item : page.getItem()){
						String key = top.getName()+"."+sub.getName()+".page."+page.getPage()+".item."+ item.getIndex();
						if(item.getTitle()!=null)prop.setProperty(key+".title", item.getTitle());
						if(item.getUrlString()!=null)prop.setProperty(key+".url", item.getUrlString());
						if(item.getAuther()!=null)prop.setProperty(key+".auther", item.getAuther());
						prop.setProperty(key+".postTime", item.getPostTime()+"");
						if(item.getReplayer()!=null)prop.setProperty(key+".replyer", item.getReplayer());
						prop.setProperty(key+".replyTime", item.getReplyTime()+"");
						prop.setProperty(key+".view", item.getView()+"");
						prop.setProperty(key+".reply", item.getReply()+"");
					}
				}
			}
			prop.setProperty(top.getName()+".sub-forum", subForum.substring(0, subForum.length()-1));
		}
		
		{
			try {
				saveTopForumData(system, "system");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		
		prop.setProperty("top-forum", topForum.substring(0, topForum.length()-1));
		
		prop.setProperty("save1", Data.saveUpdate?"true":"false");
		prop.setProperty("save2", Data.saveLike?"true":"false");
		prop.setProperty("save3", Data.saveTick?"true":"false");
		prop.setProperty("save3.1", Data.saveTickTime+"");
		
		prop.save(new FileOutputStream(tSForumsDataFile), "utf8");
	}
	
	
	@SuppressWarnings("deprecation")
	public static void saveTSForumsData() throws Exception{
		File tSForumsDataFile = new File(dataDir+"/TSForumsData.suniform");
		if(!tSForumsDataFile.exists())tSForumsDataFile.createNewFile();
		else{
			tSForumsDataFile.delete();
			tSForumsDataFile.createNewFile();
		}
		Properties prop = new Properties();
		prop.load(new BufferedReader(new InputStreamReader(new FileInputStream(tSForumsDataFile),"utf8")));
		String topForum = "";
		for(TopForum top : Forum.topForum){
			String subForum = "";
			topForum+=top.getName()+"\n";
			prop.setProperty(top.getName()+".name", top.getName());
			prop.setProperty(top.getName()+".url", top.getUrlString());
			for(SubForum sub : top.getSubs()){
				subForum+=sub.getName()+"\n";
				if(sub.getName()!=null)prop.setProperty(top.getName()+"."+sub.getName()+".name", sub.getName());
				if(sub.getUrlString()!=null)prop.setProperty(top.getName()+"."+sub.getName()+".url", sub.getUrlString());
				prop.setProperty(top.getName()+"."+sub.getName()+".size", sub.getSize()+"");
				if(sub.getFid()!=null)prop.setProperty(top.getName()+"."+sub.getName()+".fid", sub.getFid());
			}
			prop.setProperty(top.getName()+".sub-forum", subForum.substring(0, subForum.length()-1));
		}
		prop.setProperty("top-forum", topForum.substring(0, topForum.length()-1));
		
		prop.setProperty("save1", Data.saveUpdate?"true":"false");
		prop.setProperty("save2", Data.saveLike?"true":"false");
		prop.setProperty("save3", Data.saveTick?"true":"false");
		prop.setProperty("save3.1", Data.saveTickTime+"");
		
		prop.save(new FileOutputStream(tSForumsDataFile), "utf8");
		//SaveObjectToFile.save(topForum, tSForumsDataFile);
	}
	
	public static LinkedList<TopForum> loadTSForumsData() throws Exception{
		LinkedList<TopForum> topForum = new LinkedList<>();
		File tSForumsDataFile = new File(dataDir+"/TSForumsData.suniform");
		if(!tSForumsDataFile.exists())return new LinkedList<>();
		Properties prop = new Properties();
		prop.load(new BufferedReader(new InputStreamReader(new FileInputStream(tSForumsDataFile),"utf8")));
		for(String top : prop.getProperty("top-forum").split("\n")){
			LinkedList<SubForum> subForums = new LinkedList<>();
			for(String sub : prop.getProperty(top+".sub-forum").split("\n")){
				subForums.add(new SubForum(prop.getProperty(top+"."+sub+"."+"fid"), 
							prop.getProperty(top+"."+sub+".name"), 
							prop.getProperty(top+"."+sub+".url"), 
							Integer.parseInt(prop.getProperty(top+"."+sub+".size"))));
			}
			topForum.add(new TopForum(prop.getProperty(top+".name"), prop.getProperty(top+".url"), subForums));
		}
		
		Data.saveUpdate = Boolean.parseBoolean(prop.getProperty("save1","false"));
		Data.saveLike = Boolean.parseBoolean(prop.getProperty("save2","false"));
		Data.saveTick = Boolean.parseBoolean(prop.getProperty("save3","false"));
		Data.saveTickTime = Integer.parseInt(prop.getProperty("save3.1","5"));
		
		return topForum;
	}
	
	public static LinkedList<TopForum> loadAllData() throws Exception{
		LinkedList<TopForum> topForum = new LinkedList<>();
		File tSForumsDataFile = new File(dataDir+"/ForumsDatas.suniform");
		if(!tSForumsDataFile.exists())return new LinkedList<>();
		Properties prop = new Properties();
		prop.load(new BufferedReader(new InputStreamReader(new FileInputStream(tSForumsDataFile),"utf8")));
		for(String top : prop.getProperty("top-forum").split("\n")){
			LinkedList<SubForum> subForums = new LinkedList<>();
			for(String sub : prop.getProperty(top+".sub-forum").split("\n")){
				SubForum subF = new SubForum(prop.getProperty(top+"."+sub+"."+"fid"), 
						prop.getProperty(top+"."+sub+".name"), 
						prop.getProperty(top+"."+sub+".url"), 
						Integer.parseInt(prop.getProperty(top+"."+sub+".size"))); 
				subForums.add(subF);
				for(int i = 1;i<=subF.getSize();i++){
					int j = 0;
					String key = top+"."+sub+".page."+i+".item."+ j;
					LinkedList<Item> items = new LinkedList<>();
					while(prop.getProperty(key+".title", null)!=null){
						Item item = new Item(j++, prop.getProperty(key+".title"), prop.getProperty(key+".url"));
						item.setAuther(prop.getProperty(key+".auther"));
						item.setReplayer(prop.getProperty(key+".replyer"));
						try {
							item.setPostTime(Long.parseLong(prop.getProperty(key+".postTime")));
							item.setReplyTime(Long.parseLong(prop.getProperty(key+".replyTime")));
							item.setView(Long.parseLong(prop.getProperty(key+".view")));
							item.setReply(Long.parseLong(prop.getProperty(key+".reply")));							
						}catch (Exception e) {
							
						}
						items.add(item);
						key = top+"."+sub+".page."+i+".item."+ j;
					}
					subF.getPages().add(new Page(i, items));
				}
			}
			topForum.add(new TopForum(prop.getProperty(top+".name"), prop.getProperty(top+".url"), subForums));
		}
		
		Data.saveUpdate = Boolean.parseBoolean(prop.getProperty("save1","false"));
		Data.saveLike = Boolean.parseBoolean(prop.getProperty("save2","false"));
		Data.saveTick = Boolean.parseBoolean(prop.getProperty("save3","false"));
		Data.saveTickTime = Integer.parseInt(prop.getProperty("save3.1","5"));
		
		system = loadTopForumData("system");
		return topForum;
	}
	
	@SuppressWarnings("deprecation")
	public static void saveTopForumData(TopForum top,String fileName) throws Exception{
		File tSForumsDataFile = new File(dataDir+"/"+fileName+".suniform");
		if(!tSForumsDataFile.exists())tSForumsDataFile.createNewFile();
		else{
			tSForumsDataFile.delete();
			tSForumsDataFile.createNewFile();
		}
		Properties prop = new Properties();
		prop.load(new BufferedReader(new InputStreamReader(new FileInputStream(tSForumsDataFile),"utf8")));
		String topForum = "";
		topForum+=top.getName()+"\n";
		
		String subForum = "";		
			prop.setProperty(top.getName()+".name", top.getName());
			prop.setProperty(top.getName()+".url", top.getUrlString());
			for(SubForum sub : top.getSubs()){
				subForum+=sub.getName()+"\n";
				if(sub.getName()!=null)prop.setProperty(top.getName()+"."+sub.getName()+".name", sub.getName());
				if(sub.getUrlString()!=null)prop.setProperty(top.getName()+"."+sub.getName()+".url", sub.getUrlString());
				prop.setProperty(top.getName()+"."+sub.getName()+".size", sub.getSize()+"");
				if(sub.getFid()!=null)prop.setProperty(top.getName()+"."+sub.getName()+".fid", sub.getFid());
				for(Page page : sub.getPages()){
					for(Item item : page.getItem()){
						String key = top.getName()+"."+sub.getName()+".page."+page.getPage()+".item."+ item.getIndex();
						if(item.getTitle()!=null)prop.setProperty(key+".title", item.getTitle());
						if(item.getUrlString()!=null)prop.setProperty(key+".url", item.getUrlString());
						if(item.getAuther()!=null)prop.setProperty(key+".auther", item.getAuther());
						prop.setProperty(key+".postTime", item.getPostTime()+"");
						if(item.getReplayer()!=null)prop.setProperty(key+".replyer", item.getReplayer());
						prop.setProperty(key+".replyTime", item.getReplyTime()+"");
						prop.setProperty(key+".view", item.getView()+"");
						prop.setProperty(key+".reply", item.getReply()+"");
					}
				}
			}
			prop.setProperty(top.getName()+".sub-forum", subForum.substring(0, subForum.length()-1));

		prop.setProperty("top-forum", topForum.substring(0, topForum.length()-1));
		
		prop.setProperty("save1", Data.saveUpdate?"true":"false");
		prop.setProperty("save2", Data.saveLike?"true":"false");
		prop.setProperty("save3", Data.saveTick?"true":"false");
		prop.setProperty("save3.1", Data.saveTickTime+"");
		
		prop.save(new FileOutputStream(tSForumsDataFile), "utf8");
	}
	
	public static TopForum loadTopForumData(String fileName) throws Exception{
		TopForum topForum = null;
		File tSForumsDataFile = new File(dataDir+"/"+fileName+".suniform");
		if(!tSForumsDataFile.exists())return null;
		Properties prop = new Properties();
		prop.load(new BufferedReader(new InputStreamReader(new FileInputStream(tSForumsDataFile),"utf8")));
		for(String top : prop.getProperty("top-forum").split("\n")){
			LinkedList<SubForum> subForums = new LinkedList<>();
			for(String sub : prop.getProperty(top+".sub-forum").split("\n")){
				SubForum subF = new SubForum(prop.getProperty(top+"."+sub+"."+"fid"), 
						prop.getProperty(top+"."+sub+".name"), 
						prop.getProperty(top+"."+sub+".url"), 
						Integer.parseInt(prop.getProperty(top+"."+sub+".size"))); 
				subForums.add(subF);
				for(int i = 1;i<=subF.getSize();i++){
					int j = 0;
					String key = top+"."+sub+".page."+i+".item."+ j;
					//System.out.println(key);
					LinkedList<Item> items = new LinkedList<>();
					while(prop.getProperty(key+".title", null)!=null){
						Item item = new Item(j++, prop.getProperty(key+".title"), prop.getProperty(key+".url"));
						item.setAuther(prop.getProperty(key+".auther"));
						item.setReplayer(prop.getProperty(key+".replyer"));
						try {
							item.setPostTime(Long.parseLong(prop.getProperty(key+".postTime")));
							item.setReplyTime(Long.parseLong(prop.getProperty(key+".replyTime")));
							item.setView(Long.parseLong(prop.getProperty(key+".view")));
							item.setReply(Long.parseLong(prop.getProperty(key+".reply")));
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						items.add(item);
						key = top+"."+sub+".page."+i+".item."+ j;
					}
					subF.getPages().add(new Page(i, items));
				}
			}
			topForum = new TopForum(prop.getProperty(top+".name"), prop.getProperty(top+".url"), subForums);
		}
		
		Data.saveUpdate = Boolean.parseBoolean(prop.getProperty("save1","false"));
		Data.saveLike = Boolean.parseBoolean(prop.getProperty("save2","false"));
		Data.saveTick = Boolean.parseBoolean(prop.getProperty("save3","false"));
		Data.saveTickTime = Integer.parseInt(prop.getProperty("save3.1","5"));
		
		return topForum;
	}
	
	public static TopForum getSystemTopForum(){
		return system;
	}
}

package com.github.miSkYle.mcbbsshearcher.utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.LinkedList;

import org.junit.Test;

import com.github.miSkYle.mcbbsshearcher.data.Forum;

public class Download {
	
	public static void download(String title,String urlString,String savePath) throws IOException{
		if(!new File(Forum.dataDir+"/"+fixPath(savePath)+"/").exists())
			new File(Forum.dataDir+"/"+fixPath(savePath)+"/").mkdirs();
		File out = new File(Forum.dataDir+"/"+fixPath(savePath)+"/"+fixPath(title)+".html");
		if(!out.exists()) out.createNewFile();
		
		Thread thread = new Thread(new Runnable() {
			
			@Override
			public void run() {
				try {
					OutputStreamWriter write = new OutputStreamWriter(new FileOutputStream(out),"utf-8");   
					BufferedWriter bw = new BufferedWriter(write);
					LinkedList<String> lines = HttpTools.getWebData(urlString);
					System.out.println(lines);
					for(String line : lines) {
						bw.write(line);
						bw.newLine();
					}
					bw.flush();
					bw.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
		});

		thread.start();
		
	}
	public static String fixPath(String path){
		return path.replace("\"","")
							.replace("\\", "")
							.replace(";", "")
							.replace("/", "")
							.replace("|","")
							.replace("*", "")
							.replace("<", "")
							.replace(">", "")
							.replace("?", "");
	}
	@Test
	public void a(){
		System.out.println(fixPath("||;?</>\\|\"*a//asfasf/"));
	}
}

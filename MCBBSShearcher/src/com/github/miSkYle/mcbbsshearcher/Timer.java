package com.github.miSkYle.mcbbsshearcher;

import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.util.TimerTask;

import com.github.miSkYle.mcbbsshearcher.data.Forum;

public class Timer{
	private static java.util.Timer timer = new java.util.Timer();
	
	public static void start(int min){
		timer.cancel();
		timer = new java.util.Timer();
		timer.schedule(new TimerTask() {
			
			@Override
			public void run() {
				try {
					Forum.saveAllData();
					McbbsShearcher.fourmItemSizeInfo.setText("Save Successed!");
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
		}, (long)(min*60000));
	}
}

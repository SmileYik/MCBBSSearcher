package com.github.miSkYle.mcbbsshearcher.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class SaveObjectToFile {
	public static <T extends Object> void save(T obj,File path){
		try {
			ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(path));
			oos.writeObject(obj);
			oos.flush();
			oos.close();
		} catch (IOException e) {}
	}
	
	public static <T extends Object> T load(File path){
		try {
			ObjectInputStream ois = new ObjectInputStream(new FileInputStream(path));
			@SuppressWarnings("unchecked")
			T result = (T) ois.readObject();
			ois.close();
			return result;
		} catch (Exception e) {}
		return null;
	}
	
}

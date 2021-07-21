package tk.miskyle.mcbbssearcher.data;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Properties;
import java.util.Timer;
import tk.miskyle.mcbbssearcher.McbbsSearcher;
import tk.miskyle.mcbbssearcher.form.McbbsUserForm;
import tk.miskyle.mcbbssearcher.util.AutoSaveTimer;

public class Setting {
  public static final String SETTING_FILE_PATH = McbbsSearcher.DATA_FLOD + "/setting.properties";
  
  //about mcbbs user cookies
  public static HashMap<String, McbbsUserData> mcbbsUserCookies = new HashMap<>();
  public static McbbsUserData mainUser = null;
  
  //about auto save
  public static boolean enableAutoSave;
  public static boolean enableSaveWhenLike;
  public static boolean enableSaveWhenUpdate;
  public static int saveTime;
  
  //about 403 solution
  public static boolean solution403;
  
  // about font
  public static String fontName;
  public static int fontSize;
  
  // about window h&w
  public static int wh;
  public static int ww;
  
  /**
   * 检查是否第一次运行.
   */
  public static void checker() {
    if (!new File(SETTING_FILE_PATH).exists()) {
      McbbsUserForm.showUsers(true);
      save();
    }
  }
  
  /**
   * 保存所有设定.
   */
  public static void save() {
    Properties pro = new Properties();
    pro.setProperty("save.autoSave", enableAutoSave + "");
    pro.setProperty("save.like", enableSaveWhenLike + "");
    pro.setProperty("save.update", enableSaveWhenUpdate + "");
    pro.setProperty("save.saveTile", saveTime + "");
    pro.setProperty("solution403", solution403 + "");
    pro.setProperty("user.main", mainUser.getUserName());
    pro.setProperty("window.height", wh + "");
    pro.setProperty("window.width", ww + "");
    if (fontName != null) {
      pro.setProperty("font.name", fontName);
      pro.setProperty("font.size", fontSize + "");
    }
    StringBuilder sb = new StringBuilder();
    mcbbsUserCookies.forEach((k, v) -> {
      pro.setProperty("user.list." + k,  v.getCookies());
      sb.append(k + ";");
    });
    pro.setProperty("user.users", sb.toString());
    File file = new File(SETTING_FILE_PATH);
    try {
      pro.store(new FileOutputStream(file), "Setting Store");
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
  
  /**
   * 读取.
   */
  public static void load() {
    Properties pro = new Properties();
    try {
      pro.load(new FileInputStream(new File(SETTING_FILE_PATH)));
    } catch (IOException e) {
      e.printStackTrace();
    }
    
    enableAutoSave = Boolean.parseBoolean(pro.getProperty("save.autoSave"));
    enableSaveWhenLike = Boolean.parseBoolean(pro.getProperty("save.like"));
    enableSaveWhenUpdate = Boolean.parseBoolean(pro.getProperty("save.update"));
    solution403 = Boolean.parseBoolean(pro.getProperty("solution403"));
    saveTime = Integer.parseInt(pro.getProperty("save.saveTile"));
    fontName = pro.getProperty("font.name", null);
    fontSize = Integer.parseInt(pro.getProperty("font.size", "12"));
    wh = Integer.parseInt(pro.getProperty("window.height", "0"));
    ww = Integer.parseInt(pro.getProperty("window.width", "0"));
    for (String key : pro.getProperty("user.users").split(";")) {
      mcbbsUserCookies.put(key, new McbbsUserData(key, pro.getProperty("user.list." + key)));
    }
    mainUser = mcbbsUserCookies.get(pro.getProperty("user.main"));
    
    new Timer().schedule(new AutoSaveTimer(), 60000L, 60000L);
  }
}

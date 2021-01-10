package tk.miskyle.mcbbssearcher.data.favorite;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Properties;
import tk.miskyle.mcbbssearcher.McbbsSearcher;
import tk.miskyle.mcbbssearcher.data.Setting;
import tk.miskyle.mcbbssearcher.forum.data.Item;
import tk.miskyle.mcbbssearcher.util.McbbsConnection;

public class Favorite {
  public static final String FAVORITE_SETTING_PATH = 
      McbbsSearcher.DATA_FLOD + "/favorite.properties";
  public static final String FAVORITE_PATH = McbbsSearcher.DATA_FLOD + "/Favorite";
  private static HashMap<String, HashMap<String, FavoriteItem>> favoriteList = new HashMap<>();
  private static String nowShow;
  
  /**
   * 获取收藏夹目录.
   * @param name 收藏夹名字.
   * @return
   */
  public static File getPath(String name, boolean create) {
    if (!favoriteList.containsKey(name)) {
      return null;
    }
    File path = new File(FAVORITE_PATH, name);
    if (!path.exists() && create) {
      System.out.println(path);
      path.mkdirs();
    }
    return path;
  }
  
  /**
   * 创建一个收藏夹.
   * @param name 收藏夹名字.
   */
  public static void createFavoriteList(String name) {
    if (!favoriteList.containsKey(name)) {
      getPath(name, true);
      favoriteList.put(name, new HashMap<>());
    }
  }
  
  /**
   * 添加一个条目到收藏夹.
   * @param name 收藏夹名字
   * @param attachString 附加内容
   * @param item 条目
   */
  public static void like(String name, String attachString, Item item) {
    if (!favoriteList.containsKey(name)) {
      createFavoriteList(name);
    }
    FavoriteItem faItem = new FavoriteItem();
    faItem.setAttachString(attachString);
    faItem.setItem(item);
    faItem.setSaveTime(new Date().getTime());
    favoriteList.get(name).put(item.getTitle(), faItem);
    File file = new File(getPath(name, true), 
        item.getTitle().replaceAll("[:/\\\":?<>]", "") + "." 
            + new SimpleDateFormat(Item.POST_TIME_FORMAT)
                       .format(new Date().getTime()) + ".html");
    
    new Thread(() -> {
      download(file, item.getUrlString());
    }).start();
  }
  
  /**
   * 下载页面到目录.
   * @param file 目标位置
   * @param url 连接
   */
  public static void download(File file, String url) {
    McbbsConnection connection = McbbsConnection.newGetConnect(url, Setting.mainUser);
    if (connection == null) {
      // 连接失败
      return;
    }
    try (BufferedReader reader = new BufferedReader(
        new InputStreamReader(connection.getConnection().getInputStream(), "UTF-8"));
        BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
      String line = null;
      while ((line = reader.readLine()) != null) {
        writer.write(line);
      }
      writer.flush();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public static HashMap<String, HashMap<String, FavoriteItem>> getFavoriteList() {
    return favoriteList;
  }

  public static void setFavoriteList(HashMap<String, HashMap<String, FavoriteItem>> favoriteList) {
    Favorite.favoriteList = favoriteList;
  }

  public static String getNowShow() {
    return nowShow;
  }

  public static void setNowShow(String nowShow) {
    Favorite.nowShow = nowShow;
  }
  
  /**
   * 获取现在正在浏览的收藏夹列表.
   * @return
   */
  public static HashMap<String, FavoriteItem> getNowShowItems() {
    if (nowShow == null) {
      return new HashMap<>();
    }
    return favoriteList.get(nowShow);
  }
  
  /**
   * 重命名收藏夹.
   * @param oldName 旧名字
   * @param newName 新名字
   */
  public static void rename(String oldName, String newName) {
    HashMap<String, FavoriteItem> items = 
        Favorite.getFavoriteList().remove(oldName);
    Favorite.getFavoriteList().put(newName, items);
    getPath(oldName, true).renameTo(getPath(newName, false));
  }
  
  /**
   * 检查配置文件是否存在, 不存在则初始化.
   */
  public static void checker() {
    if (!new File(FAVORITE_SETTING_PATH).exists()) {
      save();
    }
  }
  
  /**
   * 保存所有收藏夹项目.
   */
  public static void save() {
    Properties pro = new Properties();
    StringBuilder sb = new StringBuilder();
    favoriteList.forEach((k, v) -> {
      sb.append(k).append(";");
      int index = 0;
      for (FavoriteItem i : favoriteList.get(k).values()) {
        String key = k + ".item." + index++ + ".";
        save(pro, i, key);
      }
    });
    pro.setProperty("list", sb.toString());
    
    try {
      pro.store(new FileOutputStream(new File(FAVORITE_SETTING_PATH)), "收藏夹");
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
  
  /**
   * 将制定收藏夹保存至指定文件中.
   * @param file 文件
   * @param name 收藏夹.
   */
  public static void save(File file, String name) {
    Properties pro = new Properties();
    pro.setProperty("name", name);
    
    int index = 0;
    for (FavoriteItem i : favoriteList.get(name).values()) {
      String key = "item." + index++ + ".";
      save(pro, i, key);
    }
    
    try {
      pro.store(new FileOutputStream(file), name);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
  
  /**
   * 从Properties中保存收藏夹条目.
   * @param pro 指定properties实例
   * @param i 要保存条目
   * @param key 当前条目对应key
   */
  private static void save(Properties pro, FavoriteItem i, String key) {
    pro.setProperty(key + "attachString", i.getAttachString());
    pro.setProperty(key + "saveTime", i.getSaveTime() + "");
    Item item = i.getItem();
    if (item.getAuther() != null) {
      pro.setProperty(key + "auther", item.getAuther());        
    }
    if (item.getReplayer() != null) {
      pro.setProperty(key + "replayer", item.getReplayer());        
    }
    if (item.getTid() != null) {
      pro.setProperty(key + "tid", item.getTid());        
    }
    if (item.getTitle() != null) {
      pro.setProperty(key + "title", item.getTitle());        
    }
    if (item.getUrlString() != null) {
      pro.setProperty(key + "url", item.getUrlString());        
    }
    pro.setProperty(key + "digest", item.getDigest() + "");
    pro.setProperty(key + "heatLevel", item.getHeatlevel() + "");
    pro.setProperty(key + "postTime", item.getPostTime() + "");
    pro.setProperty(key + "recommend", item.getRecommend() + "");
    pro.setProperty(key + "reply", item.getReply() + "");
    pro.setProperty(key + "replyTime", item.getReplyTime() + "");
    pro.setProperty(key + "view", item.getView() + "");
    pro.setProperty(key + "agree", item.isAgree() + "");
  }
  
  /**
   * 读取.
   */
  public static void load() {
    File file = new File(FAVORITE_SETTING_PATH);
    Properties pro = new Properties();
    try {
      pro.load(new FileInputStream(file));
    } catch (IOException e) {
      e.printStackTrace();
    }
    
    for (String k : pro.getProperty("list").split(";")) {
      HashMap<String, FavoriteItem> list = new HashMap<>();
      int index = 0;
      String key = k + ".item." + index + ".";
      while (pro.containsKey(key + "url")) {
        FavoriteItem item = load(pro, key);
        list.put(item.getItem().getTitle(), item);
        key = k + ".item." + index++ + ".";
      }
      favoriteList.put(k, list);
    }
  }
  
  /**
   * 从文件取读收藏夹数据.
   * @param file 目标文件.
   */
  public static void load(File file) {
    Properties pro = new Properties();
    try {
      pro.load(new FileInputStream(file));
    } catch (IOException e) {
      e.printStackTrace();
    }
    
    HashMap<String, FavoriteItem> list = new HashMap<>();
    
    String name = pro.getProperty("name");
    if (favoriteList.containsKey(name)) {
      list = favoriteList.get(name);
    }
    
    int index = 0;
    String key = "item." + index + ".";
    while (pro.containsKey(key + "url")) {
      if (list.containsKey(pro.getProperty(key + "title"))) {
        key = "item." + index++ + ".";
        continue;
      }
      FavoriteItem item = load(pro, key);
      list.put(item.getItem().getTitle(), item);
      key = "item." + index++ + ".";
    }
    favoriteList.put(name, list);
  }
  
  /**
   * 从properties中读取收藏夹条目.
   * @param pro 实例
   * @param key key
   * @return
   */
  private static FavoriteItem load(Properties pro, String key) {
    Item item = new Item();
    item.setAgree(Boolean.parseBoolean(pro.getProperty(key + "agree")));
    if (pro.containsKey(key + "auther")) {
      item.setAuther(pro.getProperty(key + "auther"));        
    }
    item.setDigest(Integer.parseInt(pro.getProperty(key + "digest")));
    item.setHeatlevel(Integer.parseInt(pro.getProperty(key + "heatLevel")));
    item.setPostTime(Long.parseLong(pro.getProperty(key + "postTime")));
    item.setReplyTime(Long.parseLong(pro.getProperty(key + "replyTime")));
    if (pro.containsKey(key + "replayer")) {
      item.setReplayer(pro.getProperty(key + "replayer"));        
    }
    item.setReply(Long.parseLong(pro.getProperty(key + "reply")));
    if (pro.containsKey(key + "tid")) {
      item.setTid(pro.getProperty(key + "tid"));        
    }
    if (pro.containsKey(key + "title")) {
      item.setTitle(pro.getProperty(key + "title"));        
    }
    if (pro.containsKey(key + "url")) {
      item.setUrlString(pro.getProperty(key + "url"));        
    }
    item.setView(Long.parseLong(pro.getProperty(key + "view")));
    item.setRecommend(Integer.parseInt(pro.getProperty(key + "recommend")));
    FavoriteItem ffItem = new FavoriteItem();
    ffItem.setItem(item);
    ffItem.setAttachString(pro.getProperty(key + "attachString"));
    ffItem.setSaveTime(Long.parseLong(pro.getProperty(key + "saveTime")));
    return ffItem;
  }
}

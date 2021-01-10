package tk.miskyle.mcbbssearcher.data.history;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Properties;
import tk.miskyle.mcbbssearcher.McbbsSearcher;
import tk.miskyle.mcbbssearcher.forum.data.Item;

public class History {
  public static final String HISTORY_SETTING_PATH = McbbsSearcher.DATA_FLOD + "/histort.properties";
  
  private static final String FORMART = "附言: {0}    "
                                                           + "所在主版块: {1}    "
                                                           + "所在子版块: {2}    "
                                                           + "浏览时间: {3}";

  
  private static HashMap<String, HistoryItem> items = new HashMap<>();
  
  /**
   * 添加浏览记录.
   * @param item 浏览项目
   * @param section 主板块
   * @param subSection 子版块
   * @param attachString 附言
   */
  public static void look(Item item, String section, String subSection, String attachString) {
    HistoryItem historyItem = new HistoryItem();
    historyItem.setAttachString(attachString);
    historyItem.setItem(item);
    historyItem.setSection(section);
    historyItem.setSubSection(subSection);
    historyItem.setTime(new Date().getTime());
    item.setName(historyItem.getTimeString() + " " + item.getTitle());
    items.put(item.getName(), historyItem);
  }

  public static HashMap<String, HistoryItem> getItems() {
    return items;
  }
  
  /**
   * 获取历史记录信息.
   * @param key 键值
   * @return
   */
  public static String getInfo(String key) {
    HistoryItem item = items.get(key);
    if (item == null) {
      return "";
    }
    return MessageFormat.format(FORMART, 
        item.getAttachString(),
        item.getSection(),
        item.getSubSection(),
        item.getTimeString());
  }
  
  
  /**
   * 保存历史记录.
   */
  public static void save() {
    Properties pro = new Properties();
    int index = 0;
    for (HistoryItem i : items.values()) {
      String key = "item." + index++ + ".";
      pro.setProperty(key + "attachString", i.getAttachString());
      pro.setProperty(key + "saveTime", i.getTime() + "");
      pro.setProperty(key + "section", i.getSection());
      pro.setProperty(key + "subSection", i.getSubSection());
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
      if (item.getName() != null) {
        pro.setProperty(key + "name", item.getName());
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
    try {
      pro.store(new FileOutputStream(new File(HISTORY_SETTING_PATH)), "history");
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
  
  /**
   * 取读历史记录.
   */
  public static void load() {
    File file = new File(HISTORY_SETTING_PATH);
    if (!file.exists()) {
      return;
    }
    Properties pro = new Properties();
    try {
      pro.load(new FileInputStream(file));
    } catch (IOException e) {
      e.printStackTrace();
    }
    
    int index = 0;
    String key = "item." + index + ".";
    while (pro.containsKey(key + "url")) {
      
      Item item = new Item();
      item.setAgree(Boolean.parseBoolean(pro.getProperty(key + "agree")));
      if (pro.containsKey(key + "name")) {
        item.setName(pro.getProperty(key + "name"));
      }
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
      HistoryItem hhItem = new HistoryItem();
      hhItem.setItem(item);
      hhItem.setAttachString(pro.getProperty(key + "attachString"));
      hhItem.setTime(Long.parseLong(pro.getProperty(key + "saveTime")));
      hhItem.setSection(pro.getProperty(key + "section"));
      hhItem.setSubSection(pro.getProperty(key + "subSection"));
      
      items.put(hhItem.getItem().getName(), hhItem);
      key = "item." + index++ + ".";
    }
    
  }

}

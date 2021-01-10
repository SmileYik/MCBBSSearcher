package tk.miskyle.mcbbssearcher.forum;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Properties;
import tk.miskyle.mcbbssearcher.McbbsSearcher;
import tk.miskyle.mcbbssearcher.forum.data.Forum;
import tk.miskyle.mcbbssearcher.forum.data.Item;
import tk.miskyle.mcbbssearcher.forum.data.Section;
import tk.miskyle.mcbbssearcher.forum.data.SubSection;
import tk.miskyle.mcbbssearcher.forum.update.ForumUpdate;

public class ForumSaver {
  public static final String FORUM_FILE_PATH = McbbsSearcher.DATA_FLOD + "./forum.properties";
  
  public static boolean checkProperties() {
    return new File(FORUM_FILE_PATH).exists();
  }
  
  /**
   * 创建默认论坛数据并保存.
   */
  public static void createDefaultProperties() {
    Forum forum = ForumUpdate.updateForum();
    ForumManager.setForum(forum);
    saveForum();
  }
  
  /**
   * 保存论坛数据到本地文件中.
   */
  public static void saveForum() {
    Properties pro = new Properties();
    StringBuilder sectionList = new StringBuilder();
    ForumManager.getForum().getSections().values().forEach(section -> {
      sectionList.append(section.getGid()).append(";");
      
      String key1 = "section." + section.getGid();
      pro.setProperty(key1 + ".name", section.getName());
      pro.setProperty(key1 + ".url", section.getUrlString());
      pro.setProperty(key1 + ".gid", section.getGid());
      
      StringBuilder subSectionList = new StringBuilder();
      section.getSubs().values().forEach(sub -> {
        subSectionList.append(sub.getName()).append(";");
        
        String key2 = "sub-section." + sub.getName();
        pro.setProperty(key2 + ".name", sub.getName());
        pro.setProperty(key2 + ".url", sub.getUrlString());
        if (sub.getFid() != null) {
          pro.setProperty(key2 + ".fid", sub.getFid());
        }
        
        int index = 0;
        for (Item item : sub.getItems()) {
          String key = "item." + sub.getName() + "." + index++;
          if (item.getTid() != null) {
            pro.setProperty(key + ".tid", item.getTid());
          }
          if (item.getTitle() != null) {
            pro.setProperty(key + ".title", item.getTitle());
          }
          pro.setProperty(key + ".post-time", item.getPostTime() + "");
          if (item.getAuther() != null) {
            pro.setProperty(key + ".auther", item.getAuther());
          }
          pro.setProperty(key + ".reply-time", item.getReplyTime() + "");
          if (item.getReplayer() != null) {
            pro.setProperty(key + ".replayer", item.getReplayer());
          }
          if (item.getUrlString() != null) {
            pro.setProperty(key + ".url", item.getUrlString());
          }
          pro.setProperty(key + ".view", item.getView() + "");
          pro.setProperty(key + ".reply", item.getReply() + "");
          pro.setProperty(key + ".heatlevel", item.getHeatlevel() + "");
          pro.setProperty(key + ".digest", item.getDigest() + "");
          pro.setProperty(key + ".agree", item.isAgree() + "");
          pro.setProperty(key + ".recommend", item.getRecommend() + "");
        }
      });
      pro.setProperty(key1 + ".sub-section-list", subSectionList.toString());
    });
    pro.setProperty("section-list", sectionList.toString());
    try {
      pro.store(new FileOutputStream(new File(FORUM_FILE_PATH)), "save all forum.");
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
  
  /**
   * 从配置文件中读取论坛数据.
   * @return
   */
  public static Forum loadForum() {
    Forum forum = new Forum();
    Properties pro = new Properties();
    try {
      pro.load(new FileInputStream(new File(FORUM_FILE_PATH)));
    } catch (IOException e) {
      e.printStackTrace();
    }
    if (pro.isEmpty()) {
      return null;
    }
    
    for (String gid : pro.getProperty("section-list").split(";")) {
      String key1 = "section." + gid;
      Section section = new Section();
      section.setName(pro.getProperty(key1 + ".name"));
      section.setUrlString(pro.getProperty(key1 + ".url"));
      if (pro.containsKey(key1 + ".gid")) {
        section.setGid(pro.getProperty(key1 + ".gid"));
      }
      
      for (String name : pro.getProperty(key1 + ".sub-section-list").split(";")) {
        String key2 = "sub-section." + name;
        SubSection sub = new SubSection();
        sub.setName(pro.getProperty(key2 + ".name"));
        sub.setUrlString(pro.getProperty(key2 + ".url"));
        if (pro.containsKey(key2 + ".fid")) {
          sub.setFid(pro.getProperty(key2 + ".fid"));
        }
        
        int index = 0;
        while (pro.containsKey("item." + name + "." + index + ".view")) {
          String key = "item." + name + "." + index++;
          Item item = new Item();
          if (pro.containsKey(key + ".tid")) {
            item.setTid(pro.getProperty(key + ".tid"));            
          }
          if (pro.containsKey(key + ".title")) {
            item.setTitle(pro.getProperty(key + ".title"));
          }
          item.setPostTime(Long.parseLong(pro.getProperty(key + ".post-time")));
          if (pro.containsKey(key + ".auther")) {
            item.setAuther(pro.getProperty(key + ".auther"));
          }
          item.setReplyTime(Long.parseLong(pro.getProperty(key + ".reply-time")));
          if (pro.containsKey(key + ".replayer")) {
            item.setReplayer(pro.getProperty(key + ".replayer"));
          }
          if (pro.containsKey(key + ".url")) {
            item.setUrlString(pro.getProperty(key + ".url"));
          }
          item.setView(Long.parseLong(pro.getProperty(key + ".view")));
          item.setReply(Long.parseLong(pro.getProperty(key + ".reply")));
          item.setHeatlevel(Integer.parseInt(pro.getProperty(key + ".heatlevel")));
          item.setDigest(Integer.parseInt(pro.getProperty(key + ".digest")));
          item.setAgree(Boolean.parseBoolean(pro.getProperty(key + ".agree")));
          item.setRecommend(Integer.parseInt(pro.getProperty(key + ".recommend")));
          sub.getItems().add(item);
        }
        section.addSubs(sub);
      }
      forum.getSections().put(section.getName(), section);
    }
    return forum;
  }
  
  /**
   * 保存单独一个版块到指定文件中.
   * @param aimFile 目标文件
   * @param sub 子版块
   * @param section 父板块
   */
  public static void saveSubForumToFile(File aimFile, SubSection sub, String section) {
    Properties pro = new Properties();
    pro.setProperty("section", section);
    pro.setProperty("subSection.name", sub.getName());
    pro.setProperty("subSection.fid", sub.getFid());
    pro.setProperty("subSection.url", sub.getUrlString());
    
    int index = 0;
    for (Item item : sub.getItems()) {
      String key = "item." + index++ + ".";
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
    try {
      pro.store(new FileOutputStream(aimFile), section + " " + sub.getName());
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
  
  /**
   * 从目的文件中取读一个子版块数据.
   * @param aimFile 目标文件
   */
  public static SubSection loadSubSection(File aimFile) {
    Properties pro = new Properties();
    try {
      pro.load(new FileInputStream(aimFile));
    } catch (IOException e) {
      e.printStackTrace();
    }
    
    String section = pro.getProperty("section");
    String subSectionName = pro.getProperty("subSection.name");
    SubSection sub = new SubSection();
    boolean flag = false;
    if (ForumManager.getForum().getSections().containsKey(section)) {
      Section s = ForumManager.getForum().getSections().get(section);
      if (s.getSubs().containsKey(subSectionName)) {
        sub = s.getSubs().get(subSectionName);
        flag = true;
      }
    } else {
      Section s = new Section();
      s.setGid("0");
      s.setUrlString("null");
      s.setName(section);
      ForumManager.getForum().getSections().put(section, s);
      s.getSubs().put(subSectionName, sub);
    }
    
    if (!flag) {
      sub.setName(subSectionName);
      sub.setFid(pro.getProperty("subSection.fid"));
      sub.setUrlString(pro.getProperty("subSection.url"));
      sub.setItems(new LinkedList<>());
    }
    
    ArrayList<String> checker = new ArrayList<>();
    for (Item item : sub.getItems()) {
      checker.add(item.getUrlString());
    }
    
    int index = 0;
    String key = "item." + index + ".";
    while (pro.containsKey(key + "url")) {
      if (checker.contains(pro.getProperty(key + "url"))) {
        key = "item." + index++ + ".";
        continue;
      }
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
      sub.getItems().add(item);
      key = "item." + index++ + ".";
    }
    System.out.println(
        ForumManager.getForum().getSections().get(section).getSubs().get(subSectionName));
    return sub;
  }
}

package tk.miskyle.mcbbssearcher.forum.data;

import java.text.ParseException;
import java.text.SimpleDateFormat;

public class Item extends HttpPage {
  public static final String POST_TIME_FORMAT = "yyyy-MM-dd";
  public static final String REPLY_TIME_FORMAT = "yyyy-MM-dd HH:mm";

  private String tid;
  private String title;
  private long postTime;
  private String auther;
  private long replyTime;
  private String replayer;
  private long view;
  private long reply;
  private int heatlevel;
  private int digest = 0;
  private boolean agree = false;
  private int recommend;
  
  public Item() {
    
  }

  /**
   * 获取帖子标题.
   * @return
   */
  public String getTitle() {
    if (title == null) {
      return "";      
    }
    return title.replace("amp;", "");
  }

  /**
   * 获取帖子url.
   */
  public String getUrlString() {
    if (urlString == null) {
      return "";      
    }
    return urlString.replace("amp;", "");
  }

  public long getPostTime() {
    return postTime;
  }

  public String getPostTimeString() {
    return new SimpleDateFormat(POST_TIME_FORMAT).format(postTime);
  }

  public void setPostTime(long postTime) {
    this.postTime = postTime;
  }

  /**
   * 设定发帖时间.
   * @param postTime 发帖时间
   */
  public void setPostTime(String postTime) {
    try {
      this.postTime = new SimpleDateFormat(POST_TIME_FORMAT).parse(postTime).getTime();
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
    return new SimpleDateFormat(REPLY_TIME_FORMAT).format(replyTime);
  }

  public void setReplyTime(long replyTime) {
    this.replyTime = replyTime;
  }

  /**
   * 设定回帖时间.
   * @param replyTime 回帖时间
   */
  public void setReplyTime(String replyTime) {
    try {
      this.replyTime = new SimpleDateFormat(REPLY_TIME_FORMAT).parse(replyTime).getTime();
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

  public void setTitle(String title) {
    this.title = title;
  }

  public void setUrlString(String urlString) {
    this.urlString = urlString;
  }

  public int getHeatlevel() {
    return heatlevel;
  }

  public void setHeatlevel(int heatlevel) {
    this.heatlevel = heatlevel;
  }

  public int getDigest() {
    return digest;
  }

  public void setDigest(int digest) {
    this.digest = digest;
  }

  public boolean isAgree() {
    return agree;
  }

  public void setAgree(boolean agree) {
    this.agree = agree;
  }

  public int getRecommend() {
    return recommend;
  }

  public void setRecommend(int recommend) {
    this.recommend = recommend;
  }

  public String getTid() {
    return tid;
  }

  public void setTid(String tid) {
    this.tid = tid;
  }

  @Override
  public String toString() {
    return "Item [tid=" + tid + ", title=" + title + ", postTime=" + postTime 
        + ", auther=" + auther + ", replyTime=" + replyTime + ", replayer=" 
        + replayer + ", view=" + view + ", reply=" + reply + ", heatlevel=" 
        + heatlevel + ", digest=" + digest + ", agree=" + agree + ", recommend=" 
        + recommend + ", name=" + name + ", urlString=" + urlString + "]";
  }
  
  
}

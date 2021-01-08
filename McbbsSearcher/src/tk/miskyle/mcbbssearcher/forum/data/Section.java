package tk.miskyle.mcbbssearcher.forum.data;

import java.util.HashMap;

public class Section extends HttpPage {
  public static final String INDEX_SPLIT = "&index";

  private String gid;
  private HashMap<String, SubSection> subs = new HashMap<>();
  
  public String getGid() {
    return gid;
  }
  
  public void setGid(String gid) {
    this.gid = gid;
  }
  
  public HashMap<String, SubSection> getSubs() {
    return subs;
  }
  
  public void setSubs(HashMap<String, SubSection> subs) {
    this.subs = subs;
  }
  
  /**
   * 加入子区块.
   * @param sub 子区块
   */
  public void addSubs(SubSection sub) {
    if (!subs.containsKey(sub.name)) {
      subs.put(sub.name, sub);
    }
  }
  
  @Override
  public String toString() {
    return "Section [gid=" + gid + ", subs=" + subs 
        + ", name=" + name + ", urlString=" + urlString + "]";
  }
}

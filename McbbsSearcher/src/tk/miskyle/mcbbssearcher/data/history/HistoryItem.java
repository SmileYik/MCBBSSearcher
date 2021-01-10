package tk.miskyle.mcbbssearcher.data.history;

import java.text.SimpleDateFormat;
import tk.miskyle.mcbbssearcher.forum.data.Item;

public class HistoryItem {
  public static final String TIME_FORMAT = "YYYY-MM-DD hh:mm:ss";
  
  private Item item;
  private Long time;
  private String section;
  private String subSection;
  private String attachString;
  
  public Item getItem() {
    return item;
  }
  
  public void setItem(Item item) {
    this.item = item;
  }
  
  public Long getTime() {
    return time;
  }
  
  public void setTime(Long time) {
    this.time = time;
  }
  
  public String getTimeString() {
    return new SimpleDateFormat(TIME_FORMAT).format(time);
  }
  
  public String getSection() {
    return section == null ? "" : section;
  }
  
  public void setSection(String section) {
    this.section = section;
  }
  
  public String getSubSection() {
    return subSection == null ? "" : subSection;
  }
  
  public void setSubSection(String subSection) {
    this.subSection = subSection;
  }
  
  public String getAttachString() {
    return attachString;
  }
  
  public void setAttachString(String attachString) {
    this.attachString = attachString;
  }
}

package tk.miskyle.mcbbssearcher.data.favorite;

import java.text.SimpleDateFormat;
import tk.miskyle.mcbbssearcher.forum.data.Item;

public class FavoriteItem {
  private Item item;
  private String attachString;
  private Long saveTime;
  
  public Item getItem() {
    return item;
  }
  
  public void setItem(Item item) {
    this.item = item;
  }
  
  public String getAttachString() {
    return attachString;
  }
  
  public void setAttachString(String attachString) {
    this.attachString = attachString;
  }
  
  public Long getSaveTime() {
    return saveTime;
  }
  
  public String getSaveTimeString() {
    return new SimpleDateFormat(Item.POST_TIME_FORMAT).format(saveTime);
  }
  
  public void setSaveTime(Long saveTime) {
    this.saveTime = saveTime;
  }
}

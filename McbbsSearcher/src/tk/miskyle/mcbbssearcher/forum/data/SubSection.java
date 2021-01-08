package tk.miskyle.mcbbssearcher.forum.data;

import java.util.LinkedList;

public class SubSection extends HttpPage {
  private String fid = null;
  private int pageAmount;
  private LinkedList<Item> items = new LinkedList<>();
  
  public String getFid() {
    return fid;
  }
  
  public void setFid(String fid) {
    this.fid = fid;
  }
  
  public int getPageAmount() {
    return pageAmount;
  }
  
  public void setPageAmount(int pageAmount) {
    this.pageAmount = pageAmount;
  }
  
  public LinkedList<Item> getItems() {
    return items;
  }
  
  public void setItems(LinkedList<Item> items) {
    this.items = items;
  }

  @Override
  public String toString() {
    return "SubSection [fid=" + fid + ", pageAmount=" + pageAmount 
        + ", items=" + items + ", name=" + name
        + ", urlString=" + urlString + "]";
  }
  
  
}

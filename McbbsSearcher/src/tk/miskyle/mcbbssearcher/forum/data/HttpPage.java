package tk.miskyle.mcbbssearcher.forum.data;

public class HttpPage {
  protected String name;
  protected String urlString;
  
  protected HttpPage(String name, String urlString) {
    super();
    this.name = name;
    this.urlString = urlString;
  }

  protected HttpPage(String urlString) {
    super();
    this.urlString = urlString;
  }
  
  protected HttpPage() {
    
  }

  public String getName() {
    return name;
  }
  
  public String getUrlString() {
    return urlString;
  }

  public void setName(String name) {
    this.name = name.replace("amp;", "");
  }

  public void setUrlString(String urlString) {
    this.urlString = urlString.replace("amp;", "");
  }
}

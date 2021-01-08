package tk.miskyle.mcbbssearcher.data;

public class McbbsUserData {
  public static McbbsUserData user;
  
  static {
    user = new McbbsUserData("McbbsSearcher", "");
  }
  
  private String userName;
  private String cookies;
  
  /**
   * 创建mcbbs用户数据.
   * @param userName 用户名
   * @param cookies cookie
   */
  public McbbsUserData(String userName, String cookies) {
    super();
    this.userName = userName;
    this.cookies = cookies;
  }

  public String getUserName() {
    return userName;
  }
  
  public void setUserName(String userName) {
    this.userName = userName;
  }
  
  public String getCookies() {
    return cookies;
  }
  
  public void setCookies(String cookies) {
    this.cookies = cookies;
  }
}

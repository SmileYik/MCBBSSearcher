package tk.miskyle.mcbbssearcher.util;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import tk.miskyle.mcbbssearcher.data.McbbsUserData;


public class McbbsConnection {
  private HttpURLConnection newGetConnection = null;
  
  public static McbbsConnection newGetConnect(String urlString) {
    return newGetConnect(urlString, null);
  }
  
  /**
   * 创建一个MCBBS的连接.
   * @param urlString 目的url
   * @param user 用户信息
   * @return
   */
  public static McbbsConnection newGetConnect(String urlString, McbbsUserData user) {
    if (urlString.indexOf("https://") != 0) {
      if (urlString.indexOf("http://") == 0) {
        urlString = urlString.replace("http://", "https://");
      } else {
        urlString = "https://" + urlString;
      }
    }
    URL url = null;
    try {
      url = new URL(urlString);
    } catch (MalformedURLException e) {
      e.printStackTrace();
    }
    return newGetConnect(url, user);
  }
  
  public static McbbsConnection newGetConnect(URL url) {
    return newGetConnect(url, null);
  }
  
  /**
   * 创建一个MCBBS的连接.
   * @param url 目的url
   * @param user 用户信息
   * @return
   */
  public static McbbsConnection newGetConnect(URL url, McbbsUserData user) {
    if (url == null) {
      return null;
    }
    McbbsConnection mcbbsCon = new McbbsConnection();
    try {
      mcbbsCon.newGetConnection = (HttpURLConnection) url.openConnection();
      mcbbsCon.newGetConnection.setConnectTimeout(6500);
      mcbbsCon.newGetConnection.setDoInput(true);
      mcbbsCon.newGetConnection.setRequestMethod("GET");
      mcbbsCon.newGetConnection.setRequestProperty(
          "User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
      if (user == null) {
        user = McbbsUserData.user;
      }
      mcbbsCon.newGetConnection.setRequestProperty("Cookie", user.getCookies());
    } catch (IOException e) {
      e.printStackTrace();
    }
    return mcbbsCon.newGetConnection == null ? null : mcbbsCon;
  }
  
  public HttpURLConnection getConnection() {
    return newGetConnection;
  }
}

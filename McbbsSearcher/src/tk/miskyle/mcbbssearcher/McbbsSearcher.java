package tk.miskyle.mcbbssearcher;

import java.io.File;
import javax.swing.JOptionPane;
import tk.miskyle.mcbbssearcher.data.Setting;
import tk.miskyle.mcbbssearcher.data.favorite.Favorite;
import tk.miskyle.mcbbssearcher.data.history.History;
import tk.miskyle.mcbbssearcher.form.MainForm;
import tk.miskyle.mcbbssearcher.forum.ForumManager;
import tk.miskyle.mcbbssearcher.forum.ForumSaver;

public class McbbsSearcher {
  
  public static final String DATA_FLOD = "./McbbsSearcher";
  
  /**
   * . 
   * @param args 0.
   */
  public static void main(String[] args) {
    propertiesChecker();
    propertiesLoader();
    MainForm.main();
  }
  
  /**
   * 检查配置文件是否可用.
   */
  public static void propertiesChecker() {
    if (!new File(DATA_FLOD).exists()) {
      new File(DATA_FLOD).mkdir();
    }
    
    Setting.checker();
    Favorite.checker();
    
    if (!ForumSaver.checkProperties()) {
      JOptionPane.showMessageDialog(null, 
          "未检测到有效论坛数据, 正在抓取论坛数据\n"
          + "当数据收集完毕后会打开用户界面.\n"
          + "此项过程一般与网络连接有关,\n"
          + "请保持网络通畅并耐心等待!\n"
          + "点击确定开始获取数据.");
      ForumSaver.createDefaultProperties();
    }
  }
  
  /**
   * 读取配置文件.
   */
  public static void propertiesLoader() {
    ForumManager.setForum(ForumSaver.loadForum());
    Favorite.load();
    History.load();
    Setting.load();
  }
}

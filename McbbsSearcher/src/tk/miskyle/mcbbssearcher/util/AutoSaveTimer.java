package tk.miskyle.mcbbssearcher.util;

import java.util.TimerTask;
import tk.miskyle.mcbbssearcher.data.Setting;
import tk.miskyle.mcbbssearcher.data.favorite.Favorite;
import tk.miskyle.mcbbssearcher.data.history.History;
import tk.miskyle.mcbbssearcher.form.MainForm;
import tk.miskyle.mcbbssearcher.forum.ForumSaver;

public class AutoSaveTimer extends TimerTask {
  private int time;

  @Override
  public void run() {
    ++time;
    if (time == Setting.saveTime) {
      time = 0;
      Setting.save();
      ForumSaver.saveForum();
      Favorite.save();
      History.save();
      MainForm.setInfoText("自动保存任务 --- 所有配置保存完成.");
    }
  }
  

}

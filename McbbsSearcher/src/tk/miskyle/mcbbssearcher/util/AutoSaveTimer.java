package tk.miskyle.mcbbssearcher.util;

import java.util.TimerTask;
import tk.miskyle.mcbbssearcher.data.Setting;

public class AutoSaveTimer extends TimerTask {
  private int time;

  @Override
  public void run() {
    ++time;
    if (time == Setting.saveTime) {
      time = 0;
      Setting.save();
      ForumSaver.saveForum();
    }
  }
  

}

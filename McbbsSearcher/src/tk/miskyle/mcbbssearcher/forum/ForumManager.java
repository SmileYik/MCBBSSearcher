package tk.miskyle.mcbbssearcher.forum;

import tk.miskyle.mcbbssearcher.forum.data.Forum;

public class ForumManager {
  private static Forum forum;

  public static Forum getForum() {
    return forum;
  }

  public static void setForum(Forum forum) {
    ForumManager.forum = forum;
  }
}

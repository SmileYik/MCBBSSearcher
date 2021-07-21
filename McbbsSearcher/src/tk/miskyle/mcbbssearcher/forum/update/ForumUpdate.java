package tk.miskyle.mcbbssearcher.forum.update;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedList;
import org.junit.Test;

import tk.miskyle.mcbbssearcher.form.MainForm;
import tk.miskyle.mcbbssearcher.forum.data.Forum;
import tk.miskyle.mcbbssearcher.forum.data.Item;
import tk.miskyle.mcbbssearcher.forum.data.Section;
import tk.miskyle.mcbbssearcher.forum.data.SubSection;
import tk.miskyle.mcbbssearcher.util.McbbsConnection;

public class ForumUpdate {
  
  public static final  String FORUM_MAIN_URL = "https://www.mcbbs.net/";
  public static final  String FORUM_URL = FORUM_MAIN_URL + "forum.php";
  
  public static boolean updateFlag = false;
  public static boolean updateFinished = false;
  
  @Test
  public void abc() {
    Forum forum = updateForum();
    System.out.println(forum);
    Section sec = forum.getSections().values().toArray(new Section[forum.getSections().size()])[5];
    updateSubSection(sec.getSubs().values().toArray(new SubSection[sec.getSubs().size()])[2], 1);
    //    ForumManager.setForum(forum);
    //    ForumSaver.saveForum();
    //    System.out.println(ForumSaver.loadForum());
  }
  
  /**
   * 更新论坛板块数据.
   */
  public static Forum updateForum() {
    McbbsConnection connection = McbbsConnection.newGetConnect(FORUM_URL,
        MainForm.getUpdateUser());
    if (connection == null) {
      //连接失败
      return null;
    }
    Forum forum = new Forum();
    try (BufferedReader reader = new BufferedReader(
        new InputStreamReader(connection.getConnection().getInputStream(), "UTF-8"))) {
      String line = null;
      while ((line = reader.readLine()) != null) {
        if (line.contains("forum.php?gid=")) {
          String temp1 = line.split("gid=")[1];
          String id = temp1.split("\"")[0];
          String temp2 = temp1.split("\">")[1];
          String name = temp2.split("<")[0].replace("amp;", "");
          
          Section section = new Section();
          section.setGid(id);
          section.setName(name);
          section.setUrlString("http://www.mcbbs.net/forum.php?gid=" + id);
          forum.getSections().put(section.getName(), section);
        }
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
    forum.getSections().values().forEach(v -> {
      updateSection(v);      
    });
    return forum;
  }
  
  /**
   * 更新板块中的子版块.
   * @param section 目的板块
   */
  public static void updateSection(Section section) {
    McbbsConnection connection = McbbsConnection.newGetConnect(section.getUrlString(),
        MainForm.getUpdateUser());
    if (connection == null) {
      // 连接失败
      return;
    }
    try (BufferedReader reader = new BufferedReader(
        new InputStreamReader(connection.getConnection().getInputStream(), "UTF-8"))) {
      String line = null;
      while ((line = reader.readLine()) != null) {
        if (line.contains("<h2><a href=\"")) {
          String temp = line.replace("<h2><a href=\"", "");
          String url = "http://www.mcbbs.net/" + temp.split("1")[0] + Section.INDEX_SPLIT + ".html";
          String name2 = temp.split(">")[1].split("<")[0];
          if (!url.contains("http://www.mcbbs.net/forum-")) {
            continue;            
          }
          SubSection sub = new SubSection();
          sub.setName(name2);
          sub.setUrlString(url);
          simpleUpdateSubSection(sub);
          section.addSubs(sub);
        }
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
  
  /**
   * 更新板块信息, 如页数.
   * @param sub 目的板块
   */
  public static void simpleUpdateSubSection(SubSection sub) {
    if (sub.getUrlString().equalsIgnoreCase("http://www.mcbbs.net/forum-&index.html")) {
      return;
    }
    McbbsConnection connection = 
        McbbsConnection.newGetConnect(sub.getUrlString().replace(Section.INDEX_SPLIT, "1"),
            MainForm.getUpdateUser());
    if (connection == null) {
      // 连接失败
      return;
    }
    try (BufferedReader reader = new BufferedReader(
        new InputStreamReader(connection.getConnection().getInputStream(), "UTF-8"))) {
      String line = null;
      while ((line = reader.readLine()) != null) {
        if (line.contains("srhfid") && sub.getFid() == null) {
          sub.setFid(line.replaceAll("[^0-9]", ""));
          sub.setUrlString(
              "http://www.mcbbs.net/forum.php?mod=forumdisplay&fid=" + sub.getFid() + "&filter=sortall&sortall=1&page=" + Section.INDEX_SPLIT);
        } else if (line.contains("class=\"last\">...")) {
          sub.setPageAmount(Integer.parseInt(
              line.split("class=\"last\">...")[1]
                  .split("<")[0].replaceAll("[^0-9]", "")));
          break;
        }
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
  
  /**
   * 更新板块中的帖子.
   * @param sub 对应板块
   * @param page 页数, 不知道如何填请填 1
   */
  public static void updateSubSection(SubSection sub, int page) {
    McbbsConnection connection = 
        McbbsConnection.newGetConnect(
            sub.getUrlString().replace(Section.INDEX_SPLIT, page + ""), MainForm.getUpdateUser());
    if (connection == null) {
      // 连接失败
      return;
    }
    try (BufferedReader reader = new BufferedReader(
        new InputStreamReader(connection.getConnection().getInputStream(), "UTF-8"))) {
      String line = null;
      
      LinkedList<String> lines = new LinkedList<>();
      int index = 0;
      int start = -1;
      //判断是否继续更新.
      while ((line = reader.readLine()) != null) {
        if (!updateFlag) {
          break;
        }
        lines.add(line);
        if (page == 1) {
          if (line.contains("srhfid") && sub.getFid() == null) {
            sub.setFid(line.replaceAll("[^0-9]", ""));
            sub.setUrlString(
                "http://www.mcbbs.net/forum.php?mod=forumdisplay&fid=" + sub.getFid() + "&filter=sortall&sortall=1&page=" + Section.INDEX_SPLIT);
          } else if (line.contains("class=\"last\">...")) {
            sub.setPageAmount(Integer.parseInt(
                line.split("class=\"last\">...")[1]
                    .split("<")[0].replaceAll("[^0-9]", "")));
          }          
        }

        if (line.contains("<tbody id=\"normalthread")) {
          start = index;
        } else if (line.contains("</tbody>") && start != -1) {
          Item item = new Item();

          for (int i = start; i <= index; i++) {
            if (!updateFlag) {
              break;
            }
            String subLine = lines.get(i);
            if (subLine.contains("atarget(this)") && subLine.contains("</em> <a href=\"")) {
              //获取帖子标题和对应链接
              String temp = subLine.split("</em> <a href=\"")[1];
              String title = temp.split(">")[1].split("<")[0].replace("amp;", "");
              String url = FORUM_MAIN_URL + temp.split("\"")[0].replace("amp;", "");
              item.setTitle(title);
              item.setUrlString(url);
              item.setTid(url.split("&tid=")[1].split("&")[0]);
            } else if (subLine.contains("#lastpost") && subLine.contains("</em>")) {
              //获取回复时间及回复者
              String preLine = lines.get(i - 1);
              String replyer;
              String replyTime;
              if (subLine.contains("><span title=")) {
                replyTime = subLine.split("><span title=\"")[1].split("\"")[0];
              } else {
                replyTime = subLine.split("\">")[1].split("<")[0];
              }
              if (preLine.contains("匿名")) {
                replyer = "匿名";
              } else {
                replyer = preLine.split("\">")[1].split("<")[0];
              }
              item.setReplayer(replyer);
              item.setReplyTime(replyTime);
            } else if (subLine.contains("uid=")) {
              //获取作者信息及发帖时间
              String auther;
              String nextLine = lines.get(i + 1);
              String postTime;
              if (subLine.contains("匿名")) {
                auther = "匿名";
              } else {
                auther = subLine.split("\">")[1].split("<")[0];
              }
              if (nextLine.contains("><span title=")) {
                postTime = nextLine.split("><span title=\"")[1].split("\"")[0];
              } else {
                postTime = nextLine.split("<em><span>")[1].split("<")[0];
              }
              item.setAuther(auther);
              item.setPostTime(postTime);
            } else if (subLine.contains("</em></td>")) {
              //获取帖子查看次数, 回帖次数
              long reply = Long.parseLong(subLine.split("\"xi2\">")[1].split("<")[0]);
              long view = Long.parseLong(subLine.split("<em>")[1].split("<")[0]);
              item.setReply(reply);
              item.setView(view);
            } else if (subLine.contains("alt=\"digest\"")) {
              //精华
              item.setDigest(Integer.parseInt(subLine.split("title")[1].replaceAll("[^0-9]", "")));
            } else if (subLine.contains("alt=\"recommend\"")) {
              //评价指数
              item.setRecommend(
                  Integer.parseInt(subLine.split("title")[1].replaceAll("[^0-9]", "")));
            } else if (subLine.contains("alt=\"heatlevel\"")) {
              //帖子热度
              item.setHeatlevel(
                  Integer.parseInt(subLine.split("title")[1].replaceAll("[^0-9]", "")));
            } else if (subLine.contains("alt=\"agree\"")) {
              //设定是否被加分
              item.setAgree(true);
            }
          }
          System.out.println(item);
          boolean flag = true;
          for (Item temp : sub.getItems()) {
            if (temp.getTid() == item.getTid()) {
              temp.copyFrom(item);
              flag = false;
            }
          }
          if (flag) {
            sub.getItems().add(item);            
          }
          start = -1;
        }
        index++;
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
    
    if (page < sub.getPageAmount() && updateFlag) {
      updateSubSection(sub, page + 1);
    } else {
      updateFinished = true;
    }
  }
  
  /**
   * 设定某个子板块开始更新.
   */
  public static void startUpdate() {
    updateFlag = true;
    updateFinished = false;
  }
  
  public static boolean isUpdate() {
    return updateFlag;
  }
  
  /**
   * 设定某个子板块停止更新.
   */
  public static void stopUpdate() {
    updateFlag = false;
  }

  public static boolean isUpdateFinished() {
    return updateFinished;
  }
  
}

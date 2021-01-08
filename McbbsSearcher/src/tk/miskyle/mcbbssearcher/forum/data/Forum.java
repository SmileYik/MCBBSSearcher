package tk.miskyle.mcbbssearcher.forum.data;

import java.util.HashMap;

public class Forum {
  private HashMap<String, Section> sections = new HashMap<>();

  public HashMap<String, Section> getSections() {
    return sections;
  }

  public void setSections(HashMap<String, Section> sections) {
    this.sections = sections;
  }

  @Override
  public String toString() {
    return "Forum [sections=" + sections + "]";
  }
  
}

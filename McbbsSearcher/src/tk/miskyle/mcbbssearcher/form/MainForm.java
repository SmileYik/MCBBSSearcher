package tk.miskyle.mcbbssearcher.form;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JMenuBar;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JTabbedPane;
import javax.swing.JScrollPane;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.JProgressBar;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import javax.swing.text.TabExpander;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

import tk.miskyle.mcbbssearcher.data.Setting;
import tk.miskyle.mcbbssearcher.forum.ForumManager;
import tk.miskyle.mcbbssearcher.forum.data.Item;
import tk.miskyle.mcbbssearcher.forum.data.SubSection;
import tk.miskyle.mcbbssearcher.forum.update.ForumUpdate;
import tk.miskyle.mcbbssearcher.util.ForumSaver;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;
import java.awt.event.KeyAdapter;
import javax.swing.SwingConstants;
import javax.swing.JPopupMenu;
import java.awt.Component;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Color;
import javax.swing.JCheckBox;

public class MainForm {
  private JFrame frame;
  private JTextField tfSearchBox;
  private JTable tableItems;
  private JTabbedPane tabbedPane;
  private JTree treeForum;
  private JList<String> listLike;
  private JLabel lblInfo;
  private JButton tfmSelect;
  private JButton tfmUpdate;
  private JButton btnSearch;
  private JButton btnSaveAllForum;
  private JButton btnStopUpdate;
  private JButton btnStoreOut;
  private JButton btnLoadIn;
  private JScrollPane scrollPane_1;
  private JPanel panel;
  private JProgressBar progressBar;
  private JPopupMenu treeFormMenu;
  
  private String selectedSubSection;
  private String selectedSection;
  private DefaultTableModel tableItemsModel;
  private HashMap<String, Item> items = new HashMap<>();
  private HashMap<String, Item> searchedItem;
  private JCheckBox chckbxSaveWhenLike;
  private JCheckBox chckbxSaveWhenUpdate;
  private JCheckBox chckbxSolution403;
  private JCheckBox chckbxAutoSave;
  private JButton btnRefreshSetting;
  private JButton btnSaveSetting;
  
  /**
   * Launch the application.
   */
  public static void main(String[] args) {
    
    if (!ForumSaver.checkProperties()) {
      JOptionPane.showMessageDialog(null, 
          "未检测到有效论坛数据, 正在抓取论坛数据\n"
          + "当数据收集完毕后会打开用户界面.\n"
          + "此项过程一般与网络连接有关,\n"
          + "请保持网络通畅并耐心等待!\n"
          + "点击确定开始获取数据.");
      ForumSaver.createDefaultProperties();
    }
    
    EventQueue.invokeLater(new Runnable() {
      public void run() {
        try {
          MainForm window = new MainForm();
          window.frame.setVisible(true);
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
    });
  }

  /**
   * Create the application.
   */
  public MainForm() {
    initialize();
    
    ForumManager.setForum(ForumSaver.loadForum());
    
    setupForumList();
    setupListener();
    refreshSetting();
  }

  /**
   * Initialize the contents of the frame.
   */
  private void initialize() {
    frame = new JFrame();
    frame.setBounds(100, 100, 1035, 533);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.getContentPane().setLayout(null);
    
    lblInfo = new JLabel("");
    lblInfo.setBounds(0, 468, 1017, 27);
    lblInfo.setHorizontalAlignment(SwingConstants.CENTER);
    frame.getContentPane().add(lblInfo);
    tabbedPane = new JTabbedPane(JTabbedPane.TOP);
    tabbedPane.setBounds(0, 13, 199, 457);
    frame.getContentPane().add(tabbedPane);
    
    JScrollPane scrollPane = new JScrollPane();
    tabbedPane.addTab("论坛", null, scrollPane, null);
    
    treeForum = new JTree();
    treeForum.setRootVisible(false);
    scrollPane.setViewportView(treeForum);
    
    treeFormMenu = new JPopupMenu();
    addPopup(treeForum, treeFormMenu);
    
    tfmSelect = new JButton("选择");
    treeFormMenu.add(tfmSelect);
    
    tfmUpdate = new JButton("更新");
    treeFormMenu.add(tfmUpdate);
    
    btnStoreOut = new JButton("导出");
    treeFormMenu.add(btnStoreOut);
    
    btnLoadIn = new JButton("导入");
    treeFormMenu.add(btnLoadIn);
    
    JScrollPane scrollPane1 = new JScrollPane();
    tabbedPane.addTab("收藏夹", null, scrollPane1, null);
    
    listLike = new JList<>();
    scrollPane1.setViewportView(listLike);
    
    scrollPane_1 = new JScrollPane();
    tabbedPane.addTab("设定", null, scrollPane_1, null);
    
    panel = new JPanel();
    scrollPane_1.setViewportView(panel);
    panel.setLayout(null);
    
    btnSaveAllForum = new JButton("保存所有论坛数据");
    btnSaveAllForum.setBounds(10, 10, 172, 32);
    panel.add(btnSaveAllForum);
    
    JButton btnMcbbsUserForm = new JButton("Mcbbs 用户设定");
    btnMcbbsUserForm.setBounds(10, 77, 172, 32);
    panel.add(btnMcbbsUserForm);
    
    chckbxAutoSave = new JCheckBox("自动保存所有配置");
    chckbxAutoSave.setHorizontalAlignment(SwingConstants.LEFT);
    chckbxAutoSave.setBounds(19, 334, 147, 23);
    panel.add(chckbxAutoSave);
    
    chckbxSaveWhenLike = new JCheckBox("收藏板块时保存");
    chckbxSaveWhenLike.setHorizontalAlignment(SwingConstants.LEFT);
    chckbxSaveWhenLike.setBounds(19, 355, 133, 23);
    panel.add(chckbxSaveWhenLike);
    
    chckbxSaveWhenUpdate = new JCheckBox("更新板块时保存");
    chckbxSaveWhenUpdate.setHorizontalAlignment(SwingConstants.LEFT);
    chckbxSaveWhenUpdate.setBounds(19, 376, 133, 23);
    panel.add(chckbxSaveWhenUpdate);
    
    chckbxSolution403 = new JCheckBox("403解决方案");
    chckbxSolution403.setHorizontalAlignment(SwingConstants.LEFT);
    chckbxSolution403.setBounds(19, 310, 133, 23);
    panel.add(chckbxSolution403);
    
    btnRefreshSetting = new JButton("刷新设定");
    btnRefreshSetting.setBounds(10, 111, 172, 32);
    panel.add(btnRefreshSetting);
    
    btnSaveSetting = new JButton("保存设定");
    btnSaveSetting.setBounds(10, 44, 172, 32);
    panel.add(btnSaveSetting);
    
    JScrollPane scrollPane2 = new JScrollPane();
    scrollPane2.setBounds(201, 38, 816, 432);
    frame.getContentPane().add(scrollPane2);
    
    tableItems = new JTable();
    tableItemsModel = getDefaltTableModel();
    tableItems.setModel(tableItemsModel);
    tableItems.setRowSorter(new TableRowSorter<DefaultTableModel>(tableItemsModel));
    tableItems.setCellSelectionEnabled(true);
    tableItems.getColumnModel().getColumn(0).setPreferredWidth(750);
    tableItems.getColumnModel().getColumn(1).setPreferredWidth(60);
    tableItems.getColumnModel().getColumn(2).setPreferredWidth(90);
    tableItems.getColumnModel().getColumn(3).setPreferredWidth(60);
    tableItems.getColumnModel().getColumn(4).setPreferredWidth(90);
    tableItems.getColumnModel().getColumn(5).setPreferredWidth(60);
    tableItems.getColumnModel().getColumn(6).setPreferredWidth(60);
    tableItems.getColumnModel().getColumn(7).setPreferredWidth(40);
    tableItems.getColumnModel().getColumn(8).setPreferredWidth(60);
    tableItems.getColumnModel().getColumn(9).setPreferredWidth(60);
    tableItems.getColumnModel().getColumn(10).setPreferredWidth(40);
    tableItems.getColumnModel().getColumn(11).setPreferredWidth(60);
    scrollPane2.setViewportView(tableItems);
    
    JMenuBar menuBar = new JMenuBar();
    menuBar.setBounds(201, 10, 813, 27);
    frame.getContentPane().add(menuBar);
    
    JMenu mnItemMenu = new JMenu(" . . . ");
    menuBar.add(mnItemMenu);
    
    btnStopUpdate = new JButton("停止更新");
    mnItemMenu.add(btnStopUpdate);
    
    JLabel labelTask = new JLabel("无任务");
    menuBar.add(labelTask);
    
    tfSearchBox = new JTextField();
    menuBar.add(tfSearchBox);
    tfSearchBox.setColumns(10);
    
    btnSearch = new JButton("搜索");
    menuBar.add(btnSearch);
    
    progressBar = new JProgressBar();
    progressBar.setBounds(0, 468, 1017, 27);
    frame.getContentPane().add(progressBar);
  }
  
  private void setupForumList() {
    DefaultMutableTreeNode node = new DefaultMutableTreeNode("论坛");
    ForumManager.getForum().getSections().forEach((k, v) -> {
      DefaultMutableTreeNode node1 = new DefaultMutableTreeNode(v.getName());
      v.getSubs().values().forEach(sub -> {
        node1.add(new DefaultMutableTreeNode(sub.getName()));
      });
      node.add(node1);
    });
    treeForum.setModel(new DefaultTreeModel(node));
  }
  
  private void loadItems(String section, String subSectionName) {
    SubSection sub = 
        ForumManager.getForum().getSections().get(section).getSubs().get(subSectionName);
    System.out.println(sub);
    tableItemsModel.setRowCount(0);
    progressBar.setMaximum(sub.getItems().size());
    progressBar.setValue(0);
    items = new HashMap<>();
    sub.getItems().forEach(item -> {
      items.put(item.getTitle(), item);
      addItemsToTable(item);
      progressBar.setValue(progressBar.getValue() + 1);
    });
    
    selectedSubSection = subSectionName;
    selectedSection = section;
    setInfoText("取读到 " + tableItemsModel.getRowCount() + " 个条目");
  }
  
  private void resetItems() {
    tableItemsModel.setRowCount(0);
    progressBar.setMaximum(items.size());
    progressBar.setValue(0);
    for (Item item : items.values()) {
      addItemsToTable(item);
      progressBar.setValue(progressBar.getValue() + 1);
    }
    
    setInfoText("取读到 " + tableItemsModel.getRowCount() + " 个条目");
  }
  
  private void addItemsToTable(Item item) {
    tableItemsModel.addRow(new String[] {
        item.getTitle(), 
        item.getAuther(), 
        item.getPostTimeString(), 
        item.getReplayer(), 
        item.getReplyTimeString(),
        item.getView() + "", 
        item.getReply() + "", 
        item.getDigest() + "", 
        item.getHeatlevel() + "",
        item.isAgree() ? "推荐" : "未被推荐", 
        item.getRecommend() + "", 
        item.getTid()
    });
  }
  
  private void searchItem(String searchString) {
    searchedItem = new HashMap<>();
    tableItemsModel.setRowCount(0);
    progressBar.setMaximum(items.size());
    progressBar.setValue(0);
    String[] strs;
    if (searchString.contains(" ")) {
      strs = searchString.split(" ");
    } else {
      strs = new String[] {searchString};
    }
    for (Item item : items.values()) {
      boolean flag = true;
      for (String str : strs) {
        if (str.contains("author:")) {
          String author = str.replace("author:", "").toUpperCase();
          if (item == null
              || item.getAuther() == null
              || item.getAuther().isEmpty()
              || !item.getAuther().toUpperCase().contains(author)) {
            flag = false;
          }
        } else if (item == null 
            || item.getTitle() == null
            || !item.getTitle().toUpperCase().contains(str.toUpperCase())) {
          flag = false;
        }
      }
      if (flag) {
        searchedItem.put(item.getTitle(), item);
        addItemsToTable(item);
      }
      progressBar.setValue(progressBar.getValue() + 1);
    }
    
    setInfoText("搜索到 " + tableItemsModel.getRowCount() + " 个条目");
  }
  
  private DefaultTableModel getDefaltTableModel() {
    DefaultTableModel dtm = new DefaultTableModel(new String[][] {},
        new String[] { 
            "标题", 
            "作者", 
            "发布时间", 
            "回复者", 
            "回复时间", 
            "查看", 
            "回复", 
            "精华", 
            "热度", 
            "推荐", 
            "加分", 
            "tid"}) {
      /**
       * a.
       * 
       */
      private static final long serialVersionUID = 1L;
      Class<?>[] columnTypes = 
          new Class[] { 
              String.class, 
              String.class, 
              String.class, 
              String.class, 
              String.class,
              String.class, 
              String.class, 
              String.class, 
              String.class, 
              String.class, 
              String.class, 
              String.class 
              };

      public Class<?> getColumnClass(int columnIndex) {
        return columnTypes[columnIndex];
      }
      
      boolean[] columnEditables = new boolean[] {
          false,
          false, 
          false, 
          false, 
          false,
          false,
          false,
          false, 
          false, 
          false,
          false,
          false
      };
      
      public boolean isCellEditable(int row, int column) {
          return columnEditables[column];
      }
    };
    return dtm;
  }
  
  private void setupListener() {
    //双击论坛板块
    treeForum.addMouseListener(new MouseAdapter() {
      @Override
      public void mouseClicked(MouseEvent e) {
        if (e.getClickCount() != 2
            || treeForum.isSelectionEmpty()) {
          return;
        }
        TreePath path = treeForum.getSelectionPath();
        if (path.getPathCount() != 3) {
          return;
        }
        loadItems(path.getPath()[1].toString(), path.getPath()[2].toString());
      }
    });
    
    //选择板块
    tfmSelect.addMouseListener(new MouseAdapter() {
      @Override
      public void mouseClicked(MouseEvent e) {
        treeFormMenu.setVisible(false);
        
        if (treeForum.isSelectionEmpty()) {
          return;
        }
        TreePath path = treeForum.getSelectionPath();
        if (path.getPathCount() != 3) {
          return;
        }
        loadItems(path.getPath()[1].toString(), path.getPath()[2].toString());
      }
    });
    
    //更新板块
    tfmUpdate.addMouseListener(new MouseAdapter() {
      @Override
      public void mouseClicked(MouseEvent e) {
        treeFormMenu.setVisible(false);
        
        if (treeForum.isSelectionEmpty()) {
          return;
        }
        TreePath path = treeForum.getSelectionPath();
        if (path.getPathCount() != 3) {
          return;
        }
        String section = path.getPath()[1].toString();
        String subSection = path.getPath()[2].toString();
        SubSection subS = 
            ForumManager.getForum().getSections().get(section).getSubs().get(subSection);
        setInfoText("开始更新 " + subSection + "板块");
        
        ForumUpdate.startUpdate();
        new Thread(() -> {
          ForumUpdate.updateSubSection(subS, 1);
        }).start();
        
        new Timer().schedule(new TimerTask() {
          
          @Override
          public void run() {
            if (!ForumUpdate.isUpdate()) {
              setInfoText("更新终止");
              this.cancel();
              return;
            }
            
            //更新更新进度信息
            int size = subS.getItems().size();
            int aimSize = subS.getPageAmount() * 23;
            setInfoText(String.format("更新中: %.2f ", size * 100 / (double) aimSize) + "%");
            progressBar.setMaximum(aimSize);
            progressBar.setValue(size);
          }
        }, 300L, 300L);
      }
    });
    
    //导出板块数据
    btnStoreOut.addMouseListener(new MouseAdapter() {
      @Override
      public void mouseClicked(MouseEvent e) {
        treeFormMenu.setVisible(false);
        
        if (treeForum.isSelectionEmpty()) {
          return;
        }
        TreePath path = treeForum.getSelectionPath();
        if (path.getPathCount() != 3) {
          return;
        }
        String section = path.getPath()[1].toString();
        String subSection = path.getPath()[2].toString();
        SubSection subS = 
            ForumManager.getForum().getSections().get(section).getSubs().get(subSection);
        
        File aim = chooseFile();
        if (aim == null) {
          JOptionPane.showMessageDialog(frame, "未选中文件!");
        } else {
          ForumSaver.saveSubForumToFile(aim, subS, section);
          JOptionPane.showMessageDialog(frame, "导出成功!");
        }
        
      }
    });
    
    //导入板块数据
    btnLoadIn.addMouseListener(new MouseAdapter() {
      @Override
      public void mouseClicked(MouseEvent e) {
        treeFormMenu.setVisible(false);
        File aim = chooseFile();
        if (aim == null) {
          JOptionPane.showMessageDialog(frame, "未选中文件!");
        } else {
          SubSection sub = ForumSaver.loadSubSection(aim);
          JOptionPane.showMessageDialog(frame, "导入成功!\n"
              + "导入至子版块: " + sub.getName());
          setupForumList();
        }
        
      }
    });
    
    //停止更新
    btnStopUpdate.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        ForumUpdate.stopUpdate();
      }
    });
    
    //搜索
    btnSearch.addMouseListener(new MouseAdapter() {
      @Override
      public void mouseClicked(MouseEvent e) {
        if (items == null
            || tfSearchBox.getText().isEmpty()) {
          //恢复默认列表
          resetItems();
          return;
        }
        searchItem(tfSearchBox.getText());
      }
    });
    
    //搜索栏按下回车键
    tfSearchBox.addKeyListener(new KeyAdapter() {
      @Override
      public void keyPressed(KeyEvent e) {
        if (e.getID() == KeyEvent.VK_ENTER) {
          if (items == null
              || tfSearchBox.getText().isEmpty()) {
            //恢复默认列表
            resetItems();
            return;
          }
          searchItem(tfSearchBox.getText());
        }
      }
    });
    
    //保存所有论坛数据
    btnSaveAllForum.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        ForumSaver.saveForum();
        JOptionPane.showMessageDialog(frame, "保存成功!");
      }
    });
    
    
    
    //403 解决方案
    chckbxSolution403.addMouseListener(new MouseAdapter() {
      @Override
      public void mouseClicked(MouseEvent arg0) {
        Setting.solution403 = chckbxSolution403.isSelected();
      }
    });
    
    //当收藏时保存
    chckbxSaveWhenLike.addMouseListener(new MouseAdapter() {
      @Override
      public void mouseClicked(MouseEvent arg0) {
        Setting.enableSaveWhenLike = chckbxSaveWhenLike.isSelected();
      }
    });
    
    //当更新时保存
    chckbxSaveWhenUpdate.addMouseListener(new MouseAdapter() {
      @Override
      public void mouseClicked(MouseEvent arg0) {
        Setting.enableSaveWhenUpdate = chckbxSaveWhenUpdate.isSelected();
      }
    });
    
    //自动保存开关
    chckbxAutoSave.addMouseListener(new MouseAdapter() {
      @Override
      public void mouseClicked(MouseEvent arg0) {
        Setting.enableAutoSave = chckbxAutoSave.isSelected();
        if (Setting.enableAutoSave) {
          String time = 
              JOptionPane.showInputDialog("多久保存一次? (单位为: 分)", Setting.saveTime);
          Setting.saveTime = Integer.parseInt(time);
        }
      }
    });
    
    //刷新配置设定
    btnRefreshSetting.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        refreshSetting();
      }
    });
    
    //保存设定
    btnSaveSetting.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        Setting.save();
      }
    });
    
  }
  
  private static void addPopup(Component component, final JPopupMenu popup) {
    component.addMouseListener(new MouseAdapter() {
      public void mousePressed(MouseEvent e) {
        if (e.isPopupTrigger()) {
          showMenu(e);
        }
      }

      public void mouseReleased(MouseEvent e) {
        if (e.isPopupTrigger()) {
          showMenu(e);
        }
      }

      private void showMenu(MouseEvent e) {
        popup.show(e.getComponent(), e.getX(), e.getY());
      }
    });
  }
  
  private static File chooseFile() {
    JFileChooser chooser = new JFileChooser();
    if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
      return chooser.getSelectedFile();
    }
    return null;
  }
  
  private void setInfoText(String text) {
    new Timer().schedule(new TimerTask() {

      @Override
      public void run() {
        lblInfo.setText(text);
      }
      
    }, 150L);
  }
  
  private void refreshSetting() {
    chckbxAutoSave.setSelected(Setting.enableAutoSave);
    chckbxSaveWhenUpdate.setSelected(Setting.enableSaveWhenUpdate);
    chckbxSaveWhenLike.setSelected(Setting.enableSaveWhenLike);
    chckbxSolution403.setSelected(Setting.solution403);
  }
}

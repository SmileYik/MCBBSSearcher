package tk.miskyle.mcbbssearcher.form;

import java.awt.EventQueue;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.Component;
import java.awt.Desktop;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.JPopupMenu;
import javax.swing.SwingConstants;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
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
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import tk.miskyle.mcbbssearcher.data.Setting;
import tk.miskyle.mcbbssearcher.data.favorite.Favorite;
import tk.miskyle.mcbbssearcher.data.favorite.FavoriteItem;
import tk.miskyle.mcbbssearcher.data.history.History;
import tk.miskyle.mcbbssearcher.forum.ForumManager;
import tk.miskyle.mcbbssearcher.forum.ForumSaver;
import tk.miskyle.mcbbssearcher.forum.data.Item;
import tk.miskyle.mcbbssearcher.forum.data.SubSection;
import tk.miskyle.mcbbssearcher.forum.update.ForumUpdate;

public class MainForm {
  private static JLabel lblInfo;
  
  private JFrame frmMcbbssearcherVBy;
  private JTextField tfSearchBox;
  private JTable tableItems;
  private JTabbedPane tabbedPane;
  private JScrollPane scrollPane11;
  private JTree treeForum;
  private JList<String> listLike;
  private DefaultTableModel tableItemsModel;
  private DefaultListModel<String> favoriteList;
  private JButton btnSearch;
  private JButton btnStopUpdate;
  private JButton btnShowHistory;
  private JProgressBar progressBar;

  //Setting
  private JPanel panel;
  private JButton btnSaveAllForum;
  private JButton btnMcbbsUserForm;
  private JButton btnRefreshSetting;
  private JButton btnSaveSetting;
  private JCheckBox chckbxSaveWhenLike;
  private JCheckBox chckbxSaveWhenUpdate;
  private JCheckBox chckbxSolution403;
  private JCheckBox chckbxAutoSave;
  
  //tree menu
  private JPopupMenu treeFormMenu;
  private JButton btnStoreOut;
  private JButton btnLoadIn;
  private JButton tfmSelect;
  private JButton tfmUpdate;
  
  //table menu
  private JPopupMenu tableMeum;
  private JButton btnAddToFavorite;
  private JButton btnItemBrower;
  
  //like menu
  private JPopupMenu menuLikeList;
  private JButton btnCreateLikeList;
  private JButton btnDeleteLikeList;
  private JButton btnRenameLikeList;
  private JButton btnShowLikeList;
  private JButton btnOutputLikeList;
  private JButton btnInputLikeList;
  
  private HashMap<String, Item> items = new HashMap<>();
  private HashMap<String, Item> searchedItem;
  private String selectedSubSection;
  private String selectedSection;
  private boolean favorite = false;
  private boolean history = false;

  
  /**
   * Launch the application.
   */
  public static void main() {
    EventQueue.invokeLater(new Runnable() {
      public void run() {
        try {
          MainForm window = new MainForm();
          window.frmMcbbssearcherVBy.setVisible(true);
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

    setupForumList();
    setupListener();
    refreshSetting();
    showFavorite();
  }

  /**
   * Initialize the contents of the frame.
   */
  private void initialize() {
    frmMcbbssearcherVBy = new JFrame();
    frmMcbbssearcherVBy.setTitle("McbbsSearcher  v1.0.0  by: miSkYle");
    frmMcbbssearcherVBy.setBounds(100, 100, 1035, 533);
    frmMcbbssearcherVBy.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frmMcbbssearcherVBy.getContentPane().setLayout(null);
    
    lblInfo = new JLabel("");
    lblInfo.setBounds(0, 468, 1017, 27);
    lblInfo.setHorizontalAlignment(SwingConstants.CENTER);
    frmMcbbssearcherVBy.getContentPane().add(lblInfo);
    tabbedPane = new JTabbedPane(JTabbedPane.TOP);
    tabbedPane.setBounds(0, 13, 199, 457);
    frmMcbbssearcherVBy.getContentPane().add(tabbedPane);
    
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
    favoriteList = new DefaultListModel<>();
    listLike.setModel(favoriteList);
    
    menuLikeList = new JPopupMenu();
    addPopup(listLike, menuLikeList);
    
    btnShowLikeList = new JButton("查看");
    menuLikeList.add(btnShowLikeList);
    
    btnOutputLikeList = new JButton("导出");
    menuLikeList.add(btnOutputLikeList);
    
    btnInputLikeList = new JButton("导入");
    menuLikeList.add(btnInputLikeList);
    
    btnCreateLikeList = new JButton("创建");
    menuLikeList.add(btnCreateLikeList);
    
    btnDeleteLikeList = new JButton("删除");
    menuLikeList.add(btnDeleteLikeList);
    
    btnRenameLikeList = new JButton("更名");
    menuLikeList.add(btnRenameLikeList);
    
    scrollPane11 = new JScrollPane();
    tabbedPane.addTab("设定", null, scrollPane11, null);
    
    panel = new JPanel();
    scrollPane11.setViewportView(panel);
    panel.setLayout(null);
    
    btnSaveAllForum = new JButton("保存所有论坛数据");
    btnSaveAllForum.setBounds(10, 10, 172, 32);
    panel.add(btnSaveAllForum);
    
    btnMcbbsUserForm = new JButton("Mcbbs 用户设定");
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
    frmMcbbssearcherVBy.getContentPane().add(scrollPane2);
    
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
    
    tableMeum = new JPopupMenu();
    addPopup(tableItems, tableMeum);
    
    btnItemBrower = new JButton("浏览");
    tableMeum.add(btnItemBrower);
    
    btnAddToFavorite = new JButton("收藏");
    tableMeum.add(btnAddToFavorite);
    scrollPane2.setViewportView(tableItems);
    
    JMenuBar menuBar = new JMenuBar();
    menuBar.setBounds(201, 10, 813, 27);
    frmMcbbssearcherVBy.getContentPane().add(menuBar);
    
    JMenu mnItemMenu = new JMenu(" . . . ");
    menuBar.add(mnItemMenu);
    
    btnStopUpdate = new JButton("停止更新");
    mnItemMenu.add(btnStopUpdate);
    
    btnShowHistory = new JButton("历史记录");
    mnItemMenu.add(btnShowHistory);
    
    tfSearchBox = new JTextField();
    menuBar.add(tfSearchBox);
    tfSearchBox.setColumns(10);
    
    btnSearch = new JButton("搜索");
    menuBar.add(btnSearch);
    
    progressBar = new JProgressBar();
    progressBar.setBounds(0, 468, 1017, 27);
    frmMcbbssearcherVBy.getContentPane().add(progressBar);
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
    favorite = false;
    history = false;
    tableItems.setToolTipText("");
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
    
    //选择一个条目并打开
    btnItemBrower.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent arg0) {
        tableMeum.setVisible(false);
        if (tableItems.getSelectedRow() == -1
            || tableItems.getSelectedRows().length != 1) {
          return;
        }
        Item item = 
            items.get(tableItems.getModel().getValueAt(tableItems.getSelectedRow(), 0));
        browse(item);
      }
    });
    
    //添加到收藏.
    btnAddToFavorite.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        tableMeum.setVisible(false);
        if (favorite) {
          JOptionPane.showMessageDialog(frmMcbbssearcherVBy, "这已经是在收藏夹里了, 不能重复选择收藏!");
          return;
        }
        if (tableItems.getSelectedRow() == -1
            || tableItems.getSelectedRows().length != 1) {
          return;
        }
        Item item = 
            items.get(tableItems.getModel().getValueAt(tableItems.getSelectedRow(), 0));
        FavoriteSelectForm.show(item);
        if (Setting.enableSaveWhenLike) {
          Favorite.save();
        }
      }
    });
    
    //双击选择一个项目.
    tableItems.addMouseListener(new MouseAdapter() {
      @Override
      public void mouseClicked(MouseEvent e) {
        if (e.getClickCount() == 1
            && tableItems.getSelectedRow() != -1
            && tableItems.getSelectedRows().length == 1) {
          String name = (String) tableItems.getModel().getValueAt(tableItems.getSelectedRow(), 0);
          if (favorite) {
            FavoriteItem item = Favorite.getNowShowItems().get(name);
            String text = item.getAttachString() + " ---" + item.getSaveTimeString(); 
            setInfoText(text);
            tableItems.setToolTipText(text);
          } else if (history) {
            String text = History.getInfo(items.get(name).getName());
            setInfoText(text);
            tableItems.setToolTipText(text);
          }
          return;
        } else if (e.getClickCount() != 2
            || tableItems.getSelectedRow() == -1
            || tableItems.getSelectedRows().length != 1) {
          return;
        }
        
        Item item = 
            items.get(tableItems.getModel().getValueAt(tableItems.getSelectedRow(), 0));
        browse(item);
      }
    });
    
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
          if (Setting.enableSaveWhenUpdate) {
            ForumSaver.saveForum();
            History.save();
          }
        }).start();
        
        new Timer().schedule(new TimerTask() {
          
          @Override
          public void run() {
            if (!ForumUpdate.isUpdate()) {
              setInfoText("更新终止");
              this.cancel();
              return;
            } else if (ForumUpdate.isUpdateFinished()) {
              setInfoText("更新完成!");
              progressBar.setValue(progressBar.getMaximum());
              this.cancel();
              return;
            }
            
            //更新更新进度信息
            int size = subS.getItems().size();
            int aimSize = subS.getPageAmount() * 28;
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
          JOptionPane.showMessageDialog(frmMcbbssearcherVBy, "未选中文件!");
        } else {
          ForumSaver.saveSubForumToFile(aim, subS, section);
          JOptionPane.showMessageDialog(frmMcbbssearcherVBy, "导出成功!");
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
          JOptionPane.showMessageDialog(frmMcbbssearcherVBy, "未选中文件!");
        } else {
          SubSection sub = ForumSaver.loadSubSection(aim);
          JOptionPane.showMessageDialog(frmMcbbssearcherVBy, "导入成功!\n"
              + "导入至子版块: " + sub.getName());
          setupForumList();
        }
        
      }
    });
    
    //停止更新
    btnStopUpdate.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        ForumUpdate.stopUpdate();
        if (Setting.enableSaveWhenUpdate) {
          ForumSaver.saveForum();
          History.save();
        }
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
        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
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
        Favorite.save();
        History.save();
        JOptionPane.showMessageDialog(frmMcbbssearcherVBy, "保存成功!");
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
              JOptionPane.showInputDialog(
                  frmMcbbssearcherVBy, "多久保存一次? (单位为: 分)", Setting.saveTime);
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
        JOptionPane.showMessageDialog(frmMcbbssearcherVBy, "保存成功!");
      }
    });
    
    //打开MCBBS用户设定窗口
    btnMcbbsUserForm.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        McbbsUserForm.showUsers(false);
      }
    });
    
    //双击收藏夹
    listLike.addMouseListener(new MouseAdapter() {
      @Override
      public void mouseClicked(MouseEvent e) {
        if (e.getClickCount() != 2
            || listLike.isSelectionEmpty()
            || listLike.getSelectedValue().isEmpty()) {
          return;
        }
        Favorite.setNowShow(listLike.getSelectedValue());
        showFavoriteList();
      }
    });
    
    //显示收藏夹列表内容
    btnShowLikeList.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent arg0) {
        menuLikeList.setVisible(false);
        if (listLike.isSelectionEmpty()
            || listLike.getSelectedValue().isEmpty()) {
          return;
        }
        Favorite.setNowShow(listLike.getSelectedValue());
        showFavoriteList();
      }
    });
    
    //创建收藏夹
    btnCreateLikeList.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        menuLikeList.setVisible(false);
        String name = JOptionPane.showInputDialog(frmMcbbssearcherVBy, "请输入收藏夹名");
        if (name == null || name.isEmpty()) {
          JOptionPane.showMessageDialog(frmMcbbssearcherVBy, "输入名字不合法");
          return;
        }
        if (Favorite.getFavoriteList().containsKey(name)) {
          JOptionPane.showMessageDialog(frmMcbbssearcherVBy, "该收藏夹已存在");
          return;
        }
        Favorite.createFavoriteList(name);
        showFavorite();
        if (Setting.enableSaveWhenLike) {
          Favorite.save();
        }
      }
    });
    
    //删除收藏夹
    btnDeleteLikeList.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        menuLikeList.setVisible(false);
        if (listLike.isSelectionEmpty()
            || listLike.getSelectedValue().isEmpty()) {
          JOptionPane.showMessageDialog(frmMcbbssearcherVBy, "没有选择一个收藏夹");
          return;
        }
        String name = listLike.getSelectedValue();
        
        String code = JOptionPane.showInputDialog(frmMcbbssearcherVBy, 
                  "你确认要删除\"" + name + "\"收藏夹吗?"
                + "\n这将会删除该收藏夹内所有条目."
                + "\n输入该收藏夹名字以进行确认删除操作!");
        if (code != null && code.equalsIgnoreCase(name)) {
          Favorite.getFavoriteList().remove(name);
          showFavorite();
          JOptionPane.showMessageDialog(frmMcbbssearcherVBy, "删除成功!");
        } else {
          JOptionPane.showMessageDialog(frmMcbbssearcherVBy, "取消删除!");
        }
        favorite = false;
        if (Setting.enableSaveWhenLike) {
          Favorite.save();
        }
      }
    });
    
    //重命名收藏夹
    btnRenameLikeList.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        menuLikeList.setVisible(false);
        if (listLike.isSelectionEmpty()
            || listLike.getSelectedValue().isEmpty()) {
          JOptionPane.showMessageDialog(frmMcbbssearcherVBy, "没有选择一个收藏夹");
          return;
        }
        String old = listLike.getSelectedValue();
        String name = 
            JOptionPane.showInputDialog(frmMcbbssearcherVBy, "请输入收藏夹名", old);
        if (name == null || name.isEmpty()) {
          JOptionPane.showMessageDialog(frmMcbbssearcherVBy, "输入名字不合法");
          return;
        }
        if (Favorite.getFavoriteList().containsKey(name)) {
          JOptionPane.showMessageDialog(frmMcbbssearcherVBy, "该收藏夹已存在");
          return;
        } else if (name.equalsIgnoreCase(old)) {
          return;
        }
        favorite = false;
        Favorite.rename(old, name);
        showFavorite();
        JOptionPane.showMessageDialog(frmMcbbssearcherVBy, "重命名成功!");
        if (Setting.enableSaveWhenLike) {
          Favorite.save();
        }
      }
    });
    
    //导出收藏夹到文件.
    btnOutputLikeList.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        menuLikeList.setVisible(false);
        if (listLike.isSelectionEmpty()
            || listLike.getSelectedValue().isEmpty()) {
          JOptionPane.showMessageDialog(frmMcbbssearcherVBy, "没有选择一个收藏夹");
          return;
        }
        String name = listLike.getSelectedValue();
        File path = chooseFile();
        if (path == null) {
          JOptionPane.showMessageDialog(frmMcbbssearcherVBy, "选择文件过程中出现问题.");
          return;
        }
        Favorite.save(path, name);
        JOptionPane.showMessageDialog(frmMcbbssearcherVBy, "收藏夹保存至\n" + path);
      }
    });
    
    //导入收藏夹
    btnInputLikeList.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        menuLikeList.setVisible(false);
        File path = chooseFile();
        if (path == null) {
          JOptionPane.showMessageDialog(frmMcbbssearcherVBy, "选择文件过程中出现问题.");
          return;
        }
        Favorite.load(path);
        showFavorite();
        JOptionPane.showMessageDialog(frmMcbbssearcherVBy, "读取完毕!");
        if (Setting.enableSaveWhenLike) {
          Favorite.save();
        }
      }
    });
    
    //显示历史记录
    btnShowHistory.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        showHistory();
      }
    });
  }
  
  private void showHistory() {
    favorite = false;
    history = true;
    items.clear();
    History.getItems().forEach((k, v) -> {
      v.getItem().setTitle(v.getItem().getName());
      items.put(v.getItem().getTitle(), v.getItem());
    });
    resetItems();
  }
  
  /**
   * 显示收藏夹内条目.
   */
  private void showFavoriteList() {
    favorite = true;
    history = false;
    items.clear();
    Favorite.getNowShowItems().forEach((k, v) -> {
      items.put(k, v.getItem());
    });
    resetItems();
  }
  
  /**
   * 显示收藏夹列表.
   */
  private void showFavorite() {
    favoriteList.clear();
    Favorite.getFavoriteList().keySet().forEach(name -> {
      favoriteList.addElement(name);
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
  
  /**
   * modify info.
   * @param text info.
   */
  public static void setInfoText(String text) {
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
  
  /**
   * 浏览网页.
   * @param item 目的条目
   */
  public void browse(Item item) {
    if (favorite) {
      History.look(item, "收藏夹", "收藏夹", "收藏夹中浏览: " + Favorite.getNowShow());
    } else if (history) {
      History.look(item, "历史记录", "历史记录", "历史记录中浏览");
    } else {
      History.look(item, selectedSection, selectedSubSection, "正常浏览");
    }
    
    URI uri;
    if (Setting.solution403 && item.getTid() != null && !item.getTid().isEmpty()) {
      uri = URI.create("https://www.mcbbs.net/thread-" + item.getTid() + "-1-1.html");
    } else {
      uri = URI.create(item.getUrlString());
    }
    Desktop dp = Desktop.getDesktop();
    if (dp.isSupported(Desktop.Action.BROWSE)) {
      try {
        dp.browse(uri);
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }
}

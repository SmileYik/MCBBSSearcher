package com.github.miSkYle.mcbbsshearcher;

import java.awt.EventQueue;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JMenu;
import javax.swing.JList;
import javax.swing.JTextField;
import javax.swing.tree.DefaultTreeModel;

import com.github.miSkYle.mcbbsshearcher.data.Forum;
import com.github.miSkYle.mcbbsshearcher.data.Item;
import com.github.miSkYle.mcbbsshearcher.data.Page;
import com.github.miSkYle.mcbbsshearcher.data.SubForum;
import com.github.miSkYle.mcbbsshearcher.data.TopForum;

import javax.swing.tree.DefaultMutableTreeNode;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JLabel;
import javax.swing.ListSelectionModel;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JPopupMenu;

import java.awt.Component;
import javax.swing.JProgressBar;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.JCheckBox;
import javax.swing.SwingConstants;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

public class McbbsShearcher {

	private static boolean inSearch = false,stopUpdate = false;
	private static HashMap<String, Item> items = new HashMap<>();
	private static DefaultTableModel listItems;
	private static HashMap<String, Item> searchItems = new HashMap<>();
	
	private static JProgressBar progressBar = new JProgressBar();
	private static JLabel forum_label = new JLabel("  No Thing  ");
	public static JLabel fourmItemSizeInfo = new JLabel("");
	
	private JFrame frmMcbbsShearcher;
	private static JTextField searchText;
	private JTextField saveTickNumber;
	private JTable table = new JTable();
	
	private TableRowSorter<DefaultTableModel> trsorter;
	
	public static void changeProgressBar(int num){
		progressBar.setValue(progressBar.getValue()+num);
		forum_label.setText("  "+progressBar.getValue()+"/"+progressBar.getMaximum()+" - "+String.format("%.2f", ((double)progressBar.getValue()/(double)progressBar.getMaximum()*100D))+"%  ");
	}
	
	public static boolean isStopUpdate(){
		return stopUpdate;
	}

	/**
	 * Launch the application.
	 * @throws IOException 
	 * @throws FileNotFoundException 
	 * @throws UnsupportedEncodingException 
	 */
	public static void main(String[] args) throws Exception {
		
		Forum.init();

		
		
//		 EventQueue.invokeLater(new Runnable() { public void run() { try {
//		 McbbsShearcher window = new McbbsShearcher();
//		 window.frmMcbbsShearcher.setVisible(true); } catch (Exception e) {
//		 e.printStackTrace(); } } }); Forum.over=false;
		 
		//Forum.over=false;
		
		 Timer timer = new Timer();
	        timer.schedule(new TimerTask() {
	        	long time = 0;
	            public void run() {
	            	if(Forum.over){
	            		EventQueue.invokeLater(new Runnable() {
	            			public void run() {
	            				try {
	            					McbbsShearcher window = new McbbsShearcher();
	            					window.frmMcbbsShearcher.setVisible(true);
	            				} catch (Exception e) {
	            					e.printStackTrace();
	            				}
	            			}
	            		});
	            		cancel();
	            	}
	            	time++;
	            	if(time>300000){
	            		JOptionPane.showMessageDialog(null, "初始化超时!");
	            		cancel();
	            	}
	            }  
	        }, 50,1000);
		

	}

	/**
	 * Create the application.
	 */
	public McbbsShearcher() {
		listItems = getDefaultTable();
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmMcbbsShearcher = new JFrame();
		frmMcbbsShearcher.setTitle("Mcbbs Shearcher -- Made by miSkYle");
		frmMcbbsShearcher.setBounds(100, 100, 939, 470);
		//frame.setBounds(100, 100, 700, 450);
		frmMcbbsShearcher.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmMcbbsShearcher.getContentPane().setLayout(null);
		
		
		JPanel scrollPane_1 = new JPanel();
		scrollPane_1.setBounds(144, 10, 763, 391);
		frmMcbbsShearcher.getContentPane().add(scrollPane_1);
		scrollPane_1.setLayout(null);
		
		JMenuBar menuBar = new JMenuBar();
		menuBar.setBounds(0, 0, 763, 28);
		scrollPane_1.add(menuBar);
		
		JMenu mnNewMenu = new JMenu("...");
		menuBar.add(mnNewMenu);
		
		JButton stopUpdate_1 = new JButton("    停止更新    ");
		mnNewMenu.add(stopUpdate_1);
		
		JButton viewAllPosts = new JButton("查看所有帖子");
		mnNewMenu.add(viewAllPosts);
		
		menuBar.add(forum_label);
		
		searchText = new JTextField();
		menuBar.add(searchText);
		searchText.setColumns(10);
		
		JButton search = new JButton("Search");
		menuBar.add(search);
		
		JScrollPane scrollPane_3 = new JScrollPane();
		scrollPane_3.setBounds(0, 28, 763, 363);
		scrollPane_1.add(scrollPane_3);
		
		viewAllPosts.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				for(TopForum top : Forum.topForum)
					for(SubForum sub : top.getSubs())
						updateItems(sub, false);
				updateListItem();
			}
		});
		
		search.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(searchText.getText().isEmpty()) {
					inSearch = false;
				}else{
					searchItems.clear();
					inSearch = true;
					String[] keyWords = searchText.getText().toUpperCase().split(" ");
					
					for(Item item : items.values()) {
						boolean in = true;
						for(String keyWord:keyWords) {
							if(keyWord.contains("AUTHER:")) {
								String auther = keyWord.split(":")[1].toUpperCase();
								if(item.getAuther()!= null && item.getAuther().equalsIgnoreCase(auther)) {
									in = true;
									break;
								}else {
									in = false;
								}
							}else {
								if(!item.getTitle().isEmpty()&&!item.getTitle().toUpperCase().contains(keyWord)){
									in = false;
									break;
								}				
							}
						}
						if(in)McbbsShearcher.searchItems.put(item.getTitle(), item);
					}
					
//					
//					for(String key : McbbsShearcher.items.keySet()){
//						boolean in = true;
//						for(String keyWord : keyWords){
//							if(!key.toUpperCase().contains(keyWord)){
//								in = false;
//								break;
//							}								
//						}
//						if(in)McbbsShearcher.searchItems.put(key, items.get(key));
//					}
				}
				updateListItem();
			}
		});
		
		
		searchText.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent arg0) {
				if(search.isEnabled() && arg0.getKeyCode() == KeyEvent.VK_ENTER){
					if(searchText.getText().isEmpty()) {
						inSearch = false;
					}else{
						searchItems.clear();
						inSearch = true;
						String[] keyWords = searchText.getText().toUpperCase().split(" ");
						
						for(Item item : items.values()) {
							boolean in = true;
							for(String keyWord:keyWords) {
								if(keyWord.contains("AUTHER:")) {
									String auther = keyWord.split(":")[1].toUpperCase();
									if(item.getAuther()!= null && item.getAuther().equalsIgnoreCase(auther)) {
										in = true;
										break;
									}else {
										in = false;
									}
								}else {
									if(!item.getTitle().isEmpty()&&!item.getTitle().toUpperCase().contains(keyWord)){
										in = false;
										break;
									}				
								}
							}
							if(in)McbbsShearcher.searchItems.put(item.getTitle(), item);
						}
					}
					updateListItem();
				}
			}
		});
		
		stopUpdate_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				McbbsShearcher.stopUpdate = true;
			}
		});
		
		fourmItemSizeInfo.setHorizontalAlignment(SwingConstants.CENTER);
		fourmItemSizeInfo.setBounds(10, 408, 897, 15);
		frmMcbbsShearcher.getContentPane().add(fourmItemSizeInfo);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 10, 124, 391);
		frmMcbbsShearcher.getContentPane().add(scrollPane);
		
		JTree forum = new JTree();
		forum.setRootVisible(false);
		forum.setModel(getForums());
		scrollPane.setViewportView(forum);
		
		JPopupMenu popupMenu = new JPopupMenu();
		addPopup(forum, popupMenu);
		
		JButton viewSubForumPosts = new JButton("查看版块内容");
		popupMenu.add(viewSubForumPosts);
		
		JButton updateSubForumPosts = new JButton("更新版块内容");
		popupMenu.add(updateSubForumPosts);
		
		JButton addSubForum = new JButton("添加新的板块");
		popupMenu.add(addSubForum);
		
		JButton addFavorite = new JButton("添加喜爱板块");
		popupMenu.add(addFavorite);
		
		JButton deleteForum = new JButton("删除这个板块");
		popupMenu.add(deleteForum);
		
		JMenuBar menuBar_1 = new JMenuBar();
		scrollPane.setColumnHeaderView(menuBar_1);
		
		JMenu mnSetting = new JMenu("Setting");
		menuBar_1.add(mnSetting);
		
		//saveData
		{
			
			JButton btnDonate = new JButton("Donate | 捐助");
			btnDonate.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					Donate.show(frmMcbbsShearcher);
				}
			});
			mnSetting.add(btnDonate);
			
			JMenu mnSaveSetting = new JMenu("Save Setting");
			mnSetting.add(mnSaveSetting);
			
			JCheckBox saveUpdate = new JCheckBox("板块更新完成时保存");
			saveUpdate.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					Data.saveUpdate = saveUpdate.isSelected();
				}
			});
			
			JButton btnSaveAllData = new JButton("Save All Data");
			mnSaveSetting.add(btnSaveAllData);
			
			btnSaveAllData.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					try {
						Forum.saveAllData();
						JOptionPane.showMessageDialog(frmMcbbsShearcher, "保存成功!");
					} catch (Exception e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
						JOptionPane.showMessageDialog(frmMcbbsShearcher, "保存失败!");
					}
				}
			});
			
			JButton button = new JButton("刷新设定选项");
			mnSaveSetting.add(button);
			
			JLabel label = new JLabel("--------------------");
			mnSaveSetting.add(label);
			mnSaveSetting.add(saveUpdate);
			
			JCheckBox saveLike = new JCheckBox("喜爱指定板块时保存");
			saveLike.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					Data.saveLike = saveLike.isSelected();
				}
			});
			mnSaveSetting.add(saveLike);
			
			JCheckBox problem403 = new JCheckBox("403  解决方案");
			problem403.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					Data.problem403 = problem403.isSelected();
				}
			});
			mnSaveSetting.add(problem403);
			
			JCheckBox saveTick = new JCheckBox("  启用定时自动保存  ");
			saveTick.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent arg0) {
					saveTickNumber.setEditable(saveTick.isSelected());
					Data.saveTick = saveTick.isSelected();
					try {
						int min = Integer.parseInt(saveTickNumber.getText());
						Data.saveTickTime = min;
					} catch (NumberFormatException e) {
						Data.saveTickTime = 5;
						saveTickNumber.setText("5");
					}
					
					if(Data.saveTick)
						com.github.miSkYle.mcbbsshearcher.Timer.start(Data.saveTickTime);
				}
			});
			mnSaveSetting.add(saveTick);
			
			JLabel saveLabel = new JLabel("定时时间(分钟):");
			mnSaveSetting.add(saveLabel);
			
			saveTickNumber = new JTextField();
			saveTickNumber.addFocusListener(new FocusAdapter() {
				@Override
				public void focusLost(FocusEvent arg0) {
					try {
						int min = Integer.parseInt(saveTickNumber.getText());
						Data.saveTickTime = min;
					} catch (NumberFormatException e) {
						Data.saveTickTime = 5;
						saveTickNumber.setText("5");
					}
				}
			});
			saveTickNumber.setHorizontalAlignment(SwingConstants.CENTER);
			saveTickNumber.setText("5");
			saveTickNumber.setEditable(false);
			mnSaveSetting.add(saveTickNumber);
			saveTickNumber.setColumns(10);
			
			saveUpdate.setSelected(Data.saveUpdate);
			saveLike.setSelected(Data.saveLike);
			saveTick.setSelected(Data.saveTick);
			saveTickNumber.setEditable(Data.saveTick);
			saveTickNumber.setText(Data.saveTickTime+"");
			if(Data.saveTick)
				com.github.miSkYle.mcbbsshearcher.Timer.start(Data.saveTickTime);
			
			
			button.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					saveUpdate.setSelected(Data.saveUpdate);
					saveLike.setSelected(Data.saveLike);
					saveTick.setSelected(Data.saveTick);
					problem403.setSelected(Data.problem403);
					saveTickNumber.setEditable(Data.saveTick);
					saveTickNumber.setText(Data.saveTickTime+"");
				}
			});
		}
		
		progressBar.setBounds(10, 407, 897, 16);
		frmMcbbsShearcher.getContentPane().add(progressBar);
		
		JList<String> items_123 = new JList<String>();
		items_123.setBounds(912, 434, 304, 9);
		frmMcbbsShearcher.getContentPane().add(items_123);
		items_123.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table.setBounds(0, 0, 761, 0);
		//frmMcbbsShearcher.getContentPane().add(table);
		//scrollPane_1.setViewportView(scrollPane_2);
		
		table.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				if(arg0.getClickCount()==2) {
					Item item = getSelectItem();
					if(item == null || item.getTitle() == null) {
//						TableRowSorter<DefaultTableModel> trsorter = new TableRowSorter<DefaultTableModel>(listItems);
//						trsorter.setSortable(0, false);
//						trsorter.setSortable(1, false);
//						trsorter.setSortable(3, false);
//						trsorter.setSortable(4, false);
//						trsorter.setSortable(5, false);
//						trsorter.setSortable(6, false);
//						trsorter.setSortable(2, false);
//						if(table.getSelectedColumn() == 2) {
//							trsorter.setSortable(2, true);
//							if(sort2 == 0) {
//								trsorter.setRowFilter(filter);
//								sort2 = 1;
//							}
//							
//							
//							
//						}else if(table.getSelectedColumn() == 4) {
//							trsorter.setSortable(4, true);
//						}
//						table.setRowSorter(trsorter);
					}else {
						try { 
							String url = item.getUrlString();
							if(Data.problem403) {
								String tid = url.split("&tid=")[1].split("&")[0];
								url = "https://www.mcbbs.net/thread-"+tid+"-1-1.html";
							}
							if(!forum_label.getText().contains("历史记录"))
								addHistory(item.getTitle(), url);
							java.net.URI uri = java.net.URI.create(url); 
							java.awt.Desktop dp = java.awt.Desktop.getDesktop(); 
							if (dp.isSupported(java.awt.Desktop.Action.BROWSE)) { 
								dp.browse(uri);// 获取系统默认浏览器打开链接 
							} 
						} catch (java.lang.NullPointerException e1) { 
							e1.printStackTrace(); 
						} catch (java.io.IOException e1) { 
							JOptionPane.showMessageDialog(frmMcbbsShearcher, "打开网页失败!");
							e1.printStackTrace(); 
						} 
					}
				}
				
				
				//Sort
				//TableRowSorter<DefaultTableModel> trsorter = new TableRowSorter<DefaultTableModel>(listItems);
				
				//table.setRowSorter(trsorter);
				
				
			}
		});
		table.setModel(listItems);
		table.getColumnModel().getColumn(0).setPreferredWidth(615);
		table.setCellSelectionEnabled(true);
		scrollPane_3.setViewportView(table);
		
		JPopupMenu itemPopMenu = new JPopupMenu();
		addPopup(table, itemPopMenu);
		
		JButton openWithDefaultBrower = new JButton("用默认浏览器打开");
		itemPopMenu.add(openWithDefaultBrower);
		
		JButton preView = new JButton("             预览             ");
		itemPopMenu.add(preView);
		
		JButton likePost = new JButton("             喜爱             ");
		itemPopMenu.add(likePost);
		
		openWithDefaultBrower.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				itemPopMenu.setVisible(false);
				Item item = getSelectItem();
				if(item == null)return;
				try { 
					String url = item.getUrlString(); 
					if(Data.problem403) {
						String tid = url.split("&tid=")[1].split("&")[0];
						url = "https://www.mcbbs.net/thread-"+tid+"-1-1.html";
					}
					if(!forum_label.getText().contains("历史记录"))
						addHistory(item.getTitle(), url);
					java.net.URI uri = java.net.URI.create(url); 
					java.awt.Desktop dp = java.awt.Desktop.getDesktop(); 
					if (dp.isSupported(java.awt.Desktop.Action.BROWSE)) { 
						dp.browse(uri);// 获取系统默认浏览器打开链接 
					} 
				} catch (java.lang.NullPointerException e1) { 
					e1.printStackTrace(); 
				} catch (java.io.IOException e1) { 
					JOptionPane.showMessageDialog(frmMcbbsShearcher, "打开网页失败!");
					e1.printStackTrace(); 
				} 
			}
		});
		
		preView.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				itemPopMenu.setVisible(false);
				Item item = getSelectItem();
				if(item == null)return;
				try { 
					new WebFrome(item.getUrlString());
				} catch (java.lang.NullPointerException e1) { 
					e1.printStackTrace(); 
				}
			}
		});
		
		likePost.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent actionevent) {
				itemPopMenu.setVisible(false);
				Item item = getSelectItem();
				if(item == null)return;
				ChoseFavoriteDialog.show(frmMcbbsShearcher, item.getTitle(),item.getUrlString());
			}
		});
		
		deleteForum.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				popupMenu.setVisible(false);
				if(forum.getLastSelectedPathComponent()!=null && ((DefaultMutableTreeNode)forum.getLastSelectedPathComponent()).getChildCount()<1){
					String key = forum.getSelectionPath().getPath()[2].toString();
					if(key ==null ||key.isEmpty())key="确定删除";
					String opinion = JOptionPane.showInputDialog(frmMcbbsShearcher, "真的要删除这个子版块吗? 删除后无法进行恢复!\n请输入 \""+key+"\" 确定删除!","");
					
					if(opinion.equalsIgnoreCase(key)){
						 Forum.getTopForum(forum.getSelectionPath().getPath()[1].toString()).getSubs().remove(Forum.getTopForum(forum.getSelectionPath().getPath()[1].toString()).getSubForum(forum.getSelectionPath().getPath()[2].toString()));
						 forum.setModel(getForums());
						 JOptionPane.showMessageDialog(frmMcbbsShearcher, "删除成功!");
					}
				}else if(forum.getLastSelectedPathComponent()!=null && ((DefaultMutableTreeNode)forum.getLastSelectedPathComponent()).getChildCount()>=1){
					String key = forum.getSelectionPath().getPath()[1].toString();
					if(key ==null ||key.isEmpty())key="确定删除";
					String opinion = JOptionPane.showInputDialog(frmMcbbsShearcher, "真的要删除这个父版块吗? 删除后其中的子版块也会删除且无法进行恢复!\n请输入 \""+key+"\" 确定删除!","");
					
					if(opinion.equalsIgnoreCase(key)){
						Forum.topForum.remove(Forum.getTopForum(forum.getSelectionPath().getPath()[1].toString()));
						 forum.setModel(getForums());
						 JOptionPane.showMessageDialog(frmMcbbsShearcher, "删除成功!");
					}
				}
				
			}
		});
		
		//添加喜爱板块
		addFavorite.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				popupMenu.setVisible(false);
				String subStr = JOptionPane.showInputDialog(frmMcbbsShearcher,"请输入子版块名称", "");
				if(subStr==null || subStr.isEmpty()){
					JOptionPane.showMessageDialog(frmMcbbsShearcher, "具有无效信息,添加失败!");
					return;
				}
				TopForum top = Forum.getTopForum("System");
				
				if(top==null){
					LinkedList<SubForum> subs = new LinkedList<>();
					subs.add(new SubForum(subStr, null));
					top = new TopForum("System", null, subs);
					Forum.topForum.add(top);
					forum.setModel(getForums());
				}else if(top.getSubForum(subStr)!=null){
					JOptionPane.showMessageDialog(frmMcbbsShearcher, "该喜爱板块已经存在!");
					return;
				}else if(top.getName().equalsIgnoreCase("System")){
					top.getSubs().add(new SubForum(subStr,subStr, null,1));
					forum.setModel(getForums());
				}else{
					top.getSubs().add(new SubForum(subStr, null));
					forum.setModel(getForums());
				}
				
			}
		});
		
		addSubForum.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				popupMenu.setVisible(false);
				String topStr = JOptionPane.showInputDialog(frmMcbbsShearcher,"请输入父板块名称", "");
				String subStr = JOptionPane.showInputDialog(frmMcbbsShearcher,"请输入子版块名称", "");
				String subUrl = JOptionPane.showInputDialog(frmMcbbsShearcher,"请输入子版块链接", "");
				if(topStr==null||subStr==null||subUrl==null||topStr.isEmpty() || subStr.isEmpty() || subUrl.isEmpty()){
					JOptionPane.showMessageDialog(frmMcbbsShearcher, "具有无效信息,添加失败!");
					return;
				}
				TopForum top = Forum.getTopForum(topStr);
				
				if(top==null){
					LinkedList<SubForum> subs = new LinkedList<>();
					subs.add(new SubForum(subStr, subUrl));
					top = new TopForum(topStr, null, subs);
					Forum.topForum.add(top);
					forum.setModel(getForums());
				}else if(top.getSubForum(subStr)!=null){
					JOptionPane.showMessageDialog(frmMcbbsShearcher, "该子版块已经存在!");
					return;
				}else if(top.getName().equalsIgnoreCase("System")){
					top.getSubs().add(new SubForum(subStr,subStr, subUrl,1));
					forum.setModel(getForums());
				}else{
					top.getSubs().add(new SubForum(subStr, subUrl));
					forum.setModel(getForums());
				}
			}
		});
		
		forum.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if(forum.getLastSelectedPathComponent()!=null && ((DefaultMutableTreeNode)forum.getLastSelectedPathComponent()).getChildCount()<1 && e.getClickCount()==2){
					SubForum sub = Forum.getTopForum(forum.getSelectionPath().getPath()[1].toString()).getSubForum(forum.getSelectionPath().getPath()[2].toString());
					if(sub == null){
						forum_label.setText("  "+forum.getSelectionPath().getPath()[2].toString()+"  Nothing  ");
						return;
					}
					if(sub.getSize()==-1){
						JOptionPane.showMessageDialog(frmMcbbsShearcher,"Can't view this, because you are not have premission.");
						return;
					}
					inSearch = false;
					forum_label.setText("  "+sub.getName()+"  ");
					updateItems(sub,true);
					updateListItem();
				}
			}
		});
		
		viewSubForumPosts.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(forum.getLastSelectedPathComponent()!=null && ((DefaultMutableTreeNode)forum.getLastSelectedPathComponent()).getChildCount()<1){
					popupMenu.setVisible(false);
					SubForum sub = Forum.getTopForum(forum.getSelectionPath().getPath()[1].toString()).getSubForum(forum.getSelectionPath().getPath()[2].toString());
					if(sub == null){
						forum_label.setText("  "+forum.getSelectionPath().getPath()[2].toString()+"  Nothing  ");
						return;
					}
					if(sub.getSize()==-1){
						JOptionPane.showMessageDialog(frmMcbbsShearcher,"Can't view this, because you are not have premission.");
						return;
					}
					inSearch = false;
					forum_label.setText("  "+sub.getName()+"  ");
					updateItems(sub,true);
					updateListItem();
				}
			}
		});
		
		updateSubForumPosts.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				popupMenu.setVisible(false);
				if(forum.getLastSelectedPathComponent()!=null && ((DefaultMutableTreeNode)forum.getLastSelectedPathComponent()).getChildCount()<1){
					SubForum sub = Forum.getTopForum(forum.getSelectionPath().getPath()[1].toString()).getSubForum(forum.getSelectionPath().getPath()[2].toString());
					if(sub == null){
						JOptionPane.showMessageDialog(frmMcbbsShearcher,"Update failed!");
						return;
					}
					if(sub.getSize()==-1){
						JOptionPane.showMessageDialog(frmMcbbsShearcher,"Can't view this, because you are not have premission.");
						return;
					}
					//JOptionPane.showMessageDialog(frame,"Please wait a moment!");
					progressBar.setMaximum(28*(sub.getSize()-1));
					progressBar.setMinimum(0);
					progressBar.setValue(0);
					table.setEnabled(false);
					forum.setEnabled(false);
					search.setEnabled(false);
					
					 Timer timer = new Timer();// 实例化Timer类  
				        timer.schedule(new TimerTask() {  
				            public void run() {
				            	McbbsShearcher.stopUpdate = false;
				                sub.updatePages();
								
				                progressBar.setValue(progressBar.getMaximum());
								inSearch = false;
								forum_label.setText("  "+sub.getName()+"  ");
								updateItems(sub,true);
								updateListItem();
								table.setEnabled(true);
								forum.setEnabled(true);
								search.setEnabled(true);
				            }  
				        }, 50);
				}
			}
		});
	}
	
	public DefaultTreeModel getForums(){
		//DefaultTreeModel dtm = new DefaultTreeModel(root)
		DefaultMutableTreeNode dmtn = new DefaultMutableTreeNode();
		for(TopForum top : Forum.topForum){
			DefaultMutableTreeNode topForum = new DefaultMutableTreeNode(top.getName());
			for(SubForum sub : top.getSubs())
				topForum.add(new DefaultMutableTreeNode(sub.getName(),false));
			dmtn.add(topForum);
		}
		if(Forum.getSystemTopForum()!=null){
			DefaultMutableTreeNode topForum = new DefaultMutableTreeNode(Forum.getSystemTopForum().getName());
			for(SubForum sub : Forum.getSystemTopForum().getSubs())
				topForum.add(new DefaultMutableTreeNode(sub.getName(),false));
			dmtn.add(topForum);			
		}
		
		return new DefaultTreeModel(dmtn);
	}
	
	public void updateListItem(){
		listItems = getDefaultTable();
		if(inSearch){
			for(Item key : McbbsShearcher.searchItems.values()) {
				if(key == null||key.getTitle() == null||key.getTitle().isEmpty())continue;
				listItems.addRow(new Object[] {key.getTitle(),key.getAuther(),key.getPostTimeString(),key.getReplayer(),key.getReplyTimeString(),key.getView(),key.getReply()});
			}
			fourmItemSizeInfo.setText("搜索到 "+McbbsShearcher.searchItems.size()+" 项相关条目.");
		}else{
		    for(Item key : McbbsShearcher.items.values()) {
		    	if(key == null||key.getTitle() == null||key.getTitle().isEmpty())continue;
		    	listItems.addRow(new Object[] {key.getTitle(),key.getAuther(),key.getPostTimeString(),key.getReplayer(),key.getReplyTimeString(),key.getView(),key.getReply()});	
		    }
		    fourmItemSizeInfo.setText("已加载 "+items.size()+" 项条目.");
		}
		table.setModel(listItems);
    	table.getColumnModel().getColumn(0).setPreferredWidth(615);
    	//if(listItems.getRowCount()<1)return;
		trsorter = new TableRowSorter<DefaultTableModel>(listItems);
		table.setRowSorter(trsorter);
	}
	
	public void updateItems(SubForum subForum,boolean isClear){
		
		if(isClear)items.clear();
		
		
		progressBar.setMaximum(28*(subForum.getSize()-1));
		progressBar.setMinimum(0);
		progressBar.setValue(0);
		for(Page page : subForum.getPages())
			for(Item item : page.getItem()){
				items.put(item.getTitle(), item);
				//changeProgressBar(1);
			}
	     progressBar.setValue(progressBar.getMaximum());
		
	     fourmItemSizeInfo.setText("已加载 "+listItems.getRowCount()+" 项条目.");
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
	
	private void addHistory(String name,String urlString){
		SimpleDateFormat sdf = new SimpleDateFormat("YYYY-MM-D HH:mm");
		name = "["+sdf.format(new Date())+"]"+forum_label.getText()+">  "+name;
		SubForum sub = Forum.getSystemTopForum().getSubForum("历史记录");
		if(sub.getPages().isEmpty())
			sub.getPages().add(new Page(1, new LinkedList<>()));
		LinkedList<Item> items = sub.getPages().get(0).getItem();
		if(items.isEmpty())
			items.add(new Item(0, name, urlString));
		items.add(new Item(items.getLast().getIndex()+1, name, urlString));
	}
	
	public DefaultTableModel getDefaultTable() {
		DefaultTableModel dtm = new DefaultTableModel(
				new Object[][] {
				},
				new String[] {
					"帖子", "作者", "发布时间", "最后回复者", "回复时间", "查看数量","回复数量"
				}
			) {
				/**
				 * 
				 */
				private static final long serialVersionUID = 1L;
				Class<?>[] columnTypes = new Class[] {
					String.class, String.class, String.class, String.class, String.class,Long.class, Long.class
				};
				public Class<?> getColumnClass(int columnIndex) {
					return columnTypes[columnIndex];
				}
				boolean[] columnEditables = new boolean[] {
					false, false, false, false, false, false, false
				};
				public boolean isCellEditable(int row, int column) {
					return columnEditables[column];
				}
//				@Override
//				public Object getValueAt(int row, int column) {
//					if(column == 2) {
//						try {
//							return new SimpleDateFormat(Item.postTimeFormat).parse(super.getValueAt(row, column).toString()).getTime();
//						} catch (ParseException e) {
//							e.printStackTrace();
//							return super.getValueAt(row, column);
//						}
//					}else if(column == 4) {
//						try {
//							return new SimpleDateFormat(Item.replyTimeFormat).parse(super.getValueAt(row, column).toString()).getTime();
//						} catch (ParseException e) {
//							e.printStackTrace();
//							return super.getValueAt(row, column);
//						}
//					}
//					return super.getValueAt(row, column);
//				}
			};
			//table.getColumnModel().getColumn(0).setPreferredWidth(615);
			
		return dtm;
	}
	
	private Item getSelectItem() {
		if(table.getSelectedRowCount()>1 || table.getSelectedColumnCount()>1 || table.getSelectedColumn() == -1 || table.getSelectedRow() == -1) {
			return null;
		}else {
			
			String key = table.getValueAt(table.getSelectedRow(), table.getSelectedColumn()).toString();
			return items.get(key);
		}
	}
}

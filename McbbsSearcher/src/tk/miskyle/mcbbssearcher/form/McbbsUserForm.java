package tk.miskyle.mcbbssearcher.form;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.List;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.DefaultListModel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import tk.miskyle.mcbbssearcher.data.McbbsUserData;
import tk.miskyle.mcbbssearcher.data.Setting;
import javax.swing.JLabel;

public class McbbsUserForm extends JDialog {

  /**
   * .
   */
  private static final long serialVersionUID = 1L;
  private final JPanel contentPanel = new JPanel();
  private DefaultListModel<String> model = new DefaultListModel<>();
  private JLabel lblMainUserInfo;
  private JList<String> list;
  
  /**
   * 显示用户选择界面.
   */
  public static void showUsers(boolean first) {
    try {
      McbbsUserForm dialog = new McbbsUserForm(first);
      dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
      dialog.setModal(true);
      dialog.setVisible(true);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * Create the dialog.
   */
  public McbbsUserForm(boolean first) {
    setTitle("Mcbbs用户选择");
    addWindowListener(new WindowAdapter() {
      @Override
      public void windowClosed(WindowEvent arg0) {
        if (first && Setting.mainUser == null) {
          JOptionPane.showMessageDialog(contentPanel, "初始化失败!\n退出程序!");
          System.exit(0);          
        }
      }
    });
    setBounds(100, 100, 250, 350);
    getContentPane().setLayout(new BorderLayout());
    contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
    getContentPane().add(contentPanel, BorderLayout.CENTER);
    contentPanel.setLayout(null);
    
    list = new JList<>();
    list.setToolTipText("右键展开菜单");
    list.setModel(model);
    list.setBounds(10, 10, 214, 258);
    contentPanel.add(list);
    
    JPopupMenu popupMenu = new JPopupMenu();
    addPopup(list, popupMenu);
    
    JButton btnCreate = new JButton("新建");
    btnCreate.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent arg0) {
        popupMenu.setVisible(false);
        String name = JOptionPane.showInputDialog(contentPanel, "请输入你的用户名", "小明");
        if (name == null || name.isEmpty()) {
          JOptionPane.showMessageDialog(contentPanel, "用户名不合法!");
          return;
        } else if (Setting.mcbbsUserCookies.containsKey(name)) {
          JOptionPane.showMessageDialog(contentPanel, "用户名已存在!");
          return;
        }
        String cookie = JOptionPane.showInputDialog(contentPanel, "请输入Cookie\n可以为空", "");
        Setting.mcbbsUserCookies.put(name, new McbbsUserData(name, cookie));
        updateList();
      }
    });
    popupMenu.add(btnCreate);
    
    JButton btnDelete = new JButton("删除");
    btnDelete.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        popupMenu.setVisible(false);
        if (list.isSelectionEmpty() || list.getSelectedValuesList().size() > 1) {
          JOptionPane.showMessageDialog(contentPanel, "未选中用户!");
          return;
        }
        String name = list.getSelectedValue();
        McbbsUserData data = Setting.mcbbsUserCookies.remove(name);
        if (data.getUserName().equalsIgnoreCase(Setting.mainUser.getUserName())) {
          Setting.mainUser = null;
        }
        updateList();
      }
    });
    popupMenu.add(btnDelete);
    
    JButton btnInfo = new JButton("详细");
    btnInfo.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        popupMenu.setVisible(false);
        if (list.isSelectionEmpty() || list.getSelectedValuesList().size() > 1) {
          JOptionPane.showMessageDialog(contentPanel, "未选中用户!");
          return;
        }
        String name = list.getSelectedValue();
        McbbsUserData data = Setting.mcbbsUserCookies.get(name);
        JOptionPane.showMessageDialog(contentPanel, "用户名: " + name 
            + "\ncookie: \n" + data.getCookies());
        
      }
    });
    popupMenu.add(btnInfo);
    
    JButton btnChangeName = new JButton("修改用户名");
    btnChangeName.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        popupMenu.setVisible(false);
        if (list.isSelectionEmpty() || list.getSelectedValuesList().size() > 1) {
          JOptionPane.showMessageDialog(contentPanel, "未选中用户!");
          return;
        }
        String old = list.getSelectedValue();
        McbbsUserData data = Setting.mcbbsUserCookies.get(old);
        String name = JOptionPane.showInputDialog(contentPanel, "请输入你的用户名", "小明");
        if (name == null || name.isEmpty()) {
          JOptionPane.showMessageDialog(contentPanel, "用户名不合法!");
          return;
        } else if (Setting.mcbbsUserCookies.containsKey(name)) {
          JOptionPane.showMessageDialog(contentPanel, "用户名已存在!");
          return;
        }
        data.setUserName(name);
        Setting.mcbbsUserCookies.remove(old);
        Setting.mcbbsUserCookies.put(name, data);
        if (old.equalsIgnoreCase(Setting.mainUser.getUserName())) {
          Setting.mainUser = data;
        }
        updateList();
      }
    });
    popupMenu.add(btnChangeName);
    
    JButton btnChangeCookie = new JButton("修改Cookie");
    btnChangeCookie.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        popupMenu.setVisible(false);
        if (list.isSelectionEmpty() || list.getSelectedValuesList().size() > 1) {
          JOptionPane.showMessageDialog(contentPanel, "未选中用户!");
          return;
        }
        String name = list.getSelectedValue();
        McbbsUserData data = Setting.mcbbsUserCookies.get(name);
        String cookie = JOptionPane.showInputDialog(contentPanel, "请输入Cookie\n可以为空", "");
        data.setCookies(cookie);
        if (Setting.mainUser.getUserName().equalsIgnoreCase(data.getUserName())) {
          Setting.mainUser = data;
        }
      }
    });
    popupMenu.add(btnChangeCookie);
    {
      JPanel buttonPane = new JPanel();
      buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
      getContentPane().add(buttonPane, BorderLayout.SOUTH);
      {
        JButton okButton = new JButton("OK");
        okButton.addActionListener(new ActionListener() {
          public void actionPerformed(ActionEvent e) {
            if (Setting.mainUser == null) {
              JOptionPane.showMessageDialog(contentPanel, "双击一个用户选择以启用!");
              return;
            }
            dispose();
          }
        });
        
        lblMainUserInfo = new JLabel("当前用户: ");
        buttonPane.add(lblMainUserInfo);
        okButton.setActionCommand("OK");
        buttonPane.add(okButton);
        getRootPane().setDefaultButton(okButton);
      }
    }
    
    updateList();
  }
  
  private void updateList() {
    model.clear();
    Setting.mcbbsUserCookies.values().forEach(user -> {
      model.addElement(user.getUserName());
    });
    if (Setting.mainUser == null) {
      lblMainUserInfo.setText("未选择主用户");
    } else {
      lblMainUserInfo.setText("主用户: " + Setting.mainUser.getUserName());
    }
  }
  
  private void addPopup(Component component, final JPopupMenu popup) {
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
      
      @Override
      public void mouseClicked(MouseEvent arg0) {
        if (arg0.getClickCount() != 2) {
          return;
        }
        
        if (list.isSelectionEmpty() || list.getSelectedValue().isEmpty()) {
          return;
        }
        
        String name = list.getSelectedValue();
        McbbsUserData data = Setting.mcbbsUserCookies.get(name);
        Setting.mainUser = data;
        updateList();
      }
    });
  }
}

package tk.miskyle.mcbbssearcher.form;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import tk.miskyle.mcbbssearcher.data.favorite.Favorite;
import tk.miskyle.mcbbssearcher.forum.data.Item;
import javax.swing.JList;
import javax.swing.JOptionPane;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class FavoriteSelectForm extends JDialog {

  private final JPanel contentPanel = new JPanel();
  
  /**
   * Launch the application.
   */
  public static void show(Item item) {
    try {
      FavoriteSelectForm dialog = new FavoriteSelectForm(item);
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
  public FavoriteSelectForm(Item item) {
    setTitle("请选择一个收藏夹");
    setBounds(100, 100, 250, 350);
    getContentPane().setLayout(new BorderLayout());
    contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
    getContentPane().add(contentPanel, BorderLayout.CENTER);
    contentPanel.setLayout(null);
    
    JList<String> list = new JList<>();
    DefaultListModel<String> model = new DefaultListModel<>();
    list.setModel(model);
    Favorite.getFavoriteList().keySet().forEach(name -> {
      model.addElement(name);
    });
    list.setBounds(10, 10, 214, 258);
    contentPanel.add(list);
    {
      JPanel buttonPane = new JPanel();
      buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
      getContentPane().add(buttonPane, BorderLayout.SOUTH);
      {
        JButton okButton = new JButton("OK");
        okButton.addActionListener(new ActionListener() {
          public void actionPerformed(ActionEvent e) {
            if (list.isSelectionEmpty()) {
              JOptionPane.showMessageDialog(contentPanel, "未选择收藏夹");
            }
            String name = list.getSelectedValue();
            String attach = JOptionPane.showInputDialog(contentPanel, "输入附加信息,\n可为空.", "");
            attach = (attach == null ? "" : attach);
            Favorite.like(name, attach, item);
            dispose();
          }
        });
        okButton.setActionCommand("OK");
        buttonPane.add(okButton);
        getRootPane().setDefaultButton(okButton);
      }
      {
        JButton cancelButton = new JButton("Cancel");
        cancelButton.setActionCommand("Cancel");
        buttonPane.add(cancelButton);
      }
    }
  }
}

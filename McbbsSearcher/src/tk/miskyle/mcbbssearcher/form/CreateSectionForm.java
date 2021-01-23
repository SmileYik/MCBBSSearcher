package tk.miskyle.mcbbssearcher.form;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import tk.miskyle.mcbbssearcher.forum.ForumManager;
import tk.miskyle.mcbbssearcher.forum.data.Section;
import tk.miskyle.mcbbssearcher.forum.data.SubSection;
import tk.miskyle.mcbbssearcher.forum.update.ForumUpdate;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class CreateSectionForm extends JDialog {

  private final JPanel contentPanel = new JPanel();
  private JTextField textFather;
  private JTextField textChild;
  private JTextField textChildUrl;

  /**
   * Launch the application.
   */
  public static void showForm() {
    try {
      CreateSectionForm dialog = new CreateSectionForm();
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
  public CreateSectionForm() {
    setTitle("添加子版块");
    setBounds(100, 100, 300, 200);
    getContentPane().setLayout(new BorderLayout());
    contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
    getContentPane().add(contentPanel, BorderLayout.CENTER);
    contentPanel.setLayout(null);
    
    JLabel lblNewLabel = new JLabel("父板块名称:");
    lblNewLabel.setBounds(10, 32, 82, 15);
    contentPanel.add(lblNewLabel);
    
    JLabel lblNewLabel1 = new JLabel("子板块名称:");
    lblNewLabel1.setBounds(10, 57, 82, 15);
    contentPanel.add(lblNewLabel1);
    
    JLabel lblNewLabel2 = new JLabel("子板块链接:");
    lblNewLabel2.setBounds(10, 82, 82, 15);
    contentPanel.add(lblNewLabel2);
    
    textFather = new JTextField();
    textFather.setBounds(91, 29, 183, 21);
    contentPanel.add(textFather);
    textFather.setColumns(10);
    
    textChild = new JTextField();
    textChild.setColumns(10);
    textChild.setBounds(91, 54, 183, 21);
    contentPanel.add(textChild);
    
    textChildUrl = new JTextField();
    textChildUrl.setColumns(10);
    textChildUrl.setBounds(91, 79, 183, 21);
    contentPanel.add(textChildUrl);
    {
      JPanel buttonPane = new JPanel();
      buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
      getContentPane().add(buttonPane, BorderLayout.SOUTH);
      {
        JButton okButton = new JButton("OK");
        okButton.addActionListener(new ActionListener() {
          public void actionPerformed(ActionEvent e) {
            String section = textFather.getText();
            String subSection = textChild.getText();
            String url = textChildUrl.getText();
            if (section == null || subSection == null || url == null
                || section.isEmpty() || section.isEmpty() || url.isEmpty()) {
              JOptionPane.showMessageDialog(contentPanel, "输入字符串不合法!");
              return;
            } else if (ForumManager.getForum().getSections().containsKey(section)
                && ForumManager.getForum().getSections()
                          .get(section).getSubs().containsKey(subSection)) {
              SubSection sub = 
                  ForumManager.getForum().getSections().get(section).getSubs().get(subSection);
              sub.setUrlString(url);
              ForumUpdate.simpleUpdateSubSection(sub);
              JOptionPane.showMessageDialog(contentPanel, "子版块已存在, 将更新子版块链接!");
            } else if (ForumManager.getForum().getSections().containsKey(section)) {
              SubSection sub = new SubSection();
              sub.setName(subSection);
              sub.setUrlString(url);
              ForumManager.getForum().getSections().get(section).addSubs(sub);
              ForumUpdate.simpleUpdateSubSection(sub);
              JOptionPane.showMessageDialog(contentPanel, "添加成功!!");
            } else {
              Section sec = new Section();
              SubSection sub = new SubSection();
              sub.setName(subSection);
              sub.setUrlString(url);
              sec.addSubs(sub);
              ForumManager.getForum().getSections().put(section, sec);
              ForumUpdate.simpleUpdateSubSection(sub);
              JOptionPane.showMessageDialog(contentPanel, "添加成功!!");
            }
            dispose();
          }
        });
        okButton.setActionCommand("OK");
        buttonPane.add(okButton);
        getRootPane().setDefaultButton(okButton);
      }
      {
        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(new ActionListener() {
          public void actionPerformed(ActionEvent e) {
            dispose();
          }
        });
        cancelButton.setActionCommand("Cancel");
        buttonPane.add(cancelButton);
      }
    }
  }
}

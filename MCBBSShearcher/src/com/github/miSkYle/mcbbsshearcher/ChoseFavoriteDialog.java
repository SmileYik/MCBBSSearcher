package com.github.miSkYle.mcbbsshearcher;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import com.github.miSkYle.mcbbsshearcher.data.Forum;
import com.github.miSkYle.mcbbsshearcher.data.Item;
import com.github.miSkYle.mcbbsshearcher.data.Page;
import com.github.miSkYle.mcbbsshearcher.data.SubForum;
import com.github.miSkYle.mcbbsshearcher.utils.Download;

import javax.swing.JScrollPane;
import javax.swing.JList;
import javax.swing.JOptionPane;

import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.LinkedList;
import java.awt.event.ActionEvent;
import javax.swing.JTextField;

public class ChoseFavoriteDialog extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private final JPanel contentPanel = new JPanel();
	private JTextField textField;
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			//ChoseFavoriteDialog dialog = new ChoseFavoriteDialog();
			//dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			//dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void show(Component  j,String name,String urlString){
		try {
			ChoseFavoriteDialog dialog = new ChoseFavoriteDialog(j,name,urlString);
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
			dialog.setComponentOrientation(j.getComponentOrientation());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public ChoseFavoriteDialog(Component  j,String name,String urlString) {
		setTitle("选择板块");
		setBounds(j.getBounds().x+(j.getWidth()-182)/2, j.getBounds().y+(j.getHeight()-300)/2, 182, 300);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		{
			JScrollPane scrollPane = new JScrollPane();
			scrollPane.setBounds(213, 10, 2, 2);
			contentPanel.add(scrollPane);
		}
		{
			@SuppressWarnings("rawtypes")
			JList list = new JList();
			list.setBounds(220, 11, 0, 0);
			contentPanel.add(list);
		}
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 10, 144, 208);
		contentPanel.add(scrollPane);
		
		JList<String> list = new JList<String>();
		scrollPane.setViewportView(list);
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("OK");
				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						if(list.isSelectionEmpty()){
							JOptionPane.showMessageDialog(contentPanel, "请选择一个子版块来保存!");
							return;
						}
						
						SubForum sub = Forum.getSystemTopForum().getSubForum(list.getSelectedValue());
						if(sub.getPages().isEmpty())
							sub.getPages().add(new Page(1, new LinkedList<>()));
						LinkedList<Item> items = sub.getPages().get(0).getItem();
						if(items.isEmpty())
							items.add(new Item(0, name, urlString));
						items.add(new Item(items.getLast().getIndex()+1, name, urlString));
						
						
						download(name, urlString, sub.getName());
						
						if(Data.saveLike)
							try {
								Forum.saveAllData();
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						
						dispose();
						return;
						
					}
				});
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
				JButton cancelButton = new JButton("Cancel");
				cancelButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent actionevent) {
						dispose();
					}
				});
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
		}
		
		DefaultListModel<String> model = new DefaultListModel<>();
		for(SubForum sub : Forum.getSystemTopForum().getSubs())
			if(sub.getName().equalsIgnoreCase("历史记录"))continue;
			else model.addElement(sub.getName());
		list.setModel(model);
		
		textField = new JTextField();
		textField.setEditable(false);
		scrollPane.setColumnHeaderView(textField);
		textField.setColumns(10);
		textField.setText(name);
		
		
	}
	
	private void download(String title,String urlString,String subForumTitle){
		try {
			Download.download(title, urlString, subForumTitle);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}

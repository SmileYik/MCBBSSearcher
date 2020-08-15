package com.github.miSkYle.mcbbsshearcher;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JEditorPane;
import javax.swing.event.HyperlinkListener;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkEvent.EventType;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

public class WebFrome extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField addressText;
	private String noMobile = "&mobile=no";

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					WebFrome frame = new WebFrome("");
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public WebFrome(String url) {
		
		if(url.contains("mcbbs.net")){
			url+=noMobile;
		}
		
		setVisible(true);
		
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 739, 502);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		JScrollPane scrollPane = new JScrollPane();
		contentPane.add(scrollPane, BorderLayout.CENTER);
		
		JEditorPane webPage = new JEditorPane();
		webPage.setEditable(false);
		scrollPane.setViewportView(webPage);
		
		addressText = new JTextField();
		addressText.setEditable(false);
		scrollPane.setColumnHeaderView(addressText);
		addressText.setColumns(10);
		
		//webPage.setText("");
		
		addressText.setText(url);
		try {
			webPage.setPage(url);
		} catch (IOException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		
		webPage.addHyperlinkListener(new HyperlinkListener() {
			public void hyperlinkUpdate(HyperlinkEvent e) {
				if(e.getEventType() == EventType.ACTIVATED){
					String url = e.getURL().toString();
					if(url.contains("www.mcbbs.net"))url+=noMobile;
					addressText.setText(url);
					try {
						webPage.setPage(url);
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
			}
		});
	}

}

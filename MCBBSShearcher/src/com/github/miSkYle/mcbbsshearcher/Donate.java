package com.github.miSkYle.mcbbsshearcher;

import java.awt.BorderLayout;
import java.awt.Component;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.awt.event.ActionEvent;
import javax.swing.SwingConstants;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;

public class Donate extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final JPanel contentPanel = new JPanel();

	/**
	 * Launch the application.
	 */
/*	public static void main(String[] args) {
		try {
			Donate dialog = new Donate();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}*/
	
	public static void show(Component  j){
		try {
			Donate dialog = new Donate(j);
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
	public Donate(Component  j) {
		setTitle("Donate | 捐款");
		setBounds(j.getBounds().x+(j.getWidth()-182)/2, j.getBounds().y+(j.getHeight()-300)/2, 784, 400);
		//setBounds(100, 100, 784, 400);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		
		JButton Alipay = new JButton("");
		try {
			Alipay.setIcon(new ImageIcon(ImageIO.read(this.getClass().getResourceAsStream("/donate/Alipay.png"))));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Alipay.setBounds(10, 10, 241, 230);
		contentPanel.add(Alipay);
		{
			JButton WeChat = new JButton("");
			try {
				WeChat.setIcon(new ImageIcon(ImageIO.read(this.getClass().getResourceAsStream("/donate/WeChat.png"))));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			WeChat.setBounds(270, 10, 241, 230);
			contentPanel.add(WeChat);
		}
		{
			JLabel label = new JLabel("支付宝捐款");
			label.setBounds(95, 250, 68, 15);
			contentPanel.add(label);
		}
		{
			JLabel label = new JLabel("微信捐款");
			label.setBounds(363, 250, 54, 15);
			contentPanel.add(label);
		}
		{
			JLabel label = new JLabel("感谢大家的捐款,你们的支持是我前进的动力.\r\n即使是不捐助也可以通过扫支付宝红包来进行捐助.");
			label.setVerticalAlignment(SwingConstants.TOP);
			label.setHorizontalAlignment(SwingConstants.CENTER);
			label.setBounds(40, 275, 669, 20);
			contentPanel.add(label);
		}
		{
			JButton button = new JButton("");
			try {
				button.setIcon(new ImageIcon(ImageIO.read(this.getClass().getResourceAsStream("/donate/Alipay2.png"))));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			button.setBounds(517, 10, 241, 230);
			contentPanel.add(button);
		}
		{
			JLabel label = new JLabel("支付宝红包");
			label.setBounds(604, 250, 68, 15);
			contentPanel.add(label);
		}
		{
			JLabel label = new JLabel("您可以扫支付宝红包后,将获得的红包金额+0.01元进行捐助.");
			label.setVerticalAlignment(SwingConstants.TOP);
			label.setHorizontalAlignment(SwingConstants.CENTER);
			label.setBounds(40, 294, 669, 20);
			contentPanel.add(label);
		}
		{
			JLabel label = new JLabel("email: miSkYle@outlook.com");
			label.setBounds(291, 313, 180, 15);
			contentPanel.add(label);
		}
		{
			JPanel buttonPane = new JPanel();
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			GridBagLayout gbl_buttonPane = new GridBagLayout();
			gbl_buttonPane.columnWidths = new int[]{296, 96, 0, 0, 0, 0, 45, 69, 0, 0, 0, 0, 0, 0, 0};
			gbl_buttonPane.rowHeights = new int[]{23, 0};
			gbl_buttonPane.columnWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
			gbl_buttonPane.rowWeights = new double[]{0.0, Double.MIN_VALUE};
			buttonPane.setLayout(gbl_buttonPane);
			{
				JLabel lblMadeByMiskyle = new JLabel("Made by miSkYle.");
				lblMadeByMiskyle.setHorizontalAlignment(SwingConstants.LEFT);
				GridBagConstraints gbc_lblMadeByMiskyle = new GridBagConstraints();
				gbc_lblMadeByMiskyle.insets = new Insets(0, 0, 0, 5);
				gbc_lblMadeByMiskyle.gridx = 0;
				gbc_lblMadeByMiskyle.gridy = 0;
				buttonPane.add(lblMadeByMiskyle, gbc_lblMadeByMiskyle);
			}
			{
				JButton okButton = new JButton("OK");
				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						dispose();
					}
				});
				okButton.setActionCommand("OK");
				GridBagConstraints gbc_okButton = new GridBagConstraints();
				gbc_okButton.insets = new Insets(0, 0, 0, 5);
				gbc_okButton.anchor = GridBagConstraints.NORTHWEST;
				gbc_okButton.gridx = 2;
				gbc_okButton.gridy = 0;
				buttonPane.add(okButton, gbc_okButton);
				getRootPane().setDefaultButton(okButton);
			}
		}
	}
}

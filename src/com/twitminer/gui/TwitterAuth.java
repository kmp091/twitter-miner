package com.twitminer.gui;

import javax.swing.JDialog;
import javax.swing.JLabel;
import java.awt.BorderLayout;
import javax.swing.SwingConstants;
import javax.swing.JPanel;
import javax.swing.BoxLayout;
import java.awt.Component;
import javax.swing.Box;
import java.awt.Dimension;
import javax.swing.JButton;
import javax.swing.border.TitledBorder;
import javax.swing.JTextField;
import java.awt.Font;

public class TwitterAuth extends JDialog {
	/**
	 * 
	 */
	private static final long serialVersionUID = 7227750323541277674L;
	private JTextField tokenField;
	private JButton accessTwitterButton;
	private JButton copyUsernameButton;
	private JButton copyPasswordButton;
	private JButton authButton;
	private JPanel firstStepPanel;
	private JPanel secondStepPanel;
	private JPanel thirdStepPanel;
	private JPanel fourthStepPanel;
	
	public JTextField getTokenField() {
		return tokenField;
	}

	public JPanel getFirstStepPanel() {
		return firstStepPanel;
	}

	public JPanel getSecondStepPanel() {
		return secondStepPanel;
	}

	public JPanel getThirdStepPanel() {
		return thirdStepPanel;
	}

	public JPanel getFourthStepPanel() {
		return fourthStepPanel;
	}

	public JButton getAccessTwitterButton() {
		return accessTwitterButton;
	}

	public JButton getCopyUsernameButton() {
		return copyUsernameButton;
	}

	public JButton getCopyPasswordButton() {
		return copyPasswordButton;
	}

	public JButton getAuthButton() {
		return authButton;
	}

	public TwitterAuth() {
		setPreferredSize(new Dimension(450, 412));
		setMinimumSize(new Dimension(450, 412));
		setTitle("Twitter Authorization");
		
		JLabel authLabel = new JLabel("Please authorize the app on Twitter. Just follow 4 steps!");
		authLabel.setHorizontalAlignment(SwingConstants.CENTER);
		getContentPane().add(authLabel, BorderLayout.NORTH);
		
		JPanel stepsPanel = new JPanel();
		getContentPane().add(stepsPanel, BorderLayout.CENTER);
		stepsPanel.setLayout(new BoxLayout(stepsPanel, BoxLayout.Y_AXIS));
		
		Component verticalStrut = Box.createVerticalStrut(20);
		stepsPanel.add(verticalStrut);
		
		JPanel firstStepPanel = new JPanel();
		firstStepPanel.setToolTipText("Access the Twitter site and approve the application");
		firstStepPanel.setMinimumSize(new Dimension(200, 10));
		firstStepPanel.setPreferredSize(new Dimension(400, 10));
		firstStepPanel.setBorder(new TitledBorder(null, "Step 1", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		stepsPanel.add(firstStepPanel);
		firstStepPanel.setLayout(new BoxLayout(firstStepPanel, BoxLayout.Y_AXIS));
		
		JLabel firstStepLabel = new JLabel("Access Twitter's website (opens your default browser).");
		firstStepLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		firstStepPanel.add(firstStepLabel);
		
		JPanel sideBySidePanel = new JPanel();
		firstStepPanel.add(sideBySidePanel);
		sideBySidePanel.setLayout(new BoxLayout(sideBySidePanel, BoxLayout.X_AXIS));
		
		Component horizontalGlue = Box.createHorizontalGlue();
		sideBySidePanel.add(horizontalGlue);
		
		accessTwitterButton = new JButton("Access Twitter");
		sideBySidePanel.add(accessTwitterButton);
		
		Component horizontalGlue_1 = Box.createHorizontalGlue();
		sideBySidePanel.add(horizontalGlue_1);
		
		JPanel secondStepPanel = new JPanel();
		secondStepPanel.setBorder(new TitledBorder(null, "Step 2", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		secondStepPanel.setToolTipText("Log in using these credentials");
		stepsPanel.add(secondStepPanel);
		secondStepPanel.setLayout(new BoxLayout(secondStepPanel, BoxLayout.Y_AXIS));
		
		JLabel secondStepLabel = new JLabel("<html>When asked for login, copy the username and password to the corresponding fields.</html>");
		secondStepLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		secondStepPanel.add(secondStepLabel);
		
		JPanel sideBySidePanel2 = new JPanel();
		secondStepPanel.add(sideBySidePanel2);
		sideBySidePanel2.setLayout(new BoxLayout(sideBySidePanel2, BoxLayout.X_AXIS));
		
		Component horizontalGlue_2 = Box.createHorizontalGlue();
		sideBySidePanel2.add(horizontalGlue_2);
		
		copyUsernameButton = new JButton("Copy Username");
		sideBySidePanel2.add(copyUsernameButton);
		
		Component horizontalStrut = Box.createHorizontalStrut(20);
		horizontalStrut.setMaximumSize(new Dimension(20, 20));
		sideBySidePanel2.add(horizontalStrut);
		
		copyPasswordButton = new JButton("Copy Password");
		sideBySidePanel2.add(copyPasswordButton);
		
		Component horizontalGlue_3 = Box.createHorizontalGlue();
		sideBySidePanel2.add(horizontalGlue_3);
		
		JPanel thirdStepPanel = new JPanel();
		thirdStepPanel.setBorder(new TitledBorder(null, "Step 3", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		stepsPanel.add(thirdStepPanel);
		thirdStepPanel.setLayout(new BoxLayout(thirdStepPanel, BoxLayout.Y_AXIS));
		
		JLabel thirdStepLabel = new JLabel("<html>Authorize the app, and put the generated numerical code in the text box.</html>");
		thirdStepLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		thirdStepPanel.add(thirdStepLabel);
		
		tokenField = new JTextField();
		tokenField.setHorizontalAlignment(SwingConstants.CENTER);
		tokenField.setPreferredSize(new Dimension(200, 28));
		tokenField.setMinimumSize(new Dimension(14, 20));
		tokenField.setMaximumSize(new Dimension(300, 28));
		tokenField.setFont(new Font("Lucida Grande", Font.PLAIN, 17));
		thirdStepPanel.add(tokenField);
		tokenField.setColumns(10);
		
		JPanel fourthStepPanel = new JPanel();
		fourthStepPanel.setBorder(new TitledBorder(null, "Step 4", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		stepsPanel.add(fourthStepPanel);
		fourthStepPanel.setLayout(new BoxLayout(fourthStepPanel, BoxLayout.Y_AXIS));
		
		JLabel fourthStepLabel = new JLabel("<html>Whether or not an authorization code was given, press the Authorize button when you're done</html>");
		fourthStepLabel.setAlignmentX(0.5f);
		fourthStepPanel.add(fourthStepLabel);
		
		authButton = new JButton("Authorize");
		authButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		fourthStepPanel.add(authButton);
	}

}

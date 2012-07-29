package com.twitminer.gui;

import java.awt.Dialog;
import java.awt.Frame;
import java.awt.Window;

import javax.swing.JDialog;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Component;
import javax.swing.Box;
import java.awt.Dimension;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JSpinner;
import javax.swing.JButton;

public class SettingsDialog extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2239786097379216095L;
	private JTextField trendField;

	public SettingsDialog() {
		super();
		setResizable(false);
		this.setModal(true);
		this.setTitle("Settings");
		
		JPanel settingsPane = new JPanel();
		getContentPane().add(settingsPane, BorderLayout.CENTER);
		settingsPane.setLayout(new BoxLayout(settingsPane, BoxLayout.Y_AXIS));
		
		Component verticalGlue_1 = Box.createVerticalGlue();
		settingsPane.add(verticalGlue_1);
		
		JPanel tweetNumberPane = new JPanel();
		settingsPane.add(tweetNumberPane);
		tweetNumberPane.setLayout(new BoxLayout(tweetNumberPane, BoxLayout.X_AXIS));
		
		Component horizontalStrut_3 = Box.createHorizontalStrut(20);
		horizontalStrut_3.setMaximumSize(new Dimension(32767, 10));
		tweetNumberPane.add(horizontalStrut_3);
		
		JLabel numTweetsLabel = new JLabel("Number of tweets");
		numTweetsLabel.setPreferredSize(new Dimension(120, 16));
		numTweetsLabel.setMinimumSize(new Dimension(120, 16));
		numTweetsLabel.setMaximumSize(new Dimension(120, 16));
		numTweetsLabel.setAlignmentX(Component.RIGHT_ALIGNMENT);
		tweetNumberPane.add(numTweetsLabel);
		
		Component horizontalStrut = Box.createHorizontalStrut(20);
		horizontalStrut.setPreferredSize(new Dimension(50, 0));
		horizontalStrut.setMinimumSize(new Dimension(50, 0));
		horizontalStrut.setMaximumSize(new Dimension(50, 50));
		tweetNumberPane.add(horizontalStrut);
		
		JSpinner tweetSpinner = new JSpinner();
		tweetSpinner.setMinimumSize(new Dimension(100, 28));
		tweetSpinner.setPreferredSize(new Dimension(130, 28));
		tweetSpinner.setMaximumSize(new Dimension(100, 30));
		tweetNumberPane.add(tweetSpinner);
		
		Component horizontalStrut_2 = Box.createHorizontalStrut(20);
		horizontalStrut_2.setMaximumSize(new Dimension(32767, 10));
		tweetNumberPane.add(horizontalStrut_2);
		
		Component verticalStrut = Box.createVerticalStrut(20);
		settingsPane.add(verticalStrut);
		
		JPanel trendPane = new JPanel();
		settingsPane.add(trendPane);
		trendPane.setLayout(new BoxLayout(trendPane, BoxLayout.X_AXIS));
		
		Component horizontalStrut_4 = Box.createHorizontalStrut(20);
		horizontalStrut_4.setMaximumSize(new Dimension(32767, 10));
		trendPane.add(horizontalStrut_4);
		
		JLabel lblTopic = new JLabel("Topic");
		lblTopic.setPreferredSize(new Dimension(120, 16));
		lblTopic.setMinimumSize(new Dimension(120, 16));
		lblTopic.setMaximumSize(new Dimension(120, 16));
		lblTopic.setAlignmentX(Component.RIGHT_ALIGNMENT);
		trendPane.add(lblTopic);
		
		Component horizontalStrut_1 = Box.createHorizontalStrut(20);
		horizontalStrut_1.setPreferredSize(new Dimension(50, 0));
		horizontalStrut_1.setMinimumSize(new Dimension(50, 0));
		horizontalStrut_1.setMaximumSize(new Dimension(50, 50));
		trendPane.add(horizontalStrut_1);
		
		trendField = new JTextField();
		trendField.setMinimumSize(new Dimension(37, 28));
		trendField.setPreferredSize(new Dimension(100, 28));
		trendField.setMaximumSize(new Dimension(100, 30));
		trendPane.add(trendField);
		trendField.setColumns(10);
		
		Component horizontalStrut_5 = Box.createHorizontalStrut(20);
		horizontalStrut_5.setMaximumSize(new Dimension(32767, 10));
		trendPane.add(horizontalStrut_5);
		
		Component verticalStrut_1 = Box.createVerticalStrut(20);
		settingsPane.add(verticalStrut_1);
		
		JPanel optionsPanel = new JPanel();
		optionsPanel.setPreferredSize(new Dimension(10, 50));
		optionsPanel.setMinimumSize(new Dimension(10, 50));
		settingsPane.add(optionsPanel);
		optionsPanel.setLayout(new BoxLayout(optionsPanel, BoxLayout.X_AXIS));
		
		Component horizontalGlue_1 = Box.createHorizontalGlue();
		horizontalGlue_1.setMaximumSize(new Dimension(32767, 50));
		horizontalGlue_1.setMinimumSize(new Dimension(0, 50));
		optionsPanel.add(horizontalGlue_1);
		
		JButton setDefaultsButton = new JButton("Set Defaults");
		optionsPanel.add(setDefaultsButton);
		
		Component horizontalGlue = Box.createHorizontalGlue();
		horizontalGlue.setMinimumSize(new Dimension(0, 50));
		horizontalGlue.setMaximumSize(new Dimension(32767, 50));
		optionsPanel.add(horizontalGlue);
		
		Component verticalGlue = Box.createVerticalGlue();
		settingsPane.add(verticalGlue);
		
		Component rigidArea = Box.createRigidArea(new Dimension(20, 20));
		getContentPane().add(rigidArea, BorderLayout.NORTH);
		
		Component rigidArea_1 = Box.createRigidArea(new Dimension(20, 20));
		getContentPane().add(rigidArea_1, BorderLayout.SOUTH);
		
		Component rigidArea_2 = Box.createRigidArea(new Dimension(20, 20));
		getContentPane().add(rigidArea_2, BorderLayout.WEST);
		
		Component rigidArea_3 = Box.createRigidArea(new Dimension(20, 20));
		getContentPane().add(rigidArea_3, BorderLayout.EAST);
	}
	
	public SettingsDialog(Dialog parent) {
		super(parent, "Settings", true);
	}
	
	public SettingsDialog(Window parent) {
		super(parent, "Settings", Dialog.ModalityType.APPLICATION_MODAL);
	}
	
	public SettingsDialog(Frame parent) {
		super(parent, "Settings", true);
	}
	
	
	
}

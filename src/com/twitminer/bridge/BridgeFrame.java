package com.twitminer.bridge;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import java.awt.Component;
import javax.swing.Box;
import java.awt.Dimension;
import javax.swing.JButton;

public class BridgeFrame extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	
	private JButton twitMinerButton;
	private JButton wekaButton;
	private JButton twitMonitorButton;
	
	public JButton getTwitMinerButton() {
		return twitMinerButton;
	}

	public JButton getWekaButton() {
		return wekaButton;
	}

	public JButton getTwitMonitorButton() {
		return twitMonitorButton;
	}

	/**
	 * Create the frame.
	 */
	public BridgeFrame() {
		setResizable(false);
		setPreferredSize(new Dimension(451, 303));
		setMinimumSize(new Dimension(451, 303));
		setTitle("TwitMiner Hub");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 361, 303);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.Y_AXIS));
		
		Component topGap = Box.createRigidArea(new Dimension(20, 20));
		contentPane.add(topGap);
		
		JLabel instructionLabel = new JLabel("Select an application to run.");
		instructionLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		contentPane.add(instructionLabel);
		
		Component rigidArea = Box.createRigidArea(new Dimension(20, 20));
		contentPane.add(rigidArea);
		
		twitMinerButton = new JButton("TwitMiner");
		twitMinerButton.setPreferredSize(new Dimension(200, 50));
		twitMinerButton.setMinimumSize(new Dimension(200, 50));
		twitMinerButton.setMaximumSize(new Dimension(200, 50));
		twitMinerButton.setActionCommand("TwitMiner");
		twitMinerButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		contentPane.add(twitMinerButton);
		
		Component rigidArea_1 = Box.createRigidArea(new Dimension(20, 20));
		rigidArea_1.setMaximumSize(new Dimension(20, 10));
		rigidArea_1.setMinimumSize(new Dimension(20, 10));
		rigidArea_1.setPreferredSize(new Dimension(20, 10));
		contentPane.add(rigidArea_1);
		
		wekaButton = new JButton("Weka");
		wekaButton.setMaximumSize(new Dimension(200, 50));
		wekaButton.setMinimumSize(new Dimension(200, 50));
		wekaButton.setPreferredSize(new Dimension(200, 50));
		wekaButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		contentPane.add(wekaButton);
		
		Component rigidArea_2 = Box.createRigidArea(new Dimension(20, 20));
		rigidArea_2.setPreferredSize(new Dimension(20, 10));
		rigidArea_2.setMinimumSize(new Dimension(20, 10));
		rigidArea_2.setMaximumSize(new Dimension(20, 10));
		contentPane.add(rigidArea_2);
		
		twitMonitorButton = new JButton("TwitMonitor");
		twitMonitorButton.setMinimumSize(new Dimension(200, 50));
		twitMonitorButton.setMaximumSize(new Dimension(200, 50));
		twitMonitorButton.setPreferredSize(new Dimension(200, 50));
		twitMonitorButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		contentPane.add(twitMonitorButton);
	}
}

package com.twitminer.bridge;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

public class BridgeController {

	BridgeFrame frame;
	
	public BridgeController() {
		frame = new BridgeFrame();
		setHandlers();
	}
	
	private static final int MINER = 1;
	private static final int VIEWER = 2;
	private static final int WEKA = 3;
	
	private void runNewProcess(int app) {
		String application;
		final JButton button;
		
		switch(app) {
		case MINER:
			application = "com.twitminer.gui.MainGUI";
			button = frame.getTwitMinerButton();
			break;
		case WEKA:
			application = "weka.gui.GUIChooser";
			button = frame.getWekaButton();
			break;
		case VIEWER:
			default:
				application = "com.twitminer.viewer.Initializer";
				button = frame.getTwitMonitorButton();
		}
		
		String classpath = System.getProperty("java.class.path");
		String[] startOptions = {"java", "-Xmx900M", "-cp", classpath, application};
		
		try {
			ProcessBuilder pb = new ProcessBuilder(startOptions);
			final Process process = pb.start();
			
			new Thread(new Runnable() {

				@Override
				public void run() {
					button.setEnabled(false);
					try {
						process.waitFor();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					finally {
						button.setEnabled(true);
					}
				}
				
			}).start();
		} catch (Exception e) {
			System.err.println("Unable to run Twitminer");
			e.printStackTrace();
		}
	}
	
	private void setHandlers() {
		frame.getTwitMinerButton().addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				runNewProcess(MINER);
			}
			
		});
		
		frame.getTwitMonitorButton().addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				runNewProcess(VIEWER);
			}
			
		});
		
		frame.getWekaButton().addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				runNewProcess(WEKA);
			}
			
		});
	}
	
	private void showWindow() {
		
		frame.setVisible(true);
	}
	
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					BridgeController bridge = new BridgeController();
					bridge.showWindow();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

}

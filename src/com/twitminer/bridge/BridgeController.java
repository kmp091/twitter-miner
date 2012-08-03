package com.twitminer.bridge;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class BridgeController {

	BridgeFrame frame;
	
	public BridgeController() {
		frame = new BridgeFrame();
		setHandlers();
	}
	
	private void setHandlers() {
		frame.getTwitMinerButton().addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
		});
		
		frame.getTwitMonitorButton().addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
		});
		
		frame.getWekaButton().addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				
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

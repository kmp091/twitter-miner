package com.twitminer.viewer.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import com.twitminer.viewer.algorithm.ClassifierFactory;
import com.twitminer.viewer.gui.AlgorithmSelect;

public class AlgorithmSelectController {

	private int currentAlgo;
	private AlgorithmSelect view;
	
	public AlgorithmSelectController() {
		currentAlgo = ClassifierFactory.SMO;
		view = new AlgorithmSelect();
		setupGUI();
	}
	
	private void setupGUI() {
		view.getSmoButton().addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				view.setVisible(false);
				currentAlgo = ClassifierFactory.SMO;
			}
			
		});
		
		view.getJ48Button().addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				view.setVisible(false);
				currentAlgo = ClassifierFactory.J48;
			}
			
		});
		
		view.getNaiveBayesButton().addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				view.setVisible(false);
				currentAlgo = ClassifierFactory.NAIVE_BAYES;
			}
			
		});
	}
	
	public void setAlgoGUI() {
		view.setVisible(true);
		view.setDefaultButton(getCurrentAlgo());
	}
	
	public int getCurrentAlgo() {
		return this.currentAlgo;
	}

}

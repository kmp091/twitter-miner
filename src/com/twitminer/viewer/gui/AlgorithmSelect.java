package com.twitminer.viewer.gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.BoxLayout;
import java.awt.Component;
import javax.swing.SwingConstants;

import com.twitminer.viewer.algorithm.ClassifierFactory;

public class AlgorithmSelect extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final JPanel contentPanel = new JPanel();
	private JButton naiveBayesButton;
	private JButton j48Button;
	private JButton smoButton;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			AlgorithmSelect dialog = new AlgorithmSelect();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public AlgorithmSelect() {
		setTitle("Select an Algorithm");
		setModal(true);
		setResizable(false);
		setBounds(100, 100, 324, 155);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.X_AXIS));
		{
			JLabel instructionLabel = new JLabel("<html>You can select an algorithm to employ classification upon a tweet.</html>\n");
			instructionLabel.setHorizontalAlignment(SwingConstants.CENTER);
			instructionLabel.setHorizontalTextPosition(SwingConstants.CENTER);
			instructionLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
			contentPanel.add(instructionLabel);
		}
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.CENTER));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				smoButton = new JButton("SMO");
				smoButton.setActionCommand("OK");
				buttonPane.add(smoButton);
				getRootPane().setDefaultButton(smoButton);
			}
			{
				j48Button = new JButton("J48");
				j48Button.setActionCommand("J48");
				buttonPane.add(j48Button);
			}
			{
				naiveBayesButton = new JButton("Naive Bayes");
				buttonPane.add(naiveBayesButton);
			}
		}
	}
	
	public void setDefaultButton(int algo) {
		switch (algo) {
		case ClassifierFactory.J48:
			getRootPane().setDefaultButton(j48Button);
			break;
		case ClassifierFactory.SMO:
			getRootPane().setDefaultButton(smoButton);
			break;
		case ClassifierFactory.NAIVE_BAYES:
			getRootPane().setDefaultButton(naiveBayesButton);
			break;
		}
	}

	public JButton getNaiveBayesButton() {
		return naiveBayesButton;
	}
	public JButton getJ48Button() {
		return j48Button;
	}
	public JButton getSmoButton() {
		return smoButton;
	}
}

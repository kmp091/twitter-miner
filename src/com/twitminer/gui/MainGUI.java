package com.twitminer.gui;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.BorderLayout;
import java.awt.Font;
import javax.swing.SwingConstants;
import javax.swing.JButton;
import java.awt.Component;
import javax.swing.Box;
import java.awt.Dimension;

import javax.swing.JPanel;

import com.twitminer.MinerInit;
import com.twitminer.beans.Tweet;
import com.twitminer.dao.DAOFactory;
import com.twitminer.dao.EmotionDAO;
import com.twitminer.event.AuthorizationInputEvent;
import com.twitminer.event.TaskFinishEvent;
import com.twitminer.event.listener.AuthorizationInputEventListener;
import com.twitminer.event.listener.SavedEventListener;
import com.twitminer.event.listener.TaskFinishEventListener;
import com.twitminer.util.Saver;
import com.twitminer.util.SaverFactory;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JProgressBar;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class MainGUI {

	private JFrame frmTwitminer;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainGUI window = new MainGUI();
					window.frmTwitminer.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public MainGUI() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmTwitminer = new JFrame();
		frmTwitminer.setTitle("TwitMiner");
		frmTwitminer.setBounds(100, 100, 450, 300);
		frmTwitminer.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JLabel titleLabel = new JLabel("Twitter Data Gatherer");
		titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
		titleLabel.setFont(new Font("Lucida Grande", Font.PLAIN, 30));
		frmTwitminer.getContentPane().add(titleLabel, BorderLayout.NORTH);
		
		Component rigidArea = Box.createRigidArea(new Dimension(75, 242));
		frmTwitminer.getContentPane().add(rigidArea, BorderLayout.WEST);
		
		Component rigidArea_1 = Box.createRigidArea(new Dimension(75, 242));
		frmTwitminer.getContentPane().add(rigidArea_1, BorderLayout.EAST);
		
		JPanel dumpArea = new JPanel();
		frmTwitminer.getContentPane().add(dumpArea, BorderLayout.SOUTH);
		dumpArea.setLayout(new BorderLayout(0, 0));
		
		Component rigidArea_2 = Box.createRigidArea(new Dimension(20, 20));
		dumpArea.add(rigidArea_2, BorderLayout.WEST);
		
		Component rigidArea_3 = Box.createRigidArea(new Dimension(20, 20));
		dumpArea.add(rigidArea_3, BorderLayout.EAST);
		
		Component rigidArea_4 = Box.createRigidArea(new Dimension(450, 9));
		dumpArea.add(rigidArea_4, BorderLayout.SOUTH);
		
		final JProgressBar progressBar = new JProgressBar();
		progressBar.setEnabled(false);
		progressBar.setString("");
		progressBar.setStringPainted(true);
		progressBar.setMaximum(MinerInit.MAX_TWEETS * 4 + 30);
		dumpArea.add(progressBar, BorderLayout.CENTER);
		
		JPanel startArea = new JPanel();
		startArea.setPreferredSize(new Dimension(10, 5));
		startArea.setMaximumSize(new Dimension(16383, 16383));
		frmTwitminer.getContentPane().add(startArea, BorderLayout.CENTER);
		startArea.setLayout(new BorderLayout(0, 0));
		
		final JButton startButton = new JButton("Start");
		startButton.setFont(new Font("Lucida Grande", Font.PLAIN, 25));
		startArea.add(startButton, BorderLayout.CENTER);
		
		final JButton saveARFFButton = new JButton("Save ARFF");
		saveARFFButton.setVisible(false);
		startArea.add(saveARFFButton, BorderLayout.WEST);
		
		Component rigidArea_5 = Box.createRigidArea(new Dimension(300, 55));
		startArea.add(rigidArea_5, BorderLayout.NORTH);
		
		Component rigidArea_7 = Box.createRigidArea(new Dimension(300, 86));
		rigidArea_7.setMinimumSize(new Dimension(300, 55));
		rigidArea_7.setMaximumSize(new Dimension(300, 55));
		rigidArea_7.setPreferredSize(new Dimension(300, 55));
		startArea.add(rigidArea_7, BorderLayout.SOUTH);
		
		final JButton saveCSVButton = new JButton("Save CSV");
		saveCSVButton.setFont(new Font("Lucida Grande", Font.PLAIN, 13));
		saveCSVButton.setVisible(false);
		startArea.add(saveCSVButton, BorderLayout.EAST);
		
		final SavedEventListener saveListener = new SavedEventListener() {

			@Override
			public void onSave() {
				saveCSVButton.setEnabled(false);
				saveARFFButton.setEnabled(false);
				progressBar.setString("Saving... Please wait...");
				frmTwitminer.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
			}

			@Override
			public void onSaveSuccess() {
				saveCSVButton.setEnabled(true);
				saveARFFButton.setEnabled(true);
				frmTwitminer.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				progressBar.setString("The save operation was successful!");
			}

			@Override
			public void onSaveFailed(Exception e) {
				saveCSVButton.setEnabled(true);
				saveARFFButton.setEnabled(true);
				frmTwitminer.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				progressBar.setString("The save operation was not successful.");
				JOptionPane.showMessageDialog(frmTwitminer, "<html>Save failed...<br><br>" + e.getMessage() + "</html>");
			}
			
		};
		
		startButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				progressBar.setEnabled(true);
				progressBar.setValue(10);
				progressBar.setString("Authorizing...");
				try {
					final MinerInit miner = new MinerInit();
					miner.addChangeListener(new ChangeListener() {
	
						@Override
						public void stateChanged(ChangeEvent e) {
							progressBar.setString(e.getSource().toString());
							progressBar.setValue(progressBar.getValue() + 1);
						}
						
					});
					
					miner.addTaskFinishListener(new TaskFinishEventListener() {
	
						@Override
						public void onTaskFinished(TaskFinishEvent evt) {
							progressBar.setValue(progressBar.getMaximum());
							progressBar.setString("Finished! You can exit or save a CSV/ARFF of the tweets.");
							saveCSVButton.setVisible(true);
							saveCSVButton.addActionListener(new ActionListener() {
	
								@Override
								public void actionPerformed(ActionEvent arg0) {
									saveFormat(SaverFactory.CSV, miner.getTweets(), saveListener);
								}
								
							});
							
							saveARFFButton.setVisible(true);
							saveARFFButton.addActionListener(new ActionListener() {
	
								@Override
								public void actionPerformed(ActionEvent arg0) {
									saveFormat(SaverFactory.ARFF, miner.getTweets(), saveListener);
								}
								
							});
							
							startButton.setVisible(false);
						}
						
					});
					miner.addOnAuthorizationListener(new AuthorizationInputEventListener() {
	
						@Override
						public void onAuthorized(AuthorizationInputEvent evt) {
							progressBar.setValue(20);
							progressBar.setString("Authorized");
							startButton.setEnabled(false);
							miner.startStream();
						}
						
					});
					miner.initialize();
				}
				catch (OutOfMemoryError out) {
					JOptionPane.showMessageDialog(frmTwitminer, "Unfortunately, the JVM was not allocated enough memory in order to run. The application cannot run.");
					System.exit(1);
				}
			}
			
		});
		
	}
	
	private void saveFormat(int format, List<Tweet> tweets, SavedEventListener listener) {
		DAOFactory daos = DAOFactory.getInstance(DAOFactory.ARRAY_LIST);
		EmotionDAO emotion = daos.getEmotionDAO();
		
		Saver saver = SaverFactory.getInstance(format);
		if (listener != null) {
			saver.addSavedEventListener(listener);
		}

		saver.save(tweets, emotion);
	}

}

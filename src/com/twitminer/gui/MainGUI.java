package com.twitminer.gui;

import java.awt.EventQueue;

import javax.swing.AbstractAction;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;

import java.awt.BorderLayout;
import java.awt.Font;
import javax.swing.SwingConstants;
import javax.swing.JButton;
import java.awt.Component;
import javax.swing.Box;
import java.awt.Dimension;

import javax.swing.JPanel;

import com.twitminer.MinerInit;
import com.twitminer.beans.TokenizedTweet;
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
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Reader;
import java.util.List;

import javax.swing.JProgressBar;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.BoxLayout;

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
		
		final JButton saveTrainingButton = new JButton("<html>Save<br> Training<br> Set...</html>");
		saveTrainingButton.setHorizontalTextPosition(SwingConstants.CENTER);
		saveTrainingButton.setVisible(false);
		startArea.add(saveTrainingButton, BorderLayout.WEST);
		
		Component rigidArea_7 = Box.createRigidArea(new Dimension(300, 86));
		rigidArea_7.setMinimumSize(new Dimension(300, 55));
		rigidArea_7.setMaximumSize(new Dimension(300, 55));
		rigidArea_7.setPreferredSize(new Dimension(300, 55));
		startArea.add(rigidArea_7, BorderLayout.SOUTH);
		
		final JButton saveTestButton = new JButton("<html>Save<br> Test Set...</html>");
		saveTestButton.setFont(new Font("Lucida Grande", Font.PLAIN, 13));
		saveTestButton.setVisible(false);
		startArea.add(saveTestButton, BorderLayout.EAST);
		
		JPanel panel = new JPanel();
		panel.setPreferredSize(new Dimension(32767, 150));
		startArea.add(panel, BorderLayout.CENTER);
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		
		final JButton startButton = new JButton("Start");
		startButton.setMaximumSize(new Dimension(32767, 29));
		startButton.setMinimumSize(new Dimension(75, 10));
		startButton.setPreferredSize(new Dimension(350, 105));
		panel.add(startButton);
		startButton.setFont(new Font("Lucida Grande", Font.PLAIN, 25));
		
		Component rigidArea_5 = Box.createRigidArea(new Dimension(20, 20));
		rigidArea_5.setPreferredSize(new Dimension(300, 55));
		rigidArea_5.setMinimumSize(new Dimension(300, 55));
		rigidArea_5.setMaximumSize(new Dimension(300, 55));
		startArea.add(rigidArea_5, BorderLayout.NORTH);
		
		final SavedEventListener saveListener = new SavedEventListener() {

			@Override
			public void onSave() {
				saveTestButton.setEnabled(false);
				saveTrainingButton.setEnabled(false);
				progressBar.setString("Saving... Please wait...");
				frmTwitminer.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
			}

			@Override
			public void onSaveSuccess() {
				saveTestButton.setEnabled(true);
				saveTrainingButton.setEnabled(true);
				frmTwitminer.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				progressBar.setString("The save operation was successful!");
			}

			@Override
			public void onSaveFailed(Exception e) {
				saveTestButton.setEnabled(true);
				saveTrainingButton.setEnabled(true);
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
				progressBar.setString("Searching...");
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
	
						private File openSourceTrainingSet(int format) {
							JFileChooser fileChooser = new JFileChooser();
							
							switch (format) {
								case SaverFactory.TRAIN_CSV:
								case SaverFactory.TEST_CSV:
									fileChooser.setFileFilter(new FileNameExtensionFilter("Comma separated values", "csv"));
									break;
								case SaverFactory.TRAIN_ARFF:
								case SaverFactory.TEST_ARFF:
								default:
									fileChooser.setFileFilter(new FileNameExtensionFilter("Weka ARFF files", "arff"));
									break;
							}
							
							int confirm = fileChooser.showOpenDialog(null);
							
							if (confirm == JFileChooser.APPROVE_OPTION) {
								return fileChooser.getSelectedFile();
							}
							
							return null;
						}
						
						@Override
						public void onTaskFinished(TaskFinishEvent evt) {
							progressBar.setValue(progressBar.getMaximum());
							progressBar.setString("Finished! You can exit or save a CSV/ARFF of the tweets.");
							
							final JPopupMenu trainingSaveOptions = new JPopupMenu();
							final JPopupMenu testSaveOptions = new JPopupMenu();
							
							final AbstractAction saveTrainingCSV = new AbstractAction("Save CSV") {

								/**
								 * 
								 */
								private static final long serialVersionUID = -3620307512860630174L;

								@Override
								public void actionPerformed(ActionEvent e) {
									// TODO Auto-generated method stub
									saveTrainingSetFormat(SaverFactory.TRAIN_CSV, miner.getTweets(), saveListener);
								}

							};
							
							final AbstractAction saveTestCSV = new AbstractAction("Save CSV") {
								/**
								 * 
								 */
								private static final long serialVersionUID = 1L;

								public void actionPerformed(ActionEvent e) {
									File sourceFile = openSourceTrainingSet(SaverFactory.TEST_CSV);
									
									if (sourceFile != null) {
										try {
											saveTestSetFormat(SaverFactory.TEST_CSV, new FileReader(sourceFile), miner.getTweets(), saveListener);
										} catch (FileNotFoundException e1) {
											System.err.println("File read exception occurred");
											e1.printStackTrace();
										}
									}
									else {
										progressBar.setString("A source training set wasn't selected.");
									}
								}
							};
							
							final AbstractAction saveTrainingARFF = new AbstractAction("Save ARFF") {

								/**
								 * 
								 */
								private static final long serialVersionUID = 1L;

								@Override
								public void actionPerformed(ActionEvent e) {
									saveTrainingSetFormat(SaverFactory.TRAIN_ARFF, miner.getTweets(), saveListener);
								}
								
							};
							
							final AbstractAction saveTestARFF = new AbstractAction("Save ARFF") {

								/**
								 * 
								 */
								private static final long serialVersionUID = 1L;

								@Override
								public void actionPerformed(ActionEvent e) {
									File sourceFile = openSourceTrainingSet(SaverFactory.TEST_ARFF);
									
									if (sourceFile != null) {
										try {
											saveTestSetFormat(SaverFactory.TEST_ARFF, new FileReader(sourceFile), miner.getTweets(), saveListener);
										} catch (FileNotFoundException e1) {
											System.err.println("File read exception occurred");
											e1.printStackTrace();
										}
									}
									else {
										progressBar.setString("A source training set wasn't selected.");
									}
								}
								
							};
							
							trainingSaveOptions.add(saveTrainingCSV);
							trainingSaveOptions.add(saveTrainingARFF);
							testSaveOptions.add(saveTestCSV);
							testSaveOptions.add(saveTestARFF);
							
							saveTestButton.setVisible(true);
							saveTestButton.addMouseListener(new MouseAdapter() {
								
								public void mousePressed(MouseEvent evt) {
									
									testSaveOptions.show(evt.getComponent(), evt.getX(), evt.getY());
								}
								
							});
							
							saveTrainingButton.setVisible(true);
							saveTrainingButton.addMouseListener(new MouseAdapter() {
	
								@Override
								public void mousePressed(MouseEvent arg0) {
									trainingSaveOptions.show(arg0.getComponent(), arg0.getX(), arg0.getY());
								}
								
							});
							
							startButton.setVisible(false);
						}
						
					});
					miner.addOnAuthorizationListener(new AuthorizationInputEventListener() {
	
						@Override
						public void onAuthorized(AuthorizationInputEvent evt) {
							progressBar.setValue(progressBar.getValue() + 20);
							progressBar.setString("Authorized");
							startButton.setEnabled(false);
							miner.startStream();
						}
						
					});
					miner.initialize();
					startButton.setEnabled(false);
					miner.startSearch();
				}
				catch (OutOfMemoryError out) {
					JOptionPane.showMessageDialog(frmTwitminer, "Unfortunately, the JVM was not allocated enough memory in order to run. The application cannot run.");
					System.exit(1);
				}
			}
			
		});
		
	}
	
	private void saveTrainingSetFormat(int format, List<TokenizedTweet> list, SavedEventListener listener) {
		DAOFactory daos = DAOFactory.getInstance(DAOFactory.ARRAY_LIST);
		EmotionDAO emotion = daos.getEmotionDAO();
		
		Saver saver = SaverFactory.getTrainingSaverInstance(format);
		if (listener != null) {
			saver.addSavedEventListener(listener);
		}

		saver.saveMultiple(list, emotion);
//		saver.save(list, emotion);
	}
	
	private void saveTestSetFormat(int format, Reader reader, List<TokenizedTweet> list, SavedEventListener listener) {
		DAOFactory daos = DAOFactory.getInstance(DAOFactory.ARRAY_LIST);
		EmotionDAO emotion = daos.getEmotionDAO();
		
		Saver saver = SaverFactory.getTestSaverInstance(format, reader);
		if (listener != null) {
			saver.addSavedEventListener(listener);
		}
		
		saver.saveMultiple(list, emotion);
//		saver.save(list, emotion);
	}

}

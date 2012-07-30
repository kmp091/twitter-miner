package com.twitminer.viewer.gui;

import javax.swing.JPanel;
import javax.swing.border.EtchedBorder;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

import com.twitminer.dao.EmotionDAO;
import com.twitminer.viewer.gui.images.ImageUtil;

import java.awt.Dimension;
import java.awt.Component;

public class MoodView extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7989475511088732583L;
	
	private ImageIcon happyIcon;
	private ImageIcon sadIcon;
	private ImageIcon disgustedIcon;
	private ImageIcon surprisedIcon;
	
	private JLabel lblMood;
	
	/**
	 * Create the panel.
	 */
	public MoodView() {
		setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		
		JLabel lblPredominantMood = new JLabel("Predominant Mood");
		lblPredominantMood.setAlignmentY(Component.TOP_ALIGNMENT);
		lblPredominantMood.setMaximumSize(new Dimension(32767, 16));
		lblPredominantMood.setHorizontalAlignment(SwingConstants.CENTER);
		lblPredominantMood.setHorizontalTextPosition(SwingConstants.LEADING);
		add(lblPredominantMood);
		
		lblMood = new JLabel("<html>The predominant mood among Twitter users is shown here.</html>");
		lblMood.setVerticalTextPosition(SwingConstants.BOTTOM);
		lblMood.setHorizontalAlignment(SwingConstants.CENTER);
		lblMood.setHorizontalTextPosition(SwingConstants.CENTER);
		lblMood.setMaximumSize(new Dimension(32767, 32767));
		add(lblMood);

		happyIcon = new ImageIcon(MoodView.class.getResource(ImageUtil.ROOT + "happy-face.png"));
		sadIcon = new ImageIcon(MoodView.class.getResource(ImageUtil.ROOT + "sad-face.png"));
		disgustedIcon = new ImageIcon(MoodView.class.getResource(ImageUtil.ROOT + "disgusted-face.png"));
		surprisedIcon = new ImageIcon(MoodView.class.getResource(ImageUtil.ROOT + "surprise-face.png"));
	}
	
	public void setMoodDisplay(int mood) {
		switch(mood) {
		case EmotionDAO.HAPPY:
			this.lblMood.setIcon(happyIcon);
			this.lblMood.setText("Happy");
			break;
		case EmotionDAO.SAD:
			this.lblMood.setIcon(sadIcon);
			this.lblMood.setText("Sad");
			break;
		case EmotionDAO.DISGUST:
			this.lblMood.setIcon(disgustedIcon);
			this.lblMood.setText("Disgusted");
			break;
		case EmotionDAO.SURPRISE:
			this.lblMood.setIcon(surprisedIcon);
			this.lblMood.setText("Surprised");
			break;
		}
	}
	

}

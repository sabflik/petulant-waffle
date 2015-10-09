package gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;

import speech.Combo;
import speech.CreateAudio;
import speech.Speech;
import speech.SpeechTab;
import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer;
import video.Video;

public class SpeechTools extends JPanel {
	
	private JButton speechCombo;
	private JButton createMP3;
	private JButton speak;
	private JButton speechButton;	
	private float speechTimeInMS;
	private JMenuItem speechTiming;
	private SpeechTab speechTab;

	public SpeechTools(final JFrame frame, final EmbeddedMediaPlayer mediaPlayer) {
		
		setBackground(Color.DARK_GRAY);
		setLayout(new GridBagLayout());
		
		GridBagConstraints gb = new GridBagConstraints();
		gb.fill = GridBagConstraints.HORIZONTAL;

		// SPEAK BUTTON
		gb.gridy = 0;gb.weightx = 0;gb.gridwidth = 2;gb.weighty = 0;
		speak = new JButton("Speak");
		speak.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String speech = speechTab.getText();
				Speech helper = new Speech(speech);
				helper.execute();
			}
		});
		speak.setBackground(new Color(255, 255, 255));
		speak.setFont(new Font("Tahoma", Font.PLAIN, 12));
		speak.setToolTipText("Press for Festival to speak the text entered");
		speak.setEnabled(false);
		add(speak, gb);

		// CREATE MP3 BUTTON
		gb.gridy = 1;
		createMP3 = new JButton("Create mp3");
		createMP3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					CreateAudio f = new CreateAudio(frame, "Please enter a name for your mp3 file", true, speechTab.getText());
					f.setVisible(true);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});
		createMP3.setBackground(Color.WHITE);
		createMP3.setFont(new Font("Tahoma", Font.PLAIN, 12));
		createMP3.setToolTipText("Press to convert entered text to mp3");
		createMP3.setEnabled(false);
		add(createMP3, gb);

		// SPEECH COMBO BUTTON
		gb.gridy = 2;
		speechCombo = new JButton("Combine Speech with Video");
		speechCombo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					Combo f = new Combo(frame, "", speechTab.getText(), mediaPlayer, speechTimeInMS);
					f.setVisible(true);
					Video.setVideoName(f.getNewFile());
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});
		speechCombo.setBackground(Color.WHITE);
		speechCombo.setFont(new Font("Tahoma", Font.PLAIN, 12));
		speechCombo.setToolTipText("Press to create a new video with text dubbed");
		speechCombo.setEnabled(false);
		add(speechCombo, gb);

		// This button contains speech settings
		gb.gridy = 3;
		speechButton = new JButton("Speech Settings");
		speechButton.setBackground(Color.WHITE);
		speechButton.setFont(new Font("Tahoma", Font.PLAIN, 12));
		speechButton.setBounds(250, 0, 150, 20);
		// speechButton.setEnabled(false);
		add(speechButton);

		JPopupMenu speechPopup = new JPopupMenu();
		JCheckBoxMenuItem speechCheck = new JCheckBoxMenuItem("Keep original audio");
		speechPopup.add(speechCheck);
		speechTiming = new JMenuItem("00:00");
		speechPopup.add(speechTiming);
		speechButton.setComponentPopupMenu(speechPopup);
		add(speechButton, gb);

	}
	
	public String getSpeechTiming() {
		return speechTiming.getText();
	}
	
	//Sets the selected time for mp3 placement
	public void speechTiming(float time) {
		if(time != -1) {
			speechTimeInMS = time;
			float timeInSeconds = time / 1000;
			int sec = (int)timeInSeconds % 60;
			int min = (int)(timeInSeconds / 60) % 60;
			speechTiming.setText(String.format("%02d:%02d", min, sec));
		}
	}
	
	public void setSpeechTab(SpeechTab speechTab) {
		this.speechTab = speechTab;
	}
	
	public void setComboEnabled(boolean selection) {
		speechCombo.setEnabled(selection);
	}
	
	public void setSpeakEnabled(boolean selection) {
		speak.setEnabled(selection);
	}
	
	public void setMP3Enabled(boolean selection) {
		createMP3.setEnabled(selection);
	}
}

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
	
	private JButton btnNewButton_6;
	private JButton btnNewButton_7;
	private JButton speak;
	private JButton speechButton;	
	private float speechTimeInMS;
	private JMenuItem speechTiming;

	public SpeechTools(final SpeechTab speechTab, final JFrame frame, final EmbeddedMediaPlayer mediaPlayer) {
		
		setBackground(Color.ORANGE);
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
		btnNewButton_7 = new JButton("Create mp3");
		btnNewButton_7.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					CreateAudio f = new CreateAudio(frame, "Please enter a name for your mp3 file", true, speechTab.getText());
					f.setVisible(true);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});
		btnNewButton_7.setBackground(Color.WHITE);
		btnNewButton_7.setFont(new Font("Tahoma", Font.PLAIN, 12));
		btnNewButton_7.setToolTipText("Press to convert entered text to mp3");
		btnNewButton_7.setEnabled(false);
		add(btnNewButton_7, gb);

		// SPEECH COMBO BUTTON
		gb.gridy = 2;
		btnNewButton_6 = new JButton("Combine Speech with Video");
		btnNewButton_6.addActionListener(new ActionListener() {
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
		btnNewButton_6.setBackground(Color.WHITE);
		btnNewButton_6.setFont(new Font("Tahoma", Font.PLAIN, 12));
		btnNewButton_6.setToolTipText("Press to create a new video with text dubbed");
		btnNewButton_6.setEnabled(false);
		add(btnNewButton_6, gb);

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
	
	public void setComboEnabled(boolean selection) {
		btnNewButton_6.setEnabled(selection);
	}
}

package gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer;

import combo.Combo;

public class TextPanel extends JPanel{

	private JFrame frame;
	private EmbeddedMediaPlayer mediaPlayer;
	private Video video;
	
	public TextPanel(final EmbeddedMediaPlayer mediaPlayer, final JFrame frame, Video video) {
		this.frame = frame;
		this.mediaPlayer = mediaPlayer;
		this.video = video;
		setUp();
	}
	
	private void setUp() {
		setBackground(Color.GRAY);
		setLayout(new GridBagLayout());
		
		GridBagConstraints gb = new GridBagConstraints();
		gb.fill = GridBagConstraints.HORIZONTAL;
				
		//TEXTFIELD
		gb.gridy = 1;gb.gridx = 0;gb.weightx = 1.00;gb.gridheight = 3;gb.insets = new Insets(0,10,0,0);
		final JTextArea textArea = new JTextArea();
		textArea.setRows(4);
		textArea.setToolTipText("Enter up to 30 words for each screen"); //30 word maximum means that processes called later will not take so long
		JScrollPane textScroll = new JScrollPane(textArea);
		add(textScroll, gb);

		//SPEAK BUTTON
		gb.gridx = 1;gb.gridy = 1;gb.weightx = 0;gb.gridheight = 1;gb.insets = new Insets(0,0,0,10);
		JButton btnNewButton_8 = new JButton("Speak");
		btnNewButton_8.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (textArea.getText().trim() != null && !textArea.getText().trim().equals("")) {
					String speech = textArea.getText();
					Speech helper = new Speech(speech);
					helper.execute();
				} else {
					JOptionPane.showMessageDialog(frame, "ERROR: Please enter text to be spoken");
				}
			}
		});
		btnNewButton_8.setBackground(new Color(255, 255, 255));
		btnNewButton_8.setFont(new Font("Tahoma", Font.PLAIN, 12));
		btnNewButton_8.setToolTipText("Press for Festival to speak the text entered");
		add(btnNewButton_8, gb);	

		//CREATE MP3 BUTTON
		gb.gridy = 2;
		JButton btnNewButton_7 = new JButton("Create mp3");
		btnNewButton_7.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (textArea.getText().trim() != null && !textArea.getText().trim().equals("")) {
					try {
						CreateAudio f = new CreateAudio(frame, "Please enter a name for your mp3 file", true, textArea.getText());
						f.setVisible(true);
					} catch (IOException e) {
						e.printStackTrace();
					}
				} else {
					JOptionPane.showMessageDialog(frame, "ERROR: Please enter text to be converted");
				}
			}
		});
		btnNewButton_7.setBackground(Color.WHITE);
		btnNewButton_7.setFont(new Font("Tahoma", Font.PLAIN, 12));
		btnNewButton_7.setToolTipText("Press to convert entered text to mp3");
		add(btnNewButton_7, gb);	

		//COMBO BUTTON
		gb.gridy = 3;
		JButton btnNewButton_6 = new JButton("Combine Speech with Video");
		btnNewButton_6.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (textArea.getText().trim() != null && !textArea.getText().trim().equals("")) {
					try {
						Combo f = new Combo(frame, "", video, textArea.getText(), mediaPlayer);
						f.setVisible(true);
						Video.setVideoName(f.getNewFile());
					} catch (IOException e) {
						e.printStackTrace();
					}
				} else {
					JOptionPane.showMessageDialog(frame, "ERROR: Please enter text to be converted");
				}
			}
		});
		btnNewButton_6.setBackground(Color.WHITE);
		btnNewButton_6.setFont(new Font("Tahoma", Font.PLAIN, 12));
		btnNewButton_6.setToolTipText("Press to create a new video with text dubbed");
		add(btnNewButton_6, gb);
	}
}

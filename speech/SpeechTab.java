package speech;

import gui.MenuPanel;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;


import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer;
import video.Video;

public class SpeechTab extends JPanel {

	private JFrame frame;
	private EmbeddedMediaPlayer mediaPlayer;
	private Video video;
	private JButton btnNewButton_6;
	private JButton btnNewButton_7;
	private JButton speak;
	private JButton speechButton;
	private JMenuItem speechTiming;
	private JTextArea textArea;
	private float speechTimeInMS;
	
	public SpeechTab(final EmbeddedMediaPlayer mediaPlayer, final JFrame frame) {
		this.frame = frame;
		this.mediaPlayer = mediaPlayer;
		setUp();
	}
	
private void setUp() {
		
//		menu.attachTextObserver(this);
		
		setBackground(Color.GRAY);
		setLayout(new GridBagLayout());
		
		GridBagConstraints gb = new GridBagConstraints();
		gb.fill = GridBagConstraints.HORIZONTAL;
				
		//TEXT LABEL
		gb.gridy = 0;gb.insets = new Insets(0,10,0,10);
		JLabel t_label = new JLabel("Enter Commentary Here");
		t_label.setFont(new Font("Tahoma", Font.PLAIN, 10));
		t_label.setForeground(Color.orange);
		add(t_label, gb);
		
		//CHAR LIMIT LABEL
		gb.gridy = 2;
		final JLabel cl_label = new JLabel("Characters Remaining: 200");
		cl_label.setFont(new Font("Tahoma", Font.PLAIN, 10));
		cl_label.setForeground(Color.orange);
		add(cl_label, gb);
		
		//TEXTAREA
		gb.fill = GridBagConstraints.BOTH;
		gb.gridy = 1;gb.gridx = 0;gb.weightx = 1.00;gb.gridwidth = 10;
		final JTextArea textArea = new JTextArea();
		// Buttons are disabled if text isn't entered into the text area
		DocumentListener documentListener = new DocumentListener() {
		      public void changedUpdate(DocumentEvent documentEvent) {
		        printIt(documentEvent);
		      }
		      public void insertUpdate(DocumentEvent documentEvent) {
		        printIt(documentEvent);
		      }
		      public void removeUpdate(DocumentEvent documentEvent) {
		        printIt(documentEvent);
		      }
		      private void printIt(DocumentEvent documentEvent) {
		    	  if(textArea.getText().trim() != null && !textArea.getText().trim().equals("")) {
		    		  speak.setEnabled(true);
		    		  btnNewButton_7.setEnabled(true);
		    		  if(Video.getVideoName() != null) {
		    			  btnNewButton_6.setEnabled(true);
		    		  }
		    	  } else {
		    		  btnNewButton_6.setEnabled(false);
		    		  btnNewButton_7.setEnabled(false);
		    		  speak.setEnabled(false);
		    	  }
		    	  int chars = textArea.getDocument().getLength();
		    	  if(200 - chars >= 0) {
		    		  cl_label.setText("Characters Remaining: "+(200 - chars));
		    	  } 
		      }
		};
		textArea.getDocument().addDocumentListener(documentListener);
		textArea.setToolTipText("Enter up to 30 words for each screen"); //30 word maximum means that processes called later will not take so long
		textArea.setRows(20);// Sets the height of text area
		textArea.setLineWrap(true); // Ensure that the text wraps
		textArea.setWrapStyleWord(true); // Ensure that when wrapping, it splits at words
		add(textArea, gb);
		
		//SPEAK BUTTON
		gb.fill = GridBagConstraints.HORIZONTAL;
		gb.gridy = 4;gb.weightx = 0;gb.gridwidth = 2;gb.gridheight = 1;gb.weighty = 0;
		speak = new JButton("Speak");
		speak.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
					String speech = textArea.getText();
					Speech helper = new Speech(speech);
					helper.execute();
			}
		});
		speak.setBackground(new Color(255, 255, 255));
		speak.setFont(new Font("Tahoma", Font.PLAIN, 12));
		speak.setToolTipText("Press for Festival to speak the text entered");
		speak.setEnabled(false);
		add(speak, gb);	
		
//		textArea.addKeyListener(new KeyListener() {
//
//			@Override
//			public void keyPressed(KeyEvent arg0) {
//				if ((arg0.getKeyChar() == KeyEvent.VK_ENTER) && (btnNewButton_8.isEnabled())) {
//					String speech = textArea.getText();
//					Speech helper = new Speech(speech);
//					helper.execute();
//				}
//			}
//
//			@Override
//			public void keyReleased(KeyEvent e) {
//				
//			}
//
//			@Override
//			public void keyTyped(KeyEvent e) {
//				
//			}
//			
//		});

		//CREATE MP3 BUTTON
		gb.gridy = 5;
		btnNewButton_7 = new JButton("Create mp3");
		btnNewButton_7.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
					try {
						CreateAudio f = new CreateAudio(frame, "Please enter a name for your mp3 file", true, textArea.getText());
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

		//SPEECH COMBO BUTTON
		gb.gridy = 6;
		btnNewButton_6 = new JButton("Combine Speech with Video");
		btnNewButton_6.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					Combo f = new Combo(frame, "", video, textArea.getText(), mediaPlayer, speechTimeInMS);
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
	
		//This button contains speech settings
		gb.gridy = 7;
		speechButton = new JButton("Speech Settings");
		speechButton.setBackground(Color.WHITE);
		speechButton.setFont(new Font("Tahoma", Font.PLAIN, 12));
		speechButton.setBounds(250, 0, 150, 20);
//		speechButton.setEnabled(false);
		add(speechButton);
		
		JPopupMenu speechPopup = new JPopupMenu();
		JCheckBoxMenuItem speechCheck = new JCheckBoxMenuItem("Keep original audio");
		speechPopup.add(speechCheck);
		speechTiming = new JMenuItem("00:00");
		speechPopup.add(speechTiming);
		speechButton.setComponentPopupMenu(speechPopup);	
		add(speechButton, gb);
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
	
	public String getSpeechTiming() {
		return speechTiming.getText();
	}
	
	public boolean isTextEnabled() {
		return (textArea.getText().trim() != null && !textArea.getText().trim().equals(""));
	}
	
	public void setComboEnabled(boolean selection) {
		btnNewButton_6.setEnabled(selection);
	}
	
	public void update() {
		
	}
}

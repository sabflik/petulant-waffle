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
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import org.w3c.dom.Document;

import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer;

import combo.Combo;

public class TextPanel extends JPanel{

	private JFrame frame;
	private EmbeddedMediaPlayer mediaPlayer;
	private Video video;
	JButton btnNewButton_6;
	JButton btnNewButton_7;
	JButton btnNewButton_8;
	
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
				
		//TEXT LABEL
		gb.gridy = 0;gb.insets = new Insets(0,10,0,0);
		JLabel t_label = new JLabel("Enter Commentary Here");
		t_label.setFont(new Font("Tahoma", Font.PLAIN, 10));
		add(t_label, gb);
		
		//CHAR LIMIT LABEL
		gb.gridy = 4;gb.insets = new Insets(0,10,0,0);
		final JLabel cl_label = new JLabel("Characters Remaining: 200");
		cl_label.setFont(new Font("Tahoma", Font.PLAIN, 10));
		add(cl_label, gb);
		
		//TEXTAREA
		gb.gridy = 1;gb.gridx = 0;gb.weightx = 1.00;gb.gridheight = 3;
		final JTextArea textArea = new JTextArea();
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
		    		  btnNewButton_6.setEnabled(true);
		    		  btnNewButton_7.setEnabled(true);
		    		  btnNewButton_8.setEnabled(true);
		    	  } else {
		    		  btnNewButton_6.setEnabled(false);
		    		  btnNewButton_7.setEnabled(false);
		    		  btnNewButton_8.setEnabled(false);
		    	  }
		    	  int chars = textArea.getDocument().getLength();
		    	  if(200 - chars >= 0) {
		    		  cl_label.setText("Characters Remaining: "+(200 - chars));
		    	  } 
		      }
		};
		textArea.getDocument().addDocumentListener(documentListener);
		textArea.setRows(4);
		textArea.setToolTipText("Enter up to 30 words for each screen"); //30 word maximum means that processes called later will not take so long
		JScrollPane textScroll = new JScrollPane(textArea);
		add(textScroll, gb);

		//SPEAK BUTTON
		gb.gridx = 1;gb.gridy = 1;gb.weightx = 0;gb.gridheight = 1;gb.insets = new Insets(0,0,0,10);
		btnNewButton_8 = new JButton("Speak");
		btnNewButton_8.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
					String speech = textArea.getText();
					Speech helper = new Speech(speech);
					helper.execute();
			}
		});
		btnNewButton_8.setBackground(new Color(255, 255, 255));
		btnNewButton_8.setFont(new Font("Tahoma", Font.PLAIN, 12));
		btnNewButton_8.setToolTipText("Press for Festival to speak the text entered");
		btnNewButton_8.setEnabled(false);
		add(btnNewButton_8, gb);	

		//CREATE MP3 BUTTON
		gb.gridy = 2;
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

		//COMBO BUTTON
		gb.gridy = 3;
		btnNewButton_6 = new JButton("Combine Speech with Video");
		btnNewButton_6.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
					try {
						Combo f = new Combo(frame, "", video, textArea.getText(), mediaPlayer);
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
	}
}

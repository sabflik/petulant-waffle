package speech;

import gui.SpeechTools;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import video.Video;

public class SpeechTab extends JPanel {

	private JMenuItem speechTiming;
	private JTextArea textArea;
	private SpeechTools sTools;
	
	public SpeechTab() {
		// menu.attachTextObserver(this);

		setBackground(Color.GRAY);
		setLayout(new GridBagLayout());

		GridBagConstraints gb = new GridBagConstraints();
		gb.fill = GridBagConstraints.HORIZONTAL;

		// TEXT LABEL
		gb.gridy = 0;
		gb.insets = new Insets(0, 10, 0, 10);
		JLabel t_label = new JLabel("Enter Commentary Here");
		t_label.setFont(new Font("Tahoma", Font.PLAIN, 10));
		t_label.setForeground(Color.orange);
		add(t_label, gb);

		// CHAR LIMIT LABEL
		gb.gridy = 2;
		final JLabel cl_label = new JLabel("Characters Remaining: 200");
		cl_label.setFont(new Font("Tahoma", Font.PLAIN, 10));
		cl_label.setForeground(Color.orange);
		add(cl_label, gb);

		// TEXTAREA
		gb.fill = GridBagConstraints.BOTH;
		gb.gridy = 1;
		gb.gridx = 0;
		gb.weightx = 1.00;
		gb.gridwidth = 10;
		
		textArea = new JTextArea();
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
				if (textArea.getText().trim() != null
						&& !textArea.getText().trim().equals("")) {
					sTools.setSpeakEnabled(true);
					sTools.setMP3Enabled(true);
					if (Video.getVideoName() != null) {
						sTools.setComboEnabled(true);
					}
				} else {
					sTools.setMP3Enabled(false);
					sTools.setSpeakEnabled(false);
					sTools.setComboEnabled(false);
				}
				int chars = textArea.getDocument().getLength();
				if (200 - chars >= 0) {
					cl_label.setText("Characters Remaining: " + (200 - chars));
				}
			}
		};
		textArea.getDocument().addDocumentListener(documentListener);
		textArea.setToolTipText("Enter up to 30 words for each screen"); 
		textArea.setRows(20);// Sets the height of text area
		textArea.setLineWrap(true); // Ensure that the text wraps
		textArea.setWrapStyleWord(true); // Ensure that when wrapping, it splits
											// at words
		add(textArea, gb);
	}

	public void setSpeechTools(SpeechTools sTools) {
		this.sTools = sTools;
	}

	public String getSpeechTiming() {
		return speechTiming.getText();
	}

	public boolean isTextEnabled() {
		return (textArea.getText().trim() != null && !textArea.getText().trim().equals(""));
	}

	public String getText() {
		return textArea.getText();
	}

	public void update() {

	}
}

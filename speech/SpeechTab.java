package speech;


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
import javax.swing.text.AbstractDocument;

import video.Video;
import vidivox.guicomponents.SpeechTools;

public class SpeechTab extends JPanel {

	private static final long serialVersionUID = 1L;
	private JMenuItem speechTiming;
	private JTextArea textArea;
	private SpeechTools sTools;
	
	public SpeechTab() {

		setBackground(Color.black);
		setLayout(new GridBagLayout());

		GridBagConstraints gb = new GridBagConstraints();
		gb.fill = GridBagConstraints.HORIZONTAL;

		// TEXT LABEL
		gb.gridy = 0;gb.insets = new Insets(0, 10, 0, 10);
		JLabel t_label = new JLabel("Type Your Commentary In Text Box Below");
		t_label.setFont(new Font("Tahoma", Font.PLAIN, 14));
		t_label.setForeground(Color.orange);
		add(t_label, gb);

		// CHAR LIMIT LABEL
		gb.gridy = 2;
		final JLabel cl_label = new JLabel("Characters Remaining: 200");
		cl_label.setFont(new Font("Tahoma", Font.PLAIN, 14));
		cl_label.setForeground(Color.orange);
		add(cl_label, gb);

		// TEXTAREA
		gb.fill = GridBagConstraints.BOTH;gb.gridy = 1;gb.gridx = 0;gb.weightx = 1.00;gb.gridwidth = 10;
		
		textArea = new JTextArea();
		textArea.setFont(new Font("Verdana", Font.PLAIN, 20));
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
		
		//Set max char limit
		CharLimitFilter limitFilter = new CharLimitFilter(200);
	    ((AbstractDocument) textArea.getDocument()).setDocumentFilter(limitFilter);

	    textArea.setToolTipText("Enter text to be spoken here"); 
		textArea.setRows(15);// Sets the height of text area
		textArea.setLineWrap(true); // Ensure that the text wraps
		textArea.setWrapStyleWord(true); // Ensure that when wrapping, it splits
											// at words
		textArea.setForeground(Color.white);
		textArea.setBackground(Color.GRAY);
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
		String speech = textArea.getText();
		// Remove all characters that would interfere with commands 
		// (newline, apostrophe and quotation)
		speech = speech.replace("\n", " ");
		speech = speech.replace("'", "");
		speech = speech.replace("\"", "");
		return speech;
	}

	public void setText(String speech) {
		textArea.setText(speech);
	}
}

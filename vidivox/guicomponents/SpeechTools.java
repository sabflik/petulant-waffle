package vidivox.guicomponents;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JToggleButton;
import speech.SpeechTab;
import speech.swingworkers.ComboCreationWorker;
import speech.swingworkers.CreateAudio;
import speech.swingworkers.Speech;
import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer;
import vidivox.FileNameFilter;
import vidivox.swingworkers.ProgressLoader;

/**This class represents the speech tools panel where all the speech settings are displayed.
 * It works with the speech tab to provide speech functionality.
 * @author Sabrina
 */
public class SpeechTools extends JPanel {

	private static final long serialVersionUID = 1L;
	private JButton speechCombo;
	private JButton createMP3;
	private JToggleButton speak;
	private JLabel settings;
	private float speechTimeInMS;
	private JLabel speechTiming;
	private SpeechTab speechTab;
	private Speech helper;
	private JRadioButton male;
	private JRadioButton female;

	/**
	 * @param frame			The main JFrame
	 * @param directory		The default Directory
	 * @param mediaPlayer	The media player instance
	 * @param mTools		The mp3 tools panel
	 * @param label			The label displaying the currently selected video
	 */
	public SpeechTools(final JFrame frame, final File directory, final EmbeddedMediaPlayer mediaPlayer, 
			final MP3Tools mTools, final VideoLabel label) {

		setBackground(Color.DARK_GRAY);
		setLayout(new GridBagLayout());
		//Implements GreidBagLayout
		GridBagConstraints gb = new GridBagConstraints();
		gb.fill = GridBagConstraints.HORIZONTAL;

		// Speech settings Label
		gb.gridy = 0;gb.gridwidth = 2;
		settings = new JLabel("Speech Settings");
		settings.setFont(new Font("Tahoma", Font.PLAIN, 14));
		settings.setForeground(Color.orange);
		add(settings, gb);

		// VOICE SELECTION - Can select Male or Female voice with JRadioButton
		gb.gridy = 1;gb.gridx = 0;gb.gridwidth = 1;
		male = new JRadioButton("Male");// Male button
		male.setFont(new Font("Tahoma", Font.PLAIN, 12));
		male.setBackground(Color.DARK_GRAY);
		male.setForeground(Color.cyan);
		
		female = new JRadioButton("Female (Robot)");// Female button
		female.setFont(new Font("Tahoma", Font.PLAIN, 12));
		female.setBackground(Color.DARK_GRAY);
		female.setForeground(Color.pink);
		
		ButtonGroup genderGroup = new ButtonGroup();// Button group
		genderGroup.add(male);
		genderGroup.add(female);
		male.setSelected(true);
		add(male, gb);
		gb.gridx = 1;
		add(female, gb);

		// SPEAK BUTTON - Calls SwingWorker process to speak entered text
		gb.gridx = 0;gb.gridy = 2;gb.weightx = 0;gb.gridwidth = 2;gb.weighty = 0;
		speak = new JToggleButton("Speak");
		speak.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (speak.isSelected()) {
					speak.setText("Cancel");
					helper = new Speech(speechTab.getText(), speak, male.isSelected());
					helper.execute();
				} else {
					speak.setText("Speak");
					helper.cancel(true);
				}
			}
		});
		speak.setBackground(new Color(255, 255, 255));
		speak.setFont(new Font("Tahoma", Font.PLAIN, 12));
		speak.setToolTipText("Press for Festival to speak the text entered");
		speak.setEnabled(false);
		add(speak, gb);

		// CREATE MP3 BUTTON - Creates mp3 from entered text
		gb.gridy = 3;
		createMP3 = new JButton("Create mp3");
		createMP3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				JFileChooser chooser = new JFileChooser();
				chooser.setCurrentDirectory(directory);
				int returnVal = chooser.showSaveDialog(null);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					// Make sure file name is valid
					FileNameFilter filter = new FileNameFilter();
					if(!filter.isValid(chooser.getSelectedFile().getAbsolutePath())){ 
				        JOptionPane.showMessageDialog(null, "The filename " // Show error dialog if not valid
				        		+ chooser.getSelectedFile().getAbsolutePath() + ".avi is invalid.", 
				        		"Save As Error", JOptionPane.ERROR_MESSAGE); 
					} else {// If valid, then show progress while waiting for file creation
						ProgressLoader progress = new ProgressLoader(frame);
						progress.execute();
						CreateAudio createMP3;// Create mp3 from text and save file
						createMP3 = new CreateAudio(frame, mTools, speechTab.getText(),
								chooser, progress, male.isSelected());
						createMP3.execute();
					}
				}
			}
		});
		createMP3.setBackground(Color.WHITE);
		createMP3.setFont(new Font("Tahoma", Font.PLAIN, 12));
		createMP3.setToolTipText("Press to convert entered text to mp3");
		createMP3.setEnabled(false);
		add(createMP3, gb);

		// SPEECH COMBO BUTTON
		gb.gridy = 4;
		speechCombo = new JButton("Combine Speech & Video");
		speechCombo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				JFileChooser chooser = new JFileChooser();
				chooser.setCurrentDirectory(directory);
				int returnVal = chooser.showSaveDialog(null);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					// Make sure file name is valid
					FileNameFilter filter = new FileNameFilter();
					if(!filter.isValid(chooser.getSelectedFile().getAbsolutePath())){ 
				        JOptionPane.showMessageDialog(null, "The filename "  // Show error dialog if not valid
				        		+ chooser.getSelectedFile().getAbsolutePath() + ".avi is invalid.", 
				        		"Save As Error", JOptionPane.ERROR_MESSAGE); 
					} else {// If valid, then show progress while waiting for file creation
						ProgressLoader progress = new ProgressLoader(frame);
						progress.execute();
						ComboCreationWorker cc;// Merge Speech with Video and save file
						cc = new ComboCreationWorker(mediaPlayer, label, frame, chooser, speechTimeInMS,
								progress, speechTab.getText(), male.isSelected());
						cc.execute();
					}
				}
			}
		});
		speechCombo.setBackground(Color.WHITE);
		speechCombo.setFont(new Font("Tahoma", Font.PLAIN, 12));
		speechCombo
				.setToolTipText("Press to create a new video with text dubbed");
		speechCombo.setEnabled(false);
		add(speechCombo, gb);

		// SPEECH TIMING LABEL
		gb.gridy = 5;// Displays time at which speech will be added
		speechTiming = new JLabel("Add speech at: 00:00");
		speechTiming.setFont(new Font("Tahoma", Font.PLAIN, 14));
		speechTiming.setForeground(Color.orange);
		speechTiming
				.setToolTipText("Right click on progressbar and select 'Add speech here'");
		add(speechTiming, gb);

		// INFORMATIVE LABEL
		gb.gridy = 6;// Tells the user how to change the time
		JLabel disclaimer1 = new JLabel("Right click on the progress bar");
		disclaimer1.setFont(new Font("Tahoma", Font.PLAIN, 10));
		disclaimer1.setForeground(Color.white);
		add(disclaimer1, gb);
		gb.gridy = 7;
		JLabel disclaimer2 = new JLabel("to change the time");
		disclaimer2.setFont(new Font("Tahoma", Font.PLAIN, 10));
		disclaimer2.setForeground(Color.white);
		add(disclaimer2, gb);
	}

	public float getSpeechTiming() {
		return speechTimeInMS;
	}

	/**Sets the selected time for speech placement by converting milliseconds to 
	 * the correct format.
	 * @param time	The time in milliseconds where the speech needs to be added
	 */
	public void speechTiming(float time) {
		if (time != -1) {
			speechTimeInMS = time;
			float timeInSeconds = time / 1000;
			int sec = (int) timeInSeconds % 60;
			int min = (int) (timeInSeconds / 60) % 60;
			String text = "Add speech at: "
					+ String.format("%02d:%02d", min, sec);
			speechTiming.setText(text);
		}
	}

	/**@param speechTab	This class needs to work with the Speech tab*/
	public void setSpeechTab(SpeechTab speechTab) {
		this.speechTab = speechTab;
	}
	/**Enables/Disables speech combo button**/
	public void setComboEnabled(boolean selection) {
		speechCombo.setEnabled(selection);
	}
	/**Enables/Disables speak button**/
	public void setSpeakEnabled(boolean selection) {
		speak.setEnabled(selection);
	}
	/**Enables/Disables create mp3 button**/
	public void setMP3Enabled(boolean selection) {
		createMP3.setEnabled(selection);
	}
	/**@return	Whether male voice is selected or not*/
	public boolean isMaleSelected() {
		return male.isSelected();
	}
	/**@param selection	Choose the gender selection*/
	public void setMaleSelected(boolean selection) {
		if(!selection) {
			female.doClick();
		}
	}
	/**Clicks speak button**/
	public void clickSpeakCancel() {
		speak.doClick();
	}
}
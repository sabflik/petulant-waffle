package vidivox.guicomponents;


import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JToggleButton;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

import mp3.MP3;
import mp3.swingworkers.MP3OverlayWorker;
import uk.co.caprica.vlcj.component.AudioMediaPlayerComponent;
import uk.co.caprica.vlcj.player.MediaPlayer;
import uk.co.caprica.vlcj.player.MediaPlayerEventAdapter;
import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer;
import video.Video;
import vidivox.FileNameFilter;
import vidivox.swingworkers.ProgressLoader;

/**This class represents the mp3 tools panel where all the mp3 settings are displayed.
 * @author Sabrina
 */
public class MP3Tools extends JPanel {

	private static final long serialVersionUID = 1L;
	private JButton mp3Button;
	private JButton chooseMP3;
	private JLabel mp3Timing;
	private float mp3TimeInMS;
	private JLabel mp3;
	private JToggleButton mp3Play;

	/**
	 * @param frame			The main JFrame
	 * @param directory		The default directory
	 * @param mediaPlayer	The media player instance
	 * @param label			The label displaying the currently selected video
	 */
	public MP3Tools(final JFrame frame, final File directory, 
			final EmbeddedMediaPlayer mediaPlayer, final VideoLabel label) {
		setBackground(Color.DARK_GRAY);
		setLayout(new GridBagLayout());
		//Implements GreidBagLayout
		GridBagConstraints gb = new GridBagConstraints();
		gb.fill = GridBagConstraints.HORIZONTAL;
		
		// SETTINGS LABEL
		gb.gridy = 1;
		JLabel settingB = new JLabel("MP3 Settings");
		settingB.setFont(new Font("Tahoma", Font.PLAIN, 14));
		settingB.setForeground(Color.orange);
		add(settingB, gb);
		
		// CHOOSE MP3 FILE BUTTON 
		// This button imports the mp3 file to be played
		gb.gridy = 2;
		chooseMP3 = new JButton("Choose MP3");
		chooseMP3.setBackground(Color.WHITE);
		chooseMP3.addActionListener(new ActionListener() {
			@SuppressWarnings("static-access")
			public void actionPerformed(ActionEvent arg0) {
				JFileChooser fChooser = new JFileChooser();
				fChooser.setCurrentDirectory(directory);
				fChooser.setAcceptAllFileFilterUsed(false);
				FileFilter filter = new FileNameExtensionFilter("mp3 files","mp3");
				// JFileChooser allows user to find and select any .mp3 files
				fChooser.addChoosableFileFilter(filter);
				int number = fChooser.showDialog(null, "Choose mp3 File");

				if (number == fChooser.APPROVE_OPTION) {
					String audio = fChooser.getSelectedFile().getAbsolutePath();
					MP3.setMP3Name(audio);// Sets the selected MP3
					if(Video.getVideoName() != null) {// Enable buttons where applicable
						mp3Button.setEnabled(true);
					}
					mp3Play.setEnabled(true);
					setMP3Selected();
				}
			}
		});
		chooseMP3.setFont(new Font("Tahoma", Font.PLAIN, 12));
		add(chooseMP3, gb);
		
		// CURRENT MP3 CHOSEN
		gb.gridy = 3;
		mp3 = new JLabel("No MP3 chosen");
		mp3.setFont(new Font("Tahoma", Font.PLAIN, 10));
		mp3.setForeground(Color.orange);
		mp3.setMaximumSize(new Dimension(100, 50));
		add(mp3, gb);

		// PLAY SELECTED MP3
		gb.gridy = 4;
		mp3Play = new JToggleButton("Play MP3");
		final AudioMediaPlayerComponent playMP3 = new AudioMediaPlayerComponent();
		mp3Play.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(mp3Play.isSelected()) {
					mp3Play.setText("Cancel");
					playMP3.getMediaPlayer().playMedia(MP3.getMP3Name());
		// Reset button when done playing. 
    //Code from: http://stackoverflow.com/questions/26909772/vlcj-mediaplayer-how-to-detect-when-video-has-finished-playing
					playMP3.getMediaPlayer().addMediaPlayerEventListener(new MediaPlayerEventAdapter() {
					    @Override
					    public void finished(MediaPlayer mediaPlayer) {
					    	mp3Play.setText("Play MP3");
					    	mp3Play.setSelected(false);
					    }
					});
				} else {
					mp3Play.setText("Play MP3");
					playMP3.getMediaPlayer().stop();
				}
			}
		});
		mp3Play.setFont(new Font("Tahoma", Font.PLAIN, 12));
		mp3Play.setBackground(Color.white);
		mp3Play.setEnabled(false);
		add(mp3Play, gb);
		
		// MP3 COMBO BUTTON
		gb.gridy = 5;
		mp3Button = new JButton("Combine MP3 & Video");
		mp3Button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				JFileChooser chooser = new JFileChooser();
				chooser.setCurrentDirectory(directory);
				int returnVal = chooser.showSaveDialog(null);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					// Make sure file name is valid
					// Code from: https://community.oracle.com/message/5491217
					FileNameFilter filter = new FileNameFilter();
					if(!filter.isValid(chooser.getSelectedFile().getAbsolutePath())){ 
				        JOptionPane.showMessageDialog(null, "The filename " // Show error dialog if not valid
				        		+ chooser.getSelectedFile().getAbsolutePath() + ".mp3 is invalid.", 
				        		"Save As Error", JOptionPane.ERROR_MESSAGE); 
					} else {// If valid, then show progress while waiting for file creation
						ProgressLoader progress = new ProgressLoader(frame);
						progress.execute();
						// Merge MP3 with Video and save file
						MP3OverlayWorker overlay = new MP3OverlayWorker(mediaPlayer, label, frame, 
								mp3TimeInMS, chooser, progress);
						overlay.execute();
					}
				}
			}
		});
		mp3Button.setBackground(Color.WHITE);
		mp3Button.setFont(new Font("Tahoma", Font.PLAIN, 12));
		mp3Button
				.setToolTipText("Press to create a new video with text dubbed");
		mp3Button.setEnabled(false);
		add(mp3Button, gb);

		// MP3 TIMING LABEL
		gb.gridy = 6;// Displays time at which mp3 will be added
		mp3Timing = new JLabel("Add mp3 at: 00:00");
		mp3Timing.setFont(new Font("Tahoma", Font.PLAIN, 14));
		mp3Timing.setForeground(Color.orange);
		mp3Timing
				.setToolTipText("Right click on progressbar and select 'Add mp3 here'");
		add(mp3Timing, gb);
		
		// INFORMATIVE LABEL
		gb.gridy = 7;// Tells the user how to change the time
		JLabel disclaimer1 = new JLabel("Right click on the progress bar");
		disclaimer1.setFont(new Font("Tahoma", Font.PLAIN, 10));
		disclaimer1.setForeground(Color.white);
		add(disclaimer1, gb);
		gb.gridy = 8;
		JLabel disclaimer2 = new JLabel("to change the time");
		disclaimer2.setFont(new Font("Tahoma", Font.PLAIN, 10));
		disclaimer2.setForeground(Color.white);
		add(disclaimer2, gb);
	}

	/**Sets the selected time for mp3 placement by converting milliseconds to 
	 * the correct format.
	 * @param time	The time in milliseconds where the mp3 needs to be added
	 */
	public void mp3Timing(float time) {
		if (time != -1) {
			mp3TimeInMS = time;
			float timeInSeconds = time / 1000;
			int sec = (int) timeInSeconds % 60;
			int min = (int) (timeInSeconds / 60) % 60;
			String text = "Add mp3 at: " + String.format("%02d:%02d", min, sec);
			mp3Timing.setText(text);
		}
	}

	/**
	 * @return	The selected time for mp3 placement in milliseconds
	 */
	public float getMP3Timing() {
		return mp3TimeInMS;
	}
	/**Enables/Disables choosemp3 button**/
	public void setChooseMP3Enabled(boolean selection) {
		chooseMP3.setEnabled(selection);
	}
	/**Sets the name of the mp3**/
	public void setMP3Selected() {
		File mp3File = new File(MP3.getMP3Name());
		mp3.setText("Chosen MP3: " + mp3File.getName());
	}
	/**Enables/Disables play mp3 button**/
	public void setMP3PlayEnabled(boolean selection) {
		mp3Play.setEnabled(selection);
	}
	/**Enables/Disables mp3 combo button**/
	public void setMP3ComboEnabled(boolean selection) {
		mp3Button.setEnabled(selection);
	}
	/**Clicks mp3 play button**/
	public void clickMP3Cancel() {
		mp3Play.doClick();
	}
}

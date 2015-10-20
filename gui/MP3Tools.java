package gui;

import java.awt.Color;
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
import javax.swing.JPanel;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

import mp3.MP3;
import mp3.MP3OverlayWorker;

public class MP3Tools extends JPanel {

	private static final long serialVersionUID = 1L;
	private JButton mp3Button;
	private JButton chooseMP3;
	private JLabel mp3Timing;
	private float mp3TimeInMS;

	public MP3Tools(final JFrame frame, final File directory) {
		setBackground(Color.DARK_GRAY);
		setLayout(new GridBagLayout());

		GridBagConstraints gb = new GridBagConstraints();
		gb.fill = GridBagConstraints.HORIZONTAL;

		// SETTINGS LABEL
		gb.gridx = 1;gb.gridy = 0;
		JLabel settingB = new JLabel("MP3 Settings");
		settingB.setFont(new Font("Tahoma", Font.PLAIN, 14));
		settingB.setForeground(Color.orange);
		add(settingB, gb);
		
		// CHOOSE MP3 FILE BUTTON 
		// This button imports the mp3 file to be played
		gb.gridx = 1;gb.gridy = 1;
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
					MP3.setMP3Name(audio);
					mp3Button.setEnabled(true);
				}
			}
		});
		chooseMP3.setFont(new Font("Tahoma", Font.PLAIN, 12));
		chooseMP3.setBounds(100, 0, 150, 20);
		// chooseMP3.setEnabled(false);
		chooseMP3.setEnabled(false);
		add(chooseMP3, gb);

		// MP3 COMBO BUTTON
		gb.gridy = 2;
		mp3Button = new JButton("Combine MP3 & Video");
		mp3Button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				JFileChooser chooser = new JFileChooser();
				chooser.setCurrentDirectory(directory);
				int returnVal = chooser.showSaveDialog(null);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					ProgressLoader progress = new ProgressLoader(frame);
					progress.execute();

					MP3OverlayWorker overlay = new MP3OverlayWorker(
							mp3TimeInMS, chooser.getSelectedFile().getAbsolutePath(), progress);
					overlay.execute();
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
		gb.gridy = 3;
		mp3Timing = new JLabel("Add mp3 at: 00:00");
		mp3Timing.setFont(new Font("Tahoma", Font.PLAIN, 14));
		mp3Timing.setForeground(Color.orange);
		mp3Timing
				.setToolTipText("Right click on progressbar and select 'Add mp3 here'");
		add(mp3Timing, gb);
		
		// INFORMATIVE LABEL
		gb.gridy = 4;
		JLabel disclaimer1 = new JLabel("Right click on the progress bar");
		disclaimer1.setFont(new Font("Tahoma", Font.PLAIN, 10));
		disclaimer1.setForeground(Color.white);
		add(disclaimer1, gb);
		gb.gridy = 5;
		JLabel disclaimer2 = new JLabel("to change the time");
		disclaimer2.setFont(new Font("Tahoma", Font.PLAIN, 10));
		disclaimer2.setForeground(Color.white);
		add(disclaimer2, gb);
	}

	// Sets the selected time for mp3 placement
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

	// Gets the selected time for mp3 placement
	public String getMP3Timing() {
		return mp3Timing.getText();
	}

	public void setChooseMP3Enabled(boolean selection) {
		chooseMP3.setEnabled(selection);
	}
}

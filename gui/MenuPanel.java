package gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

import speech.*;
import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer;
import video.*;

//Observer pattern from http://www.tutorialspoint.com/design_pattern/observer_pattern.htm
public class MenuPanel extends JPanel {

	private static final long serialVersionUID = 1L;

	public MenuPanel(final EmbeddedMediaPlayer mediaPlayer,
			final SpeechTab sTab, final SpeechTools sTools,
			final MP3Tools mTools, final File directory) {

		setBackground(Color.GRAY);
		setLayout(null);

		// This button imports the video file to be played
		final JButton btnNewButton = new JButton("Select Video");
		btnNewButton.setBackground(Color.WHITE);
		btnNewButton.addActionListener(new ActionListener() {
			@SuppressWarnings("static-access")
			public void actionPerformed(ActionEvent arg0) {
				JFileChooser fChooser = new JFileChooser();
				fChooser.setCurrentDirectory(directory);
				fChooser.setAcceptAllFileFilterUsed(false);
				FileFilter filter = new FileNameExtensionFilter("avi, mp4",
						"avi", "mp4"); // JFileChooser allows user to find and
										// select any files with .avi and .mp4
										// extension
				fChooser.addChoosableFileFilter(filter);
				int number = fChooser.showDialog(null, "Choose Video");

				if (number == fChooser.APPROVE_OPTION) {
					String video = fChooser.getSelectedFile().getAbsolutePath();
					Video.setVideoName(video);
					mediaPlayer.playMedia(video);
					if (sTab.isTextEnabled()) {
						sTools.setComboEnabled(true);
					}
					mTools.setChooseMP3Enabled(true);
				}
			}
		});
		btnNewButton.setFont(new Font("Tahoma", Font.PLAIN, 12));
		btnNewButton.setBounds(0, 0, 150, 20);
		add(btnNewButton);
	}
}

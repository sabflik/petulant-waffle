package vidivox.guicomponents;

import java.awt.Color;
import java.awt.Desktop;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import mp3.MP3;
import speech.*;
import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer;
import video.*;
import vidivox.workspace.WorkspaceLoader;
import vidivox.workspace.WorkspaceSaver;

public class MenuPanel extends JPanel {

	private static final long serialVersionUID = 1L;

	public MenuPanel(final VideoLabel vLabel, final JFrame frame, 
			final EmbeddedMediaPlayer mediaPlayer,
			final SpeechTab sTab, final VideoTab vTab,
			final SpeechTools sTools, final MP3Tools mTools,
			final File directory) {

		setBackground(Color.GRAY);
		setLayout(null);

		// SELECT VIDEO BUTTON
		final JButton selectVideo = new JButton("Select Video");
		selectVideo.setBackground(Color.WHITE);
		selectVideo.addActionListener(new ActionListener() {
			@SuppressWarnings("static-access")
			public void actionPerformed(ActionEvent arg0) {
				JFileChooser fChooser = new JFileChooser();
				fChooser.setCurrentDirectory(directory);
				fChooser.setAcceptAllFileFilterUsed(false);
				// JFileChooser allows user to find and select files with .avi
				// and .mp4 extension
				FileFilter filter = new FileNameExtensionFilter("avi, mp4",
						"avi", "mp4");
				fChooser.addChoosableFileFilter(filter);
				int number = fChooser.showDialog(null, "Choose Video");

				if (number == fChooser.APPROVE_OPTION) {
					String video = fChooser.getSelectedFile().getAbsolutePath();
					Video.setVideoName(video);
					mediaPlayer.playMedia(video);
					vLabel.setCurrentVideo();
					
					if (sTab.isTextEnabled()) {
						sTools.setComboEnabled(true);
					}
					if (MP3.getMP3Name() != null) {
						mTools.setChooseMP3Enabled(true);
					}
				}
			}
		});
		selectVideo.setFont(new Font("Tahoma", Font.PLAIN, 12));
		selectVideo.setBounds(0, 0, 150, 20);
		add(selectVideo);

		// LOAD WORKSPACE BUTTON
		final JButton load = new JButton("Load...");
		load.setBackground(Color.WHITE);
		load.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				File dir = new File(".PetulantWaffle/Workspace");

				if (dir.exists()) {
					String ObjButtons[] = { "Yes", "No", "Cancel" };
					int PromptResult = JOptionPane.showOptionDialog(null,
							"Do you want to load previously saved workspace?",
							"Confirm Load", JOptionPane.DEFAULT_OPTION,
							JOptionPane.QUESTION_MESSAGE, null, ObjButtons,
							ObjButtons[1]);
					if (PromptResult == JOptionPane.YES_OPTION) {
						WorkspaceLoader loader = new WorkspaceLoader(vLabel, sTab,
								sTools, mTools, mediaPlayer);
						try {
							loader.load();
						} catch (Exception e) {
							System.out.println("Couldn't load workspace");
						}
					}
				} else {
					JOptionPane.showMessageDialog(frame, "There are no saved workspaces", 
							"Workspace error", JOptionPane.ERROR_MESSAGE);
				}

			}
		});
		load.setFont(new Font("Tahoma", Font.PLAIN, 12));
		load.setBounds(150, 0, 150, 20);
		add(load);

		// SAVE BUTTON
		final JButton save = new JButton("Save...");
		save.setBackground(Color.WHITE);
		save.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				WorkspaceSaver saver = new WorkspaceSaver(sTab,
						sTools, mTools, false);
				saver.promptSave();
			}
		});
		save.setFont(new Font("Tahoma", Font.PLAIN, 12));
		save.setBounds(300, 0, 150, 20);
		add(save);

		// HELP BUTTON
		final JButton help = new JButton("Help");
		help.setBackground(Color.WHITE);
		help.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String currentDirectory = System.getProperty("user.dir");
				//Opens User Manual for Help
// Code from: http://stackoverflow.com/questions/2546968/open-pdf-file-on-fly-from-java-application
				if (Desktop.isDesktopSupported()) {
				    try {
				        File myFile = new File(currentDirectory+"/UserManual.pdf");
				        Desktop.getDesktop().open(myFile);
				    } catch (IOException | IllegalArgumentException ex) {
				    	JOptionPane.showMessageDialog(frame, "User Manual not found", 
								"User Manual error", JOptionPane.ERROR_MESSAGE);
				    }
				}
			}
		});
		help.setFont(new Font("Tahoma", Font.PLAIN, 12));
		help.setBounds(450, 0, 150, 20);
		add(help);
	}
}

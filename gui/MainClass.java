package gui;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JFrame;
import javax.swing.JTabbedPane;

import speech.SpeechTab;
import uk.co.caprica.vlcj.discovery.NativeDiscovery;
import uk.co.caprica.vlcj.player.MediaPlayerFactory;
import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer;
import video.VideoTab;

import java.io.File;
import java.io.IOException;

public class MainClass {

	private final JFrame frame;
	private Container contentPane;
	private EmbeddedMediaPlayer mediaPlayer;
	private File dir;

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					new MainClass();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});
	}
	// This method creates all necessary files before executing the gui
	private void createFiles() throws IOException {
		// create hidden directory to store intermediate files
		File hDir = new File(".PetulantWaffle");
		if (!hDir.exists()) {
			hDir.mkdir();
		}
		// create directory for files to be saved to that user creates
		File dir = new File("PWNewFiles");
		if (!dir.exists()) {
			dir.mkdir();
		}
		this.dir = dir;
		// create file for saving text to turn into speech if it doesn't already
		// exist
		File speechFile = new File(".PetulantWaffle/Speech.txt");
		speechFile.createNewFile();
	}

	public MainClass() throws IOException {

		createFiles(); // Create files
		new NativeDiscovery().discover();

		frame = new JFrame("Media Player");
		frame.setMinimumSize(new Dimension(700, 600));

		final Canvas canvas = new Canvas();// Creates a canvas to display the
											// video
		canvas.setBackground(Color.black);

		MediaPlayerFactory mediaPlayerFactory = new MediaPlayerFactory();// Creates the media player
																			// to be placed on the canvas
		mediaPlayer = mediaPlayerFactory.newEmbeddedMediaPlayer();// The video runs on
																	// the media player
		mediaPlayer.setVideoSurface(mediaPlayerFactory.newVideoSurface(canvas));

		/*-------------------------------MP3 Tools----------------------------*/
		MP3Tools mp3Tools = new MP3Tools(frame, dir); // The panel on the left

		/*--------------------------------Speech Tools-----------------------*/
		SpeechTools speechTools = new SpeechTools(frame, dir);// The panel on the right

		/*-------------------------This is the Tabbed pane---------------------*/
		JTabbedPane tabPane = new JTabbedPane();// The tabs in the center

		SpeechTab sTab = new SpeechTab();
		sTab.setSpeechTools(speechTools);
		speechTools.setSpeechTab(sTab);
		VideoTab vTab = new VideoTab(mp3Tools, sTab, speechTools, mediaPlayer,
				canvas);

		tabPane.addTab("Video", vTab);
		tabPane.addTab("Speech", sTab);

		/*-------------------------This is the Menu---------------------------*/
		final MenuPanel menu = new MenuPanel(mediaPlayer, sTab,// The menu ribbon
				speechTools, mp3Tools, dir);
		menu.setBackground(Color.GRAY);

		/*---------------This is the overall layout-----------------------*/
		// The content pane uses GridBagLayout
		contentPane = frame.getContentPane();
		contentPane.setBackground(Color.GRAY);
		contentPane.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.BOTH;

		// MP3 Tools
		c.gridy = 1;c.gridx = 0;c.gridwidth = 1;c.weighty = 1;c.weightx = 0;c.ipadx = 90;
		contentPane.add(mp3Tools, c);

		// Speech Tools
		c.gridy = 1;c.gridx = 2;
		contentPane.add(speechTools, c);

		// Tabs
		c.gridy = 1;c.weighty = 1.0;c.gridx = 1;c.weightx = 0.33;c.gridheight = 3;
		contentPane.add(tabPane, c);

		// Menu
		c.ipady = 18;c.weightx = 0.0;c.gridx = 0;c.gridy = 0;
		c.gridheight = 1;c.gridwidth = 3;c.weighty = 0;
		contentPane.add(menu, c);

		// Launch the application
		frame.pack();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}
}

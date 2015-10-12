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
	
	public static void main(String[] args) throws IOException {
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
		// create file for saving text to turn into speech if it doesn't already exist
		File speechFile = new File(".PetulantWaffle/Speech.txt");
		speechFile.createNewFile();
			EventQueue.invokeLater(new Runnable() {
			public void run() {
				new MainClass();
			}
		});
	}

	public MainClass() {
		
		new NativeDiscovery().discover();

		frame = new JFrame("Media Player");
		frame.setMinimumSize(new Dimension(700, 600));
		
		final Canvas canvas = new Canvas();// Creates a canvas to display the video
		canvas.setBackground(Color.black);
		
		MediaPlayerFactory mediaPlayerFactory = new MediaPlayerFactory();// Creates the media player to
																		// be placed on the canvas
		mediaPlayer = mediaPlayerFactory.newEmbeddedMediaPlayer();// The video runs on the media player
		mediaPlayer.setVideoSurface(mediaPlayerFactory.newVideoSurface(canvas));
		
		/*-------------------------------MP3 Tools----------------------------*/
		MP3Tools mp3Tools = new MP3Tools(frame, mediaPlayer);
		
		/*--------------------------------Speech Tools-----------------------*/
		SpeechTools speechTools = new SpeechTools(frame, mediaPlayer);
		
		/*-------------------------This is the Tabbed pane---------------------*/
		JTabbedPane tabPane = new JTabbedPane();
		
		SpeechTab sTab = new SpeechTab();
		sTab.setSpeechTools(speechTools);
		speechTools.setSpeechTab(sTab);
		VideoTab vTab = new VideoTab(mp3Tools, sTab, speechTools, mediaPlayer, canvas);
		
		tabPane.addTab("Video", vTab);
		tabPane.addTab("Speech", sTab);
		
		/*-------------------------This is the Menu---------------------------*/

		final MenuPanel menu = new MenuPanel(frame, mediaPlayer, sTab, speechTools, mp3Tools);
		menu.setBackground(Color.GRAY);

		/*---------------This is the overall layout-----------------------*/
		//The content pane uses GridBagLayout
		contentPane = frame.getContentPane();
		contentPane.setBackground(Color.GRAY);
		contentPane.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.BOTH;
		
		//MP3 Tools
		c.gridy = 1; c.gridx = 0;c.gridwidth = 1;c.weighty = 1;c.weightx = 0;c.ipadx = 90;
		contentPane.add(mp3Tools, c);
				
		//Speech Tools
		c.gridy = 1;c.gridx = 2;
		contentPane.add(speechTools, c);
		
		//Tabs
		c.gridy = 1;c.weighty = 1.0;c.gridx = 1;c.weightx = 0.33;
		c.gridheight = 3;
		contentPane.add(tabPane, c);	
		
		// Menu
		c.ipady = 18;c.weightx = 0.0;c.gridx = 0;c.gridy = 0;c.gridheight = 1;c.gridwidth = 3;c.weighty = 0;
		contentPane.add(menu, c);
		
		// Launch the application
		frame.pack();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}
}

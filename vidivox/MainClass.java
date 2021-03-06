package vidivox;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JFrame;
import javax.swing.JTabbedPane;
import speech.SpeechTab;
import uk.co.caprica.vlcj.discovery.NativeDiscovery;
import uk.co.caprica.vlcj.player.MediaPlayerFactory;
import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer;
import video.VideoTab;
import vidivox.guicomponents.*;
import vidivox.keyshortcuts.*;
import vidivox.workspace.WorkspaceSaver;

import java.io.File;
import java.io.IOException;

/**This is the main VIDIVOX class that contains the overall JFrame. It uses GridBagLayout to 
 * handle re-sizing and contains all the other panels and tabs.
 * @author Sabrina Zafarullah
 */
public class MainClass {

	private final JFrame frame;
	private Container contentPane;
	private EmbeddedMediaPlayer mediaPlayer;
	private File dir;
	private Canvas canvas;

	// Main class to set up VIDIVOX
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					new MainClass();
				} catch (IOException e) {
					System.out.println("Couldn't run VIDIVOX");
				}
			}
		});
	}

	/**This method creates all necessary files before executing the GUI
	 * @throws IOException	Attempts to create files
	 */
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
		// create file for saving text to turn into speech
		File speechFile = new File(".PetulantWaffle/Speech.txt");
		speechFile.createNewFile();
	}

	/**
	 * Creates the canvas and media player that goes into it
	 */
	private void setUpCanvas() {
		final Canvas canvas = new Canvas();// Creates a canvas to display the video
		canvas.setBackground(Color.black);
		this.canvas = canvas;
		// Create the media player to be placed on the canvas
		MediaPlayerFactory mediaPlayerFactory = new MediaPlayerFactory();
		mediaPlayer = mediaPlayerFactory.newEmbeddedMediaPlayer();
		// The video runs on the media player
		mediaPlayer.setVideoSurface(mediaPlayerFactory.newVideoSurface(canvas));
	}

	public MainClass() throws IOException {

		createFiles(); // Create files
		new NativeDiscovery().discover();

		frame = new JFrame("Media Player");
		frame.setMinimumSize(new Dimension(700, 600));
		setUpCanvas();
		
		/*-----------------------This is the Current Video Name-------------------*/
		VideoLabel videoName = new VideoLabel();
		videoName.setBackground(Color.GRAY);

		/*-------------------------------MP3 Tools----------------------------*/
		// The panel on the left
		final MP3Tools mp3Tools = new MP3Tools(frame, dir, mediaPlayer, videoName); 

		/*--------------------------------Speech Tools-----------------------*/
		// The panel on the right
		final SpeechTools speechTools = new SpeechTools(frame, dir, mediaPlayer, mp3Tools, videoName);

		/*-------------------------This is the Tabbed pane---------------------*/
		JTabbedPane tabPane = new JTabbedPane();// The tabs in the center

		final SpeechTab sTab = new SpeechTab();
		sTab.setSpeechTools(speechTools);
		speechTools.setSpeechTab(sTab);
		VideoTab vTab = new VideoTab(mp3Tools, sTab, speechTools, mediaPlayer,
				canvas);
		tabPane.addTab("Video", vTab);
		tabPane.addTab("Speech", sTab);
		
		/*-------------------------This is the Menu---------------------------*/
		final MenuPanel menu = new MenuPanel(videoName, frame, mediaPlayer, sTab, vTab,
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
		c.gridy = 2;c.gridx = 0;c.gridwidth = 1;c.weighty = 1;c.weightx = 0;c.ipadx = 90;
		contentPane.add(mp3Tools, c);

		// Speech Tools
		c.gridy = 2;c.gridx = 2;
		contentPane.add(speechTools, c);

		// Tabs
		c.gridy = 2;c.weighty = 1.0;c.gridx = 1;c.weightx = 0.33;c.gridheight = 3;
		contentPane.add(tabPane, c);

		// Menu
		c.ipady = 18;c.weightx = 0.0;c.gridx = 0;c.gridy = 0;
		c.gridheight = 1;c.gridwidth = 3;c.weighty = 0;
		contentPane.add(menu, c);
		
		// CURRENT VIDEO CHOSEN
		c.gridy = 1;c.ipady = 2;c.insets = new Insets(0,20,0,0);
		contentPane.add(videoName, c);

		// Launch the application
		frame.pack();
		// Prompts to save before closing. 
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent we) {
				WorkspaceSaver saver = new WorkspaceSaver(sTab, speechTools,
						mp3Tools, true);
				saver.promptSave();
			}
		});
		// Sets up the shortcuts
		Shortcut msc = new Shortcut(vTab, mp3Tools, speechTools);
		msc.addShortcut(frame);
		
		frame.setVisible(true);
	}
}

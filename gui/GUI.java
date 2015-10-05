package gui;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.ComponentOrientation;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.MouseInfo;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JProgressBar;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;

import uk.co.caprica.vlcj.discovery.NativeDiscovery;
import uk.co.caprica.vlcj.player.MediaPlayerFactory;
import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer;

import java.awt.Insets;
import java.io.File;
import java.io.IOException;

public class GUI {

	private final JFrame frame;
	private Container contentPane;
	final JProgressBar progressBar;
	private Video video = null;
	private EmbeddedMediaPlayer mediaPlayer;
	private float time;

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
				new GUI();
			}
		});
	}

	public GUI() {

		new NativeDiscovery().discover();

		frame = new JFrame("Media Player");
		frame.setMinimumSize(new Dimension(650, 500));

		/*-------------------------This is the Screen---------------------------*/

		final Canvas canvas = new Canvas();// Creates a canvas to display the video
		canvas.setBackground(Color.black);

		MediaPlayerFactory mediaPlayerFactory = new MediaPlayerFactory();// Creates the media player to
																			// be placed on the canvas
		mediaPlayer = mediaPlayerFactory.newEmbeddedMediaPlayer();// The video runs on the media player
		mediaPlayer.setVideoSurface(mediaPlayerFactory.newVideoSurface(canvas));
		mediaPlayer.setAspectRatio("16:9");// Fixes aspect ratio
		Video.getInstance();
		MP3.getInstance();

		/*-------------------------This is the Menu---------------------------*/

		final MenuPanel menu = new MenuPanel(frame, mediaPlayer);

		/*-------------------------This is the Panel with all the buttons------------------------*/

		final ButtonPanel pane = new ButtonPanel(mediaPlayer, menu);
//		pane.setVisible(false);

		final MouseMotionListener listener = new MouseMotionListener() {
			@Override
			public void mouseDragged(MouseEvent arg0) {
			}

			@Override
			public void mouseMoved(MouseEvent arg0) {
//				pane.setVisible(true);

				Timer timer = new Timer(5000, new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent e) {
//						pane.setVisible(false);
					}
				});
				timer.setRepeats(false);
				timer.start();
			}
		};

		canvas.addMouseMotionListener(listener);

		/*----------------------This is the Panel with the specialized buttons------------------*/

		TextPanel extension = new TextPanel(mediaPlayer, frame, video, menu);

		/*-------------------------This is the Overall layout---------------------------*/
		contentPane = frame.getContentPane();
		contentPane.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
		contentPane.setBackground(Color.GRAY);

		contentPane.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;

		// Menu
		c.ipady = 18;
		c.weightx = 0.0;
		c.gridwidth = 3;
		c.gridx = 0;
		c.gridy = 0;
		contentPane.add(menu, c);

		// Pane
		c.gridx = 0;
		c.gridwidth = 2;
		c.gridy = 4;
		contentPane.add(pane, c);

		// Time Label
		c.gridy = 3;
		JLabel timeStamp = new JLabel("00:00");
		contentPane.add(timeStamp, c);

		// Popup
		final JPopupMenu popup = new JPopupMenu();
		JMenuItem addSpeech = new JMenuItem("Add speech here");
		popup.add(addSpeech);
		JMenuItem addMP3 = new JMenuItem("Add mp3 here");
		addMP3.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				menu.timing(time);
			}
		});
		popup.add(addMP3);

		// Progress Bar
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.gridy = 2;
		gbc.gridx = 0;
		gbc.gridwidth = 18;
		gbc.ipady = 0;
		gbc.insets = new Insets(0, 0, 0, 0);
		progressBar = new JProgressBar(0, 100);
		progressBar.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {

				int pos = (e.getX() * 100) / frame.getWidth();
				float position = (float) pos / 100;

				if (Video.getVideoName() != null && (SwingUtilities.isLeftMouseButton(e))) {
					mediaPlayer.setPosition(position);
				} else if (Video.getVideoName() != null	&& (SwingUtilities.isRightMouseButton(e))) {
					time = position * mediaPlayer.getLength();
					popup.show(frame, e.getX(), progressBar.getY());
				}
			}
		});
		progressBar.setForeground(Color.orange);
		progressBar.setValue(0);
		ProgressBar pbHelper = new ProgressBar(progressBar, timeStamp, mediaPlayer);
		pbHelper.execute();
		contentPane.add(progressBar, gbc);

		// Text Panel
		c.gridy = 5;
		c.ipady = 50;
		contentPane.add(extension, c);

		// Screen
		c.weightx = 0.5;
		c.fill = GridBagConstraints.BOTH;
		c.gridx = 0;
		c.gridy = 1;
		c.weighty = 1.0;
		contentPane.add(canvas, c);

		// Launch the application
		frame.pack();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);

		// video = "sample_video.avi";
		// mediaPlayer.playMedia(video);
		// pause_btn.setIcon(getResizedImage("pause.png"));
	}
}

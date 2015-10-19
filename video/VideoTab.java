package video;


import gui.MP3Tools;
import gui.SpeechTools;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.ComponentOrientation;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JProgressBar;
import javax.swing.SwingUtilities;
import mp3.MP3;
import speech.SpeechTab;
import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer;

public class VideoTab extends JPanel {

	private static final long serialVersionUID = 1L;
	private EmbeddedMediaPlayer mediaPlayer;
	private final JProgressBar progressBar;
	private float time;
	private VideoTab tab;
	
	public VideoTab(final MP3Tools mTools, final SpeechTab sTab, final SpeechTools sTools, final EmbeddedMediaPlayer mediaPlayer, final Canvas canvas) {
		tab = this;
		
		/*-------------------------This is the Screen---------------------------*/

		mediaPlayer.setAspectRatio("16:9");// Fixes aspect ratio
		Video.getInstance();
		MP3.getInstance();
		
		/*-------------------------This is the Panel with all the buttons------------------------*/

		final ButtonPanel pane = new ButtonPanel(mediaPlayer);
//		pane.setVisible(false);

		//This is meant to allow button hiding
//		final MouseMotionListener listener = new MouseMotionListener() {
//			@Override
//			public void mouseDragged(MouseEvent arg0) {
//			}
//
//			@Override
//			public void mouseMoved(MouseEvent arg0) {
////				pane.setVisible(true);
//
//				Timer timer = new Timer(5000, new ActionListener() {
//
//					@Override
//					public void actionPerformed(ActionEvent e) {
////						pane.setVisible(false);
//					}
//				});
//				timer.setRepeats(false);
//				timer.start();
//			}
//		};
//
//		canvas.addMouseMotionListener(listener);
		
		/*------------------This is the overall layout---------------*/
		setBackground(Color.black);
		setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
		setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		
		// Time Label
		c.gridy = 3;
		JLabel timeStamp = new JLabel("00:00");
		timeStamp.setForeground(Color.orange);
		add(timeStamp, c);

		// Popup
		final JPopupMenu popup = new JPopupMenu();
		JMenuItem addSpeech = new JMenuItem("Add speech here");
		addSpeech.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				sTools.speechTiming(time);
			}
		});
		popup.add(addSpeech);
		JMenuItem addMP3 = new JMenuItem("Add mp3 here");
		addMP3.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				mTools.mp3Timing(time);
			}
		});
		popup.add(addMP3);
			
		// Progress Bar
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.gridy = 2;gbc.ipady = 0;gbc.insets = new Insets(0, 0, 0, 0);
		progressBar = new JProgressBar(0, 100);
		progressBar.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
					int pos = (e.getX() * 100) / tab.getWidth();
					float position = (float) pos / 100;
					if (Video.getVideoName() != null && (SwingUtilities.isLeftMouseButton(e))) {
					mediaPlayer.setPosition(position);
				} else if (Video.getVideoName() != null	&& (SwingUtilities.isRightMouseButton(e))) {
					time = position * mediaPlayer.getLength();
					popup.show(tab, e.getX(), progressBar.getY());
				}
			}
		});
		progressBar.setForeground(Color.orange);
		progressBar.setValue(0);
		ProgressBarWorker pbHelper = new ProgressBarWorker(progressBar, timeStamp, mediaPlayer);
		pbHelper.execute();
		add(progressBar, gbc);		
		
		// Pane
		c.gridy = 4;
		add(pane, c);
		
		// Screen
		c.weightx = 0.5;c.fill = GridBagConstraints.BOTH;c.gridx = 0;c.gridy = 1;c.weighty = 1.0;
		add(canvas, c);
	}

	public EmbeddedMediaPlayer getMediaPlayer() {
		return mediaPlayer;
	}
}

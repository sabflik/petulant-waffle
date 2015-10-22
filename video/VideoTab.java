package video;

import java.awt.Canvas;
import java.awt.Color;
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
import video.swingworkers.ProgressBarWorker;
import vidivox.guicomponents.MP3Tools;
import vidivox.guicomponents.SpeechTools;

public class VideoTab extends JPanel {

	private static final long serialVersionUID = 1L;
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
		
		/*------------------This is the overall layout---------------*/
		setBackground(Color.black);
		
		setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		
		// TIME LABEL
		c.gridy = 3;c.gridx = 0;
		JLabel timeStamp = new JLabel("00:00");
		timeStamp.setForeground(Color.orange);
		add(timeStamp, c);
		
		//ANOTHER LABEL
		c.gridx = 1;c.anchor = GridBagConstraints.EAST;
		JLabel lengthStamp = new JLabel("00:00");
		lengthStamp.setForeground(Color.orange);
		add(lengthStamp, c);

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
			
		// PROGRESS BAR
		c.fill = GridBagConstraints.HORIZONTAL;c.gridwidth = 2;c.gridx = 0;
		c.gridy = 2;c.ipady = 0;c.insets = new Insets(0, 0, 0, 0);
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
		ProgressBarWorker pbHelper = new ProgressBarWorker(progressBar, timeStamp, lengthStamp, mediaPlayer);
		pbHelper.execute();
		add(progressBar, c);		
		
		// Pane
		c.gridy = 4;
		add(pane, c);
		
		// Screen
		c.weightx = 0.5;c.fill = GridBagConstraints.BOTH;c.gridx = 0;c.gridy = 1;c.weighty = 1.0;
		add(canvas, c);
	}
}

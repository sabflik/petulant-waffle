package gui;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.ComponentOrientation;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.UIManager;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import uk.co.caprica.vlcj.discovery.NativeDiscovery;
import uk.co.caprica.vlcj.player.MediaPlayerFactory;
import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer;

import java.awt.Font;
import java.awt.Image;
import java.awt.Insets;
import java.io.File;
import java.io.IOException;
import java.net.URL;

import javax.swing.JSlider;


public class GUI {
	
	final static boolean shouldWeightX = true;
	private final JFrame frame;
	private Container contentPane;
	final JProgressBar progressBar;
	private VideoManipulation vFF;
	private VideoManipulation vRewind;
	private boolean hasRewinded = false;
	private boolean hasFFed = false;
	private String video = null;
	private EmbeddedMediaPlayer mediaPlayer;

	public static void main(String[] args) throws IOException {

		//create hidden directory to store intermediate files
		File hDir = new File(".ZealousQuack");
		if (!hDir.exists()) {
			hDir.mkdir();
		}
		//create directory for files to be saved to that user creates
		File dir = new File("ZQNewFiles");
		if (!dir.exists()) {
			dir.mkdir();
		}
		//create file for saving text to turn into speech if it doesn't already exist
		File speechFile = new File(".ZealousQuack/Speech.txt");
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

		MediaPlayerFactory mediaPlayerFactory = new MediaPlayerFactory();// Creates the media player to be placed on the canvas
		mediaPlayer = mediaPlayerFactory.newEmbeddedMediaPlayer();// The video runs on the media player
		mediaPlayer.setVideoSurface(mediaPlayerFactory.newVideoSurface(canvas));
		mediaPlayer.setAspectRatio("16:9");// Fixes aspect ratio	
		
		/*-------------------------This is the Menu---------------------------*/

		JPanel menu = new JPanel();
		menu.setBackground(Color.GRAY);
		menu.setLayout(null);

		//This button imports the video file to be played
		final JButton btnNewButton = new JButton("Import File");
		btnNewButton.setBackground(Color.WHITE);
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				OpenFile f;
				try {
					f = new OpenFile(frame, "Please select a video to import", mediaPlayer);
					f.setVisible(true);
					if (f.getVideo() != null) {
						video = f.getVideo();
					}
				} catch (IOException e) {
					e.printStackTrace();
				}		
			}
		});
		btnNewButton.setFont(new Font("Tahoma", Font.PLAIN, 12));
		btnNewButton.setBounds(0, 0, 140, 20);
		menu.add(btnNewButton);

		/*-------------------------This is the Panel with all the buttons---------------------------*/

		JPanel pane = new JPanel();
		pane.setBackground(Color.GRAY);
		pane.setLayout(new GridBagLayout());
		
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.HORIZONTAL;
	
				
		//REWIND BUTTON
		gbc.gridx = 0;gbc.gridy = 1;gbc.gridwidth = 3;
		gbc.insets = new Insets(5,0,5,0);  //vertical padding
		final JButton rewind_btn = new JButton();
		rewind_btn.setBackground(Color.WHITE);
		rewind_btn.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent arg0) {
			if(hasRewinded) {
				vRewind.cancel(true);
			}
			if(hasFFed) {
				vFF.cancel(true);
			}
			hasRewinded = true;
			vRewind = new VideoManipulation(mediaPlayer, "rewind");
			vRewind.execute();
			}
		});
		rewind_btn.setIcon(getResizedImage("rewind.png"));
		pane.add(rewind_btn, gbc);

		//PAUSE AND PLAY BUTTON
		gbc.gridx = 3;
		final JButton pause_btn = new JButton();
		pause_btn.setBackground(Color.WHITE);
		pause_btn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(hasFFed) {
					vFF.cancel(true);
				}
				if(hasRewinded) {
					vRewind.cancel(true);
				}
				if(mediaPlayer.isPlaying()) { //toggle image of button depending on whether video is playing or not
					pause_btn.setIcon(getResizedImage("play.png"));
				} else {
					pause_btn.setIcon(getResizedImage("pause.png"));
				}
				mediaPlayer.pause();
			}
		});

		if(mediaPlayer.isPlaying()) { //toggle image of button depending on whether video is playing or not
			pause_btn.setIcon(getResizedImage("pause.png"));
		} else {
			pause_btn.setIcon(getResizedImage("play.png"));
		}
		pane.add(pause_btn, gbc);

		//FASTFORWARD BUTTON
		gbc.gridx = 6;
		final JButton fastforward_btn = new JButton();
		fastforward_btn.setBackground(Color.WHITE);
		fastforward_btn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(hasRewinded) {
					vRewind.cancel(true);
				}
				if(hasFFed) {
					vFF.cancel(true);
				}
				hasFFed = true;
				vFF = new VideoManipulation(mediaPlayer, "ff");
				vFF.execute();
			}
		});
		fastforward_btn.setIcon(getResizedImage("forward.png"));
		pane.add(fastforward_btn, gbc);

		//REPLAY BUTTON
		gbc.gridx = 9;
		final JButton replay_btn = new JButton();
		replay_btn.setBackground(Color.WHITE);
		replay_btn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				mediaPlayer.playMedia(video);
				pause_btn.setIcon(getResizedImage("pause.png"));
			}
		});
		replay_btn.setIcon(getResizedImage("replay.png"));
		pane.add(replay_btn, gbc);
		
		//MUTE BUTTON
		gbc.gridx = 12;
		final JButton mute_btn = new JButton();
		mute_btn.setBackground(Color.WHITE);
		mute_btn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {	
				if(mediaPlayer.isMute()) { //toggle image of button depending on whether video is playing or not
					mute_btn.setIcon(getResizedImage("mute.png"));
				} else {
					mute_btn.setIcon(getResizedImage("sound.png"));
				}
				mediaPlayer.mute();
			}
		});
		
		if(mediaPlayer.isMute()) { //toggle image of button depending on whether video is playing or not
			mute_btn.setIcon(getResizedImage("sound.png"));
		} else {
			mute_btn.setIcon(getResizedImage("mute.png"));
		}
		pane.add(mute_btn, gbc);

		//VOLUME SLIDER
		gbc.gridx = 15;gbc.gridwidth = 3;
		final JSlider slider = new JSlider();
		slider.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				int sliderPos = slider.getValue();
				mediaPlayer.setVolume(sliderPos*2);
			}	
		});
		slider.setBackground(Color.GRAY);
		pane.add(slider, gbc);

		//TEXTFIELD
		gbc.gridy = 3;gbc.gridx = 0;gbc.gridwidth = 18;
		final JTextArea textArea = new JTextArea();
		textArea.setColumns(10);
		textArea.setRows(3);
		textArea.setToolTipText("Enter up to 30 words for each screen"); //30 word maximum means that processes called later will not take so long
		JScrollPane textScroll = new JScrollPane(textArea);
		pane.add(textScroll, gbc);

		//SPEAK BUTTON
		gbc.gridx = 0;gbc.gridy = 4;gbc.gridwidth = 6;
		JButton btnNewButton_8 = new JButton("Speak");
		btnNewButton_8.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (textArea.getText().trim() != null && !textArea.getText().trim().equals("")) {
					String speech = textArea.getText();
					Helper helper = new Helper(speech);
					helper.execute();
				} else {
					JOptionPane.showMessageDialog(frame, "ERROR: Please enter text to be spoken");
				}
			}
		});
		btnNewButton_8.setBackground(new Color(255, 255, 255));
		btnNewButton_8.setFont(new Font("Tahoma", Font.PLAIN, 12));
		btnNewButton_8.setToolTipText("Press for Festival to speak the text entered");
		pane.add(btnNewButton_8, gbc);	

		//CREATE MP3 BUTTON
		gbc.gridx = 6;
		JButton btnNewButton_7 = new JButton("Create mp3");
		btnNewButton_7.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (textArea.getText().trim() != null && !textArea.getText().trim().equals("")) {
					try {
						CreateAudio f = new CreateAudio(frame, "Please enter a name for your mp3 file", true, textArea.getText());
						f.setVisible(true);
					} catch (IOException e) {
						e.printStackTrace();
					}
				} else {
					JOptionPane.showMessageDialog(frame, "ERROR: Please enter text to be converted");
				}
			}
		});
		btnNewButton_7.setBackground(Color.WHITE);
		btnNewButton_7.setFont(new Font("Tahoma", Font.PLAIN, 12));
		btnNewButton_7.setToolTipText("Press to convert entered text to mp3");
		pane.add(btnNewButton_7, gbc);	

		//COMBO BUTTON
		gbc.gridx = 12;
		JButton btnNewButton_6 = new JButton("Combine Speech with Video");
		btnNewButton_6.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (textArea.getText().trim() != null && !textArea.getText().trim().equals("")) {
					try {
						Combo f = new Combo(frame, "", video, textArea.getText());
						f.setVisible(true);
						video = f.getNewFile();
						if (video != null) { //play newly created video
							mediaPlayer.playMedia(video);
							pause_btn.setIcon(getResizedImage("pause.png"));
						}
					} catch (IOException e) {
						e.printStackTrace();
					}
				} else {
					JOptionPane.showMessageDialog(frame, "ERROR: Please enter text to be converted");
				}
			}
		});
		btnNewButton_6.setBackground(Color.WHITE);
		btnNewButton_6.setFont(new Font("Tahoma", Font.PLAIN, 12));
		btnNewButton_6.setToolTipText("Press to create a new video with text dubbed");
		pane.add(btnNewButton_6, gbc);

		/*-------------------------This is the Overall layout---------------------------*/
		contentPane = frame.getContentPane();
		contentPane.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
		contentPane.setBackground(Color.GRAY);

		contentPane.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;

		//Menu
		c.ipady = 18;c.weightx = 0.0;c.gridwidth = 3;c.gridx = 0;c.gridy = 0;
		contentPane.add(menu, c);

		//Pane
		c.ipady = 50;c.anchor = GridBagConstraints.PAGE_END;c.gridx = 0;c.gridwidth = 2;c.gridy = 3;       
		contentPane.add(pane, c);
		
		//Progress Bar
		gbc.gridy = 2;gbc.gridx = 0;gbc.gridwidth = 18;gbc.ipady = 0;gbc.insets = new Insets(0,0,0,0);
		progressBar = new JProgressBar(0,100);
		progressBar.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if(video != null) {
					int pos = (e.getX() * 100) / frame.getWidth();
					float position = (float)pos / 100;
//					System.out.println(mediaPlayer.getPosition());
//					if(mediaPlayer.getPosition() == 1.00) {
//						mediaPlayer.playMedia(video);
//					}
					mediaPlayer.setPosition(position);
				}
			}	
		});
		//progressBar.setBackground(Color.GRAY);
		progressBar.setForeground(Color.orange);
		progressBar.setValue(0);
		ProgressBar pbHelper = new ProgressBar(progressBar, mediaPlayer);
		pbHelper.execute();
		contentPane.add(progressBar, gbc);
		
		//Screen
		c.weightx = 0.5;c.fill = GridBagConstraints.BOTH;c.gridx = 0;c.gridy = 1;c.weighty = 1.0;
		contentPane.add(canvas, c);
		
		//Launch the application
		frame.pack();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		
//		video = "sample_video.avi";
//		mediaPlayer.playMedia(video);
		pause_btn.setIcon(getResizedImage("pause.png"));
	}	
	
	//gets image that fits the button
	private ImageIcon getResizedImage(String image) {
		URL url = GUI.class.getResource("/icons/"+image);
		ImageIcon icon = new ImageIcon(url);
		Image getImage = icon.getImage().getScaledInstance(29, 23, java.awt.Image.SCALE_SMOOTH);
		return new ImageIcon(getImage);
	}
}

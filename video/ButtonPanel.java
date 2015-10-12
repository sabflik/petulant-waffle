package video;

import gui.MainClass;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer;

public class ButtonPanel extends JPanel {
	
	private VideoManipulationWorker vFF;
	private VideoManipulationWorker vRewind;
	private boolean hasRewinded = false;
	private boolean hasFFed = false;
	private JButton pause_btn;

	public ButtonPanel(final EmbeddedMediaPlayer mediaPlayer) {
		
//		menu.attachButtonObserver(this);
		
		setBackground(Color.GRAY);
		setLayout(new GridBagLayout());
		
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
			vRewind = new VideoManipulationWorker(mediaPlayer, VMType.REWIND);
			vRewind.execute();
			}
		});
		rewind_btn.setIcon(getResizedImage("rewind.png"));
		add(rewind_btn, gbc);

		//PAUSE AND PLAY BUTTON
		gbc.gridx = 3;
		pause_btn = new JButton();
		pause_btn.setBackground(Color.WHITE);
		pause_btn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(hasFFed) {
					vFF.cancel(true);
				}
				if(hasRewinded) {
					vRewind.cancel(true);
				}
//				if(mediaPlayer.isPlaying()) { //toggle image of button depending on whether video is playing or not
//					pause_btn.setIcon(getResizedImage("play.png"));
//				} else {
//					pause_btn.setIcon(getResizedImage("pause.png"));
//				}
				mediaPlayer.pause();
			}
		});

		if(mediaPlayer.isPlaying()) { //toggle image of button depending on whether video is playing or not
			pause_btn.setIcon(getResizedImage("pause.png"));
		} else {
			pause_btn.setIcon(getResizedImage("play.png"));
		}
		pause_btn.setIcon(getResizedImage("pause.png"));
		add(pause_btn, gbc);

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
				vFF = new VideoManipulationWorker(mediaPlayer, VMType.FASTFORWARD);
				vFF.execute();
			}
		});
		fastforward_btn.setIcon(getResizedImage("forward.png"));
		add(fastforward_btn, gbc);

		//REPLAY BUTTON
		gbc.gridx = 9;
		final JButton replay_btn = new JButton();
		replay_btn.setBackground(Color.WHITE);
		replay_btn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				mediaPlayer.playMedia(Video.getVideoName());
				pause_btn.setIcon(getResizedImage("pause.png"));
			}
		});
		replay_btn.setIcon(getResizedImage("replay.png"));
		add(replay_btn, gbc);
		
		//MUTE BUTTON
		gbc.gridx = 12;
		final JButton mute_btn = new JButton();
		mute_btn.setBackground(Color.WHITE);
		mute_btn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {	
//				if(mediaPlayer.isMute()) { //toggle image of button depending on whether video is playing or not
//					mute_btn.setIcon(getResizedImage("mute.png"));
//				} else {
//					mute_btn.setIcon(getResizedImage("sound.png"));
//				}
				mediaPlayer.mute();
			}
		});
		
		if(mediaPlayer.isMute()) { //toggle image of button depending on whether video is playing or not
			mute_btn.setIcon(getResizedImage("sound.png"));
		} else {
			mute_btn.setIcon(getResizedImage("mute.png"));
		}
		mute_btn.setIcon(getResizedImage("mute.png"));
		add(mute_btn, gbc);

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
		add(slider, gbc);	
	}

	//gets image that fits the button
	private ImageIcon getResizedImage(String image) {
		URL url = MainClass.class.getResource("/icons/"+image);
		ImageIcon icon = new ImageIcon(url);
		Image getImage = icon.getImage().getScaledInstance(29, 23, java.awt.Image.SCALE_SMOOTH);
		return new ImageIcon(getImage);
	}
//	
//	public void setPauseImage() {
//		pause_btn.setIcon(getResizedImage("pause.png"));
//	}
	
	public void update() {
		
	}
}

package gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

import openfile.OpenFile;
import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer;

//Observer pattern from http://www.tutorialspoint.com/design_pattern/observer_pattern.htm
public class MenuPanel extends JPanel {
	
	private ButtonPanel button_observer;
	private TextPanel text_observer;
	private JButton mp3Button;

	public MenuPanel(final JFrame frame, final EmbeddedMediaPlayer mediaPlayer) {
		
		setBackground(Color.GRAY);
		setLayout(null);

		//This button imports the video file to be played
		final JButton btnNewButton = new JButton("Video");
		btnNewButton.setBackground(Color.WHITE);
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				JFileChooser fChooser = new JFileChooser();
				fChooser.setAcceptAllFileFilterUsed(false);
				FileFilter filter = new FileNameExtensionFilter("avi, mp4", "avi", "mp4"); //JFileChooser allows user to find and select any files with .avi and .mp4 extension
				fChooser.addChoosableFileFilter(filter);
				int number = fChooser.showDialog(null, "Choose Video");
				
				if(number == fChooser.APPROVE_OPTION) {
					String video = fChooser.getSelectedFile().getAbsolutePath();
					Video.setVideoName(video);
					mediaPlayer.playMedia(video);
					mp3Button.setEnabled(true);
				}
			}
		});
		btnNewButton.setFont(new Font("Tahoma", Font.PLAIN, 12));
		btnNewButton.setBounds(0, 0, 100, 20);
		add(btnNewButton);
		
		//This button imports the mp3 file to be played
		final JButton mp3Button = new JButton("MP3");
		mp3Button.setBackground(Color.WHITE);
		mp3Button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				JFileChooser fChooser = new JFileChooser();
				fChooser.setAcceptAllFileFilterUsed(false);
				FileFilter filter = new FileNameExtensionFilter("mp3 files", "mp3"); //JFileChooser allows user to find and select any .mp3 files
				fChooser.addChoosableFileFilter(filter);
				int number = fChooser.showDialog(null, "Choose mp3 File");
				
				if(number == fChooser.APPROVE_OPTION) {
					String audio = fChooser.getSelectedFile().getAbsolutePath();
					MP3.setMP3Name(audio);
				}	
			}
		});
		mp3Button.setFont(new Font("Tahoma", Font.PLAIN, 12));
		mp3Button.setBounds(100, 0, 100, 20);
		mp3Button.setEnabled(false);
		add(mp3Button);
		
		//This button imports the video file to be played
		final JButton tnNewButton = new JButton("File");
		tnNewButton.setBackground(Color.WHITE);
		tnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				OpenFile f;
				try {
					f = new OpenFile(frame, "Please select a video to import", mediaPlayer);
					f.setVisible(true);
					if (f.getVideo() != null) {
						Video.setVideoName(f.getVideo());
					}
				} catch (IOException e) {
					e.printStackTrace();
				}		
			}
		});
		tnNewButton.setFont(new Font("Tahoma", Font.PLAIN, 12));
		tnNewButton.setBounds(200, 0, 100, 20);
		add(tnNewButton);
	}
	
	public void attachButtonObserver(ButtonPanel observer) {
		button_observer = observer;
	}
	
	public void attachTextObserver(TextPanel observer) {
		text_observer = observer;
	}
	
	public void notifyAllObservers() {
		button_observer.update();
		text_observer.update();
	}
}

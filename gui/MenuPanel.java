package gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

import openfile.OpenFile;
import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer;

//Observer pattern from http://www.tutorialspoint.com/design_pattern/observer_pattern.htm
public class MenuPanel extends JPanel {
	
	private ButtonPanel button_observer;
	private TextPanel text_observer;
	final private JButton mp3Button;
	final private JButton speechButton;
	JMenuItem mp3Timing;
	JMenuItem speechTiming;

	public MenuPanel(final JFrame frame, final EmbeddedMediaPlayer mediaPlayer) {
		
		setBackground(Color.GRAY);
		setLayout(null);
		
		//This button imports the mp3 file to be played
		mp3Button = new JButton("MP3 Settings");
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
		mp3Button.setBounds(100, 0, 150, 20);
		mp3Button.setEnabled(false);
		add(mp3Button);
		
		JPopupMenu mp3Popup = new JPopupMenu();
		JCheckBoxMenuItem mp3Check = new JCheckBoxMenuItem("Keep original audio");
		mp3Popup.add(mp3Check);
		mp3Timing = new JMenuItem("00:00");
		mp3Popup.add(mp3Timing);
		mp3Button.setComponentPopupMenu(mp3Popup);
		
		//This button imports the mp3 file to be played
		speechButton = new JButton("Speech Settings");
		speechButton.setBackground(Color.WHITE);
		speechButton.setFont(new Font("Tahoma", Font.PLAIN, 12));
		speechButton.setBounds(250, 0, 150, 20);
		speechButton.setEnabled(false);
		add(speechButton);
		
		JPopupMenu speechPopup = new JPopupMenu();
		JCheckBoxMenuItem speechCheck = new JCheckBoxMenuItem("Keep original audio");
		speechPopup.add(speechCheck);
		speechTiming = new JMenuItem("00:00");
		speechPopup.add(speechTiming);
		speechButton.setComponentPopupMenu(speechPopup);		
		
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
					speechButton.setEnabled(true);
				}
			}
		});
		btnNewButton.setFont(new Font("Tahoma", Font.PLAIN, 12));
		btnNewButton.setBounds(0, 0, 100, 20);
		add(btnNewButton);
		
		//This button imports the video file to be played
		final JButton tnNewButton = new JButton("File");
		tnNewButton.setBackground(Color.WHITE);
		tnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				OpenFile f;
				try {
					f = new OpenFile(frame, "Please select a video to import", mediaPlayer);
					f.setVisible(true);
				} catch (IOException e) {
					e.printStackTrace();
				}		
			}
		});
		tnNewButton.setFont(new Font("Tahoma", Font.PLAIN, 12));
		tnNewButton.setBounds(400, 0, 100, 20);
		add(tnNewButton);
	}
	
	//Sets the selected time for mp3 placement
	public void mp3Timing(float time) {
		if(time != -1) {
			float timeInSeconds = time / 1000;
			int sec = (int)timeInSeconds % 60;
			int min = (int)(timeInSeconds / 60) % 60;
			mp3Timing.setText(String.format("%02d:%02d", min, sec));
		}
	}
	
	//Sets the selected time for mp3 placement
	public void speechTiming(float time) {
		if(time != -1) {
			float timeInSeconds = time / 1000;
			int sec = (int)timeInSeconds % 60;
			int min = (int)(timeInSeconds / 60) % 60;
			speechTiming.setText(String.format("%02d:%02d", min, sec));
		}
	}
	
	//Gets the selected time for mp3 placement
	public String getMP3Timing() {
		return mp3Timing.getText();
	}
	
	public String getSpeechTiming() {
		return speechTiming.getText();
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

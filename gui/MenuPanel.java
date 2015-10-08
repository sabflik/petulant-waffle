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
import javax.swing.JTabbedPane;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

import mp3.*;
import speech.*;
import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer;
import video.*;

//Observer pattern from http://www.tutorialspoint.com/design_pattern/observer_pattern.htm
public class MenuPanel extends JPanel {
	
//	private ButtonPanel button_observer;
//	private TextPanel text_observer;

	public MenuPanel(final JFrame frame, final EmbeddedMediaPlayer mediaPlayer, final VideoTab vTab, final MP3Tab mTab, final SpeechTab sTab, final JTabbedPane tabPane) {
		
		setBackground(Color.GRAY);
		setLayout(null);
		
		//This button imports the video file to be played
		final JButton btnNewButton = new JButton("Select Video");
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
					tabPane.setEnabledAt(1, true);
					if(sTab.isTextEnabled()) {
						sTab.setComboEnabled(true);
					}
//					mp3Button.setEnabled(true);
//					speechButton.setEnabled(true);
				}
			}
		});
		btnNewButton.setFont(new Font("Tahoma", Font.PLAIN, 12));
		btnNewButton.setBounds(0, 0, 150, 20);
		add(btnNewButton);
		
//	public void attachButtonObserver(ButtonPanel observer) {
//		button_observer = observer;
//	}
//	
//	public void attachTextObserver(TextPanel observer) {
//		text_observer = observer;
//	}
//	
//	public void notifyAllObservers() {
//		button_observer.update();
//		text_observer.update();
	}
}

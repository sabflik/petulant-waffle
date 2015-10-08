package mp3;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer;
import video.Video;

public class MP3Tab extends JPanel {

	JButton mp3Button;
	final private JButton chooseMP3;
	JMenuItem mp3Timing;
	private MP3Tab tab;
	private float mp3TimeInMS;
	
	public MP3Tab(final EmbeddedMediaPlayer mediaPlayer) {
		tab = this;
		
		setBackground(Color.GRAY);
		setLayout(new GridBagLayout());
		GridBagConstraints gb = new GridBagConstraints();
		gb.fill = GridBagConstraints.HORIZONTAL;
		
		gb.gridx = 0;gb.gridy = 0;
		JLabel choose = new JLabel("Select an mp3 to overlay:");
		add(choose, gb);
		
		//This button imports the mp3 file to be played
		gb.gridx = 1;gb.gridy = 0;
		chooseMP3 = new JButton("Choose MP3");
		chooseMP3.setBackground(Color.WHITE);
		chooseMP3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				JFileChooser fChooser = new JFileChooser();
				fChooser.setAcceptAllFileFilterUsed(false);
				FileFilter filter = new FileNameExtensionFilter("mp3 files", "mp3"); //JFileChooser allows user to find and select any .mp3 files
				fChooser.addChoosableFileFilter(filter);
				int number = fChooser.showDialog(null, "Choose mp3 File");
				
				if(number == fChooser.APPROVE_OPTION) {
					String audio = fChooser.getSelectedFile().getAbsolutePath();
					MP3.setMP3Name(audio);
					mp3Button.setEnabled(true);
				}	
			}
		});
		chooseMP3.setFont(new Font("Tahoma", Font.PLAIN, 12));
		chooseMP3.setBounds(100, 0, 150, 20);
//		chooseMP3.setEnabled(false);
		add(chooseMP3, gb);
		
		//This button does settings stuff
		gb.gridx = 1;gb.gridy = 1;
		JButton settingB = new JButton("Settings");
		settingB.setBackground(Color.WHITE);
		JPopupMenu mp3Popup = new JPopupMenu();
		JCheckBoxMenuItem mp3Check = new JCheckBoxMenuItem("Keep original audio");
		mp3Popup.add(mp3Check);
		mp3Timing = new JMenuItem("00:00");
		mp3Popup.add(mp3Timing);
		settingB.setComponentPopupMenu(mp3Popup);
		add(settingB, gb);
		
		gb.gridx = 0;gb.gridy = 1;
		JLabel settings = new JLabel("MP3 Settings");
		settingB.setFont(new Font("Tahoma", Font.PLAIN, 12));
		add(settings, gb);

		//MP3 COMBO BUTTON
		gb.gridy = 3;
		mp3Button = new JButton("Combine MP3 with Video");
		mp3Button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if((MP3.getMP3Name() != null) && (Video.getVideoName() != null)) {
					MP3OverlayWorker overlay = new MP3OverlayWorker(Video.getVideoName(), MP3.getMP3Name(), mediaPlayer, mp3TimeInMS);
					overlay.execute();
				} else {
					JOptionPane.showMessageDialog(tab, "ERROR: Please select a file");
				}
			}
		});
		mp3Button.setBackground(Color.WHITE);
		mp3Button.setFont(new Font("Tahoma", Font.PLAIN, 12));
		mp3Button.setToolTipText("Press to create a new video with text dubbed");
		mp3Button.setEnabled(false);
		add(mp3Button, gb);	
		
	}
	
	//Sets the selected time for mp3 placement
	public void mp3Timing(float time) {
		if(time != -1) {
			mp3TimeInMS = time;
			float timeInSeconds = time / 1000;
			int sec = (int)timeInSeconds % 60;
			int min = (int)(timeInSeconds / 60) % 60;
			mp3Timing.setText(String.format("%02d:%02d", min, sec));
		}
	}
	
	//Gets the selected time for mp3 placement
	public String getMP3Timing() {
		return mp3Timing.getText();
	}
}

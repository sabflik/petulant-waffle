package gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer;
import video.Video;

import mp3.MP3;
import mp3.MP3Combo;

public class MP3Tools extends JPanel {

	private static final long serialVersionUID = 1L;
	private JButton mp3Button;
	private JButton chooseMP3;
	private JLabel mp3Timing;
	private MP3Tools tools;
	private float mp3TimeInMS;

	public MP3Tools(final JFrame frame, final EmbeddedMediaPlayer mediaPlayer) {
		tools = this;

		setBackground(Color.DARK_GRAY);
		setLayout(new GridBagLayout());

		GridBagConstraints gb = new GridBagConstraints();
		gb.fill = GridBagConstraints.HORIZONTAL;

		// This button imports the mp3 file to be played
		gb.gridx = 1;
		gb.gridy = 0;
		chooseMP3 = new JButton("Choose MP3");
		chooseMP3.setBackground(Color.WHITE);
		chooseMP3.addActionListener(new ActionListener() {
			@SuppressWarnings("static-access")
			public void actionPerformed(ActionEvent arg0) {
				JFileChooser fChooser = new JFileChooser();
				fChooser.setAcceptAllFileFilterUsed(false);
				FileFilter filter = new FileNameExtensionFilter("mp3 files",
						"mp3"); // JFileChooser allows user to find and select
								// any .mp3 files
				fChooser.addChoosableFileFilter(filter);
				int number = fChooser.showDialog(null, "Choose mp3 File");

				if (number == fChooser.APPROVE_OPTION) {
					String audio = fChooser.getSelectedFile().getAbsolutePath();
					MP3.setMP3Name(audio);
					mp3Button.setEnabled(true);
				}
			}
		});
		chooseMP3.setFont(new Font("Tahoma", Font.PLAIN, 12));
		chooseMP3.setBounds(100, 0, 150, 20);
		// chooseMP3.setEnabled(false);
		chooseMP3.setEnabled(false);
		add(chooseMP3, gb);

		// MP3 COMBO BUTTON
		gb.gridy = 1;
		mp3Button = new JButton("Combine MP3 with Video");
		mp3Button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if ((MP3.getMP3Name() != null)
						&& (Video.getVideoName() != null)) {
					MP3Combo f;
					try {
						f = new MP3Combo(frame, "", mediaPlayer, mp3TimeInMS);
						f.setVisible(true);
					} catch (IOException e) {
						e.printStackTrace();
					}
				} else {
					JOptionPane.showMessageDialog(tools,
							"ERROR: Please select a file");
				}
			}
		});
		mp3Button.setBackground(Color.WHITE);
		mp3Button.setFont(new Font("Tahoma", Font.PLAIN, 12));
		mp3Button
				.setToolTipText("Press to create a new video with text dubbed");
		mp3Button.setEnabled(false);
		add(mp3Button, gb);
		
		// Settings label
		gb.gridx = 1;
		gb.gridy = 2;
		JLabel settingB = new JLabel("MP3 Settings");
		settingB.setFont(new Font("Tahoma", Font.PLAIN, 12));
		settingB.setForeground(Color.WHITE);
//		settingB.setBackground(Color.WHITE);
//		JPopupMenu mp3Popup = new JPopupMenu();
//		JCheckBoxMenuItem mp3Check = new JCheckBoxMenuItem("Keep original audio");
//		mp3Popup.add(mp3Check);
//		mp3Timing = new JMenuItem("00:00");
//		mp3Popup.add(mp3Timing);
//		settingB.setComponentPopupMenu(mp3Popup);
		add(settingB, gb);
		
		//Settings options
		gb.gridy = 3;
		mp3Timing = new JLabel("Add mp3 at: 00:00");
		mp3Timing.setFont(new Font("Tahoma", Font.PLAIN, 12));
		mp3Timing.setForeground(Color.WHITE);
		mp3Timing.setToolTipText("Right click on progressbar and select 'Add mp3 here'");
		add(mp3Timing, gb);

	}

	// Sets the selected time for mp3 placement
	public void mp3Timing(float time) {
		if (time != -1) {
			mp3TimeInMS = time;
			float timeInSeconds = time / 1000;
			int sec = (int) timeInSeconds % 60;
			int min = (int) (timeInSeconds / 60) % 60;
			String text = "Add mp3 at: "+String.format("%02d:%02d", min, sec);
			mp3Timing.setText(text);
		}
	}

	// Gets the selected time for mp3 placement
	public String getMP3Timing() {
		return mp3Timing.getText();
	}
	
	public void setChooseMP3Enabled(boolean selection) {
		chooseMP3.setEnabled(selection);
	}
}

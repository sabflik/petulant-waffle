package gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import openfile.OpenFile;
import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer;

public class MenuPanel extends JPanel {

	public MenuPanel(final JFrame frame, final EmbeddedMediaPlayer mediaPlayer) {
		
		setBackground(Color.GRAY);
		setLayout(null);

		//This button imports the video file to be played
		final JButton btnNewButton = new JButton("File");
		btnNewButton.setBackground(Color.WHITE);
		btnNewButton.addActionListener(new ActionListener() {
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
		btnNewButton.setFont(new Font("Tahoma", Font.PLAIN, 12));
		btnNewButton.setBounds(0, 0, 100, 20);
		add(btnNewButton);
	}
}

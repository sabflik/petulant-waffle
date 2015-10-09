package mp3;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JTextField;


import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer;
import video.Video;

import java.awt.event.ActionListener;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.awt.event.ActionEvent;

public class MP3Combo extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JTextField textField;
	private String newFile;

	/**
	 * Create the dialog.
	 * @throws IOException 
	 */
	public MP3Combo(JFrame jframe, String title, final EmbeddedMediaPlayer mediaPlayer, final float mp3TimeInMS) throws IOException {
		super(jframe, title, true);
		setBounds(100, 100, 450, 150);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		contentPanel.setBackground(Color.GRAY);

		//create JLabel to instruct user on what to do
		JLabel lblName = new JLabel("Please enter a name for your new video file");
		lblName.setBounds(57, 10, 316, 15);
		contentPanel.add(lblName);

		//create JTextField for user to enter name of new video file
		textField = new JTextField();
		textField.setBounds(57, 50, 350, 19);
		contentPanel.add(textField);
		textField.setColumns(10);

		JPanel buttonPane = new JPanel();
		buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
		buttonPane.setBackground(Color.GRAY);
		getContentPane().add(buttonPane, BorderLayout.SOUTH);

		//ok button
		JButton okButton = new JButton("OK");
		okButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (textField.getText().trim() != null && !textField.getText().trim().equals("") && Video.getVideoName() != null) { //ensure file is selected and text field is not empty
					//overwrite the contents of the Speech.txt file
										
					if ((MP3.getMP3Name() != null) && (Video.getVideoName() != null)) {
						MP3OverlayWorker overlay = new MP3OverlayWorker(Video.getVideoName(), MP3.getMP3Name(), mediaPlayer, mp3TimeInMS, textField.getText());
						overlay.execute();
					} 
					
					newFile = "PWNewFiles/" + textField.getText() + ".avi";
					((JDialog)((java.awt.Component)arg0.getSource()).getParent().getParent().getParent().getParent().getParent()).dispose();
				} else {
					JOptionPane.showMessageDialog(contentPanel, "ERROR: Please enter the name you want to give the new file");
				}
			}
		});
		okButton.setActionCommand("OK");
		buttonPane.add(okButton);
		getRootPane().setDefaultButton(okButton);

		//cancel button
		JButton cancelButton = new JButton("Cancel");
		cancelButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				((JDialog)((java.awt.Component)e.getSource()).getParent().getParent().getParent().getParent().getParent()).dispose();
			}
		});
		cancelButton.setActionCommand("Cancel");
		buttonPane.add(cancelButton);
	}
	
	//getter - retrieves the file that the user created so it can be played
	public String getNewFile() {
		return newFile;
	}
}


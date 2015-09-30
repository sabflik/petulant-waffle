package combo;

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

import java.awt.event.ActionListener;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.awt.event.ActionEvent;

public class Combo extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JTextField textField;
	private String video = null;
	private String cmd;
	private String newFile;

	/**
	 * Create the dialog.
	 * @throws IOException 
	 */
	public Combo(JFrame jframe, String title, final String currentlyPlaying, final String text) throws IOException {
		super(jframe, title, true);
		setBounds(100, 100, 450, 220);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		contentPanel.setBackground(Color.GRAY);
				
		//JFileChooser allows user to find and select any files with .avi and .mp4 extension
		JLabel label = new JLabel("Choose video to play");
		label.setBounds(57, 20, 200, 20);
		contentPanel.add(label);
		
		final JButton chooseVideo = new JButton("Choose");
		chooseVideo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				JFileChooser fChooser = new JFileChooser();
				fChooser.setAcceptAllFileFilterUsed(false);
				FileFilter filter = new FileNameExtensionFilter("video files", "avi", "mp4");
				fChooser.addChoosableFileFilter(filter);
				int number = fChooser.showDialog(null, "Choose a Video File");
				
				if(number == fChooser.APPROVE_OPTION) {
					video = fChooser.getSelectedFile().getAbsolutePath();
				}
				
			}
		});
		chooseVideo.setBounds(250, 20, 100, 20);
		contentPanel.add(chooseVideo);
		
		

		//-------------------------------Check Box Current Video----------------------------
		//the user can choose whether or not they want to overlay the selected video with an existing audio file (this combination is NOT saved)
		final JLabel label1 = new JLabel("Select this video");
		label1.setBounds(80, 55, 200, 20);
		contentPanel.add(label1);
		
		final JCheckBox checkBox = new JCheckBox();
		checkBox.setBounds(57, 55, 20, 20);
		checkBox.setBackground(Color.GRAY);
		contentPanel.add(checkBox);
		checkBox.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(checkBox.isSelected()) {
					chooseVideo.setEnabled(false);
				} else {
					chooseVideo.setEnabled(true);
				}
			}
		});
		
		if(currentlyPlaying == null) {
			label1.setEnabled(false);
			checkBox.setEnabled(false);
		}
		
		//-------------------------------Check Box Keep audio----------------------------
		//the user can choose whether or not they want to keep the video's audio
		final JLabel label2 = new JLabel("Keep the video's audio");
		label2.setBounds(80, 75, 200, 20);
		contentPanel.add(label2);
				
		final JCheckBox checkBox1 = new JCheckBox();
		checkBox1.setBounds(57, 75, 20, 20);
		checkBox1.setBackground(Color.GRAY);
		contentPanel.add(checkBox1);

		//create JLabel to instruct user on what to do
		JLabel lblName = new JLabel("Please enter a name for your new video file");
		lblName.setBounds(57, 105, 316, 15);
		contentPanel.add(lblName);

		//create JTextField for user to enter name of new video file
		textField = new JTextField();
		textField.setBounds(57, 130, 366, 19);
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
				if(checkBox.isSelected()) {
					video = currentlyPlaying;
				}
				if (textField.getText().trim() != null && !textField.getText().trim().equals("") && video != null) { //ensure file is selected and text field is not empty
					//overwrite the contents of the Speech.txt file
					try {
						PrintWriter out;
						out = new PrintWriter(new FileWriter(".PetulantWaffle/Speech.txt"));
						out.println(text);
						out.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
					
					if (checkBox1.isSelected()) {
						try {
							mergeAudio(video, textField.getText());
						} catch (IOException | InterruptedException e) {
							e.printStackTrace();
						}
					} else {
						try {
							replaceAudio(video, textField.getText());
						} catch (IOException | InterruptedException e) {
							e.printStackTrace();
						}
					}
					
					
					newFile = "PWNewFiles/" + textField.getText() + ".avi";
					((JDialog)((java.awt.Component)arg0.getSource()).getParent().getParent().getParent().getParent().getParent()).dispose();
				} else {
					JOptionPane.showMessageDialog(contentPanel, "ERROR: Please select a video and enter the name you want to give the new file");
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
		
	//method called that converts the text to an mp3 file and combines it with the selected video
	private void replaceAudio(String video, String name) throws IOException, InterruptedException {
		//create the wav file from the text in Speech.txt, convert the wav file to mp3 and finally combine the new audio with the video file selected and name the created file according to the user's input
		cmd = "text2wave .PetulantWaffle/Speech.txt -o .PetulantWaffle/speech.wav;"
				+ "ffmpeg -i .PetulantWaffle/speech.wav -y .PetulantWaffle/audio.mp3;"
				+ "ffmpeg -i " + video + " -i .PetulantWaffle/audio.mp3 -map 0:v -map 1:a PWNewFiles/" + name + ".avi";
 		ProcessBuilder makeWav = new ProcessBuilder("/bin/bash", "-c", cmd);
 		Process processMW;
 		processMW = makeWav.start();
 		processMW.waitFor();
	}
	
	//Code from http://stackoverflow.com/questions/24804928/singler-line-ffmpeg-cmd-to-merge-video-audio-and-retain-both-audios
	private void mergeAudio(String video, String name) throws IOException, InterruptedException {
		cmd = "text2wave .PetulantWaffle/Speech.txt -o .PetulantWaffle/speech.wav;"
				+ "ffmpeg -i .PetulantWaffle/speech.wav -y .PetulantWaffle/audio.mp3;"
				+"ffmpeg -i " + video + " -y -vn -acodec copy .PetulantWaffle/video.mp3;"
				+"ffmpeg -i .PetulantWaffle/video.mp3 -i .PetulantWaffle/audio.mp3 -filter_complex amix=inputs=2:duration=first:dropout_transition=3 -y .PetulantWaffle/audiofinal.mp3;"
				+"ffmpeg -i "+ video + " -an -y .PetulantWaffle/tempVideo.avi;"
				+ "ffmpeg -i .PetulantWaffle/tempVideo.avi -i .PetulantWaffle/audiofinal.mp3 -map 0:v -map 1:a PWNewFiles/" + name + ".avi";
 		ProcessBuilder makeWav = new ProcessBuilder("/bin/bash", "-c", cmd);
 		Process processMW;
 		processMW = makeWav.start();
 		processMW.waitFor();
	}
}

package openfile;

import gui.Video;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.IOException;


public class OpenFile extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private MP3OverlayWorker overlay;
	private boolean withMP3 = false;
	private boolean canCancel = false;
	private String audio = null;

	/**
	 * Create the dialog. 
	 */
	public OpenFile(JFrame jframe, String title, final EmbeddedMediaPlayer mediaPlayer) throws IOException {
		super(jframe, title, true);
		setBounds(100, 100, 400, 200);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPanel.setBackground(Color.GRAY);
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		
		
		//-------------------------------Choose Audio----------------------------
		final JLabel aLabel = new JLabel("Choose audio to play");
		aLabel.setBounds(57, 100, 200, 20);
		aLabel.setEnabled(false);
		contentPanel.add(aLabel);
		
		final JButton chooseAudio = new JButton("Open");
		chooseAudio.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				JFileChooser fChooser = new JFileChooser();
				fChooser.setAcceptAllFileFilterUsed(false);
				FileFilter filter = new FileNameExtensionFilter("mp3 files", "mp3"); //JFileChooser allows user to find and select any .mp3 files
				fChooser.addChoosableFileFilter(filter);
				int number = fChooser.showDialog(null, "Choose mp3 File");
				
				if(number == fChooser.APPROVE_OPTION) {
					audio = fChooser.getSelectedFile().getAbsolutePath();
				}	
			}
		});
		chooseAudio.setEnabled(false); 
		chooseAudio.setBounds(250, 100, 100, 20);
		contentPanel.add(chooseAudio);
		
		//-------------------------------Check Box----------------------------
		//the user can choose whether or not they want to overlay the selected video with an existing audio file (this combination is NOT saved)
		JLabel label = new JLabel("Replace audio with mp3");
		label.setBounds(80, 70, 200, 20);
		contentPanel.add(label);
		
		final JCheckBox checkBox = new JCheckBox();
		checkBox.setBounds(57, 70, 20, 20);
		checkBox.setBackground(Color.GRAY);
		contentPanel.add(checkBox);
		checkBox.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(checkBox.isSelected()) {
					chooseAudio.setEnabled(true); 
					aLabel.setEnabled(true);
					withMP3 = true;
				} else {
					chooseAudio.setEnabled(false);
					aLabel.setEnabled(false);
					withMP3 = false;
				}
			}
		});
	
		//create JPanel
		JPanel buttonPane = new JPanel();
		buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
		buttonPane.setBackground(Color.GRAY);
		getContentPane().add(buttonPane, BorderLayout.SOUTH);

		//ok button - combines the audio and video files if necessary
		JButton okButton = new JButton("OK");
		okButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				if(!withMP3 && (Video.getVideoName() != null)) {
					mediaPlayer.playMedia(Video.getVideoName());
					((JDialog)((java.awt.Component)arg0.getSource()).getParent().getParent().getParent().getParent().getParent()).dispose();
				} else if(!withMP3 && (Video.getVideoName() == null)){
					JOptionPane.showMessageDialog(contentPanel, "ERROR: Please select a file");
				}
				
				if(withMP3 && (audio != null) && (Video.getVideoName() != null)) {
					overlay = new MP3OverlayWorker(Video.getVideoName(), audio, mediaPlayer);
					overlay.execute();
					canCancel = true;
					((JDialog)((java.awt.Component)arg0.getSource()).getParent().getParent().getParent().getParent().getParent()).dispose();
				} else if((withMP3 && (Video.getVideoName() == null)) || (withMP3 && (audio == null))) {
					JOptionPane.showMessageDialog(contentPanel, "ERROR: Please select a file");
				}				
			}
		});
		okButton.setActionCommand("OK");
		buttonPane.add(okButton);
		getRootPane().setDefaultButton(okButton);

		//cancel button - just closes the dialog
		JButton cancelButton = new JButton("Cancel");
		cancelButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(canCancel) {
					overlay.cancel(true);
				}
				((JDialog)((java.awt.Component)arg0.getSource()).getParent().getParent().getParent().getParent().getParent()).dispose();
			}
		});
		cancelButton.setActionCommand("Cancel");
		buttonPane.add(cancelButton);		
	}
}

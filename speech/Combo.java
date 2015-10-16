package speech;


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

public class Combo extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JTextField textField;
	private String newFile;

	/**
	 * Create the dialog.
	 * @throws IOException 
	 */
	public Combo(JFrame jframe, String title, final String text, final EmbeddedMediaPlayer mediaPlayer, final float speechTimeInMS) throws IOException {
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
				
			//overwrite the contents of the Speech.txt file
			try {
				PrintWriter out = new PrintWriter(new FileWriter(".PetulantWaffle/Speech.txt"));
				out.println(text);
				out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
					
//			if (checkBox1.isSelected()) {//Convert text to mp3 and merge it with video's audio
				ComboCreationWorker cc = new ComboCreationWorker(Video.getVideoName(), textField.getText(), AudioSetting.MERGE, mediaPlayer, speechTimeInMS);
				cc.execute();
//			} else { //Convert the text to an mp3 file and replace video's audio with it
//				ComboCreationWorker cc = new ComboCreationWorker(Video.getVideoName(), textField.getText(), AudioSetting.REPLACE, mediaPlayer, speechTimeInMS);
//				cc.execute();
//			}
				
			newFile = "PWNewFiles/" + textField.getText() + ".avi";
			((JDialog)((java.awt.Component)arg0.getSource()).getParent().getParent().getParent().getParent().getParent()).dispose();
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

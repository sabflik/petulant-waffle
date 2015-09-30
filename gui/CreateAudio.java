package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import javax.swing.JTextField;


public class CreateAudio extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JTextField textField;
	private String cmd;

	/**
	 * Create the dialog. 
	 */
	public CreateAudio(JFrame jframe, String title, boolean isModal, final String text) throws IOException {
		super(jframe, title, isModal);
		setBounds(100, 100, 450, 100);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		contentPanel.setBackground(Color.GRAY);

		//create text field for name of file
		textField = new JTextField();
		textField.setBounds(59, 25, 326, 19);
		contentPanel.add(textField);
		textField.setColumns(10);

		//create JPanel
		JPanel buttonPane = new JPanel();
		buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
		getContentPane().add(buttonPane, BorderLayout.SOUTH);

		//ok button - creates new audio files as per user input
		JButton okButton = new JButton("OK");
		okButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				//check whether or not a file with the inputted file name already exists
				File file = new File("ZQNewFiles/" + textField.getText());
				if (file.exists()) {
					JOptionPane.showMessageDialog(contentPanel, "ERROR: A file with this name already exists, please enter another name");
				} else {
					//overwrite the contents of the Speech.txt file
					PrintWriter out;
					try {
						out = new PrintWriter(new FileWriter(".ZealousQuack/Speech.txt"));
						out.println(text);
						out.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
					//create the wav file and convert that wav file to mp3
					cmd = "text2wave .ZealousQuack/Speech.txt -o .ZealousQuack/speech.wav;"
							+"ffmpeg -i .ZealousQuack/speech.wav ZQNewFiles/" + textField.getText() + ".mp3";
					ProcessBuilder makeWav = new ProcessBuilder("/bin/bash", "-c", cmd);
					Process processMW;
					try {
						processMW = makeWav.start();
						processMW.waitFor();
					} catch (IOException | InterruptedException e) {
						e.printStackTrace();
					}
					((JDialog)((java.awt.Component)arg0.getSource()).getParent().getParent().getParent().getParent().getParent()).dispose();
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
				((JDialog)((java.awt.Component)arg0.getSource()).getParent().getParent().getParent().getParent().getParent()).dispose();
			}
		});
		cancelButton.setActionCommand("Cancel");
		buttonPane.add(cancelButton);

	}
}

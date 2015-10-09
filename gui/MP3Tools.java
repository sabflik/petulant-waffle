package gui;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JPanel;

public class MP3Tools extends JPanel {

	public MP3Tools() {
		setBackground(Color.ORANGE);
		setLayout(new GridBagLayout());

		GridBagConstraints gb = new GridBagConstraints();
		gb.fill = GridBagConstraints.HORIZONTAL;
	}
}

package vidivox.guicomponents;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.io.File;
import javax.swing.JLabel;
import javax.swing.JPanel;
import video.Video;

@SuppressWarnings("serial")
public class VideoLabel extends JPanel {

	private JLabel videoLabel;
	
	public VideoLabel() {
		
		setLayout(new BorderLayout());
		
		videoLabel = new JLabel("No Video chosen");
		videoLabel.setFont(new Font("Tahoma", Font.PLAIN, 10));
		videoLabel.setForeground(Color.orange);
		add(videoLabel, BorderLayout.WEST);
	}
	
	public void setCurrentVideo() {
		File videoFile = new File(Video.getVideoName());
		videoLabel.setText("Chosen Video: " + videoFile.getName());
	}
}

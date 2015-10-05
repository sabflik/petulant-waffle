package gui;

import javax.swing.JLabel;
import javax.swing.JProgressBar;
import javax.swing.SwingWorker;

import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer;

public class ProgressBar extends SwingWorker<Void,Void>{
	
	JProgressBar progressBar;
	EmbeddedMediaPlayer mediaPlayer;
	JLabel timeStamp;

	public ProgressBar(JProgressBar progressBar, JLabel timeStamp, EmbeddedMediaPlayer mediaPlayer) {
		this.progressBar = progressBar;
		this.mediaPlayer = mediaPlayer;
		this.timeStamp = timeStamp;
	}
	
	@Override
	protected Void doInBackground() throws Exception {
		int position = 0;
		
		while(true) {
			//progressBar.setMaximum((int)mediaPlayer.getLength());
			//float currentPos = mediaPlayer.getTime();
			float currentPos = mediaPlayer.getPosition()*100;
			position = (int)currentPos;
		
			progressBar.setValue(position);
			Long time = mediaPlayer.getTime();
			timeStamp.setText(time.toString());
		}
	}
}

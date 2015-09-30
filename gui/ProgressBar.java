package gui;

import javax.swing.JProgressBar;
import javax.swing.SwingWorker;

import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer;

public class ProgressBar extends SwingWorker<Void,Void>{
	
	JProgressBar progressBar;
	EmbeddedMediaPlayer mediaPlayer;

	public ProgressBar(JProgressBar progressBar, EmbeddedMediaPlayer mediaPlayer) {
		this.progressBar = progressBar;
		this.mediaPlayer = mediaPlayer;
	}
	
	@Override
	protected Void doInBackground() throws Exception {
		int position = 0;
		
		while(true) {
			float currentPos = mediaPlayer.getPosition()*100;
			position = (int)currentPos;
		
			progressBar.setValue(position);
		}
	}
}

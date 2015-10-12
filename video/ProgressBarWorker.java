package video;

import javax.swing.JLabel;
import javax.swing.JProgressBar;
import javax.swing.SwingWorker;

import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer;

public class ProgressBarWorker extends SwingWorker<Void,Void>{
	
	JProgressBar progressBar;
	EmbeddedMediaPlayer mediaPlayer;
	JLabel timeStamp;

	public ProgressBarWorker(JProgressBar progressBar, JLabel timeStamp, EmbeddedMediaPlayer mediaPlayer) {
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
			timeStamp.setText(speechTiming(time));
		}
	}
	
	//Sets the selected time for mp3 placement
	public String speechTiming(float time) {
		if(time != -1) {
			float timeInSeconds = time / 1000;
			int sec = (int)timeInSeconds % 60;
			int min = (int)(timeInSeconds / 60) % 60;
			return String.format("%02d:%02d", min, sec);
		} else {
			return "00:00";
		}
	}
	
}

package video.swingworkers;

import javax.swing.JLabel;
import javax.swing.JProgressBar;
import javax.swing.SwingWorker;

import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer;

/**This SwingWorker class constantly updates the position of the video progress bar,
 * the time label displayed and the total length label.
 * @author Sabrina
 */
public class ProgressBarWorker extends SwingWorker<Void, Void> {

	JProgressBar progressBar;
	EmbeddedMediaPlayer mediaPlayer;
	JLabel timeStamp;
	JLabel lengthStamp;

	/**
	 * @param progressBar	The video's progress bar
	 * @param timeStamp		The current play back time
	 * @param lengthStamp	The total length of the video
	 * @param mediaPlayer	The media player instance
	 */
	public ProgressBarWorker(JProgressBar progressBar, JLabel timeStamp, JLabel lengthStamp,
			EmbeddedMediaPlayer mediaPlayer) {
		this.progressBar = progressBar;
		this.mediaPlayer = mediaPlayer;
		this.timeStamp = timeStamp;
		this.lengthStamp = lengthStamp;
	}

	/* (non-Javadoc)
	 * @see javax.swing.SwingWorker#doInBackground()
	 */
	@Override
	protected Void doInBackground() throws Exception {
		int position = 0;

		while (true) {
			// Set position
			float currentPos = mediaPlayer.getPosition() * 100;
			position = (int) currentPos;

			progressBar.setValue(position);
			timeStamp.setText(speechTiming(mediaPlayer.getTime()));// Set current time
			lengthStamp.setText(speechTiming(mediaPlayer.getLength()));// Set total length
		}
	}

	// Convert milliseconds to preferred format
	public String speechTiming(float time) {
		if (time != -1) {
			float timeInSeconds = time / 1000;
			int sec = (int) timeInSeconds % 60;
			int min = (int) (timeInSeconds / 60) % 60;
			return String.format("%02d:%02d", min, sec);
		} else {
			return "00:00";
		}
	}

}

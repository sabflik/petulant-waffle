package mp3.swingworkers;

import java.io.IOException;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingWorker;

import mp3.MP3;
import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer;
import video.Video;
import vidivox.guicomponents.VideoLabel;
import vidivox.swingworkers.ProgressLoader;

/**This is the SwingWorker class that merges the video with mp3
 * @author Sabrina
 */
public class MP3OverlayWorker extends SwingWorker<Void, Void> {

	private float mp3TimeInMS;
	private JFileChooser chooser;
	private ProgressLoader progress;
	private JFrame frame;
	private EmbeddedMediaPlayer mediaPlayer;
	private VideoLabel label;

	/**
	 * @param mediaPlayer	The media player instance
	 * @param label			The label displaying the currently selected video
	 * @param frame			The main frame
	 * @param mp3TimeInMS	The time in milliseconds that the user chose for mp3
	 * @param chooser		The file chooser for saving said file
	 * @param progress		The progress bar to be displayed during the creation
	 */
	public MP3OverlayWorker(EmbeddedMediaPlayer mediaPlayer, VideoLabel label,
			JFrame frame, float mp3TimeInMS, JFileChooser chooser, ProgressLoader progress) {
		this.mp3TimeInMS = mp3TimeInMS;
		this.chooser = chooser;
		this.progress = progress;
		this.frame = frame;
		this.mediaPlayer = mediaPlayer;
		this.label = label;
	}

	/* (non-Javadoc)
	 * @see javax.swing.SwingWorker#doInBackground()
	 */
	@Override
	protected Void doInBackground() throws Exception {

		if (!isCancelled()) {

			String cmd;

			if (mp3TimeInMS != 0.0) { //Merge mp3 at given time 
				cmd = "ffmpeg -i " + Video.getVideoName() + " -i " + MP3.getMP3Name() + " -filter_complex '[1:a]adelay="
						+ mp3TimeInMS + "[aud2];[0:a][aud2]amix=inputs=2' -map 0:v -map 1:a "
						+ chooser.getSelectedFile().getAbsolutePath() + ".avi";
			} else {// Merge mp3 at beginning
				cmd = "ffmpeg -i " + Video.getVideoName() + " -i " + MP3.getMP3Name()
						+ " -filter_complex '[1:a][0:a]amix=inputs=2' -map 0:v -map 1:a "
						+ chooser.getSelectedFile().getAbsolutePath() + ".avi";
			}

			ProcessBuilder builder = new ProcessBuilder("/bin/bash", "-c", cmd);
			Process process;
			try {
				process = builder.start();
				process.waitFor();
			} catch (IOException | InterruptedException e) {
				e.printStackTrace();
			}
		}

		return null;
	}

	/* (non-Javadoc)
	 * @see javax.swing.SwingWorker#done()
	 */
	protected void done() {
		progress.disposeProgress();// After file creation, close the progress bar
		// Show successful save dialog
		JOptionPane.showMessageDialog(frame, chooser.getSelectedFile().getName() 
				+ " was successfully saved in "+chooser.getSelectedFile().getPath());
		// Prompt user is they'd like to open the created file
		String ObjButtons[] = { "Yes", "No" };
		int PromptResult = JOptionPane.showOptionDialog(null,
				"Do you want to open this file?", "Open created file",
				JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null,
				ObjButtons, ObjButtons[1]);
		if (PromptResult == JOptionPane.YES_OPTION) {
			Video.setVideoName(chooser.getSelectedFile().getAbsolutePath()+".avi");
			mediaPlayer.playMedia(Video.getVideoName());//If yes, set this as selected video
			label.setCurrentVideo();// Update video label
		}
	}
}
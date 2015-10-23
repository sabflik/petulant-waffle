package mp3.swingworkers;

import java.io.IOException;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingWorker;

import mp3.MP3;
import video.Video;
import vidivox.swingworkers.ProgressLoader;

public class MP3OverlayWorker extends SwingWorker<Void, Void> {

	private float mp3TimeInMS;
	private JFileChooser chooser;
	private ProgressLoader progress;
	private JFrame frame;

	public MP3OverlayWorker(JFrame frame, float mp3TimeInMS, JFileChooser chooser, ProgressLoader progress) {
		this.mp3TimeInMS = mp3TimeInMS;
		this.chooser = chooser;
		this.progress = progress;
		this.frame = frame;
	}

	@Override
	protected Void doInBackground() throws Exception {

		if (!isCancelled()) {

			String cmd;

			if (mp3TimeInMS != 0.0) {
				cmd = "ffmpeg -i " + Video.getVideoName() + " -i " + MP3.getMP3Name() + " -filter_complex '[1:a]adelay="
						+ mp3TimeInMS + "[aud2];[0:a][aud2]amix=inputs=2' -map 0:v -map 1:a "
						+ chooser.getSelectedFile().getAbsolutePath() + ".avi";
			} else {
				cmd = "ffmpeg -i " + Video.getVideoName() + " -i " + MP3.getMP3Name()
						+ " -filter_complex '[1:a][0:a]amix=inputs=2' -map 0:v -map 1:a "
						+ chooser.getSelectedFile().getAbsolutePath() + ".avi";
				System.out.println(cmd);
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

	protected void done() {
		progress.disposeProgress();
		JOptionPane.showMessageDialog(frame, chooser.getSelectedFile().getName() + " was successfully saved");
	}

}
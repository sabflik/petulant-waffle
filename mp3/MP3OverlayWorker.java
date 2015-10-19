package mp3;

import gui.ProgressLoader;
import java.io.IOException;
import javax.swing.SwingWorker;
import video.Video;

public class MP3OverlayWorker extends SwingWorker<Void,Void>{

	private float mp3TimeInMS;
	private String name;
	private ProgressLoader progress;
	
	public MP3OverlayWorker(float mp3TimeInMS, String name, ProgressLoader progress) {
		this.mp3TimeInMS = mp3TimeInMS;
		this.name = name;
		this.progress = progress;
	}
	
	@Override
	protected Void doInBackground() throws Exception {
		
		if (!isCancelled()) {
			
			String cmd;
			
			if(mp3TimeInMS != 0.0) {
				cmd = "ffmpeg -i "+Video.getVideoName()+" -i "+MP3.getMP3Name()+" -filter_complex '[1:a]adelay="+mp3TimeInMS+"[aud2];[0:a][aud2]amix=inputs=2' -map 0:v -map 1:a "+name+".avi";
			} else {
				cmd = "ffmpeg -i "+Video.getVideoName()+" -i "+MP3.getMP3Name()+" -filter_complex '[1:a][0:a]amix=inputs=2' -map 0:v -map 1:a "+name+".avi";
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
	}

}
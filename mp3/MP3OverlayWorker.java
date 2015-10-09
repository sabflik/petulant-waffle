package mp3;

import java.io.IOException;

import javax.swing.SwingWorker;

import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer;

public class MP3OverlayWorker extends SwingWorker<Void,Void>{

	private String video;
	private String audio;
	private EmbeddedMediaPlayer mediaPlayer;
	private float mp3TimeInMS;
	private String name;
	
	public MP3OverlayWorker(String video, String audio, EmbeddedMediaPlayer mediaPlayer, float mp3TimeInMS, String name) {
		this.video = video;
		this.audio = audio;
		this.mediaPlayer = mediaPlayer;
		this.mp3TimeInMS = mp3TimeInMS;
		this.name = name;
	}
	
	@Override
	protected Void doInBackground() throws Exception {
		if (!isCancelled()) {
			String cmd = "ffmpeg -i "+video+" -i "+audio+" -filter_complex '[1:a]adelay="+mp3TimeInMS+"[aud2];[0:a][aud2]amix=inputs=2' -map 0:v -map 1:a -y PWNewFiles/"+name+".avi";
//			String cmd = "ffmpeg -i "+video+" -i "+audio+" -map 0:v -map 1:a -y .PetulantWaffle/out.avi";
			System.out.println(cmd);
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
		if (!isCancelled()) {
			video = ".PetulantWaffle/out.avi";
//			mediaPlayer.playMedia(video);
		}
	}

}
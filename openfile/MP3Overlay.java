package openfile;

import java.io.IOException;

import javax.swing.SwingWorker;

import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer;

public class MP3Overlay extends SwingWorker<Void,Void>{

	private String video;
	private String audio;
	private EmbeddedMediaPlayer mediaPlayer;
	
	public MP3Overlay(String video, String audio, EmbeddedMediaPlayer mediaPlayer) {
		this.video = video;
		this.audio = audio;
		this.mediaPlayer = mediaPlayer;
	}
	
	@Override
	protected Void doInBackground() throws Exception {
		if (!isCancelled()) {
			String cmd = "ffmpeg -i "+video+" -i "+audio+" -map 0:v -map 1:a -y .PetulantWaffle/out.avi";
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
			mediaPlayer.playMedia(video);
		}
	}

}
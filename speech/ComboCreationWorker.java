package speech;


import javax.swing.SwingWorker;

import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer;
import video.Video;

public class ComboCreationWorker extends SwingWorker<Void,Void> {

	private AudioSetting setting;
	private String video;
	private String name;
	private EmbeddedMediaPlayer mediaPlayer;
	private float speechTimeInMS;
	
	public ComboCreationWorker(String video, String name, AudioSetting setting, EmbeddedMediaPlayer mediaPlayer, float speechTimeInMS) {
		this.setting = setting;
		this.video = video;
		this.name = name;
		this.mediaPlayer = mediaPlayer;
		this.speechTimeInMS = speechTimeInMS;
	}
	
	//Code from http://stackoverflow.com/questions/24804928/singler-line-ffmpeg-cmd-to-merge-video-audio-and-retain-both-audios
	@Override
	protected Void doInBackground() throws Exception {
		
					//Create the wav file from the text in Speech.txt
		String cmd = "text2wave .PetulantWaffle/Speech.txt -o .PetulantWaffle/speech.wav;"
					//Convert the wav file to mp3
					+ "ffmpeg -i .PetulantWaffle/speech.wav -y .PetulantWaffle/audio.mp3;";
			
		if(setting == AudioSetting.REPLACE) {
//			System.out.println("Replace!");
			//Remove video's audio if user chose replace
			cmd = cmd + "ffmpeg -i " + video + " -i .PetulantWaffle/audio.mp3 -filter_complex '[1:a]adelay="+speechTimeInMS+"[aud2];[0:a][aud2]amix=inputs=2' -map 0:v -map 1:a PWNewFiles/" + name + ".avi";
		} else {
			//Replace with merged audio and create file specified by the user
			cmd = cmd + "ffmpeg -i "+video+" -i .PetulantWaffle/audio.mp3 -filter_complex '[1:a]adelay="+speechTimeInMS+"[aud2];[0:a][aud2]amix=inputs=2' -map 0:v -map 1:a PWNewFiles/" + name + ".avi";
		}
		
		ProcessBuilder makeWav = new ProcessBuilder("/bin/bash", "-c", cmd);
 		Process processMW;
 		processMW = makeWav.start();
 		processMW.waitFor();
		
		return null;
	}
	
	@Override
	protected void done() {
		String newFile = "PWNewFiles/" + name + ".avi";
//		mediaPlayer.playMedia(newFile);
	}
	
}

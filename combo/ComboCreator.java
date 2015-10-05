package combo;

import gui.Video;

import javax.swing.SwingWorker;

import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer;

public class ComboCreator extends SwingWorker<Void,Void> {

	private AudioSetting setting;
	private String video;
	private String name;
	private EmbeddedMediaPlayer mediaPlayer;
	private String timing;
	
	public ComboCreator(String video, String name, AudioSetting setting, EmbeddedMediaPlayer mediaPlayer, String timing) {
		this.setting = setting;
		this.video = video;
		this.name = name;
		this.mediaPlayer = mediaPlayer;
		this.timing = timing;
	}
	
	//Code from http://stackoverflow.com/questions/24804928/singler-line-ffmpeg-cmd-to-merge-video-audio-and-retain-both-audios
	@Override
	protected Void doInBackground() throws Exception {
		
		String cmd;
		
		if(setting == AudioSetting.REPLACE) {
					//Create the wav file from the text in Speech.txt
			cmd = "text2wave .PetulantWaffle/Speech.txt -o .PetulantWaffle/speech.wav;"
					//Convert the wav file to mp3
					+ "ffmpeg -i .PetulantWaffle/speech.wav -y .PetulantWaffle/audio.mp3;"
					//Finally combine the new audio with the video file selected and name the created file according to the user's input
					+ "ffmpeg -i " + video + " -itsoffset 00:"+timing+" -i .PetulantWaffle/audio.mp3 -map 0:v -map 1:a PWNewFiles/" + name + ".avi";
			System.out.println(cmd);
		} else {
					//Create the wav file from the text in Speech.txt
			cmd = "text2wave .PetulantWaffle/Speech.txt -o .PetulantWaffle/speech.wav;"
					//Convert the wav file to mp3
					+ "ffmpeg -i .PetulantWaffle/speech.wav -y .PetulantWaffle/audio.mp3;"
					//Extract the audio from the chosen video
					+"ffmpeg -i " + video + " -y -vn -acodec copy .PetulantWaffle/video.mp3;"
					//Merge the audio from the video with the audio generated by Festival
					+"ffmpeg -i .PetulantWaffle/video.mp3 -i .PetulantWaffle/audio.mp3 -filter_complex amix=inputs=2:duration=first:dropout_transition=3 -y .PetulantWaffle/audiofinal.mp3;"
					//Remove the audio from the video
					+"ffmpeg -i "+ video + " -an -y .PetulantWaffle/tempVideo.avi;"
					//Replace with merged audio and create file specified by the user
					+ "ffmpeg -i .PetulantWaffle/tempVideo.avi -i .PetulantWaffle/audiofinal.mp3 -map 0:v -map 1:a PWNewFiles/" + name + ".avi";
	 	
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
		mediaPlayer.playMedia(newFile);
	}
	
}

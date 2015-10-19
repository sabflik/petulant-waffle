package speech;


import gui.ProgressLoader;
import javax.swing.SwingWorker;
import video.Video;

public class ComboCreationWorker extends SwingWorker<Void,Void> {

	private String name;
	private float speechTimeInMS;
	private ProgressLoader progress;
	
	public ComboCreationWorker(String name, float speechTimeInMS, ProgressLoader progress) {
		this.name = name;
		this.speechTimeInMS = speechTimeInMS;
		this.progress = progress;
	}
	
	//Code from http://stackoverflow.com/questions/24804928/singler-line-ffmpeg-cmd-to-merge-video-audio-and-retain-both-audios
	@Override
	protected Void doInBackground() throws Exception {
		
					//Create the wav file from the text in Speech.txt
		String cmd = "text2wave .PetulantWaffle/Speech.txt -o .PetulantWaffle/speech.wav;"
					//Convert the wav file to mp3
					+ "ffmpeg -i .PetulantWaffle/speech.wav -y .PetulantWaffle/audio.mp3;";
			
		if(speechTimeInMS != 0.0) {
		//Replace with merged audio and create file specified by the user
		cmd = cmd + "ffmpeg -i "+Video.getVideoName()+" -i .PetulantWaffle/audio.mp3 -filter_complex '[1:a]adelay="+speechTimeInMS+"[aud2];[0:a][aud2]amix=inputs=2' -map 0:v -map 1:a " + name + ".avi";
		} else {
			cmd = cmd + "ffmpeg -i "+Video.getVideoName()+" -i .PetulantWaffle/audio.mp3 -filter_complex '[0:a][1:a]amix=inputs=2' -map 0:v -map 1:a " + name + ".avi";
		}

		ProcessBuilder makeWav = new ProcessBuilder("/bin/bash", "-c", cmd);
 		Process processMW;
 		processMW = makeWav.start();
 		processMW.waitFor();
		
		return null;
	}
	
	@Override
	protected void done() {
		progress.disposeProgress();
	}
	
}

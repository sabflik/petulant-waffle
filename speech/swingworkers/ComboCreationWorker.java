package speech.swingworkers;


import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingWorker;

import speech.SchemeCreator;
import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer;
import video.Video;
import vidivox.guicomponents.VideoLabel;
import vidivox.swingworkers.ProgressLoader;

public class ComboCreationWorker extends SwingWorker<Void, Void> {

	private JFileChooser chooser;
	private float speechTimeInMS;
	private ProgressLoader progress;
	private boolean isMale;
	private String speech;
	private JFrame frame;
	private EmbeddedMediaPlayer mediaPlayer;
	private VideoLabel label;

	public ComboCreationWorker(EmbeddedMediaPlayer mediaPlayer, VideoLabel label,
			JFrame frame, JFileChooser chooser, float speechTimeInMS,
			ProgressLoader progress, String speech, boolean isMale) {
		this.chooser = chooser;
		this.speechTimeInMS = speechTimeInMS;
		this.progress = progress;
		this.isMale = isMale;
		this.speech = speech;
		this.frame = frame;
		this.mediaPlayer = mediaPlayer;
		this.label = label;
	}

	// Code from
	// http://stackoverflow.com/questions/24804928/singler-line-ffmpeg-cmd-to-merge-video-audio-and-retain-both-audios
	@Override
	protected Void doInBackground() throws Exception {
		
		String cmd = null;

		if (isMale) {
			
			try {// Writes speech to text file
				PrintWriter out = new PrintWriter(new FileWriter(
						".PetulantWaffle/Speech.txt"));
				out.println(speech);
				out.close();
			} catch (IOException e) {
				System.out.println("Couldn't write to Speech.txt");
			}
			
			// Create the wav file from the text in Speech.txt
			cmd = "text2wave .PetulantWaffle/Speech.txt -o .PetulantWaffle/speech.wav;";
		} else {
			SchemeCreator scheme = new SchemeCreator(speech);
			scheme.createMP3Scheme();
			
			cmd = "festival -b .PetulantWaffle/Scheme.scm;";
		}

		// Convert the wav file to mp3
		cmd = cmd + "ffmpeg -i .PetulantWaffle/speech.wav -y .PetulantWaffle/audio.mp3;";

		if (speechTimeInMS != 0.0) {
			// Replace with merged audio and create file specified by the user
			cmd = cmd + "ffmpeg -i " + Video.getVideoName()
					+ " -i .PetulantWaffle/audio.mp3 -filter_complex '[1:a]adelay=" + speechTimeInMS
					+ "[aud2];[0:a][aud2]amix=inputs=2' -map 0:v -map 1:a "	+ chooser.getSelectedFile()
					.getAbsolutePath() + ".avi";
		} else {
			cmd = cmd + "ffmpeg -i " + Video.getVideoName()
					+ " -i .PetulantWaffle/audio.mp3 -filter_complex '[0:a][1:a]amix=inputs=2' -map 0:v -map 1:a "
					+ chooser.getSelectedFile().getAbsolutePath() + ".avi";
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
		JOptionPane.showMessageDialog(frame, chooser.getSelectedFile().getName() 
				+ " was successfully saved in "+chooser.getSelectedFile().getPath());
		
		String ObjButtons[] = { "Yes", "No" };
		int PromptResult = JOptionPane.showOptionDialog(null,
				"Do you want to open this file?", "Open created file",
				JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null,
				ObjButtons, ObjButtons[1]);
		if (PromptResult == JOptionPane.YES_OPTION) {
			Video.setVideoName(chooser.getSelectedFile().getAbsolutePath()+".avi");
			mediaPlayer.playMedia(Video.getVideoName());
			label.setCurrentVideo();
		}
	}
}

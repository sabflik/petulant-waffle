package speech.swingworkers;


import javax.swing.JFileChooser;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import javax.swing.SwingWorker;

import speech.SchemeCreator;
import vidivox.swingworkers.ProgressLoader;

/**
 * This SwingWorker class converts the text entered into an mp3 file.
 */
public class CreateAudio extends SwingWorker<Void, Void> {

	private JFileChooser chooser;
	private String text;
	private ProgressLoader progress;
	private boolean isMale;

	public CreateAudio(String text, JFileChooser chooser,
			ProgressLoader progress, boolean isMale) throws IOException {
		this.text = text;
		this.chooser = chooser;
		this.progress = progress;
		this.isMale= isMale;
	}

	@Override
	protected Void doInBackground() throws Exception {

		String cmd;
		
		if (isMale) {
			// create the wav file and convert that wav file to mp3
			cmd = "text2wave .PetulantWaffle/Speech.txt -o .PetulantWaffle/speech.wav;"
					+ "ffmpeg -i .PetulantWaffle/speech.wav "
					+ chooser.getSelectedFile().getAbsolutePath() + ".mp3";
		} else {
			SchemeCreator scheme = new SchemeCreator(text);
			scheme.createMP3Scheme();
			
			cmd = "festival -b .PetulantWaffle/Scheme.scm;"
					+ "ffmpeg -i .PetulantWaffle/speech.wav "
					+ chooser.getSelectedFile().getAbsolutePath() + ".mp3";
		}
		
		ProcessBuilder makeWav = new ProcessBuilder("/bin/bash", "-c", cmd);

		PrintWriter out;
		Process processMW;

		try {
			out = new PrintWriter(new FileWriter(
					".PetulantWaffle/Speech.txt"));
			out.println(text);
			out.close();

			processMW = makeWav.start();
			processMW.waitFor();

		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}
		return null;
	}

	public void done() {
		progress.disposeProgress();
	}
}

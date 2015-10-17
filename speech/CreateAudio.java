package speech;

import javax.swing.JFileChooser;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import javax.swing.SwingWorker;

/**
 * This SwingWorker class converts the text entered into an mp3 file. 
 */
public class CreateAudio extends SwingWorker<Void, Void> {

	private JFileChooser chooser;
	private String text;

	public CreateAudio(final String text, final JFileChooser chooser) throws IOException {
		this.text = text;
		this.chooser = chooser;
	}

	@Override
	protected Void doInBackground() throws Exception {
		
		// create the wav file and convert that wav file to mp3
		String cmd = "text2wave .PetulantWaffle/Speech.txt -o .PetulantWaffle/speech.wav;"
				+ "ffmpeg -i .PetulantWaffle/speech.wav " + chooser.getSelectedFile().getAbsolutePath() + ".mp3";
		ProcessBuilder makeWav = new ProcessBuilder("/bin/bash", "-c", cmd);
		
		PrintWriter out;
		Process processMW;
		
		try {
			out = new PrintWriter(new FileWriter(".PetulantWaffle/Speech.txt"));
			out.println(text);
			out.close();
			
			processMW = makeWav.start();
			processMW.waitFor();
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}
		return null;
	}
}

package speech;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * This class creates a scheme file that alters the pitch of festival to sound more like a female
 * **/
public class SchemeCreator {

	private String speech;
	private PrintWriter out;

	public SchemeCreator(String speech) throws IOException {
		this.speech = speech;

		// Creates scheme file
		File hDir = new File(".PetulantWaffle/Scheme.scm");
		if (!hDir.exists()) {
			hDir.createNewFile();
		}
		// Changes pitch
		out = new PrintWriter(new FileWriter(".PetulantWaffle/Scheme.scm"));
		out.println("(set! duffint_params '((start 245)(end 230)))");
		out.println("(Parameter.set 'Int_Method 'DuffInt)");
		out.println("(Parameter.set 'Int_Target_Method Int_Targets_Default)");
	}
	// Speaks the text
	public void createSpeakScheme() throws IOException {
		out.println("(SayText \"" + speech + "\")");
		out.close();
	}
	//Saves the text as a wave file
	public void createMP3Scheme() {
		out.println("(utt.save.wave (Saytext \"" + speech + "\") \".PetulantWaffle/speech.wav\" 'riff)");
		out.close();
	}
}

package speech;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

/**This class creates a scheme file that alters the pitch of festival to sound more like a female
 * @author Sabrina
 */
public class SchemeCreator {

	private String speech;
	private PrintWriter out;

	/**
	 * @param speech		The text entered in text area to convert to speech
	 * @throws IOException	Writing to file might incur IOException
	 */
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
		String currentDirectory = System.getProperty("user.dir");
		out.println("(set! utt1 (Utterance Text \"" + speech + "\"))");
		out.println("(utt.synth utt1)");
		out.println("(utt.save.wave utt1 \""+currentDirectory+"/.PetulantWaffle/speech.wav\" 'riff)");
		out.close();
	}
}

package speech.swingworkers;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import javax.swing.JToggleButton;
import javax.swing.SwingWorker;

import speech.SchemeCreator;

/**This SwingWorker class calls festival in the background for the speech button 
 * in the main frame.
 * @author Sabrina
 */
public class Speech extends SwingWorker<Void, Void> { 
	
	private String speech;
	private Process process;
	private String pID;
	private JToggleButton speak;
	private boolean isMale;

	/**
	 * @param speech	The text to be converted to speech
	 * @param speak		Speak button
	 * @param isMale	Gender selection
	 */
	public Speech(String speech, JToggleButton speak, boolean isMale) {
		this.speak = speak;
		this.speech = speech; 
		this.isMale = isMale;
	}

	/* (non-Javadoc)
	 * @see javax.swing.SwingWorker#doInBackground()
	 */
	@Override
	protected Void doInBackground() throws Exception {

		String cmd;
		
		if (isMale) { // If male
			cmd = "echo $$;echo " + speech+ " | festival --tts; echo 'done'";
		} else {// If female, create scheme file
			SchemeCreator scheme = new SchemeCreator(speech);
			scheme.createSpeakScheme();
			
			cmd = "echo $$; festival -b .PetulantWaffle/Scheme.scm; echo 'done'";
		}
		
		ProcessBuilder builder = new ProcessBuilder("/bin/bash", "-c", cmd);

		process = builder.start();

		InputStream stdout = process.getInputStream();
		BufferedReader br = new BufferedReader(
				new InputStreamReader(stdout));
		pID = br.readLine();

		String finished = "in progress";// Determine when festival
		while (!isCancelled()) {// process ends
			if (br.ready()) {
				finished = br.readLine();
			}
			if (finished.equals("done")) {
				break;
			}
		}
		// Cancel the festival process
		String cmdKill = "kill -9 $(pstree -p " + pID
				+ " | grep -P -o '\\([0-9]+\\)' | grep -P -o '[0-9]+')";
		ProcessBuilder builderK = new ProcessBuilder("/bin/bash", "-c",
				cmdKill);
		process = builderK.start();
		
		setSpeak();

		return null;
	}
	// De-select the button when done
	private void setSpeak() {
		speak.setSelected(false);
		speak.setText("Speak");
	}
}

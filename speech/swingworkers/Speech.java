package speech.swingworkers;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import javax.swing.JToggleButton;
import javax.swing.SwingWorker;

import speech.SchemeCreator;

/**This class calls festival in the background for the speech button in the main frame**/
public class Speech extends SwingWorker<Void, Void> { 
	
	private String speech;
	private Process process;
	private String pID;
	private JToggleButton speak;
	private boolean isMale;

	public Speech(String speech, JToggleButton speak, boolean isMale) {
		this.speak = speak;
		this.speech = speech; 
		this.isMale = isMale;
	}

	@Override
	protected Void doInBackground() throws Exception { // call festival

		String cmd;
		
		if (isMale) {
			cmd = "echo $$;echo " + speech+ " | festival --tts; echo 'done'";
		} else {
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

		String finished = "in progress";
		while (!isCancelled()) {
			if (br.ready()) {
				finished = br.readLine();
			}
			if (finished.equals("done")) {
				break;
			}
		}

		String cmdKill = "kill -9 $(pstree -p " + pID
				+ " | grep -P -o '\\([0-9]+\\)' | grep -P -o '[0-9]+')";
		ProcessBuilder builderK = new ProcessBuilder("/bin/bash", "-c",
				cmdKill);
		process = builderK.start();
		
		setSpeak();

		return null;
	}

	private void setSpeak() {
		speak.setSelected(false);
		speak.setText("Speak");
	}

}

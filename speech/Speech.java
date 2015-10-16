package speech;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.swing.JToggleButton;
import javax.swing.SwingWorker;

public class Speech extends SwingWorker<Void, Void>{ //this class calls festival in the background for the speech button in the main frame

	private String speech;
	private Process process;
	private  String pID;
	private JToggleButton speak;
	
	public Speech(String speech, JToggleButton speak) {
		this.speak = speak;
		this.speech = speech.replace("\n", " "); //if new lines have been entered these are replaced with spaces so festival will run
	}
	
	@Override
	protected Void doInBackground() throws Exception { //call festival
		
		String cmd = "echo $$;echo "+speech+" | festival --tts; echo 'done'"; 
		ProcessBuilder builder = new ProcessBuilder("/bin/bash", "-c", cmd);
		
		process = builder.start();
			
		InputStream stdout = process.getInputStream();
	  	BufferedReader br = new BufferedReader(new InputStreamReader(stdout));
	  	pID = br.readLine();
	  	
	  	String finished = "in progress";
	  	while(!isCancelled()) {
			if (br.ready()) {
				finished = br.readLine();
			}
			if (finished.equals("done")) {
				break;
			}
		}
	  	
	  	String cmdKill = "kill -9 $(pstree -p "+pID+" | grep -P -o '\\([0-9]+\\)' | grep -P -o '[0-9]+')";
		ProcessBuilder builderK = new ProcessBuilder("/bin/bash", "-c", cmdKill);
		process = builderK.start();

		setSpeak();
		
		return null;
	}
	
	private void setSpeak() {
		speak.setSelected(false);
		speak.setText("Speak");
	}

}

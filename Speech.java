package gui;

import javax.swing.SwingWorker;

public class Speech extends SwingWorker<Void, Void>{ //this class calls festival in the background for the speech button in the main frame

	private String speech;
	private Process process;
	
	public Speech(String speech) {
		this.speech = speech.replace("\n", " "); //if new lines have been entered these are replaced with spaces so festival will run
	}
	
	@Override
	protected Void doInBackground() throws Exception { //call festival
		
		String cmd = "echo "+speech+" | festival --tts"; 
		
		ProcessBuilder builder = new ProcessBuilder("/bin/bash", "-c", cmd);
		
		try {
			process = builder.start();
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		
		return null;
	}

}

package gui;

import java.io.BufferedReader;
import java.io.FileReader;

import speech.SpeechTab;

public class WorkspaceLoader {

	private SpeechTab sTab;
	private SpeechTools sTools;
	private MP3Tools mTools;

	public WorkspaceLoader(SpeechTab sTab, SpeechTools sTools, MP3Tools mTools) {
		this.mTools = mTools;
		this.sTab = sTab;
		this.sTools = sTools;
	}
	
	public void load() throws Exception {
		
		String speech = null;// Loads the saved speech
		String line = null;
	    @SuppressWarnings("resource")
		BufferedReader br = new BufferedReader(new FileReader(".PetulantWaffle/Workspace/speech.txt"));
	    while ((line = br.readLine()) != null) {
	    	speech = speech + line;
	    }
	    sTab.setText(speech);
	    
	    
	    
	}
}

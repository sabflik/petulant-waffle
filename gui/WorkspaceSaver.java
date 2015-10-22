package gui;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import mp3.MP3;
import speech.SpeechTab;
import video.Video;

public class WorkspaceSaver {
	
	private SpeechTab sTab;
	private SpeechTools sTools;
	private MP3Tools mTools;

	public WorkspaceSaver(SpeechTab sTab, SpeechTools sTools, MP3Tools mTools) {
		this.mTools = mTools;
		this.sTab = sTab;
		this.sTools = sTools;
	}
	
	public void save() throws IOException {
		// Creates file to store workspace information
		File dir = new File(".PetulantWaffle/Workspace");
		if (!dir.exists()) {
			dir.mkdir();
		}
		// Stores the speech entered
		File speechFile = new File(".PetulantWaffle/Workspace/speech.txt");
		speechFile.createNewFile();
		
		try {// Writes speech to text file
			PrintWriter out = new PrintWriter(new FileWriter(
					".PetulantWaffle/Workspace/speech.txt"));
			out.println(sTab.getText());
			out.close();
		} catch (IOException e) {
			System.out.println("Couldn't save speech to workspace");
		}
		
		// Stores the settings
		File settingsFile = new File(".PetulantWaffle/Workspace/settings.txt");
		settingsFile.createNewFile();
		
		try {// Writes settings to text file
			PrintWriter out = new PrintWriter(new FileWriter(
					".PetulantWaffle/Workspace/settings.txt"));
			out.println(Video.getVideoName());// Video name
			out.println(MP3.getMP3Name());// MP3 name
			out.println(sTools.isMaleSelected());// Selected gender
			out.println(sTools.getSpeechTiming());// Speech timing
			out.println(mTools.getMP3Timing());// MP3 timing
			out.close();
		} catch (IOException e) {
			System.out.println("Couldn't save settings to workspace");
		}
	}
}

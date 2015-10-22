package gui;

import java.io.BufferedReader;
import java.io.FileReader;

import mp3.MP3;

import speech.SpeechTab;
import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer;
import video.Video;

/**
 * This class loads the previous workspace by accessing a folder called "Workspace"
 * in the hidden directory ".PetulantWaffle"
 * **/
public class WorkspaceLoader {

	private SpeechTab sTab;
	private SpeechTools sTools;
	private MP3Tools mTools;
	private EmbeddedMediaPlayer mediaPlayer;

	public WorkspaceLoader(SpeechTab sTab, SpeechTools sTools, MP3Tools mTools, 
			EmbeddedMediaPlayer mediaPlayer) {
		this.mTools = mTools;
		this.sTab = sTab;
		this.sTools = sTools;
		this.mediaPlayer = mediaPlayer;
	}
	
	public void load() throws Exception {
		
		String speech = "";// Loads the saved speech
		String line;
	    @SuppressWarnings("resource")
		BufferedReader br = new BufferedReader(new FileReader(".PetulantWaffle/Workspace/speech.txt"));
	    
	    while ((line = br.readLine()) != null) {
	    	speech = speech + line + "\n";
	    }
	    sTab.setText(speech);
	    
	    @SuppressWarnings("resource")// Loads the saved video
		BufferedReader b = new BufferedReader(new FileReader(".PetulantWaffle/Workspace/settings.txt"));
	    line = b.readLine();
	    if(!line.equals("null")) {
	    	Video.setVideoName(line);
	    	mediaPlayer.playMedia(Video.getVideoName());
	    	if (sTab.isTextEnabled()) {
				sTools.setComboEnabled(true);
			}
	    }
	    
	    line = b.readLine();// Loads the saved mp3
	    if(!line.equals("null")) {
	    	MP3.setMP3Name(line);
	    	mTools.setMP3Selected();
	    	mTools.setMP3PlayEnabled(true);
	    	if(Video.getVideoName() != null) {
				mTools.setMP3ComboEnabled(true);
			}
	    }
	    
	    line = b.readLine();// Loads gender selection
	    sTools.setMaleSelected(line.equals(Boolean.valueOf(line)));
	    
	    line = b.readLine();// Loads speech time
	    sTools.speechTiming(Float.parseFloat(line));
	    
	    line = b.readLine();// Loads mp3 time
	    mTools.mp3Timing(Float.parseFloat(line));
	}
}

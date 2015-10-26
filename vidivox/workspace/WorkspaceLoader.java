package vidivox.workspace;

import java.io.BufferedReader;
import java.io.FileReader;
import mp3.MP3;
import speech.SpeechTab;
import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer;
import video.Video;
import vidivox.guicomponents.MP3Tools;
import vidivox.guicomponents.SpeechTools;
import vidivox.guicomponents.VideoLabel;

/**
 * This class loads the previous workspace by accessing a folder called "Workspace"
 * in the hidden directory ".PetulantWaffle"
 * @author Sabrina
 */
public class WorkspaceLoader {

	private SpeechTab sTab;
	private SpeechTools sTools;
	private MP3Tools mTools;
	private EmbeddedMediaPlayer mediaPlayer;
	private VideoLabel vLabel;

	/**
	 * @param vLabel		The label displaying the currently selected video
	 * @param sTab			the speech tab
	 * @param sTools		the speech Tools panel
	 * @param mTools		the mp3 Tools panel
	 * @param mediaPlayer	THe media player instance
	 */
	public WorkspaceLoader(VideoLabel vLabel, SpeechTab sTab, SpeechTools sTools, 
			MP3Tools mTools, EmbeddedMediaPlayer mediaPlayer) {
		this.mTools = mTools;
		this.sTab = sTab;
		this.sTools = sTools;
		this.mediaPlayer = mediaPlayer;
		this.vLabel = vLabel;
	}
	
	/**
	 * @throws Exception	Text is written into a file
	 */
	public void load() throws Exception {
		// Loads the saved speech
		String speech = "";
		String line;
	    @SuppressWarnings("resource")
		BufferedReader br = new BufferedReader(new FileReader(".PetulantWaffle/Workspace/speech.txt"));
	    
	    while ((line = br.readLine()) != null) {
	    	speech = speech + line + "\n";
	    }
	    sTab.setText(speech);
	 // Loads the saved video
	    @SuppressWarnings("resource")
		BufferedReader b = new BufferedReader(new FileReader(".PetulantWaffle/Workspace/settings.txt"));
	    line = b.readLine();
	    if(!line.equals("null")) {
	    	Video.setVideoName(line);
	    	mediaPlayer.playMedia(Video.getVideoName());
	    	vLabel.setCurrentVideo();
	    	
	    	if (sTab.isTextEnabled()) {
				sTools.setComboEnabled(true);
			}
	    }
	    // Loads the saved mp3
	    line = b.readLine();
	    if(!line.equals("null")) {
	    	MP3.setMP3Name(line);
	    	mTools.setMP3Selected();
	    	mTools.setMP3PlayEnabled(true);
	    	if(Video.getVideoName() != null) {
				mTools.setMP3ComboEnabled(true);
			}
	    }
	    // Loads gender selection
	    line = b.readLine();
	    sTools.setMaleSelected(line.equals(Boolean.valueOf(line)));
	    // Loads speech time
	    line = b.readLine();
	    sTools.speechTiming(Float.parseFloat(line));
	    // Loads mp3 time
	    line = b.readLine();
	    mTools.mp3Timing(Float.parseFloat(line));
	}
}

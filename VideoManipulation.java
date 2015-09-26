package gui;

import javax.swing.SwingWorker;

import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer;

public class VideoManipulation extends SwingWorker<Void, Void>{ //this class fast forwards/rewinds the video until another video manipulator button is clicked (done in background)
	
	private String type;
	private EmbeddedMediaPlayer mediaPlayer;
	
	public VideoManipulation(EmbeddedMediaPlayer mediaPlayer, String type) { //save inputted variables
		this.type = type;
		this.mediaPlayer = mediaPlayer;
	}

	@Override
	protected Void doInBackground() throws Exception {
		
		if(type.equals("ff")) { //if fast forward button is pressed
			if(mediaPlayer.isPlaying()) {
				while(!isCancelled() && (mediaPlayer.getTime() < (mediaPlayer.getLength() - 50))) {
					mediaPlayer.skip(50); //continue skipping forwards by 50 frames until another video manipulator button is clicked
				}
			}
		} else if(type.equals("rewind")) { //if rewind button is pressed
			if(mediaPlayer.isPlaying()) {
				while(!isCancelled() && (mediaPlayer.getTime() > 50)) {
					mediaPlayer.skip(-50); //continue skipping backwards by 50 frames until another video manipulator button is clicked
				}
			}
		}	
		return null;
	}
}

package video;

import javax.swing.SwingWorker;

import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer;

//this class fast forwards/rewinds the video until another video manipulator button is clicked (done in background)
public class VideoManipulationWorker extends SwingWorker<Void, Void>{
	
	private VMType type;
	//private String type;
	private EmbeddedMediaPlayer mediaPlayer;
	
	public VideoManipulationWorker(EmbeddedMediaPlayer mediaPlayer, VMType type) { //save inputted variables
		this.type = type;
		this.mediaPlayer = mediaPlayer;
	}

	@Override
	protected Void doInBackground() throws Exception {
		
		if(type == VMType.FASTFORWARD) { //if fast forward button is pressed
			if(mediaPlayer.isPlaying()) {
				while(!isCancelled() && (mediaPlayer.getTime() < (mediaPlayer.getLength() - 50))) {
					mediaPlayer.skip(50); //continue skipping forwards by 50 frames until another video manipulator button is clicked
				}
				if(!isCancelled() && mediaPlayer.getTime() >= (mediaPlayer.getLength() - 50)) {
					mediaPlayer.setTime(mediaPlayer.getLength());//If cannot fast forward anymore, skip to the end
				}
			}
		} else if(type == VMType.REWIND) { //if rewind button is pressed
			if(mediaPlayer.isPlaying()) {
				while(!isCancelled() && (mediaPlayer.getTime() > 50)) {
					mediaPlayer.skip(-50); //continue skipping backwards by 50 frames until another video manipulator button is clicked
				}
				if(!isCancelled() && mediaPlayer.getTime() <= 50) {
					mediaPlayer.setTime(0);//If cannot rewind anymore, skip to the beginning
				}
			}
		}	
		return null;
	}
}

package video.swingworkers;

import javax.swing.SwingWorker;

import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer;
import video.VMType;

/**This class fast forwards/rewinds the video until another video manipulator button is clicked 
 * (done in background)
 * @author Sabrina
 */
public class VideoManipulationWorker extends SwingWorker<Void, Void>{
	
	private VMType type;
	private EmbeddedMediaPlayer mediaPlayer;
	
	/**
	 * @param mediaPlayer	The media player instance
	 * @param type			Fast forward or Rewind
	 */
	public VideoManipulationWorker(EmbeddedMediaPlayer mediaPlayer, VMType type) { //save inputted variables
		this.type = type;
		this.mediaPlayer = mediaPlayer;
	}

	/* (non-Javadoc)
	 * @see javax.swing.SwingWorker#doInBackground()
	 */
	@Override
	protected Void doInBackground() throws Exception {
		
		if(type == VMType.FASTFORWARD) { //if fast forward button is pressed
			if(mediaPlayer.isPlaying()) {
				while(!isCancelled() && (mediaPlayer.getTime() < (mediaPlayer.getLength() - 10))) {
					mediaPlayer.skip(10); //continue skipping forwards by 50 frames until another video manipulator button is clicked
				}
				if(!isCancelled() && mediaPlayer.getTime() >= (mediaPlayer.getLength() - 10)) {
					mediaPlayer.setTime(mediaPlayer.getLength());//If cannot fast forward anymore, skip to the end
				}
			}
		} else if(type == VMType.REWIND) { //if rewind button is pressed
			if(mediaPlayer.isPlaying()) {
				while(!isCancelled() && (mediaPlayer.getTime() > 10)) {
					mediaPlayer.skip(-10); //continue skipping backwards by 50 frames until another video manipulator button is clicked
				}
				if(!isCancelled() && mediaPlayer.getTime() <= 10) {
					mediaPlayer.setTime(0);//If cannot rewind anymore, skip to the beginning
				}
			}
		}	
		return null;
	}
}

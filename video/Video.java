package video;

/**
 * This class is used to represent a Video object. It is a singleton object to
 * ensure that there is only ever one instance of it and is accessible from
 * any other class in the project. 
 * @author Sabrina
 */
public class Video {
	
	private static Video theInstance = null;
	private static String name = null;

	/**This method is used to create the singleton instance of video
	 * @return	the singleton video object
	 */
	public static Video getInstance() {
		if(theInstance == null) {
			theInstance = new Video();
		}
		return theInstance;
	}
	
	/**
	 * @param newName	The new name of the Video (used to change the 
	 * name of the video file)
	 */
	public static void setVideoName(String newName) {
		Video.name = newName;
	}
	
	/**
	 * Destroys the singleton object
	 */
	public static void destroy() {
		theInstance = null;
	}
	
	/**Retrieves the name of the current Video file chosen
	 * @return	name of video
	 */
	public static String getVideoName() {
		return Video.name;
	}
}

package gui;

public class Video {
	private static Video theInstance = null;
	private static String name = null;
	
	private Video() {
	}

	public static Video getInstance() {
		if(theInstance == null) {
			theInstance = new Video();
		}
		return theInstance;
	}
	
	public static void setVideoName(String newName) {
		Video.name = newName;
	}
	
	public static void destroy() {
		theInstance = null;
	}
	
	public static String getVideoName() {
		return Video.name;
	}
}

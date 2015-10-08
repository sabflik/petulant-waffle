package mp3;

public class MP3 {

	private static MP3 theInstance = null;
	private static String name = null;
	
	private MP3() {
	}

	public static MP3 getInstance() {
		if(theInstance == null) {
			theInstance = new MP3();
		}
		return theInstance;
	}
	
	public static void setMP3Name(String newName) {
		MP3.name = newName;
	}
	
	public static void destroy() {
		theInstance = null;
	}
	
	public static String getMP3Name() {
		return MP3.name;
	}
}

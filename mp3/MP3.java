package mp3;

/**
 * This class is used to represent an MP3 object. It is a singleton object to
 * ensure that there is only ever one instance of it and is accessible from
 * any other class in the project. 
 * @author Sabrina
 */
public class MP3 {

	private static MP3 theInstance = null;
	private static String name = null;

	/**This method is used to create the singleton instance of mp3
	 * @return	the singleton mp3 object
	 */
	public static MP3 getInstance() {
		if (theInstance == null) {
			theInstance = new MP3();
		}
		return theInstance;
	}

	/**
	 * @param newName	The new name of the MP3 (used to change the 
	 * name of the mp3 file)
	 */
	public static void setMP3Name(String newName) {
		MP3.name = newName;
	}

	/**
	 * Destroys the singleton object
	 */
	public static void destroy() {
		theInstance = null;
	}

	/**Retrieves the name of the current MP3 file chosen
	 * @return	name of mp3
	 */
	public static String getMP3Name() {
		return MP3.name;
	}
}

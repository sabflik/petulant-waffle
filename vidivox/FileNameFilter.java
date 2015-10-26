package vidivox;

/**This class ensures that the filename entered does not contain
 * any invalid characters.
 * @author Sabrina
 */
public class FileNameFilter {
	
	/**
	 * @param filename	Name of the file entered
	 * @return			Whether or not the name is valid
	 */
	public boolean isValid(String filename) {
		String[] invalidChars = {" ", "\\", "?", "%", "*", "|", "\"", "<", ">"};
		for (int i = 0; i < invalidChars.length; i++) {
			if (filename.contains(invalidChars[i])) {
				return false;
			}
		}
		return true;
	}
}

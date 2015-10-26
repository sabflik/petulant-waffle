package vidivox;

public class FileNameFilter {
	
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

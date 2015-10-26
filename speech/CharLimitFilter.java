package speech;

import java.awt.Toolkit;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;

/**This class extends DocumentFilter and is used by SpeechTab to set a character limit to its
 * JTextArea. This code is from: http://www.jroller.com/dpmihai/entry/documentfilter
 * @author Sabrina
 */
public class CharLimitFilter extends DocumentFilter {

	private int maxChars;

	/**
	 * @param maxChars	character limit
	 */
	public CharLimitFilter(int maxChars) {
		this.maxChars = maxChars;
	}

	/* (non-Javadoc)
	 * @see javax.swing.text.DocumentFilter#insertString(javax.swing.text.DocumentFilter.FilterBypass, int, java.lang.String, javax.swing.text.AttributeSet)
	 */
	public void insertString(FilterBypass fb, int offs, String str, AttributeSet a) throws BadLocationException {

		if ((fb.getDocument().getLength() + str.length()) <= maxChars) {
			super.insertString(fb, offs, str, a);
		} else {
			Toolkit.getDefaultToolkit().beep();
		}
	}

	/* (non-Javadoc)
	 * @see javax.swing.text.DocumentFilter#replace(javax.swing.text.DocumentFilter.FilterBypass, int, int, java.lang.String, javax.swing.text.AttributeSet)
	 */
	public void replace(FilterBypass fb, int offs, int length, String str, AttributeSet a) throws BadLocationException {

		if ((fb.getDocument().getLength() + str.length() - length) <= maxChars) {
			super.replace(fb, offs, length, str, a);
		} else {
			Toolkit.getDefaultToolkit().beep();
		}
	}

}

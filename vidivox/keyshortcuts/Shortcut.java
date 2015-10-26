package vidivox.keyshortcuts;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.KeyStroke;

import video.VideoTab;
import vidivox.guicomponents.*;

/**This class sets up the shortcuts that VIDIVOX utilises
 * @author Sabrina
 */
public class Shortcut {

	private VideoTab vTab;
	private MP3Tools mTools;
	private SpeechTools sTools;

	/**
	 * @param vTab		The video tab
	 * @param mTools	The mp3 Tools panel
	 * @param sTools	The speech Tools panel
	 */
	public Shortcut(VideoTab vTab, MP3Tools mTools, SpeechTools sTools) {
		this.vTab = vTab;
		this.mTools = mTools;
		this.sTools = sTools;
	}

	/**This method creates the shortcuts
	 * @param frame	The main JFrame
	 */
	public void addShortcut(JFrame frame) {
		KeyStroke f2KeyStroke = KeyStroke// This links the F2 key to click the...
				.getKeyStroke(KeyEvent.VK_F2, 0, false);
		@SuppressWarnings("serial")
		Action mp3Action = new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				vTab.getButtonPane().clickPlayPause();//...Pause/Play button of the video tab
				mTools.clickMP3Cancel();// and the MP3 play button of the mp3 tools panel
			}
		};
		frame.getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
				.put(f2KeyStroke, "MP3");
		frame.getRootPane().getActionMap().put("MP3", mp3Action);

		KeyStroke f1KeyStroke = KeyStroke// This links the F1 key to click the...
				.getKeyStroke(KeyEvent.VK_F1, 0, false);
		@SuppressWarnings("serial")
		Action speechAction = new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				vTab.getButtonPane().clickPlayPause();//...Pause/Play button of the video tab
				sTools.clickSpeakCancel();// and the speak button of the speech tools panel
			}
		};
		frame.getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
				.put(f1KeyStroke, "SPEECH");
		frame.getRootPane().getActionMap().put("SPEECH", speechAction);
	}
}

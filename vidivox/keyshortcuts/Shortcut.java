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

public class Shortcut {

	private VideoTab vTab;
	private MP3Tools mTools;
	private SpeechTools sTools;

	public Shortcut(VideoTab vTab, MP3Tools mTools, SpeechTools sTools) {
		this.vTab = vTab;
		this.mTools = mTools;
		this.sTools = sTools;
	}

	public void addShortcut(JFrame frame) {
		KeyStroke f2KeyStroke = KeyStroke
				.getKeyStroke(KeyEvent.VK_F2, 0, false);
		@SuppressWarnings("serial")
		Action mp3Action = new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				vTab.getButtonPane().clickPlayPause();
				mTools.clickMP3Cancel();
			}
		};
		frame.getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
				.put(f2KeyStroke, "MP3");
		frame.getRootPane().getActionMap().put("MP3", mp3Action);

		KeyStroke f1KeyStroke = KeyStroke
				.getKeyStroke(KeyEvent.VK_F1, 0, false);
		@SuppressWarnings("serial")
		Action speechAction = new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				vTab.getButtonPane().clickPlayPause();
				sTools.clickSpeakCancel();
			}
		};
		frame.getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
				.put(f1KeyStroke, "SPEECH");
		frame.getRootPane().getActionMap().put("SPEECH", speechAction);
	}
}

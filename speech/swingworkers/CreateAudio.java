package speech.swingworkers;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import javax.swing.SwingWorker;

import mp3.MP3;
import speech.SchemeCreator;
import video.Video;
import vidivox.guicomponents.MP3Tools;
import vidivox.swingworkers.ProgressLoader;

/**This SwingWorker class converts the text entered into an mp3 file.
 * @author Sabrina
 */
public class CreateAudio extends SwingWorker<Void, Void> {

	private JFileChooser chooser;
	private String text;
	private ProgressLoader progress;
	private boolean isMale;
	private JFrame frame;
	private MP3Tools mTools;

	/**
	 * @param frame			The main frame
	 * @param mTools		mp3 tools panel
	 * @param text			The text to be used as speech
	 * @param chooser		The file chooser for saving said file
	 * @param progress		The progress bar to be displayed during the creation
	 * @param isMale		Gender selection
	 */
	public CreateAudio(JFrame frame, MP3Tools mTools,
			String text, JFileChooser chooser, ProgressLoader progress, boolean isMale) {
		this.text = text;
		this.chooser = chooser;
		this.progress = progress;
		this.isMale = isMale;
		this.frame = frame;
		this.mTools = mTools;
	}

	/* (non-Javadoc)
	 * @see javax.swing.SwingWorker#doInBackground()
	 */
	@Override
	protected Void doInBackground() throws Exception {

		String cmd;

		if (isMale) {// If male
			// create the wav file and convert that wav file to mp3
			cmd = "text2wave .PetulantWaffle/Speech.txt -o .PetulantWaffle/speech.wav;"
					+ "ffmpeg -i .PetulantWaffle/speech.wav " + chooser.getSelectedFile().getAbsolutePath() + ".mp3";
		} else {// If female create scheme file
			SchemeCreator scheme = new SchemeCreator(text);
			scheme.createMP3Scheme();

			cmd = "festival -b .PetulantWaffle/Scheme.scm;" + "ffmpeg -i .PetulantWaffle/speech.wav "
					+ chooser.getSelectedFile().getAbsolutePath() + ".mp3";
		}

		ProcessBuilder makeWav = new ProcessBuilder("/bin/bash", "-c", cmd);

		PrintWriter out;
		Process processMW;

		try {
			out = new PrintWriter(new FileWriter(".PetulantWaffle/Speech.txt"));
			out.println(text);
			out.close();

			processMW = makeWav.start();
			processMW.waitFor();

		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see javax.swing.SwingWorker#done()
	 */
	public void done() {
		progress.disposeProgress();// After file creation, close the progress bar
		// Show successful save dialog
		JOptionPane.showMessageDialog(frame, chooser.getSelectedFile().getName() 
				+ " was successfully saved in "+ chooser.getSelectedFile().getPath());
		// Prompt user is they'd like to open the created file
		String ObjButtons[] = { "Yes", "No" };
		int PromptResult = JOptionPane.showOptionDialog(null,
				"Do you want to open this file?", "Open created file",
				JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null,
				ObjButtons, ObjButtons[1]);
		if (PromptResult == JOptionPane.YES_OPTION) {
			MP3.setMP3Name(chooser.getSelectedFile().getAbsolutePath()+".mp3");
			mTools.setMP3Selected();//If yes, set this as selected mp3
			mTools.setMP3PlayEnabled(true);// Enable certain buttons
		}
		if(Video.getVideoName() != null) {
			mTools.setMP3ComboEnabled(true);
		}
	}
}

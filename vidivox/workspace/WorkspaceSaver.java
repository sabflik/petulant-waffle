package vidivox.workspace;


import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import javax.swing.JOptionPane;

import mp3.MP3;
import speech.SpeechTab;
import video.Video;
import vidivox.guicomponents.MP3Tools;
import vidivox.guicomponents.SpeechTools;

/**
 * This class saves the current workspace by writing to files inside a folder
 * called "Workspace" in side the ".PetulantWaffle" hidden folder.
 *  @author Sabrina
 * **/
public class WorkspaceSaver {

	private SpeechTab sTab;
	private SpeechTools sTools;
	private MP3Tools mTools;
	private boolean exit;

	/**
	 * @param sTab		The speech tab
	 * @param sTools	The speech tools panel
	 * @param mTools	The mp3 Tools panel
	 * @param exit		Is the user trying to exit
	 */
	public WorkspaceSaver(SpeechTab sTab, SpeechTools sTools, MP3Tools mTools,
			boolean exit) {
		this.mTools = mTools;
		this.sTab = sTab;
		this.sTools = sTools;
		this.exit = exit;
	}
	
	/**This method prompts the user if they want to save their workspace
	 * Code from:
	// http://stackoverflow.com/questions/15449022/show-prompt-before-closing-jframe
	 */
	public void promptSave() {
		String ObjButtons[] = { "Yes", "No", "Cancel" };
		int PromptResult = JOptionPane.showOptionDialog(null,
				"Do you want to save this workspace?", "Confirm Save",
				JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null,
				ObjButtons, ObjButtons[1]);
		// If no is selected and the user is trying to exit, the close the application
		if (exit && (PromptResult == JOptionPane.NO_OPTION)) {
			System.exit(0);
		// If the user selects yes, the save the workspace
		} else if (PromptResult == JOptionPane.YES_OPTION) {
			try {
				save();
			} catch (IOException e) {
				System.out.println("Couldn't save workspace, sorry :(");
			}
			if(exit) {
				System.exit(0);
			}
		}
	}

	/**
	 * @throws IOException	Tries to write settings to a file
	 */
	public void save() throws IOException {

		// Creates file to store workspace information
		File dir = new File(".PetulantWaffle/Workspace");
		if (!dir.exists()) {
			dir.mkdir();
		}
		// Stores the speech entered
		File speechFile = new File(".PetulantWaffle/Workspace/speech.txt");
		speechFile.createNewFile();

		try {// Writes speech to text file
			PrintWriter out = new PrintWriter(new FileWriter(
					".PetulantWaffle/Workspace/speech.txt"));
			out.println(sTab.getText());
			out.close();
		} catch (IOException e) {
			System.out.println("Couldn't save speech to workspace");
		}

		// Stores the settings
		File settingsFile = new File(".PetulantWaffle/Workspace/settings.txt");
		settingsFile.createNewFile();

		try {// Writes settings to text file
			PrintWriter out = new PrintWriter(new FileWriter(
					".PetulantWaffle/Workspace/settings.txt"));
			out.println(Video.getVideoName());// Video name
			out.println(MP3.getMP3Name());// MP3 name
			out.println(sTools.isMaleSelected());// Selected gender
			out.println(sTools.getSpeechTiming());// Speech timing
			out.println(mTools.getMP3Timing());// MP3 timing
			out.close();
		} catch (IOException e) {
			System.out.println("Couldn't save settings to workspace");
		}
	}
}

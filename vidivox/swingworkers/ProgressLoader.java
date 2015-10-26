package vidivox.swingworkers;

import java.awt.BorderLayout;
import java.awt.Color;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.SwingWorker;

/**This SwingWorker class creates the indeterminate progress dialog while files
 * are being created to keep the user informed of the creation and closes it when
 * it's done.
 * @author Sabrina
 */
public class ProgressLoader extends SwingWorker<Void, Void> {

	private JFrame frame;
	private ProgressManager loading;
	
	/**
	 * @param frame	 THe main frame
	 */
	public ProgressLoader(JFrame frame) {
		this.frame = frame;
	}
	
	/* (non-Javadoc)
	 * @see javax.swing.SwingWorker#doInBackground()
	 */
	@Override
	protected Void doInBackground() throws Exception {
		// Created the progress dialog
		loading = new ProgressManager(frame);
		loading.setVisible(true);
		
		return null;
	}
	
	/**
	 * Closes the dialog
	 */
	public void disposeProgress() {
		loading.dispose();
	}
}

/**
 * This class is the actual dialog with the progress bar
 */
@SuppressWarnings({ "serial" }) 
class ProgressManager extends JDialog {

	private JProgressBar bar;
	private final JPanel contentPane = new JPanel();

	/**
	 * @param frame	 The main JFrame
	 */
	public ProgressManager(JFrame frame) {
		super(frame, "Creating file...", true);
		setBounds(300, 300, 350, 70);
	
		getContentPane().setLayout(new BorderLayout());
		getContentPane().add(contentPane, BorderLayout.CENTER);
	
		contentPane.setLayout(new BorderLayout());
		// This is the progress bar
		bar = new JProgressBar();
		bar.setIndeterminate(true);
		bar.setForeground(Color.orange);
		bar.setBackground(Color.DARK_GRAY);
		contentPane.add(bar, BorderLayout.CENTER);
		// Label to keep the user informed
		JLabel label = new JLabel("Please be patient. This might take a while!");
		contentPane.add(label, BorderLayout.NORTH);
	}
}
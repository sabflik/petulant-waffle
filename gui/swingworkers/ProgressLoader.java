package gui.swingworkers;

import java.awt.BorderLayout;
import java.awt.Color;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.SwingWorker;

public class ProgressLoader extends SwingWorker<Void, Void> {

	private JFrame frame;
	private ProgressManager loading;
	
	public ProgressLoader(JFrame frame) {
		this.frame = frame;
	}
	
	@Override
	protected Void doInBackground() throws Exception {
		
		loading = new ProgressManager(frame);
		loading.setVisible(true);
		
		return null;
	}
	
	public void disposeProgress() {
		loading.dispose();
	}
}

@SuppressWarnings({ "serial" }) 
class ProgressManager extends JDialog {

	private JProgressBar bar;
	private final JPanel contentPane = new JPanel();

	public ProgressManager(JFrame frame) {
		super(frame, "Creating file...", true);
		setBounds(300, 300, 350, 70);
	
		getContentPane().setLayout(new BorderLayout());
		getContentPane().add(contentPane, BorderLayout.CENTER);
	
		contentPane.setLayout(new BorderLayout());
		
		bar = new JProgressBar();
		bar.setIndeterminate(true);
		bar.setForeground(Color.orange);
		bar.setBackground(Color.DARK_GRAY);
		contentPane.add(bar, BorderLayout.CENTER);
	
		JLabel label = new JLabel("Please be patient. This might take a while!");
		contentPane.add(label, BorderLayout.NORTH);
	}
}
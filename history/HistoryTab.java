package history;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.TableModel;

@SuppressWarnings("serial")
public class HistoryTab extends JPanel {

	public HistoryTab() {
		setBackground(Color.black);

		setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();

		// TABLE
		Object rowData[][] = { { "Row1-Column1", "Row1-Column2", "Row1-Column3" },
				{ "Row2-Column1", "Row2-Column2", "Row2-Column3" } };
		Object columnNames[] = { "Column One", "Column Two", "Column Three" };
		JTable table = new JTable(rowData, columnNames);
		add(table, c);
	}
}

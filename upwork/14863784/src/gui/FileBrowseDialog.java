package gui;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import javax.swing.*;

public class FileBrowseDialog extends JDialog {
	private JTextField fileField;
	private JFileChooser fileChooser;
	private boolean changed;

	public FileBrowseDialog(Frame owner, String title, File initialValue,
			JFileChooser fileChooser) {
		super(owner, title, true);
		this.fileChooser = fileChooser;

		JPanel panel = new JPanel(new BorderLayout());
		fileField = new JTextField(initialValue.getPath());
		panel.add(new JLabel(title, JLabel.CENTER), BorderLayout.NORTH);
		panel.add(fileField, BorderLayout.CENTER);

		// Browsing opens up the file chooser the user specifies.
		JButton browseButton = new JButton("Browse...");
		browseButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int result = getFileChooser().showOpenDialog(
						FileBrowseDialog.this);
				if (result == JFileChooser.APPROVE_OPTION) {
					setSelectedFile(getFileChooser().getSelectedFile());
				}
			}
		});
		panel.add(browseButton, BorderLayout.EAST);

		JPanel bottomButtons = new JPanel();
		JButton okButton = new JButton("OK");
		okButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setChanged(true);
				dispose();
			}
		});
		bottomButtons.add(okButton);
		JButton cancelButton = new JButton("Cancel");
		cancelButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setChanged(false);
				dispose();
			}
		});
		bottomButtons.add(cancelButton);
		panel.add(bottomButtons, BorderLayout.SOUTH);

		getContentPane().add(panel, BorderLayout.NORTH);

		setSize(400, 100);

		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent windowEvent) {
				setChanged(false);
				dispose();
			}
		});
		setVisible(true);
	}

	public boolean getChanged() {
		return changed;
	}

	public void setChanged(boolean changed) {
		this.changed = changed;
	}

	public JFileChooser getFileChooser() {
		return fileChooser;
	}

	public void setSelectedFile(File f) {
		fileField.setText(f.getPath());
	}

	public File getSelectedFile() {
		return new File(fileField.getText());
	}
}

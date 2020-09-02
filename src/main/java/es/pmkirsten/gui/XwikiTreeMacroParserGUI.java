package es.pmkirsten.gui;

import java.awt.EventQueue;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;

import es.pmkirsten.parser.XwikiTreeMacroPathParser;

public class XwikiTreeMacroParserGUI {

	protected JFrame frame;
	protected JFileChooser fileChooser = new JFileChooser();
	protected String path = null;
	protected JTextArea textArea;
	protected JTextPane textPane;
	protected JButton btnFileChooser;
	protected JButton btnParseFolder;
	protected JButton btnCopyText;
	protected ActionListener fileChooserActionListener = new ActionListener() {

		@Override
		public void actionPerformed(ActionEvent e) {
			String path = XwikiTreeMacroParserGUI.this.path == null ? "." : XwikiTreeMacroParserGUI.this.path;
			XwikiTreeMacroParserGUI.this.fileChooser.setCurrentDirectory(new java.io.File(path));
			XwikiTreeMacroParserGUI.this.fileChooser.setDialogTitle("Seleccionar carpeta");
			XwikiTreeMacroParserGUI.this.fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			XwikiTreeMacroParserGUI.this.fileChooser.setAcceptAllFileFilterUsed(false);

			if (XwikiTreeMacroParserGUI.this.fileChooser.showOpenDialog(XwikiTreeMacroParserGUI.this.frame) == JFileChooser.APPROVE_OPTION) {
				XwikiTreeMacroParserGUI.this.path = XwikiTreeMacroParserGUI.this.fileChooser.getSelectedFile().getAbsolutePath();
				XwikiTreeMacroParserGUI.this.textPane.setText(XwikiTreeMacroParserGUI.this.path);
				XwikiTreeMacroParserGUI.this.btnParseFolder.doClick();

			} else {
				System.out.println("No Selection ");
			}
		}

	};

	protected ActionListener parseTreeActionListener = new ActionListener() {

		@Override
		public void actionPerformed(ActionEvent e) {
			XwikiTreeMacroPathParser parser = new XwikiTreeMacroPathParser();
			String stringTree = parser.walk(XwikiTreeMacroParserGUI.this.path);
			XwikiTreeMacroParserGUI.this.textArea.setText(stringTree);
		}
	};

	protected ActionListener copyActionListener = new ActionListener() {

		@Override
		public void actionPerformed(ActionEvent e) {
			StringSelection stringSelection = new StringSelection(XwikiTreeMacroParserGUI.this.textArea.getText());
			Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
			clipboard.setContents(stringSelection, null);

		}
	};


	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
					for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
						if ("Nimbus".equals(info.getName())) {
							UIManager.setLookAndFeel(info.getClassName());
							break;
						}
					}
					XwikiTreeMacroParserGUI window = new XwikiTreeMacroParserGUI();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public XwikiTreeMacroParserGUI() {
		this.initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		this.frame = new JFrame();
		this.frame.setBounds(100, 100, 850, 500);
		this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[] { 0, 0, 0, 0, 0 };
		gridBagLayout.rowHeights = new int[] { 0, 0, 0, 0, 0, 0, 0 };
		gridBagLayout.columnWeights = new double[] { 0.0, 0.0, 0.0, 1.0, Double.MIN_VALUE };
		gridBagLayout.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, Double.MIN_VALUE };
		this.frame.getContentPane().setLayout(gridBagLayout);

		JLabel lblCarpeta = new JLabel("Carpeta:");
		lblCarpeta.setHorizontalAlignment(SwingConstants.CENTER);
		GridBagConstraints gbc_lblCarpeta = new GridBagConstraints();
		gbc_lblCarpeta.anchor = GridBagConstraints.WEST;
		gbc_lblCarpeta.insets = new Insets(0, 0, 5, 5);
		gbc_lblCarpeta.gridx = 0;
		gbc_lblCarpeta.gridy = 0;
		this.frame.getContentPane().add(lblCarpeta, gbc_lblCarpeta);

		JScrollPane scrollPane = new JScrollPane();
		GridBagConstraints gbc_scrollPane = new GridBagConstraints();
		gbc_scrollPane.gridheight = 6;
		gbc_scrollPane.fill = GridBagConstraints.BOTH;
		gbc_scrollPane.gridx = 3;
		gbc_scrollPane.gridy = 0;
		this.frame.getContentPane().add(scrollPane, gbc_scrollPane);

		this.textArea = new JTextArea();
		scrollPane.setViewportView(this.textArea);

		this.textPane = new JTextPane();
		this.textPane.setEditable(false);
		GridBagConstraints gbc_textPane = new GridBagConstraints();
		gbc_textPane.gridwidth = 3;
		gbc_textPane.insets = new Insets(0, 0, 5, 5);
		gbc_textPane.fill = GridBagConstraints.BOTH;
		gbc_textPane.gridx = 0;
		gbc_textPane.gridy = 1;
		this.frame.getContentPane().add(this.textPane, gbc_textPane);

		this.btnFileChooser = new JButton("Selecciona carpeta...");
		GridBagConstraints gbc_btnFileChooser = new GridBagConstraints();
		gbc_btnFileChooser.insets = new Insets(0, 0, 5, 5);
		gbc_btnFileChooser.gridx = 2;
		gbc_btnFileChooser.gridy = 2;
		this.frame.getContentPane().add(this.btnFileChooser, gbc_btnFileChooser);
		this.btnFileChooser.addActionListener(this.fileChooserActionListener);

		this.btnParseFolder = new JButton("Reiniciar \u00E1rbol");
		GridBagConstraints gbc_btnParseFolder = new GridBagConstraints();
		gbc_btnParseFolder.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnParseFolder.insets = new Insets(0, 0, 5, 5);
		gbc_btnParseFolder.gridx = 0;
		gbc_btnParseFolder.gridy = 3;
		this.frame.getContentPane().add(this.btnParseFolder, gbc_btnParseFolder);
		this.btnParseFolder.addActionListener(this.parseTreeActionListener);

		this.btnCopyText = new JButton("Copiar texto");
		GridBagConstraints gbc_btnCopyText = new GridBagConstraints();
		gbc_btnCopyText.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnCopyText.insets = new Insets(0, 0, 5, 5);
		gbc_btnCopyText.gridx = 0;
		gbc_btnCopyText.gridy = 4;
		this.btnCopyText.addActionListener(this.copyActionListener);
		this.frame.getContentPane().add(this.btnCopyText, gbc_btnCopyText);
	}

	public JTextArea getTextArea() {
		return this.textArea;
	}

	public JTextPane getTextPane() {
		return this.textPane;
	}
}

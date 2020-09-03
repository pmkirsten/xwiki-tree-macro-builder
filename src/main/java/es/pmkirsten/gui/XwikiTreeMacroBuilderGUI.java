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
import java.nio.file.Paths;

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

import es.pmkirsten.builder.XwikiTreeMacroPathBuilder;

public class XwikiTreeMacroBuilderGUI {

	protected JFrame frmXwikiTreeMacro;
	protected JFileChooser fileChooser = new JFileChooser();
	protected String path = null;
	protected JTextArea stringTree;
	protected JTextPane textPane;
	protected JButton btnFileChooser;
	protected JButton btnParseFolder;
	protected JButton btnCopyText;
	protected JLabel lblExclusions;
	protected JScrollPane scrollExclusionTextArea;
	protected JTextArea exclusionTextArea;
	protected XwikiTreeMacroPathBuilder builder = new XwikiTreeMacroPathBuilder();
	protected ActionListener fileChooserActionListener = new ActionListener() {

		@Override
		public void actionPerformed(ActionEvent e) {
			String path = XwikiTreeMacroBuilderGUI.this.path == null ? "." : XwikiTreeMacroBuilderGUI.this.path;
			XwikiTreeMacroBuilderGUI.this.fileChooser.setCurrentDirectory(new java.io.File(path));
			XwikiTreeMacroBuilderGUI.this.fileChooser.setDialogTitle("Seleccionar carpeta");
			XwikiTreeMacroBuilderGUI.this.fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			XwikiTreeMacroBuilderGUI.this.fileChooser.setAcceptAllFileFilterUsed(false);

			if (XwikiTreeMacroBuilderGUI.this.fileChooser.showOpenDialog(XwikiTreeMacroBuilderGUI.this.frmXwikiTreeMacro) == JFileChooser.APPROVE_OPTION) {
				XwikiTreeMacroBuilderGUI.this.path = XwikiTreeMacroBuilderGUI.this.fileChooser.getSelectedFile().getAbsolutePath();
				XwikiTreeMacroBuilderGUI.this.textPane.setText(XwikiTreeMacroBuilderGUI.this.path);
				XwikiTreeMacroBuilderGUI.this.builder.getIgnoreExtensions().clear();
				XwikiTreeMacroBuilderGUI.this.builder.getIgnoreElements().clear();
				XwikiTreeMacroBuilderGUI.this.builder.getIgnoreElements().add(".git");
				XwikiTreeMacroBuilderGUI.this.builder.checkGitignores(Paths.get(XwikiTreeMacroBuilderGUI.this.path));
				StringBuilder builder = new StringBuilder();
				for (String ignore : XwikiTreeMacroBuilderGUI.this.builder.getIgnoreElements()) {
					builder.append(ignore + "\n");
				}
				XwikiTreeMacroBuilderGUI.this.exclusionTextArea.setText(builder.toString());
				XwikiTreeMacroBuilderGUI.this.btnParseFolder.doClick();

			} else {
				System.out.println("No Selection ");
			}
		}

	};

	protected ActionListener parseTreeActionListener = new ActionListener() {

		@Override
		public void actionPerformed(ActionEvent e) {
			XwikiTreeMacroBuilderGUI.this.builder.getIgnoreExtensions().clear();
			XwikiTreeMacroBuilderGUI.this.builder.getIgnoreElements().clear();
			for (String line : XwikiTreeMacroBuilderGUI.this.exclusionTextArea.getText().split("\\n")) {
				XwikiTreeMacroBuilderGUI.this.builder.getIgnoreElements().add(line);

				if (line.startsWith("*")) {
					XwikiTreeMacroBuilderGUI.this.builder.getIgnoreExtensions().add(line.substring(1));
				}

			}
			String stringTree = XwikiTreeMacroBuilderGUI.this.builder.walk(XwikiTreeMacroBuilderGUI.this.path);
			XwikiTreeMacroBuilderGUI.this.stringTree.setText(stringTree);
		}
	};

	protected ActionListener copyActionListener = new ActionListener() {

		@Override
		public void actionPerformed(ActionEvent e) {
			StringSelection stringSelection = new StringSelection(XwikiTreeMacroBuilderGUI.this.stringTree.getText());
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
					XwikiTreeMacroBuilderGUI window = new XwikiTreeMacroBuilderGUI();
					window.frmXwikiTreeMacro.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public XwikiTreeMacroBuilderGUI() {
		this.initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		this.frmXwikiTreeMacro = new JFrame();
		this.frmXwikiTreeMacro.setTitle("XWiki Tree Macro Builder");
		this.frmXwikiTreeMacro.setBounds(100, 100, 850, 500);
		this.frmXwikiTreeMacro.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[] { 0, 0, 0, 0, 0 };
		gridBagLayout.rowHeights = new int[] { 0, 0, 0, 0, 0, 0, 0 };
		gridBagLayout.columnWeights = new double[] { 0.0, 0.0, 0.0, 1.0, Double.MIN_VALUE };
		gridBagLayout.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, Double.MIN_VALUE };
		this.frmXwikiTreeMacro.getContentPane().setLayout(gridBagLayout);

		JLabel lblCarpeta = new JLabel("Carpeta:");
		lblCarpeta.setHorizontalAlignment(SwingConstants.CENTER);
		GridBagConstraints gbc_lblCarpeta = new GridBagConstraints();
		gbc_lblCarpeta.anchor = GridBagConstraints.WEST;
		gbc_lblCarpeta.insets = new Insets(0, 0, 5, 5);
		gbc_lblCarpeta.gridx = 0;
		gbc_lblCarpeta.gridy = 0;
		this.frmXwikiTreeMacro.getContentPane().add(lblCarpeta, gbc_lblCarpeta);

		JScrollPane scrollStringTree = new JScrollPane();
		GridBagConstraints gbc_scrollStringTree = new GridBagConstraints();
		gbc_scrollStringTree.gridheight = 6;
		gbc_scrollStringTree.fill = GridBagConstraints.BOTH;
		gbc_scrollStringTree.gridx = 3;
		gbc_scrollStringTree.gridy = 0;
		this.frmXwikiTreeMacro.getContentPane().add(scrollStringTree, gbc_scrollStringTree);

		this.stringTree = new JTextArea();
		scrollStringTree.setViewportView(this.stringTree);

		this.textPane = new JTextPane();
		this.textPane.setEditable(false);
		GridBagConstraints gbc_textPane = new GridBagConstraints();
		gbc_textPane.gridwidth = 3;
		gbc_textPane.insets = new Insets(0, 0, 5, 5);
		gbc_textPane.fill = GridBagConstraints.BOTH;
		gbc_textPane.gridx = 0;
		gbc_textPane.gridy = 1;
		this.frmXwikiTreeMacro.getContentPane().add(this.textPane, gbc_textPane);

		this.btnFileChooser = new JButton("Selecciona carpeta...");
		GridBagConstraints gbc_btnFileChooser = new GridBagConstraints();
		gbc_btnFileChooser.anchor = GridBagConstraints.EAST;
		gbc_btnFileChooser.insets = new Insets(0, 0, 5, 5);
		gbc_btnFileChooser.gridx = 2;
		gbc_btnFileChooser.gridy = 2;
		this.frmXwikiTreeMacro.getContentPane().add(this.btnFileChooser, gbc_btnFileChooser);
		this.btnFileChooser.addActionListener(this.fileChooserActionListener);

		this.btnCopyText = new JButton("Copiar \u00E1rbol");
		GridBagConstraints gbc_btnCopyText = new GridBagConstraints();
		gbc_btnCopyText.anchor = GridBagConstraints.EAST;
		gbc_btnCopyText.insets = new Insets(0, 0, 5, 5);
		gbc_btnCopyText.gridx = 2;
		gbc_btnCopyText.gridy = 3;
		this.btnCopyText.addActionListener(this.copyActionListener);
		this.frmXwikiTreeMacro.getContentPane().add(this.btnCopyText, gbc_btnCopyText);

		this.lblExclusions = new JLabel("Exclusiones (incluye contenido .gitignore):");
		GridBagConstraints gbc_lblExclusions = new GridBagConstraints();
		gbc_lblExclusions.gridwidth = 2;
		gbc_lblExclusions.anchor = GridBagConstraints.WEST;
		gbc_lblExclusions.insets = new Insets(0, 0, 5, 5);
		gbc_lblExclusions.gridx = 0;
		gbc_lblExclusions.gridy = 4;
		this.frmXwikiTreeMacro.getContentPane().add(this.lblExclusions, gbc_lblExclusions);

		this.btnParseFolder = new JButton("Reanalizar \u00E1rbol");
		GridBagConstraints gbc_btnParseFolder = new GridBagConstraints();
		gbc_btnParseFolder.anchor = GridBagConstraints.EAST;
		gbc_btnParseFolder.insets = new Insets(0, 0, 5, 5);
		gbc_btnParseFolder.gridx = 2;
		gbc_btnParseFolder.gridy = 4;
		this.frmXwikiTreeMacro.getContentPane().add(this.btnParseFolder, gbc_btnParseFolder);
		this.btnParseFolder.addActionListener(this.parseTreeActionListener);

		this.scrollExclusionTextArea = new JScrollPane();
		GridBagConstraints gbc_scrollExclusionTextArea = new GridBagConstraints();
		gbc_scrollExclusionTextArea.gridwidth = 3;
		gbc_scrollExclusionTextArea.insets = new Insets(0, 0, 0, 5);
		gbc_scrollExclusionTextArea.fill = GridBagConstraints.BOTH;
		gbc_scrollExclusionTextArea.gridx = 0;
		gbc_scrollExclusionTextArea.gridy = 5;
		this.frmXwikiTreeMacro.getContentPane().add(this.scrollExclusionTextArea, gbc_scrollExclusionTextArea);

		this.exclusionTextArea = new JTextArea();
		this.scrollExclusionTextArea.setViewportView(this.exclusionTextArea);
	}

	public JTextArea getTextArea() {
		return this.stringTree;
	}

	public JTextPane getTextPane() {
		return this.textPane;
	}
}
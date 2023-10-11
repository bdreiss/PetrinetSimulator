package view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuBar;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.JToolBar;

import control.MainController;

public class MainFrame extends JFrame {

	public static final double GRAPH_PERCENT = 0.5;
	
	private ResizableSplitPane splitPane;

	private JScrollPane scrollPane;
	private JTextArea textArea;

	private JLabel statusLabel;

	private MainController controller;
	public MainFrame(String title) {
		super(title);
		textArea = new JTextArea();
		textArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
		
		scrollPane = new JScrollPane(textArea);

		splitPane = new ResizableSplitPane(this, JSplitPane.VERTICAL_SPLIT);
		splitPane.setDefaultRatio(0.8);
		splitPane.setRightComponent(scrollPane);

		controller = new MainController(this);

		JMenuBar menuBar = new PetrinetMenu(controller);

		this.setJMenuBar(menuBar);

		JToolBar toolbar = new PetrinetToolbar(controller);

		toolbar.setFloatable(false);

		this.add(toolbar, BorderLayout.NORTH);
		add(splitPane, BorderLayout.CENTER);

		// Erzeuge ein Label, welches als Statuszeile dient, ...
		// ... und zeige dort ein paar hilfreiche Systeminfos an, ...
		statusLabel = new JLabel("java.version = " + System.getProperty("java.version") + "  |  user.dir = "
				+ System.getProperty("user.dir"));
		// ... und füge es zum Haupt-Frame hinzu
		this.add(statusLabel, BorderLayout.SOUTH);

//		f.setSize(400, 240);
//	      f.setLocationRelativeTo(null);
		// bestimme eine geeignete Fenstergröße in Abhängigkeit von der
		// Bildschirmauflösung
		double heightPerc = 0.7; // relative Höhe des Fensters bzgl. der der Bildschirmhöhe (1.0), hier also 60 %
		double aspectRatio = 16.0 / 10.0; // Seitenverhältnis des Fensters
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		int h = (int) (screenSize.height * heightPerc);
		int w = (int) (h * aspectRatio);
		setBounds((screenSize.width - w) / 2, (screenSize.height - h) / 2, w, h);

		// Konfiguriere weitere Parameter des Haupt-Frame
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);
		
	}

	public ResizableSplitPane getSplitPane() {
		return splitPane;
	}
	
	public void print(String s) {
		textArea.append(s);
	}

	public void clearTextArea() {
		textArea.setText("");
	}
	
	public void setStatusLabel(String status) {
		statusLabel.setText(status);
	}

}

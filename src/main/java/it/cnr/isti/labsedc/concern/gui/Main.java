package it.cnr.isti.labsedc.concern.gui;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;

import java.awt.Panel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.Color;
import java.awt.Window.Type;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

import it.cnr.isti.labsedc.concern.ConcernApp;

import java.awt.Font;

import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.awt.FlowLayout;

public class Main {

	private JFrame frmGlimpseReloaded;
	private JTextArea textArea = new JTextArea();
	JProgressBar progressBar = new JProgressBar();
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Main window = new Main();
					window.frmGlimpseReloaded.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 * @throws IOException
	 */
	public Main() throws IOException {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 * @throws IOException
	 */
	private void initialize() throws IOException {
		PrintStream printStream = new PrintStream(new CustomOutputStream(textArea));
		System.setOut(printStream);
		System.setErr(printStream);

		progressBar.setSize(200, 20);
		frmGlimpseReloaded = new JFrame();
		frmGlimpseReloaded.setType(Type.UTILITY);
		frmGlimpseReloaded.setFont(new Font("Verdana", Font.PLAIN, 14));
		frmGlimpseReloaded.getContentPane().setBackground(new Color(62, 120, 202));
		frmGlimpseReloaded.setTitle("Glimpse_Reloaded - Interface");
		frmGlimpseReloaded.setBounds(100, 100, 900	, 620);
		frmGlimpseReloaded.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmGlimpseReloaded.getContentPane().setLayout(null);

		Panel panel = new Panel();
		panel.setBackground(new Color(23, 35, 54));
		panel.setBounds(0, 0, 220, 590);
		frmGlimpseReloaded.getContentPane().add(panel);

		JButton runButton = new JButton("RUN");
		runButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					runButton.setEnabled(false);
					progressBar.setIndeterminate(true);
					ConcernApp.main(null);
					} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}

		});

		BufferedImage logo = ImageIO.read(new File(System.getProperty("user.dir")+ "/src/main/resources/binoculars.png"));
		JLabel image = new JLabel(new ImageIcon(logo));
		panel.add(image);

		BufferedImage components = ImageIO.read(new File(System.getProperty("user.dir")+ "/src/main/resources/algorithm.png"));
		JLabel component = new JLabel(new ImageIcon(components));
		panel.add(component);
		panel.add(progressBar);

		JPanel panel_2 = new JPanel();
		panel_2.setBackground(new Color(23, 35, 54));
		FlowLayout flowLayout = (FlowLayout) panel_2.getLayout();
		flowLayout.setAlignment(FlowLayout.LEADING);
		flowLayout.setVgap(150);
		flowLayout.setHgap(90);
		panel.add(panel_2);
		panel.add(runButton);

		JLabel lblNewLabel = new JLabel("Glimpse - Complex Event Processing Infrastructure");
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setFont(new Font("Verdana", Font.BOLD, 20));
		lblNewLabel.setForeground(new Color(255, 255, 255));
		lblNewLabel.setBounds(189, 0, 705, 50);
		frmGlimpseReloaded.getContentPane().add(lblNewLabel);

		JPanel panel_1 = new JPanel();
		panel_1.setBackground(new Color(113, 168, 255));
		panel_1.setBounds(219, 51, 675, 64);

		BufferedImage broker = ImageIO.read(new File(System.getProperty("user.dir")+ "/src/main/resources/broker.png"));
		JLabel brokerim = new JLabel(new ImageIcon(broker));
		panel_1.add(brokerim);

		BufferedImage notification = ImageIO.read(new File(System.getProperty("user.dir")+ "/src/main/resources/notification.png"));
		JLabel notificationim = new JLabel(new ImageIcon(notification));
		panel_1.add(notificationim);

		BufferedImage web = ImageIO.read(new File(System.getProperty("user.dir")+ "/src/main/resources/web.png"));
		JLabel webim = new JLabel(new ImageIcon(web));
		panel_1.add(webim);

		BufferedImage channel = ImageIO.read(new File(System.getProperty("user.dir")+ "/src/main/resources/channel.png"));
		JLabel channelim = new JLabel(new ImageIcon(channel));
		panel_1.add(channelim);

		BufferedImage storage = ImageIO.read(new File(System.getProperty("user.dir")+ "/src/main/resources/storage.png"));
		JLabel storageim = new JLabel(new ImageIcon(storage));
		panel_1.add(storageim);

		frmGlimpseReloaded.getContentPane().add(panel_1);

		textArea.setForeground(new Color(50, 205, 50));
		textArea.setFont(new Font("Courier 10 Pitch", Font.PLAIN, 12));
		textArea.setBackground(new Color(0, 0, 0));
		textArea.setEditable(false);
		textArea.setBounds(189, 481, 705, 110);
		JScrollPane console = new JScrollPane(textArea, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		console.setSize(675, 160);
		console.setLocation(220, 430);
		frmGlimpseReloaded.getContentPane().add(console);
	}

	class CustomOutputStream extends OutputStream {
	    private JTextArea textArea;

	    public CustomOutputStream(JTextArea textArea) {
	        this.textArea = textArea;
	    }

	    @Override
	    public void write(int b) throws IOException {
	        // redirects data to the text area
	        textArea.append(String.valueOf((char)b));
	        // scrolls the text area to the end of data
	        textArea.setCaretPosition(textArea.getDocument().getLength());
	        // keeps the textArea up to date
	        textArea.update(textArea.getGraphics());
	    }
	}
}

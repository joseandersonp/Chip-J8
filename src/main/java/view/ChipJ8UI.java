package view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.swing.ButtonGroup;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;

import emulator.Chip8;
import emulator.Display;

@SuppressWarnings("serial")
public class ChipJ8UI extends JFrame {

	private JFileChooser fileChooser;
	private JMenuBar menuBar;
	private JMenu fileMenu;
	private JMenuItem openMenuItem;
	private JMenuItem exitMenuItem;
	private JMenu gameMenu;
	private JMenuItem runMenuItem;
	private JMenuItem stopMenuItem;
	private JMenuItem resetMenuItem;
	private JMenu optionsMenu;
	private JMenu speedMenu;
	
	private JMenuItem helpMenu;
	private JMenuItem aboutMenuItem;
	
	private JMenuItem speed05x;
	private JMenuItem speed1x;
	private JMenuItem speed2x;
	private JMenuItem speed3x;
	
	private JMenu videoMenu;
	private JMenuItem video1x;
	private JMenuItem video2x;
	private JMenuItem video3x;
	
	private AboutDialog aboutDialog; 
			
	private Chip8 emulator;
	private DisplayPanel displayPanel;

	public ChipJ8UI() {

		initComponents();
		initListeners();

		emulator = new Chip8();

		displayPanel = new DisplayPanel(emulator.getDisplay(), 8);
		displayPanel.start();
		getContentPane().add(displayPanel);
		addKeyListener(emulator.getKeyboard());

		setTitle("JCHIP-8");
		setDefaultCloseOperation(EXIT_ON_CLOSE);

		pack();
		setVisible(true);

	}

	private void initComponents() {

		menuBar = new JMenuBar();
		getContentPane().add(menuBar, BorderLayout.NORTH);

		fileMenu = new JMenu("File");
		menuBar.add(fileMenu);
		openMenuItem = new JMenuItem("Open");
		exitMenuItem = new JMenuItem("Exit");
		fileMenu.add(openMenuItem);
		fileMenu.addSeparator();
		fileMenu.add(exitMenuItem);

		gameMenu = new JMenu("Game");
		menuBar.add(gameMenu);

		runMenuItem = new JMenuItem("Run");
		stopMenuItem = new JMenuItem("Stop");
		resetMenuItem = new JMenuItem("Reset");
		gameMenu.add(runMenuItem);
		gameMenu.add(stopMenuItem);
		gameMenu.add(resetMenuItem);
		
		runMenuItem.setEnabled(false);
		stopMenuItem.setEnabled(false);
		resetMenuItem.setEnabled(false);

		optionsMenu = new JMenu("Options");
		menuBar.add(optionsMenu);

		speedMenu = new JMenu("Speed");
		ButtonGroup speedGroup = new ButtonGroup();
		optionsMenu.add(speedMenu);
		speed05x = new JCheckBoxMenuItem("1/2x");
		speed1x = new JCheckBoxMenuItem("1x");
		speed2x = new JCheckBoxMenuItem("2x");
		speed3x = new JCheckBoxMenuItem("3x");
		
		speedMenu.add(speed05x);
		speedGroup.add(speed05x);
		speedMenu.add(speed1x);
		speedGroup.add(speed1x);
		speedMenu.add(speed2x);
		speedGroup.add(speed2x);
		speedMenu.add(speed3x);
		speedGroup.add(speed3x);
		
		speed1x.setSelected(true);

		videoMenu = new JMenu("Video Size");
		ButtonGroup videoGroup = new ButtonGroup();
		optionsMenu.add(videoMenu);
		video1x = new JCheckBoxMenuItem("1x");
		video2x = new JCheckBoxMenuItem("2x");
		video3x = new JCheckBoxMenuItem("3x");
		videoMenu.add(video1x);
		videoGroup.add(video1x);
		videoMenu.add(video2x);
		videoGroup.add(video2x);
		videoMenu.add(video3x);
		videoGroup.add(video3x);
		video2x.setSelected(true);
		
		helpMenu = new JMenu("help");
		menuBar.add(helpMenu);
		aboutMenuItem = new JMenuItem("About");
		helpMenu.add(aboutMenuItem);

		fileChooser = new JFileChooser("./games");
		fileChooser.setDialogTitle("Open Rom");
		
		aboutDialog = new AboutDialog();

	}

	private void initListeners() {

		openMenuItem.addActionListener(e -> {
			openRom();
		});

		stopMenuItem.addActionListener(e -> {
			emulator.stop();
			runMenuItem.setEnabled(true);
			resetMenuItem.setEnabled(false);
			stopMenuItem.setEnabled(false);
		});

		runMenuItem.addActionListener(e -> {
			emulator.start();
			runMenuItem.setEnabled(false);
			resetMenuItem.setEnabled(true);
			stopMenuItem.setEnabled(true);
		});

		resetMenuItem.addActionListener(e -> {
			emulator.stop();
			emulator.start();
		});
		
		speed05x.addActionListener(e -> {
			emulator.setSpeed(0.5f);
			
		});
		
		speed1x.addActionListener(e -> {
			emulator.setSpeed(1);
			
		});
		
		speed2x.addActionListener(e -> {
			emulator.setSpeed(2);
			
		});
		speed3x.addActionListener(e -> {
			emulator.setSpeed(3);
			
		});

		video1x.addActionListener(e -> {
			displayPanel.setScale(4);
			pack();
		});
		video2x.addActionListener(e -> {
			displayPanel.setScale(8);
			pack();
		});
		video3x.addActionListener(e -> {
			displayPanel.setScale(16);
			pack();
		});
		
		aboutMenuItem.addActionListener(e -> {
			aboutDialog.setVisible(true);
		});

		exitMenuItem.addActionListener(e -> {
			System.exit(0);
		});
	}

	public void openRom() {
		int option = fileChooser.showOpenDialog(this);
		if (option == JFileChooser.APPROVE_OPTION) {
			try {
				emulator.stop();
				emulator.loadRom(fileChooser.getSelectedFile());
				emulator.start();

				runMenuItem.setEnabled(false);
				resetMenuItem.setEnabled(true);
				stopMenuItem.setEnabled(true);

			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}

@SuppressWarnings("serial")
class DisplayPanel extends JPanel {

	private int scale;
	private Display display;
	private BufferedImage image;
	private Dimension dimension;

	public DisplayPanel(Display display, int scale) {
		this.display = display;
		setScale(scale);

		image = new BufferedImage(display.getCols(), display.getRows(), BufferedImage.TYPE_INT_RGB);
	}

	public void start() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				while (true) {
					render();
				}
			}
		}).start();
	}

	public void render() {

		int i = 0;
		int x = 0;
		int y = 0;
		while (i < display.getPixels().length) {

			image.setRGB(x, y, 0xFFFFFF * display.getPixels()[i]);

			i++;
			x++;
			if (i % 64f == 0) {
				x = 0;
				y++;
			}
		}

		this.repaint();
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.drawImage(image, 0, 0, getWidth(), getHeight(), null);
	}

	public int getScale() {
		return scale;
	}

	public void setScale(int scale) {
		this.scale = scale;
		dimension = new Dimension(scale * display.getCols(), scale * display.getRows());
		setPreferredSize(dimension);
	}

	public Display getDisplay() {
		return display;
	}
}
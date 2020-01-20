package etf.dotsandboxes.tn160392d;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

@SuppressWarnings("serial")
public class Main extends JPanel {

	// Game parameters
	private static int m;
	private static int n;
	private static Player redPlayer;
	private static Player bluePlayer;
	private static boolean start;
	private static int depth;
	private static boolean readFromFile;
	private static boolean enStepByStep;

	// Gui components
	private JComboBox<String> redPlayerSelector;
	private JComboBox<String> bluePlayerSelector;
	private JLabel DotsAndBox;
	private JButton startBtn;
	private JLabel blueLabel;
	private JLabel redLabel;
	private JCheckBox loadFile;
	private JCheckBox stepByStep;
	private JLabel depthLabel;
	private JComboBox<Integer> treeDepth;
	private JLabel widthLabel;
	private JLabel heightLabel;
	private JComboBox<Integer> tWidth;
	private JComboBox<Integer> tHeight;

	private Player getSolver(int level) {
		if (level == 1)
			return new EasyComputer();
		else if (level == 2)
			return new MediumComputer();
		else if (level == 3)
			return new HardComputer();
		else
			return null;
	}

	private ActionListener btnListener = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent actionEvent) {
			int rIndex = redPlayerSelector.getSelectedIndex();
			int bIndex = bluePlayerSelector.getSelectedIndex();

			n = tWidth.getSelectedIndex() + 2;
			m = tHeight.getSelectedIndex() + 2;
			depth = treeDepth.getSelectedIndex() + 1;

			redPlayer = getSolver(rIndex);
			bluePlayer = getSolver(bIndex);

			readFromFile = loadFile.isSelected();
			enStepByStep = stepByStep.isSelected();

			start = true;
		}
	};

	public Main() {
	
		// construct preComponents
		String[] players = { "Human", "Beginner", "Advanced", "Competative" };
		Integer[] treeDepthItems = { 1, 2, 3, 4, 5, 6, 7, 8 };
		Integer[] tWidthItems = { 2, 3, 4, 5, 6, 7, 8, 9, 10 };
		Integer[] tHeightItems = { 2, 3, 4, 5, 6, 7, 8, 9, 10 };

		// construct components
		redPlayerSelector = new JComboBox<String>(players);
		bluePlayerSelector = new JComboBox<String>(players);
		DotsAndBox = new JLabel("Dots and Boxes");
		startBtn = new JButton("Start");
		blueLabel = new JLabel("Blue Player :");
		redLabel = new JLabel("Red Player :");
		loadFile = new JCheckBox("Load moves from file");
		stepByStep = new JCheckBox("Enable step by step");
		depthLabel = new JLabel("Tree depth if used medium or hard opponent:");
		treeDepth = new JComboBox<Integer>(treeDepthItems);
		widthLabel = new JLabel("Table width :");
		heightLabel = new JLabel("Table height :");
		tWidth = new JComboBox<Integer>(tWidthItems);
		tHeight = new JComboBox<Integer>(tHeightItems);

		// adjust size and set layout
		setPreferredSize(new Dimension(448, 379));
		setLayout(null);

		// add components
		add(redPlayerSelector);
		add(bluePlayerSelector);
		add(DotsAndBox);
		add(startBtn);
		add(blueLabel);
		add(redLabel);
		add(loadFile);
		add(stepByStep);
		add(depthLabel);
		add(treeDepth);
		add(widthLabel);
		add(heightLabel);
		add(tWidth);
		add(tHeight);

		// set component bounds (only needed by Absolute Positioning)
		redPlayerSelector.setBounds(50, 140, 150, 25);
		bluePlayerSelector.setBounds(260, 140, 150, 25);
		DotsAndBox.setBounds(170, 10, 200, 40);
		startBtn.setBounds(80, 310, 310, 65);
		blueLabel.setBounds(260, 120, 100, 25);
		redLabel.setBounds(50, 120, 100, 25);
		loadFile.setBounds(100, 250, 250, 30);
		stepByStep.setBounds(100, 280, 250, 30);
		depthLabel.setBounds(50, 180, 340, 30);
		treeDepth.setBounds(150, 210, 100, 25);
		widthLabel.setBounds(50, 60, 100, 25);
		heightLabel.setBounds(260, 60, 100, 25);
		tWidth.setBounds(50, 80, 100, 25);
		tHeight.setBounds(260, 80, 100, 25);

		startBtn.addActionListener(btnListener);

		start = false;
	}

	public static void main(String[] args) {
		
		JFrame frameStart = new JFrame("The Game");
		frameStart.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frameStart.getContentPane().add(new Main());
		frameStart.pack();
		frameStart.setLocationRelativeTo(null);
		frameStart.setVisible(true);

		while (!start) {
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		new Game(frameStart, m, n, redPlayer, bluePlayer, depth, readFromFile, enStepByStep);
	}

}
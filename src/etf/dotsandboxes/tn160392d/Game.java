package etf.dotsandboxes.tn160392d;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;

public class Game {

	// Used for table dimension
	private final static int size = 10;
	private final static int dist = 40;

	// Main game parts
	private int n, m;
	private Board board;
	private int turn;
	private int depth;
	private Player onTurn, redPlayer, bluePlayer;
	
	// IO parts
	private boolean readFile;
	private PrintWriter writer = null;
	private File file = null;
	private Scanner sc = null;

	// GUI parts
	private JFrame jf;
	private JLabel redScoreLabel, blueScoreLabel, statusLabel;
	private JLabel[][] hLines, vLines, box;
	private boolean[][] isSetHLines, isSetVLines;
	private boolean mouseEnabled;
	
	// Step by step mode parts
	private boolean stepByStep;
	private boolean goNextStep;


	public Game(JFrame frame, int m, int n, Player redPlayer, Player bluePlayer, int depth, boolean readFile,
			boolean stepByStep) {
		this.jf = frame;
		jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.m = m;
		this.n = n;
		this.redPlayer = redPlayer;
		this.bluePlayer = bluePlayer;
		this.depth = depth;
		this.readFile = readFile;
		this.file = null;
		this.sc = null;
		if (redPlayer != null && bluePlayer != null)
			this.stepByStep = stepByStep;
		else
			this.stepByStep = false;
		this.goNextStep = false;

		startGame();
	}

	private void startGame() {

		if (readFile)
			startReadFromFile();
		
		// Opens file and writes board dimensions in number of dots - m x n
		startWriteToFile();

		board = new Board(m, n);
		turn = Board.RED;
		onTurn = redPlayer;
		

		JPanel mainPanel = new JPanel(new BorderLayout());
		
		// Score panel - contains player names and theri scores
		JPanel score = new JPanel(new GridLayout(2, 2));
		
		score.add(new JLabel("Red score:"));
		score.add(new JLabel("Blue score:"));
		
		redScoreLabel = new JLabel("0");
		redScoreLabel.setForeground(Color.RED);
		blueScoreLabel = new JLabel("0");
		blueScoreLabel.setForeground(Color.BLUE);
		
		score.add(redScoreLabel);
		score.add(blueScoreLabel);
		
		mainPanel.add(score, BorderLayout.NORTH);

		// Status panel - contains end button, status of game and possible on more button
		JPanel statusPanel = new JPanel(new BorderLayout());
		
		// Adding status label
		statusLabel = new JLabel("Player-1's Turn...");
		statusLabel.setForeground(Color.RED);
		statusPanel.add(statusLabel, BorderLayout.NORTH);
		
		// Adding end and save moves button
		JButton endButton = new JButton("End and save moves to file");
		endButton.addActionListener(e -> {
			writer.close();
			System.exit(0);
		});
		statusPanel.add(endButton, BorderLayout.SOUTH);

		mainPanel.add(statusPanel, BorderLayout.SOUTH);
		
		
		// Creating additional components for step by step mode
		if (stepByStep) {
			// Creating button for next move
			JButton nextBtn = new JButton("Next");
			nextBtn.addActionListener(e -> {
				goNextStep = true;
			});
			statusPanel.add(nextBtn, BorderLayout.CENTER);
			
			// Creating dialog for displaying heuristics
			JDialog heuristicsDialog = new JDialog(jf, "Heuristics for previous move");
			JTextArea heuristicsDisplay = new JTextArea(5, 20);
			
			redPlayer.setTextArea(heuristicsDisplay);
			bluePlayer.setTextArea(heuristicsDisplay);
			
			heuristicsDialog.add(heuristicsDisplay);
			heuristicsDialog.setSize(300,400);
			heuristicsDialog.setLocation(800,500);
			heuristicsDialog.setVisible(true);
			
		}

		// Grid - panel for game board
		JPanel grid = new JPanel(new GridBagLayout());

		GridBagConstraints constraints = new GridBagConstraints();
		constraints.gridx = 0;
		constraints.gridy = 0;
		grid.add(getEmptyLabel(new Dimension(2 * (n * size + (n - 1) * dist), 10)), constraints);

		hLines = new JLabel[m][n - 1];
		isSetHLines = new boolean[m][n - 1];

		vLines = new JLabel[m - 1][n];
		isSetVLines = new boolean[m - 1][n];

		box = new JLabel[m - 1][n - 1];

		// Creating of game board
		for (int i = 0; i < (2 * m - 1); i++) {
			JPanel pane = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
			if (i % 2 == 0) {
				pane.add(newDot());
				for (int j = 0; j < (n - 1); j++) {
					hLines[i / 2][j] = newHorizontalLine();
					pane.add(hLines[i / 2][j]);
					pane.add(newDot());
				}
			} else {
				for (int j = 0; j < (n - 1); j++) {
					vLines[i / 2][j] = newVerticalLine();
					pane.add(vLines[i / 2][j]);
					box[i / 2][j] = newBox();
					pane.add(box[i / 2][j]);
				}
				vLines[i / 2][n - 1] = newVerticalLine();
				pane.add(vLines[i / 2][n - 1]);
			}
			++constraints.gridy;
			grid.add(pane, constraints);
		}

		++constraints.gridy;
		grid.add(getEmptyLabel(new Dimension(2 * (n * size + (n - 1) * dist), 10)), constraints);
		
		// Add game board to mainPanel
		mainPanel.add(grid, BorderLayout.CENTER);

		jf.getContentPane().removeAll();
		jf.revalidate();
		jf.repaint();

		jf.setContentPane(mainPanel);
		jf.pack();

		jf.setLocation(500,500);
		jf.setVisible(true);

		if (readFile) {
			// Reads moves and executes them
			readFromFile();
		}

		// Function that controls who will play next
		gameControl();

		closeOpenedFiles();
		
	}

	private void gameControl() {
		
		while (!board.isComplete()) {
			
			if (onTurn == null) { // Human player on turn
				mouseEnabled = true;
			} else { // Computer player on turn
				mouseEnabled = false;
				// Step by step mode - wait for next button to be clicked
				if (stepByStep) {
					while (!goNextStep)
						try {
							Thread.sleep(100);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					goNextStep = false;
				}
				processMove(onTurn.getNextMove(board, turn, depth));
			}
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	
	private void processMove(Line source) {
		
		int x = source.getX(), y = source.getY();
		ArrayList<Point> ret;

		// Used for setting additional comments in output file
		int beforeScore = turn == Board.RED ? board.getRedScore() : board.getBlueScore();

		if (source.isHorizontal()) {
			if (isSetHLines[x][y])
				return; // Line already set
			ret = board.setHLine(x, y, turn); // Set the line on board
			hLines[x][y].setBackground(Color.BLACK);
			isSetHLines[x][y] = true;
		} else {
			if (isSetVLines[x][y])
				return; // Line already set
			ret = board.setVLine(x, y, turn); // Set the line on board
			vLines[x][y].setBackground(Color.BLACK);
			isSetVLines[x][y] = true;
		}

		// Setting up color if the box is closed
		for (Point p : ret)
			box[p.x][p.y].setBackground((turn == Board.RED) ? Color.red : Color.BLUE);

		redScoreLabel.setText(String.valueOf(board.getRedScore()));
		blueScoreLabel.setText(String.valueOf(board.getBlueScore()));

		// Used for setting additional comments in output file
		int afterScore = turn == Board.RED ? board.getRedScore() : board.getBlueScore();
		if (beforeScore < afterScore)
			writer.println(convertTo(source, true)); // writing to file with comment that the player closed the box
		else
			writer.println(convertTo(source, false)); // writing to file with regular comment

		if (board.isComplete()) {
			int winner = board.getWinner();
			if (winner == Board.RED) {
				statusLabel.setText("Red player is the winner!");
				statusLabel.setForeground(Color.RED);
			} else if (winner == Board.BLUE) {
				statusLabel.setText("Blue player is the winner!");
				statusLabel.setForeground(Color.BLUE);
			} else {
				statusLabel.setText("Game Tied!");
				statusLabel.setForeground(Color.BLACK);
			}
		}

		// If we didn't closed any box, change the player
		if (ret.isEmpty()) {
			if (turn == 0) {
				turn = 1;
				onTurn = bluePlayer;
				statusLabel.setText("Player-2's Turn...");
				statusLabel.setForeground(Color.BLUE);
			} else {
				turn = 0;
				onTurn = redPlayer;
				statusLabel.setText("Player-1's Turn...");
				statusLabel.setForeground(Color.RED);
			}
		}
	}

	// Opens file and reads board dimensions
	private void startReadFromFile() {
		file = new File("input.txt");
		try {
			sc = new Scanner(file);

			if (sc.hasNextLine()) {
				String data = sc.nextLine();
				m = Character.getNumericValue(data.charAt(0));
				n = Character.getNumericValue(data.charAt(2));
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	// Read from file and execute moves
	private void readFromFile() {
		String data = null;
		while (sc.hasNextLine()) {
			data = sc.nextLine();
			data = data.substring(0, 2);
			processMove(convertFrom(data));
		}
	}
	
	// Open file and write board dimensions in number of dots
	private void startWriteToFile() {
		try {
			writer = new PrintWriter("output.txt", "UTF-8");
			writer.println((m) + " " + (n));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	// Close files opened for reading and writing
	private void closeOpenedFiles() {
		if(writer != null)
			writer.close();
		if(sc != null)
			sc.close();
	}
	
	// Method that convert line to output format move
	private String convertTo(Line line, Boolean squareFormed) {
		StringBuilder sb = new StringBuilder();
		if (line.isHorizontal()) {
			sb.append(line.getX());
			sb.append((char) ('A' + line.getY()));
			sb.append(" //");
			if (turn == 0)
				sb.append("Prvi igrac povlaci liniju");
			else
				sb.append("Drugi igrac povlaci liniju");
			if (squareFormed) {
				sb.append(", formira kvadrat i igra ponovo");
			}
		} else {
			sb.append((char) ('A' + line.getX()));
			sb.append(line.getY());
			sb.append(" //");
			if (turn == 0)
				sb.append("Prvi igrac povlaci liniju");
			else
				sb.append("Drugi igrac povlaci liniju");
			if (squareFormed) {
				sb.append(", formira kvadrat i igra ponovo");
			}
		}

		return sb.toString();
	}

	// Method that converts input format move to line
	private Line convertFrom(String data) {
		int x = 0, y = 0;
		boolean horizontal = false;

		if (Character.isAlphabetic(data.charAt(0))) {
			x = Character.getNumericValue(data.charAt(0)) - Character.getNumericValue('A');
			y = Character.getNumericValue(data.charAt(1));
		} else {
			horizontal = true;
			x = Character.getNumericValue(data.charAt(0));
			y = Character.getNumericValue(data.charAt(1)) - Character.getNumericValue('A');
		}
		return new Line(x, y, horizontal);
	}

	// Methods that make GUI components for board
	
	private JLabel getEmptyLabel(Dimension d) {
		JLabel label = new JLabel();
		label.setPreferredSize(d);
		return label;
	}

	
	private JLabel newDot() {
		JLabel label = new JLabel();
		label.setPreferredSize(new Dimension(size, size));
		label.setBackground(Color.BLACK);
		label.setOpaque(true);
		return label;
	}

	
	private JLabel newBox() {
		JLabel label = new JLabel();
		label.setPreferredSize(new Dimension(dist, dist));
		label.setOpaque(true);
		return label;
	}

	
	private JLabel newHorizontalLine() {
		JLabel label = new JLabel();
		label.setPreferredSize(new Dimension(dist, size));
		label.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		label.setOpaque(true);
		label.addMouseListener(mouseListener);
		return label;
	}

	
	private JLabel newVerticalLine() {
		JLabel label = new JLabel();
		label.setPreferredSize(new Dimension(size, dist));
		label.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		label.setOpaque(true);
		label.addMouseListener(mouseListener);
		return label;
	}

	
	// Processing mouse movement and clicking
	private MouseListener mouseListener = new MouseListener() {
		
		@Override
		public void mouseClicked(MouseEvent mouseEvent) {
			if (!mouseEnabled)
				return;
			processMove(getSource(mouseEvent.getSource()));
		}

		@Override
		public void mouseEntered(MouseEvent mouseEvent) {
			if (!mouseEnabled)
				return;
			Line location = getSource(mouseEvent.getSource());
			int x = location.getX(), y = location.getY();
			if (location.isHorizontal()) {
				if (isSetHLines[x][y])
					return;
				hLines[x][y].setBackground((turn == Board.RED) ? Color.RED : Color.BLUE);
			} else {
				if (isSetVLines[x][y])
					return;
				vLines[x][y].setBackground((turn == Board.RED) ? Color.RED : Color.BLUE);
			}
		}

		@Override
		public void mouseExited(MouseEvent mouseEvent) {
			if (!mouseEnabled)
				return;
			Line location = getSource(mouseEvent.getSource());
			int x = location.getX(), y = location.getY();
			if (location.isHorizontal()) {
				if (isSetHLines[x][y])
					return;
				hLines[x][y].setBackground(Color.WHITE);
			} else {
				if (isSetVLines[x][y])
					return;
				vLines[x][y].setBackground(Color.WHITE);
			}
		}

		@Override
		public void mousePressed(MouseEvent arg0) {

		}

		@Override
		public void mouseReleased(MouseEvent arg0) {
			
		}
	};

	private Line getSource(Object object) {
		for (int i = 0; i < m; i++)
			for (int j = 0; j < (n - 1); j++)
				if (hLines[i][j] == object)
					return new Line(i, j, true);
		for (int i = 0; i < (m - 1); i++)
			for (int j = 0; j < n; j++)
				if (vLines[i][j] == object)
					return new Line(i, j, false);

		return new Line();
	}

}

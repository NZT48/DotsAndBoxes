package dotbox;

import java.awt.*;
import javax.swing.*;


import java.awt.event.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;




public class Game {
	
	//Used for table dimension
    private final static int size = 10;
    private final static int dist = 40;
	
	private int n, m;
	private Board board;
	private int turn;
	private int depth;
	
	private boolean readFile;
	private PrintWriter writer;
	private File file = null;
	private Scanner sc = null;
	
	//Gui parts
	private JFrame jf;
	private JLabel redScoreLabel, blueScoreLabel, statusLabel;
	private JLabel[][] hLines, vLines, box;
	private boolean[][] isSetHLines, isSetVLines;

	
	//for mouse
	private boolean mouseEnabled;
	private boolean stepByStep;
	private boolean goNextStep;
	
	//for computer
	private Player onTurn, redPlayer, bluePlayer;
	
	
 	public Game(JFrame frame, int m, int n, Player redSolver, Player blueSolver, int depth, boolean readFile, boolean stepByStep) {
		this.jf = frame;
		jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.m = m;
		this.n = n;
		this.redPlayer = redSolver;
		this.bluePlayer = blueSolver;
		this.depth = depth;
		this.readFile = readFile;
		this.file = null;
		this.sc = null;
		
		if(redPlayer != null && bluePlayer != null)
			this.stepByStep = stepByStep;
		else this.stepByStep = false;
		this.goNextStep = false;
		
		startGame();
	}
	
	private void startGame() {
		
		if(readFile) {
			file = new File("input.txt"); 
			try {
				sc = new Scanner(file);
				
				if(sc.hasNextLine()) {
					String data = sc.nextLine();
					//System.out.println(data + " "+ data.charAt(0) + "  "+ data.charAt(2));
					m = Character.getNumericValue(data.charAt(0));
					n = Character.getNumericValue(data.charAt(2));
					
				}
				
			} catch (FileNotFoundException e1) {
				e1.printStackTrace();
			} 
			
		}
		
		board = new Board(m,n);
		int boardWidth = n * size + (n-1) *dist;
		turn = Board.RED;
		onTurn = redPlayer;
		
		try {
			writer = new PrintWriter("output.txt", "UTF-8");
			writer.println((m) + " " + (n));
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		
		
		//Glavni panel na koji se sve nadodaje
		JPanel mainPanel = new JPanel(new BorderLayout());
		
		// Ubacivanje labela sa rezultatima
		JPanel score = new JPanel(new GridLayout(2,2));
		score.add(new JLabel("P1 score"));
		score.add(new JLabel("P2 score"));
		redScoreLabel = new JLabel("0");
		redScoreLabel.setForeground(Color.RED);
		blueScoreLabel = new JLabel("0");
		blueScoreLabel.setForeground(Color.BLUE);
		score.add(redScoreLabel);
		score.add(blueScoreLabel);
		mainPanel.add(score, BorderLayout.NORTH);
		
		// Panel sa statusom i dugmetom za izlazak
		JPanel bottomPanel = new JPanel(new BorderLayout());
		
		//Ubacivanje labele za status
		statusLabel = new JLabel("Player-1's Turn...");
        statusLabel.setForeground(Color.RED);
        bottomPanel.add(statusLabel, BorderLayout.NORTH);

        
     // Ubacivanej dugmeta za step by step
        if(stepByStep) {
     		JButton nextBtn = new JButton("Next");
     		nextBtn.addActionListener(e -> {goNextStep = true;});
     		bottomPanel.add(nextBtn, BorderLayout.CENTER);
        }
        // Ubacivanej dugmeta za izlaz
		JButton endButton = new JButton("End");
		endButton.addActionListener(e  -> {writer.close(); System.exit(0);});
		bottomPanel.add(endButton, BorderLayout.SOUTH);
		
		mainPanel.add(bottomPanel, BorderLayout.SOUTH);

		
		 JPanel grid = new JPanel(new GridBagLayout());
		 
		 
		 
		 // ----------------------------------------------------------
		 
		 GridBagConstraints constraints = new GridBagConstraints();
	     constraints.gridx = 0;
	     constraints.gridy = 0;
	     grid.add(getEmptyLabel(new Dimension(2 * boardWidth, 10)), constraints);
		 
		 
		 // ----------------------------------------------------------

		 //mainPanel.add(grid, BorderLayout.CENTER);
		
		
		
		hLines = new JLabel[m][n-1];
		isSetHLines = new boolean[m][n-1];
		
		vLines = new JLabel[m-1][n];
		isSetVLines = new boolean[m-1][n];
		
		box = new JLabel[m-1][n-1];
		
		
		for(int i=0; i<(2 * m -1); i++) {
            JPanel pane = new JPanel(new FlowLayout(FlowLayout.CENTER,0,0));
            if(i%2==0) {
                pane.add(newDot());
                for(int j=0; j<(n-1); j++) {
                    hLines[i/2][j] = newHorizontalLine();
                    pane.add(hLines[i/2][j]);
                    pane.add(newDot());
                }
            }
            else {
                for(int j=0; j<(n-1); j++) {
                    vLines[i/2][j] = newVerticalLine();
                    pane.add(vLines[i/2][j]);
                    box[i/2][j] = newBox();
                    pane.add(box[i/2][j]);
                }
                vLines[i/2][n-1] = newVerticalLine();
                pane.add(vLines[i/2][n-1]);
            } 
            ++constraints.gridy;
            grid.add(pane, constraints);
        }

        ++constraints.gridy;
        grid.add(getEmptyLabel(new Dimension(2 * boardWidth, 10)), constraints);
	
        
        grid.add(mainPanel);
        
        jf.getContentPane().removeAll();
        jf.revalidate();
        jf.repaint();
        
        jf.setContentPane(grid);
        jf.pack();
        
        //jf.add(mainPanel);
        
        
		//jf.setSize();
		jf.setLocationRelativeTo(null);
		jf.setVisible(true);
		
		
		if(readFile) {
			String data = null;
			while(sc.hasNextLine()) {
				data = sc.nextLine();
				data = data.substring(0, 2);
	            processMove(convertFrom(data));
			}
		}

		manageGame();
		
		writer.close();
		
	}
	

	
	private Line convertFrom(String data) {
		int x = 0, y = 0;
		boolean horizontal = false;

		if(Character.isAlphabetic(data.charAt(0))) {
			x = Character.getNumericValue(data.charAt(0)) - Character.getNumericValue('A');
			y =  Character.getNumericValue(data.charAt(1));
		}else {
			horizontal = true;
			x = Character.getNumericValue(data.charAt(0));
			y =  Character.getNumericValue(data.charAt(1)) - Character.getNumericValue('A');
		}
		//System.out.println("\n The value of x and y is : " + xx + " " +yy);
		return new Line(x , y, horizontal);
	}

	
	/* Pravljenje manjih grafickih komponenti */

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
	
	/* Obrada misa */
	
	private MouseListener mouseListener = new MouseListener() {
        @Override
        public void mouseClicked(MouseEvent mouseEvent) {
            if(!mouseEnabled) return;
            processMove(getSource(mouseEvent.getSource()));
        }

        @Override
        public void mouseEntered(MouseEvent mouseEvent) {
            if(!mouseEnabled) return;
            Line location = getSource(mouseEvent.getSource());
            int x=location.getX(), y=location.getY();
            if(location.isHorizontal()) {
                if(isSetHLines[x][y]) return;
                hLines[x][y].setBackground((turn == Board.RED) ? Color.RED : Color.BLUE);
            }
            else {
                if(isSetVLines[x][y]) return;
                vLines[x][y].setBackground((turn == Board.RED) ? Color.RED : Color.BLUE);
            }
        }

        @Override
        public void mouseExited(MouseEvent mouseEvent) {
            if(!mouseEnabled) return;
            Line location = getSource(mouseEvent.getSource());
            int x=location.getX(), y=location.getY();
            if(location.isHorizontal()) {
                if(isSetHLines[x][y]) return;
                hLines[x][y].setBackground(Color.WHITE);
            }
            else {
                if(isSetVLines[x][y]) return;
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
        for(int i=0; i< m; i++)
            for(int j=0; j<(n-1); j++)
                if(hLines[i][j] == object)
                    return new Line(i,j,true);
        for(int i=0; i<(m-1); i++)
            for(int j=0; j<n; j++)
                if(vLines[i][j] == object)
                    return new Line(i,j,false);
     
        
        return new Line();
    }
    

    
    private String convertTo(Line line, Boolean squareFormed) {
    	StringBuilder sb = new StringBuilder();
    	if(line.isHorizontal()) {
    		sb.append(line.getX());
    		sb.append((char)('A' + line.getY()));
    		sb.append(" //");
    		if(turn == 0)
    			sb.append("Prvi igrac povlaci liniju");
    		else
    			sb.append("Drugi igrac povlaci liniju");
    		if(squareFormed) {
    			sb.append(", formira kvadrat i igra ponovo");
    		}
    	} else {
    		sb.append((char)('A' + line.getX()));
    		sb.append(line.getY());
    		sb.append(" //");
    		if(turn == 0)
    			sb.append("Prvi igrac povlaci liniju");
    		else
    			sb.append("Drugi igrac povlaci liniju");
    		if(squareFormed) {
    			sb.append(", formira kvadrat i igra ponovo");
    		}
    	}
    	
		return sb.toString();
	}
	
    private void processMove(Line source) {
		int x = source.getX(), y = source.getY();
		ArrayList<Point> ret;
		
		int beforeScore = turn == Board.RED ? board.getRedScore() :board.getBlueScore();
		
		
		if(source.isHorizontal()) {
			if(isSetHLines[x][y]) return; // vec je postavljena linija
			ret = board.setHLine(x, y, turn); // postavljamo na tabli ovo poljena true
			hLines[x][y].setBackground(Color.BLACK);
			isSetHLines[x][y] = true;
		} else {
			if(isSetVLines[x][y]) return; // vec je postavljena linija
			ret = board.setVLine(x, y, turn); // postavljamo na tabli ovo poljena true
			vLines[x][y].setBackground(Color.BLACK);
			isSetVLines[x][y] = true;
		}
		
		//postavljanje boje polja na odgovarajucu vrednost
		for(Point p : ret)
			box[p.x][p.y].setBackground((turn == Board.RED) ? Color.red : Color.BLUE); 
		
		// Postavljanje labela za skor
		redScoreLabel.setText(String.valueOf(board.getRedScore()));
        blueScoreLabel.setText(String.valueOf(board.getBlueScore()));
		
		int afterScore = turn == Board.RED ? board.getRedScore() :board.getBlueScore();

		if(beforeScore < afterScore)
			writer.println(convertTo(source, true));
		else 
			writer.println(convertTo(source, false));

        
        //Obrada ako smo dosli do kraja
		if(board.isComplete()) {
			int winner = board.getWinner();
			if(winner == Board.RED) {
				statusLabel.setText("Player-1 is the winner!");
                statusLabel.setForeground(Color.RED);
			}else if(winner == Board.BLUE) {
                statusLabel.setText("Player-2 is the winner!");
                statusLabel.setForeground(Color.BLUE);
            }
            else {
                statusLabel.setText("Game Tied!");
                statusLabel.setForeground(Color.BLACK);
            }
		}
		
		if(ret.isEmpty()) {
			if(turn == 0) {
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

	

	private void manageGame() {
        while(!board.isComplete()) {
            if(onTurn == null) {
            	mouseEnabled = true;
            }
            else {
            	mouseEnabled = false;
            	if(stepByStep) {
	            	while(!goNextStep)
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
    
}

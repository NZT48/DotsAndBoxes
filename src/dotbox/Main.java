package dotbox;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

public class Main extends JPanel{

	private static final long serialVersionUID = 1L;
	private static JFrame frame;
    private static int m;
	private static int n;
	private static Player redPlayer;
	private static Player bluePlayer;
	private static boolean start;
	private static int depth;
	private static boolean readFromFile;
	
	private JComboBox<String> redPlayerSelector;
    private JComboBox<String> bluePlayerSelector;
    private JLabel DotsAndBox;
    private JButton startBtn;
    private JLabel jcomp5;
    private JLabel jcomp6;
    private JCheckBox loadFile;
    private JLabel jcomp8;
    private JComboBox<Integer> treeDepth;
    private JLabel jcomp10;
    private JLabel jcomp11;
    private JComboBox<Integer> tWidth;
    private JComboBox<Integer> tHeight;

	private Player getSolver(int level) {
        if(level == 1) return new EasyComputer();
        else if(level == 2) return new MediumComputer();
        //else if(level == 3) return new HardComputer();
        else return null;
    }
	
	private ActionListener btnListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            int rIndex = redPlayerSelector.getSelectedIndex();
            int bIndex = bluePlayerSelector.getSelectedIndex();
            
            n = tWidth.getSelectedIndex()+2;
            m = tHeight.getSelectedIndex()+2;
            depth = treeDepth.getSelectedIndex()+1;
            
            //System.out.println("\n m is " + m + " n is " + n + ",Tree depth is " + depth);
            
            redPlayer = getSolver(rIndex);
            bluePlayer = getSolver(bIndex);
            
            readFromFile = loadFile.isSelected();
            //if(readFromFile) System.out.println("The value for loading file is set" );
            
            start = true;
            
        }
    };
	
    @SuppressWarnings("static-access")
	public Main() {
    	//construct preComponents
        String[] players = {"Human", "Easy", "Medium", "Hard"};
        Integer[] treeDepthItems = {1, 2, 3, 4, 5, 6, 7, 8};
        Integer[] tWidthItems = {2, 3, 4, 5, 6, 7, 8, 9, 10};
        Integer[] tHeightItems = {2, 3, 4, 5, 6, 7, 8, 9, 10};

        //construct components
        redPlayerSelector = new JComboBox<String>(players);
        bluePlayerSelector = new JComboBox<String>(players);
        DotsAndBox = new JLabel ("Dots and Boxes");
        startBtn = new JButton ("Start");
        jcomp5 = new JLabel ("Blue Player :");
        jcomp6 = new JLabel ("Red Player :");
        loadFile = new JCheckBox ("Load moves from file");
        jcomp8 = new JLabel ("Tree depth if used medium or hard opponent:");
        treeDepth = new JComboBox<Integer>(treeDepthItems);
        jcomp10 = new JLabel ("Table width :");
        jcomp11 = new JLabel ("Table height :");
        tWidth = new JComboBox<Integer>(tWidthItems);
        tHeight = new JComboBox<Integer>(tHeightItems);

        //adjust size and set layout
        setPreferredSize (new Dimension (448, 379));
        setLayout (null);

        //add components
        add (redPlayerSelector);
        add (bluePlayerSelector);
        add (DotsAndBox);
        add (startBtn);
        add (jcomp5);
        add (jcomp6);
        add (loadFile);
        add (jcomp8);
        add (treeDepth);
        add (jcomp10);
        add (jcomp11);
        add (tWidth);
        add (tHeight);

        //set component bounds (only needed by Absolute Positioning)
        redPlayerSelector.setBounds (50, 140, 150, 25);
        bluePlayerSelector.setBounds (260, 140, 150, 25);
        DotsAndBox.setBounds (170, 10, 200, 40);
        startBtn.setBounds (80, 290, 310, 65);
        jcomp5.setBounds (260, 120, 100, 25);
        jcomp6.setBounds (50, 120, 100, 25);
        loadFile.setBounds (100, 250, 250, 30);
        jcomp8.setBounds (50, 180, 340, 30);
        treeDepth.setBounds (150, 210, 100, 25);
        jcomp10.setBounds (50, 60, 100, 25);
        jcomp11.setBounds (260, 60, 100, 25);
        tWidth.setBounds (50, 80, 100, 25);
        tHeight.setBounds (260, 80, 100, 25);
    	
        startBtn.addActionListener(btnListener);

    	
        start = false;
        
        /*
    	frame = new JFrame("Game Dots and Boxes");
    	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    	this.m = 4;
    	this.n = 4;
    	redPlayer = getSolver(1);
    	bluePlayer = getSolver(2);
    	new Game(frame, m, n, redPlayer, bluePlayer);
    	*/
    }

	public static void main(String[] args) {
		//new Main();
		JFrame frameStart = new JFrame ("Start page");
		frameStart.setDefaultCloseOperation (JFrame.EXIT_ON_CLOSE);
		frameStart.getContentPane().add (new Main());
		frameStart.pack();
		frameStart.setLocationRelativeTo(null);
        frameStart.setVisible (true);
        
        frame = new JFrame("Game Dots and Boxes");
    	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        while(!start) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        new Game(frameStart, m, n, redPlayer, bluePlayer, depth, readFromFile);
	}

}
package dotbox;

import java.awt.*;
//import java.awt.event.*;
import javax.swing.*;


public class StartPage extends JFrame{
	
	//private JFrame frame;
	
	private static final long serialVersionUID = 1L;
	private JComboBox<String> playerOne, playerTwo;
    String[] players = {"Select player", "Human", "Weak Opponent", "Neutarl Opponent", "Strong opponent"};

	private JComboBox<Integer> mTF, nTF;
	Integer[] dimension = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
	private JLabel mLabel, nLabel;
	
	//private JLabel treeDepthLabel;
	//private JTextField treeDepth;
	
	//private JButton startGame, loadMoves;
	
	public StartPage() {
		super("Start page!");
		setLayout(new GridLayout(6,1));
		//frame = new JFrame();
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(800,800,800,820);
		
		playerOne = new JComboBox<String>(players);
		playerTwo = new JComboBox<String>(players);
		
		mTF = new JComboBox<Integer>(dimension);
		nTF = new JComboBox<Integer>(dimension);

		
		//mTF = new JTextField("2");
		mLabel = new JLabel("Parametar m (visina matrice):");
		//nTF = new JTextField("2");
		nLabel = new JLabel("Parametar n (sirina matrice):");

	
		
		
		putOnScreen();
		
		
		setLocationRelativeTo(null);
		setVisible(true);
		
		
		
	}
	
	public void putOnScreen() {
		JPanel params = new JPanel(new GridLayout(2,2));
		
		params.add(nLabel);
		params.add(mLabel);
		
		params.add(nTF);
		params.add(mTF);
		
		add(params);
		
		JPanel north = new JPanel(new FlowLayout());
		north.add(playerOne);
		north.add(playerTwo);
		
		add(north);
		
		add(new JLabel("TEST"));



	}
	
	
}

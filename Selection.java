// This Jframe is where the user selects their opponent: 1 AI, 2 AI or 3 AI

import javax.swing.*;
import java.awt.Font;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Selection extends JFrame implements ActionListener {
	private JLabel choose;
	private JButton one;
	private JButton two;
	private JButton three;
	
	private Font chooseFont = new Font("Serif", Font.BOLD, 30);
	private Font buttonFont = new Font("Serif", Font.BOLD, 20);
	Color lightBlue = new Color(173, 216, 230);
	
	// Constructor
	public Selection() {
		setSize(800, 600);
		setTitle("Opponent Selection");
		setLayout(null);
		this.setResizable(false);
		this.getContentPane().setBackground(lightBlue);

		// Prompt for user to choose opponent
		choose = new JLabel("Choose Your Opponent");
		choose.setFont(chooseFont);
		choose.setBounds(200, 100, 400, 70);
		this.add(choose);

		// Option 1: vs. one AI
		one = new JButton("1 AI");
		one.setFont(buttonFont);
		one.setBounds(340, 200, 120, 70);
		one.addActionListener(this);
		this.add(one);

		// Option 2: vs. two AIs
		two = new JButton("2 AIs");
		two.setFont(buttonFont);
		two.setBounds(340, 300, 120, 70);
		two.addActionListener(this);
		this.add(two);

		// Option 3: vs. three AIs
		three = new JButton("3 AIs");
		three.setFont(buttonFont);
		three.setBounds(340, 400, 120, 70);
		three.addActionListener(this);
		this.add(three);
		
		setVisible(true);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
	}

	// actionPerformed method
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == one) {
			Game.maxPlayers = 2;
			this.dispose();
			new Game();
		}
		if (e.getSource() == two) {
			Game.maxPlayers = 3;
			this.dispose();
			new Game();
		}
		if (e.getSource() == three) {
			Game.maxPlayers = 4;
			this.dispose();
			new Game();
		}
	}
}
// This JFrame is displayed when the user loses

import javax.swing.*;
import java.awt.Font;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Lose extends JFrame implements ActionListener {
	private JLabel lose;
	private JButton playAgain;
	private JButton mainMenu;
	
	private Font loseFont = new Font("Serif", Font.BOLD, 40);
	private Font buttonFont = new Font("Serif", Font.BOLD, 18);
	Color lightBlue = new Color(173, 216, 230);
	
	// Constructor
	public Lose() {
		setSize(800, 600);
		setTitle("Game Over!");
		setLayout(null);
		this.setResizable(false);
		this.getContentPane().setBackground(lightBlue);

		// Lose text
		lose = new JLabel("You Lost!");
		lose.setFont(loseFont);
		lose.setBounds(290, 150, 250, 100);
		this.add(lose);

		// Play again button
		playAgain = new JButton("Play Again");
		playAgain.setFont(buttonFont);
		playAgain.setBounds(325, 300, 150, 70);
		playAgain.addActionListener(this);
		this.add(playAgain);

		// Main menu button
		mainMenu = new JButton("Menu");
		mainMenu.setFont(buttonFont);
		mainMenu.setBounds(325, 400, 150, 70);
		mainMenu.addActionListener(this);
		this.add(mainMenu);
		
		setVisible(true);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
	}

	// actionPerformed method
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == playAgain) {
			this.dispose();
			new Selection();
		}
		if (e.getSource() == mainMenu) {
			this.dispose();
			new Menu();
		}
	}
}
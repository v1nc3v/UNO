// This JFrame is displayed when the user wins

import javax.swing.*;
import java.awt.Font;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Win extends JFrame implements ActionListener {
	private JLabel win;
	private JButton playAgain;
	private JButton mainMenu;
	
	private Font winFont = new Font("Serif", Font.BOLD, 40);
	private Font buttonFont = new Font("Serif", Font.BOLD, 18);
	Color lightBlue = new Color(173, 216, 230);
	
	// Constructor
	public Win() {
		setSize(800, 600);
		setTitle("Winner!");
		setLayout(null);
		this.setResizable(false);
		this.getContentPane().setBackground(lightBlue);

		// Win text
		win = new JLabel("You Won!");
		win.setFont(winFont);
		win.setBounds(290, 150, 250, 100);
		this.add(win);

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
// This JFrame is the main menu screen

import javax.swing.*;
import java.awt.Font;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Menu extends JFrame implements ActionListener {
	private JLabel title;
	private JButton play;
	
	private Font titleFont = new Font("Serif", Font.BOLD, 75);
	private Font buttonFont = new Font("Serif", Font.BOLD, 20);
	Color lightBlue = new Color(173, 216, 230);
	
	// Constructor
	public Menu() {
		setSize(800, 600);
		setTitle("Menu");
		setLayout(null);
		this.setResizable(false);
		this.getContentPane().setBackground(lightBlue);

		// Title
		title = new JLabel("UNO");
		title.setFont(titleFont);
		title.setBounds(300, 125, 200, 100);
		this.add(title);

		// Play button
		play = new JButton("Play");
		play.setFont(buttonFont);
		play.setBounds(350, 300, 100, 70);
		play.addActionListener(this);
		this.add(play);
		
		setVisible(true);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
	}

	// actionPerformed method
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == play) {
			this.dispose();
			new Selection();
		}
	}
}
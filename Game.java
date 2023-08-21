// Main Game

import javax.swing.*;
import java.awt.*;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class Game extends JFrame implements ActionListener {
	// swing variables
	private JLabel turnText, drawText, discardText, discardPile, handText, wildColor, ai1, ai2, ai3, ai1Num, ai2Num, ai3Num, ai1Card, ai2Card, ai3Card;
	private JButton drawDeck;
	private JButton[] cards;

	// other variables
	private String file;
	private String face;
	private BufferedImage img;
	private Image image;
	private ImageIcon icon;
	
	private Font faceFont = new Font("Serif", Font.BOLD, 70); // Font for the number on cards
	private Font smallFont = new Font("Serif", Font.PLAIN, 12); // Font for the "draw" and "discard" labels in the center
	private Font textFont = new Font("Serif", Font.PLAIN, 25); // Font for general text like AI names and turnText
	private Font aiFont = new Font("Serif", Font.PLAIN, 20); // Font for number of cards in AI's hand
	Color lightBlue = new Color(173, 216, 230); // Background color
	
	// class variables
	public static int maxPlayers = 0;
	private static int turn = 1;
	private static boolean reverse = false;
	private static Card discard = new Card();
	private static Player player = new Player();
	private static Player aiOne = new Player();
	private static Player aiTwo = new Player();
	private static Player aiThree = new Player();
	
	// Constructor
	public Game() {
		setSize(800, 600);
		setTitle("UNO");
		setLayout(null);
		this.setResizable(false);
		this.getContentPane().setBackground(lightBlue);

		// Display opponents
		if (maxPlayers >= 2) {
			aiOne = new Player();
			
			// AI 1 text
			ai1 = new JLabel("AI 1");
			ai1.setFont(textFont);
			ai1.setBounds(25, 210, 60, 50);
			this.add(ai1);

			// Display number of cards in opponent's hand
			ai1Num = new JLabel(aiOne.getNumCards() + " x");
			ai1Num.setFont(aiFont);
			ai1Num.setBounds(10, 265, 50, 40);
			this.add(ai1Num);

			image = readFile("card back").getScaledInstance(30, 50, Image.SCALE_DEFAULT);
			ai1Card = new JLabel(new ImageIcon(image));
			ai1Card.setBackground(Color.WHITE);
			ai1Card.setBounds(55, 260, 30, 50);
			this.add(ai1Card);

			if (maxPlayers >= 3) {
				aiTwo = new Player();
				
				// AI 2 text
				ai2 = new JLabel("AI 2");
				ai2.setFont(textFont);
				ai2.setBounds(370, 10, 60, 50);
				this.add(ai2);

				// Display number of cards in opponent's hand
				ai2Num = new JLabel(aiTwo.getNumCards() + " x");
				ai2Num.setFont(aiFont);
				ai2Num.setBounds(360, 65, 50, 40);
				this.add(ai2Num);

				image = readFile("card back").getScaledInstance(30, 50, Image.SCALE_DEFAULT);
				ai2Card = new JLabel(new ImageIcon(image));
				ai2Card.setBackground(Color.WHITE);
				ai2Card.setBounds(405, 60, 30, 50);
				this.add(ai2Card);

				if (maxPlayers == 4) {
					aiThree = new Player();
					
					// AI 3 text
					ai3 = new JLabel("AI 3");
					ai3.setFont(textFont);
					ai3.setBounds(720, 210, 60, 50);
					this.add(ai3);

					// Display number of cards in opponent's hand
					ai3Num = new JLabel(aiThree.getNumCards() + " x");
					ai3Num.setFont(aiFont);
					ai3Num.setBounds(715, 265, 50, 40);
					this.add(ai3Num);

					image = readFile("card back").getScaledInstance(30, 50, Image.SCALE_DEFAULT);
					ai3Card = new JLabel(new ImageIcon(image));
					ai3Card.setBackground(Color.WHITE);
					ai3Card.setBounds(760, 260, 30, 50);
					this.add(ai3Card);
				}
			}
		}
		
		// Display the draw card
		image = readFile("cardback").getScaledInstance(60, 100, Image.SCALE_DEFAULT);
		drawDeck = new JButton(new ImageIcon(image));
		drawDeck.setBackground(Color.WHITE);
		drawDeck.setBounds(330, 230, 60, 100);
		drawDeck.addActionListener(this);
		this.add(drawDeck);

		// Ensure starting card on discard pile is not a wild card
		while ((discard.getSymbol()).equals("wild") || (discard.getSymbol()).equals("wild draw 4")) {
			discard = new Card();
		}
		
		discardPile = new JLabel();
		updateDiscard();

		// Text above draw card
		drawText = new JLabel("Draw");
		drawText.setFont(smallFont);
		drawText.setBounds(342, 205, 40, 25);
		this.add(drawText);

		// Text above discard pile
		discardText = new JLabel("Discard");
		discardText.setFont(smallFont);
		discardText.setBounds(415, 205, 60, 25);
		this.add(discardText);

		// Text indicating the color of a wild card
		wildColor = new JLabel();
		wildColor.setFont(smallFont);
		wildColor.setBounds(480, 265, 60, 25);
		this.add(wildColor);
		wildColor.setVisible(false);

		// Text indicating who's turn it is
		turnText = new JLabel("Your Turn");
		turnText.setFont(textFont);
		turnText.setBounds(335, 335, 150, 50);
		this.add(turnText);

		// Text below your hand
		handText = new JLabel("Your Hand");
		handText.setFont(textFont);
		handText.setBounds(340, 525, 150, 50);
		this.add(handText);

		// Ensure everything is resetted when playing again
		player = new Player();
		
		updateHand();
		
		setVisible(true);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
	}
	
	// actionPerformed method
	@Override
	public void actionPerformed(ActionEvent e) {
		if (turn == 1) {
			if (e.getSource() == drawDeck) {
				// Update player's hand in GUI
				removeHand();
				player.setNumCards(cards.length + 1);
				player.setLargerHand(player.getHand());
				updateHand();

				if (reverse) {
					turn = maxPlayers;
					aiPlay();
				}
				else {
					turn++;
					aiPlay();
				}
			}	

			for (int i = 0; i < cards.length; i++) {
				if (e.getSource() == cards[i]) { // When a button is clicked
					// Check if card is able to be played
					if ((player.getHand()[i]).isSimilar(discard)) {
						discard = (player.getHand())[i];
						updateDiscard();
						wildColor.setVisible(false);

						// If user plays a wild card, let them choose the color
						if ((discard.getSymbol()).equals("wild") || (discard.getSymbol()).equals("wild draw 4")) {
							Object[] colors = { "Red", "Blue", "Yellow", "Green"};
							Object colorChosen = JOptionPane.showInputDialog(null, "Choose a color", "Wild Card Color", JOptionPane.INFORMATION_MESSAGE, null, colors, colors[0]);

							// Display the correct color based on user's choice
							if (colorChosen == colors[0]) {
								discard.setColor("red");
								wildColor.setText("Red");
								wildColor.setForeground(Color.RED);
								wildColor.setVisible(true);
							}
							else if (colorChosen == colors[1]) {
								discard.setColor("blue");
								wildColor.setText("Blue");
								wildColor.setForeground(Color.BLUE);
								wildColor.setVisible(true);
							}
							else if (colorChosen == colors[2]) {
								discard.setColor("yellow");
								wildColor.setText("Yellow");
								wildColor.setForeground(Color.YELLOW);
								wildColor.setVisible(true);
							}
							else if (colorChosen == colors[3]) {
								discard.setColor("green");
								wildColor.setText("Green");
								wildColor.setForeground(Color.GREEN);
								wildColor.setVisible(true);
							}
						}
						
						// Update player's hand in GUI
						removeHand();
						player.setNumCards(cards.length - 1);
						player.setSmallerHand(player.getHand(), discard);
						updateHand();

						// If you have no more cards, you win
						if (player.getNumCards() == 0) {
							this.dispose();
							new Win();
						}

						// 2 players handle action cards
						if (maxPlayers == 2) {
							// Check for draw 2
							if ((discard.getSymbol()).equals("draw 2")) {
								aiOne.setNumCards((aiOne.getHand()).length + 2);
								aiOne.setLargerHand(aiOne.getHand());
								ai1Num.setText(aiOne.getNumCards() + " x");
		
								turn = 1;
								turnText.setText("Your Turn");
							}
							// Check for skip
							else if ((discard.getSymbol()).equals("skip")) {
								turn = 1;
								turnText.setText("Your Turn");
							}
							// Check for draw 4
							else if ((discard.getSymbol()).equals("wild draw 4")) {
								aiOne.setNumCards((aiOne.getHand()).length + 4);
								aiOne.setLargerHand(aiOne.getHand());
								ai1Num.setText(aiOne.getNumCards() + " x");
		
								turn = 1;
								turnText.setText("Your Turn");
							}
							else {
								turn++;
								aiPlay();
							}
						}
						// 3 players handle action cards
						else if (maxPlayers == 3) {
							// Check for draw 2
							if ((discard.getSymbol()).equals("draw 2")) {
								// Check for reverse and normal
								if (reverse) {
									aiTwo.setNumCards((aiTwo.getHand()).length + 2);
									aiTwo.setLargerHand(aiTwo.getHand());
									ai2Num.setText(aiTwo.getNumCards() + " x");

									turn = 2;
									aiPlay();
								}
								else {
									aiOne.setNumCards((aiOne.getHand()).length + 2);
									aiOne.setLargerHand(aiOne.getHand());
									ai1Num.setText(aiOne.getNumCards() + " x");
		
									turn = 3;
									aiPlay();
								}
							}
							// Check for skip
							else if ((discard.getSymbol()).equals("skip")) {
								if (reverse) {
									turn = 2;
									aiPlay();
								}
								else {
									turn = 3;
									aiPlay();
								}
							}
							// Check for reverse
							else if ((discard.getSymbol()).equals("reverse")) {
								if (reverse) {
									reverse = false;
									turn = 2;
									aiPlay();
								}
								else {
									reverse = true;
									turn = 3;
									aiPlay();
								}
							}
							// Check for draw 4
							else if ((discard.getSymbol()).equals("wild draw 4")) {
								if (reverse) {
									aiTwo.setNumCards((aiTwo.getHand()).length + 4);
									aiTwo.setLargerHand(aiTwo.getHand());
									ai2Num.setText(aiTwo.getNumCards() + " x");

									turn = 2;
									aiPlay();
								}
								else {
									aiOne.setNumCards((aiOne.getHand()).length + 4);
									aiOne.setLargerHand(aiOne.getHand());
									ai1Num.setText(aiOne.getNumCards() + " x");
			
									turn = 3;
									aiPlay();
								}
							}
							else {
								if (reverse) {
									turn = 3;
									aiPlay();
								}
								else {
									turn++;
									aiPlay();
								}
							}
						}
						// 4 players handle action cards
						else if (maxPlayers == 4) {
							// Check for draw 2
							if ((discard.getSymbol()).equals("draw 2")) {
								// Check for reverse and normal
								if (reverse) {
									aiThree.setNumCards((aiThree.getHand()).length + 2);
									aiThree.setLargerHand(aiThree.getHand());
									ai3Num.setText(aiThree.getNumCards() + " x");
								}
								else {
									aiOne.setNumCards((aiOne.getHand()).length + 2);
									aiOne.setLargerHand(aiOne.getHand());
									ai1Num.setText(aiOne.getNumCards() + " x");
								}

								turn = 3;
								aiPlay();
							}
							// Check for skip
							else if ((discard.getSymbol()).equals("skip")) {
								turn = 3;
								aiPlay();
							}
							// Check for reverse
							else if ((discard.getSymbol()).equals("reverse")) {
								if (reverse) {
									reverse = false;
									turn = 2;
									aiPlay();
								}
								else {
									reverse = true;
									turn = 4;
									aiPlay();
								}
							}
							// Check for draw 4
							else if ((discard.getSymbol()).equals("wild draw 4")) {
								if (reverse) {
									aiThree.setNumCards((aiThree.getHand()).length + 4);
									aiThree.setLargerHand(aiThree.getHand());
									ai3Num.setText(aiThree.getNumCards() + " x");
								}
								else {
									aiOne.setNumCards((aiOne.getHand()).length + 4);
									aiOne.setLargerHand(aiOne.getHand());
									ai1Num.setText(aiOne.getNumCards() + " x");
								}

								turn = 3;
								aiPlay();
							}
							else {
								if (reverse) {
									turn = 4;
									aiPlay();
								}
								else {
									turn = 2;
									aiPlay();
								}
							}
						}
					}
				}
			}
		}
	}
	
	// Method to read an image file
	private BufferedImage readFile(String f) {
		try {
			img = ImageIO.read(new File("Cards/" + f + ".png"));
			
		} catch (IOException e) {}

		return img;
	}

	// AIs' turn
	private void aiPlay() {
		Timer timer = new Timer();

		// AI 1's turn
		if (turn == 2) {
			turnText.setText("AI 1's Turn");
				
			TimerTask task1 = new TimerTask() {
				// AI turn happens after 1.5 seconds
				public void run() {
					boolean cardNotPlayed = true;
	
					// AI goes through their hand and plays the first card that is similar to discard
					for (int i = 0; i < (aiOne.getHand()).length; i++) {
						if ((aiOne.getHand())[i].isSimilar(discard)) {
							discard = (aiOne.getHand())[i];
							updateDiscard();
							wildColor.setVisible(false);	
	
							// If AI plays a wild card, choose a random color
							if ((discard.getSymbol()).equals("wild") || (discard.getSymbol()).equals("wild draw 4")) {
								getRandomColor();
							}
						
							aiOne.setNumCards((aiOne.getHand()).length - 1);
							aiOne.setSmallerHand(aiOne.getHand(), discard);
							ai1Num.setText(aiOne.getNumCards() + " x");
					
							// If AI has no more cards, you lose
							if (aiOne.getNumCards() == 0) {
								loseScreen();
							}
								
							cardNotPlayed = false;
							break;
						}
					}
	
					// If AI cannot play a card, they will draw a card
					if (cardNotPlayed) {
						aiOne.setNumCards((aiOne.getHand()).length + 1);
						aiOne.setLargerHand(aiOne.getHand());
						ai1Num.setText(aiOne.getNumCards() + " x");
					}

					// 2 players handle action cards
					if (turn == maxPlayers) {
						// Check for draw 2
						if ((discard.getSymbol()).equals("draw 2")) {
							removeHand();
							player.setNumCards(cards.length + 2);
							player.setLargerHand(player.getHand());
							updateHand();

							turn = 2;
							aiPlay();
						}
						// Check for skip
						else if ((discard.getSymbol()).equals("skip")) {
							turn = 2;
							aiPlay();
						}
						// Check for draw 4
						else if ((discard.getSymbol()).equals("wild draw 4")) {
							removeHand();
							player.setNumCards(cards.length + 4);
							player.setLargerHand(player.getHand());
							updateHand();

							turn = 2;
							aiPlay();
						}
						else {
							turn = 1;
							turnText.setText("Your Turn");
						}
					}
					else {
						// 3 players handle action cards
						if (maxPlayers == 3) {
							// Check for draw 2
							if ((discard.getSymbol()).equals("draw 2")) {
								// Check for reverse and normal
								if (reverse) {
									removeHand();
									player.setNumCards(cards.length + 2);
									player.setLargerHand(player.getHand());
									updateHand();

									turn = 3;
									aiPlay();
								}
								else {
									aiTwo.setNumCards((aiTwo.getHand()).length + 2);
									aiTwo.setLargerHand(aiTwo.getHand());
									ai2Num.setText(aiTwo.getNumCards() + " x");
		
									turn = 1;
									turnText.setText("Your Turn");
								}
							}
							// Check for skip
							else if ((discard.getSymbol()).equals("skip")) {
								if (reverse) {
									turn = 3;
									aiPlay();
								}
								else {
									turn = 1;
									turnText.setText("Your Turn");
								}
							}
							// Check for reverse
							else if ((discard.getSymbol()).equals("reverse")) {
								if (reverse) {
									reverse = false;
									turn = 3;
									aiPlay();
								}
								else {
									reverse = true;
									turn = 1;
									turnText.setText("Your Turn");
								}
							}
							// Check for draw 4
							else if ((discard.getSymbol()).equals("wild draw 4")) {
								if (reverse) {
									removeHand();
									player.setNumCards(cards.length + 4);
									player.setLargerHand(player.getHand());
									updateHand();

									turn = 3;
									aiPlay();
								}
								else {
									aiTwo.setNumCards((aiTwo.getHand()).length + 4);
									aiTwo.setLargerHand(aiTwo.getHand());
									ai2Num.setText(aiTwo.getNumCards() + " x");
									
									turn = 1;
									turnText.setText("Your Turn");
								}
							}
							else {
								if (reverse) {
									turn = 1;
									turnText.setText("Your Turn");
								}
								else {
									turn = 3;
									aiPlay();
								}
							}
						}
						// 4 players handle action cards
						else if (maxPlayers == 4) {
							// Check for draw 2
							if ((discard.getSymbol()).equals("draw 2")) {
								// Check for reverse and normal
								if (reverse) {
									removeHand();
									player.setNumCards(cards.length + 2);
									player.setLargerHand(player.getHand());
									updateHand();
								}
								else {
									aiTwo.setNumCards((aiTwo.getHand()).length + 2);
									aiTwo.setLargerHand(aiTwo.getHand());
									ai2Num.setText(aiTwo.getNumCards() + " x");
								}
	
								turn = 4;
								aiPlay();
							}
							// Check for skip
							else if ((discard.getSymbol()).equals("skip")) {
								turn = 4;
								aiPlay();
							}
							// Check for reverse
							else if ((discard.getSymbol()).equals("reverse")) {
								if (reverse) {
									reverse = false;
									turn = 3;
									aiPlay();
								}
								else {
									reverse = true;
									turn = 1;
									turnText.setText("Your Turn");
								}
							}
							// Check for draw 4
							else if ((discard.getSymbol()).equals("wild draw 4")) {
								if (reverse) {
									removeHand();
									player.setNumCards(cards.length + 4);
									player.setLargerHand(player.getHand());
									updateHand();
								}
								else {
									aiTwo.setNumCards((aiTwo.getHand()).length + 4);
									aiTwo.setLargerHand(aiTwo.getHand());
									ai2Num.setText(aiTwo.getNumCards() + " x");
								}
	
								turn = 4;
								aiPlay();
							}
							else {
								if (reverse) {
									turn = 1;
									turnText.setText("Your Turn");
								}
								else {
									turn = 3;
									aiPlay();
								}
							}
						}
					}
				}
			};

			timer.schedule(task1, 2250);
		}
		
		// AI 2's turn
		if (turn == 3) {
			turnText.setText("AI 2's Turn");
				
			TimerTask task2 = new TimerTask() {
				// AI turn happens after 1.5 seconds
				public void run() {
					boolean cardNotPlayed = true;
	
					// AI goes through their hand and plays the first card that is similar to discard
					for (int i = 0; i < (aiTwo.getHand()).length; i++) {
						if ((aiTwo.getHand())[i].isSimilar(discard)) {
							discard = (aiTwo.getHand())[i];
							updateDiscard();
							wildColor.setVisible(false);	
	
							// If AI plays a wild card, choose a random color
							if ((discard.getSymbol()).equals("wild") || (discard.getSymbol()).equals("wild draw 4")) {
								getRandomColor();
							}
						
							aiTwo.setNumCards((aiTwo.getHand()).length - 1);
							aiTwo.setSmallerHand(aiTwo.getHand(), discard);
							ai2Num.setText(aiTwo.getNumCards() + " x");

							// If AI has no more cards, you lose
							if (aiTwo.getNumCards() == 0) {
								loseScreen();
							}
							
							cardNotPlayed = false;
							break;
						}
					}
	
					// If AI cannot play a card, they will draw a card
					if (cardNotPlayed) {
						aiTwo.setNumCards((aiTwo.getHand()).length + 1);
						aiTwo.setLargerHand(aiTwo.getHand());
						ai2Num.setText(aiTwo.getNumCards() + " x");
					}

					// 3 players handle action cards
					if (turn == maxPlayers) {
						// Check for draw 2
						if ((discard.getSymbol()).equals("draw 2")) {
							// Check for reverse and normal
							if (reverse) {
								aiOne.setNumCards((aiOne.getHand()).length + 2);
								aiOne.setLargerHand(aiOne.getHand());
								ai1Num.setText(aiOne.getNumCards() + " x");

								turn = 1;
								turnText.setText("Your Turn");
							}
							else {
								removeHand();
								player.setNumCards(cards.length + 2);
								player.setLargerHand(player.getHand());
								updateHand();

								turn = 2;
								aiPlay();
							}
						}
						// Check for skip
						else if ((discard.getSymbol()).equals("skip")) {
							if (reverse) {
								turn = 1;
								turnText.setText("Your Turn");
							}
							else {
								turn = 2;
								aiPlay();
							}
						}
						// Check for reverse
						else if ((discard.getSymbol()).equals("reverse")) {
							if (reverse) {
								reverse = false;
								turn = 1;
								turnText.setText("Your Turn");
							}
							else {
								reverse = true;
								turn = 2;
								aiPlay();
							}
						}
						// Check for draw 4
						else if ((discard.getSymbol()).equals("wild draw 4")) {
							if (reverse) {
								aiOne.setNumCards((aiOne.getHand()).length + 4);
								aiOne.setLargerHand(aiOne.getHand());
								ai1Num.setText(aiOne.getNumCards() + " x");

								turn = 1;
								turnText.setText("Your Turn");
							}
							else {
								removeHand();
								player.setNumCards(cards.length + 4);
								player.setLargerHand(player.getHand());
								updateHand();

								turn = 2;
								aiPlay();
							}
						}
						else {
							if (reverse) {
								turn = 2;
								aiPlay();
							}
							else {
								turn = 1;
								turnText.setText("Your Turn");
							}
						}
					}
					// 4 player handle action cards
					else {
						// Check for draw 2
						if ((discard.getSymbol()).equals("draw 2")) {
							// Check for reverse and normal
							if (reverse) {
								aiOne.setNumCards((aiOne.getHand()).length + 2);
								aiOne.setLargerHand(aiOne.getHand());
								ai1Num.setText(aiOne.getNumCards() + " x");
							}
							else {
								aiThree.setNumCards((aiThree.getHand()).length + 2);
								aiThree.setLargerHand(aiThree.getHand());
								ai3Num.setText(aiThree.getNumCards() + " x");
							}

							turn = 1;
							turnText.setText("Your Turn");
						}
						// Check for skip
						else if ((discard.getSymbol()).equals("skip")) {
							turn = 1;
							turnText.setText("Your Turn");
						}
						// Check for reverse
						else if ((discard.getSymbol()).equals("reverse")) {
							if (reverse) {
								reverse = false;
								turn = 4;
								turnText.setText("Your Turn");
							}
							else {
								reverse = true;
								turn = 2;
								aiPlay();
							}
						}
						// Check for draw 4
						else if ((discard.getSymbol()).equals("wild draw 4")) {
							if (reverse) {
								aiOne.setNumCards((aiOne.getHand()).length + 4);
								aiOne.setLargerHand(aiOne.getHand());
								ai1Num.setText(aiOne.getNumCards() + " x");
							}
							else {
								aiThree.setNumCards((aiThree.getHand()).length + 4);
								aiThree.setLargerHand(aiThree.getHand());
								ai3Num.setText(aiThree.getNumCards() + " x");
							}

							turn = 1;
							turnText.setText("Your Turn");
						}
						else {
							if (reverse) {
								turn = 2;
								aiPlay();
							}
							else {
								turn = 4;
								aiPlay();
							}
						}
					}
				}
			};

			timer.schedule(task2, 2250);
		}

		// AI 3's turn
		if (turn == 4) {
			turnText.setText("AI 3's Turn"); 
				
			TimerTask task3 = new TimerTask() {
				// AI turn happens after 1.5 seconds
				public void run() {
					boolean cardNotPlayed = true;
					
					// AI goes through their hand and plays the first card that is similar to discard
					for (int i = 0; i < (aiThree.getHand()).length; i++) {
						if ((aiThree.getHand())[i].isSimilar(discard)) {
							discard = (aiThree.getHand())[i];
							updateDiscard();
							wildColor.setVisible(false);	
	
							// If AI plays a wild card, choose a random color
							if ((discard.getSymbol()).equals("wild") || (discard.getSymbol()).equals("wild draw 4")) {
								getRandomColor();
							}
						
							aiThree.setNumCards((aiThree.getHand()).length - 1);
							aiThree.setSmallerHand(aiThree.getHand(), discard);
							ai3Num.setText(aiThree.getNumCards() + " x");

							// If AI has no more cards, you lose
							if (aiThree.getNumCards() == 0) {
								loseScreen();
							}
							
							cardNotPlayed = false;
							break;
						}
					}
	
					// If AI cannot play a card, they will draw a card
					if (cardNotPlayed) {
						aiThree.setNumCards((aiThree.getHand()).length + 1);
						aiThree.setLargerHand(aiThree.getHand());
						ai3Num.setText(aiThree.getNumCards() + " x");
					}

					// Handle action cards
					// Check for draw 2
					if ((discard.getSymbol()).equals("draw 2")) {
						// Check for reverse and normal
						if (reverse) {
							aiTwo.setNumCards((aiTwo.getHand()).length + 2);
							aiTwo.setLargerHand(aiTwo.getHand());
							ai2Num.setText(aiTwo.getNumCards() + " x");
						}
						else {
							removeHand();
							player.setNumCards(cards.length + 2);
							player.setLargerHand(player.getHand());
							updateHand();
						}

						turn = 2;
						aiPlay();
					}
					// Check for skip
					else if ((discard.getSymbol()).equals("skip")) {
						turn = 2;
						aiPlay();
					}
					// Check for reverse
					else if ((discard.getSymbol()).equals("reverse")) {
						if (reverse) {
							reverse = false;
							turn = 1;
							turnText.setText("Your Turn");
						}
						else {
							reverse = true;
							turn = 3;
							aiPlay();
						}
					}
					// Check for draw 4
					else if ((discard.getSymbol()).equals("wild draw 4")) {
						if (reverse) {
							aiTwo.setNumCards((aiTwo.getHand()).length + 4);
							aiTwo.setLargerHand(aiTwo.getHand());
							ai2Num.setText(aiTwo.getNumCards() + " x");
						}
						else {
							removeHand();
							player.setNumCards(cards.length + 4);
							player.setLargerHand(player.getHand());
							updateHand();
						}

						turn = 2;
						aiPlay();
					}
					else {
						if (reverse) {
							turn = 3;
							aiPlay();
						}
						else {
							turn = 1;
							turnText.setText("Your Turn");
						}
					}
				}
			};

			timer.schedule(task3, 2250);
		}
	}

	// Get a random color for the AI's wild card
	private void getRandomColor() {
		Random random = new Random();
		int num = random.nextInt(4);
		
		if (num == 0) {
			discard.setColor("red");
			wildColor.setText("Red");
			wildColor.setForeground(Color.RED);
			wildColor.setVisible(true);
		}
		else if (num == 1) {
			discard.setColor("blue");
			wildColor.setText("Blue");
			wildColor.setForeground(Color.BLUE);
			wildColor.setVisible(true);
		}
		else if (num == 2) {
			discard.setColor("yellow");
			wildColor.setText("Yellow");
			wildColor.setForeground(Color.YELLOW);
			wildColor.setVisible(true);
		}
		else if (num == 3) {
			discard.setColor("green");
			wildColor.setText("Green");
			wildColor.setForeground(Color.GREEN);
			wildColor.setVisible(true);
		}
	}
	
	// Displays your hand
	private void updateHand() {
		Card temp;
		int x = 75;
		int y = 425;
		cards = new JButton[(player.getHand()).length]; 
		
		for (int i = 0; i < cards.length; i++) {
			temp = (player.getHand())[i];
			
			// Check for wild or wild draw 4 card
			if ((temp.getColor()).equals("n/a")) {
				file = temp.getSymbol();
			}
			// Check for reverse, skip, or draw 2 card
			else if ((temp.getSymbol()).equals("reverse") || (temp.getSymbol()).equals("skip") || (temp.getSymbol()).equals("draw 2")) {
				file = temp.getColor() + " " + temp.getSymbol();
			}
			else {
				file = temp.getColor();
			}

			// Read file
			try {
				img = ImageIO.read(new File("Cards/" + file + ".png"));
				
			} catch (IOException e) {}

			// Draw number on card if necessary
			if (temp.getNumber() != -1) {
				face = temp.getNumber() + "";
				Graphics g = img.getGraphics();
    		g.setFont(faceFont);
    		g.drawString(face, 70, 165);
    		g.dispose();
			}

			// Resize image and add it to the GUI
			image = img.getScaledInstance(60, 100, Image.SCALE_DEFAULT);
			icon = new ImageIcon(image);
			cards[i] = new JButton();
			cards[i].setIcon(icon);
			cards[i].setBackground(Color.WHITE);
			cards[i].setBounds(x, y, 60, 100);
			cards[i].addActionListener(this);
			this.add(cards[i]);

			x += 50; // adjust position of next card
		}
	}

	// Remove old hand from view
	private void removeHand() {
		for (int i = 0; i < cards.length; i++) {
			cards[i].setVisible(false);
		}
	}
	
	// Displays card on top of discard pile
	private void updateDiscard() {
		
		// Check for wild or wild draw 4 card
		if ((discard.getColor()).equals("n/a")) {
			file = discard.getSymbol();
		}
		// Check for reverse, skip, or draw 2 card
		else if ((discard.getSymbol()).equals("reverse") || (discard.getSymbol()).equals("skip") || (discard.getSymbol()).equals("draw 2")) {
			file = discard.getColor() + " " + discard.getSymbol();
		}
		else {
			file = discard.getColor();
		}

		// Read file
		try {
			img = ImageIO.read(new File("Cards/" + file + ".png"));
			
		} catch (IOException e) {}

		// Draw number on card if necessary
		if (discard.getNumber() != -1) {
			face = discard.getNumber() + "";
			Graphics g = img.getGraphics();
    	g.setFont(faceFont);
    	g.drawString(face, 70, 165);
    	g.dispose();
		}

		// Resize image and add it to the GUI
		image = img.getScaledInstance(60, 100, Image.SCALE_DEFAULT);
		icon = new ImageIcon(image);
		discardPile.setIcon(icon);
		discardPile.setBackground(Color.WHITE);
		discardPile.setBounds(410, 230, 60, 100);
		this.add(discardPile);
	}

	// Method to switch to Lose.java
	public void loseScreen() {
		this.dispose();
		new Lose();
	}
}
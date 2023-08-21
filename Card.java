import java.util.Random;

public class Card {

	// instance variables
	private int number;
	private String symbol;
	private String color;

	// constructors
	public Card(int n, String s, String c) {
		number = n;
		symbol = s;
		color = c;
	}

	public Card() {
		// Random card
		Random random = new Random();
		int num;

		// Get a random number or symbol
		num = random.nextInt(15);
		if (num == 10) {
			symbol = "reverse";
			number = -1;
		}
		else if (num == 11) {
			symbol = "skip";
			number = -1;
		}
		else if (num == 12) {
			symbol = "draw 2";
			number = -1;
		}
		else if (num == 13) {
			symbol = "wild";
			number = -1;
		}
		else if (num == 14) {
			symbol = "wild draw 4";
			number = -1;
		}
		else {
			number = num;
			symbol = "n/a";
		}

		// Get a random color
		num = random.nextInt(4);
		if (symbol.equals("wild") || symbol.equals("wild draw 4")) {
			color = "n/a";
		}
		else if (num == 0) {
			color = "red";
		}
		else if (num == 1) {
			color = "blue";
		}
		else if (num == 2) {
			color = "yellow";
		}
		else if (num == 3) {
			color = "green";
		}
	}

	// getter for number
	public int getNumber() {
		return number;
	}

	// getter for symbol
	public String getSymbol() {
		return symbol;
	}

	// No setters for number or symbol, card does not to be changed after instantiated. 
	
	// getter for color
	public String getColor() {
		return color;
	}

	// setter for color is only to be called to set the color of a wild card
	public void setColor(String c) {
		color = c; 
	}

	// Check if two cards are similar in number, symbol, or color
	public boolean isSimilar(Card c) {
		
		// Check for wild card
		if ((this.getSymbol()).equals("wild") || (this.getSymbol()).equals("wild draw 4")) {
			return true;
		}
		// Check for same color
		if ((this.getColor()).equals(c.getColor())) {
			return true;
		}
		// Check for same number
		if (this.getNumber() == c.getNumber() && this.getNumber() != -1) {
			return true;
		}
		// Check for same symbol
		if ((this.getSymbol()).equals(c.getSymbol()) && !((this.getSymbol()).equals("n/a"))) {
			return true;
		}

		return false;
	}

	// equals method
	@Override
	public boolean equals(Object o) {
		if (o instanceof Card) {
			Card c = (Card) o;

			if ((this.getColor()).equals(c.getColor()) && (this.getSymbol()).equals(c.getSymbol()) && this.getNumber() == c.getNumber()) {
				return true;
			}
		}

		return false;
	}
}
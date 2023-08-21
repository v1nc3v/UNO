public class Player {
	
	// instance variables
	private int numCards;
	private Card[] hand;

	// constructors
	public Player(int n) {
		numCards = n;
		hand = new Card[numCards];

		// Give player random cards
		for (int i = 0; i < hand.length; i++) {
			hand[i] = new Card();
		}
	}

	public Player() {
		this(7); // by default a player starts with 7 cards
	}

	// getter and setter for numCards
	public int getNumCards() {
		return numCards;
	}

	public void setNumCards(int n) {
		numCards = n;
	}

	// getter for hand
	public Card[] getHand() {
		return hand;
	}

	// setting a smaller hand (after playing a card)
	public void setSmallerHand(Card[] oldHand, Card old) {
		int counter = 0;
		hand = new Card[numCards];

		for (int i = 0; i < hand.length; i++) {
			// Skip over the card that was just played
			if (oldHand[i].equals(old) && counter == 0) {
				hand[i] = oldHand[i + 1];
				counter++; // Ensure that only one card from the old hand is omitted in the new hand
			}
			else {
				if (counter == 1) {
					hand[i] = oldHand[i + 1]; // Ensure all unplayed cards from the old hand is present in the new hand
				}
				else {
					hand[i] = oldHand[i];
				}
			}
		}
	}

	// setting a larger hand (after drawing a card)
	public void setLargerHand(Card[] oldHand) {
		hand = new Card[numCards];

		for (int i = 0; i < hand.length; i++) {
			if (i >= oldHand.length) {
				hand[i] = new Card(); // Add a new card to player's hand
			}
			else {
				hand[i] =  oldHand[i]; // Include all old cards in player's new hand
			}
		}
	}
}
package blackjack;

import java.util.*;

public class Blackjack implements BlackjackEngine {
	private int playerAccount; // field to keep track of a player's account balance
	private int playerBet; // field to identify the actual player's bet
	private int numberOfDecks; // field to track the numbers of decks
	private int gameStatus;
	private Random random;
	private ArrayList<Card> gameDeck;
	private ArrayList<Card> dealerDeck;
	private ArrayList<Card> playerDeck;

	/**
	 * Constructor you must provide. Initializes the player's account to 200 and the
	 * initial bet to 5. Feel free to initialize any other fields. Keep in mind that
	 * the constructor does not define the deck(s) of cards.
	 * 
	 * @param randomGenerator
	 * @param numberOfDecks
	 */

	public Blackjack(Random randomGenerator, int numberOfDecks) {
		this.playerAccount = 200; // sets the player amount at the start of a game
		this.playerBet = 5;
		this.numberOfDecks = numberOfDecks;
		this.random = randomGenerator;
	}

	public int getNumberOfDecks() {
		return numberOfDecks;
	}

	public void createAndShuffleGameDeck() { // method to make and shuffle a deck
		this.gameDeck = new ArrayList<Card>(); // creates a new deck
		for (int i = 0; i < this.numberOfDecks; i++) {
			for (CardSuit suits : CardSuit.values()) { // uses the CardSuit enumerator to organize suits
				for (CardValue values : CardValue.values()) { // uses the CardValue enumerator to organize values
					gameDeck.add(new Card(values, suits)); // adds all the cards to the deck
				}
			}
		}
		Collections.shuffle(gameDeck, random); // shuffles the game deck using the random generator
	}

	public Card[] getGameDeck() {
		return gameDeck.toArray(new Card[gameDeck.size()]); // returns the gameDeck
	}

	public void deal() {
		createAndShuffleGameDeck();
		this.dealerDeck = new ArrayList<Card>();
		this.playerDeck = new ArrayList<Card>();

		playerDeck.add(gameDeck.get(0)); // adds a card to the player deck
		gameDeck.remove(gameDeck.get(0)); // removes the top card from the game deck
		playerDeck.get(0).setFaceUp(); // sets the first playDeck card upwards

		dealerDeck.add(gameDeck.get(0)); // adds a card to the dealer deck
		gameDeck.remove(gameDeck.get(0)); // removes a card from the game deck
		dealerDeck.get(0).setFaceDown(); // sets the first dealer card down

		playerDeck.add(gameDeck.get(0)); // adds a 2nd card to the player deck
		gameDeck.remove(gameDeck.get(0)); // removes a card from the game deck
		playerDeck.get(1).setFaceUp(); // sets the second dealer card up

		dealerDeck.add(gameDeck.get(0)); // adds a 2nd card to the dealer deck
		gameDeck.remove(gameDeck.get(0)); // removes a card from the game deck
		dealerDeck.get(1).setFaceUp(); // sets the 2nd dealer card up

		gameStatus = Blackjack.GAME_IN_PROGRESS; // starts the game
		playerAccount -= playerBet; // updates the player account balance
	}

	public Card[] getDealerCards() {
		return dealerDeck.toArray(new Card[dealerDeck.size()]);
	}

	public int[] getDealerCardsTotal() {
		int firstValue = 0;
		int secondValue = 0;
		int[] array = new int[1];
		boolean hasAce = false; // checks for an ace

		for (Card c : dealerDeck) { // checks for a ACE in the whole dealerDeck
			if (c.getValue().getIntValue() == 1) {
				hasAce = true;
			}
		}
		if (hasAce) {
			for (int i = 0; i <= dealerDeck.size() - 1; i++) {
				firstValue += dealerDeck.get(i).getValue().getIntValue(); // adds up the deckValue if Ace is 1
			}
			for (int i = 0; i <= dealerDeck.size() - 1; i++) {
				if (dealerDeck.get(i).getValue().getIntValue() == 1 && secondValue + 11 <= 21) {
					secondValue += 11; // adds up a ACE as 11 if it doesn't cause a bust
				} else {
					secondValue += dealerDeck.get(i).getValue().getIntValue(); // adds values regularly (Ace =1)
				}
			}
			if (firstValue != secondValue && firstValue <= 21 && secondValue <= 21) {
				array = new int[2]; // new array if both ACE values don't cause a bust
				array[0] = firstValue; // value with ACE as 1
				array[1] = secondValue; // value with ACE as 11
			} else if (firstValue != secondValue && secondValue > 21 && firstValue <= 21) {
				array = new int[1]; // new array if the high ACE values cause a bust
				array[0] = firstValue; // value wit ACE as 1
			} else if (firstValue == secondValue && firstValue <= 21) { // considers potential of both values being the
																		// same
				array = new int[1];
				array[0] = firstValue;
			} else {
				array = null; // if all values cause a bust
			}
		} else {
			for (int i = 0; i <= dealerDeck.size() - 1; i++) {
				firstValue += dealerDeck.get(i).getValue().getIntValue(); // adds up all deckValues
			}
			if (firstValue <= 21) {
				array[0] = firstValue; // adds the deckValue if it doesn't cause a bust
			} else {
				array = null; // if all values cause a bust
			}
		}
		return array;
	}

	public int getDealerCardsEvaluation() {
		int dealerDeckValue = 0;
		boolean hasBlackjack = false; // checks a blackjack
		int evaluation = 0;

		int[] dealerTotals = getDealerCardsTotal();
		if (dealerTotals == null) { // considers a null array
			dealerDeckValue = 22; // forces a bust
		} else if (dealerTotals.length == 2) { // considers 2 ace values
			dealerDeckValue = dealerTotals[1]; // takes the highest ace value
			if (dealerDeckValue == 21 && dealerDeck.size() == 2) {
				hasBlackjack = true; // confirms blackjack
			}
		} else {
			dealerDeckValue = dealerTotals[0]; // if only 1 deckValue is possible
		}

		if (dealerDeckValue < 21) {
			evaluation = BlackjackEngine.LESS_THAN_21;
		} else if (dealerDeckValue > 21) {
			evaluation = BlackjackEngine.BUST;
		} else if (dealerDeckValue == 21 && hasBlackjack) {
			evaluation = BlackjackEngine.BLACKJACK;
		} else if (dealerDeckValue == 21 && !hasBlackjack) {
			evaluation = BlackjackEngine.HAS_21;
		}
		return evaluation;
	}

	public Card[] getPlayerCards() {
		return playerDeck.toArray(new Card[playerDeck.size()]);
	}

	public int[] getPlayerCardsTotal() {
		int firstValue = 0;
		int secondValue = 0;
		int[] array = new int[1];
		boolean hasAce = false;

		for (int i = 0; i <= playerDeck.size() - 1; i++) {
			if (playerDeck.get(i).getValue().getIntValue() == 1) {
				hasAce = true; // checks for a ace in the playerDeck
			}
		}
		if (hasAce) {
			for (int i = 0; i <= playerDeck.size() - 1; i++) {
				firstValue += playerDeck.get(i).getValue().getIntValue(); // adds values if Ace is 1
			}

			for (int i = 0; i <= playerDeck.size() - 1; i++) {
				if (playerDeck.get(i).getValue().getIntValue() == 1 && secondValue + 11 <= 21) {
					secondValue += 11; // considers if ACE can be 11 without busting
				} else {
					secondValue += playerDeck.get(i).getValue().getIntValue();// considers if ACE as 11 causes a bust
				}
			}
			if (firstValue != secondValue && firstValue <= 21 && secondValue <= 21) {
				array = new int[2];// adds 2 values if both ACE values prevent a bust
				array[0] = firstValue;
				array[1] = secondValue;
			} else if (firstValue != secondValue && secondValue > 21 && firstValue <= 21) {
				array[0] = firstValue; // adds 1 value if the high ACE causes a bust
			} else if (firstValue == secondValue && firstValue <= 21) {
				array[0] = firstValue;
			} else {
				array = null; // considers a bust for both values
			}
		} else {
			for (int i = 0; i <= playerDeck.size() - 1; i++) {
				firstValue += playerDeck.get(i).getValue().getIntValue(); // adds values regularly(No Ace)
			}
			if (firstValue <= 21) {
				array[0] = firstValue; // array set to value as long as it doesn't cause a bust
			} else {
				array = null;
			}
		}
		return array;
	}

	public int getPlayerCardsEvaluation() {
		int playerDeckValue = 0;
		int[] playerTotals = getPlayerCardsTotal();
		boolean hasBlackjack = false;
		int evaluation = 0;

		if (playerTotals == null) {
			playerDeckValue = 22; // forces a bust
		} else if (playerTotals.length == 2) {
			playerDeckValue = playerTotals[1]; // sets the deck value to the higher ACE value
			if (playerDeck.size() == 2 && playerDeckValue == 21) {
				hasBlackjack = true; // checks for blackjack
			}
		} else {
			playerDeckValue = playerTotals[0];
		}

		if (playerDeckValue < 21) {
			evaluation = BlackjackEngine.LESS_THAN_21;
		} else if (playerDeckValue > 21) {
			evaluation = BlackjackEngine.BUST;
		} else if (playerDeckValue == 21 && hasBlackjack) {
			evaluation = BlackjackEngine.BLACKJACK;
		} else if (playerDeckValue == 21 && !hasBlackjack) {
			evaluation = BlackjackEngine.HAS_21;
		}
		return evaluation;
	}

	public void playerHit() {
		int playerValue = 0;
		playerDeck.add(gameDeck.get(0));
		gameDeck.remove(gameDeck.get(0));
		int[] playerTotals = getPlayerCardsTotal();
		if (playerTotals == null) {
			playerValue = 22; // forces a dealer win
		} else if (playerTotals.length == 2) {
			playerValue = playerTotals[1]; // adds the highest possible ACE value
		} else {
			playerValue = playerTotals[0]; // adds the only value that doesn't bust
		}

		if (playerValue > 21) {
			gameStatus = BlackjackEngine.DEALER_WON;
		} else {
			gameStatus = BlackjackEngine.GAME_IN_PROGRESS;
		}
	}

	public void playerStand() {
		dealerDeck.get(0).setFaceUp();
		int playerHighestTotal = 0;
		int dealerHighestTotal = 0;
		int[] playerTotals = getPlayerCardsTotal();
		int[] dealerTotals = getDealerCardsTotal();

		while (dealerTotals != null && dealerTotals[dealerTotals.length - 1] < 16) { //runs until dealer has 16
			dealerDeck.add(gameDeck.get(0)); // adds a new card into the dealer deck
			dealerTotals = getDealerCardsTotal(); // updates the dealer cards
			gameDeck.remove(gameDeck.get(0)); // removes a card from the game deck
		}

		if (dealerTotals == null) { // considers a null dealer array
			gameStatus = BlackjackEngine.PLAYER_WON;
			playerAccount += (playerBet * 2); // provides player with his bet*2 for a win
			return;
		}

		if (dealerTotals[dealerTotals.length - 1] >= 22) {
			gameStatus = BlackjackEngine.PLAYER_WON;
			playerAccount += (playerBet * 2);
		} else {
			if (playerTotals == null) {
				gameStatus = BlackjackEngine.DEALER_WON;
			}
			if (playerTotals.length == 2) {
				playerHighestTotal = playerTotals[1]; // sets playerHighestTotal to high ace value
			} else {
				playerHighestTotal = playerTotals[0]; // sets playerHighestTotal to low ace value
			}

			if (dealerTotals.length == 2) {
				dealerHighestTotal = dealerTotals[1]; // sets dealerHighestTotal to high ace value
			} else {
				dealerHighestTotal = dealerTotals[0]; // sets dealerHighestTotal to low ace value
			}

			if (dealerHighestTotal > playerHighestTotal) {
				gameStatus = BlackjackEngine.DEALER_WON;
			}
			if (dealerHighestTotal < playerHighestTotal) {
				gameStatus = BlackjackEngine.PLAYER_WON;
				playerAccount += (playerBet * 2);
			}
			if (dealerHighestTotal == playerHighestTotal) {
				gameStatus = BlackjackEngine.DRAW;
				playerAccount += playerBet;
			}
		}
	}

	public int getGameStatus() {
		return gameStatus;
	}

	public void setBetAmount(int amount) {
		this.playerBet = amount;
	}

	public int getBetAmount() {
		return playerBet;
	}

	public void setAccountAmount(int amount) {
		this.playerAccount = amount;
	}

	public int getAccountAmount() {
		return playerAccount;
	}

	/* Feel Free to add any private methods you might need */
}
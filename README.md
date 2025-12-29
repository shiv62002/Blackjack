# Blackjack Game Simulator

## Project Description
This project simulates a game of Blackjack using Java and a graphical user interface to visually display cards on the screen. A random number generator is used to create and shuffle a deck of cards, and the resulting randomized values are passed into two separate ENUMs representing card suits and card values. These ENUMs assign attributes to each card in the deck, which are then dealt into player and dealer hands. Each card is read into the system, processed, and evaluated to determine the outcome of the game. The process repeats for every new round based on user input, allowing multiple consecutive games.

## How It Works
- A **random number generator** produces values used to shuffle and construct the deck.
- Two **ENUMs** define:
  - card **suits** (e.g., Hearts, Clubs, Diamonds, Spades)
  - card **values** (e.g., Ace, numeric values, face cards)
- These ENUMs are used to create and assign values to each card object.
- A **GUI interface** displays dealt cards and allows the user to play Blackjack visually.
- A **reader component** processes cards in a player's hand and calculates their Blackjack value.
- Game logic compares the values of the player and dealer hands to determine win, loss, or draw.
- The program repeats this process whenever the user requests a new round.

## Skills Demonstrated
- Java-based GUI development
- Random number generation and card shuffling
- ENUM usage for structured card representation
- Scanner usage and input handling
- JUnit testing to validate code efficiency and correctness
- Core Blackjack game logic and decision flow

## Key Takeaways
This project highlights my ability to:
- build interactive Java applications with GUI components  
- utilize enums to organize and represent structured data  
- apply randomness to simulate real-world behavior (shuffling)  
- read and process card objects to compute hand values  
- perform repeated game logic based on user input  
- write maintainable and testable code using JUnit

## Example Gameplay Flow
1. Program initializes GUI and generates a shuffled deck.
2. Player and dealer are each dealt cards.
3. Card values are calculated and compared.
4. GUI displays results (win/lose/draw).
5. User selects "Play Again" to repeat the process.

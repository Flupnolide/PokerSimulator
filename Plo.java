
import java.util.List;
import java.util.ArrayList;
import java.util.Stack;
import java.util.Scanner;
import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Iterator;
import java.util.Set;
import java.util.*;
import java.util.Scanner;
import java.util.Random;
import java.lang.Math;
import java.security.SecureRandom;
	
public class Plo {

	public HashMap<String, HandType> createMapOfFiveCardHandsFullDeck() {
		HashMap<String, HandType> map = new HashMap<String, HandType>();
		TypeOfHandEvaluation eval = new TypeOfHandEvaluation();
		ArrayList<Card> deck = eval.loadDeck();
		for (int i = 1; i <= 48; i++) {
			for (int j = i + 1; j <= 49; j++ ) {
				for (int k = j + 1; k <= 50; k++ ) {
					for (int l = k + 1; l <= 51; l++ ) {
						for (int m = l + 1; m <= 52; m++ ) {
							Card[] boardListForSim = new Card[5];
							boardListForSim[0] = deck.get(i-1);
							boardListForSim[1] = deck.get(j-1);
							boardListForSim[2] = deck.get(k-1);
							boardListForSim[3] = deck.get(l-1);
							boardListForSim[4] = deck.get(m-1);

							HandType handTester = eval.bestHand(boardListForSim, 13);
							boardListForSim = eval.sortHandBySuit(boardListForSim);
							boardListForSim = eval.sortHandByRank(boardListForSim);

							map.put(eval.toStringHand(boardListForSim), handTester);
						}
					}
				}
			}
		}

		//FileOutputStream fos = new FileOutputStream( '/Users/kids/desktop/HuBot/hands.txt' );
		//fos.writeBytes( map.)

		return map;
	}

	public HandType getBestPLOHand(Hand ploFourCards, Card[] board, HashMap<String, HandType> map) {
		TypeOfHandEvaluation eval = new TypeOfHandEvaluation();
		Card[] ploHand = ploFourCards.getArrayOfPLOCards();
		HandType currentBestHand = null; 
		for (int i = 0; i <= 2; i++) {
			for (int j = i + 1; j <= 3; j++ ) {
				for (int k = 0; k <= 2; k++ ) {
					for (int l = k + 1; l <= 3; l++ ) {
						for (int m = l + 1; m <= 4; m++ ) {
							Card[] fiveCardsToCheckInMap = new Card[5];
							fiveCardsToCheckInMap[0] = ploHand[i];
							fiveCardsToCheckInMap[1] = ploHand[j];
							fiveCardsToCheckInMap[2] = board[k];
							fiveCardsToCheckInMap[3] = board[l];
							fiveCardsToCheckInMap[4] = board[m];
							fiveCardsToCheckInMap = eval.sortHandBySuit(fiveCardsToCheckInMap);
							fiveCardsToCheckInMap = eval.sortHandByRank(fiveCardsToCheckInMap);
							if (currentBestHand == null) {
								currentBestHand = map.get(eval.toStringHand(fiveCardsToCheckInMap)); 
							}
							else {						
								HandType hand = map.get(eval.toStringHand(fiveCardsToCheckInMap));
								if (currentBestHand.compareHandsFullDeck( hand, 13 ) == 2) {
									currentBestHand = hand;
								}
							} 
						}
					}
				}
			}
		}
		return currentBestHand;
	}

		//pass in a deck + give the number of cards needed.
	public Card[] drawXNumberOfCards(ArrayList<Card> deck, int numberOfCards) {
		Card[] hand = new Card[numberOfCards];
		for (int i = 0; i < numberOfCards; i++) {
			Random rand = new SecureRandom();
			int cardNumber = rand.nextInt(deck.size());
			hand[i] = deck.get(cardNumber);
			deck.remove(cardNumber);
		}
		return hand;
	}

	public double simulatePLOHandEquityVsRange(Hand hand, int numberOfOpponents, HashMap<String, HandType> map) {
		TypeOfHandEvaluation eval = new TypeOfHandEvaluation();
		ArrayList<Card> deck = eval.loadDeck();

		for (int i = deck.size() - 1; i >= 0; i--) {
			if (deck.get(i).isEqual(hand.getCardOne()) || deck.get(i).isEqual(hand.getCardTwo()) ||
				deck.get(i).isEqual(hand.getCardThree()) || deck.get(i).isEqual(hand.getCardFour())) {
				deck.remove(i);
			}
		}

		//creating new hands 
		ArrayList<Card[]> otherHands = new ArrayList<Card[]>();
		for (int i = 0; i < numberOfOpponents; i++) {
			otherHands.add(drawXNumberOfCards(deck, 4));
		}

		Card[] board = drawXNumberOfCards(deck, 5);

		ArrayList<HandType> opponentsBestHands = new ArrayList<HandType>();
		for (int i = 0; i < numberOfOpponents; i++) {
			Hand tempHand = new Hand(otherHands.get(i)[0], otherHands.get(i)[1], otherHands.get(i)[2], otherHands.get(i)[3]);
			opponentsBestHands.add(getBestPLOHand(tempHand, board, map));
		}

		HandType simulatedHand = getBestPLOHand(hand, board, map);

		boolean isTie = false;
		double numberOfPeopleToSplitThePot = 1;
		for (int i = 0; i < opponentsBestHands.size(); i++) {
			int comparisonNumber = simulatedHand.compareHandsFullDeck(opponentsBestHands.get(i), 13);
			if (comparisonNumber == 2) {
				return 0;
			}
			if (comparisonNumber == 0) {
				isTie = true;
				numberOfPeopleToSplitThePot++;
			}
		}

		if (isTie) {
			return 1/numberOfPeopleToSplitThePot;
		}
		else {
			return 1;
		}
		

	}

	public int simTwoPLOHands(Hand handOne, Hand handTwo, HashMap<String, HandType> map) {
		TypeOfHandEvaluation eval = new TypeOfHandEvaluation();
		ArrayList<Card> deck = eval.loadDeck();
		Card[] board = new Card[5];

		//delete the given hands from deck.
		for (int i = deck.size() - 1; i >= 0; i--) {
			if (deck.get(i).isEqual(handOne.getCardOne()) || deck.get(i).isEqual(handOne.getCardTwo()) ||
				deck.get(i).isEqual(handTwo.getCardOne()) || deck.get(i).isEqual(handTwo.getCardTwo()) ||
				deck.get(i).isEqual(handOne.getCardThree()) || deck.get(i).isEqual(handOne.getCardFour()) ||
				deck.get(i).isEqual(handTwo.getCardThree()) || deck.get(i).isEqual(handTwo.getCardFour())) {
				deck.remove(i);
			}
		}

		for (int i = 0; i < 5; i++) {
			Random rand = new SecureRandom();
			int number = rand.nextInt(deck.size());
			board[i] = deck.get(number);
			deck.remove(number);
		}

		HandType hand1 = getBestPLOHand(handOne, board, map);
		HandType hand2 = getBestPLOHand(handTwo, board, map);

		return hand1.compareHandsFullDeck(hand2, 13);
	}

}
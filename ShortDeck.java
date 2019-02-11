
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
	
public class ShortDeck {

	public ArrayList<Card> loadShortDeck() {
		ArrayList<Card> deck = new ArrayList<Card>();
		for (int i = 5; i <= 13; i++) {
			for (int j = 1; j <= 4; j++) {
				Card temp = new Card(j, i);
				temp.convertCardToShortDeck();
				deck.add(temp);
			}
		}
		return deck;
	}

	//handOne is ALREADY a shortDeck hand.
	public int simulateAHandVsRangeForShortDeckOne(Hand handOne, int numberOfOpponents) {
		ArrayList<Card> deck = loadShortDeck();
		Card[] completeBoardList = new Card[7];
		ArrayList<Card[]> otherHands = new ArrayList<Card[]>();

		for (int i = 0; i < numberOfOpponents; i++) {
			otherHands.add(new Card[7]);
		}

		completeBoardList[0] = handOne.getCardOne();
		completeBoardList[1] = handOne.getCardTwo();

		//delete the given hand from deck.
		for (int i = deck.size() - 1; i >= 0; i--) {
			if (deck.get(i).isEqual(handOne.getCardOne()) || deck.get(i).isEqual(handOne.getCardTwo())) {
				deck.remove(i);
			}
		}

		//populate all the opps with hands.
		for (int i = 0; i < otherHands.size(); i++) {
			Random rand1 = new SecureRandom();
			int number1 = rand1.nextInt(deck.size());
			otherHands.get(i)[0] = deck.get(number1);
			deck.remove(number1);
			Random rand2 = new SecureRandom();
			int number2 = rand1.nextInt(deck.size());
			otherHands.get(i)[1] = deck.get(number2);
			deck.remove(number2);
		}

		//creating flop
		for (int i = 0; i < 5; i++) {
			Random rand = new SecureRandom();
			int number = rand.nextInt(deck.size());
			completeBoardList[2 + i] = deck.get(number);
			for (int j = 0; j < otherHands.size(); j++) {
				otherHands.get(j)[2 + i] = deck.get(number);
			}
			deck.remove(number);
		}

		HandType hand1 = bestHandShortDeckOne(completeBoardList, 9);

		boolean isTie = false;
		for (int i = 0; i < otherHands.size(); i++) {
			int comparisonNumber = hand1.compareHandsShortDeckOne(bestHandShortDeckOne(otherHands.get(i), 9), 9);
			if (comparisonNumber == 2) {
				return comparisonNumber;
			}
			if (comparisonNumber == 0) {
				isTie = true;
			}
		}

		if (isTie) {
			return 0;
		}
		else {
			return 1;
		}
	}

	//handOne is ALREADY a shortDeck hand.
	public double simulateAHandEquityVsRangeForShortDeckOne(Hand handOne, int numberOfOpponents) {
		ArrayList<Card> deck = loadShortDeck();
		Card[] completeBoardList = new Card[7];
		ArrayList<Card[]> otherHands = new ArrayList<Card[]>();

		for (int i = 0; i < numberOfOpponents; i++) {
			otherHands.add(new Card[7]);
		}

		completeBoardList[0] = handOne.getCardOne();
		completeBoardList[1] = handOne.getCardTwo();

		//delete the given hand from deck.
		for (int i = deck.size() - 1; i >= 0; i--) {
			if (deck.get(i).isEqual(handOne.getCardOne()) || deck.get(i).isEqual(handOne.getCardTwo())) {
				deck.remove(i);
			}
		}

		//populate all the opps with hands.
		for (int i = 0; i < otherHands.size(); i++) {
			Random rand1 = new SecureRandom();
			int number1 = rand1.nextInt(deck.size());
			otherHands.get(i)[0] = deck.get(number1);
			deck.remove(number1);
			Random rand2 = new SecureRandom();
			int number2 = rand1.nextInt(deck.size());
			otherHands.get(i)[1] = deck.get(number2);
			deck.remove(number2);
		}

		//creating flop
		for (int i = 0; i < 5; i++) {
			Random rand = new SecureRandom();
			int number = rand.nextInt(deck.size());
			completeBoardList[2 + i] = deck.get(number);
			for (int j = 0; j < otherHands.size(); j++) {
				otherHands.get(j)[2 + i] = deck.get(number);
			}
			deck.remove(number);
		}

		HandType hand1 = bestHandShortDeckOne(completeBoardList, 9);

		boolean isTie = false;
		double numberOfPeopleToSplitThePot = 1;
		for (int i = 0; i < otherHands.size(); i++) {
			int comparisonNumber = hand1.compareHandsShortDeckOne(bestHandShortDeckOne(otherHands.get(i), 9), 9);
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

	//handOne is ALREADY a shortDeck hand.
	public int simulateAHandVsRangeForShortDeckTwo(Hand handOne, int numberOfOpponents) {
		ArrayList<Card> deck = loadShortDeck();
		Card[] completeBoardList = new Card[7];
		ArrayList<Card[]> otherHands = new ArrayList<Card[]>();

		for (int i = 0; i < numberOfOpponents; i++) {
			otherHands.add(new Card[7]);
		}

		completeBoardList[0] = handOne.getCardOne();
		completeBoardList[1] = handOne.getCardTwo();

		//delete the given hand from deck.
		for (int i = deck.size() - 1; i >= 0; i--) {
			if (deck.get(i).isEqual(handOne.getCardOne()) || deck.get(i).isEqual(handOne.getCardTwo())) {
				deck.remove(i);
			}
		}

		//populate all the opps with hands.
		for (int i = 0; i < otherHands.size(); i++) {
			Random rand1 = new SecureRandom();
			int number1 = rand1.nextInt(deck.size());
			otherHands.get(i)[0] = deck.get(number1);
			deck.remove(number1);
			Random rand2 = new SecureRandom();
			int number2 = rand1.nextInt(deck.size());
			otherHands.get(i)[1] = deck.get(number2);
			deck.remove(number2);
		}

		//creating flop
		for (int i = 0; i < 5; i++) {
			Random rand = new SecureRandom();
			int number = rand.nextInt(deck.size());
			completeBoardList[2 + i] = deck.get(number);
			for (int j = 0; j < otherHands.size(); j++) {
				otherHands.get(j)[2 + i] = deck.get(number);
			}
			deck.remove(number);
		}

		HandType hand1 = bestHandShortDeckTwo(completeBoardList, 9);

		boolean isTie = false;
		for (int i = 0; i < otherHands.size(); i++) {
			int comparisonNumber = hand1.compareHandsShortDeckTwo(bestHandShortDeckTwo(otherHands.get(i), 9), 9);
			if (comparisonNumber == 2) {
				return comparisonNumber;
			}
			if (comparisonNumber == 0) {
				isTie = true;
			}
		}

		if (isTie) {
			return 0;
		}
		else {
			return 1;
		}
	}

	//handOne is ALREADY a shortDeck hand.
	public double simulateAHandEquityVsRangeForShortDeckTwo(Hand handOne, int numberOfOpponents) {
		ArrayList<Card> deck = loadShortDeck();
		Card[] completeBoardList = new Card[7];
		ArrayList<Card[]> otherHands = new ArrayList<Card[]>();

		for (int i = 0; i < numberOfOpponents; i++) {
			otherHands.add(new Card[7]);
		}

		completeBoardList[0] = handOne.getCardOne();
		completeBoardList[1] = handOne.getCardTwo();

		//delete the given hand from deck.
		for (int i = deck.size() - 1; i >= 0; i--) {
			if (deck.get(i).isEqual(handOne.getCardOne()) || deck.get(i).isEqual(handOne.getCardTwo())) {
				deck.remove(i);
			}
		}

		//populate all the opps with hands.
		for (int i = 0; i < otherHands.size(); i++) {
			Random rand1 = new SecureRandom();
			int number1 = rand1.nextInt(deck.size());
			otherHands.get(i)[0] = deck.get(number1);
			deck.remove(number1);
			Random rand2 = new SecureRandom();
			int number2 = rand1.nextInt(deck.size());
			otherHands.get(i)[1] = deck.get(number2);
			deck.remove(number2);
		}

		//creating flop
		for (int i = 0; i < 5; i++) {
			Random rand = new SecureRandom();
			int number = rand.nextInt(deck.size());
			completeBoardList[2 + i] = deck.get(number);
			for (int j = 0; j < otherHands.size(); j++) {
				otherHands.get(j)[2 + i] = deck.get(number);
			}
			deck.remove(number);
		}

		HandType hand1 = bestHandShortDeckTwo(completeBoardList, 9);

		boolean isTie = false;
		double numberOfPeopleToSplitThePot = 1;
		for (int i = 0; i < otherHands.size(); i++) {
			int comparisonNumber = hand1.compareHandsShortDeckTwo(bestHandShortDeckTwo(otherHands.get(i), 9), 9);
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

	//hands must be already coverted to shortdeck
	// 1 = hand 1 wins, 2 = hand 2 wins, 0 = tie.
	public int simTwoHandsShortDeckOne(Hand handOne, Hand handTwo) {
		ArrayList<Card> deck = loadShortDeck();

		Card[] completeBoardListOne = new Card[7];
		Card[] completeBoardListTwo = new Card[7];

		completeBoardListOne[0] = handOne.getCardOne();
		completeBoardListOne[1] = handOne.getCardTwo();

		completeBoardListTwo[0] = handTwo.getCardOne();
		completeBoardListTwo[1] = handTwo.getCardTwo();

		//delete the given hand from deck.
		for (int i = deck.size() - 1; i >= 0; i--) {
			if (deck.get(i).isEqual(handOne.getCardOne()) || deck.get(i).isEqual(handOne.getCardTwo()) ||
				deck.get(i).isEqual(handTwo.getCardOne()) || deck.get(i).isEqual(handTwo.getCardTwo())) {
				deck.remove(i);
			}
		}

		for (int i = 0; i < 5; i++) {
			Random rand = new SecureRandom();
			int number = rand.nextInt(deck.size());
			completeBoardListOne[2 + i] = deck.get(number);
			completeBoardListTwo[2 + i] = deck.get(number);
			deck.remove(number);
		}

		HandType hand1 = bestHandShortDeckOne(completeBoardListOne, 9);
		HandType hand2 = bestHandShortDeckOne(completeBoardListTwo, 9);

		return hand1.compareHandsShortDeckOne(hand2, 9);
	}

	//hands must be already coverted to shortdeck
	// 1 = hand 1 wins, 2 = hand 2 wins, 0 = tie.
	public int simTwoHandsShortDeckTwo(Hand handOne, Hand handTwo) {
		ArrayList<Card> deck = loadShortDeck();

		Card[] completeBoardListOne = new Card[7];
		Card[] completeBoardListTwo = new Card[7];

		completeBoardListOne[0] = handOne.getCardOne();
		completeBoardListOne[1] = handOne.getCardTwo();

		completeBoardListTwo[0] = handTwo.getCardOne();
		completeBoardListTwo[1] = handTwo.getCardTwo();

		//delete the given hand from deck.
		for (int i = deck.size() - 1; i >= 0; i--) {
			if (deck.get(i).isEqual(handOne.getCardOne()) || deck.get(i).isEqual(handOne.getCardTwo()) ||
				deck.get(i).isEqual(handTwo.getCardOne()) || deck.get(i).isEqual(handTwo.getCardTwo())) {
				deck.remove(i);
			}
		}

		for (int i = 0; i < 5; i++) {
			Random rand = new SecureRandom();
			int number = rand.nextInt(deck.size());
			completeBoardListOne[2 + i] = deck.get(number);
			completeBoardListTwo[2 + i] = deck.get(number);
			deck.remove(number);
		}

		HandType hand1 = bestHandShortDeckTwo(completeBoardListOne, 9);
		HandType hand2 = bestHandShortDeckTwo(completeBoardListTwo, 9);

		return hand1.compareHandsShortDeckTwo(hand2, 9);
	}

	//for shortdeck
	public HandType bestHandShortDeckOne( Card[] completeBoardList, int highestRankInDeck ) {
		TypeOfHandEvaluation handEval = new TypeOfHandEvaluation();
		completeBoardList = handEval.sortHandByRank(completeBoardList);
		Card[] hand = null;
		if ( completeBoardList.length >= 5) {
			hand = new Card[5];
		}
		else {
			hand = new Card[completeBoardList.length];
		}

		if ( handEval.isStraightFlush(completeBoardList, highestRankInDeck)) {
			return handEval.createStraightFlush(completeBoardList, highestRankInDeck);
		}

		//Determining if quads
		if ( handEval.numberOfOccurencesMostPopularCardAppears(completeBoardList) == 4) {
			return handEval.createQuads(completeBoardList, hand);
		}

		//Determining if flush
		if ( handEval.numberOfOccurencesOfHighestSuit(completeBoardList) >= 5 ) {
			return handEval.createFlush(completeBoardList, hand);
		}

		//Determine if boat
		if ( handEval.numberOfOccurencesMostPopularCardAppears(completeBoardList) == 3 && handEval.countNumberOfPairs(completeBoardList) >= 2) {
			return handEval.createTripsOrBoat(completeBoardList, hand);
		}

		//Determing if straight
		if (  handEval.countNumberOfCardsInARow(completeBoardList, highestRankInDeck) >= 5 ) {
			return handEval.createStraight(completeBoardList, highestRankInDeck);
		}

		//Determing if trips
		if ( handEval.numberOfOccurencesMostPopularCardAppears(completeBoardList) == 3) {
			return handEval.createTripsOrBoat(completeBoardList, hand);
		}

		//Determing if one/two pair
		if ( handEval.numberOfOccurencesMostPopularCardAppears(completeBoardList) == 2) {
			return handEval.createOneOrTwoPair(completeBoardList, hand);
		}

		//Determining if the board is a high card;
		if ( handEval.isHighCard(completeBoardList) ) {
			return handEval.createHighCardHand(completeBoardList, hand);
		} 


		return null;

	}

	//for shortdeck
	public HandType bestHandShortDeckTwo( Card[] completeBoardList, int highestRankInDeck ) {
		TypeOfHandEvaluation handEval = new TypeOfHandEvaluation();
		completeBoardList = handEval.sortHandByRank(completeBoardList);
		Card[] hand = null;
		if ( completeBoardList.length >= 5) {
			hand = new Card[5];
		}
		else {
			hand = new Card[completeBoardList.length];
		}

		if ( handEval.isStraightFlush(completeBoardList, highestRankInDeck)) {
			return handEval.createStraightFlush(completeBoardList, highestRankInDeck);
		}

		//Determining if quads
		if ( handEval.numberOfOccurencesMostPopularCardAppears(completeBoardList) == 4) {
			return handEval.createQuads(completeBoardList, hand);
		}

		//Determining if flush
		if ( handEval.numberOfOccurencesOfHighestSuit(completeBoardList) >= 5 ) {
			return handEval.createFlush(completeBoardList, hand);
		}

		//Determine if boat
		if ( handEval.numberOfOccurencesMostPopularCardAppears(completeBoardList) == 3 && handEval.countNumberOfPairs(completeBoardList) >= 2) {
			return handEval.createTripsOrBoat(completeBoardList, hand);
		}

		//Determing if trips
		if ( handEval.numberOfOccurencesMostPopularCardAppears(completeBoardList) == 3) {
			return handEval.createTripsOrBoat(completeBoardList, hand);
		}

		//Determing if straight
		if (  handEval.countNumberOfCardsInARow(completeBoardList, highestRankInDeck) >= 5 ) {
			return handEval.createStraight(completeBoardList, highestRankInDeck);
		}

		//Determing if one/two pair
		if ( handEval.numberOfOccurencesMostPopularCardAppears(completeBoardList) == 2) {
			return handEval.createOneOrTwoPair(completeBoardList, hand);
		}

		//Determining if the board is a high card;
		if ( handEval.isHighCard(completeBoardList) ) {
			return handEval.createHighCardHand(completeBoardList, hand);
		} 

		return null;

	}
}
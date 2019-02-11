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
	
public class HandStrengthEvaluator {

	private Random RANDOM = new SecureRandom();

	public void dispOrderedHand(Card[] completeBoardList) {
		for (int i = 0; i < completeBoardList.length; i++) {
			System.out.println(completeBoardList[i].toStringFullDeck());
		}
	}

	public int compare(SpecialHand handOne, SpecialHand handTwo) {
		return handOne.compareTo(handTwo);
	}

	//returns 0 to 1 depending on win. NOT 0 or 1.
	public double simulateAHandEquityVsRangeForFullDeck(Hand handOne, int numberOfOpponents) {
		TypeOfHandEvaluation eval = new TypeOfHandEvaluation();
		ArrayList<Card> deck = eval.loadDeck();
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
			int number1 = RANDOM.nextInt(deck.size());
			otherHands.get(i)[0] = deck.get(number1);
			deck.remove(number1);
			int number2 = RANDOM.nextInt(deck.size());
			otherHands.get(i)[1] = deck.get(number2);
			deck.remove(number2);
		}

		//creating flop
		for (int i = 0; i < 5; i++) {
			int number = RANDOM.nextInt(deck.size());
			completeBoardList[2 + i] = deck.get(number);
			for (int j = 0; j < otherHands.size(); j++) {
				otherHands.get(j)[2 + i] = deck.get(number);
			}
			deck.remove(number);
		}

		HandType hand1 = eval.bestHand(completeBoardList, 13);

		boolean isTie = false;
		double numberOfPeopleToSplitThePot = 1;
		for (int i = 0; i < otherHands.size(); i++) {
			int comparisonNumber = hand1.compareHandsFullDeck(eval.bestHand(otherHands.get(i), 13), 13);
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

 	// map of hands WITH % of range needs to be done beforehand, this maximizes the efficiency
	public double simEquityVsPercentageOfRange(Hand handOne, int numberOfOpponents, HashMap<String, SpecialHand> mapOfHands) {

		TypeOfHandEvaluation eval = new TypeOfHandEvaluation();
		ArrayList<Card> deck = eval.loadDeck();
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

		for (int i = 0; i < otherHands.size(); i++) {
			boolean isCardInRangeFound = false;

			while (!isCardInRangeFound) {
				int number1 = RANDOM.nextInt(deck.size());
				Card cardOne = deck.get(number1);
				deck.remove(number1);
				int number2 = RANDOM.nextInt(deck.size());
				Card cardTwo = deck.get(number2);
				deck.remove(number2);
				Hand hand = new Hand(cardOne, cardTwo);
				if (mapOfHands.get(hand.convertHandToSpecialHand().getName()) != null) {
					isCardInRangeFound = true;
					otherHands.get(i)[0] = cardOne;
					otherHands.get(i)[1] = cardTwo;
				}
				else {
					deck.add(cardOne);
					deck.add(cardTwo);
				}
			}
		}

		//creating flop
		for (int i = 0; i < 5; i++) {
			int number = RANDOM.nextInt(deck.size());
			completeBoardList[2 + i] = deck.get(number);
			for (int j = 0; j < otherHands.size(); j++) {
				otherHands.get(j)[2 + i] = deck.get(number);
			}
			deck.remove(number);
		}

		HandType hand1 = eval.bestHand(completeBoardList, 13);

		boolean isTie = false;
		double numberOfPeopleToSplitThePot = 1;
		for (int i = 0; i < otherHands.size(); i++) {
			int comparisonNumber = hand1.compareHandsFullDeck(eval.bestHand(otherHands.get(i), 13), 13);
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

	public HashMap<String, SpecialHand> convertListToMapOfHands(ArrayList<SpecialHand> listOfHands) {
		HashMap<String, SpecialHand> mapOfHands = new HashMap<String, SpecialHand>();
		for (int i = 0; i < listOfHands.size(); i++) {
			mapOfHands.put(listOfHands.get(i).getName(), listOfHands.get(i));
		}

		return mapOfHands;
	}

	public ArrayList<SpecialHand> getTopPercentageOfHands(double percentRangeOfHands, ArrayList<SpecialHand> listOfHands) {
		//Always 1326 combos
		HandStrengthEvaluator hse = new HandStrengthEvaluator();
		listOfHands = hse.sortHandList(listOfHands); // Sorting just in case, should already be sorted
		double numberOfCombosToAdd = percentRangeOfHands * 1326 / 100;
		double totalNumberOfCombosAdded = 0;
		ArrayList<SpecialHand> newRange = new ArrayList<SpecialHand>();
		for (int i = 0; i < listOfHands.size(); i++) {
			totalNumberOfCombosAdded = totalNumberOfCombosAdded + listOfHands.get(i).getNumberOfCombinations();
			if (totalNumberOfCombosAdded < numberOfCombosToAdd) {
				newRange.add(listOfHands.get(i));
			}
			else {
				break;
			}
		}

		return newRange;
	} 

	public int simTwoHandsFullDeck(Hand handOne, Hand handTwo) {
		TypeOfHandEvaluation eval = new TypeOfHandEvaluation();
		ArrayList<Card> deck = eval.loadDeck();

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
			int number = RANDOM.nextInt(deck.size());
			completeBoardListOne[2 + i] = deck.get(number);
			completeBoardListTwo[2 + i] = deck.get(number);
			deck.remove(number);
		}

		HandType hand1 = eval.bestHand(completeBoardListOne, 13);
		HandType hand2 = eval.bestHand(completeBoardListTwo, 13);

		return hand1.compareHandsFullDeck(hand2, 13);
	}

	public ArrayList<SpecialHand> sortHandList(ArrayList<SpecialHand> listOfHands) {
		ArrayList<SpecialHand>  sortedList = new ArrayList<SpecialHand>();
		ArrayList<SpecialHand>  listOfHandsDupe = new ArrayList<SpecialHand>();
		double currentHighestEquity = listOfHands.get(0).getEquity();
		int placeInArrayOfEquity = 0;
		int sizeOfList = listOfHands.size();

		for (int i = 0; i < sizeOfList; i++) {
			listOfHandsDupe.add(listOfHands.get(i));
		}

		for (int i = 0; i < sizeOfList; i++) {
			for (int j = 0; j < listOfHandsDupe.size(); j++) {
				if ( listOfHandsDupe.get(j).getEquity() > currentHighestEquity ) {
					currentHighestEquity = listOfHandsDupe.get(j).getEquity();
					placeInArrayOfEquity = j;
				}
			}
			sortedList.add(listOfHandsDupe.get(placeInArrayOfEquity));
			listOfHandsDupe.remove(placeInArrayOfEquity);
			if (listOfHandsDupe.size() > 0) {
				currentHighestEquity = listOfHandsDupe.get(0).getEquity();
				placeInArrayOfEquity = 0;
			}
		}

		return sortedList;
	}

	public ArrayList<SpecialHand> getListOfHandRankings(String filename) throws Exception {
		      String fileName = filename;

              String line = null;

              FileReader fileReader = new FileReader(fileName);

              BufferedReader bufferedReader = new BufferedReader(fileReader);

              ArrayList<SpecialHand> handRankings = new ArrayList<SpecialHand>();

              int equity = 200;
              while ( (line = bufferedReader.readLine()) != null ) {
                     String[] parts = line.split(",");
                     handRankings.add(new SpecialHand(parts[0], equity));
                     equity--;
              }
         return handRankings;
	}


	public static void main(String[] args) throws Exception {
		
	//	long startTime = System.getTimeMillis();

		TypeOfHandEvaluation typeOfHE = new TypeOfHandEvaluation();
		/*
		FileInputStream fis = new FileInputStream("/Users/kids/desktop/HuBot/hands.txt");
		ObjectInputStream ois = new ObjectInputStream(fis);
		HashMap map = (HashMap) ois.readObject();
		ois.close(); */

	//	System.out.println("TIME: " + ( System.getTimeMillis() - startTime ) );

		HandStrengthEvaluator hse = new HandStrengthEvaluator();

		ArrayList<SpecialHand> listOf169Hands = hse.getListOfHandRankings("169handRankings.txt");

		ArrayList<SpecialHand> listOf169HandsT50 = new ArrayList<SpecialHand>();

		ArrayList<SpecialHand> listOf169HandsT80 = new ArrayList<SpecialHand>();

		ArrayList<SpecialHand> listOf169HandsT50Printable = new ArrayList<SpecialHand>();

		ArrayList<SpecialHand> listOf169HandsT80Printable = new ArrayList<SpecialHand>();

		ArrayList<SpecialHand> listOf169HandsForAllPercents = new ArrayList<SpecialHand>();
		

		int NUMBER_OF_SIMS = 500000; 
		/*
		for (int i = 1; i <= 13; i++) {
			for (int j = 1; j <= 13; j++) {
				Hand hand1 = null;
				String name = null;
				if (i == j) {
					Card cardOne = new Card(1, i);
					Card cardTwo = new Card(2, j);
					hand1 = new Hand(cardOne, cardTwo);
					name = cardOne.singleCharFormat() + cardTwo.singleCharFormat();
				}
				else if (i < j) {
					Card cardOne = new Card(1, i);
					Card cardTwo = new Card(1, j);
					hand1 = new Hand(cardOne, cardTwo);
					name = cardTwo.singleCharFormat() + cardOne.singleCharFormat() + "s";
				}
				else {
					Card cardOne = new Card(1, i);
					Card cardTwo = new Card(2, j);
					hand1 = new Hand(cardOne, cardTwo);
					name = cardOne.singleCharFormat() + cardTwo.singleCharFormat() + "o";
				}
				double handEquity = 0;

				for (int k = 0; k < NUMBER_OF_SIMS; k++) {
					handEquity = handEquity + hse.simulateAHandEquityVsRangeForFullDeck(hand1, 1);
				}
				listOf169Hands.add( new SpecialHand(name, handEquity/NUMBER_OF_SIMS*100) );
			}
		}

		listOf169Hands = hse.sortHandList(listOf169Hands);
		
		for (int i = 0; i < 169; i++) {
			listOf169HandsT50.add(listOf169Hands.get(i));
			listOf169HandsT80.add(listOf169Hands.get(i));
		} 

		for (int i = 0; i < listOf169Hands.size(); i++) {
			System.out.println(listOf169Hands.get(i).getName() + "," + listOf169Hands.get(i).getEquity() + "," + 
							   listOf169Hands.get(i).getNumberOfCombinations());
		} */
	

		
		HashMap<String, SpecialHand> mapOfHands = new HashMap<String, SpecialHand>();

		for (int percent = 15; percent < 20; percent++) {
			int percentOfHands = 100 - (percent * 5);

			mapOfHands = hse.convertListToMapOfHands(hse.getTopPercentageOfHands(percentOfHands, listOf169Hands));

			for (int i = 1; i <= 13; i++) {
				for (int j = 1; j <= 13; j++) {
					Hand hand1 = null;
					String name = null;
					if (i == j) {
						Card cardOne = new Card(1, i);
						Card cardTwo = new Card(2, j);
						hand1 = new Hand(cardOne, cardTwo);
						name = cardOne.singleCharFormat() + cardTwo.singleCharFormat();
					}
					else if (i < j) {
						Card cardOne = new Card(1, i);
						Card cardTwo = new Card(1, j);
						hand1 = new Hand(cardOne, cardTwo);
						name = cardTwo.singleCharFormat() + cardOne.singleCharFormat() + "s";
					}
					else {
						Card cardOne = new Card(1, i);
						Card cardTwo = new Card(2, j);
						hand1 = new Hand(cardOne, cardTwo);
						name = cardOne.singleCharFormat() + cardTwo.singleCharFormat() + "o";
					}
					double handEquity = 0;

					for (int k = 0; k < NUMBER_OF_SIMS; k++) {
						handEquity = handEquity + hse.simEquityVsPercentageOfRange(hand1, 1, mapOfHands);
					}
					listOf169HandsForAllPercents.add( new SpecialHand(name, handEquity/NUMBER_OF_SIMS*100) );
				}
			}

			listOf169HandsForAllPercents = hse.sortHandList(listOf169HandsForAllPercents);

			System.out.println("------------- Top " + percentOfHands +  " Percent ---------------");
			for (int i = 0; i < listOf169HandsForAllPercents.size(); i++) {
				System.out.println(listOf169HandsForAllPercents.get(i).getName() + "," + listOf169HandsForAllPercents.get(i).getEquity() + "," + 
							   	   listOf169HandsForAllPercents.get(i).getNumberOfCombinations());
			}

			listOf169HandsForAllPercents.clear();
		}


		
		/*
		for (int cycle = 0; cycle < 20; cycle++) {
			mapOfHands = hse.convertListToMapOfHands(hse.getTopPercentageOfHands(20, listOf169HandsT80));
			listOf169HandsT80.clear();

			for (int i = 1; i <= 13; i++) {
				for (int j = 1; j <= 13; j++) {
					Hand hand1 = null;
					String name = null;
					if (i == j) {
						Card cardOne = new Card(1, i);
						Card cardTwo = new Card(2, j);
						hand1 = new Hand(cardOne, cardTwo);
						name = cardOne.singleCharFormat() + cardTwo.singleCharFormat();
					}
					else if (i < j) {
						Card cardOne = new Card(1, i);
						Card cardTwo = new Card(1, j);
						hand1 = new Hand(cardOne, cardTwo);
						name = cardTwo.singleCharFormat() + cardOne.singleCharFormat() + "s";
					}
					else {
						Card cardOne = new Card(1, i);
						Card cardTwo = new Card(2, j);
						hand1 = new Hand(cardOne, cardTwo);
						name = cardOne.singleCharFormat() + cardTwo.singleCharFormat() + "o";
					}
					double handEquity = 0;

					for (int k = 0; k < NUMBER_OF_SIMS; k++) {
						handEquity = handEquity + hse.simEquityVsPercentageOfRange(hand1, 1, mapOfHands);
					}
					listOf169HandsT80.add( new SpecialHand(name, handEquity/NUMBER_OF_SIMS*100) );
				}
			}

			listOf169HandsT80 = hse.sortHandList(listOf169HandsT80);

			for (int i = 0; i < listOf169HandsT80.size(); i++) {
				listOf169HandsT80Printable.add(listOf169HandsT80.get(i));
			}

		//	listOf169HandsT80Printable = hse.getTopPercentageOfHands(80, listOf169HandsT80Printable);

			System.out.println("------------- Top 20 Percent ---------------");
			for (int i = 0; i < listOf169HandsT80Printable.size(); i++) {
				System.out.println(listOf169HandsT80Printable.get(i).getName() + "," + listOf169HandsT80Printable.get(i).getEquity() + "," + 
							   	   listOf169HandsT80Printable.get(i).getNumberOfCombinations());
			}

			listOf169HandsT80Printable.clear();

			mapOfHands = hse.convertListToMapOfHands(hse.getTopPercentageOfHands(80, listOf169HandsT50));
			listOf169HandsT50.clear();

			for (int i = 1; i <= 13; i++) {
				for (int j = 1; j <= 13; j++) {
					Hand hand1 = null;
					String name = null;
					if (i == j) {
						Card cardOne = new Card(1, i);
						Card cardTwo = new Card(2, j);
						hand1 = new Hand(cardOne, cardTwo);
						name = cardOne.singleCharFormat() + cardTwo.singleCharFormat();
					}
					else if (i < j) {
						Card cardOne = new Card(1, i);
						Card cardTwo = new Card(1, j);
						hand1 = new Hand(cardOne, cardTwo);
						name = cardTwo.singleCharFormat() + cardOne.singleCharFormat() + "s";
					}
					else {
						Card cardOne = new Card(1, i);
						Card cardTwo = new Card(2, j);
						hand1 = new Hand(cardOne, cardTwo);
						name = cardOne.singleCharFormat() + cardTwo.singleCharFormat() + "o";
					}
					double handEquity = 0;

					for (int k = 0; k < NUMBER_OF_SIMS; k++) {
						handEquity = handEquity + hse.simEquityVsPercentageOfRange(hand1, 1, mapOfHands);
					}
					listOf169HandsT50.add( new SpecialHand(name, handEquity/NUMBER_OF_SIMS*100) );
				}
			}

			listOf169HandsT50 = hse.sortHandList(listOf169HandsT50);

			for (int i = 0; i < 169; i++) {
				listOf169HandsT50Printable.add(listOf169HandsT50.get(i));
			}

	//		listOf169HandsT50Printable = hse.getTopPercentageOfHands(50, listOf169HandsT50Printable);

			System.out.println("------------- Top 50 Percent ---------------");
			for (int i = 0; i < listOf169HandsT50Printable.size(); i++) {
				System.out.println(listOf169HandsT50Printable.get(i).getName() + "," + listOf169HandsT50Printable.get(i).getEquity() + "," + 
							   	   listOf169HandsT50Printable.get(i).getNumberOfCombinations());
			}

			listOf169HandsT50Printable.clear();

		} */

	}
}






	/*
	// map of hands WITH % of range needs to be done beforehand, this maximizes the efficiency
	public double simEquityVsPercentageOfRangeUsingMap(Hand handOne, int numberOfOpponents, 
													   HashMap<String, SpecialHand> mapOfHands, HashMap<String, HandType>  map) {

		TypeOfHandEvaluation eval = new TypeOfHandEvaluation();
		ArrayList<Card> deck = eval.loadDeck();
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

		for (int i = 0; i < otherHands.size(); i++) {
			boolean isCardInRangeFound = false;

			while (!isCardInRangeFound) {
				int number1 = RANDOM.nextInt(deck.size());
				Card cardOne = deck.get(number1);
				deck.remove(number1);
				int number2 = RANDOM.nextInt(deck.size());
				Card cardTwo = deck.get(number2);
				deck.remove(number2);
				Hand hand = new Hand(cardOne, cardTwo);
				if (mapOfHands.get(hand.convertHandToSpecialHand().getName()) != null) {
					isCardInRangeFound = true;
					otherHands.get(i)[0] = cardOne;
					otherHands.get(i)[1] = cardTwo;
				}
				else {
					deck.add(cardOne);
					deck.add(cardTwo);
				}
			}
		}

		//creating flop
		for (int i = 0; i < 5; i++) {
			int number = RANDOM.nextInt(deck.size());
			completeBoardList[2 + i] = deck.get(number);
			for (int j = 0; j < otherHands.size(); j++) {
				otherHands.get(j)[2 + i] = deck.get(number);
			}
			deck.remove(number);
		}

		HandType hand1 = eval.bestHandUsingMapFullDeck(completeBoardList, map);

		boolean isTie = false;
		double numberOfPeopleToSplitThePot = 1;
		for (int i = 0; i < otherHands.size(); i++) {
			int comparisonNumber = hand1.compareHandsFullDeck(eval.bestHandUsingMapFullDeck(otherHands.get(i), map), 13);
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

	// map of hands WITH % of range needs to be done beforehand, this maximizes the efficiency
	public double simEquityVsPercentageOfRangeUsingMapWithInteger(Hand handOne, int numberOfOpponents, 
													   HashMap<String, SpecialHand> mapOfHands, HashMap<String, Integer>  map) {

		TypeOfHandEvaluation eval = new TypeOfHandEvaluation();
		ArrayList<Card> deck = eval.loadDeck();
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

		for (int i = 0; i < otherHands.size(); i++) {
			boolean isCardInRangeFound = false;

			while (!isCardInRangeFound) {
				int number1 = RANDOM.nextInt(deck.size());
				Card cardOne = deck.get(number1);
				deck.remove(number1);
				int number2 = RANDOM.nextInt(deck.size());
				Card cardTwo = deck.get(number2);
				deck.remove(number2);
				Hand hand = new Hand(cardOne, cardTwo);
				if (mapOfHands.get(hand.convertHandToSpecialHand().getName()) != null) {
					isCardInRangeFound = true;
					otherHands.get(i)[0] = cardOne;
					otherHands.get(i)[1] = cardTwo;
				}
				else {
					deck.add(cardOne);
					deck.add(cardTwo);
				}
			}
		}

		//creating flop
		for (int i = 0; i < 5; i++) {
			int number = RANDOM.nextInt(deck.size());
			completeBoardList[2 + i] = deck.get(number);
			for (int j = 0; j < otherHands.size(); j++) {
				otherHands.get(j)[2 + i] = deck.get(number);
			}
			deck.remove(number);
		}

		int hand1Value = eval.bestHandUsingMapFullDeckNumberOutput(completeBoardList, map);

		boolean isTie = false;
		double numberOfPeopleToSplitThePot = 1;
		for (int i = 0; i < otherHands.size(); i++) {
			int otherHandValue = eval.bestHandUsingMapFullDeckNumberOutput(otherHands.get(i), map);
			if (otherHandValue > hand1Value) {
				return 0;
			}
			if (otherHandValue == hand1Value) {
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


	} */


	/*
	//returns 0 to 1 depending on win. NOT 0 or 1.
	public double simulateAHandEquityVsRangeForFullDeckUsingMap(Hand handOne, int numberOfOpponents, HashMap<String, HandType> map) {
		TypeOfHandEvaluation eval = new TypeOfHandEvaluation();
		ArrayList<Card> deck = eval.loadDeck();
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
			int number1 = RANDOM.nextInt(deck.size());
			otherHands.get(i)[0] = deck.get(number1);
			deck.remove(number1);
			int number2 = RANDOM.nextInt(deck.size());
			otherHands.get(i)[1] = deck.get(number2);
			deck.remove(number2);
		}

		//creating flop
		for (int i = 0; i < 5; i++) {
			int number = RANDOM.nextInt(deck.size());
			completeBoardList[2 + i] = deck.get(number);
			for (int j = 0; j < otherHands.size(); j++) {
				otherHands.get(j)[2 + i] = deck.get(number);
			}
			deck.remove(number);
		}

		HandType hand1 = eval.bestHandUsingMapFullDeck(completeBoardList, map);

		boolean isTie = false;
		double numberOfPeopleToSplitThePot = 1;
		for (int i = 0; i < otherHands.size(); i++) {
			int comparisonNumber = hand1.compareHandsFullDeck(eval.bestHandUsingMapFullDeck(otherHands.get(i), map), 13);
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


	//returns 0 to 1 depending on win. NOT 0 or 1.
	public double simulateAHandEquityVsRangeForFullDeckUsingMapWithInteger(Hand handOne, int numberOfOpponents, HashMap<String, Integer> map) {
		TypeOfHandEvaluation eval = new TypeOfHandEvaluation();
		ArrayList<Card> deck = eval.loadDeck();
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
			int number1 = RANDOM.nextInt(deck.size());
			otherHands.get(i)[0] = deck.get(number1);
			deck.remove(number1);
			int number2 = RANDOM.nextInt(deck.size());
			otherHands.get(i)[1] = deck.get(number2);
			deck.remove(number2);
		}

		//creating flop
		for (int i = 0; i < 5; i++) {
			int number = RANDOM.nextInt(deck.size());
			completeBoardList[2 + i] = deck.get(number);
			for (int j = 0; j < otherHands.size(); j++) {
				otherHands.get(j)[2 + i] = deck.get(number);
			}
			deck.remove(number);
		}

		int hand1Value = eval.bestHandUsingMapFullDeckNumberOutput(completeBoardList, map);

		boolean isTie = false;
		double numberOfPeopleToSplitThePot = 1;
		for (int i = 0; i < otherHands.size(); i++) {
			int otherHandValue = eval.bestHandUsingMapFullDeckNumberOutput(otherHands.get(i), map);
			if (otherHandValue > hand1Value) {
				return 0;
			}
			if (otherHandValue == hand1Value) {
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
	} */



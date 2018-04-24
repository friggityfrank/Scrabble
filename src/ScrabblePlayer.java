/*

  Authors (group members): Frank Savino, Gavin Smith
  Email addresses of group members:	fsavino2014@my.fit.edu, gsmith2016@my.fit.edu
  Group name: 23f

  Course:  CSE2010
  Section: 2/3

  Description of the overall algorithm and key data structures:
  
  	This program will generate a Scrabble Word from 7 given letter tiles and the letters that
   are present on the board. Recursion is used to find valid words stored within a dictionary Trie


*/

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;


public class ScrabblePlayer
{
	private Trie dictionary = new Trie();
	private int[] pointVals = {0, 1, 3, 3, 2, 1, 4, 2, 4, 1, 8, 5, 1, 3, 1, 1, 3, 10, 1, 1, 1, 1, 4, 4, 8, 4, 10};
    private Multipliers multi = new Multipliers();

    // initialize ScrabblePlayer with a file of English words
    public ScrabblePlayer(String wordFile) {
    	String input;			// String to handle input
    	try {					// try-catch to allow for input
    		FileReader fr = new FileReader(wordFile);		// Readers declared
    		BufferedReader br = new BufferedReader(fr);
    		while ((input = br.readLine()) != null) {
    			dictionary.addWord(input);			// Add words while file is being read
    		}
    		br.close();								// Close readers
    		fr.close();
    	} catch (IOException e) {			// Throw exception if necessary
    		e.printStackTrace();
    	}
    	
    }

    // based on the board and available letters, 
    //    return a valid word with its location and orientation
    //    See ScrabbleWord.java for details of the ScrabbleWord class 
    //
    // board: 15x15 board, each element is an UPPERCASE letter or space;
    //    a letter could be an underscore representing a blank (wildcard);
    //    first dimension is row, second dimension is column
    //    ie, board[row][col]     
    //    row 0 is the top row; col 0 is the leftmost column
    // 
    // availableLetters: a char array that has seven letters available
    //    to form a word
    //    a blank (wildcard) is represented using an underscore '_'
    //

    public ScrabbleWord getScrabbleWord(char[][] board, char[] availableLetters) {

    	// Get word and relevant data, then organize it
    	String rawWord = getWordFromBoard(board);
    	String[] split = rawWord.split(" ");
    	String boardWord = split[4];
    	int row = Integer.parseInt(split[0]);
    	int col = Integer.parseInt(split[1]);
    	int length = Integer.parseInt(split[2]);
    	char orientation = split[3].charAt(0);
    	// Declare variables for available cells around the word
    	int availLeft = 0, availRight = 0, availUp = 0, availDown = 0;
    	// Declare lists to store words and locations
    	ArrayList<Word> words = new ArrayList<Word>();
    	ArrayList<int[]> startList = new ArrayList<int[]>();
    	// Determine availability using orientation
    	if (orientation == 'h') {
    		availUp = row;
    		availDown = 14 - row;
    		availLeft = col;
    		availRight = 15 - (col + length);
    	} else {
    		availUp = row;
    		availDown = 15 - (row + length);
    		availLeft = col;
    		availRight = 14 - col;
    	}
    	// Store available letters
    	String wordLetters = String.copyValueOf(availableLetters);
    	// Iterate possible words from available letters and those present on the board
    	char newOr = 'h';
    	int index = 0;
    	Word compare = new Word("", 0);
    	for (int i = 0; i < boardWord.length(); i++) {
    		wordLetters = wordLetters.substring(0, 7) + boardWord.charAt(i);    		// Store board letter
    		int startR, startC, loc = 0;    		// Declare player word's start row and column
    		Word tempW = new Word("", 0);			// Declare temp word for comparison
    		if (orientation == 'h') {			// Check word with horizonal orientation
    			tempW = getBestWord(wordLetters, availUp, availDown, wordLetters.charAt(7), row, col + i, 'h');	// Store best word
    			startC = col + i;	// Store starting row and column of the new word
    			startR = row - tempW.getWord().indexOf("*");
    			startList.add(new int[]{startR, startC});
    			newOr = 'v';		// Set word's orientation
    		} else {				// Perform same operation for vertical orientation words
    			tempW = getBestWord(wordLetters, availLeft, availRight, wordLetters.charAt(7), row + i, col, 'v');
    			startR = row + i;
    			startC = col - tempW.getWord().indexOf("*");
    			startList.add(new int[]{startR, startC});
    			newOr = 'h';
    		}
    		if (compare.getPoints() < tempW.getPoints()) {	// Compare points of the two words
    			compare = tempW;			// Store the highest value word
    			index = i;					// Store coordinate index of the word
    		}
    	}
		int bestR = startList.get(index)[0];	// Fetch coordinates
    	int bestC = startList.get(index)[1];
    	int intersect = compare.getWord().indexOf("*");
    	if (compare.getWord().length() < 1) {			// Check that throws a dummy word and intersection index
    		compare = new Word(" ", 0);		// to prevent the program from breaking
    		intersect = 0;
    	}
    	String readyWord = compare.getWord().substring(0, intersect) + compare.getWord().substring(intersect + 1);
        return  new ScrabbleWord(readyWord.toUpperCase(), bestR, bestC, newOr);	// Return new word
    }
    
    // String to get information from word on board
    public String getWordFromBoard (char[][] board) {
    	String out = "";					// Output string declared
    	for (int i = 0; i < 15; i++)			// Nested loops to iterate through board
    		for (int j = 0; j < 15; j++)		
    			if (board[i][j] != ' ') {		// If board space contains non space character, fetch word and information
    				out = getOrientation(board, i, j);
    				out = (i + " " + j + " " + (out.length() - 1) + " " + out);	// Return row, col, length, orientation, and
    				return out;													//  contents of the word
    			}
    	return out;
    }
    
    // Method that returns orientation and contents of word on the Scrabble board
    public String getOrientation (char[][] board, int r, int c) {
    	String out = "";				// Output string declared
    	if (c == 14) {
    		out += "v ";
    		while (board[r][c] != ' ' && r <= 14)
    			out += board[r++][c];
    		return out;
    	}
    	if (r == 14) {
    		out += "h ";
    		while (board[r][c] != ' ' && c <= 14)
    			out += board[r][c++];
    		return out;
    	}
    	if (board[r][c + 1] != ' ') {	// Check board space to the right for a character
    		out += "h ";					// If found, add orientation to string
    		while (board[r][c] != ' ' && c <= 14)	// Get letters that make up string
    			out += board[r][c++];	// Add to output string
    	} else {						// Repeat for vertical cases
    		out += "v ";
    		while (board[r][c] != ' ' && r <= 14)
    			out += board[r++][c];
    	}
    	return out;						// Return output string
    }
    
    // Method that calls findWord() to get best word. 
    public Word getBestWord (String letters, int back, int ahead, char c, int row, int col, char or) {
    	Word best = new Word ("", 0);
    	best = findWord("", letters, dictionary.getRoot(), new Word("", 0), false, back, ahead, row, col, or);
    	return best;
    }
    
    // Method to remove character from string
    public String trimOut (String s, int i) {
    	if (i == 0)		// Return substring(1) if first char is being removed
    		return s.substring(1);
    	if (i == s.length() - 1) 	// Return substring(0, n-1) if last char removed
    		return s.substring(0, i - 1);
    	return (s.substring(0, i) + s.substring(i + 1)); // Otherwise, perform appropriate substring operation
    }
    
    // Recursive method to find the highest point word from a set of characters
    public Word findWord (String currWord, String options, Node n, Word compare, boolean foundBoard, int back, int ahead, int row, int col, char or) {
    	Word temp = compare;							// Store variables from parameters 
    	Node traverse = n;
    	if ((n.isEnd() && foundBoard) && currWord.length() > 2) {							// Check if current Node can end a word
    		int index = currWord.indexOf("*");
    		String newWord = currWord.substring(0, index) + currWord.substring(index + 1);
    		int newPoints = 0, newRow = 0, newCol = 0;
    		if (or == 'h') {
    			newRow = row - index;
    			newCol = col;
    			if (newRow + newWord.length() < 15)
    				newPoints = pointsByBoard(newWord, newRow, newCol, newWord.length(), or, index);
    		} else {
    			newRow = row;
    			newCol = col - index;
    			if (newCol + newWord.length() < 15)
    				newPoints = pointsByBoard(newWord, newRow, newCol, newWord.length(), or, index);
    		}
    		if (temp.getPoints() < newPoints)				// If so, check if it has more points than the given parameter
    			if (index <= back && (newWord.length() - index) <= ahead)
    				temp = new Word(currWord, newPoints);		// If it has more points, store as a new word
    	}
    	for (int i = 0; i < options.length(); i++) {	// Loop through all available letters
    		Character c = Character.toLowerCase(options.charAt(i));
    		if (c == '_') {				// Check for blank space, iterating through all 26 letters if the case
    			for (int j = 0; j < n.getChildren().size(); j++) {
    				traverse = n.getChildren().get(j);	// If letter is available, check the child
        			if (traverse != null && traverse.getLetter() != '*') {	// If valid, call findWord with the string
        				temp = findWord(currWord + '_', trimOut(options, i),//  appended with blank space
        						traverse, temp, foundBoard, back, ahead, row, col, or);
        			}
    			}
    		} else {						// If not blank, only check for child containing available letter
    			traverse = n.getChild(c);
    			if (traverse != null) {
    				if (i == options.length() - 1 && !foundBoard) { 		 // Check if board char has been found
    					temp = findWord(currWord + '*' + c, trimOut(options, i),	// If yes, flag it has been found for
    						traverse, temp, true, back, ahead, row, col, or);   //  future loops
    				}														
    				else					// Otherwise, amend character to string when found.
    					temp = findWord(currWord + c, trimOut(options, i),
        						traverse, temp, foundBoard, back, ahead, row, col, or);
    			}
    		}
    	}
    	return temp; 	// Return best word
    }
    
    public int getPoints (Character c) {
    	return pointVals[((int) c) - 96];
    }
    
    public int pointsByBoard (String word, int startR, int startC, int length, char or, int index) {
    	int doubleW = 0, tripleW = 0;
    	int currR, currC;
    	int points = 0;
    	if (or == 'h') {
    		currR = startR;
    		currC = startC;
    		for (int i = 0; i < length; i++) {
    			if ((currR >= 0 && currR <= 14) && (currC >= 0 && currC <= 14))
					if (i != index && word.charAt(i) != '_') {
						switch (multi.getMulti(currR + i, currC)) {
						case 1:
							points += getPoints(Character.toLowerCase(word.charAt(i))) * 2;
							break;
						case 2:
							points += getPoints(Character.toLowerCase(word.charAt(i))) * 3;
							break;
						case 3:
							doubleW++;
							break;
						case 4:
							tripleW++;
							break;
						default:
							points += getPoints(Character.toLowerCase(word.charAt(i)));
							break;
						}
    				}
    		}
    	} else {
    		currR = startR;
    		currC = startC;
    		for (int i = 0; i < length; i++) {
    			if ((currR >= 0 && currR <= 14) && (currC >= 0 && currC <= 14))
					if (i != index && word.charAt(i) != '_') {
						switch (multi.getMulti(currR, currC + i)) {
						case 1:
							points += getPoints(Character.toLowerCase(word.charAt(i))) * 2;
							break;
						case 2:
							points += getPoints(Character.toLowerCase(word.charAt(i))) * 3;
							break;
						case 3:
							doubleW++;
							break;
						case 4:
							tripleW++;
							break;
						default:
							points += getPoints(Character.toLowerCase(word.charAt(i)));
							break;
						}
    				}
    		}
    	}
    	points = points * (int) Math.pow(2, doubleW);
    	points = points * (int) Math.pow(3, tripleW);
    	return points;
    }
    
    /*
    public Word findWordExtend (String currWord, String options, int p, Node n, String stock, Word compare) {
    	Node traverse = n;
    	Word temp = compare;
    	if (n.isEnd()) {
    		if (temp.getPoints() < p)
    			temp = new Word(currWord, p);
    		
    	}
    		
    }
    */
    
    // Method to check if a full string is stored in the trie
    public boolean confirmWord (String s, Node n) {
    	Node traverse = n;			// Objects declared for trie traversal and checking
    	String checkWord = s;
    	for (int i = 0; i < checkWord.length(); i++) {	// Loop to traverse through length of string
    		if ((traverse = traverse.getChild(checkWord.charAt(i))) == null) // Check if the passed Node has this child
    			return false;							// If not, return false
    	}
    	return true; 									// Otherwise, trturn true
    }
    

}

/*

  Authors (group members):
  Email addresses of group members:
  Group name:

  Course:
  Section:

  Description of the overall algorithm and key data structures:


*/

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;


public class ScrabblePlayer
{
	Trie dictionary = new Trie();

    // initialize ScrabblePlayer with a file of English words
    public ScrabblePlayer(String wordFile) {
    	
    	String input;
    	try {
    		FileReader fr = new FileReader(wordFile);
    		BufferedReader br = new BufferedReader(fr);
    		while ((input = br.readLine()) != null) {
    			dictionary.addWord(input);
    		}
    		br.close();
    		fr.close();
    	} catch (IOException e) {
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
    	String boardWord = rawWord.substring(4);
    	int row = Integer.parseInt(rawWord.substring(0, 1));
    	int col = Integer.parseInt(rawWord.substring(1, 2));
    	int length = Integer.parseInt(rawWord.substring(2, 3));
    	char orientation = rawWord.charAt(3);
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
    	for (int i = 0; i < boardWord.length(); i++) {
    		// Store board letter
    		wordLetters = wordLetters.substring(0, 7) + boardWord.charAt(i);
    		int startR; int startC;
    		Word tempW;
    		if (orientation == 'h') {
    			tempW = getBestWord(wordLetters, availUp, availDown, wordLetters.charAt(7));
    			words.add(tempW);
    			startC = col + i;
    			startR = row - tempW.getWord().indexOf(Character.toLowerCase(boardWord.charAt(i)));
    			startList.add(new int[]{startR, startC});
    			newOr = 'v';
    		} else {
    			tempW = getBestWord(wordLetters, availLeft, availRight, wordLetters.charAt(7));
    			words.add(tempW);
    			startR = row + i;
    			startC = col - tempW.getWord().length() + tempW.getWord().indexOf(Character.toLowerCase(board[col][startR])) + 1;
    			startList.add(new int[]{startR, startC});
    			newOr = 'h';
    		}
    	}
		Word best, temp;
		String bestS = "", tempS;
		int bestR = 0, bestC = 0, index = 0;
    	if (!words.isEmpty()) {
    		best = words.get(0);
    		bestS = best.getWord();
    		for (int i = 1; i < words.size(); i++) {
    			temp = words.get(i);
    			tempS = temp.getWord();
    			if (best.getPoints() < temp.getPoints()) {
    					best = temp;
    					bestS = best.getWord();
    					index = i;
    			}
    		}
    		bestR = startList.get(index)[0];
    		bestC = startList.get(index)[1];
    	}
		words = null;
		startList = null;
        return  new ScrabbleWord(bestS.toUpperCase(), bestR, bestC, newOr);
    }
    
    public String getWordFromBoard (char[][] board) {
    	boolean foundOrientation = false;
    	String out = "";
    	for (int i = 0; i < 15; i++)
    		for (int j = 0; j < 15; j++)
    			if (!foundOrientation && board[i][j] != ' ') {
    				out = getOrientation(board, i, j);
    				out = ("" + i + "" + j + "" + (out.length() - 1) + out);
    				return out;
    			}
    	return out;
    }
    
    public String getOrientation (char[][] board, int r, int c) {
    	String out = "";				// Output string declared
    	if (board[r][c + 1] != ' ') {	// Check board space to the right for a character
    		out += 'h';					// If found, add orientation to string
    		while (board[r][c] != ' ')	//
    			out += board[r][c++];
    	} else {
    		out += 'v';
    		while (board[r][c] != ' ')
    			out += board[r++][c];
    	}
    	return out;
    }
    
    public Word getBestWord (String letters, int back, int ahead, char c) {
    	ArrayList<Word> wordList = new ArrayList<Word>();
    	Word best = new Word ("", 0), temp;
    	String bestS = best.getWord(), tempS;
    	int index = 0;
    	findWord("", letters, 0, dictionary.getRoot(), wordList, c, false);
    	for (int k = 0; k < wordList.size(); k++) {
    		temp = wordList.get(k);
    		tempS = temp.getWord();
    		index = tempS.indexOf(Character.toLowerCase(c));
    		if (best.getPoints() < temp.getPoints() && index > -1) {
    				best = temp;
    				bestS = tempS;
    		}
    	}
    	
    	return best;
    }
    
    public String trimOut (String s, int i) {
    	if (i == 0)
    		return s.substring(1);
    	if (i == s.length() - 1)
    		return s.substring(0, i - 1);
    	return (s.substring(0, i) + s.substring(i + 1));
    }
    
    public void findWord (String currWord, String options, int p, Node n, ArrayList<Word> list, Character c, boolean foundBoard) {
    	Node traverse = n;
    	if (n.isEnd())
    		list.add(new Word(currWord, p));
    	for (int i = 0; i < options.length(); i++) {
    		if (options.charAt(i) == '_') {
    			for (int j = 0; j < n.getChildren().size(); j++) {
    				traverse = n.getChildren().get(j);
        			if (traverse != null && traverse.getLetter() != '*') {
        				findWord(currWord + '_', trimOut(options, i),
        						p, traverse, list, c, foundBoard);
        			}
    			}
    		} else {
    			traverse = n.getChild(options.charAt(i));
    			if (traverse != null) {
    				if (i == options.length() - 1 && !foundBoard)
    					findWord(currWord, trimOut(options, i),
    						p, traverse, list, c, true);
    				else
    					findWord(currWord + Character.toLowerCase(options.charAt(i)), trimOut(options, i),
        						p + traverse.getPoints(), traverse, list, c, foundBoard);
    			}
    		}
    	}
    }

}

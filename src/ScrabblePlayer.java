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

    	String rawWord = getWordFromBoard(board);
    	String boardWord = rawWord.substring(4);
    	int row = Integer.parseInt(rawWord.substring(0, 1));
    	int col = Integer.parseInt(rawWord.substring(1, 2));
    	char orientation = rawWord.charAt(3);

        return  new ScrabbleWord("MYWORD", 0, 0, 'h');
    }
    
    public String getWordFromBoard (char[][] board) {
    	boolean foundOrientation = false;
    	String out = "";
    	for (int i = 0; i < 15; i++)
    		for (int j = 0; j < 15; j++)
    			if (!foundOrientation && board[i][j] != ' ') {
    				out = getOrientation(board, i, j);
    				return i + j + (out.length() - 1) + out;
    			}
    	return out;
    }
    
    public String getOrientation (char[][] board, int r, int c) {
    	String out = "";
    	if (board[r][c + 1] != ' ') {
    		out += 'H';
    		while (board[r][c] != ' ')
    			out += board[r][c++];
    	} else {
    		out += 'V';
    		while (board[r][c] != ' ')
    			out += board[r++][c];
    	}
    	return out;
    }

}

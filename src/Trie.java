// Trie class for managing dictionary
public class Trie {

	// Root Node declared
	private Node root;
	
	// Default constructor
	public Trie () {
		root = new Node(' ', null);
	}
	
	// Get method for root Node
	public Node getRoot () {
		return root;
	}
	
	// Method to add words to Trie
	public void addWord (String s) {
		// With 7 available letters and one additional one from the word on the board,
		//  the longest any word can be is 8 characters. As such, any strings passed 
		//  longer than 8 characters is ignored.
		if (s.length() > 8)
			return;
		// Traversal Node declared and assigned to Trie root
		Node traverse = root;
		// char array to store passed String 
		char[] word = s.toCharArray();
		// Loop to iterate through array
		for (int i = 0; i < word.length; i++) {
			// If letter exists on the board, move to the appropriate Node
			if (traverse.hasChild(word[i]))
				traverse = traverse.getChild(word[i]);
			// Otherwise, add a new Node
			else
				traverse = traverse.addChild(word[i]);
		}
		// End the word
		traverse.endWord();
	}
	
}

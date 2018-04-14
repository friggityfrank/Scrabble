
public class Trie {

	private Node root;
	
	public Trie () {
		root = new Node(' ', null);
	}
	
	public Node getRoot () {
		return root;
	}
	
	public void addWord (String s) {
		Node traverse = root;
		char[] word = s.toCharArray();
		for (int i = 0; i < word.length; i++) {
			if (traverse.hasChild(word[i]))
				traverse = traverse.getChild(word[i]);
			else
				traverse = traverse.addChild(word[i]);
		}
		traverse.endWord();
	}
	
}

import java.util.ArrayList;
import java.util.Collections;

public class Node implements Comparable<Node> {
	
	private Character letter;
	private int points;
	private Node parent;
	private ArrayList<Node> children;
	private int[] pointVals = {0, 1, 3, 3, 2, 1, 4, 2, 4, 1, 8, 5, 1, 3, 1, 1, 3, 10, 1, 1, 1, 1, 4, 4, 8, 4, 10};
	private boolean isEnd = false;
	private boolean presence[] = new boolean[27];
	
	public Node () {
		
	}
	
	// Constructor to store Letter information and parent Node
	public Node (Character c, Node p) {
		letter = c;
		parent = p;
		// Check if blank space or '*' - assign parent's points + Letter's
		//  point value if it is not
		if (c != '_' && c != '*')
			points = pointVals[(int) c - 96] + p.getPoints();
		// If the letter is a blank space, assign parent's points
		else if (p != null)
			points = p.getPoints();
		// Otherwise, it is null and should be ignored
		else
			points = 0;
		// If '*', the word is marked as completed
		if (c == '*')
			isEnd = true;
		children = new ArrayList<Node>();
	}
	
	public Character getLetter () {
		return letter;
	}
	
	public int getPoints () {
		return points;
	}
	
	public Node getParent () {
		return parent;
	}
	
	public ArrayList<Node> getChildren () {
		return children;
	}
	
	// Method that returns a child Node using binary search
	public Node getChild (Character c) {
		int index = Collections.binarySearch(children, new Node(c, null));
		if (index >= 0)
			return children.get(index);
		return null;
	}
	
	// Method that returns a child using index
	public Node getChild (int i) {
		if (i < children.size() && i >= 0)
			return children.get(i);
		return null;
	}
	
	// Method to check if the Node has a child containing the passed character
	public boolean hasChild (Character c) {
		// Check for blank child
		if (c == '_')
			return presence[0];
		// Return false if end of word
		if (c == '*')
			return false;
		// Check for other letter
		return presence[(int) c - 96];
	}
	
	public void setParent (Node p) {
		parent = p;
	}
	
	public void setChildren (ArrayList<Node> n) {
		children = n;
	}
	
	// Method to add child Node by passing a character
	public Node addChild (Character c) {
		// Mark presence of children
		if (c == '_')
			presence[0] = true;
		else
			presence[(int) c - 96] = true;
		// Create new Node, setting this as the parent
		Node child = new Node(c, this);
		// Add if empty
		if (children.isEmpty())
			children.add(child);
		// Otherwise, find the appropriate index by using binary search
		else {
			int index = Collections.binarySearch(children,  child, new NodeComparator());
			// If found, add at the appropriate index
			if (index < 0)
				children.add(-1 * (index - 1), child);
		}
		return child;
	}
	
	public void endWord () {
		children.add(new Node('*', this));
	}
	
	public boolean isEnd () {
		return isEnd;
	}

	@Override
	public int compareTo(Node o) {
		return this.getLetter().compareTo(o.getLetter());
	}
	
	public String toString () {
		return letter.toString();
	}

}

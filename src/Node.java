import java.util.ArrayList;
import java.util.Collections;

public class Node implements Comparable<Node> {
	
	private char letter;
	private ArrayList<Node> children = new ArrayList<Node>();
	
	public Node () {
		
	}
	
	// Constructor to store Letter information and parent Node
	public Node (Character c, Node p) {
		letter = Character.toLowerCase(c);
	}
	
	public Character getLetter () {
		return letter;
	}
	
	public ArrayList<Node> getChildren () {
		return children;
	}
	
	// Method that returns a child Node using binary search
	public Node getChild (Character c) {
		int index = Collections.binarySearch(children, new Node(c, this));
		if (index >= 0)
			return children.get(index);
		return null;
	}
	
	// Method to check if the Node has a child containing the passed character
	public boolean isEnd () {
		return (getChild('*') != null);
	}
	
	public void setChildren (ArrayList<Node> n) {
		children = n;
	}
	
	// Method to add child Node by passing a character
	public Node addChild (Character c) {
		// Mark presence of children
		char lower = Character.toLowerCase(c);
		// Create new Node, setting this as the parent
		Node child = new Node(lower, this);
		// Add if empty
		if (children.isEmpty())
			children.add(child);
		// Otherwise, find the appropriate index by using binary search
		else {
			int index = Collections.binarySearch(children, child, new NodeComparator());
			// If found, add at the appropriate index
			if (index < 0)
				children.add(-1 * index - 1, child);
		}
		return child;
	}

	@Override
	public int compareTo(Node o) {
		if (letter < o.getLetter())
			return -1;
		if (letter > o.getLetter())
			return 1;
		return 0;
	}
	

}

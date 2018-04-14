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
	
	public Node (Character c, Node p) {
		letter = c;
		parent = p;
		if (c != ' ')
			points = pointVals[(int) c - 96] + p.getPoints();
		else if (p != null)
			points = p.getPoints();
		else
			points = 0;
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
	
	public Node getChild (Character c) {
		int index = Collections.binarySearch(children, new Node(c, null));
		if (index >= 0)
			return children.get(index);
		return null;
	}
	
	public Node getChild (int i) {
		if (i < children.size() && i >= 0)
			return children.get(i);
		return null;
	}
	
	public boolean endOfWord () {
		return isEnd;
	}
	
	public boolean hasChild (Character c) {
		if (c == ' ')
			return presence[0];
		return presence[(int) c - 96];
	}
	
	public void setLetter (Character c) {
		letter = c;
		if (c != ' ')
			points = pointVals[(int) c - 96];
		else
			points = parent.getPoints();
	}
	
	public void setParent (Node p) {
		parent = p;
	}
	
	public void setChildren (ArrayList<Node> n) {
		children = n;
	}
	
	public Node addChild (Character c) {
		if (c == ' ')
			presence[0] = true;
		else
			presence[(int) c - 96] = true;
		Node child = new Node(c, this);
		if (children.isEmpty())
			children.add(child);
		else {
			int index = Collections.binarySearch(children,  child, new NodeComparator());
			if (index < 0)
				children.add(-1 * (index - 1), child);
		}
		return child;
	}
	
	public void endWord () {
		isEnd = true;
	}

	@Override
	public int compareTo(Node o) {
		return this.getLetter().compareTo(o.getLetter());
	}
	
	public String toString () {
		return letter.toString();
	}

}

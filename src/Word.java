
public class Word implements Comparable<Word> {
	
	private String thisWord;
	private int pointVal;
	
	public Word () {
		
	}
	
	public Word (String s, int p) {
		thisWord = s;
		pointVal = p;
	}
	
	public Word (char[] c, int p) {
		thisWord = String.copyValueOf(c);
		pointVal = p;
	}
	
	public String getWord () {
		return thisWord;
	}
	
	public int getPoints () {
		return pointVal;
	}

	@Override
	public int compareTo(Word o) {
		if (pointVal < o.getPoints())
			return -1;
		if (pointVal > o.getPoints())
			return 1;
		return 0;
	}
	

}

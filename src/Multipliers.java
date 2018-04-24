
public class Multipliers {
	
	private int[][] bonus = new int[15][15];
	
	public Multipliers () {	
		bonus[3][0] = 1;                     // 1=2L; 2=3L; 3=2W; 4=3W
		bonus[11][0] = 1;					// Hard-coded for ease of use in program
		bonus[6][2] = 1;
		bonus[8][2] = 1;
		bonus[0][3] = 1;
		bonus[7][3] = 1;
		bonus[14][3] = 1;
		bonus[2][6] = 1;
		bonus[6][6] = 1;
		bonus[8][6] = 1;
		bonus[12][6] = 1;
		bonus[3][7] = 1;
		bonus[11][7] = 1;
		bonus[2][8] = 1;
		bonus[6][8] = 1;
		bonus[8][8] = 1;
		bonus[12][8] = 1;
		bonus[0][11] = 1;
		bonus[7][11] = 1;
		bonus[14][11] = 1;
		bonus[6][12] = 1;
		bonus[8][12] = 1;
		bonus[3][14] = 1;
		bonus[11][14] = 1;

		bonus[5][1] = 2;
		bonus[9][1] = 2;
		bonus[1][5] = 2;
		bonus[5][5] = 2;
		bonus[9][5] = 2;
		bonus[13][5] = 2;
		bonus[1][9] = 2;
		bonus[5][9] = 2;
		bonus[9][9] = 2;
		bonus[13][9] = 2;
		bonus[5][13] = 2;
		bonus[9][13] = 2;

		bonus[1][1] = 3;
		bonus[2][2] = 3;
		bonus[3][3] = 3;
		bonus[4][4] = 3;
		bonus[1][13] = 3;
		bonus[2][12] = 3;
		bonus[3][11] = 3;
		bonus[4][10] = 3;
		bonus[13][1] = 3;
		bonus[12][2] = 3;
		bonus[11][3] = 3;
		bonus[10][4] = 3;
		bonus[10][10] = 3;
		bonus[11][11] = 3;
		bonus[12][12] = 3;
		bonus[13][13] = 3;
		bonus[7][7] = 3;

		bonus[0][0] = 4;
		bonus[7][0] = 4;
		bonus[0][7] = 4;
		bonus[0][14] = 4;
		bonus[14][0] = 4;
		bonus[14][7] = 4;
		bonus[7][14] = 4;
		bonus[14][14] = 4;
	}
	
	public int getMulti (int row, int col) {
		return bonus[row][col];
	}

}

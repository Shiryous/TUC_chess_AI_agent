package src;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;


public class World
{
	private String[][] board = null;
	private int rows = 7;
	private int columns = 5;
	private int myColor = 0;
	private ArrayList<String> availableMoves = null;
	private int rookBlocks = 3;		// rook can move towards <rookBlocks> blocks in any vertical or horizontal direction
	private int nTurns = 0;
	private int nBranches = 0;
	private int noPrize = 9;
	private double inf = Double.POSITIVE_INFINITY;
	private int score_white, score_black;
	public World()
	{
		board = new String[rows][columns];
		
		/* represent the board
		
		BP|BR|BK|BR|BP
		BP|BP|BP|BP|BP
		--|--|--|--|--
		P |P |P |P |P 
		--|--|--|--|--
		WP|WP|WP|WP|WP
		WP|WR|WK|WR|WP
		*/
		
		// initialization of the board
		for(int i=0; i<rows; i++)
			for(int j=0; j<columns; j++)
				board[i][j] = " ";
		
		// setting the black player's chess parts
		
		// black pawns
		for(int j=0; j<columns; j++)
			board[1][j] = "BP";
		
		board[0][0] = "BP";
		board[0][columns-1] = "BP";
		
		// black rooks
		board[0][1] = "BR";
		board[0][columns-2] = "BR";
		
		// black king
		board[0][columns/2] = "BK";
		
		// setting the white player's chess parts
		
		// white pawns
		for(int j=0; j<columns; j++)
			board[rows-2][j] = "WP";
		
		board[rows-1][0] = "WP";
		board[rows-1][columns-1] = "WP";
		
		// white rooks
		board[rows-1][1] = "WR";
		board[rows-1][columns-2] = "WR";
		
		// white king
		board[rows-1][columns/2] = "WK";
		
		// setting the prizes
		for(int j=0; j<columns; j++)
			board[rows/2][j] = "P";
		
		availableMoves = new ArrayList<String>();
	}
	
	public void setMyColor(int myColor)
	{
		this.myColor = myColor;
	}
	
	public String selectAction(int score_white,int score_black)
	{
		this.score_white = score_white;
		this.score_black = score_black;
		
		availableMoves = new ArrayList<String>();
				
		if(myColor == 0)		// I am the white player
			this.whiteMoves(board);
		else					// I am the black player
			this.blackMoves(board);
		
		// keeping track of the branch factor
		nTurns++;
		nBranches += availableMoves.size();

		//return  this.UCTSearch();
		if (myColor == 0){
			return this.selectMinmax(5, true);
		}
		else{
			return this.selectRandomAction();
		}
	}
	/**
	 * This function is used to find the available moves for black
	 * @param board The board current state
	 */
	private void blackMoves(String[][] board)
	{
		String firstLetter = "";
		String secondLetter = "";
		String move = "";
				
		for(int i=0; i<rows; i++)
		{
			for(int j=0; j<columns; j++)
			{
				firstLetter = Character.toString(board[i][j].charAt(0));
				
				// if it there is not a black chess part in this position then keep on searching
				if(firstLetter.equals("W") || firstLetter.equals(" ") || firstLetter.equals("P"))
					continue;
				
				// check the kind of the white chess part
				secondLetter = Character.toString(board[i][j].charAt(1));
				
				if(secondLetter.equals("P"))	// it is a pawn
				{
					
					// check if it can move one vertical position ahead
					firstLetter = Character.toString(board[i+1][j].charAt(0));
					
					if(firstLetter.equals(" ") || firstLetter.equals("P"))
					{
						move = Integer.toString(i) + Integer.toString(j) + 
							   Integer.toString(i+1) + Integer.toString(j);
						
						availableMoves.add(move);
					}
					
					// check if it can move crosswise to the left
					if(j!=0 && i!=rows-1)
					{
						firstLetter = Character.toString(board[i+1][j-1].charAt(0));
						
						if(!(firstLetter.equals("B") || firstLetter.equals(" ") || firstLetter.equals("P"))) {
							move = Integer.toString(i) + Integer.toString(j) + 
									   Integer.toString(i+1) + Integer.toString(j-1);
								
							availableMoves.add(move);
						}																	
					}
					
					// check if it can move crosswise to the right
					if(j!=columns-1 && i!=rows-1)
					{
						firstLetter = Character.toString(board[i+1][j+1].charAt(0));
						
						if(!(firstLetter.equals("B") || firstLetter.equals(" ") || firstLetter.equals("P"))) {
							move = Integer.toString(i) + Integer.toString(j) + 
									   Integer.toString(i+1) + Integer.toString(j+1);
								
							availableMoves.add(move);
						}
							
						
						
					}
				}
				else if(secondLetter.equals("R"))	// it is a rook
				{
					// check if it can move upwards
					for(int k=0; k<rookBlocks; k++)
					{
						if((i-(k+1)) < 0)
							break;
						
						firstLetter = Character.toString(board[i-(k+1)][j].charAt(0));
						
						if(firstLetter.equals("B"))
							break;
						
						move = Integer.toString(i) + Integer.toString(j) + 
							   Integer.toString(i-(k+1)) + Integer.toString(j);
						
						availableMoves.add(move);
						
						// prevent detouring a chesspart to attack the other
						if(firstLetter.equals("W") || firstLetter.equals("P"))
							break;
					}
					
					// check if it can move downwards
					for(int k=0; k<rookBlocks; k++)
					{
						if((i+(k+1)) == rows)
							break;
						
						firstLetter = Character.toString(board[i+(k+1)][j].charAt(0));
						
						if(firstLetter.equals("B"))
							break;
						
						move = Integer.toString(i) + Integer.toString(j) + 
							   Integer.toString(i+(k+1)) + Integer.toString(j);
						
						availableMoves.add(move);
						
						// prevent detouring a chesspart to attack the other
						if(firstLetter.equals("W") || firstLetter.equals("P"))
							break;
					}
					
					// check if it can move on the left
					for(int k=0; k<rookBlocks; k++)
					{
						if((j-(k+1)) < 0)
							break;
						
						firstLetter = Character.toString(board[i][j-(k+1)].charAt(0));
						
						if(firstLetter.equals("B"))
							break;
						
						move = Integer.toString(i) + Integer.toString(j) + 
							   Integer.toString(i) + Integer.toString(j-(k+1));
						
						availableMoves.add(move);
						
						// prevent detouring a chesspart to attack the other
						if(firstLetter.equals("W") || firstLetter.equals("P"))
							break;
					}
					
					// check of it can move on the right
					for(int k=0; k<rookBlocks; k++)
					{
						if((j+(k+1)) == columns)
							break;
						
						firstLetter = Character.toString(board[i][j+(k+1)].charAt(0));
						
						if(firstLetter.equals("B"))
							break;
						
						move = Integer.toString(i) + Integer.toString(j) + 
							   Integer.toString(i) + Integer.toString(j+(k+1));
						
						availableMoves.add(move);
						
						// prevent detouring a chesspart to attack the other
						if(firstLetter.equals("W") || firstLetter.equals("P"))
							break;
					}
				}
				else // it is the king
				{
					// check if it can move upwards
					if((i-1) >= 0)
					{
						firstLetter = Character.toString(board[i-1][j].charAt(0));
						
						if(!firstLetter.equals("B"))
						{
							move = Integer.toString(i) + Integer.toString(j) + 
								   Integer.toString(i-1) + Integer.toString(j);
								
							availableMoves.add(move);	
						}
					}
					
					// check if it can move downwards
					if((i+1) < rows)
					{
						firstLetter = Character.toString(board[i+1][j].charAt(0));
						
						if(!firstLetter.equals("B"))
						{
							move = Integer.toString(i) + Integer.toString(j) + 
								   Integer.toString(i+1) + Integer.toString(j);
								
							availableMoves.add(move);	
						}
					}
					
					// check if it can move on the left
					if((j-1) >= 0)
					{
						firstLetter = Character.toString(board[i][j-1].charAt(0));
						
						if(!firstLetter.equals("B"))
						{
							move = Integer.toString(i) + Integer.toString(j) + 
								   Integer.toString(i) + Integer.toString(j-1);
								
							availableMoves.add(move);	
						}
					}
					
					// check if it can move on the right
					if((j+1) < columns)
					{
						firstLetter = Character.toString(board[i][j+1].charAt(0));
						
						if(!firstLetter.equals("B"))
						{
							move = Integer.toString(i) + Integer.toString(j) + 
								   Integer.toString(i) + Integer.toString(j+1);
								
							availableMoves.add(move);	
						}
					}
				}			
			}	
		}
	}
	/**
	 * This function is used to find the available moves for white
	 * @param board The board current state
	 */
	private void whiteMoves(String[][] board)
	{
		String firstLetter = "";
		String secondLetter = "";
		String move = "";
				
		for(int i=0; i<rows; i++)
		{
			for(int j=0; j<columns; j++)
			{
				firstLetter = Character.toString(board[i][j].charAt(0));
				
				// if it there is not a white chess part in this position then keep on searching
				if(firstLetter.equals("B") || firstLetter.equals(" ") || firstLetter.equals("P"))
					continue;
				
				// check the kind of the white chess part
				secondLetter = Character.toString(board[i][j].charAt(1));
				
				if(secondLetter.equals("P"))	// it is a pawn
				{
					
					// check if it can move one vertical position ahead
					firstLetter = Character.toString(board[i-1][j].charAt(0));
					
					if(firstLetter.equals(" ") || firstLetter.equals("P"))
					{
						move = Integer.toString(i) + Integer.toString(j) + 
							   Integer.toString(i-1) + Integer.toString(j);
						
						availableMoves.add(move);
					}
					
					// check if it can move crosswise to the left
					if(j!=0 && i!=0)
					{
						firstLetter = Character.toString(board[i-1][j-1].charAt(0));						
						if(!(firstLetter.equals("W") || firstLetter.equals(" ") || firstLetter.equals("P"))) {
							move = Integer.toString(i) + Integer.toString(j) + 
									   Integer.toString(i-1) + Integer.toString(j-1);
								
							availableMoves.add(move);
						}											
					}
					
					// check if it can move crosswise to the right
					if(j!=columns-1 && i!=0)
					{
						firstLetter = Character.toString(board[i-1][j+1].charAt(0));
						if(!(firstLetter.equals("W") || firstLetter.equals(" ") || firstLetter.equals("P"))) {
							
							move = Integer.toString(i) + Integer.toString(j) + 
									   Integer.toString(i-1) + Integer.toString(j+1);							
							availableMoves.add(move);
						}
					}
				}
				else if(secondLetter.equals("R"))	// it is a rook
				{
					// check if it can move upwards
					for(int k=0; k<rookBlocks; k++)
					{
						if((i-(k+1)) < 0)
							break;
						
						firstLetter = Character.toString(board[i-(k+1)][j].charAt(0));
						
						if(firstLetter.equals("W"))
							break;
						
						move = Integer.toString(i) + Integer.toString(j) + 
							   Integer.toString(i-(k+1)) + Integer.toString(j);
						
						availableMoves.add(move);
						
						// prevent detouring a chesspart to attack the other
						if(firstLetter.equals("B") || firstLetter.equals("P"))
							break;
					}
					
					// check if it can move downwards
					for(int k=0; k<rookBlocks; k++)
					{
						if((i+(k+1)) == rows)
							break;
						
						firstLetter = Character.toString(board[i+(k+1)][j].charAt(0));
						
						if(firstLetter.equals("W"))
							break;
						
						move = Integer.toString(i) + Integer.toString(j) + 
							   Integer.toString(i+(k+1)) + Integer.toString(j);
						
						availableMoves.add(move);
						
						// prevent detouring a chesspart to attack the other
						if(firstLetter.equals("B") || firstLetter.equals("P"))
							break;
					}
					
					// check if it can move on the left
					for(int k=0; k<rookBlocks; k++)
					{
						if((j-(k+1)) < 0)
							break;
						
						firstLetter = Character.toString(board[i][j-(k+1)].charAt(0));
						
						if(firstLetter.equals("W"))
							break;
						
						move = Integer.toString(i) + Integer.toString(j) + 
							   Integer.toString(i) + Integer.toString(j-(k+1));
						
						availableMoves.add(move);
						
						// prevent detouring a chesspart to attack the other
						if(firstLetter.equals("B") || firstLetter.equals("P"))
							break;
					}
					
					// check of it can move on the right
					for(int k=0; k<rookBlocks; k++)
					{
						if((j+(k+1)) == columns)
							break;
						
						firstLetter = Character.toString(board[i][j+(k+1)].charAt(0));
						
						if(firstLetter.equals("W"))
							break;
						
						move = Integer.toString(i) + Integer.toString(j) + 
							   Integer.toString(i) + Integer.toString(j+(k+1));
						
						availableMoves.add(move);
						
						// prevent detouring a chesspart to attack the other
						if(firstLetter.equals("B") || firstLetter.equals("P"))
							break;
					}
				}
				else // it is the king
				{
					// check if it can move upwards
					if((i-1) >= 0)
					{
						firstLetter = Character.toString(board[i-1][j].charAt(0));
						
						if(!firstLetter.equals("W"))
						{
							move = Integer.toString(i) + Integer.toString(j) + 
								   Integer.toString(i-1) + Integer.toString(j);
								
							availableMoves.add(move);	
						}
					}
					
					// check if it can move downwards
					if((i+1) < rows)
					{
						firstLetter = Character.toString(board[i+1][j].charAt(0));
						
						if(!firstLetter.equals("W"))
						{
							move = Integer.toString(i) + Integer.toString(j) + 
								   Integer.toString(i+1) + Integer.toString(j);
								
							availableMoves.add(move);	
						}
					}
					
					// check if it can move on the left
					if((j-1) >= 0)
					{
						firstLetter = Character.toString(board[i][j-1].charAt(0));
						
						if(!firstLetter.equals("W"))
						{
							move = Integer.toString(i) + Integer.toString(j) + 
								   Integer.toString(i) + Integer.toString(j-1);
								
							availableMoves.add(move);	
						}
					}
					
					// check if it can move on the right
					if((j+1) < columns)
					{
						firstLetter = Character.toString(board[i][j+1].charAt(0));
						
						if(!firstLetter.equals("W"))
						{
							move = Integer.toString(i) + Integer.toString(j) + 
								   Integer.toString(i) + Integer.toString(j+1);
								
							availableMoves.add(move);	
						}
					}
				}			
			}	
		}
	}
	
	
	private String selectRandomAction()
	{		
		Random ran = new Random();
		int x = ran.nextInt(availableMoves.size());
		
		return availableMoves.get(x);
	}
	/**
	 * This part is the Minimax algorithm
	 * @param depth The depth that we will cut the search
	 * @return A selected move for the agent to play
	 */
	
	private String selectMinmax(int depth, boolean ab_On){
		double start = System.currentTimeMillis();
		String best_move = new String();
		double max = -inf;
		double min = inf;
		int max_depth = depth;
		ArrayList<String> myAvailableMoves = new ArrayList<>();
		
		if(symmetric_pruning(board)) {
			for(int i = 0; i < availableMoves.size(); i++) {
				if(Character.getNumericValue(availableMoves.get(i).charAt(3)) == 3 || Character.getNumericValue(availableMoves.get(i).charAt(3)) == 4){
					myAvailableMoves.add(availableMoves.get(i));
				}
			}
			max_depth = 7;
		}		
				
		myAvailableMoves = sort_dist(myAvailableMoves);
				
		for(String move : myAvailableMoves){
			/* Deep copy the board*/
			String[][] tmp_board = new String[rows][columns];
			
			for(int i=0; i<rows; i++)
				for(int j=0; j<columns; j++)
					tmp_board[i][j] = board[i][j];

			tmp_board = simulate_move(tmp_board, move);
						
			if(myColor == 0) { // We are the maximizer
				double score = minimize(tmp_board, max_depth-1, -inf, inf, ab_On);		
				if (score > max){
					max = score;
					best_move = move;
				}
			}
			else {				// We are the minimizer
				double score = maximize(tmp_board, max_depth-1, -inf, inf, ab_On);		
				if (score < min){
					min = score;
					best_move = move;
				}
			}			
		}
		double end = System.currentTimeMillis();
		System.out.println("Execution time = "+ (end-start));
		return best_move;
	}
	/**
	 * This is a function for the maximizer. In our case the white player is the maximizer
	 * @param tmp_board The boards current layout
	 * @param depth The depth from the cut-off 
	 * @param alpha Alpha used for the alpha-beta pruning
	 * @param beta Beta used for the alpha-beta pruning
	 * @return Evaluation of the moves
	 */
	private double maximize(String[][] tmp_board, int depth, double alpha, double beta, boolean ab_On){
		

		double evaluation = evaluate_board(tmp_board);
		//System.out.println("ev = "+ evaluation);
		if (depth == 0){// || evaluation == 1000 || evaluation == -1000){//|| evaluation == 1000){
			return evaluation;
		}
		
		availableMoves = new ArrayList<String>();
		this.whiteMoves(tmp_board);
		
		if (availableMoves.size() == 0) {
			if (this.score_black > this.score_white)
				return -1000;
			else
				return 1000;
		}
		
		double max = -inf;
		
		ArrayList<String> myAvailableMoves = new ArrayList<>();
		
		if(symmetric_pruning(tmp_board)) {
	
			for(int i = 0; i < availableMoves.size(); i++) {
				if(Character.getNumericValue(availableMoves.get(i).charAt(3)) == 3 || Character.getNumericValue(availableMoves.get(i).charAt(3)) == 4){
					myAvailableMoves.add(availableMoves.get(i));
				}
			}
		}
		myAvailableMoves = sort_dist(myAvailableMoves);
		
		for( String move : myAvailableMoves){
			/* Deep copy the board*/
			String[][] tmp_board2 = new String[rows][columns];
			
			for(int i=0; i<rows; i++)
				for(int j=0; j<columns; j++)
					tmp_board2[i][j] = tmp_board[i][j];
			
			tmp_board2 = simulate_move(tmp_board2, move);
			
			double score = minimize(tmp_board2, depth-1, alpha, beta, ab_On);
			max = Math.max(max, score);

			if (ab_On) {
				alpha = Math.max(max, alpha);
				
				if (beta <= alpha) {
					break;
				}
			}

			
		}

		return max;
	}
	/**
	 * This is a function for the minimizer. In our case the black player is the minimizer
	 * @param tmp_board The boards current layout
	 * @param depth The depth from the cut-off 
	 * @param alpha Alpha used for the alpha-beta pruning
	 * @param beta Beta used for the alpha-beta pruning
	 * @return Evaluation of the moves
	 */
	private double minimize(String[][] tmp_board, int depth, double alpha, double beta, boolean ab_On){
		
		double evaluation = evaluate_board(tmp_board);
		
		if(depth == 0) {// || evaluation == -1000 || evaluation == 1000){
			return evaluation;
		}
		
		availableMoves = new ArrayList<String>();
		this.blackMoves(tmp_board);
		
		if (availableMoves.size() == 0) {
			if (this.score_black > this.score_white)
				return -1000;
			else
				return 1000;
		}
		
		double min = inf;
		
		ArrayList<String> myAvailableMoves = new ArrayList<>();
		
		if(symmetric_pruning(tmp_board)) {
		
			for(int i = 0; i < availableMoves.size(); i++) {
				if(Character.getNumericValue(availableMoves.get(i).charAt(3)) == 3 || Character.getNumericValue(availableMoves.get(i).charAt(3)) == 4){
					myAvailableMoves.add(availableMoves.get(i));
				}
			}
		}
		
		myAvailableMoves = sort_dist(myAvailableMoves);
		
		for( String move : myAvailableMoves){
			/* Deep copy the board*/
			String[][] tmp_board2 = new String[rows][columns];
			
			for(int i=0; i<rows; i++)
				for(int j=0; j<columns; j++)
					tmp_board2[i][j] = tmp_board[i][j];
			
			tmp_board2 = simulate_move(tmp_board2, move);
			
			double score = maximize(tmp_board2, depth-1,  alpha, beta, ab_On);
			min = Math.min(min, score);			
			
			if(ab_On) {
				beta = Math.min(beta, min);

				if (beta <= alpha) {
					break;
				}
			}

		}

		return min;
	}

	/**
	 * This is a simple evaluation function that counts the board piece worth
	 * @return evaluation
	 */
	private double evaluate_board(String[][] tmp_board){
		
		double evaluation = 0;
		boolean white_wins = true, black_wins = true;
		
		int counter_rows = 0;
		int counter_cols;
		evaluation = score_white - score_black;
		
		for(String[] row : tmp_board){
			counter_cols = 0; 
			for (String square : row){
				
				if (square == "BP"){					
					evaluation = evaluation - 1;
					
					if (counter_rows < 6) {
						if (counter_cols < 4) {
							evaluation = evaluation - 0.1; // Expand on the board
							if (tmp_board[counter_rows + 1][counter_cols + 1] == "WP" || tmp_board[counter_rows + 1][counter_cols + 1] == "ΒP") {
								evaluation = evaluation - 0.25;
							}
							else if (tmp_board[counter_rows + 1][counter_cols + 1] == "WR" || tmp_board[counter_rows + 1][counter_cols + 1] == "ΒR") {
								evaluation = evaluation - 0.5;
							}
							else if (tmp_board[counter_rows + 1][counter_cols + 1] == "WK") {
								evaluation = evaluation - 1;
							}
						}
						if (counter_cols > 0) {
							evaluation = evaluation - 0.1; // Expand on the board
							if (tmp_board[counter_rows + 1][counter_cols - 1] == "WP" || tmp_board[counter_rows + 1][counter_cols - 1] == "ΒP") {
								evaluation = evaluation - 0.25;
							}
							else if (tmp_board[counter_rows + 1][counter_cols - 1] == "WR" || tmp_board[counter_rows + 1][counter_cols - 1] == "ΒR") {
								evaluation = evaluation - 0.5;
							}
							else if (tmp_board[counter_rows + 1][counter_cols - 1] == "WK") {
								evaluation = evaluation - 1;
							}
						}
					}
				}
				else if (square == "BR") {					
					evaluation = evaluation - 3;
					for(int i=1; i < 4; i++) {
						if (counter_cols + i < 4) {
							if (tmp_board[counter_rows][counter_cols + i] == " " || tmp_board[counter_rows][counter_cols + i] == "P" ) {
								evaluation = evaluation - 0.05;
							}
							else {
								evaluation = evaluation - 0.05;
								break;
							}
						}

					}
					for(int i=1; i < 4; i++) {
						if (counter_cols - i > 0) {
							if (tmp_board[counter_rows][counter_cols - i] == " " || tmp_board[counter_rows][counter_cols - i] == "P" ) {
								evaluation = evaluation - 0.05;
							}
							else {
								evaluation = evaluation - 0.05;
								break;
							}
						}
					}
					for(int i=1; i < 4; i++) {
						if (counter_rows - i > 0) {
							if (tmp_board[counter_rows-i][counter_cols] == " " || tmp_board[counter_rows-i][counter_cols] == "P" ) {
								evaluation = evaluation - 0.05;
							}
							else {
								evaluation = evaluation - 0.05;
								break;
							}
						}
					}
					for(int i=1; i < 4; i++) {
						if (counter_rows + i < 6) {
							if (tmp_board[counter_rows+i][counter_cols] == " " || tmp_board[counter_rows+i][counter_cols] == "P" ) {
								evaluation = evaluation - 0.05;
							}
							else {
								evaluation = evaluation - 0.05
										;
								break;
							}
						}
					}
				}
				else if (square == "BK") {
					white_wins = false;
					evaluation = evaluation - 8;
				}
				else if (square == "WP") {			
					evaluation = evaluation + 1;
					if (counter_rows > 0) {
						if (counter_cols < 4) {
							evaluation = evaluation + 0.1; // Expand on the board
							if (tmp_board[counter_rows - 1][counter_cols + 1] == "BP" || tmp_board[counter_rows - 1][counter_cols + 1] == "WP") {
								evaluation = evaluation + 0.25;
							}
							else if (tmp_board[counter_rows - 1][counter_cols + 1] == "BR" || tmp_board[counter_rows - 1][counter_cols + 1] == "WR") {
								evaluation = evaluation + 0.5;
							}
							else if (tmp_board[counter_rows - 1][counter_cols + 1] == "BK") {
								evaluation = evaluation + 1;
							}
						}
						if (counter_cols > 0) {
							evaluation = evaluation + 0.1; // Expand on the board
							if (tmp_board[counter_rows - 1][counter_cols - 1] == "BP" || tmp_board[counter_rows - 1][counter_cols - 1] == "WP") {
								evaluation = evaluation + 0.25;
							}
							else if (tmp_board[counter_rows - 1][counter_cols - 1] == "BR" || tmp_board[counter_rows - 1][counter_cols - 1] == "WR") {
								evaluation = evaluation + 0.5;
							}
							else if (tmp_board[counter_rows - 1][counter_cols - 1] == "BK") {
								evaluation = evaluation + 1;
							}
						}
					}
				}
				else if (square == "WR") {		
					evaluation = evaluation + 3;
					for(int i=1; i < 4; i++) {
						if (counter_cols + i < 4) {
							if (tmp_board[counter_rows][counter_cols + i] == " " || tmp_board[counter_rows][counter_cols + i] == "P" ) {
								evaluation = evaluation + 0.05;
							}
							else {
								evaluation = evaluation + 0.05;
								break;
							}
						}

					}
					for(int i=1; i < 4; i++) {
						if (counter_cols - i > 0) {
							if (tmp_board[counter_rows][counter_cols - i] == " " || tmp_board[counter_rows][counter_cols - i] == "P" ) {
								evaluation = evaluation + 0.05;
							}
							else {
								evaluation = evaluation + 0.05;
								break;
							}
						}
					}
					for(int i=1; i < 4; i++) {
						if (counter_rows - i > 0) {
							if (tmp_board[counter_rows-i][counter_cols] == " " || tmp_board[counter_rows-i][counter_cols] == "P" ) {
								evaluation = evaluation + 0.05;
							}
							else {
								evaluation = evaluation + 0.05;
								break;
							}
						}
					}
					for(int i=1; i < 4; i++) {
						if (counter_rows + i < 6) {
							if (tmp_board[counter_rows+i][counter_cols] == " " || tmp_board[counter_rows+i][counter_cols] == "P" ) {
								evaluation = evaluation + 0.05;
							}
							else {
								evaluation = evaluation + 0.05;
								break;
							}
						}
					}
				}
				else if (square == "WK") {	
					black_wins = false;
					evaluation = evaluation + 8;
				}
				
				else if (square == "P") {
					if (myColor == 0) // White player
						evaluation = evaluation - 0.7;
					else
						evaluation = evaluation + 0.7;
				}
				counter_cols = counter_cols + 1;
			}
			counter_rows = counter_rows + 1;
		}
				
		if(white_wins && this.score_white + 8 > this.score_black) {
			evaluation = 1000;
		}
		else if(white_wins && this.score_white + 8 < this.score_black)
			evaluation = -1000;
		
		if(black_wins && this.score_white < this.score_black + 8) {
			evaluation = -1000;
		}
		else if(black_wins && this.score_white > this.score_black + 8)
			evaluation = 1000;
		
		return evaluation;
	}
	
	private ArrayList<String> sort_dist(ArrayList<String> avail) {
		ArrayList<String> tmp = new ArrayList<>();
		for(String move : availableMoves) {
			int x, y;
			x = Character.getNumericValue(move.charAt(2));
			y = Character.getNumericValue(move.charAt(3));
			double dist = Math.abs(Math.floor(rows/2)-x) + Math.abs(Math.floor(columns/2)-y);
			
			if(tmp.isEmpty()) {
				tmp.add(move);
			}
			else {
				int x2 = Character.getNumericValue(tmp.get(0).charAt(2));
				int y2 = Character.getNumericValue(tmp.get(0).charAt(3));
				int el_dist = (int) (Math.abs(Math.floor(rows/2)-x2) + Math.abs(Math.floor(columns/2)-y2));
				if(el_dist > dist)
					tmp.add(0, move);
				else
					tmp.add(move);
			}
			
		}
		return tmp;
		
	}
	
	private boolean symmetric_pruning(String[][] board) {
		for(int i = 0; i < Math.floor(columns/2) ; i++) {
			for(int j = 0; j < rows; j++) {
				if(!(board[j][i] == board[j][columns - i - 1])) {
					return false;
				}
			}
		}	
		return true;
	}
	/**
	 * This is the monte carlo tree search algorithm
	 * @return A selected move to play
	 */
	private String UCTSearch() {
		// Make the initial node
		String[][] initial_board = new String[rows][columns];
		for(int i=0; i<rows; i++)
			for(int j=0; j<columns; j++)
				initial_board[i][j] = board[i][j];
		
		Node node_initial;
		if (myColor == 0) {
			node_initial = new Node(initial_board, null, true);
		}
		else{
			node_initial = new Node(initial_board, null, false);
		}

		// Computational bound is the time in seconds for our turn.
		long startTime = System.currentTimeMillis();
		while(System.currentTimeMillis()-startTime < 4000) {
			
			Node selected_node = TreePolicy(node_initial);

			int reward = DefaultPolicy(selected_node);

			BackupNegamax(selected_node, reward);
			
		}
		Node bChild = BestChild(node_initial,0);
		System.out.println("N = "+node_initial.N);
		return node_initial.actions.get(bChild);
	}
	
	private Node TreePolicy(Node node) {
		
		while (isNonTerminal(node.key)) {
			if(isNotFullyExpanded(node)) {
				return Expand(node);
			}
			else {
				node = BestChild(node, (1/Math.sqrt(2)));
			}
		}
		
		return node;
	}
	/**
	 * Expand the current nodes child
	 * @param node The node to expand
	 * @return The expanded child
	 */
	private Node Expand(Node node) {
		
		// Deep copy the board
		String[][] tmp_board = new String[rows][columns];
		
		for(int i=0; i<rows; i++)
			for(int j=0; j<columns; j++)
				tmp_board[i][j] = node.key[i][j];
		
		String move = null;
		
		// Make move
		if (node.white) { // White available moves
			availableMoves = new ArrayList<String>();
			this.whiteMoves(tmp_board);
		}
		else {// BLack available moves
			availableMoves = new ArrayList<String>();
			this.blackMoves(tmp_board);
		}
		
		for(int i=0; i < availableMoves.size(); i++) {
			move = availableMoves.get(i);
			if (!node.moves.contains(move)) {
				node.moves.add(move);
				break;
			}
		}
		// Make move
		tmp_board = simulate_move(tmp_board, move);
		/*
		tmp_board[Character.getNumericValue(move.charAt(2))][Character.getNumericValue(move.charAt(3))] 
				= node.key[Character.getNumericValue(move.charAt(0))][Character.getNumericValue(move.charAt(1))];
		tmp_board[Character.getNumericValue(move.charAt(0))][Character.getNumericValue(move.charAt(1))] = " ";
		
		if(Character.getNumericValue(move.charAt(2)) == 0 && tmp_board[Character.getNumericValue(move.charAt(2))][Character.getNumericValue(move.charAt(3))] == "WP") {
			tmp_board[Character.getNumericValue(move.charAt(2))][Character.getNumericValue(move.charAt(3))] = " ";
		}
		else if(Character.getNumericValue(move.charAt(2)) == 6 && tmp_board[Character.getNumericValue(move.charAt(2))][Character.getNumericValue(move.charAt(3))] == "BP") {
			tmp_board[Character.getNumericValue(move.charAt(2))][Character.getNumericValue(move.charAt(3))] = " ";
		}
		*/
		// Make it node and add to list
		Node tmp_node = new Node(tmp_board, node, !node.white);
		node.child.add(tmp_node);
		node.actions.put(tmp_node, move);
		return tmp_node;
	}
	/**
	 * Choose the best child of this node base on the UCT
	 * @param node 
	 * @param Cp constant
	 * @return The best child node based on the Upper Confidence Bound for Trees
	 */
	private Node BestChild(Node node, double Cp) {
		double best_UCT = -inf;
		Node best_child = new Node(null, null, !node.white);
		for (Node child : node.child) {
			double child_UCT = (child.Q/child.N) + 2 * Cp * Math.sqrt(Math.log(node.N)/child.N);
			if (child_UCT > best_UCT) {
				best_UCT = child_UCT;
				best_child = child;
			}
		}
		return best_child;
	}
	/**
	 * This part is responsible for the simulation
	 * @param selected_node
	 * @return The best child node based on the Upper Confidence Bound for Trees
	 */
	private int DefaultPolicy(Node selected_node) {
		Random rand = new Random();
		String move; 
		String[][] tmp_board = new String[rows][columns];
		
		for(int i=0; i<rows; i++) {
			for(int j=0; j<columns; j++) {
				tmp_board[i][j] = selected_node.key[i][j];
			}
		}
		boolean color = selected_node.white;

		while(isNonTerminal(tmp_board)) {
			// Make move
			if (color) { // White available moves
				availableMoves = new ArrayList<String>();
				this.whiteMoves(tmp_board);
			}
			else {// BLack available moves
				availableMoves = new ArrayList<String>();
				this.blackMoves(tmp_board);
			}
			color = !color;
			int move_num = availableMoves.size();
			if(move_num == 0) {
				break;
			}
			move = availableMoves.get(rand.nextInt(move_num));
			tmp_board = simulate_move(tmp_board, move);
			
		}

		return whiteWon(tmp_board);
	}
	/**
	 * Update the statistics of the selected tree
	 * @param node The node which we started the simulation
	 * @param reward Which player won
	 */
	private void BackupNegamax(Node node, int reward) {
		while (!(node.parent == null)) {
			node.N = node.N + 1;
			node.Q = node.Q + reward;
			reward = -reward;
			node = node.parent;
		}
		node.N = node.N + 1;
		node.Q = node.Q + reward;
		reward = -reward;
	}
	
	/**
	 * Checks if the state of the board is non terminal
	 * @param node The board
	 * @return True or False
	 */
	private boolean isNonTerminal(String[][] node) {
		boolean white_wins = true, black_wins = true;
		for(int i=0; i<rows; i++) {
			for(int j=0; j<columns; j++) {

				//System.out.print(node[i][j]);
				if(node[i][j] == "BK") {
					white_wins = false;
				}
				else if(node[i][j] == "WK")  {
					black_wins = false;
				}
			}
			//System.out.println();
		}
		//System.out.println("-------------");

		return !(white_wins || black_wins);
	}
	/**
	 * This function returns 1 if white won, or -1 if black won
	 * @param node
	 * @return Which player won
	 */
	private int whiteWon(String[][] node) {
		int white_wins = 1;
		for(int i=0; i<rows; i++) {
			for(int j=0; j<columns; j++) {
				if(node[i][j] == "BK") {
					white_wins = -1;
				}
//				else if (node[i][j] == "WK") {
//					white_wins = 1;
//				}
//				else
//					white_wins = 0;
			}
		}
		return white_wins;
	}
	/**
	 * Checks if the node is fully expanded
	 * @param node The node
	 * @return True or False
	 */
	private boolean isNotFullyExpanded(Node node) {
		if(availableMoves.size() == node.child.size()) {
			return false;
		}
		return true;
	}
	
	private String[][] simulate_move(String[][] tmp_board, String move) {

		tmp_board[Character.getNumericValue(move.charAt(2))][Character.getNumericValue(move.charAt(3))] 
				= tmp_board[Character.getNumericValue(move.charAt(0))][Character.getNumericValue(move.charAt(1))];
		tmp_board[Character.getNumericValue(move.charAt(0))][Character.getNumericValue(move.charAt(1))] = " ";

		if(Character.getNumericValue(move.charAt(2)) == 0 && tmp_board[Character.getNumericValue(move.charAt(2))][Character.getNumericValue(move.charAt(3))] == "WP") {
			tmp_board[Character.getNumericValue(move.charAt(2))][Character.getNumericValue(move.charAt(3))] = " ";
		}
		else if(Character.getNumericValue(move.charAt(2)) == 6 && tmp_board[Character.getNumericValue(move.charAt(2))][Character.getNumericValue(move.charAt(3))] == "BP") {
			tmp_board[Character.getNumericValue(move.charAt(2))][Character.getNumericValue(move.charAt(3))] = " ";
		}
		return tmp_board;
		
	}

	public double getAvgBFactor()
	{
		return nBranches / (double) nTurns;
	}
	
	public void makeMove(int x1, int y1, int x2, int y2, int prizeX, int prizeY)
	{
		String chesspart = Character.toString(board[x1][y1].charAt(1));
		
		boolean pawnLastRow = false;
		
		// check if it is a move that has made a move to the last line
		if(chesspart.equals("P"))
			if( (x1==rows-2 && x2==rows-1) || (x1==1 && x2==0) )
			{
				board[x2][y2] = " ";	// in a case an opponent's chess part has just been captured
				board[x1][y1] = " ";
				pawnLastRow = true;
			}
		
		// otherwise
		if(!pawnLastRow)
		{
			board[x2][y2] = board[x1][y1];
			board[x1][y1] = " ";
		}
		
		// check if a prize has been added in the game
		if(prizeX != noPrize)
			board[prizeX][prizeY] = "P";
	}
	
}

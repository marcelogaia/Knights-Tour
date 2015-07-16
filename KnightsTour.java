import java.util.ArrayList;


public class KnightsTour {
	public static int BOARD_SIZE = 6;
	public static final int EMPTY = 0;
	//public static final int KNIGHT = 1;
	
	private int[][] board;
	private int[][] moves = new int[8][2];
	private int moveCount;
	
	public static void main(String[] args) {
		
		String boardSize;

        for (int i=0; i < args.length; i++) {
            if (args[i].startsWith("--size=")) {
            	boardSize = args[i].substring(args[i].indexOf("=")+1);
            	
            	try{
            		BOARD_SIZE = Integer.valueOf(boardSize);	
            	} catch(NumberFormatException e){
            		System.err.println("Number inputted should be an Integer. Disconsidering \"size\"");
            	}
            }
        }
		KnightsTour k = new KnightsTour();
		long startTime = System.currentTimeMillis();
		
		k.placeKnightWarnsdorfs(0, 0);
		
		k.displayBoard();
		long endTime = System.currentTimeMillis();
		
		System.out.println(endTime - startTime + "ms");
	}

	public KnightsTour(){
		this.board = new int[BOARD_SIZE][BOARD_SIZE];
		
		this.moves[0] = new int[]{2,1};
		this.moves[1] = new int[]{-2,1};
		this.moves[2] = new int[]{2,-1};
		this.moves[3] = new int[]{-2,-1};
		this.moves[4] = new int[]{1,2};
		this.moves[5] = new int[]{-1,2};
		this.moves[6] = new int[]{1,-2};
		this.moves[7] = new int[]{-1,-2};
		
		this.moveCount = 1;
	}
	
	public void clearBoard(){
		for(int i = 0; i < BOARD_SIZE; i++){
			for(int j = 0; j < BOARD_SIZE; j++){
				removeKnight(i, j);
			}	
		}
	}
	
	public boolean isBoardFull(){
		for(int i = 0; i < BOARD_SIZE; i++){
			for(int j = 0; j < BOARD_SIZE; j++){
				if(this.board[i][j] == 0) return false;
			}
		}
		
		return true;
	}
	
	public void displayBoard(){
		for(int i = 0; i < BOARD_SIZE; i++){
			for(int j = 0; j < BOARD_SIZE; j++){
				System.out.print("[");
				if(board[i][j] > 0) System.out.print(String.format("%02d", board[i][j]));
				else System.out.print("  ");
				System.out.print("]");
			}
			System.out.println();
		}
	}

	private void removeKnight(int row, int column) {
		this.board[row][column] = EMPTY;
		this.moveCount--;
	}

	private void setKnight(int row, int column) {
		this.board[row][column] = this.moveCount;
		this.moveCount++;
	}
	
	private boolean placeKnight(int row, int column){
		 int moveCount = 0;
		//System.out.println("row: " + row + ", column: " + column);
		if(this.isBoardFull()){
			return true;
		} else {
			boolean knightPlaced = false;
			
			while(!knightPlaced && (moveCount < this.moves.length) ){
				if(possibleMove(row,column)){
					
					setKnight(row,column);
					//System.out.println("Knight placed.");
					//System.out.println();
					//this.displayBoard();
					knightPlaced = placeKnight( row + this.moves[moveCount][0], column + this.moves[moveCount][1]);
					
					if(!knightPlaced){
						// BACKTRACKING
						//System.out.println("Backtracking...");
						this.removeKnight(row, column);
					}
				}
				moveCount++;
			}
			return knightPlaced;
		}
	}
	
	private boolean placeKnightWarnsdorfs(int row, int column){
		 int moveCount = 0;
		if(this.isBoardFull()){
			return true;
		} else {
			boolean knightPlaced = false;
			
			while(!knightPlaced && (moveCount < this.moves.length) ){
				if(possibleMove(row,column)){
					
					setKnight(row,column);

					ArrayList<Object[]> al = new ArrayList<Object[]>();
					int[] bestMove = new int[2];
					
					for(int[] move : this.moves){
						int[] nextMove = new int[2];
						nextMove[0] = row + move[0];
						nextMove[1] = column + move[1];
						
						if(possibleMove(nextMove[0], nextMove[1])){
							Object[] o = new Object[2];
							int possibleMoves = 0;
							for(int[] nMove : this.moves){
								if(possibleMove(nextMove[0] + nMove[0], nextMove[1] + nMove[1]))
									possibleMoves++;
							}
							
							o[0] = possibleMoves;
							o[1] = move;
							al.add(o);
						}
					}
					
					int bestMoveCount = 10;
					for(Object[] o : al){
						if((int)o[0] < bestMoveCount){
							bestMoveCount = (int)o[0];
							bestMove = (int[]) o[1];
						}
					}
					
					knightPlaced = placeKnightWarnsdorfs( bestMove[0], bestMove[1]);
					
					if(!knightPlaced){
						// BACKTRACKING
						this.removeKnight(row, column);
					}
				}
				moveCount++;
			}
			return knightPlaced;
		}
	}
	
	private boolean possibleMove(int row, int column){
		if ( (row < 0) || (row >= BOARD_SIZE) || (column < 0) || (column >= BOARD_SIZE) ) return false;
		else if (this.board[row][column] > 0) return false;
		else return true;
	}
}

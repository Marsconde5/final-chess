// ChessBoard class (original, from pt 1)


import java.util.Vector;
import pieces.*;

public class ChessBoard {
    Piece[][] board = new Piece[8][8];

    public ChessBoard() {initializeBoard();}

    Vector<String> capturedPiecesW = new Vector<>();
    Vector<String> capturedPiecesB = new Vector<>();

/**
 *  The initializeBoard method initalizes the board and adds all the pieces of black
 *  and white to the board.
 * s
 * This method is always called by main as it is crucial to start the game. This method
 * is a void method and does not have any return value.
 * i
 */

    public void initializeBoard() {
        // Set up the black pieces
        board[0][0] = new Rook("black");
        board[0][1] = new Knight("black");
        board[0][2] = new Bishop("black");
        board[0][3] = new Queen("black");
        board[0][4] = new King("black");
        board[0][5] = new Bishop("black");
        board[0][6] = new Knight("black");
        board[0][7] = new Rook("black");
        for (int i = 0; i < 8; i++) {board[1][i] = new Pawn("black");}

        // Set up the white pieces
        board[7][0] = new Rook("white");
        board[7][1] = new Knight("white");
        board[7][2] = new Bishop("white");
        board[7][3] = new Queen("white");
        board[7][4] = new King("white");
        board[7][5] = new Bishop("white");
        board[7][6] = new Knight("white");
        board[7][7] = new Rook("white");
        for (int i = 0; i < 8; i++) {board[6][i] = new Pawn("white");}

    }
/**
 * This method prints the board when called by the main function.
 * This method is a void statement and has no return values. In addition this method
 * has no parameters.
 */

        public void printBoard() {
            // Om's work here: Print the chessboard and alternate ## to represent squares.
            System.out.println("  A  B  C  D  E  F  G  H");
            for (int i = 0; i < 8; i++) {
                System.out.print((8 - i) + " ");
                for (int j = 0; j < 8; j++) {
                    if (board[i][j] == null) {
                        if ((i + j) % 2 == 0) {
                            System.out.print("## ");
                        } else {
                            System.out.print("   ");
                        }
                    } else {
                        System.out.print(board[i][j] + " ");
                    }
                }
                System.out.println((8 - i));
            }
            System.out.println("  A  B  C  D  E  F  G  H");
            System.out.print("White's captured pieces: ");
            for(int i = 0; i < capturedPiecesW.size(); i++){
                System.out.print(capturedPiecesW.get(i)+", ");
                
            }
            System.out.println(); 
            System.out.print("Black's captured pieces: ");
            for(int i = 0; i < capturedPiecesB.size(); i++){
                System.out.print(capturedPiecesB.get(i)+", ");
            }
            System.out.println(); 
        }

/**
 *  The movePiece method takes input(the starting x and y cordinates of the the piece the user desires to move and then the cordinates the user wants to move the piece to).
 *  the method then carries out the move.
 * @return The movePiece method returns a bool value (true if the move is made, false if the move isnt made)
 *  The movePiece method takes in five paramaters
 *  @param startX: the x cordinate of the desired pieces starting position
 *  @param startY: the y cordinate of the desired pieces starting position
 *  @param endX: the x cordinate of the desired move's square
 *  @param endY: the y cordinate of the desired move's square
 *  @param currentPlayer: a string containing the player whos turn it currently is.
 */
    public boolean movePiece(int startX, int startY, int endX, int endY, String currentPlayer) {
        Piece piece = board[startX][startY];
        if (piece == null || !piece.color.equals(currentPlayer)) {return false;}
        if (piece.isValidMove(startX, startY, endX, endY, board) /* && checkPath(startX, startY, endX, endY, board)*/) {
            // Capture or move
            if (board[endX][endY] == null || !board[endX][endY].color.equals(currentPlayer)) {

                // add to vector list of taken pieces
                if (board[endX][endY] != null && !board[endX][endY].color.equals(currentPlayer)){
                    if (currentPlayer == "white"){
                        capturedPiecesB.add(board[endX][endY].toString());
                    }
                    else {capturedPiecesW.add(board[endX][endY].toString());
                    }
                    
                    //numTakenPieces++;
                } 
                    
                board[endX][endY] = piece;
                board[startX][startY] = null;
                return true;
            }
        }
        return false;
    }

   /**
     * Checks if the current player's king is in checkmate.
     * 
     * @param color The color of the current player ("white" or "black")
     * @return True if the player is in checkmate, false otherwise.
     */

    public boolean isCheckmated(String color){
        int[] kingPos = getKingPos(color);
        int row = kingPos[0];
        int col = kingPos[1];
        if (!isInCheck(board[row][col].color)) { return false; }

        /* to test with a fool's mate (fastest checkmate):
            f2 f3
            e7 e5
            g2 g4
            d8 h4
        */  // Check every move to see if the king can escape check


        for (int i = 0; i < 8; i++){
            for (int j = 0; j < 8; j++){
                // System.out.print(i + "" + j + ", ");

                //if (piece == null || !piece.color.equals(CurrentPlayer)) {return false;}
                 
                if (board[row][col].isValidMove(row, col, i, j, board)){
                    if (board[i][j] == null){
                        System.out.println("false, empty space");
                        return false;
                    }
                    if (!board[i][j].color.equals(board[row][col].color)){
                        System.out.println("false " + board[i][j].getClass());
                        return false;
                    }
                }                               
            }
        }
        System.out.println("True");
        return true;
    }
    /**
     * Gets the current position of the king for the given player color.
     * 
     * @param color The color of the current player ("white" or "black")
     * @return An integer array containing the x and y coordinates of the king
     */

    public int[] getKingPos(String color){
        int row = 0, col = 0;

        for(int x = 0; x<board.length; x++){
            for(int y = 0; y<board[0].length; y++){
                if(board[x][y] != null){
                    if(board[x][y].getClass().isInstance(new King("white")) && (board[x][y].color ==(color))){
                        row = x;
                        col = y;
                    }
                }
            }
        }
        int[] returnArray = new int[2];
        returnArray[0] = row;
        returnArray[1] = col;

        return returnArray;
    }
/**
     * Determines if a move would result in a stalemate.
     * 
     * @param color The color of the current player ("white" or "black")
     * @return True if no valid moves are possible and the player is not in check, false otherwise.
     */


    public boolean isStalemate(String color){
        for(int x=0; x<8; x++){
            for(int y=0; y<8; y++){
                if (board[x][y] != null && board[x][y].color == color){
                    for (int a=0; a<8; a++){
                        for (int b=0; b<8; b++){
                            // System.out.println(x + "" + y + "" + a + "" + b);
                            if (board[x][y].isValidMove (x, y, a, b, board) && (x != a) && (y != b)){ return false; }
                        }
                    }                   
                }
            }
        }
        return true;// No valid moves, player is stalemated
    } 


   /* chatgpt's method
// New Stalemate detection method
    public boolean isStalemate(String currentPlayer) {
        // If the current player is not in check, check for valid moves
        if (!isInCheck(currentPlayer)) {
            for (int x = 0; x < 8; x++) {
                for (int y = 0; y < 8; y++) {
                    if (board[x][y] != null && board[x][y].color.equals(currentPlayer)) {
                        for (int a = 0; a < 8; a++) {
                            for (int b = 0; b < 8; b++) {
                                if (board[x][y].isValidMove(x, y, a, b, board)) {
                                    return false; // There is a valid move
                                }
                            }
                        }
                    }
                }
            }
            // No valid moves and not in check -> stalemate
            return true;
        }
        return false; // Not a stalemate
    } */


/*  public boolean isJeopardizingKing(int startX, int startY, int endX, int endY, String color) {
        // Get the position of the current player's king
        int[] kingPos = getKingPos(color);
        int kingX = kingPos[0];
        int kingY = kingPos[1];
    
        // Create a copy of the current board for simulation
        Piece[][] testBoard = new Piece[8][8];
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                testBoard[i][j] = board[i][j]; // Copy each piece into testBoard
            }
        }
    
        // Simulate the move on the testBoard
        testBoard[endX][endY] = testBoard[startX][startY]; // Move the piece to the new position
        testBoard[startX][startY] = null;                  // Empty the starting position
    
        // Check if the king is in check after this move
        for (int x = 0; x < 8; x++) {
            for (int y = 0; y < 8; y++) {
                if (testBoard[x][y] != null && !testBoard[x][y].color.equals(color)) {
                    // If the opponent's piece can move to the king's position, the king is in check
                    if (testBoard[x][y].isValidMove(x, y, kingX, kingY, testBoard)) {
                        return true; // This move would jeopardize the king
                    }
                }
            }
        }
    
        return false; // This move does not jeopardize the king
    }
    */

    /** 
     * Checks if a path between two positions is clear for a piece to move through.
    
     * @param startX The x-coordinate of the starting position
     * @param startY The y-coordinate of the starting position
     * @param endX   The x-coordinate of the target position
     * @param endY   The y-coordinate of the target position
     * @param board  The current chessboard
     * @return True if the path is clear, false otherwise.
     */
 
    public boolean checkPath(int startX, int startY, int endX, int endY, Piece [][] board){
        
        if(startX == endX || startY == endY){
            if (startX < endX){
                for(int i = startX + 1; i < endX; i++){
                    if (board[i][startY] != null){
                        return false;
                    }
                }
            }
            else if (startX > endX){
                for(int i = endX + 1; i < startX; i++){
                    if (board[i][startY] != null){
                        return false;
                    }
                }
            }
            if (startY < endY){
                for(int i = startY + 1; i < endY; i++){
                    if (board[startX][i] != null){
                        return false;
                    }
                }
            }
            else if (startY > endY){
                for(int i = endY + 1; i < startY; i++){
                    if (board[startX][i] != null){
                        return false;
                    }
                }
            }
        }
        // diagonal
        else if (Math.abs(startX - endX) == Math.abs(startY - endY)) {
            int dx = (endX > startX) ? 1 : -1;
            int dy = (endY > startY) ? 1 : -1;
    
            for (int i = 1; i < Math.abs(endX - startX); i++) {
                if (board[startX + i * dx][startY + i * dy] != null) return false;
            }
        }
        return true;    
    }

    /**
     * Checks if the current player's king is in check.
     * 
     * @param color The color of the current player ("white" or "black")
     * @return True if the king is in check, false otherwise.
     */
   public boolean isInCheck(String color) {
    int[] kingPos = getKingPos(color); // Get the position of the king
    int row = kingPos[0];
    int col = kingPos[1];

    // Check every piece on the board to see if it can move to the king's position
    for (int x = 0; x < board.length; x++) {
        for (int y = 0; y < board[0].length; y++) {
            if (board[x][y] != null) {
                // Check if the current piece can move to the king's position and it's an enemy piece
                if (board[x][y].isValidMove(x, y, row, col, board) && !board[x][y].color.equals(color)) {
                    return true; // King is in check
                }
            }
        }
    }
    return false; // King is not in check
}

}

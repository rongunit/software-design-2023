import java.util.Scanner;

import static java.lang.Math.max;

public class Game {
    private final Character BLACK = 'b';
    private final Character WHITE = 'w';
    private final int WIDTH = 8;
    private final int HEIGHT = 8;
    private final Character EMPTY = '-';
    private final Character POSSIBLE = 'o';
    private final Character side;
    private Character turn;
    private Character[][] board;
    private int black_score;
    private int white_score;
    Scanner scanner;
    private final int mode;
    private final int difficulty;

    protected Game(Character side, int difficulty, int mode) {
        this.side = side;
        this.turn = BLACK;
        this.black_score = 2;
        this.white_score = 2;
        this.mode = mode;
        this.difficulty = difficulty;
        this.scanner = new Scanner(System.in);
        createBoard();
    }


    /***
     * Creates the initial board
     */
    private void createBoard() {
        board = new Character[HEIGHT][WIDTH];
        for (int i = 0; i < HEIGHT; i++) {
            for (int j = 0; j < WIDTH; j++) {
                board[i][j] = EMPTY;
            }
        }
        board[3][3] = WHITE;
        board[4][4] = WHITE;
        board[3][4] = BLACK;
        board[4][3] = BLACK;
    }

    /***
     * Main game handling method
     * @return the best score of any (human) player that game
     */
    public int processGame() {
        processPlayerVsBoth(mode);
        markPossiblePositions();
        displayBoard();
        System.out.println("Game ended with the score: b" + black_score + " w" + white_score);
        if (mode == 1) {
            return side == BLACK ? black_score : white_score;
        }
        return max(black_score, white_score);
    }

    /***
     *
     * @param mode 1 for PvBot, 2 for PvP; gets passed by the main controller method
     */
    public void processPlayerVsBoth(int mode){
        while (this.white_score != 0 && this.black_score != 0 && this.white_score + this.black_score != 64) {
            markPossiblePositions();
            displayBoard();
            if (turn == side) {
                processPlayerMove();
            } else {
                if(mode==1){ //the default mode - PLayerVsBot
                    processComputerMove();
                } else {
                    processPlayerMove();
                }
            }
        }
    }

    /***
     * Updates the board to show the currently available cells
     */
    private void markPossiblePositions() {
        for (int i = 0; i < HEIGHT; i++) {
            for (int j = 0; j < WIDTH; j++) {
                if (board[i][j] == POSSIBLE) {
                    board[i][j] = EMPTY;
                }
                if (board[i][j] == EMPTY) {
                    if (isCellAvailable(i, j)) {
                        board[i][j] = POSSIBLE;
                    }
                }
            }
        }
    }

    /***
     * Calculates if a (row,col) position is available this turn.
     * @param row row
     * @param col column
     * @return True, if the position suits the game rules
     */
    private boolean isCellAvailable(int row, int col) {
        boolean isNeighbor;
        int x;
        int y;
        for (int i = -1; i <= 1; ++i) {
            for (int j = -1; j <= 1; ++j) {
                if (i == 0 && j == 0) {
                    continue;
                }
                x = row;
                y = col;
                isNeighbor = true;
                while (true) {
                    x += i;
                    y += j;

                    if (x < 0 || x >= HEIGHT || y < 0 || y >= WIDTH) {
                        break; //current cell is off the map
                    }
                    if (board[x][y] == EMPTY || board[x][y] == POSSIBLE) {
                        break; //current neighbor cell is empty or possible
                    }
                    // if this neighbor cell is enemy-owned, continue the while loop to see if the rest of this line matches the rules
                    if (isNeighbor && board[x][y] != turn) {
                        isNeighbor = false;
                    } else if (isNeighbor) {
                        break;
                    }
                    if (board[x][y] == turn) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /***
     * A controller-method for each player move.
     * Checks the possible moves, and if they exist, offers them to the player and registers his choice
     * Redirecting the 'backend' part of it to the makeMove() method
     */
    private void processPlayerMove() {
        boolean isTurnImpossible = displayPossibleMoves();
        if (isTurnImpossible){
            System.out.println("\nNo possible moves, skipping turn... ");
        } else{
            System.out.print("Select cell: ");
            int[] cell = getInputCell();
            makeMove(cell[0], cell[1]);
        }
        turn = turn == BLACK ? WHITE : BLACK;
    }

    private int[] getInputCell() {
        int[] cell = new int[2];
        cell[0] = -1;
        cell[1] = -1;
        if (scanner.hasNextInt()) {
            cell[0] = scanner.nextInt();
            if (scanner.hasNextInt()) {
                cell[1] = scanner.nextInt();
            } else {
                scanner.nextLine();
            }
        } else {
            scanner.nextLine();
        }
        //keeps reading the input until it's valid coordinates
        while (isOutOfBoard(cell) || board[cell[0]][cell[1]] != POSSIBLE) {
            System.out.print("\nIncorrect input! Try again: ");
            if (scanner.hasNextInt()) {
                cell[0] = scanner.nextInt();
                if (scanner.hasNextInt()) {
                    cell[1] = scanner.nextInt();
                } else {
                    scanner.nextLine();
                }
            } else {
                scanner.nextLine();
            }
        }

        return cell;
    }

    private boolean isOutOfBoard(int[] cell) {
        return cell[0] < 0 || cell[0] > 7 || cell[1] < 0 || cell[1] > 7;
    }

    /***
     * Prints out the possible positions where a checker can be placed
     * @return True if there are no possible moves
     */
    private boolean displayPossibleMoves() {
        System.out.println("\nPossible moves for you:");
        boolean isEmpty = true;
        for (int i = 0; i < HEIGHT; i++) {
            for (int j = 0; j < WIDTH; j++) {
                if (board[i][j] == POSSIBLE) {
                    System.out.println(i + " " + j);
                    isEmpty = false;
                }
            }
        }
        return isEmpty;
    }

    private void processComputerMove() {
        int bestX = 0;
        int bestY = 0;
        float bestRating = 0;
        float curRating;
        for (int i = 0; i < HEIGHT; i++) {
            for (int j = 0; j < WIDTH; j++) {
                if (board[i][j] == POSSIBLE) {
                    curRating = rateMove(i, j);
                    if (curRating > bestRating) {
                        bestRating = curRating;
                        bestX = i;
                        bestY = j;
                    }
                }
            }
        }
        System.out.println("Computer moved on cell: " + bestX + " " + bestY);
        System.out.println();
        makeMove(bestX, bestY);
        turn = turn == BLACK ? WHITE : BLACK;
    }

    /***
     * Displays the current board with the score
     */
    private void displayBoard() {
        System.out.println();
        for (int i = 0; i < HEIGHT; i++) {
            System.out.print(i);
            for (int j = 0; j < WIDTH; j++) {
                System.out.print("\t");
                System.out.print(board[i][j]);
            }
            System.out.print("\t|");
            if (i == 4) {
                System.out.println("\tCurrent score: b" + black_score + " w" + white_score);
            } else {
                System.out.println();
            }
        }
        System.out.print("\t");
        for (int i = 0; i < WIDTH; i++) {
            System.out.print(i);
            System.out.print("\t");
        }
        System.out.println("|");
        System.out.println();
    }

    /***
     * Making a move includes placing a new checker and coloring every existing, if it is now in between
     * The i and j for-loops check every neighboring position, the while loop checks where exactly the re-coloring is needed
     * Changes the turn variable at the end (indicates which player is active right now)
     * @param row Row (from input)
     * @param col Column (from Input)
     */
    private void makeMove(int row, int col) {
        boolean isNeighbor;
        int x;
        int y;
        for (int i = -1; i <= 1; ++i) {
            for (int j = -1; j <= 1; ++j) {
                if (i == 0 && j == 0) {
                    continue;
                }
                x = row;
                y = col;
                isNeighbor = true;
                while (true) {
                    x += i;
                    y += j;

                    if (x < 0 || x >= HEIGHT || y < 0 || y >= WIDTH) {
                        break;
                    }
                    if (board[x][y] == EMPTY || board[x][y] == POSSIBLE) {
                        break;
                    }
                    if (isNeighbor && board[x][y] != turn) {
                        isNeighbor = false;
                    } else if (isNeighbor) {
                        break;
                    }
                    if (board[x][y] == turn) {
                        flip(row, col, x, y, i, j);
                        break;
                    }
                }
            }
        }
        board[row][col] = turn;
        if (turn == BLACK) {
            ++black_score;
        } else {
            ++white_score;
        }
    }

    /***
     * Changes the color of a line after a move has been made
     * @param x New checker's row index
     * @param y New checker's column index
     * @param toX Row index of last existing checker that needs to be recolored
     * @param toY Column index of last existing checker that needs to be recolored
     * @param dX Indicates the direction along rows
     * @param dY Indicates the direction along columns
     */
    private void flip(int x, int y, int toX, int toY, int dX, int dY) {
        while (!(x == toX && y == toY)) {
            x += dX;
            y += dY;
            board[x][y] = turn;
            if (turn == BLACK && !(x == toX && y == toY)) {
                ++black_score;
                --white_score;
            } else if (turn == WHITE && !(x == toX && y == toY)) {
                --black_score;
                ++white_score;
            }
        }
    }

    /***
     * Estimates the 'value' of a move based on the coordinates of the potentially new checker
     * @param row row coordinate
     * @param col column coordinate
     * @return A float as a representation of move's 'usefulness'
     */
    private float rateMove(int row, int col) {
        boolean isNeighbor;
        int x;
        int y;
        float rate = 0;
        for (int i = -1; i <= 1; ++i) {
            for (int j = -1; j <= 1; ++j) {
                if (i == 0 && j == 0) {
                    continue;
                }
                x = row;
                y = col;
                isNeighbor = true;
                while (true) {
                    x += i;
                    y += j;

                    if (x < 0 || x >= HEIGHT || y < 0 || y >= WIDTH) {
                        break;//current cell is off the map
                    }

                    if (board[x][y] == EMPTY || board[x][y] == POSSIBLE) {
                        break;//current neighbor cell is empty or possible
                    }
                    // if this neighbor cell is enemy-owned, continue the while loop to see if the rest of this line matches the rules
                    if (isNeighbor && board[x][y] != turn) {
                        isNeighbor = false;
                    } else if (isNeighbor) {
                        break;
                    }
                    if (board[x][y] == turn) {
                        rate += rateLine(row, col, x, y, i, j);
                        break;
                    }
                }
            }
        }
        return rate;
    }

    /***
     * Implements the estimation formula for the computer side
     * @param x the line's start row coordinate
     * @param y the line's start column coordinate
     * @param toX the line's end row coordinate
     * @param toY the line's end column coordinate
     * @param dX the line's direction row-wise
     * @param dY the line's direction column-wise
     * @return A float, estimating the move
     */
    private float rateLine(int x, int y, int toX, int toY, int dX, int dY) {
        float rate = 0;
        while (!(x == toX && y == toY)) {
            x += dX;
            y += dY;
            if (x == HEIGHT - 1 || y == WIDTH - 1 || x == 0 || y == 0) { //border cell
                rate += 2;
            } else {
                ++rate;
            }
        }
        if ((toX == 0 && toY == 0) || (toX == HEIGHT - 1 && toY == WIDTH - 1) || (toX == 0 && toY == WIDTH - 1) || (toX == HEIGHT - 1 && toY == 0)) { //corner cell
            rate += 0.8;
        } else if (toX == HEIGHT - 1 || toY == WIDTH - 1 || toX == 0 || toY == 0) { //border cell
            rate += 0.4;
        }
        return rate;
    }
}

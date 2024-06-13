/*
@Author hakim a.k.a. Hakim Amarullah
Java Developer
Created on 6/13/2024 3:47 PM
@Last Modified 6/13/2024 3:47 PM
Version 1.0
*/

public class SudokuSolver {

    private static final int SIZE = 9;

    public static void main(String[] args) {
        int[][] board = {
                {1, 0, 5, 8, 0, 2, 0, 0, 0},
                {0, 9, 0, 0, 7, 6, 4, 0, 5},
                {2, 0, 0, 4, 0, 0, 8, 1, 9},
                {0, 1, 9, 0, 0, 7, 3, 0, 6},
                {7, 6, 2, 0, 8, 3, 0, 9, 0},
                {0, 0, 0, 0, 6, 1, 0, 5, 0},
                {0, 0, 7, 6, 0, 0, 0, 3, 0},
                {4, 3, 0, 0, 2, 0, 5, 0, 1},
                {6, 0, 0, 3, 0, 8, 9, 0, 0}
        };

        if (solveSudoku(board)) {
            System.out.println("Sudoku solved successfully!");
            printBoard(board);
        } else {
            System.out.println("No solution exists.");
        }

    }


    private static boolean solveSudoku(int[][] board) {
        int[] empty = findEmptyLocation(board);
        if (empty == null) {
            return true; // No empty locations, puzzle solved
        }
        int row = empty[0];
        int col = empty[1];

        for (int num = 1; num <= 9; num++) {
            if (isValid(board, row, col, num)) {
                board[row][col] = num;

                if (solveSudoku(board)) {
                    return true;
                }

                // Undo the move (backtrack)
                board[row][col] = 0;
            }
        }
        return false;
    }

    private static boolean isValid(int[][] board, int row, int col, int num) {
        // Check if num is in the given row
        for (int x = 0; x < SIZE; x++) {
            if (board[row][x] == num) {
                return false;
            }
        }

        // Check if num is in the given column
        for (int x = 0; x < SIZE; x++) {
            if (board[x][col] == num) {
                return false;
            }
        }

        // Check if num is in the 3x3 subgrid
        int startRow = row - row % 3;
        int startCol = col - col % 3;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (board[i + startRow][j + startCol] == num) {
                    return false;
                }
            }
        }
        return true;
    }

    private static int[] findEmptyLocation(int[][] board) {
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (board[i][j] == 0) {
                    return new int[]{i, j};
                }
            }
        }
        return null;
    }

    private static void printBoard(int[][] board) {
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                System.out.print(board[i][j] + " ");
            }
            System.out.println();
        }
    }
}

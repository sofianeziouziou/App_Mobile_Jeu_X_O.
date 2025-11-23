package com.example.jeu_x_o.game;

public class GameEngine {
    private char[][] board;
    private char currentPlayer;
    private boolean gameOver;
    private String winner;

    public GameEngine() {
        board = new char[3][3];
        resetGame();
    }

    public void resetGame() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                board[i][j] = ' ';
            }
        }
        currentPlayer = 'X';
        gameOver = false;
        winner = null;
    }

    public boolean makeMove(int row, int col) {
        if (gameOver || board[row][col] != ' ') {
            return false;
        }

        board[row][col] = currentPlayer;

        if (checkWin(row, col)) {
            gameOver = true;
            winner = String.valueOf(currentPlayer);
        } else if (isBoardFull()) {
            gameOver = true;
            winner = "Draw";
        } else {
            currentPlayer = (currentPlayer == 'X') ? 'O' : 'X';
        }
        return true;
    }

    private boolean checkWin(int row, int col) {
        // Check row
        if (board[row][0] == currentPlayer && board[row][1] == currentPlayer && board[row][2] == currentPlayer) {
            return true;
        }
        // Check column
        if (board[0][col] == currentPlayer && board[1][col] == currentPlayer && board[2][col] == currentPlayer) {
            return true;
        }
        // Check diagonals
        if (row == col && board[0][0] == currentPlayer && board[1][1] == currentPlayer && board[2][2] == currentPlayer) {
            return true;
        }
        if (row + col == 2 && board[0][2] == currentPlayer && board[1][1] == currentPlayer && board[2][0] == currentPlayer) {
            return true;
        }
        return false;
    }

    private boolean isBoardFull() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (board[i][j] == ' ') {
                    return false;
                }
            }
        }
        return true;
    }

    public char getCurrentPlayer() { return currentPlayer; }
    public boolean isGameOver() { return gameOver; }
    public String getWinner() { return winner; }
    public char[][] getBoard() { return board; }
}
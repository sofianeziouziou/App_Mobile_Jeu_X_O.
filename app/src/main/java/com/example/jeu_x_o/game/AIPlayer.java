package com.example.jeu_x_o.game;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class AIPlayer {
    private String difficulty;
    private Random random;

    public AIPlayer(String difficulty) {
        this.difficulty = difficulty;
        this.random = new Random();
    }

    public int[] makeMove(char[][] board) {
        switch (difficulty) {
            case "Easy":
                return makeEasyMove(board);
            case "Medium":
                return makeMediumMove(board);
            case "Hard":
                return makeHardMove(board);
            default:
                return makeEasyMove(board);
        }
    }

    private int[] makeEasyMove(char[][] board) {
        List<int[]> emptyCells = getEmptyCells(board);
        return emptyCells.get(random.nextInt(emptyCells.size()));
    }

    private int[] makeMediumMove(char[][] board) {
        // 50% chance de faire un bon coup, 50% chance aléatoire
        if (random.nextBoolean()) {
            int[] smartMove = findWinningOrBlockingMove(board, 'O');
            if (smartMove != null) return smartMove;

            smartMove = findWinningOrBlockingMove(board, 'X');
            if (smartMove != null) return smartMove;
        }
        return makeEasyMove(board);
    }

    private int[] makeHardMove(char[][] board) {
        // Toujours chercher le meilleur coup
        int[] winningMove = findWinningOrBlockingMove(board, 'O');
        if (winningMove != null) return winningMove;

        int[] blockingMove = findWinningOrBlockingMove(board, 'X');
        if (blockingMove != null) return blockingMove;

        // Prendre le centre si disponible
        if (board[1][1] == ' ') return new int[]{1, 1};

        return makeEasyMove(board);
    }

    private int[] findWinningOrBlockingMove(char[][] board, char player) {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (board[i][j] == ' ') {
                    board[i][j] = player;
                    if (checkWin(board, player)) {
                        board[i][j] = ' ';
                        return new int[]{i, j};
                    }
                    board[i][j] = ' ';
                }
            }
        }
        return null;
    }

    private boolean checkWin(char[][] board, char player) {
        // Check rows and columns
        for (int i = 0; i < 3; i++) {
            if ((board[i][0] == player && board[i][1] == player && board[i][2] == player) ||
                    (board[0][i] == player && board[1][i] == player && board[2][i] == player)) {
                return true;
            }
        }
        // Check diagonals
        if ((board[0][0] == player && board[1][1] == player && board[2][2] == player) ||
                (board[0][2] == player && board[1][1] == player && board[2][0] == player)) {
            return true;
        }
        return false;
    }

    private List<int[]> getEmptyCells(char[][] board) {
        List<int[]> emptyCells = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (board[i][j] == ' ') {
                    emptyCells.add(new int[]{i, j});
                }
            }
        }
        return emptyCells;
    }
}
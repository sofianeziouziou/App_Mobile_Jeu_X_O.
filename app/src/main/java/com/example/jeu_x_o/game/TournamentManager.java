package com.example.jeu_x_o.game;

import com.example.jeu_x_o.model.TournamentResult;

public class TournamentManager {
    private int totalGames;
    private int currentGame;
    private int scoreX;
    private int scoreO;
    private int drawCount;
    private String playerSymbol;
    private boolean vsAI;

    public TournamentManager(int totalGames, String playerSymbol, boolean vsAI) {
        this.totalGames = totalGames;
        this.currentGame = 1;
        this.scoreX = 0;
        this.scoreO = 0;
        this.drawCount = 0;
        this.playerSymbol = playerSymbol;
        this.vsAI = vsAI;
    }

    public void recordGameResult(String winner) {
        if ("X".equals(winner)) {
            scoreX++;
        } else if ("O".equals(winner)) {
            scoreO++;
        } else {
            drawCount++;
        }
        currentGame++;
    }

    public boolean isTournamentOver() {
        return currentGame > totalGames;
    }

    public TournamentResult getTournamentResult() {
        String tournamentWinner;
        if (scoreX > scoreO) {
            tournamentWinner = "X";
        } else if (scoreO > scoreX) {
            tournamentWinner = "O";
        } else {
            tournamentWinner = "Draw";
        }

        return new TournamentResult(scoreX, scoreO, drawCount, totalGames, tournamentWinner);
    }

    public int getCurrentGame() { return currentGame; }
    public int getTotalGames() { return totalGames; }
    public int getScoreX() { return scoreX; }
    public int getScoreO() { return scoreO; }
    public int getDrawCount() { return drawCount; }
    public String getPlayerSymbol() { return playerSymbol; }
    public boolean isVsAI() { return vsAI; }
}
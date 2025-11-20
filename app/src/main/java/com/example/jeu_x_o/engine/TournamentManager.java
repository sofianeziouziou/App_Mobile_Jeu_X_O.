package com.example.jeu_x_o.engine;
public class TournamentManager {
    private int scoreX,scoreO,nullCount; private int currentGame,totalGames;
    public TournamentManager(int totalGames){ this.totalGames=totalGames; this.currentGame=1; scoreX=0;scoreO=0;nullCount=0;}
    public void addWinX(){ scoreX++; } public void addWinO(){ scoreO++; } public void addNull(){ nullCount++; }
    public void nextGame(){ currentGame++; } public boolean isFinished(){ return currentGame>totalGames; }
    public int getScoreX(){ return scoreX; } public int getScoreO(){ return scoreO; } public int getNullCount(){ return nullCount; }
    public int getCurrentGame(){ return currentGame; } public int getTotalGames(){ return totalGames; }
}

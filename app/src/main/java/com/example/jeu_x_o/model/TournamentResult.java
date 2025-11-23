package com.example.jeu_x_o.model;

import java.io.Serializable;

public class TournamentResult implements Serializable {
    private static final long serialVersionUID = 1L;

    public int scoreX;
    public int scoreO;
    public int nulCount;
    public int totalGames;
    public String winner;

    public TournamentResult(int scoreX, int scoreO, int nulCount, int totalGames, String winner) {
        this.scoreX = scoreX;
        this.scoreO = scoreO;
        this.nulCount = nulCount;
        this.totalGames = totalGames;
        this.winner = winner;
    }

    @Override
    public String toString() {
        return String.format(
                "Score X: %d\nScore O: %d\nNuls: %d\nTotal: %d\nVainqueur: %s",
                scoreX, scoreO, nulCount, totalGames, winner
        );
    }
}
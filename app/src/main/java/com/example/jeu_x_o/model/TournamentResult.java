package com.example.jeu_x_o.model;
import java.io.Serializable;
public class TournamentResult implements Serializable {
    private static final long serialVersionUID = 1L;
    public int scoreX; public int scoreO; public int nulCount; public int totalGames; public String winner;
    public TournamentResult(int sX,int sO,int n,int total,String w){ scoreX=sX; scoreO=sO; nulCount=n; totalGames=total; winner=w;}
}

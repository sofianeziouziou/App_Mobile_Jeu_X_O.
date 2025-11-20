package com.example.jeu_x_o.engine;
public class GameEngine {
    private char[][] board;
    private char currentPlayer;
    public GameEngine(){ board=new char[3][3]; resetBoard(); }
    public void resetBoard(){ for(int i=0;i<3;i++) for(int j=0;j<3;j++) board[i][j]='\0'; currentPlayer='X'; }
    public boolean isCellEmpty(int r,int c){ return board[r][c]=='\0'; }
    public void playMove(int r,int c){ if(board[r][c]=='\0') board[r][c]=currentPlayer; }
    public Character checkWinner(){
        for(int i=0;i<3;i++){ if(board[i][0]!='\0' && board[i][0]==board[i][1] && board[i][1]==board[i][2]) return board[i][0];
            if(board[0][i]!='\0' && board[0][i]==board[1][i] && board[1][i]==board[2][i]) return board[0][i]; }
        if(board[0][0]!='\0' && board[0][0]==board[1][1] && board[1][1]==board[2][2]) return board[0][0];
        if(board[0][2]!='\0' && board[0][2]==board[1][1] && board[1][1]==board[2][0]) return board[0][2];
        return null;
    }
    public boolean isBoardFull(){ for(int i=0;i<3;i++) for(int j=0;j<3;j++) if(board[i][j]=='\0') return false; return true; }
    public void togglePlayer(){ currentPlayer=(currentPlayer=='X')?'O':'X'; }
    public char getCurrentPlayer(){ return currentPlayer; }
    public void setCurrentPlayer(char p){ if(p=='X'||p=='O') currentPlayer=p; }
    public char[][] getBoardCopy(){ char[][] c=new char[3][3]; for(int i=0;i<3;i++) for(int j=0;j<3;j++) c[i][j]=board[i][j]; return c; }
}

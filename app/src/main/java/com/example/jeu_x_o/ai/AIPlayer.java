package com.example.jeu_x_o.ai;
import com.example.jeu_x_o.engine.GameEngine;
import java.util.*;
public class AIPlayer {
    private String level; private Random rand=new Random();
    public AIPlayer(String level){ this.level=level; }
    public int[] nextMove(GameEngine engine){
        char[][] b = engine.getBoardCopy();
        if(level.equalsIgnoreCase("Easy")) return randomMove(b);
        if(level.equalsIgnoreCase("Medium")){
            int[] win = findWinning(b,getAiChar(engine));
            if(win!=null) return win;
            int[] block = findWinning(b,getPlayerChar(engine));
            if(block!=null) return block;
            return randomMove(b);
        }
        return minimaxMove(b,getAiChar(engine));
    }
    private char getAiChar(GameEngine engine){ return engine.getCurrentPlayer(); }
    private char getPlayerChar(GameEngine engine){ return (getAiChar(engine)=='X')?'O':'X'; }
    private int[] randomMove(char[][] b){ List<int[]> m=new ArrayList<>(); for(int i=0;i<3;i++) for(int j=0;j<3;j++) if(b[i][j]=='\0') m.add(new int[]{i,j}); if(m.isEmpty()) return null; return m.get(rand.nextInt(m.size())); }
    private int[] findWinning(char[][] b,char ch){ for(int i=0;i<3;i++) for(int j=0;j<3;j++) if(b[i][j]=='\0'){ b[i][j]=ch; if(isWinner(b,ch)){ b[i][j]='\0'; return new int[]{i,j}; } b[i][j]='\0'; } return null; }
    private boolean isWinner(char[][] b,char ch){ for(int i=0;i<3;i++) if(b[i][0]==ch && b[i][1]==ch && b[i][2]==ch) return true; for(int i=0;i<3;i++) if(b[0][i]==ch && b[1][i]==ch && b[2][i]==ch) return true; if(b[0][0]==ch && b[1][1]==ch && b[2][2]==ch) return true; if(b[0][2]==ch && b[1][1]==ch && b[2][0]==ch) return true; return false; }
    private int[] minimaxMove(char[][] board,char aiChar){ int bestScore=Integer.MIN_VALUE; int[] best=null; for(int i=0;i<3;i++) for(int j=0;j<3;j++) if(board[i][j]=='\0'){ board[i][j]=aiChar; int sc=minimax(board,false,aiChar,aiChar=='X'?'O':'X'); board[i][j]='\0'; if(sc>bestScore){ bestScore=sc; best=new int[]{i,j}; } } return best; }
    private int minimax(char[][] board, boolean isMax, char aiChar, char playerChar){
        if(isWinner(board,aiChar)) return 10;
        if(isWinner(board,playerChar)) return -10;
        boolean empty=false; for(int i=0;i<3;i++) for(int j=0;j<3;j++) if(board[i][j]=='\0') empty=true;
        if(!empty) return 0;
        if(isMax){ int best=Integer.MIN_VALUE; for(int i=0;i<3;i++) for(int j=0;j<3;j++) if(board[i][j]=='\0'){ board[i][j]=aiChar; int val=minimax(board,false,aiChar,playerChar); board[i][j]='\0'; best=Math.max(best,val);} return best;}
        else{ int best=Integer.MAX_VALUE; for(int i=0;i<3;i++) for(int j=0;j<3;j++) if(board[i][j]=='\0'){ board[i][j]=playerChar; int val=minimax(board,true,aiChar,playerChar); board[i][j]='\0'; best=Math.min(best,val);} return best;}
    }
}

package com.example.jeu_x_o;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.jeu_x_o.engine.GameEngine;
import com.example.jeu_x_o.engine.TournamentManager;
import com.example.jeu_x_o.model.TournamentResult;
import com.example.jeu_x_o.ai.AIPlayer;
public class GameActivity extends AppCompatActivity {
    private TextView tvPartie, tvScoreX, tvScoreO, tvNuls;
    private Button[][] btns = new Button[3][3];
    private GameEngine engine;
    private TournamentManager manager;
    private String playerSymbol;
    private boolean vsAi;
    private String aiLevel;
    private AIPlayer aiPlayer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        tvPartie = findViewById(R.id.tvPartie);
        tvScoreX = findViewById(R.id.tvScoreX);
        tvScoreO = findViewById(R.id.tvScoreO);
        tvNuls = findViewById(R.id.tvNuls);
        playerSymbol = getIntent().getStringExtra("playerSymbol");
        int totalGames = getIntent().getIntExtra("totalGames", 5);
        vsAi = getIntent().getBooleanExtra("vsAi", false);
        aiLevel = getIntent().getStringExtra("aiLevel");
        manager = new TournamentManager(totalGames);
        engine = new GameEngine();
        if (playerSymbol != null && playerSymbol.equals("O")) engine.setCurrentPlayer('O');
        if (vsAi) aiPlayer = new AIPlayer(aiLevel);
        setupBoard();
        updateUI();
    }
    private void setupBoard() {
        GridLayout grid = findViewById(R.id.gridBoard);
        int childCount = grid.getChildCount();
        int index = 0;
        for (int r = 0; r < 3; r++) {
            for (int c = 0; c < 3; c++) {
                if (index >= childCount) break;
                View child = grid.getChildAt(index);
                if (child instanceof Button) {
                    Button b = (Button) child;
                    final int fr = r, fc = c;
                    btns[r][c] = b;
                    b.setText("");
                    b.setEnabled(true);
                    b.setOnClickListener(v -> onCellClicked(fr, fc));
                }
                index++;
            }
        }
    }
    private void onCellClicked(int r, int c) {
        if (!engine.isCellEmpty(r, c)) return;
        char cur = engine.getCurrentPlayer();
        engine.playMove(r, c);
        btns[r][c].setText(String.valueOf(cur));
        btns[r][c].setEnabled(false);
        Character winner = engine.checkWinner();
        if (winner != null) {
            char w = winner.charValue();
            if (w == 'X') manager.addWinX(); else manager.addWinO();
            Toast.makeText(this, "Gagnant de la partie: " + w, Toast.LENGTH_SHORT).show();
            proceedAfterRound();
            return;
        }
        if (engine.isBoardFull()) {
            manager.addNull();
            Toast.makeText(this, "Partie nulle", Toast.LENGTH_SHORT).show();
            proceedAfterRound();
            return;
        }
        engine.togglePlayer();
        if (vsAi && engine.getCurrentPlayer() == getOpponentChar(playerSymbol)) {
            aiPlay();
        }
    }
    private char getOpponentChar(String player) { return (player.equals("X")) ? 'O' : 'X'; }
    private void aiPlay() {
        int[] mv = aiPlayer.nextMove(engine);
        if (mv != null) {
            engine.playMove(mv[0], mv[1]);
            btns[mv[0]][mv[1]].setText(String.valueOf(engine.getCurrentPlayer()));
            btns[mv[0]][mv[1]].setEnabled(false);
        }
        Character winner = engine.checkWinner();
        if (winner != null) {
            char w = winner.charValue();
            if (w == 'X') manager.addWinX(); else manager.addWinO();
            Toast.makeText(this, "Gagnant de la partie: " + w, Toast.LENGTH_SHORT).show();
            proceedAfterRound();
            return;
        }
        if (engine.isBoardFull()) {
            manager.addNull();
            Toast.makeText(this, "Partie nulle", Toast.LENGTH_SHORT).show();
            proceedAfterRound();
            return;
        }
        engine.togglePlayer();
    }
    private void proceedAfterRound() {
        updateUI();
        manager.nextGame();
        if (manager.isFinished()) {
            showResult();
        } else {
            engine.resetBoard();
            clearBoardButtons();
            updateUI();
        }
    }
    private void clearBoardButtons() { for (int r=0;r<3;r++) for (int c=0;c<3;c++){ btns[r][c].setText(""); btns[r][c].setEnabled(true);} }
    private void updateUI() {
        tvPartie.setText("Partie " + manager.getCurrentGame() + " / " + manager.getTotalGames());
        tvScoreX.setText("X: " + manager.getScoreX());
        tvScoreO.setText("O: " + manager.getScoreO());
        tvNuls.setText("Nuls: " + manager.getNullCount());
    }
    private void showResult() {
        String winner;
        if (manager.getScoreX() > manager.getScoreO()) winner = "X";
        else if (manager.getScoreO() > manager.getScoreX()) winner = "O";
        else winner = "Egalite";
        TournamentResult tr = new TournamentResult(manager.getScoreX(), manager.getScoreO(), manager.getNullCount(), manager.getTotalGames(), winner);
        Intent intent = new Intent(this, ResultActivity.class);
        intent.putExtra("result", tr);
        startActivity(intent);
        finish();
    }
}

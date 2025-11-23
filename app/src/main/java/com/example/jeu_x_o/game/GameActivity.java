package com.example.jeu_x_o.game;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.jeu_x_o.utils.DatabaseHelper;
import com.example.jeu_x_o.R;
import com.example.jeu_x_o.model.ResultActivity;

public class GameActivity extends AppCompatActivity {
    private GameEngine gameEngine;
    private AIPlayer aiPlayer;
    private TournamentManager tournamentManager;

    private GridLayout gridBoard;
    private TextView tvPartie, tvScoreX, tvScoreO, tvNuls;
    private Button[][] buttons = new Button[3][3];
    private boolean isAITurn = false;
    private Handler aiHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        // Récupérer les paramètres
        String playerSymbol = getIntent().getStringExtra("playerSymbol");
        int totalGames = getIntent().getIntExtra("totalGames", 3);
        boolean vsAi = getIntent().getBooleanExtra("vsAi", true);
        String aiLevel = getIntent().getStringExtra("aiLevel");

        // Initialiser les composants
        gameEngine = new GameEngine();
        aiPlayer = new AIPlayer(aiLevel);
        tournamentManager = new TournamentManager(totalGames, playerSymbol, vsAi);

        initViews();
        updateUI();
    }

    private void initViews() {
        gridBoard = findViewById(R.id.gridBoard);
        tvPartie = findViewById(R.id.tvPartie);
        tvScoreX = findViewById(R.id.tvScoreX);
        tvScoreO = findViewById(R.id.tvScoreO);
        tvNuls = findViewById(R.id.tvNuls);

        // Initialiser les boutons
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                String buttonID = "btn_" + i + j;
                int resID = getResources().getIdentifier(buttonID, "id", getPackageName());
                buttons[i][j] = findViewById(resID);

                final int finalI = i;
                final int finalJ = j;
                buttons[i][j].setOnClickListener(v -> onCellClick(finalI, finalJ));
            }
        }
    }

    private void onCellClick(int row, int col) {
        if (isAITurn || gameEngine.isGameOver()) {
            return;
        }

        if (gameEngine.makeMove(row, col)) {
            updateBoard();

            if (gameEngine.isGameOver()) {
                handleGameEnd();
            } else if (tournamentManager.isVsAI() && gameEngine.getCurrentPlayer() == 'O') {
                isAITurn = true;
                aiHandler.postDelayed(this::makeAIMove, 500);
            }
        }
    }

    private void makeAIMove() {
        if (!gameEngine.isGameOver()) {
            int[] move = aiPlayer.makeMove(gameEngine.getBoard());
            if (move != null) {
                gameEngine.makeMove(move[0], move[1]);
                updateBoard();

                if (gameEngine.isGameOver()) {
                    handleGameEnd();
                }
            }
            isAITurn = false;
        }
    }

    private void handleGameEnd() {
        String winner = gameEngine.getWinner();
        tournamentManager.recordGameResult(winner);

        // RÉCUPÉRER LE NOM D'UTILISATEUR CONNECTÉ
        SharedPreferences sharedPreferences = getSharedPreferences("user_prefs", MODE_PRIVATE);
        String username = sharedPreferences.getString("username", "Anonyme");

        // SAUVEGARDER DANS L'HISTORIQUE AVEC USERNAME
        DatabaseHelper dbHelper = new DatabaseHelper(this);
        dbHelper.saveGame(username, winner); // UTILISER LA NOUVELLE MÉTHOD

        // Afficher le résultat de la partie
        String message;
        if ("Draw".equals(winner)) {
            message = "Match nul !";
        } else {
            message = "Joueur " + winner + " gagne !";
        }
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();

        if (tournamentManager.isTournamentOver()) {
            // Tournoi terminé
            Intent intent = new Intent(this, ResultActivity.class);
            intent.putExtra("result", tournamentManager.getTournamentResult());
            startActivity(intent);
            finish();
        } else {
            // Nouvelle partie
            new Handler().postDelayed(() -> {
                gameEngine.resetGame();
                updateBoard();
                updateUI();
            }, 1500);
        }
    }

    private void updateBoard() {
        char[][] board = gameEngine.getBoard();
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                buttons[i][j].setText(String.valueOf(board[i][j]));
                if (board[i][j] != ' ') {
                    buttons[i][j].setTextColor(getColor(board[i][j] == 'X' ?
                            R.color.player_x : R.color.player_o));
                }
            }
        }
    }

    private void updateUI() {
        tvPartie.setText(String.format("Partie %d / %d",
                tournamentManager.getCurrentGame(), tournamentManager.getTotalGames()));
        tvScoreX.setText(String.format("X: %d", tournamentManager.getScoreX()));
        tvScoreO.setText(String.format("O: %d", tournamentManager.getScoreO()));
        tvNuls.setText(String.format("Nuls: %d", tournamentManager.getDrawCount()));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        aiHandler.removeCallbacksAndMessages(null);
    }
}
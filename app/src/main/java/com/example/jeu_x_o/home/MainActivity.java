package com.example.jeu_x_o.home;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.jeu_x_o.R;
import com.example.jeu_x_o.game.GameActivity;
import com.example.jeu_x_o.historique.HistoryActivity;
import com.example.jeu_x_o.login.LoginActivity;
import com.example.jeu_x_o.model.TournamentResult;
import com.example.jeu_x_o.utils.FileHelper;

public class MainActivity extends AppCompatActivity {

    private RadioGroup rgSymbol, rgNbGames, rgMode, rgAiLevel;
    private Button btnPlay, btnRules, btnLoad, btnHistory, btnLogout;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sharedPreferences = getSharedPreferences("user_prefs", MODE_PRIVATE);

        initViews();
        setupListeners();
        displayWelcomeMessage();
    }

    private void initViews() {
        rgSymbol = findViewById(R.id.rgSymbol);
        rgNbGames = findViewById(R.id.rgNbGames);
        rgMode = findViewById(R.id.rgMode);
        rgAiLevel = findViewById(R.id.rgAiLevel);
        btnPlay = findViewById(R.id.btnPlay);
        btnRules = findViewById(R.id.btnRules);
        btnLoad = findViewById(R.id.btnLoad);
        btnHistory = findViewById(R.id.btnHistory);
        btnLogout = findViewById(R.id.btnLogout);

        // Vérifier si les RadioButtons existent avant de les cocher
        RadioButton rbX = findViewById(R.id.rbX);
        RadioButton rb5 = findViewById(R.id.rb5);
        RadioButton rbLocal = findViewById(R.id.rbLocal);
        RadioButton rbEasy = findViewById(R.id.rbEasy);

        if (rbX != null) rbX.setChecked(true);
        if (rb5 != null) rb5.setChecked(true);
        if (rbLocal != null) rbLocal.setChecked(true);
        if (rbEasy != null) rbEasy.setChecked(true);
    }

    private void setupListeners() {
        btnPlay.setOnClickListener(v -> startGame());

        btnRules.setOnClickListener(v ->
                Toast.makeText(this, getString(R.string.rules_text), Toast.LENGTH_LONG).show()
        );

        btnLoad.setOnClickListener(v -> loadLastTournament());

        btnHistory.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, HistoryActivity.class);
            startActivity(intent);
        });

        // Listener pour déconnexion
        btnLogout.setOnClickListener(v -> logoutUser());

        // Masquer/Afficher les niveaux IA selon le mode
        rgMode.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.rbAI) {
                // Trouver la carte du niveau IA
                android.view.View cardAiLevel = findViewById(R.id.cardAiLevel);
                if (cardAiLevel != null) {
                    cardAiLevel.setVisibility(android.view.View.VISIBLE);
                }
            } else {
                android.view.View cardAiLevel = findViewById(R.id.cardAiLevel);
                if (cardAiLevel != null) {
                    cardAiLevel.setVisibility(android.view.View.GONE);
                }
            }
        });
    }

    private void displayWelcomeMessage() {
        String username = sharedPreferences.getString("username", "");
        if (!username.isEmpty()) {
            Toast.makeText(this, "Bienvenue " + username + " !", Toast.LENGTH_SHORT).show();
        }
    }

    private void logoutUser() {
        new android.app.AlertDialog.Builder(this)
                .setTitle("Déconnexion")
                .setMessage("Êtes-vous sûr de vouloir vous déconnecter ?")
                .setPositiveButton("Oui", (dialog, which) -> {
                    // Effacer les préférences utilisateur
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.clear();
                    editor.apply();

                    // Rediriger vers LoginActivity
                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();

                    Toast.makeText(this, "Déconnexion réussie", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("Non", null)
                .show();
    }

    private void startGame() {
        if (!validateSelections()) {
            return;
        }

        String symbol = getSelectedSymbol();
        int totalGames = getSelectedTotalGames();
        boolean vsAi = isVsAISelected();
        String aiLevel = vsAi ? getSelectedAILevel() : "Easy";

        Intent intent = new Intent(this, GameActivity.class);
        intent.putExtra("playerSymbol", symbol);
        intent.putExtra("totalGames", totalGames);
        intent.putExtra("vsAi", vsAi);
        intent.putExtra("aiLevel", aiLevel);
        startActivity(intent);
    }

    private boolean validateSelections() {
        if (rgSymbol.getCheckedRadioButtonId() == -1) {
            Toast.makeText(this, "Choisir un symbole", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (rgNbGames.getCheckedRadioButtonId() == -1) {
            Toast.makeText(this, "Choisir le nombre de parties", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (rgMode.getCheckedRadioButtonId() == -1) {
            Toast.makeText(this, "Choisir le mode de jeu", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (isVsAISelected() && rgAiLevel.getCheckedRadioButtonId() == -1) {
            Toast.makeText(this, "Choisir le niveau de l'IA", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private String getSelectedSymbol() {
        int id = rgSymbol.getCheckedRadioButtonId();
        RadioButton selected = findViewById(id);
        return selected != null ? selected.getText().toString() : "X";
    }

    private int getSelectedTotalGames() {
        int id = rgNbGames.getCheckedRadioButtonId();
        RadioButton selected = findViewById(id);
        if (selected != null) {
            try {
                return Integer.parseInt(selected.getText().toString());
            } catch (NumberFormatException e) {
                return 5; // Valeur par défaut
            }
        }
        return 5;
    }

    private boolean isVsAISelected() {
        return rgMode.getCheckedRadioButtonId() == R.id.rbAI;
    }

    private String getSelectedAILevel() {
        int id = rgAiLevel.getCheckedRadioButtonId();
        RadioButton selected = findViewById(id);
        return selected != null ? selected.getText().toString() : "Easy";
    }

    private void loadLastTournament() {
        TournamentResult tr = FileHelper.loadTournament(this);
        if (tr == null) {
            Toast.makeText(this, "Aucun tournoi sauvegardé", Toast.LENGTH_SHORT).show();
        } else {
            String msg = String.format(
                    "Dernier tournoi:\nX: %d | O: %d | Nuls: %d\nTotal: %d | Vainqueur: %s",
                    tr.scoreX, tr.scoreO, tr.nulCount, tr.totalGames, tr.winner
            );
            Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
        }
    }
}
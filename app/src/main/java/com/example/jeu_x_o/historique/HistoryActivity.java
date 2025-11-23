package com.example.jeu_x_o.historique;

import android.database.Cursor;
import android.graphics.Typeface;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.example.jeu_x_o.utils.DatabaseHelper;
import com.example.jeu_x_o.R;

public class HistoryActivity extends AppCompatActivity {

    private DatabaseHelper dbHelper;
    private LinearLayout historyLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        dbHelper = new DatabaseHelper(this);
        historyLayout = findViewById(R.id.historyLayout);

        loadHistory();
    }

    private void loadHistory() {
        Cursor cursor = dbHelper.getHistory();

        if (cursor == null || cursor.getCount() == 0) {
            showEmptyMessage();
            return;
        }

        try {
            while (cursor.moveToNext()) {
                // RÉCUPÉRER TOUTES LES COLONNES
                int usernameIndex = cursor.getColumnIndex("username");
                int winnerIndex = cursor.getColumnIndex("winner");
                int dateIndex = cursor.getColumnIndex("date");

                if (usernameIndex == -1 || winnerIndex == -1 || dateIndex == -1) {
                    continue;
                }

                String username = cursor.getString(usernameIndex);
                String winner = cursor.getString(winnerIndex);
                String date = cursor.getString(dateIndex);

                addHistoryItem(username, winner, date);
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    private void showEmptyMessage() {
        TextView emptyText = new TextView(this);
        emptyText.setText("Aucun historique disponible");
        emptyText.setTextColor(getResources().getColor(android.R.color.white));
        emptyText.setTextSize(16f);
        emptyText.setPadding(20, 20, 20, 20);
        emptyText.setGravity(android.view.Gravity.CENTER);
        historyLayout.addView(emptyText);
    }

    private void addHistoryItem(String username, String winner, String date) {
        // Créer le layout principal de l'item
        LinearLayout itemLayout = new LinearLayout(this);
        itemLayout.setOrientation(LinearLayout.VERTICAL);
        itemLayout.setPadding(20, 15, 20, 15);
        itemLayout.setBackgroundResource(R.drawable.card_bg);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        params.setMargins(0, 0, 0, 15);
        itemLayout.setLayoutParams(params);

        // Ligne 1: Utilisateur
        TextView usernameText = new TextView(this);
        usernameText.setText("Joueur: " + username);
        usernameText.setTextColor(getResources().getColor(android.R.color.white));
        usernameText.setTextSize(14f);
        usernameText.setTypeface(null, Typeface.BOLD); // CORRECTION ICI

        // Ligne 2: Résultat et Date
        LinearLayout resultLayout = new LinearLayout(this);
        resultLayout.setOrientation(LinearLayout.HORIZONTAL);
        resultLayout.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        ));

        TextView winnerText = new TextView(this);
        if ("Draw".equals(winner)) {
            winnerText.setText("Match nul");
            winnerText.setTextColor(getResources().getColor(android.R.color.holo_orange_light));
        } else {
            winnerText.setText("Vainqueur: " + winner);
            winnerText.setTextColor(getResources().getColor(android.R.color.holo_green_light));
        }
        winnerText.setTextSize(14f);
        winnerText.setTypeface(null, Typeface.BOLD); // CORRECTION ICI
        winnerText.setLayoutParams(new LinearLayout.LayoutParams(
                0,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                1f
        ));

        TextView dateText = new TextView(this);
        dateText.setText(date);
        dateText.setTextColor(getResources().getColor(android.R.color.white));
        dateText.setTextSize(12f);
        dateText.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        ));

        resultLayout.addView(winnerText);
        resultLayout.addView(dateText);

        // Ajouter les vues au layout principal
        itemLayout.addView(usernameText);
        itemLayout.addView(resultLayout);

        historyLayout.addView(itemLayout);
    }
}
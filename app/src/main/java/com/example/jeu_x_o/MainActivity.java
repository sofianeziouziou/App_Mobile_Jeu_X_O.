package com.example.jeu_x_o;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.jeu_x_o.model.TournamentResult;
import com.example.jeu_x_o.utils.FileHelper;
public class MainActivity extends AppCompatActivity {
    private RadioGroup rgSymbol, rgNbGames, rgMode, rgAiLevel;
    private Button btnPlay, btnRules, btnLoad;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        rgSymbol = findViewById(R.id.rgSymbol);
        rgNbGames = findViewById(R.id.rgNbGames);
        rgMode = findViewById(R.id.rgMode);
        rgAiLevel = findViewById(R.id.rgAiLevel);
        btnPlay = findViewById(R.id.btnPlay);
        btnRules = findViewById(R.id.btnRules);
        btnLoad = findViewById(R.id.btnLoad);
        btnPlay.setOnClickListener(v -> startGame());
        btnRules.setOnClickListener(v -> Toast.makeText(this, getString(R.string.rules_text), Toast.LENGTH_LONG).show());
        btnLoad.setOnClickListener(v -> {
            TournamentResult tr = FileHelper.loadTournament(this);
            if (tr == null) { Toast.makeText(this, "Aucun tournoi sauvegardé", Toast.LENGTH_SHORT).show(); }
            else {
                String msg = String.format("Score X: %d | Score O: %d | Nuls: %d | Total: %d | Vainqueur: %s",
                        tr.scoreX, tr.scoreO, tr.nulCount, tr.totalGames, tr.winner);
                Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
            }
        });
    }
    private void startGame() {
        int checkedSymbolId = rgSymbol.getCheckedRadioButtonId();
        int checkedNbId = rgNbGames.getCheckedRadioButtonId();
        int checkedModeId = rgMode.getCheckedRadioButtonId();
        if (checkedSymbolId == -1 || checkedNbId == -1 || checkedModeId == -1) {
            Toast.makeText(this, "Choisir un symbole, mode et nombre de parties", Toast.LENGTH_SHORT).show();
            return;
        }
        String symbol = ((RadioButton)findViewById(checkedSymbolId)).getText().toString();
        int totalGames = Integer.parseInt(((RadioButton)findViewById(checkedNbId)).getText().toString());
        boolean vsAi = (checkedModeId == R.id.rbAI);
        String aiLevel = "Easy";
        if (vsAi) {
            int checkedAi = rgAiLevel.getCheckedRadioButtonId();
            if (checkedAi == -1) { Toast.makeText(this, "Choisir le niveau IA", Toast.LENGTH_SHORT).show(); return; }
            aiLevel = ((RadioButton)findViewById(checkedAi)).getText().toString();
        }
        Intent intent = new Intent(this, GameActivity.class);
        intent.putExtra("playerSymbol", symbol);
        intent.putExtra("totalGames", totalGames);
        intent.putExtra("vsAi", vsAi);
        intent.putExtra("aiLevel", aiLevel);
        startActivity(intent);
    }
}

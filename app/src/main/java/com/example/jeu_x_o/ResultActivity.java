package com.example.jeu_x_o;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.jeu_x_o.model.TournamentResult;
import com.example.jeu_x_o.utils.FileHelper;
public class ResultActivity extends AppCompatActivity {
    private TextView tvFinal;
    private Button btnSave, btnHome;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        tvFinal = findViewById(R.id.tvFinal);
        btnSave = findViewById(R.id.btnSave);
        btnHome = findViewById(R.id.btnHome);
        TournamentResult tr = (TournamentResult) getIntent().getSerializableExtra("result");
        if (tr == null) { finish(); return; }
        String text = String.format("Score X: %d\nScore O: %d\nNuls: %d\nTotal: %d\nVainqueur: %s",
                tr.scoreX, tr.scoreO, tr.nulCount, tr.totalGames, tr.winner);
        tvFinal.setText(text);
        btnSave.setOnClickListener(v -> {
            boolean ok = FileHelper.saveTournament(this, tr);
            Toast.makeText(this, ok ? "Tournoi sauvegardé" : "Erreur de sauvegarde", Toast.LENGTH_SHORT).show();
        });
        btnHome.setOnClickListener(v -> finish());
    }
}

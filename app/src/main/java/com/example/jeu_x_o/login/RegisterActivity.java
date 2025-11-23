package com.example.jeu_x_o.login;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.jeu_x_o.utils.DatabaseHelper;
import com.example.jeu_x_o.R;

public class RegisterActivity extends AppCompatActivity {

    EditText etUser, etPass;
    Button btnRegister;
    DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        db = new DatabaseHelper(this);

        etUser = findViewById(R.id.etRegUsername);
        etPass = findViewById(R.id.etRegPassword);
        btnRegister = findViewById(R.id.btnRegister);

        btnRegister.setOnClickListener(v -> {
            String username = etUser.getText().toString().trim();
            String password = etPass.getText().toString().trim();

            // Validation des champs
            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show();
                return;
            }

            // Vérifier si le nom d'utilisateur existe déjà
            if (db.isUsernameTaken(username)) {
                Toast.makeText(this, "Ce nom d'utilisateur existe déjà", Toast.LENGTH_SHORT).show();
                return;
            }

            // Inscription de l'utilisateur
            boolean ok = db.registerUser(username, password);

            if (ok) {
                Toast.makeText(this, "Compte créé avec succès !", Toast.LENGTH_SHORT).show();

                // REDIRECTION VERS LOGIN ACTIVITY
                new android.os.Handler().postDelayed(() -> {
                    Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                    // Optionnel : pré-remplir le nom d'utilisateur dans le login
                    intent.putExtra("username", username);
                    startActivity(intent);
                    finish(); // Fermer RegisterActivity pour éviter de revenir en arrière
                }, 1000);

            } else {
                Toast.makeText(this, "Erreur lors de la création du compte", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
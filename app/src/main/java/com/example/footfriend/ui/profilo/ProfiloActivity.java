package com.example.footfriend.ui.profilo;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.example.footfriend.R;
import com.example.footfriend.ui.login.LoginActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ProfiloActivity extends AppCompatActivity {

    private TextView nomeTextView, emailTextView;
    private Button logoutButton;
    private ImageView profiloImageView;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profilo);

        mAuth = FirebaseAuth.getInstance();

        nomeTextView = findViewById(R.id.textNome);
        emailTextView = findViewById(R.id.textEmail);
        logoutButton = findViewById(R.id.buttonLogout);
        profiloImageView = findViewById(R.id.imageProfilo);

        FirebaseUser user = mAuth.getCurrentUser();

        if (user != null) {
            emailTextView.setText(user.getEmail());

            String displayName = user.getDisplayName();
            if (displayName != null && !displayName.isEmpty()) {
                nomeTextView.setText(displayName);
            } else {
                nomeTextView.setText("Nome non disponibile");
            }
        }

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                Intent intent = new Intent(ProfiloActivity.this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
        });
    }
}
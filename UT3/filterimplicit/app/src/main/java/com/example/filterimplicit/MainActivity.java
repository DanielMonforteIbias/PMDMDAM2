package com.example.filterimplicit;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        TextView receivedText = findViewById(R.id.receivedText);
        ImageView imageView=findViewById(R.id.imageView);
        imageView.setImageURI(null);
        receivedText.setText("");
        // Obtenemos el Intent que inició la actividad
        Intent intent = getIntent();
        String action = intent.getAction();
        String type = intent.getType();

        // Verificamos que la acción sea ACTION_SEND y el tipo de dato sea "text/plain"
        if (Intent.ACTION_SEND.equals(action) && "text/plain".equals(type)) {
            handleSendText(intent, receivedText); // Maneja el texto compartido
        }
        if (Intent.ACTION_SEND.equals(action) && "image/jpeg".equals(type)) {
            handleSendImage(intent, imageView); // Maneja la imagen compartida
        }
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void handleSendText(Intent intent, TextView receivedText) {
        String sharedText = intent.getStringExtra(Intent.EXTRA_TEXT);
        if (sharedText != null) {
            receivedText.setText(sharedText); // Muestra el texto en el TextView
        }
    }

    private void handleSendImage(Intent intent, ImageView imageView) {
        Uri sharedImage = intent.getParcelableExtra(Intent.EXTRA_STREAM);
        if (sharedImage != null) {
            imageView.setImageURI(sharedImage); // Muestra la imagen en el ImageView
        }
    }
}
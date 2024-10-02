package edu.pmdm.ciclovida;

import android.os.Bundle;
import android.util.Log;

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
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        Log.i("TAG CICLOVIDA", "CICLO VIDA ONCREATE");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i("TAG CICLOVIDA", "CICLO VIDA ONSTART");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i("TAG CICLOVIDA", "CICLO VIDA ONRESUME");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.wtf("TAG CICLOVIDA", "CICLO VIDA ONPAUSE");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.wtf("TAG CICLOVIDA", "CICLO VIDA ONSTOP");
    }

    @Override
    protected void onDestroy() {
        Log.wtf("TAG CICLOVIDA", "CICLO VIDA ONDESTROY");
        super.onDestroy();
    }
}
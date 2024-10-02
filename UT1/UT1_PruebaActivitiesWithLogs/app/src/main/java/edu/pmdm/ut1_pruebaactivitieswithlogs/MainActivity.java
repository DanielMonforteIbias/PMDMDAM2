package edu.pmdm.ut1_pruebaactivitieswithlogs;

import android.os.Bundle;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    int i=0;
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
        Log.i("TAG ACTIVITY","CICLO DE VIDA: ON CREATE");
        i++;
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i("TAG ACTIVITY","CICLO DE VIDA: ON START");
        i++;
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i("TAG ACTIVITY","CICLO DE VIDA: ON RESUME");
        i++;
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i("TAG ACTIVITY","CICLO DE VIDA: ON PAUSE");
        i--;
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i("TAG ACTIVITY","CICLO DE VIDA: ON STOP");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.i("TAG ACTIVITY","CICLO DE VIDA: ON RESTART");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i("TAG ACTIVITY","CICLO DE VIDA: ON DESTROY");
    }
}
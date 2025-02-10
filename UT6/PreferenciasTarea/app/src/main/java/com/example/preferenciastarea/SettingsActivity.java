package com.example.preferenciastarea;

import android.Manifest;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.app.ActivityCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class SettingsActivity extends AppCompatActivity implements SharedPreferences.OnSharedPreferenceChangeListener {
    SharedPreferences preferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        preferences= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        preferences.registerOnSharedPreferenceChangeListener(this);

    }

    @Override
    protected void onDestroy() {
        if(preferences!=null) preferences.unregisterOnSharedPreferenceChangeListener(SettingsActivity.this);
        super.onDestroy();
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if(key.equals("modoOscuro")){
            boolean modoOscuro=sharedPreferences.getBoolean(key, false);
            if(modoOscuro) AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            else AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            Toast.makeText(this, "Preferencia " + key + " actualizada: " +modoOscuro, Toast.LENGTH_SHORT).show();
        }
        else if(key.equals("formatoTelefono")){
            Toast.makeText(this, "Preferencia " + key + " actualizada: " + sharedPreferences.getString(key, ""), Toast.LENGTH_SHORT).show();
        }
        else if(key.equals("notificaciones")){
            if(PermissionsManager.comprobarPermisosNotificaciones(getApplicationContext())){
                boolean notificaciones=sharedPreferences.getBoolean(key,false);
                if(notificaciones) Toast.makeText(this, "Se han activado las notificaciones al abrir la app", Toast.LENGTH_SHORT).show();
                else Toast.makeText(this, "Se han desactivado las notificaciones al abrir la app", Toast.LENGTH_SHORT).show();
            }
            else{
                PermissionsManager.pedirPermisosNotificaciones(this);
                Toast.makeText(this, "Permisos de notificaciones denegados", Toast.LENGTH_SHORT).show();
                sharedPreferences.edit().putBoolean("notificaciones",false);
            }
        }
    }
}
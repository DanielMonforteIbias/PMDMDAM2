package com.example.preferenciastarea;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.core.app.ActivityCompat;
import androidx.preference.CheckBoxPreference;
import androidx.preference.PreferenceFragmentCompat;

public class PreferenciasNuevas extends PreferenceFragmentCompat {
    private CheckBoxPreference notificacionesPref;
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey);
        notificacionesPref=findPreference("notificaciones");
        configurarPermisosNotificaciones();
    }
    private void configurarPermisosNotificaciones() {
        if (notificacionesPref != null) {
            if (!PermissionsManager.comprobarPermisosNotificaciones(getContext())) {
                notificacionesPref.setChecked(false);
            }
            notificacionesPref.setOnPreferenceClickListener(preference -> {
                if (!PermissionsManager.comprobarPermisosNotificaciones(getContext())) {
                    notificacionesPref.setChecked(false);
                    PermissionsManager.pedirPermisosNotificaciones(getActivity());
                    return true;
                }
                return false;
            });
        }
    }
}
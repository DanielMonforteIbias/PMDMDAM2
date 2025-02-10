package com.example.preferenciastarea;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;

import androidx.core.app.ActivityCompat;

public class PermissionsManager {
    public static boolean comprobarPermisosNotificaciones(Context c) {
        return ActivityCompat.checkSelfPermission(c, android.Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED;
    }


    public static void pedirPermisosNotificaciones(Activity c) {
        ActivityCompat.requestPermissions(c, new String[]{Manifest.permission.POST_NOTIFICATIONS}, 1);
    }
}

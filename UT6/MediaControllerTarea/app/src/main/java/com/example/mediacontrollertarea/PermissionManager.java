package com.example.mediacontrollertarea;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;

import androidx.core.app.ActivityCompat;

public class PermissionManager {
    private static final int PERMISO_ALMACENAMIENTO=1;
    private static final int PERMISO_NOTIFICACIONES=2;

    public static boolean comprobarPermisosAlmacenamiento(Context c) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) return true; //Si estamos en la API 29 o superior no hacen falta permisos al usar ACTION_OPEN_DOCUMENT
        else return ActivityCompat.checkSelfPermission(c, android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
    }

    public static void pedirPermisosAlmacenamiento(Activity a) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            ActivityCompat.requestPermissions(a, new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISO_ALMACENAMIENTO);
        }
    }
    public static boolean comprobarPermisosNotificaciones(Context c) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            return ActivityCompat.checkSelfPermission(c, Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED;
        } else {
            return true;
        }
    }

    public static void pedirPermisosNotificaciones(Activity a) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ActivityCompat.requestPermissions(a, new String[]{Manifest.permission.POST_NOTIFICATIONS}, PERMISO_NOTIFICACIONES);
        }
    }
}

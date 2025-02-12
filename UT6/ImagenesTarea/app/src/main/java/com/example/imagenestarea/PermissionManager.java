package com.example.imagenestarea;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class PermissionManager {
    private static final int PERMISO_ALMACENAMIENTO_CAMARA =1;
    private static final int PERMISO_VIDEO=2;

    public static boolean comprobarPermisosAlmacenamiento(Context c) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            return ContextCompat.checkSelfPermission(c,Manifest.permission.CAMERA) ==PackageManager.PERMISSION_GRANTED; //Si estamos en la API 29 o superior solo hace falta el permiso de camara
        }
        else return (ContextCompat.checkSelfPermission(c,Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED  &&
                ContextCompat.checkSelfPermission(c,Manifest.permission.CAMERA) ==PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(c,Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED);
    }

    public static void pedirPermisosAlmacenamiento(Activity a) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) { //En las APIs por debajo de la 29 pedimos todos los permisos
            ActivityCompat.requestPermissions(a, new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.CAMERA}, PERMISO_ALMACENAMIENTO_CAMARA);
        }
        else{ //En la 29 o superior pedimos solo el de camara
            ActivityCompat.requestPermissions(a, new String[]{Manifest.permission.CAMERA}, PERMISO_ALMACENAMIENTO_CAMARA);
        }
    }

    public static boolean comprobarPermisosVideo(Context c) {
        return ContextCompat.checkSelfPermission(c,Manifest.permission.CAMERA) ==PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(c,Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED;
    }

    public static void pedirPermisosVideo(Activity a) {
        ActivityCompat.requestPermissions(a, new String[]{Manifest.permission.RECORD_AUDIO,Manifest.permission.CAMERA}, PERMISO_VIDEO);
    }
}
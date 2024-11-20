package com.example.parcelablesasignaturas;

import android.content.Context;
import android.util.JsonReader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class DataSource {
    public static ArrayList<Asignatura> ASIGNATURAS = new ArrayList<Asignatura>();

    public static void leerJSONAsignaturas(Context context){
        String json = null;
        try {
            InputStream is = context.getAssets().open("asignaturas.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
            JSONObject jsonObject = new JSONObject(json);
            JSONArray jsonArray = jsonObject.getJSONArray("asignaturas");
            for (int i = 0; i < jsonArray.length(); i++) {
                System.out.println(i);
                JSONObject jsonCouch = jsonArray.getJSONObject(i);
                String asignatura = jsonCouch.getString("asignatura");
                double nota= jsonCouch.getDouble("nota");
                ASIGNATURAS.add(new Asignatura(asignatura,nota));
            }
        } catch (JSONException | IOException e) {
            e.printStackTrace();
        }
    }


    /*public static String convertirAsignaturasAJSON(ArrayList<Asignatura> asignaturas) {
        try {
            JSONArray jsonArray = new JSONArray();
            for (Asignatura asignatura : asignaturas) {
                JSONObject asignaturaJson = new JSONObject();
                asignaturaJson.put("nombre", asignatura.getNombre());
                asignaturaJson.put("nota", asignatura.getNota());
                jsonArray.put(asignaturaJson);
            }
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("asignaturas", jsonArray);
            return jsonObject.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    public static void guardarJSONEnArchivo(Context context) {
        String json = convertirAsignaturasAJSON(ASIGNATURAS);
        try (FileOutputStream fos = context.openFileOutput("asignaturas.json", Context.MODE_PRIVATE)) {
            fos.write(json.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }*/
}

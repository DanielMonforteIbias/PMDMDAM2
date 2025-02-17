package com.example.prc6_danielmonforte;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class JSONExtractor {
    public static ArrayList<Recurso> loadResources(Context c) {
        ArrayList<Recurso> recursos=new ArrayList<>();
        try {
            InputStream is = c.getAssets().open("recursosList.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            String json = new String(buffer, "UTF-8");

            JSONObject jsonObject = new JSONObject(json);
            JSONArray couchList = jsonObject.getJSONArray("recursos_list");
            for (int i = 0; i < couchList.length(); i++) {
                JSONObject jsonCouch = couchList.getJSONObject(i);
                String nombre = jsonCouch.getString("nombre");
                String descripcion = jsonCouch.getString("descripcion");
                int tipo=-1;
                try {
                    tipo=Integer.parseInt(jsonCouch.getString("tipo"));
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
                String uri=jsonCouch.getString("URI");
                Bitmap imagen=null;
                try {
                    imagen= BitmapFactory.decodeStream(c.getAssets().open("images/"+ jsonCouch.getString("imagen")));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                recursos.add(new Recurso(nombre,descripcion,tipo,uri,imagen));
            }
        } catch (JSONException e)  {
            Toast.makeText(c.getApplicationContext(), "El archivo de datos de recursos está vacío",Toast.LENGTH_SHORT).show(); //Mensaje si el JSON existe pero esta vacio
        }catch(IOException e){
            Toast.makeText(c.getApplicationContext(), "No se encontró el archivo de recursos de las bicis",Toast.LENGTH_SHORT).show(); //Mensaje si el JSON no existe
        }
        return recursos;
    }
}

package com.example.prc3_danielmonforte.bikes;

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
import java.util.List;

public class BikesContent {

    //List of all the bikes to be listed in the RecyclerView
    public static List<Bike> ITEMS = new ArrayList<Bike>();
    public static String selectedDate=""; //Inicializamos la variable a cadena vacia, para evitar errores nulos

    public static void loadBikesFromJSON(Context c) {
        String json = null;
        try {
            InputStream is = c.getAssets().open("bikeList.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");

            JSONObject jsonObject = new JSONObject(json);
            JSONArray couchList = jsonObject.getJSONArray("bike_list");
            for (int i = 0; i < couchList.length(); i++) {
                JSONObject jsonCouch = couchList.getJSONObject(i);
                String owner = jsonCouch.getString("owner");
                String description = jsonCouch.getString("description");
                String city=jsonCouch.getString("city");
                String location=jsonCouch.getString("location");
                String email=jsonCouch.getString("email");
                Bitmap photo=null;
                try {
                    photo= BitmapFactory.decodeStream(c.getAssets().open("images/"+ jsonCouch.getString("image")));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                ITEMS.add(new BikesContent.Bike(photo,owner,description,city,location,email));
            }
        } catch (JSONException e)  {
            Toast.makeText(c.getApplicationContext(), "El archivo de datos de bicis está vacío",Toast.LENGTH_SHORT).show(); //Mensaje si el JSON existe pero esta vacio
        }catch(IOException e){
            Toast.makeText(c.getApplicationContext(), "No se encontró el archivo de datos de las bicis",Toast.LENGTH_SHORT).show(); //Mensaje si el JSON no existe
        }
    }

    public static class Bike {
        private Bitmap photo;
        private String owner;
        private String description;
        private String city;
        private String location;
        private String email;

        public String getEmail() {
            return email;
        }

        public Bitmap getPhoto() {
            return photo;
        }

        public String getOwner() {
            return owner;
        }

        public String getDescription() {
            return description;
        }

        public String getCity() {
            return city;
        }

        public String getLocation() {
            return location;
        }

        public Bike(Bitmap photo, String owner, String description, String city, String location, String email) {
            this.photo = photo;
            this.owner = owner;
            this.description = description;
            this.city = city;
            this.location = location;
            this.email= email;
        }

        @Override
        public String toString() {
            return owner+" "+description;
        }
    }
}

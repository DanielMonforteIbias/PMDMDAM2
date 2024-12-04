package edu.pmdm.asynctaskactualizado;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.TransactionTooLargeException;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {

    private TextView txtDescarga;
    private Button btnDescarga;
    private Button btnDescargaImagen;
    private Button btnLimpiarImagenes;
    private EditText edURL;
    private EditText edURLImagen;
    private TextView txtContadorImagenes;
    public static ArrayList<Bitmap> imagenes=new ArrayList<Bitmap>();
    private int limiteTamaño=6;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        txtContadorImagenes=findViewById(R.id.txtContadorImagenes);
        actualizarContadorImagenes();
        edURL=findViewById(R.id.edURL);
        edURLImagen=findViewById(R.id.edURLImagen);
        txtDescarga=findViewById(R.id.txtDescarga);
        txtDescarga.setMovementMethod(new ScrollingMovementMethod());
        btnDescarga=findViewById(R.id.btnDescargar);
        btnDescargaImagen=findViewById(R.id.btnDescargarImagen);
        btnLimpiarImagenes=findViewById(R.id.btnLimpiarImagenes);
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        btnDescarga.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (networkInfo != null && networkInfo.isConnected()) {
                    new DescargaPaginaWeb().execute(edURL.getText().toString());
                } else {
                    txtDescarga.setText("No se ha podido establecer conexión a internet");
                }
            }
        });
        btnDescargaImagen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (networkInfo != null && networkInfo.isConnected()) {
                    new DescargaImagen().execute(edURLImagen.getText().toString());
                } else {
                    Toast.makeText(getApplicationContext(),"No se ha podido establecer conexión a internet",Toast.LENGTH_SHORT).show();
                }
            }
        });
        btnLimpiarImagenes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(imagenes.size()>0){
                    imagenes.clear();
                    actualizarContadorImagenes();
                    Toast.makeText(getApplicationContext(),"La lista de imágenes se ha reseteado",Toast.LENGTH_SHORT).show();
                }
                else Toast.makeText(getApplicationContext(),"No hay imágenes en la lista",Toast.LENGTH_SHORT).show();
            }
        });
    }

    private class DescargaPaginaWeb extends AsyncTaskExecutorService<String, Void, String> {
        @Override
        protected String doInBackground(String s) {
            try {
                return descargaUrl(s);
            } catch (IOException e) {
                return "Imposible cargar la web! URL mal formada";
            }
        }

        @Override
        protected void onPostExecute(String s) {
            txtDescarga.setText(s);
        }


        /**
         * Este método lee el inputstream convirtiéndolo en una cadena
         * ayudándonos con un ByteArrayOutputStream()
         */
        private String Leer(InputStream is) {
            try {
                ByteArrayOutputStream bo = new ByteArrayOutputStream();
                int i = is.read();
                while (i != -1) {
                    bo.write(i);
                    i = is.read();
                }
                return bo.toString();
            } catch (IOException e) {
                return "";
            }
        }

        // Dada una URL, establece una conexión HttpUrlConnection y devuelve
        // el contenido de la página web con un InputStream, y que se transforma a un String.
        private String descargaUrl(String myurl) throws IOException {
            InputStream is = null;
            try {
                URL url = new URL(myurl);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(10000 /* milisegundos */);
                conn.setConnectTimeout(15000 /* milisegundos */);
                conn.setRequestMethod("GET");
                conn.setDoInput(true);
                // comienza la consulta
                conn.connect();
                int response = conn.getResponseCode();
                is = conn.getInputStream();

                // convertir el InputStream a string
                return Leer(is);


            } finally {
                if (is != null) {
                    //Nos aseguramos de cerrar el inputStream.
                    is.close();
                }
            }
        }
    }

    private class DescargaImagen extends AsyncTaskExecutorService<String, Void, Bitmap> {
        @Override
        protected Bitmap doInBackground(String s) {
            try {
                return descargaUrlImagen(s);
            } catch (IOException e) {
                return null;
            }
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            if(bitmap!=null){
                if(bitmap.getWidth()>=200 && bitmap.getHeight()>=200){ //Si la imagen mide 200x200 o mas, sera valida. Si no, no, pues al ser mu pequeña se vería con muy mala calidad
                    if(imagenes.size()<limiteTamaño){
                        //En un primer momento se intentaba pasar la imagen a la otra actividad poniendolo como extra en el Intent y pasandolo como ByteArray
                        //Esto daba Transaction Too Large Exception con imagenes muy grandes (el limite es 1MB), asi que se añaden al ArrayList desde aqui
                        imagenes.add(bitmap); //Añadimos la imagen a la lista de imagenes
                    }
                    else Toast.makeText(getApplicationContext(),"Límite de imágenes alcanzado. No se añade",Toast.LENGTH_SHORT).show();
                    //Se añada o no la imagen, iniciamos la otra actividad. Esto con el fin de que el usuario no tenga que borrar las fotos para poder acceder a esta pantala
                    Intent intent = new Intent(getApplicationContext(), ListaImagenes.class);
                    startActivity(intent); //Creamos un intent para pasar a la otra actividad
                    actualizarContadorImagenes();
                }
                else Toast.makeText(getApplicationContext(),"La imagen debe ser minimo 200x200",Toast.LENGTH_SHORT).show();
                edURLImagen.setText(""); //Vaciamos el campo de URL de imagen
            }
            else Toast.makeText(getApplicationContext(),"No se pudo obtener imagen",Toast.LENGTH_SHORT).show();
        }

        // Dada una URL, establece una conexión HttpUrlConnection y devuelve
        // el contenido de la página web con un InputStream, y que se transforma a un String.
        private Bitmap descargaUrlImagen(String myurl) throws IOException {
            InputStream is = null;
            try {
                URL url = new URL(myurl);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(10000 /* milisegundos */);
                conn.setConnectTimeout(15000 /* milisegundos */);
                conn.setRequestMethod("GET");
                conn.setDoInput(true);
                // comienza la consulta
                conn.connect();
                int response = conn.getResponseCode();
                is = conn.getInputStream();
                // convertir el InputStream a Bitmap
                return BitmapFactory.decodeStream(is);
            } finally {
                if (is != null) {
                    //Nos aseguramos de cerrar el inputStream.
                    is.close();
                }
            }
        }
    }
    private void actualizarContadorImagenes(){
        txtContadorImagenes.setText("Contador imágenes: "+imagenes.size()+" de "+limiteTamaño);
    }

}
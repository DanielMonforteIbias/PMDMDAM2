package edu.pmdm.files;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button btnLeer=findViewById(R.id.btnLeer);
        Button btnGuardar=findViewById(R.id.btnGuardar);
        Button btnLoremIpsum=findViewById(R.id.btnLeerLoremIpsum);

        btnLeer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText edRuta=findViewById(R.id.edRuta);
                EditText edTexto=findViewById(R.id.edTexto);
                String ruta=edRuta.getText().toString();
                String linea;

                File f=new File(getRaiz(),ruta);
                try {
                    FileReader fr=new FileReader(f);
                    BufferedReader br=new BufferedReader(fr);
                    edTexto.setText("[Leído el fichero "+f.getAbsolutePath()+"]\n");
                    while((linea=br.readLine())!=null){
                        edTexto.setText(edTexto.getText().toString()+"\n"+linea);
                    }

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });

        btnGuardar.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View view) {
                  EditText edRuta=findViewById(R.id.edRuta);
                  EditText edTexto=findViewById(R.id.edTexto);
                  String ruta=edRuta.getText().toString();
                  File f=new File(getRaiz(),ruta);
                  try {
                      FileWriter fr=new FileWriter(f);
                      fr.write(edTexto.getText().toString());
                      fr.close();
                  } catch (IOException e) {
                      e.printStackTrace();
                  }
                  Toast.makeText(getApplicationContext(),"Fichero guardado en "+f.getAbsolutePath(),
                          Toast.LENGTH_SHORT).show();
              }
        });


        btnLoremIpsum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText edTexto=findViewById(R.id.edTexto);

                InputStream in=getResources().openRawResource(R.raw.lorem_ipsum);
                BufferedReader bf=new BufferedReader(new InputStreamReader(in));

                String linea;
                try {
                    while ((linea = bf.readLine()) != null) {
                        edTexto.setText(edTexto.getText().toString() + "\n" + linea);
                    }
                }catch (IOException ioException){
                    Log.d("Files","Error al leer de lorem ipsum");
                }
            }
        });
    }



    File getRaiz(){
        File raiz;
        CheckBox chkExterno=findViewById(R.id.chkExterno);
        if(chkExterno.isChecked())
            raiz=getApplicationContext().getExternalFilesDir(null);
        else
            raiz=getApplicationContext().getFilesDir();
        return raiz;
    }
}
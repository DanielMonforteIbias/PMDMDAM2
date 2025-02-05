package edu.pmdm.leerarchivo;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

import edu.pmdm.leerarchivo.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;

    private ArrayList<String> lineas = new ArrayList<String>();
    private LinesAdapter adapter;
    private Uri archivoUri;
    private ActivityResultLauncher<Intent> elegirArchivoLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        adapter = new LinesAdapter(lineas);
        binding.listaLineas.setLayoutManager(new LinearLayoutManager(this));
        binding.listaLineas.setAdapter(adapter);
        elegirArchivoLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            Intent data = result.getData();
                            archivoUri = result.getData().getData();
                            binding.txtNombreArchivo.setText(getFileName(archivoUri));
                        }
                    }
                });
        binding.btnElegirArchivo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.setType("text/plain");
                elegirArchivoLauncher.launch(intent);
            }
        });
        binding.btnLeerArchivo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (archivoUri == null) {
                    Toast.makeText(MainActivity.this, "Selecciona un archivo", Toast.LENGTH_SHORT).show();
                    return;
                }
                lineas.clear();
                try {
                    InputStream inputStream = getContentResolver().openInputStream(archivoUri);
                    BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                    String linea = "";
                    while ((linea = reader.readLine()) != null) {
                        lineas.add(linea);
                    }
                    adapter.notifyDataSetChanged();
                    actualizarContadorPalabras();
                } catch (Exception e) {
                    Toast.makeText(MainActivity.this, "Error al leer el archivo", Toast.LENGTH_SHORT).show();
                }
            }
        });
        binding.btnAnadirLinea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText editTextUrl = new EditText(MainActivity.this);
                AlertDialog urlDialog = new AlertDialog.Builder(MainActivity.this)
                        .setTitle("Introduce la linea")
                        .setView(editTextUrl)
                        .setPositiveButton("Añadir", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String linea = editTextUrl.getText().toString();
                                if (!linea.isEmpty()) {
                                    lineas.add(linea);
                                    adapter.notifyItemInserted(lineas.size());
                                } else
                                    Toast.makeText(MainActivity.this, "Linea vacia, no se añade", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setNegativeButton("Cancelar", null)
                        .create();
                urlDialog.show();
            }
        });
        binding.btnGuardarArchivo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (archivoUri == null) {
                    Toast.makeText(MainActivity.this, "Selecciona un archivo", Toast.LENGTH_SHORT).show();
                    return;
                }
                try {
                    OutputStream outputStream = getContentResolver().openOutputStream(archivoUri);
                    BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream));
                    for (String linea : lineas) {
                        writer.write(linea);
                        writer.newLine();
                    }
                    Toast.makeText(MainActivity.this, "Archivo guardado", Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    Toast.makeText(MainActivity.this, "Error al guardar el archivo", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void actualizarContadorPalabras() {
        int contador = 0;
        for (String linea : lineas) {
            String[] palabrasLinea = linea.split("\\s+");
            contador += palabrasLinea.length;
        }
        binding.txtContadorPalabras.setText("Palabras: " + contador);
    }


    private String getFileName(Uri uri) {
        String fileName = "";
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        if (cursor != null) {
            int nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
            if (cursor.moveToFirst()) {
                fileName = cursor.getString(nameIndex);
            }
            cursor.close();
        }
        return fileName;
    }
}
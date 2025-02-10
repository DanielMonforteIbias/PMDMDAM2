package edu.pmdm.leerarchivo;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

import edu.pmdm.leerarchivo.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;

    private ArrayList<String> lines = new ArrayList<String>();
    private LinesAdapter adapter;
    private Uri uri;
    private ActivityResultLauncher<Intent> elegirArchivoLauncher;

    private final int PERMISO_ALMACENAMIENTO=1;

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
        adapter = new LinesAdapter(lines);
        binding.listaLineas.setLayoutManager(new LinearLayoutManager(this));
        binding.listaLineas.setAdapter(adapter);
        elegirArchivoLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            uri = result.getData().getData();
                            binding.txtNombreArchivo.setText(getFileName(uri));
                        }
                    }
                });
        binding.btnElegirArchivo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(comprobarPermisosAlmacenamiento()){
                    Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                    intent.addCategory(Intent.CATEGORY_OPENABLE);
                    intent.setType("text/plain");
                    elegirArchivoLauncher.launch(intent);
                }
                else{
                    pedirPermisosAlmacenamiento();
                    Toast.makeText(MainActivity.this, "Permisos de almacenamiento denegados", Toast.LENGTH_SHORT).show();
                }
            }
        });
        binding.btnLeerArchivo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (uri == null) {
                    Toast.makeText(MainActivity.this, "Selecciona un archivo", Toast.LENGTH_SHORT).show();
                    return;
                }
                lines.clear();
                try {
                    InputStream inputStream = getContentResolver().openInputStream(uri);
                    BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                    String line = "";
                    while ((line = reader.readLine()) != null) {
                        lines.add(line);
                    }
                    adapter.notifyDataSetChanged();
                    updateWordCount();
                    reader.close();
                    inputStream.close();
                } catch (Exception e) {
                    Toast.makeText(MainActivity.this, "Error leyendo el archivo", Toast.LENGTH_SHORT).show();
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
                                    lines.add(linea);
                                    adapter.notifyItemInserted(lines.size());
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
                if (uri == null) {
                    Toast.makeText(MainActivity.this, "Selecciona un archivo", Toast.LENGTH_SHORT).show();
                    return;
                }
                try {
                    OutputStream outputStream = getContentResolver().openOutputStream(uri,"wt"); //wt es importante para sobrescribir el archivo
                    BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream));
                    for (String linea : lines) {
                        writer.write(linea);
                        writer.newLine();
                    }
                    writer.close();
                    outputStream.close();
                    updateWordCount();
                    Toast.makeText(MainActivity.this, "Archivo guardado", Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    Toast.makeText(MainActivity.this, "Error guardando el archivo", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void updateWordCount() {
        int contador = 0;
        for (String line : lines) {
            String[] palabrasLinea = line.split("\\s+");
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

    private boolean comprobarPermisosAlmacenamiento() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) return true; //Si estamos en la API 29 o superior no hacen falta permisos al usar ACTION_OPEN_DOCUMENT
        else return ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
    }

    private void pedirPermisosAlmacenamiento() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISO_ALMACENAMIENTO);
        }
    }
}
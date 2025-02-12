package com.example.imagenestarea;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.media.MediaPlayer;
import android.media.PlaybackParams;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.View;
import android.widget.SeekBar;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.imagenestarea.databinding.ActivityMainBinding;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;

    private String ruta = "";
    private Bitmap photo = null;
    private Bitmap newPhoto = null;

    private MediaPlayer mediaPlayer;

    private ActivityResultLauncher<Intent> cameraLauncher;
    private ActivityResultLauncher<Intent> videoLauncher;

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
        comprobarPermisos();

        cameraLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            Intent data = result.getData();
                            if (data != null) {
                                if (ruta != null && !ruta.isEmpty()) {
                                    photo = BitmapFactory.decodeFile(ruta);
                                    newPhoto = BitmapFactory.decodeFile(ruta);
                                    binding.imgCamara.setImageBitmap(photo);
                                }
                            }
                        }
                    }
                });

        videoLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            Intent data = result.getData();
                            if (data != null) {
                                Uri uri = data.getData();
                                reproducirVideo(uri);
                            }
                        }
                    }
                });

        //LISTENERS
        binding.btnCamara.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (PermissionManager.comprobarPermisosAlmacenamiento(MainActivity.this)) {
                    Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE); //El intent abre la camara
                    if (getApplicationContext().getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
                        File fich_foto = null;
                        try {
                            fich_foto = crearFichero();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        if (fich_foto != null) {
                            Uri foto_uri = FileProvider.getUriForFile(MainActivity.this,
                                    BuildConfig.APPLICATION_ID + ".provider",
                                    fich_foto);
                            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, foto_uri);
                            cameraLauncher.launch(cameraIntent);
                        }
                    }
                } else {
                    Toast.makeText(MainActivity.this, "Permisos denegados", Toast.LENGTH_SHORT).show();
                    PermissionManager.pedirPermisosAlmacenamiento(MainActivity.this);
                }
            }
        });

        binding.btnEscalaGrises.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (photo != null) {
                    newPhoto = aplicarEfecto(newPhoto, escalaGrises());
                    binding.imgCamara.setImageBitmap(newPhoto);
                }
            }
        });
        binding.btnInvertirColores.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (photo != null) {
                    newPhoto = aplicarEfecto(newPhoto, invertirColores());
                    binding.imgCamara.setImageBitmap(newPhoto);
                }
            }
        });
        binding.btnRestaurar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (photo != null) {
                    binding.imgCamara.setImageBitmap(photo);
                    newPhoto = photo; //Quitamos los cambios de la foto nueva igualandola a la original
                    binding.seekBarBrillo.setProgress(255); //Restauramos a la mitad la seekBar
                }
            }
        });
        binding.btnRecortar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (photo != null) {
                    newPhoto = recortarBitmap(newPhoto);
                    binding.imgCamara.setImageBitmap(newPhoto);
                }
            }
        });
        binding.btnRotar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (photo != null) {
                    newPhoto = rotarBitmap(newPhoto, 90);
                    binding.imgCamara.setImageBitmap(newPhoto);
                }
            }
        });

        binding.seekBarBrillo.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (photo != null) {
                    newPhoto = ajustarBrillo(photo, progress); //En vez de usar newPhoto usamos la original para no acumular brillo, si no se volveria blanco o negro
                    binding.imgCamara.setImageBitmap(newPhoto);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        binding.btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (photo != null) guardarImagen(newPhoto);
            }
        });

        binding.btnVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (PermissionManager.comprobarPermisosVideo(MainActivity.this)) {
                    Intent videoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
                    if (videoIntent.resolveActivity(getPackageManager()) != null) {
                        videoLauncher.launch(videoIntent);
                    } else
                        Toast.makeText(MainActivity.this, "No hay app para videos", Toast.LENGTH_SHORT).show();
                } else {
                    PermissionManager.pedirPermisosVideo(MainActivity.this);
                    Toast.makeText(MainActivity.this, "Permisos de video denegados", Toast.LENGTH_SHORT).show();
                }
            }
        });

        binding.btnLento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cambiarVelocidadReproduccion(0.5f);
            }
        });
        binding.btnNormal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cambiarVelocidadReproduccion(1f);
            }
        });
        binding.btnRapido.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cambiarVelocidadReproduccion(2f);
            }
        });
    }

    private Bitmap aplicarEfecto(Bitmap bitmap, ColorMatrix colorMatrix) {
        Bitmap bitmapNuevo = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), bitmap.getConfig());
        Canvas canvas = new Canvas(bitmapNuevo);
        Paint paint = new Paint();
        ColorMatrixColorFilter filtro = new ColorMatrixColorFilter(colorMatrix);
        paint.setColorFilter(filtro);
        canvas.drawBitmap(bitmap, 0, 0, paint);
        return bitmapNuevo;
    }

    private ColorMatrix escalaGrises() {
        ColorMatrix matrix = new ColorMatrix();
        matrix.setSaturation(0);
        return matrix;
    }

    // Invertir colores
    private ColorMatrix invertirColores() {
        float[] invert = {
                -1.0f, 0, 0, 0, 255,
                0, -1.0f, 0, 0, 255,
                0, 0, -1.0f, 0, 255,
                0, 0, 0, 1.0f, 0
        };
        return new ColorMatrix(invert);
    }

    private Bitmap recortarBitmap(Bitmap bitmap) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();

        int newWidth = width / 2;
        int newHeight = height / 2;

        int x = (width - newWidth) / 2;
        int y = (height - newHeight) / 2;

        return Bitmap.createBitmap(bitmap, x, y, newWidth, newHeight);
    }

    private Bitmap rotarBitmap(Bitmap bitmap, float grados) {
        Matrix matrix = new Matrix();
        matrix.postRotate(grados);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }

    private Bitmap ajustarBrillo(Bitmap bitmap, float brillo) {
        brillo = brillo - 255; //Pasamos del rango 0-510 a -255-255
        ColorMatrix colorMatrix = new ColorMatrix();
        colorMatrix.set(new float[]{
                1, 0, 0, 0, brillo,
                0, 1, 0, 0, brillo,
                0, 0, 1, 0, brillo,
                0, 0, 0, 1, 0
        });

        ColorMatrixColorFilter filtro = new ColorMatrixColorFilter(colorMatrix);
        Paint paint = new Paint();
        paint.setColorFilter(filtro);

        Bitmap bitmapBrillo = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), bitmap.getConfig());
        Canvas canvas = new Canvas(bitmapBrillo);
        canvas.drawBitmap(bitmap, 0, 0, paint);

        return bitmapBrillo;
    }

    private void guardarImagen(Bitmap bitmap) {
        try {
            File archivo = crearFichero();
            FileOutputStream fos = new FileOutputStream(archivo);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
            añadirAGaleria(archivo);
            Toast.makeText(this, "Imagen guardada en: " + archivo.getAbsolutePath(), Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Error al guardar la imagen", Toast.LENGTH_SHORT).show();
        }
    }

    public File crearFichero() throws IOException {
        String tiempo = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String nombre_fichero = "JPEG_" + tiempo + "_";
        File directorio_almacenaje = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(nombre_fichero, ".jpg", directorio_almacenaje);
        ruta = image.getAbsolutePath();
        return image;
    }

    public void añadirAGaleria(File f) {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        this.sendBroadcast(mediaScanIntent);
    }

    private void comprobarPermisos() {
        if (!PermissionManager.comprobarPermisosAlmacenamiento(this)) {
            PermissionManager.pedirPermisosAlmacenamiento(this);
        }
    }


    private void reproducirVideo(Uri uri) {
        mediaPlayer = new MediaPlayer();
        binding.surfaceView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(@NonNull SurfaceHolder holder) {
                try {
                    mediaPlayer.prepare();
                    mediaPlayer.setDataSource(MainActivity.this, uri);
                    mediaPlayer.setSurface(holder.getSurface());
                    mediaPlayer.start();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void surfaceChanged(@NonNull SurfaceHolder holder, int format, int width, int height) {

            }

            @Override
            public void surfaceDestroyed(@NonNull SurfaceHolder holder) {
                if (mediaPlayer != null) {
                    mediaPlayer.release();
                    mediaPlayer = null;
                }
            }
        });
    }

    private void cambiarVelocidadReproduccion(float velocidad) {
        if (mediaPlayer != null) {
            PlaybackParams playbackParams = new PlaybackParams();
            playbackParams.setSpeed(velocidad);
            mediaPlayer.setPlaybackParams(playbackParams);
        }
    }
}
package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import com.example.myapplication.BuildConfig;


public class MainActivity extends AppCompatActivity implements SeekBar.OnSeekBarChangeListener {
private final int CAPTURA_IMAGEN=10;
        ImageView mImageView;
        SeekBar seekBar;
        ToggleButton toggleButton;

@Override
protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mImageView=(ImageView)findViewById(R.id.imageView);
        seekBar=(SeekBar)findViewById(R.id.seekBar);
        toggleButton=(ToggleButton)findViewById(R.id.toggleButton);
        seekBar.setOnSeekBarChangeListener(this);
        seekBar.setProgress(100);

        comprobarPermisos();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_WRITE_EXTERNAL_STORAGE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    private final int MY_PERMISSIONS_WRITE_EXTERNAL_STORAGE=10;
    public void comprobarPermisos(){
        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED  || checkSelfPermission(Manifest.permission.CAMERA) !=PackageManager.PERMISSION_GRANTED ){

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

                // Show an expanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.CAMERA},
                        MY_PERMISSIONS_WRITE_EXTERNAL_STORAGE);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }
    }

    public void hacerFoto(View v){
        Intent hacerFotoIntent= new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if(getApplicationContext().getPackageManager().hasSystemFeature(
                PackageManager.FEATURE_CAMERA)){
            //Crear un fichero y pasárselo como extra a la actividad
            File fich_foto=null;
            try {
                fich_foto=crearFichero();
            }
            catch(IOException e){
                //acción a realizar si no he podido crear el fichero
                e.printStackTrace();
            }
            if(fich_foto!=null){
                Uri foto_uri = FileProvider.getUriForFile(this,
                        BuildConfig.APPLICATION_ID + ".provider",
                        fich_foto);
                hacerFotoIntent.putExtra(MediaStore.EXTRA_OUTPUT, foto_uri);
                startActivityForResult(hacerFotoIntent,CAPTURA_IMAGEN);
            }
        }
    }

    public String mRutaDefinitiva="";
    public File crearFichero() throws IOException{
        String tiempo=new
                SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String nombre_fichero="JPEG_"+tiempo+"_";
        File directorio_almacenaje=
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File image=File.createTempFile(nombre_fichero,".jpg",directorio_almacenaje);
        mRutaDefinitiva=image.getAbsolutePath();
        return image;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode,resultCode,data);
        if(requestCode==CAPTURA_IMAGEN && resultCode==RESULT_OK){
            Toast.makeText(this,
                    "Has guardado una imagen en "+mRutaDefinitiva,Toast.LENGTH_LONG).show();
            añadirAGaleria();
            //Cargar la foto en el imageView
            reescala();
        }
    }
    public void añadirAGaleria(){
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(mRutaDefinitiva);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        this.sendBroadcast(mediaScanIntent);
    }

    private void resizeDecoder() {
        int scaleFactor=0;
        // Calcular factor de escalado: 1->100, 2->50, 4->25, ...
        int progreso=seekBar.getProgress();
        if(progreso!=0)
            scaleFactor = 100/seekBar.getProgress();
        else
            scaleFactor=Integer.MAX_VALUE;

        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        BitmapFactory.decodeFile(mRutaDefinitiva, bmOptions);
        int anchoOriginal = bmOptions.outWidth;
        int altoOriginal = bmOptions.outHeight;

        // El decoder solo escala en potencias de 2
        bmOptions.inSampleSize = scaleFactor;
        bmOptions.inPurgeable = true;
        Bitmap bitmap = BitmapFactory.decodeFile(mRutaDefinitiva, bmOptions);
        int anchoFinal=bmOptions.outWidth;
        int altoFinal=bmOptions.outHeight;
        mImageView.getLayoutParams().height=altoFinal;
        mImageView.getLayoutParams().width=anchoFinal;
        mImageView.setImageBitmap(bitmap);

    }

    private void resizeScaled() {
        // Calcular factor de escalado en porcentaje: 100%->tamaño original
        int progreso=seekBar.getProgress();

        // cargar el bitmap
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();

        Bitmap bitmap =BitmapFactory.decodeFile(mRutaDefinitiva, bmOptions);
        int anchoOriginal = bmOptions.outWidth;
        int altoOriginal = bmOptions.outHeight;
        try{
            Bitmap nuevoBitmap=bitmap.createScaledBitmap(
                    bitmap,
                    anchoOriginal*progreso/100,
                    altoOriginal*progreso/100,false);

            int altoFinal=nuevoBitmap.getHeight();
            int anchoFinal=nuevoBitmap.getWidth();

            mImageView.getLayoutParams().height=altoFinal;
            mImageView.getLayoutParams().width=anchoFinal;
            mImageView.setImageBitmap(nuevoBitmap);

        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
        reescala();
    }

    public void reescala(){
        if(mRutaDefinitiva!="")
            if(!toggleButton.isChecked())
                resizeDecoder();
            else
                resizeScaled();
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }
}

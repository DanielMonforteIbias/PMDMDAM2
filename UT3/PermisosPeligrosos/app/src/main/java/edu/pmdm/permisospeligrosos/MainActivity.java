package edu.pmdm.permisospeligrosos;

import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {
    public final int PERMISO_UBICACION=1;
    private TextView txtUbicacion;
    private LocationManager locationManager;
    private Criteria criteria = new Criteria();

    private Location location;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        locationManager=(LocationManager)getSystemService(Context.LOCATION_SERVICE);
        txtUbicacion=findViewById(R.id.txtUbicacion);
        if(checkSelfPermission("android.permission.ACCESS_COARSE_LOCATION")== PackageManager.PERMISSION_GRANTED) {
            actualizarUbicacion();
        }
        else {
            txtUbicacion.setText("Permiso de ubicacion denegado!");
            requestPermissions(new String[]{"android.permission.ACCESS_COARSE_LOCATION"},PERMISO_UBICACION);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==PERMISO_UBICACION)
            if(grantResults[0]==PackageManager.PERMISSION_GRANTED) actualizarUbicacion();

    }
    public void actualizarUbicacion(){
       if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{"android.permission.ACCESS_COARSE_LOCATION"},PERMISO_UBICACION);
            return;
        }
        location=locationManager.getLastKnownLocation(locationManager.getBestProvider(criteria, false));
        double latitud = location.getLatitude();
        double longitud = location.getLongitude();
        String ubicacion="Latitud: "+latitud+"\nLongitud: "+longitud;
        txtUbicacion.setText(ubicacion);
    }
}
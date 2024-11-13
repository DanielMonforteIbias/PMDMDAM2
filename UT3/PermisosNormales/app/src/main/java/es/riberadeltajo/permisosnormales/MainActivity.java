package es.riberadeltajo.permisosnormales;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.pm.PackageManager;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    public final int PERMISO_ENVIAR_SMS=1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView textWifi=findViewById(R.id.txtWifiState);
        TextView textSMS=findViewById(R.id.txtPermisosSMS);
        Button btnActualizar=findViewById(R.id.btnActualizar);


        //consultar el estado WIFI -> permisos normales (Solo en el manifest)
        WifiManager wifi=(WifiManager) getSystemService(Context.WIFI_SERVICE);
        btnActualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(wifi.isWifiEnabled())
                    textWifi.setText("Wifi ON");
                else
                    textWifi.setText("Wifi OFF");



                if(checkSelfPermission("android.permission.SEND_SMS")== PackageManager.PERMISSION_GRANTED) {
                    textSMS.setText("Puede Enviar SMS");
                    enviarSMS();
                }
                else {
                    textSMS.setText("Denegado!");
                    requestPermissions(new String[]{"android.permission.SEND_SMS"},PERMISO_ENVIAR_SMS);
                }
            }
        });


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==PERMISO_ENVIAR_SMS)
            if(grantResults[0]==PackageManager.PERMISSION_GRANTED)
                enviarSMS();
    }


    public void enviarSMS(){
        //enviar un sms?
        SmsManager smsManager=SmsManager.getDefault();
        smsManager.sendTextMessage("666666688",null,"Hola, quedamos?",null,null);
    }
}
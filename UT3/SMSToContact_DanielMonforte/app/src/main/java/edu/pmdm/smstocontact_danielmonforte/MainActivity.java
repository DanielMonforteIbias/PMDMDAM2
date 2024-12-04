package edu.pmdm.smstocontact_danielmonforte;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.InputStream;
import java.lang.reflect.Array;
import java.util.ArrayList;

import edu.pmdm.smstocontact_danielmonforte.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    public ContactosAdapter adaptador;
    private ArrayList<Contacto>contactos=new ArrayList<Contacto>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater()); //Inicializamos la variable de binding
        View view= binding.getRoot(); //Obtenemos la vista total del layout
        setContentView(view); //Establecemos el contenido de la vista como el obtenido anteriormente
        EdgeToEdge.enable(this);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        binding.btnSeleccionarContacto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.editTextNombre.setVisibility(View.VISIBLE);
                binding.editTextApellido.setVisibility(View.VISIBLE);
                binding.btnFiltrarPorApellido.setVisibility(View.VISIBLE);
                binding.btnFiltrarPorNombre.setVisibility(View.VISIBLE);
            }
        });
        binding.btnEnviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String mensaje=binding.editTextMensaje.getText().toString();
            }
        });
        adaptador=new ContactosAdapter(contactos);
        binding.recyclerViewContactos.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerViewContactos.setAdapter(adaptador);
    }

    public InputStream abrirFoto(int id){
        Uri contactUri= ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI,id);
        InputStream inputStream= ContactsContract.Contacts.openContactPhotoInputStream(getContentResolver(),contactUri,true);
        return inputStream;
    }
    public void enviarSMS(String telefono,String mensaje){
        try{
            SmsManager smsManager=SmsManager.getDefault();
            smsManager.sendTextMessage(telefono,null,mensaje,null,null);
            showToast("Mensaje enviado");
        }catch(Exception e){
            showToast("SMS no enviado, intentelo de nuevo");
            e.printStackTrace();
        }
    }
    private void buscar(String contacto){
        String proyeccion[]={ContactsContract.Contacts._ID,ContactsContract.Contacts.DISPLAY_NAME,ContactsContract.Contacts.HAS_PHONE_NUMBER,ContactsContract.Contacts.PHOTO_ID};
        String filtro=ContactsContract.Contacts.DISPLAY_NAME+" like ? ";
        String argsFiltro[]={"%"+contacto+"%"};
        ContentResolver cr=getContentResolver();
        Cursor cur=cr.query(ContactsContract.Contacts.CONTENT_URI,proyeccion,filtro,argsFiltro,null);
        if(cur.getCount()>0){
            while(cur.moveToNext()){
                String id=cur.getString(cur.getColumnIndex(ContactsContract.Contacts._ID));
                String nombre=cur.getString(cur.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                if(Integer.parseInt(cur.getString(cur.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)))>0) {
                    contactos.add(new Contacto(id,nombre));
                }
            }
        }
        cur.close();
    }
    public void showToast(String s){
        Toast.makeText(this,s,Toast.LENGTH_SHORT).show();
    }
}
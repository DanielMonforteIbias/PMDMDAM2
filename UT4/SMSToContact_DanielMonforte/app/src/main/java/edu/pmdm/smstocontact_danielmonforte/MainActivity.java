package edu.pmdm.smstocontact_danielmonforte;

import android.Manifest;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.telephony.SmsManager;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
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
    private static final int ACCEDER_A_CONTACTOS = 1;
    private static final int ENVIAR_SMS = 2;

    public static ActivityMainBinding binding;
    public ContactosAdapter adaptador;
    private ArrayList<Contacto>contactos=new ArrayList<Contacto>();
    public static Contacto contactoSeleccionado=null;

    String mensaje="";
    int longitudMaxima=160;
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
        pedirPermisosContactos();

        binding.btnSeleccionarContacto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(comprobarPermisosContactos()){
                    binding.editTextNombre.setVisibility(View.VISIBLE);
                    binding.editTextApellido.setVisibility(View.VISIBLE);
                    binding.btnFiltrarPorApellido.setVisibility(View.VISIBLE);
                    binding.btnFiltrarPorNombre.setVisibility(View.VISIBLE);
                }
                else{
                    showToast("Permiso de leer contactos denegado");
                    pedirPermisosContactos();
                }
            }
        });
        binding.btnFiltrarPorNombre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nombre=binding.editTextNombre.getText().toString();
                buscar(nombre);
            }
        });
        binding.btnEnviar.setEnabled(false); //Al principio está desactivado
        binding.btnEnviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(comprobarPermisosSMS()){
                    mensaje=binding.editTextMensaje.getText().toString();
                    crearDialogo(mensaje);
                    enviarSMS(contactoSeleccionado.getTelefono(),mensaje);
                }
                else {
                    pedirPermisosSMS();
                    showToast("Permisos de SMS denegados");
                }
            }
        });
        binding.txtCaracteresRestantes.setText(mensaje.length()+" de "+longitudMaxima);
        binding.editTextMensaje.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                boolean contieneUnicode = contieneUnicode(s.toString());
                if(contieneUnicode(s.toString()))longitudMaxima=70;
                else longitudMaxima=160;
                binding.editTextMensaje.setFilters(new InputFilter[]{ new InputFilter.LengthFilter(longitudMaxima) });

                binding.txtCaracteresRestantes.setText(s.length()+" de "+longitudMaxima);
                if(binding.editTextMensaje.getText().toString().equals("")) binding.btnEnviar.setEnabled(false);
                else binding.btnEnviar.setEnabled(true);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        adaptador=new ContactosAdapter(contactos);
        binding.recyclerViewContactos.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerViewContactos.setAdapter(adaptador);
    }

    private void pedirPermisosContactos(){
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_CONTACTS}, ACCEDER_A_CONTACTOS);
    }

    private boolean comprobarPermisosContactos() {
        return ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED;
    }

    private void pedirPermisosSMS(){
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS}, ENVIAR_SMS);
    }

    private boolean comprobarPermisosSMS() {
        return ActivityCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED;
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
        contactos.clear(); //Limpiamos la lista
        String proyeccion[]={ContactsContract.Contacts._ID,ContactsContract.Contacts.DISPLAY_NAME,ContactsContract.Contacts.HAS_PHONE_NUMBER,ContactsContract.Contacts.PHOTO_ID};
        String filtro=ContactsContract.Contacts.DISPLAY_NAME+" like ? ";
        String argsFiltro[]={"%"+contacto+"%"};
        ContentResolver cr=getContentResolver();
        Cursor cur=cr.query(ContactsContract.Contacts.CONTENT_URI,proyeccion,filtro,argsFiltro,ContactsContract.Contacts.DISPLAY_NAME + " ASC");
        if(cur.getCount()>0){
            while(cur.moveToNext()){
                String id=cur.getString(cur.getColumnIndex(ContactsContract.Contacts._ID));
                String nombre=cur.getString(cur.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                if(Integer.parseInt(cur.getString(cur.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)))>0) {
                    String telefono="";
                    Cursor phones = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + id, null, null);
                    while (phones.moveToNext()) {
                        telefono = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));

                    }
                    phones.close();
                    contactos.add(new Contacto(id,nombre,telefono));
                    adaptador.notifyDataSetChanged();
                }
            }
        }

        cur.close();
    }
    private void crearDialogo(String mensaje){
        View alertCustomDialog= LayoutInflater.from(MainActivity.this).inflate(R.layout.dialogo_mensaje_enviado,null);
        AlertDialog.Builder alertDialog=new AlertDialog.Builder(MainActivity.this);
        alertDialog.setView(alertCustomDialog);
        AlertDialog dialog=alertDialog.create();
        Button btnVolver=alertCustomDialog.findViewById(R.id.btnVolver);
        TextView txtMensaje=alertCustomDialog.findViewById(R.id.txtMensaje);
        TextView txtTelefono=alertCustomDialog.findViewById(R.id.txtTelefono);
        txtTelefono.setText(contactoSeleccionado.getTelefono());
        txtMensaje.setText(mensaje);
        btnVolver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });
        dialog.show();
    }
    public void showToast(String s){
        Toast.makeText(this,s,Toast.LENGTH_SHORT).show();
    }

    private boolean contieneUnicode(String cadena) {
        for (int i = 0; i < cadena.length(); i++) {
            if (cadena.charAt(i) > 127) { // Si el carácter tiene un valor mayor que 127, es Unicode
                return true;
            }
        }
        return false;
    }
}
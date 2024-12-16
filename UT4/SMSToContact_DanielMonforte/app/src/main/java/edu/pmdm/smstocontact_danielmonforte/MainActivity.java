package edu.pmdm.smstocontact_danielmonforte;

import android.Manifest;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import android.widget.ImageView;
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
import edu.pmdm.smstocontact_danielmonforte.databinding.DialogoMensajeEnviadoBinding;

public class MainActivity extends AppCompatActivity {
    private static final int ACCEDER_A_CONTACTOS = 1;
    private static final int ENVIAR_SMS = 2;

    public static ActivityMainBinding binding;
    private DialogoMensajeEnviadoBinding dialogoBinding;
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
                String apellido=binding.editTextApellido.getText().toString();
                buscar(nombre,apellido);
            }
        });
        binding.btnFiltrarPorApellido.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nombre=binding.editTextNombre.getText().toString();
                String apellido=binding.editTextApellido.getText().toString();
                buscar(nombre,apellido);
            }
        });
        binding.btnEnviar.setEnabled(false); //Al principio está desactivado
        binding.btnEnviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(comprobarPermisosSMS()){
                    mensaje=binding.editTextMensaje.getText().toString(); //Obtenemos el mensaje
                    crearDialogo(mensaje); //Creamos un dialogo con ese mensaje
                    enviarSMS(contactoSeleccionado.getTelefono(),mensaje); //Enviamos un SMS al contacto seleccionado con el mensaje introducido
                    binding.editTextMensaje.setText(""); //Vaciamos la caja del mensaje
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
        binding.recyclerViewContactos.setBackgroundResource(R.drawable.fondo_recycler_view);
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
    private void buscar(String nombreContacto, String apellidoContacto){
        contactos.clear(); //Limpiamos la lista
        //El comodin ? se reemplazara por _ para que el filtro funcione bien y el caracter en esa posicion sea cualquiera
        nombreContacto = nombreContacto.replace('?', '_');
        apellidoContacto = apellidoContacto.replace('?', '_');

        String proyeccion[]={ContactsContract.Contacts._ID,ContactsContract.Contacts.DISPLAY_NAME,ContactsContract.Contacts.HAS_PHONE_NUMBER,ContactsContract.Contacts.PHOTO_ID};
        String filtro=ContactsContract.Contacts.DISPLAY_NAME+" like ? ";
        String argsFiltro[]={nombreContacto+"% "+apellidoContacto+"%"};
        ContentResolver cr=getContentResolver();
        Cursor cur=cr.query(ContactsContract.Contacts.CONTENT_URI,proyeccion,filtro,argsFiltro,ContactsContract.Contacts.DISPLAY_NAME + " ASC");
        if(cur.getCount()>0){
            while(cur.moveToNext()){
                String id=cur.getString(cur.getColumnIndex(ContactsContract.Contacts._ID));
                String nombre=cur.getString(cur.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                Bitmap foto= BitmapFactory.decodeStream(abrirFoto(Integer.parseInt(id))); //Obtenemos un bitmap de la foto del contacto
                if(Integer.parseInt(cur.getString(cur.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)))>0) {
                    String telefono="";
                    Cursor cursorTelefonos = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + id, null, null);
                    while (cursorTelefonos.moveToNext()) {
                        telefono = cursorTelefonos.getString(cursorTelefonos.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DATA));
                    }
                    cursorTelefonos.close();
                    contactos.add(new Contacto(id,nombre,telefono,foto));
                }
            }
        }
        adaptador.notifyDataSetChanged(); //Notificamos al adaptador para actualizar la lista con los contactos coincidentes
        cur.close();
    }
    private void crearDialogo(String mensaje){
        dialogoBinding=DialogoMensajeEnviadoBinding.inflate(getLayoutInflater()); //Inicializamos la variable de binding del dialogo
        AlertDialog.Builder alertDialog=new AlertDialog.Builder(MainActivity.this);
        alertDialog.setView(dialogoBinding.getRoot());
        AlertDialog dialog=alertDialog.create();
        dialogoBinding.imgViewContacto.setImageBitmap(contactoSeleccionado.getFoto());
        dialogoBinding.txtTelefono.setText(contactoSeleccionado.getTelefono());
        dialogoBinding.txtMensaje.setText(mensaje);
        dialogoBinding.btnVolver.setOnClickListener(new View.OnClickListener() {
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
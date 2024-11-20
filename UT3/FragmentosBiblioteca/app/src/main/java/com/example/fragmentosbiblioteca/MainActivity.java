package com.example.fragmentosbiblioteca;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentContainerView;
import androidx.fragment.app.FragmentManager;

public class MainActivity extends AppCompatActivity {
    private FragmentContainerView fragmentContainer;
    private Button boton;

    private View.OnClickListener onClickListenerVolver;
    private View.OnClickListener onClickListenerAgregarLibro;

    private ActivityResultLauncher<Intent> addBookActivityLauncher;
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
        fragmentContainer=findViewById(R.id.fragmentContainerView);
        boton=findViewById(R.id.btnMain);
        DataSource.añadirLibrosPrueba();
        crearListeners();
        boton.setOnClickListener(onClickListenerAgregarLibro); //En un primer momento ponemos el onCLickListener de agregar libros

        //Launcher de AddBookActivity. Obtendremos lo que se haya ingresado y lo meteremos en la lista
        addBookActivityLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            Intent data = result.getData();
                            Book libro=data.getParcelableExtra("Libro"); //Obtenemos el objeto Libro
                            DataSource.LIBROS.add(libro); //Lo añadimos al ArrayList
                            Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.fragmentContainerView); //Encontramos el fragmento con el RecyclerView
                            if (fragment instanceof BookListFragment) { //Si es de tipo BookListFragment
                                ((BookListFragment) fragment).adaptador.notifyItemInserted(DataSource.LIBROS.size()-1); //Notificamos a su adaptador
                            }
                        }
                    }
                });
    }

    public void crearListeners(){
        onClickListenerAgregarLibro=new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), AddBookActivity.class);
                addBookActivityLauncher.launch(intent); //Lanzamos la actividad de AddBook
            }
        };
        onClickListenerVolver=new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainerView, new BookListFragment()).addToBackStack(null).commit(); //Reemplazamos el fragment actual por el de BookListFragment
            }
        };
        //Listener que se ejecuta cuando hay cambios en el back stack, como reemplazar fragmentos
        getSupportFragmentManager().addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
            @Override
            public void onBackStackChanged() {
                System.out.println("a");
                Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.fragmentContainerView); //Obtenemos el fragmento
                if (currentFragment instanceof BookDetailFragment) { //Si es de tipo BookDetailFragment
                    System.out.println("a");
                    boton.setText("Volver"); //Ponemos "Volver" en el botón
                    boton.setOnClickListener(onClickListenerVolver); //Le damos el listener de volver
                } else if (currentFragment instanceof BookListFragment) { //Si es de tipo BookListFragment
                    System.out.println("b");
                    boton.setText("Agregar libro"); //Ponemos "Agregar libro" en el botón
                    boton.setOnClickListener(onClickListenerAgregarLibro); //Le damos el Listener de agregar libros
                }
            }
        });
    }
}
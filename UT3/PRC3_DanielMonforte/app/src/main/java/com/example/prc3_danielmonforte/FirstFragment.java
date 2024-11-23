package com.example.prc3_danielmonforte;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.prc3_danielmonforte.databinding.FragmentFirstBinding;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class FirstFragment extends Fragment {
    private FragmentFirstBinding binding;

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = FragmentFirstBinding.inflate(inflater, container, false);
        if(!BikeActivity.fecha.equals("")) { //Si ya se ha seleccionado una fecha antes
            long fechaLong=0;
            try {
                fechaLong= new SimpleDateFormat("dd/MM/yyyy").parse(BikeActivity.fecha).getTime(); //Obtenemos el valor en long de la fecha
            } catch (ParseException e) {
                Toast.makeText(getActivity(),"Error al convertir la fecha",Toast.LENGTH_SHORT);
            }
            binding.calendarView.setDate(fechaLong); //Seleccionamos la fecha en el CalendarView
            binding.txtCalendario.setText("Date: "+BikeActivity.fecha); //Actualizamos el TextView para que ya tenga la fecha seleccionada
        }

        //Listener del calendarView para detectar cambios de fecha
        binding.calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                BikeActivity.fecha=dayOfMonth+"/"+(month+1)+"/"+year; //Damos valor a la fecha segun lo seleccionado. Se suma 1 al mes porque empieza en 0, pero queremos que enero sea el 1
                binding.txtCalendario.setText("Date: "+BikeActivity.fecha); //Ponemos el dia, mes y año de la fecha seleccionada en el textView
                BikeActivity.editor.putString("Fecha",BikeActivity.fecha).apply(); //Guardamos la nueva fecha en Preferencias
            }
        });
        //Listener del botón para navegar al segundo fragmento
        binding.btnListaBicicletas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!BikeActivity.fecha.equals("")) NavHostFragment.findNavController(FirstFragment.this).navigate(R.id.action_FirstFragment_to_SecondFragment); //Si hay fecha, podemos pasar al siguiente fragmento
                else Toast.makeText(getActivity(),"Selecciona una fecha",Toast.LENGTH_SHORT).show(); //Si no, avisamos al usuario de que debe seleccionar una
            }
        });
        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
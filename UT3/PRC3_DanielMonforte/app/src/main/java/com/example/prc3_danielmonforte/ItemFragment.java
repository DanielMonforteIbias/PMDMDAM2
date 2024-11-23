package com.example.prc3_danielmonforte;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.prc3_danielmonforte.bikes.BikesContent;
import com.example.prc3_danielmonforte.placeholder.PlaceholderContent;

/**
 * A fragment representing a list of Items.
 */
public class ItemFragment extends Fragment {

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ItemFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_item_list, container, false);
        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
            MyItemRecyclerViewAdapter adaptador=new MyItemRecyclerViewAdapter(BikesContent.ITEMS);
            recyclerView.setAdapter(adaptador);
            if(adaptador.getItemCount()==0) Toast.makeText(getContext(),"No existen datos de bicicletas",Toast.LENGTH_LONG).show(); //Si no hay items en la lista, avisamos al usuario
        }
        return view;
    }
}
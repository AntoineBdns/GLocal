package com.example.glocal;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class ListFragment extends Fragment {
    private static final String TAG = "ListFragment";

    private Button btnTest;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.list_fragment, container, false);

        btnTest = (Button) view.findViewById(R.id.btnTest);
        btnTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(), "TESTING BUTTON CLICK", Toast.LENGTH_SHORT).show();
            }
        });

        PlaceAdapter adapter = new PlaceAdapter(getContext());
        ListView listView = (ListView) view.findViewById(R.id.listViewPlaces);
        listView.setAdapter(adapter);

        // Construction des données de test
        PlaceList tmp = new PlaceList();
        tmp.getPlaces(10.0,20.0);

        adapter.setList(tmp);

        AdapterView.OnItemClickListener itemClickListener = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View container, int position, long id) {
                Place place = (Place) parent.getItemAtPosition(position);
                Toast.makeText(getContext(), "Place : " + place.adresse, Toast.LENGTH_LONG).show();
                /*
                ArrayList<String> lesInfos = contact.getInfos();
                Intent intent = new Intent(ListeContacts.this, InfosContact.class);
                intent.putStringArrayListExtra("InfosContact", lesInfos);
                ListeContacts.this.startActivity(intent);
                */
            }
        };
        listView.setOnItemClickListener(itemClickListener);

        return view;
    }
}

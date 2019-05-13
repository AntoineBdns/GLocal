package com.example.glocal;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.Arrays;

public class ListFragment extends Fragment {
    private static final String TAG = "ListFragment";

    private Button btnTest;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.list_fragment, container, false);

        Bundle params = this.getArguments();
        int size = params.getInt("size");

        ArrayList<Place> placesList = new ArrayList<>();

        for (int i = 0; i < size; i++) {
            String[] data = params.getStringArray("" + i);
            String nom = data[0];
            String adresse = data[1];
            String categorie = data[2];
            String latitude = data[3];
            String longitude = data[4];
            placesList.add(new Place(nom, adresse, categorie, latitude, longitude));
        }

        PlaceAdapter adapter = new PlaceAdapter(getContext());
        ListView listView = (ListView) view.findViewById(R.id.listViewPlaces);
        listView.setAdapter(adapter);

        adapter.setList(placesList);

        AdapterView.OnItemClickListener itemClickListener = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View container, int position, long id) {
                Place place = (Place) parent.getItemAtPosition(position);
                Toast.makeText(getContext(), "Coordonnées : (" + place.latitude + ", " + place.longitude + ")", Toast.LENGTH_LONG).show();
            }
        };
        listView.setOnItemClickListener(itemClickListener);

        return view;
    }
}

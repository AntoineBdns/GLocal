package com.example.glocal;


import com.google.android.gms.maps.model.LatLng;

public class Place {
    String nom;
    String adresse;
    String telephone;
    LatLng loca;

    public Place (String _nom,String _adresse, String _telephone,LatLng _loca) {
        nom = _nom;
        adresse = _adresse;
        telephone = _telephone;
        loca = _loca;
    }


}

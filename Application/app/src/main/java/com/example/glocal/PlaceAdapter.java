package com.example.glocal;

import android.database.ContentObservable;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.content.Context;

import java.util.ArrayList;

public class PlaceAdapter extends BaseAdapter {

    private LayoutInflater inflater;
    private ArrayList<Place> placesList = null;

    public PlaceAdapter(Context context) {
        super();

        inflater = LayoutInflater.from(context);
        placesList = new ArrayList<Place>();

    }

    public void setList(ArrayList<Place> list) {
        this.placesList = list;
    }

    @Override
    public int getCount() {
        return placesList.size();
    }

    @Override
    public Object getItem(int arg0) {
        return placesList.get(arg0);
    }

    @Override
    public long getItemId(int arg0) {
        return arg0;
    }

    @Override
    public View getView(int arg0, View convertView, ViewGroup parent) {
        LinearLayout layoutItem;

        if (convertView == null) {
            layoutItem = (LinearLayout) inflater.inflate(
                    R.layout.place_row, parent, false);
        } else {
            layoutItem = (LinearLayout) convertView;
        }

        TextView adresseView = (TextView) layoutItem.findViewById(R.id.textViewAdresse);
        adresseView.setText(this.placesList.get(arg0).adresse);
        TextView telephoneView = (TextView) layoutItem.findViewById(R.id.textViewTelephone);
        telephoneView.setText(this.placesList.get(arg0).telephone);
        TextView coordonneesView = (TextView) layoutItem.findViewById(R.id.textViewCoordonnees);
        coordonneesView.setText(this.placesList.get(arg0).coordonnees);

        return layoutItem;
    }
}


package com.example.glocal;

import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.loopj.android.http.*;

import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;


public class PlaceList extends ArrayList<Place>{
    public void getPlaces(double latitude, double longitude){
        RequestParams params = new RequestParams();
        params.put("app_id","J1QEb7Ad09VFODkddGYj");
        params.put("app_code","n6LWISWbT3Daq45oIxpEmw");
        params.put("at",latitude+","+longitude);

        PlacesRestClient.get("places/v1/discover/explore", params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject data) {
                try {
                    insertData(data.getJSONObject("results"));
                }catch (Exception e){
                    Log.d("ERROR REST",e.getMessage());
                }
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.d("Failure",errorResponse.toString()+" \n"+throwable.getMessage());
            }
        });
    }

    public void insertData(JSONObject jOBJ) throws Exception {
        try {
            JSONArray array = jOBJ.getJSONArray("items");
            for(int i = 0 ;i < array.length(); i++){
               JSONObject elt = array.getJSONObject(i);
               String nom = elt.getString("title");
               String adresse = elt.getString("vicinity")  ;
               String category = elt.getJSONObject("category").getString("title");
               String localisation = elt.getString("position").replaceAll("\\[|\\]", "");
               double latitude = Double.parseDouble(localisation.split(",")[0]);
               double longitude = Double.parseDouble(localisation.split(",")[1]);
               this.add(
                       new Place(nom,adresse,category,new LatLng(latitude,longitude))
               );
            }
        }catch (Exception e){
            throw new Exception(e.getMessage());
        }
        Log.d("TESTa",this.size()+"");
    }
}






package com.example.glocal;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class MapFragment extends SupportMapFragment implements OnMapReadyCallback {
    private boolean needsInit=false;

    private GoogleMap mMap;
    private GoogleApiClient mGoogleApiClient;
    private LatLngBounds.Builder mBounds = new LatLngBounds.Builder();
    private boolean mLocationPermissionGranted;
    private static final int REQUEST_PLACE_PICKER = 1;



    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (savedInstanceState == null) {
            needsInit=true;
        }

        getMapAsync(this);

        // Set up the API client for Places API
        mGoogleApiClient = new GoogleApiClient.Builder(getContext())
                .addApi(Places.GEO_DATA_API)
                .build();
        mGoogleApiClient.connect();


    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Ask permission to use location service
        getLocationPermission();

        setLocation();


        /*PlaceList list = new PlaceList();
        list.getPlaces(50.9580472,2.3203764);
        Log.d("Number of places",list.size()+"");
        for (com.example.glocal.Place p: list ) {
            mMap.addMarker(new MarkerOptions().position(p.loca).title(p.nom));
        }*/

        // Add a marker in Sydney and move the camera
//        LatLng sydney = new LatLng(-34, 151);
//        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));



    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[],
                                           @NonNull int[] grantResults) {
        mLocationPermissionGranted = false;
        switch (requestCode) {
            case 1: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mLocationPermissionGranted = true;
                }
            }
        }
        setLocation();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_PLACE_PICKER) {
            if (resultCode == Activity.RESULT_OK) {
                Place place = PlacePicker.getPlace(data, getContext());
            } else if (resultCode == PlacePicker.RESULT_ERROR) {
                Toast.makeText(getContext(), "Places API failure! Check that the API is enabled for your key",
                        Toast.LENGTH_LONG).show();
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void getLocationPermission() {
        /*
         * Request location permission, so that we can get the location of the
         * device. The result of the permission request is handled by a callback,
         * onRequestPermissionsResult.
         */
        if (ContextCompat.checkSelfPermission(getContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mLocationPermissionGranted = true;
        } else {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    1);
        }
    }


    private void setLocation() throws SecurityException {
        mMap.setMyLocationEnabled(true);


        mMap.setOnMyLocationChangeListener(new GoogleMap.OnMyLocationChangeListener() {
            @Override
            public void onMyLocationChange(Location location) {
                LatLng ll = new LatLng(location.getLatitude(), location.getLongitude());
                // we only want to grab the location once, to allow the user to pan and zoom freely.
                mMap.setOnMyLocationChangeListener(null);
                mMap.moveCamera(CameraUpdateFactory.newLatLng(ll));
                getPlaces(location.getLatitude(),location.getLongitude());
            }
        });
    }
     public void getPlaces(double lat,double lon){
         RequestParams params = new RequestParams();
         params.put("app_id","J1QEb7Ad09VFODkddGYj");
         params.put("app_code","n6LWISWbT3Daq45oIxpEmw");
         params.put("in",lat+","+lon+";r=10000");
         params.put("size","200");

         PlacesRestClient.get("places/v1/discover/explore", params, new JsonHttpResponseHandler() {
             @Override
             public void onSuccess(int statusCode, Header[] headers, JSONObject data) {
                 try {
                     JSONArray array = data.getJSONObject("results").getJSONArray("items");
                     for (int i = 0; i < array.length(); i++) {
                         JSONObject elt = array.getJSONObject(i);
                         String nom = elt.getString("title");
                         String adresse = elt.getString("vicinity").replaceAll("<br/>"," ");
                         String category = elt.getJSONObject("category").getString("title");
                         String localisation = elt.getString("position").replaceAll("\\[|\\]", "");
                         double latitude = Double.parseDouble(localisation.split(",")[0]);
                         double longitude = Double.parseDouble(localisation.split(",")[1]);
                         BitmapDescriptor iconMarker;
                         switch (category){
                             case "Restaurant":
                             case "Snacks/Fast food":
                             case "Food & Drink":
                                 iconMarker = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE);
                                 break;
                             case "Hotel":
                             case "Hôtel":
                                 iconMarker = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE);
                                 break;
                             case "Bar/Pub":
                             case "Cofee/Tea":
                                 iconMarker = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED);
                                 break;

                             default:
                                 iconMarker = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN);
                                 break;
                         }
                         Marker tmp = mMap.addMarker(
                                 new MarkerOptions()
                                    .position(new LatLng(latitude, longitude))
                                    .title(nom)
                                    .snippet(category+"\n"+adresse)
                                    .icon(iconMarker)
                         );


                     }
                     mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {

                         @Override
                         public View getInfoWindow(Marker arg0) {
                             return null;
                         }

                         @Override
                         public View getInfoContents(Marker marker) {
                             Context mContext = getContext();
                             LinearLayout info = new LinearLayout(mContext);
                             info.setOrientation(LinearLayout.VERTICAL);

                             TextView title = new TextView(mContext);
                             title.setTextColor(Color.BLACK);
                             title.setGravity(Gravity.CENTER);
                             title.setTypeface(null, Typeface.BOLD);
                             title.setText(marker.getTitle());

                             TextView snippet = new TextView(mContext);
                             snippet.setTextColor(Color.GRAY);
                             snippet.setText(marker.getSnippet());

                             info.addView(title);
                             info.addView(snippet);

                             return info;
                         }
                     });



                 } catch (JSONException e) {
                     e.printStackTrace();
                 }
             }
             @Override
             public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                 Log.d("Failure",errorResponse.toString()+" \n"+throwable.getMessage());
             }
         });
     }



    /*
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if (needsInit) {
            CameraUpdate center=
                    CameraUpdateFactory.newLatLng(new LatLng(40.76793169992044,
                            -73.98180484771729));
            CameraUpdate zoom=CameraUpdateFactory.zoomTo(15);

            mMap.moveCamera(center);
            mMap.animateCamera(zoom);
        }

        addMarker(mMap, 40.748963847316034, -73.96807193756104, 4,
               4);
        addMarker(mMap, 40.76866299974387, -73.98268461227417,
                1,1);
        addMarker(mMap, 40.765136435316755, -73.97989511489868,
                2,2);
        addMarker(mMap, 40.70686417491799, -74.01572942733765,
                3,3);
    }

    private void addMarker(GoogleMap map, double lat, double lon,
                           int title, int snippet) {
        map.addMarker(new MarkerOptions().position(new LatLng(lat, lon))
                .title("Test")
                .snippet("TestSnippet"));
    }
    */

}

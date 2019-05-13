package com.example.glocal;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentActivity;

import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class TabActivity extends FragmentActivity {
    private SectionsPageAdapter mSectionsPageAdapter;

    private Bundle mBundle;

    private ViewPager mViewPager;
    private TabLayout mTabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tab_activity);


        mBundle = new Bundle();
        getPlaces(50.2912992,3.7853924);
    }

    private void initView() {
        mSectionsPageAdapter = new SectionsPageAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        setupViewPager(mViewPager);

        mTabLayout = (TabLayout) findViewById(R.id.tabs);
        mTabLayout.setupWithViewPager(mViewPager);
    }

    private void setupViewPager(ViewPager viewPager) {
        SectionsPageAdapter adapter = new SectionsPageAdapter(getSupportFragmentManager());

        ListFragment listFragment = new ListFragment();
        MapFragment mapFragment = new MapFragment();

        listFragment.setArguments(mBundle);

        adapter.addFragment(listFragment, "LIST");
        adapter.addFragment(mapFragment, "MAP");
        viewPager.setAdapter(adapter);
    }

    public void getPlaces(final double lat, double lon){
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
                        String adresse = elt.getString("vicinity").replaceAll("<br/>", " ");
                        String category = elt.getJSONObject("category").getString("title");
                        String localisation = elt.getString("position").replaceAll("\\[|\\]", "");
                        double latitude = Double.parseDouble(localisation.split(",")[0]);
                        double longitude = Double.parseDouble(localisation.split(",")[1]);
                        String latStr = String.valueOf(latitude);
                        String longStr = String.valueOf(longitude);


                        mBundle.putStringArray("" + i, new String[]{nom, adresse, category, localisation, latStr, longStr});
                    }

                    mBundle.putInt("size", array.length());

                    initView();
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
}

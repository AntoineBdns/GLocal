package com.example.glocal;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


public class MainActivity extends Activity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mainactivity);

        Button map1 = (Button) this.findViewById(R.id.btnMap1);
        map1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, MapsActivity.class);
                MainActivity.this.startActivity(intent);
            }
        });

        Button map2 = (Button) this.findViewById(R.id.btnMap2);
        map2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, MapsActivity2.class);
                MainActivity.this.startActivity(intent);
            }
        });
    }

}
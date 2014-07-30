package com.ryan.keyboardscrambler;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

//TODO: Look up how to make menu float in or look like it's floating

public class HomeScreen extends Activity {

    private TextView startTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_screen);

        final String[] theWords = getIntent().getExtras().getStringArray("words");

        startTV = (TextView) findViewById(R.id.startTV);

        startTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Intent toMedium = new Intent(HomeScreen.this, Medium_Level.class);
                toMedium.putExtra("words", theWords);
                startActivity(toMedium);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home_screen, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}

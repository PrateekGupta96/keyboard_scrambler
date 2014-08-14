package com.ryan.keyboardscrambler;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import com.ryan.keyboardscrambler.R;

public class Dvorak_Level extends KeyboardLevel {

    private static final String theStr = "1234567890" + DELETE_CHAR + "\",.pyfgcrl?=\\aoeuidhtns:qjkxbmwvz";
    private static final String punctuation = "\",.?=\\:";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dvorak__level);




    }

    protected String getLevelWord() {
        return super.getRandomWord();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.dvorak__level, menu);
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

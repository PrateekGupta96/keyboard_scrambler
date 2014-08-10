package com.ryan.keyboardscrambler;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;
import android.widget.PopupMenu.OnMenuItemClickListener;
import android.widget.TextView;
import android.view.View.OnClickListener;
import android.app.AlertDialog;
import android.app.AlertDialog;
import android.content.Context;

//TODO: Look up how to make menu float in or look like it's floating

public class HomeScreen extends Activity {

    private TextView startTV, howToPlayTV, scoresTV, rateOnPlayTV;
    private String[] theWords;
    private PopupMenu thePopup;
    private Context theC;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_screen);
        getActionBar().setIcon(new ColorDrawable(getResources().getColor(android.R.color.transparent)));

        theWords = getIntent().getExtras().getStringArray("words");
        theC = getApplicationContext();

        startTV = (TextView) findViewById(R.id.startTV);
        howToPlayTV = (TextView) findViewById(R.id.howToPlayTV);
        scoresTV = (TextView) findViewById(R.id.scoresTV);
        rateOnPlayTV = (TextView) findViewById(R.id.rateTV);

        thePopup = new PopupMenu(HomeScreen.this, findViewById(R.id.locationForChooseGameMenu));

        thePopup.getMenuInflater().inflate(R.menu.choose_level, thePopup.getMenu());
        thePopup.setOnMenuItemClickListener(new PopupMenuListenerChooseLevel());

        startTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                thePopup.show();
            }
        });
        howToPlayTV.setOnClickListener(howToPlayListener);
        scoresTV.setOnClickListener(scoresListener);
        rateOnPlayTV.setOnClickListener(rateListener);
    }

    private final OnClickListener howToPlayListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            final AlertDialog.Builder instructions = new AlertDialog.Builder(theC);

            instructions.setTitle("How to Play");
            instructions.setMessage("LOL");
            instructions.create().show();
        }
    };

    private final OnClickListener scoresListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            final AlertDialog.Builder scores = new AlertDialog.Builder(theC);
        }
    };

    private final OnClickListener rateListener = new OnClickListener() {
        @Override
        public void onClick(View v) {

        }
    };

    public class PopupMenuListenerChooseLevel implements OnMenuItemClickListener {
        @Override
        public boolean onMenuItemClick(MenuItem item) {
            Intent theIntent;
            switch (item.getItemId()) {
                case R.id.easyLevelItem:
                    theIntent = new Intent(HomeScreen.this, Easy_Level.class);
                    break;
                case R.id.mediumLevelItem :
                    theIntent = new Intent(HomeScreen.this, Medium_Level.class);
                    break;
                case R.id.hardLevelItem:
                    theIntent = new Intent(HomeScreen.this, HardLevel.class);
                    break;
                default:
                    return false;
            }
            theIntent.putExtra("words", theWords);
            startActivity(theIntent);
            return true;
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        Intent theIntent;
        switch (item.getItemId()) {
            case R.id.easyLevelItem:
                theIntent = new Intent(HomeScreen.this, Easy_Level.class);
                break;
            case R.id.mediumLevelItem :
                theIntent = new Intent(HomeScreen.this, Medium_Level.class);
                break;
            case R.id.hardLevelItem:
                theIntent = new Intent(HomeScreen.this, HardLevel.class);
                break;
            default:
                return super.onContextItemSelected(item);
        }
        theIntent.putExtra("words", theWords);
        startActivity(theIntent);
        return true;
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
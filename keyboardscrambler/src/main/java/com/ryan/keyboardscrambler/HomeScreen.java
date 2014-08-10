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
import android.content.SharedPreferences;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.content.ActivityNotFoundException;

//TODO: Look up how to make menu float in or look like it's floating

public class HomeScreen extends Activity {

    private TextView startTV, howToPlayTV, scoresTV, rateOnPlayTV;
    private String[] theWords;
    private PopupMenu thePopup;
    private Context theC;

    protected static final String TAG_EASY = "EASY_LEVEL_BEST";
    protected static final String TAG_MEDIUM = "MEDIUM_LEVEL_BEST";
    protected static final String TAG_DIFFICULT = "HARD_LEVEL_BEST";
    protected static final String TAG_HIGH_SCORE = "HIGH_SCORES";
    protected SharedPreferences highScores;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_screen);
        getActionBar().setIcon(new ColorDrawable(getResources().getColor(android.R.color.transparent)));

        theWords = getIntent().getExtras().getStringArray("words");
        theC = getApplicationContext();
        highScores = theC.getSharedPreferences(TAG_HIGH_SCORE, 0);

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
            final AlertDialog.Builder instructions = new AlertDialog.Builder(HomeScreen.this);

            final String howToPlay = "Click on 'Start' and choose a Level. " +
                    "Once you've chosen a level, you'll see a '00:00:00' in red at the top " +
                    "right of the screen and words in light blue below that. " +
                    "You'll also see a 'keyboard' at the bottom of the screen. " +
                    "\n\nThe point of the game is to try to type the words on the screen as quickly " +
                    "as possible using the keyboard at the bottom of the screen.\n" +
                    "The '<' character is for deleting the last letter typed.\n";

            instructions.setTitle("How to Play");
            instructions.setMessage(howToPlay);
            instructions.setPositiveButton("Sounds like fun, let me try it!", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                }
            });
            instructions.show();
        }
    };

    private final OnClickListener scoresListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            final AlertDialog.Builder highScores = new AlertDialog.Builder(HomeScreen.this);
            highScores.setMessage("High Scores");

            final String scores = "Easy Level:\t" + getHighScore(Level.EASY) + "\n\n" +
                    "Medium Level:\t" + getHighScore(Level.MEDIUM) + "\n\n" +
                    "Pro. Level:\t" + getHighScore(Level.DIFFICULT) + "\n";

            highScores.setPositiveButton("Hmm. Let me try to improve", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });

            highScores.setMessage(scores);
            highScores.show();
        }
    };

    private final OnClickListener rateListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            final Uri uri = Uri.parse("market://details?id=" + theC.getPackageName());
            final Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
            try {
                startActivity(goToMarket);
            } catch (ActivityNotFoundException e) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=" +
                        theC.getPackageName())));
            }
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

    public String getHighScore(final Level theLevel) {
        switch(theLevel) {
            case EASY:
                return highScores.getString(TAG_EASY, "Never finished");
            case MEDIUM:
                return highScores.getString(TAG_MEDIUM, "Never finished");
            case DIFFICULT:
                return highScores.getString(TAG_DIFFICULT, "Never finished");
            default:
                return "0";
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home_screen, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
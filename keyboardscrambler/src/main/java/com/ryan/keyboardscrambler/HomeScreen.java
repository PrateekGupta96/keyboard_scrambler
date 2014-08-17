package com.ryan.keyboardscrambler;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.PopupMenu.OnMenuItemClickListener;
import android.widget.TextView;

import java.text.DecimalFormat;

public class HomeScreen extends Activity {

    private static final String TAG_EASY_SCORE = "EASY_LEVEL_BEST_SCORE";
    private static final String TAG_EASY_LPS = "EASY_LEVEL_BEST_LPS";
    private static final String TAG_MEDIUM_SCORE = "MEDIUM_LEVEL_BEST_SCORE";
    private static final String TAG_MEDIUM_LPS = "MEDIUM_LEVEL_LPS";
    private static final String TAG_DIFFICULT_SCORE = "HARD_LEVEL_BEST_SCORE";
    private static final String TAG_DIFFICULT_LPS = "HARD_LEVEL_BEST_LPS";
    private static final String TAG_DVORAK_SCORE = "DVORAK_LEVEL_BEST_SCORE";
    private static final String TAG_DVORAK_LPS = "DVORAK_LEVEL_LPS";
    private static final String TAG_HIGH_SCORE = "HIGH_SCORES";

    private static final DecimalFormat theFormat = new DecimalFormat("##0.000");

    private String[] theWords;

    private Context theC;
    private SharedPreferences highScores;
    private TextView startTV, howToPlayTV, scoresTV, rateOnPlayTV;
    private PopupMenu thePopup;

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
            highScores.setTitle("High Scores");

            final LinearLayout theLayout = new LinearLayout(theC);
            theLayout.setOrientation(LinearLayout.VERTICAL);

            final LinearLayout theTitles = getLayout();
            theTitles.addView(getTV("         ", Gravity.LEFT));
            theTitles.addView(getTV("Score", Gravity.CENTER));
            theTitles.addView(getTV("LPS", Gravity.CENTER));

            final LinearLayout easy = getLayout();
            easy.addView(getTV("Easy      ", Gravity.LEFT));
            easy.addView(getTV(getValue(TAG_EASY_SCORE), Gravity.CENTER));
            easy.addView(getTV(getValue(TAG_EASY_LPS), Gravity.CENTER));

            final LinearLayout medium = getLayout();
            medium.addView(getTV("Medium", Gravity.LEFT));
            medium.addView(getTV(getValue(TAG_MEDIUM_SCORE), Gravity.CENTER));
            medium.addView(getTV(getValue(TAG_MEDIUM_LPS), Gravity.CENTER));

            final LinearLayout pro = getLayout();
            pro.addView(getTV("Pro        ", Gravity.LEFT));
            pro.addView(getTV(getValue(TAG_DIFFICULT_SCORE), Gravity.CENTER));
            pro.addView(getTV(getValue(TAG_DIFFICULT_LPS), Gravity.CENTER));

            final LinearLayout dvorak = getLayout();
            dvorak.addView(getTV("Dvorak  ", Gravity.LEFT));
            dvorak.addView(getTV(getValue(TAG_DVORAK_SCORE), Gravity.CENTER));
            dvorak.addView(getTV(getValue(TAG_DVORAK_LPS), Gravity.CENTER));

            theLayout.addView(theTitles);
            theLayout.addView(easy);
            theLayout.addView(medium);
            theLayout.addView(pro);
            theLayout.addView(dvorak);

            theLayout.addView(getAsterixTV("LPS = Letters per second"));
            theLayout.addView(getAsterixTV("N/F = Not Finished"));
            theLayout.addView(getLayout());

            highScores.setView(theLayout);

            highScores.setPositiveButton("Hmm. Let me try to improve", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });

            highScores.show();
        }
    };

    private TextView getAsterixTV(final String message) {
        final TextView theTV = new TextView(theC);
        theTV.setText(message);
        theTV.setTextSize(15);
        theTV.setGravity(Gravity.CENTER);
        theTV.setPadding(0, 80, 0, 0);
        theTV.setTextColor(Color.parseColor("#ffff4444"));
        return theTV;
    }

    private LinearLayout getLayout() {
        final LinearLayout theLayout = new LinearLayout(theC);
        theLayout.setOrientation(LinearLayout.HORIZONTAL);
        theLayout.setWeightSum(1.0f);
        theLayout.setPadding(15, 40, 0, 0);
        return theLayout;
    }

    private final LinearLayout.LayoutParams tvParams = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT,
            LayoutParams.WRAP_CONTENT, 0.33f);

    private TextView getTV(final String theText, final int theGravity) {
        final TextView theTV = new TextView(theC);
        theTV.setText(theText);
        theTV.setTextColor(Color.BLACK);
        theTV.setTextSize(20);
        theTV.setGravity(theGravity);
        theTV.setLayoutParams(tvParams);
        return theTV;
    }

    private final OnClickListener rateListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            final Uri uri = Uri.parse("market://details?id=" + theC.getPackageName());
            final Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
            try {
                startActivity(goToMarket);
            }
            catch (ActivityNotFoundException e) {
                startActivity(new Intent(Intent.ACTION_VIEW,
                        Uri.parse("http://play.google.com/store/apps/details?id=" +
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
                case R.id.dvorakLevelItem:
                    theIntent = new Intent(HomeScreen.this, Dvorak_Level.class);
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
            case R.id.dvorakLevelItem:
                theIntent = new Intent(HomeScreen.this, Dvorak_Level.class);
                break;
            default:
                return super.onContextItemSelected(item);
        }
        theIntent.putExtra("words", theWords);
        startActivity(theIntent);
        return true;
    }

    private String getValue(final String TAG) {
        try {
            final String value = highScores.getString(TAG, "N/F");
            if(value.equals("N/F")) {
                return "N/F";
            }
            try {
                return theFormat.format(Double.parseDouble(value));
            }
            catch(Exception e) {
                return value;
            }
        }
        catch(ClassCastException e) {
            final int theScore = highScores.getInt(TAG, 0);
            if (theScore == 0) {
                return "N/F";
            }
            return String.valueOf(theScore);
        }
    }

    public String getHighScore(final Level theLevel) {
        switch(theLevel) {
            case EASY:
                return getValue(TAG_EASY_SCORE) + "\t" + getValue(TAG_EASY_LPS);
            case MEDIUM:
                return getValue(TAG_MEDIUM_SCORE) + "\t" + getValue(TAG_MEDIUM_LPS);
            case DIFFICULT:
                return getValue(TAG_DIFFICULT_SCORE) + "\t" + getValue(TAG_DIFFICULT_LPS);
            case DVORAK:
                return getValue(TAG_DVORAK_SCORE) + "\t" + getValue(TAG_DVORAK_LPS);
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

    public void log(final String TAG) {
        Log.e("com.ryan.keyboardscrambler", TAG);
    }
}
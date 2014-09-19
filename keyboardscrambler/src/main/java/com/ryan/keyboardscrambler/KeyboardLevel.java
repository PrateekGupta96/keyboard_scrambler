package com.ryan.keyboardscrambler;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
/**
 * A level of the game
 */

public abstract class KeyboardLevel extends Activity {

    protected static final String fileName = "keyboard_scrambler_words.txt";
    protected static final String DELETE_CHAR = "<";
    protected static final String theStr = "abcdefghijklmnopqrstuvwxyz 1234567890" + DELETE_CHAR;

    protected static final String TAG_EASY_SCORE = "EASY_LEVEL_BEST_SCORE";
    protected static final String TAG_EASY_LPS = "EASY_LEVEL_BEST_LPS";
    protected static final String TAG_MEDIUM_SCORE = "MEDIUM_LEVEL_BEST_SCORE";
    protected static final String TAG_MEDIUM_LPS = "MEDIUM_LEVEL_LPS";
    protected static final String TAG_DIFFICULT_SCORE = "HARD_LEVEL_BEST_SCORE";
    protected static final String TAG_DIFFICULT_LPS = "HARD_LEVEL_BEST_LPS";
    protected static final String TAG_HIGH_SCORE = "HIGH_SCORES";

    protected static final DecimalFormat theFormat = new DecimalFormat("##0.000");

    protected static final NumberFormat theTwoF = NumberFormat.getInstance();
    protected static final Random theGenerator = new Random();

    protected static final LinearLayout.LayoutParams tvParams =  new LinearLayout.LayoutParams(
            LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, 0.33f);

    protected Context theC;
    protected SharedPreferences highScores;
    protected Level LEVEL;

    protected static final byte NUM_WORDS = 2; //SHOULD BE 5
    protected byte onWord = 0; //Word user is on

    private final Handler theHandler = new Handler();
    protected LinearLayout firstRow, secondRow, thirdRow, fourthRow;
    protected TextView userResponse;
    protected TextView refresh;
    protected TextView timeTV;

    protected String[] theWords;
    protected int width, height, adjustedHeight;
    protected short totalNumChars;
    protected long startTime, elapsed;
    protected boolean hasStarted = false;

    private UpdateTimeTV theUpdater;
    private int secs, minutes, seconds, milliseconds;

    protected abstract String getLevelWord();

    protected void onCreate(Bundle savedInstanceState, String[] theWords) {
        super.onCreate(savedInstanceState);
        this.theC = getApplicationContext();
        this.theWords = theWords;
        setDimensions();
        getActionBar().setIcon(new ColorDrawable(getResources().getColor(android.R.color.transparent)));

        theC = getApplicationContext();
        highScores = getApplicationContext().getSharedPreferences(TAG_HIGH_SCORE, 0);
    }

    /**
     * Returns a scrambled version of a string
     */
    protected static String scrambleString(final String theString) {
        List<Character> theChars = new ArrayList<Character>(theString.length());
        for (short i = 0; i < theString.length(); i++)
            theChars.add(theString.charAt(i));

        String theResult = "";

        while (theChars.size() != 0) {
            theResult += theChars.remove((int) (Math.random() * theChars.size()));
        }

        return theResult;
    }

    protected double getLPS() {
        return (((double)secs) / ((double)totalNumChars));
    }

    protected int getScore(final Level theLevel) {
        final double secPerChar = getLPS();
        final int scoreSoFar = 3000 - (int) (secPerChar * 100);

        switch(theLevel) {
            case EASY:
                return scoreSoFar;
            case MEDIUM:
                return scoreSoFar + 150;
            case DIFFICULT:
                return scoreSoFar + 350;
            default:
                return scoreSoFar;
        }
    }

    protected void setUpTimer(final TextView theTimeTV) {
        theUpdater = new UpdateTimeTV(theTimeTV);
    }

    protected void startTimer() {
        theHandler.postDelayed(theUpdater, 0);
        startTime = System.currentTimeMillis();
    }

    protected void stopTimer() {
        theHandler.removeCallbacks(theUpdater);
    }

    protected String getElapsed() {
        return "Min: " + minutes + " Secs: " + seconds + " MS: " + milliseconds;
    }

    /** Intializes dimension variables */
    private void setDimensions()  {
        final Display display = getWindowManager().getDefaultDisplay();
        final Point size = new Point();
        display.getSize(size);
        width = size.x;
        height = size.y;
        adjustedHeight = (int) (0.25 * (height * 0.4));
    }

    /** Returns TextView with parameter character --> Used as keys */
    protected TextView getCharTV(final char theChar, final int numInRow)    {
        final TextView theV = new TextView(theC);
        theV.setMaxWidth(width / numInRow);
        theV.setMinWidth(width / numInRow);
        theV.setMaxHeight(adjustedHeight);
        theV.setMinimumHeight(adjustedHeight);
        theV.setText(String.valueOf(theChar));
        theV.setTextSize(30);
        theV.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL);
        theV.setTextColor(Color.BLACK);
        theV.setBackgroundResource(R.drawable.keycharacter_border);
        theV.setOnClickListener(KeyPressedListener);
        return theV;
    }

    private final OnClickListener KeyPressedListener = new android.view.View.OnClickListener() {
        @Override
        public void onClick(View v) {
            final TextView theV = (TextView) v;

            if (!hasStarted) {
                hasStarted = true;
                startTimer();
            }

            String theR = userResponse.getText().toString();

            if(theV.getText().toString().equals(DELETE_CHAR))  {
                if(theR.length() == 0) {
                    return;
                }
                userResponse.setText(theR.substring(0, theR.length()-1));
                return;
            }

            else {
                theR += theV.getText().toString();
            }

            userResponse.setText(theR);

            if(theR.equals(refresh.getText().toString()))   {
                userFinished();
                return;
            }
        }
    };

    /** Returns random word */
    protected String getRandomWord() {
        final String theWord =  theWords[theGenerator.nextInt(theWords.length)].replace(" ", "");

        if(theWord.length() < 4) {
            return getRandomWord();
        }
        return theWord;
    }

    protected void userFinished() {
        totalNumChars += refresh.getText().toString().length();
        onWord++;

        if(onWord >= NUM_WORDS) {
            stopTimer();
            makeToast("You finished! " + getElapsed());

            final LevelScore theScore = new LevelScore(LEVEL, getLPS(), getScore(LEVEL));
            showScoreDialog(theScore);
            setHighScore(theScore);
            log("Score\t" + theScore.getClass() + "LPS:\t" + theScore.getLettersPerSecond());
            return;
        }

        int more = NUM_WORDS - onWord;

        refresh.setText(getLevelWord());
        userResponse.setText("");
        if(more == 1) {
            makeToast("Last word!");
        }
        else {
            makeToast(more + " more words!");
        }
    }

    private void showScoreDialog(final LevelScore theScore) {
        AlertDialog.Builder theAlert = new AlertDialog.Builder(KeyboardLevel.this);

        if(isHighScore(theScore)) {
            theAlert.setTitle("New High Score for " + theScore.getLevel().toString());
        }
        else {
            theAlert.setTitle(theScore.getLevel().toString());
        }

        final Level theLevel = theScore.getLevel();

        final LinearLayout theLayout = new LinearLayout(theC);
        theLayout.setOrientation(LinearLayout.VERTICAL);

        final LinearLayout theTitles = getLayout();
        theTitles.addView(getTV("          ", Gravity.LEFT));
        theTitles.addView(getTV("Score", Gravity.CENTER));
        theTitles.addView(getTV("LPS", Gravity.CENTER));

        final LinearLayout highScore = getLayout();
        highScore.addView(getTV("High Score", Gravity.LEFT));
        highScore.addView(getTV(getValue(getLevelTagScore(theLevel)), Gravity.CENTER));
        highScore.addView(getTV((getValue(getLevelTagLPS(theLevel))), Gravity.CENTER));

        final LinearLayout yourScore = getLayout();
        yourScore.addView(getTV("Your Score", Gravity.LEFT));
        yourScore.addView(getTV(String.valueOf(theScore.getScore()), Gravity.CENTER));
        yourScore.addView(getTV(theFormat.format(theScore.getLettersPerSecond()), Gravity.CENTER));

        final LinearLayout differenceScore = getDifferenceScoreLayout(theScore);

        theLayout.addView(theTitles);
        theLayout.addView(highScore);
        theLayout.addView(yourScore);
        theLayout.addView(differenceScore);

        theLayout.addView(getAsterixTV("LPS = Letters per second"));
        theLayout.addView(getAsterixTV("N/F = Not Finished"));
        theLayout.addView(getLayout());

        theAlert.setView(theLayout);

        theAlert.setPositiveButton("Hmm. Let me try to improve", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        theAlert.show();
    }

    private String getLevelTagLPS(final Level theLevel) {
        switch (theLevel) {
            case EASY:
                return TAG_EASY_LPS;
            case MEDIUM:
                return TAG_MEDIUM_LPS;
            case DIFFICULT:
                return TAG_DIFFICULT_LPS;
            default:
                return "";
        }
    }

    private String getLevelTagScore(final Level theLevel) {
        switch (theLevel) {
            case EASY:
                return TAG_EASY_SCORE;
            case MEDIUM:
                return TAG_MEDIUM_SCORE;
            case DIFFICULT:
                return TAG_DIFFICULT_SCORE;
            default:
                return "";
        }
    }

    private LinearLayout getDifferenceScoreLayout(final LevelScore theScore) {
        final LinearLayout theLayout = getLayout();

        theLayout.addView(getTV("Difference", Gravity.LEFT));

        String scoreString = getValue(getLevelTagScore(theScore.getLevel()));
        String lpsString = getValue(getLevelTagLPS(theScore.getLevel()));

        if(scoreString.contains("N/F") || scoreString.contains("0")) {
            scoreString = "0";
            lpsString = String.valueOf(Double.MAX_VALUE);
        }

        final int scoreDiff = theScore.getScore() - Integer.parseInt(scoreString);
        final TextView scoreV = getTV(scoreString, Gravity.CENTER);

        if(scoreDiff < 0) {
            scoreV.setTextColor(Color.parseColor("#ffff4444"));
        }
        else {
            scoreV.setTextColor(Color.parseColor("#ff99cc00"));
        }

        final TextView lpsV;
        final double lpsDiff;

        if(Double.parseDouble(lpsString) == Double.MAX_VALUE) {
            lpsDiff = theScore.getLettersPerSecond();
            lpsV = getTV(theFormat.format(lpsDiff), Gravity.CENTER);
            lpsV.setTextColor(Color.parseColor("#ff99cc00"));
        }
        else {
            lpsDiff = theScore.getLettersPerSecond() - Double.parseDouble(lpsString);
            lpsV = getTV(theFormat.format(lpsDiff), Gravity.CENTER);

            if(lpsDiff > 0) {
                lpsV.setTextColor(Color.parseColor("#ffff4444"));
            }
            else {
                lpsV.setTextColor(Color.parseColor("#ff99cc00"));
            }
        }

        theLayout.addView(scoreV);
        theLayout.addView(lpsV);
        return theLayout;
    }

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

    private TextView getTV(final String theText, final int theGravity) {
        final TextView theTV = new TextView(theC);
        theTV.setText(theText);
        theTV.setTextColor(Color.BLACK);
        theTV.setTextSize(20);
        theTV.setGravity(theGravity);
        theTV.setLayoutParams(tvParams);
        return theTV;
    }

    private class UpdateTimeTV implements Runnable {
        private TextView timeView;

        public UpdateTimeTV(final TextView timeView) {
            this.timeView = timeView;
            this.timeView.setTextColor(Color.parseColor("#ffcc0000"));
        }

        @Override
        public void run() {
            elapsed = System.currentTimeMillis() - startTime;

            secs = (int) (elapsed / 1000);
            minutes = secs / 60;
            seconds = secs % 60;
            milliseconds = (int) (elapsed % 1000);

            timeView.setText(String.format("%02d", minutes) + ":" + String.format("%02d", seconds) + ":" +
                    String.format("%03d", milliseconds));

            theHandler.postDelayed(this, 0);
        }
    }

    public boolean isHighScore(final LevelScore theScore) {
        return (theScore.getScore() < getInt(getLevelTagScore(theScore.getLevel())));
    }

    public void setHighScore(final LevelScore theScore) {
        if(!isHighScore(theScore)) {
            return;
        }

        final Editor newScore = highScores.edit();

        newScore.putString(getLevelTagScore(theScore.getLevel()), String.valueOf(theScore.getLettersPerSecond()));
        newScore.putInt(getLevelTagLPS(theScore.getLevel()), theScore.getScore());

        newScore.commit();
    }

    private String getValue(final String TAG) {
        try {
            final String high = highScores.getString(TAG, "0");
            if(high.contains("N/F") || high.contains("0")) {
                return high;
            }
            else {
                try {
                    return theFormat.format(Double.parseDouble(high));
                } catch (Exception e) {
                    return high;
                }
            }
        }
        catch (Exception e) {
            return String.valueOf(highScores.getInt(TAG, 0));
        }
    }
    private double getDouble(final String TAG) {
        return Double.parseDouble(getValue(TAG));
    }
    private int getInt(final String TAG) {
        return highScores.getInt(TAG, 0);
    }

    public LevelScore getHighScore(final Level theLevel) {
        return new LevelScore(theLevel,
                getDouble(getLevelTagLPS(theLevel)),
                getInt(getLevelTagScore(theLevel)));
    }

    @Override
    public void onResume() {
        //theHandler.postDelayed(theUpdater, 0);
        super.onResume();
    }

    @Override
    public void onPause() {
        //theHandler.removeCallbacks(theUpdater);
        super.onPause();
    }

    protected long getElapsedMilliseconds() {
        return elapsed;
    }

    protected long getElapsedSeconds() {
        return secs;
    }

    protected int getElapsedMinutes() {
        return minutes;
    }

    protected void log(final String message) {
        Log.e("com.ryan.keyboardscrambler", message);
    }

    protected void log(final int num) {
        log(String.valueOf(num));
    }

    protected void makeToast(final String message) {
        final Toast theToast= Toast.makeText(theC, message, Toast.LENGTH_SHORT);
        theToast.setGravity(android.view.Gravity.CENTER, 0, 0);
        theToast.show();
    }

    protected String[] getWords() {
        return this.theWords;
    }

    public void setWordChoice(final String[] theWords) {
        this.theWords = theWords;
    }
}

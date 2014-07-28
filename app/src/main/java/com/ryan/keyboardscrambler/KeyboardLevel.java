package com.ryan.keyboardscrambler;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.graphics.Color;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * A level of the game
 */

public abstract class KeyboardLevel extends Activity {

    protected static final String fileName = "keyboard_scrambler_words.txt";
    protected static final Random theGenerator = new Random();
    protected static final String DELETE_CHAR = "<";
    protected static final String theStr = "abcdefghijklmnopqrstuvwxyz 1234567890" + DELETE_CHAR;
    protected static final NumberFormat theTwoF = NumberFormat.getInstance();
    protected final Context theC = this;

    protected byte NUM_WORDS = 5;
    protected byte onWord; //Word user is on

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

    protected void onCreate(Bundle savedInstanceState, String[] theWords) {
        super.onCreate(savedInstanceState);
        this.theWords = theWords;
        setDimensions();
    }

    /**
     * Returns a scrambled version of a string
     */
    protected static String scrambleString(final String theString) {
        List<Character> theChars = new ArrayList<Character>(theString.length());
        for (short i = 0; i < theString.length(); i++)
            theChars.add(theString.charAt(i));

        String theResult = "";

        while (theChars.size() != 0)
            theResult += theChars.remove((int) (Math.random() * theChars.size()));

        return theResult;
    }

    protected int getScore() {
        double secPerChar = secs / totalNumChars;
        return 1000 - (int) (secPerChar * 10);
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
        Point size = new Point();
        display.getSize(size);
        width = size.x;
        height = size.y;
        adjustedHeight = (int) (0.25 * (height * 0.4));
    }

    /** Returns TextView with parameter character --> Used as keys */
    protected View getCharTV(final char theChar, final int numInRow)    {
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
        theV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!hasStarted) {
                    hasStarted = true;
                    startTimer();
                }

                String theR = userResponse.getText().toString();

                if(theV.getText().toString().equals(DELETE_CHAR))  {
                    if(theR.length() == 0)
                        return;
                    userResponse.setText(theR.substring(0, theR.length()-1));
                    return;
                }

                else
                    theR += theV.getText().toString();

                userResponse.setText(theR);

                if(theR.equals(refresh.getText().toString()))   {
                    userFinished();
                    return;
                }
            }
        });
        return theV;
    }

    protected abstract void userFinished();

    /** Returns random word */
    protected String getRandomWord() {
        String theWord =  theWords[theGenerator.nextInt(theWords.length)].replace(" ", "");

        if(theWord.length() < 4)
            return getRandomWord();
        return theWord;
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

            String.format("%03d", secs);
            timeView.setText(String.format("%02d", minutes) + ":" + String.format("%02d", secs) + ":" +
                    String.format("%03d", milliseconds));

            theHandler.postDelayed(this, 0);
        }
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
        Toast.makeText(theC, message, Toast.LENGTH_LONG).show();
    }

    protected String[] getWords() {
        return this.theWords;
    }

    public void setWordChoice(final String[] theWords) {
        this.theWords = theWords;
    }
}

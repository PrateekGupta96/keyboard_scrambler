package com.ryan.keyboardscrambler;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Point;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
/**
 * A level of the game
 */

public abstract class KeyboardLevel extends Activity {
    protected final Context theC = this;
    protected int width, height, adjustedHeight;

    protected static final String fileName = "keyboard_scrambler_words.txt";
    protected static final Random theGenerator = new Random();
    protected String[] theWords;


    protected LinearLayout firstRow, secondRow, thirdRow, fourthRow;
    protected TextView userResponse;
    protected TextView refresh;

    protected static final String DELETE_CHAR = "<";

    protected static final String theStr = "abcdefghijklmnopqrstuvwxyz 1234567890" + DELETE_CHAR;

    /** Intializes dimension variables */
    protected void setDimensions()  {
        final Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        width = size.x;
        height = size.y;
        adjustedHeight = (int) (0.25 * (height * 0.4));

        theWords = getKeyboardWords();
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
        if(theWords == null)
            theWords = getKeyboardWords();
        String theWord =  theWords[theGenerator.nextInt(theWords.length)].replace(" ", "");

        if(theWord.length() < 4)
            return getRandomWord();
        return theWord;
    }

    /** Returns a scrambled version of a string */
    protected static String scrambleString(final String theString)  {
        List<Character> theChars = new ArrayList<Character>(theString.length());
        for(int i = 0; i < theString.length(); i++)
            theChars.add(theString.charAt(i));

        String theResult = "";

        while(theChars.size() != 0)
            theResult += theChars.remove((int)(Math.random()*theChars.size()));

        return theResult;
    }


    /** Returns array of stored words*/
    protected String[] getKeyboardWords()   {
        try {
            String theInput = "";

            InputStreamReader theISR =
                    new InputStreamReader(theC.getResources().openRawResource(R.raw.keyboard_scrambler_words));
            BufferedReader theReader = new BufferedReader(theISR);

            while(theReader.ready())
                theInput += " " + theReader.readLine();

            return theInput.split(" ");
        }

        catch (Exception e) { e.printStackTrace(); return new String[]{e.toString()}; }
    }

    protected void log(final int num)   {
        log(String.valueOf(num));
    }

    protected void log(final String message)    {
        Log.e("com.ryan.keyboardscrambler", message);
    }

    protected void makeToast(final String message)  {
        Toast.makeText(theC, message, Toast.LENGTH_LONG).show();
    }
}

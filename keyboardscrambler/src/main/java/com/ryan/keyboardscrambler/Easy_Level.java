package com.ryan.keyboardscrambler;

import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
public class Easy_Level extends KeyboardLevel {

    private static final String theStr = "abcdefghijklmnopqrstuvwxyz " + DELETE_CHAR;

    @Override
    protected void onCreate(Bundle savedInstanceState)  {
        super.onCreate(savedInstanceState, getIntent().getExtras().getStringArray("words"));
        setContentView(R.layout.medium_level);

        this.LEVEL = Level.EASY;

        //Initialize the rows that will hold the character keys
        firstRow = (LinearLayout) findViewById(R.id.firstRow);
        secondRow = (LinearLayout) findViewById(R.id.secondRow);
        thirdRow = (LinearLayout) findViewById(R.id.thirdRow);

        //Shows characters user pressed
        userResponse = (TextView) findViewById(R.id.userResponseTV);
        userResponse.setCursorVisible(true);

        timeTV = (TextView) findViewById(R.id.timeTV);
        setUpTimer(timeTV);

        //Scrambled version of the str
        final String reArranged = scrambleString(this.theStr);
        int counter = 0;

        TextView view;
        //Add the keys to the screen
        for(byte i = 0; i < 10; i++, counter++) {
            view = getCharTV(reArranged.charAt(counter), 10);
            view.setBackgroundResource(com.ryan.keyboardscrambler.R.drawable.circular_button_selector);
            view.setTextColor(getResources().getColor(com.ryan.keyboardscrambler.R.color.button_text_selector));
            view.setOnClickListener(new android.view.View.OnClickListener() {
                @Override
                public void onClick(android.view.View v) {
                    if(v.isSelected()) {
                        v.setSelected(false);
                    }
                    else {
                        v.setSelected(true);
                    }
                }
            });
            firstRow.addView(view);
            //new RippleView(this, view);
        }
        for(byte i = 0; i < 9; i++, counter++) {
            view = getCharTV(reArranged.charAt(counter), 9);
            secondRow.addView(view);
            //new RippleView(this, view);
        }
        for(byte i = 0; i < 9; i++, counter++) {
            view = getCharTV(reArranged.charAt(counter), 9);
            thirdRow.addView(view);
            //new RippleView(this, view);
        }

        //Refresh button --> DEV PURPOSES
        refresh = (TextView) findViewById(R.id.refresh);
        refresh.setText(getLevelWord());
        refresh.setTextColor(Color.parseColor("#ff0099cc"));
        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recreate();
            }
        });
    }


    protected String getLevelWord() {
        return super.getRandomWord();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
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

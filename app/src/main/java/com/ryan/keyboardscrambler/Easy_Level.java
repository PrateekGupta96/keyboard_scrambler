package com.ryan.keyboardscrambler;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.view.View;

public class Easy_Level extends KeyboardLevel {

    private static final String theStr = "abcdefghijklmnopqrstuvwxyz " + DELETE_CHAR;

    @Override
    protected void onCreate(Bundle savedInstanceState)  {
        super.onCreate(savedInstanceState);
        super.setWordChoice(getIntent().getExtras().getStringArray("words"));

        setContentView(R.layout.medium_level);

        //Initialize the rows that will hold the character keys
        firstRow = (LinearLayout) findViewById(R.id.firstRow);
        secondRow = (LinearLayout) findViewById(R.id.secondRow);
        thirdRow = (LinearLayout) findViewById(R.id.thirdRow);

        //Shows characters user pressed
        userResponse = (TextView) findViewById(R.id.userResponseTV);
        userResponse.setCursorVisible(true);

        timeTV = (TextView) findViewById(R.id.timeTV);
        setUpTimer(timeTV);

        //Initializes dimension variables
        setDimensions();

        //Scrambled version of the str
        final String reArranged = scrambleString(this.theStr);
        int counter = 0;

        //Add the keys to the screen
        for(byte i = 0; i < 10; i++, counter++)
            firstRow.addView(getCharTV(reArranged.charAt(counter), 10));
        for(byte i = 0; i < 9; i++, counter++)
            secondRow.addView(getCharTV(reArranged.charAt(counter), 9));
        for(byte i = 0; i < 9; i++, counter++)
            thirdRow.addView(getCharTV(reArranged.charAt(counter), 10));

        //Refresh button --> DEV PURPOSES
        refresh = (TextView) findViewById(R.id.refresh);

        refresh.setText(getEasyLevel(getRandomWord()));

        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recreate();
            }
        });
    }

    protected void userFinished() {
        totalNumChars += refresh.getText().toString().length();
        onWord++;
        if(onWord > 5) {
            stopTimer();
            makeToast("You finished! " + getElapsed());
            startActivity(new Intent(theC, HardLevel.class));
            return;
        }

        int more = NUM_WORDS - onWord;

        refresh.setText(getEasyLevel(getRandomWord()));
        if(more == 1)
            makeToast(more + " more word!");
        else
            makeToast(more + " more words!");
    }

    private String getEasyLevel(final String theWord) {
        return theWord;
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

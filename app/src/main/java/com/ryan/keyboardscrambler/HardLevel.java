package com.ryan.keyboardscrambler;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.content.Intent;
import android.view.View;

public class HardLevel extends Medium_Level {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState, getIntent().getExtras().getStringArray("words"));
        setContentView(R.layout.hard_level);
        getActionBar().setTitle("Keyboard Scrambler Level Hard");

        //Initialize the rows that will hold the character keys
        firstRow = (LinearLayout) findViewById(R.id.firstRow);
        secondRow = (LinearLayout) findViewById(R.id.secondRow);
        thirdRow = (LinearLayout) findViewById(R.id.thirdRow);
        fourthRow = (LinearLayout) findViewById(R.id.fourthRow);

        //Shows characters user pressed
        userResponse = (TextView) findViewById(R.id.userResponseTV);
        userResponse.setCursorVisible(true);

        //Scrambled version of the str
        final String reArranged = scrambleString(theStr);
        short counter = 0;

        byte i;
        //Add the keys to the screen
        for(i = 0; i < 10; i++, counter++)
            firstRow.addView(getCharTV(reArranged.charAt(counter), 10));
        for(i = 0; i < 9; i++, counter++)
            secondRow.addView(getCharTV(reArranged.charAt(counter), 9));
        for(i = 0; i < 9; i++, counter++)
            thirdRow.addView(getCharTV(reArranged.charAt(counter), 9));
        for(i = 0; i < 10; i++, counter++)
            fourthRow.addView(getCharTV(reArranged.charAt(counter), 10));

        //Refresh button --> DEV PURPOSES
        refresh = (TextView) findViewById(R.id.refresh);

        refresh.setText(getHardLevelWord());

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

        refresh.setText(getHardLevelWord());
        if(more == 1)
            makeToast(more + " more word!");
        else
            makeToast(more + " more words!");
    }


    private String getHardLevelWord() {
        String theWord = getRandomWord() + " " + getRandomWord() + " " + getRandomWord();
        String theNum = String.valueOf(theGenerator.nextInt(100) + 1);

        switch (theGenerator.nextInt(1))
        {
            case 0:
                return theNum + " " + theWord;
            case 1:
                return theWord + " " + theNum;
            default:
                return theWord;
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.hard_lavel, menu);
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

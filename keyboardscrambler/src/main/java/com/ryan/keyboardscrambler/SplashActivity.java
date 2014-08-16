package com.ryan.keyboardscrambler;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.text.DecimalFormat;
import java.util.Random;

/** Words:
 *  http://listofrandomwords.com/index.cfm?blist
 */

public class SplashActivity extends Activity {

    private final Context theC = this;
    private static final String fileName = "keyboard_scrambler_words.txt";
    private static final Random theRandom = new Random();
    private static final DecimalFormat theFormat = new DecimalFormat("00.00");

    private final int WORDS = 3185;
    private final short SIZE = 150;

    private long startTime;
    private final short MIN = 2; //Seconds

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        startTime = System.currentTimeMillis();

        new LoadWords().execute();
    }

    private class LoadWords extends AsyncTask<Void, Integer, String[]> {

        private final long startTime;
        private final TextView loadingTV;

        public LoadWords() {
            startTime = System.currentTimeMillis();
            loadingTV = (TextView) findViewById(R.id.loadingTV);
        }

        @Override
        protected String[] doInBackground(Void... params)  {
            InputStreamReader theISR = null;
            BufferedReader theReader = null;

            try {
                StringBuilder theInput = new StringBuilder("");
                int counter = 0;

                theISR = new InputStreamReader(
                        theC.getResources().openRawResource(R.raw.keyboard_scrambler_words));

                theReader = new BufferedReader(theISR);

                while(theReader.ready()) {
                    theInput.append(" " + theReader.readLine());
                    counter++;

                    if(counter % 50 == 0) {
                        final double theCounter = counter;
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                loadingTV.setText("Loading... " +
                                        theFormat.format((((theCounter / WORDS)) * 100.0)) + "%");
                            }
                        });
                    }
                }
                return theInput.toString().split(" ");
            }
            catch (Exception e) {
                e.printStackTrace();
                log(e.toString());
                return new String[]{e.toString()};
            }

            finally {
                try {
                    if (theISR != null)
                        theISR.close();
                    if (theReader != null)
                        theReader.close();
                }
                catch (Exception e) { e.printStackTrace(); }
            }
        }

        @Override
        protected void onPostExecute(final String[] words) {
            final long st = System.currentTimeMillis();


            final boolean[] wordsChosen = new boolean[words.length];
            final String[] theWords = new String[SIZE];

            for(short i = 0; i < SIZE; i++) {
                short randomNum = (short) theRandom.nextInt(words.length);

                //If word has not been chosen yet
                if(!wordsChosen[randomNum]) {
                    theWords[i] = words[randomNum];
                    wordsChosen[randomNum] = true;
                }
                //If word has been chosen, decrement i (like starting over)
                else {
                    i--;
                }
            }

            final Intent toHomeScreen = new Intent(SplashActivity.this, HomeScreen.class);
            toHomeScreen.putExtra("words", theWords);

            log("Array:\t" + (System.currentTimeMillis() - st));

            log("Total time:\t" + (System.currentTimeMillis() - startTime));

            startActivity(toHomeScreen);
            finish();
        }
    }

    public void log(final String message) {
        Log.e("com.ryan.keyboardscrambler", message);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.splash, menu);
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
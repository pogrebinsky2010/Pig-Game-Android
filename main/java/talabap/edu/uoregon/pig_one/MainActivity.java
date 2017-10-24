package talabap.edu.uoregon.pig_one;

import android.content.Intent;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.view.Menu;
import android.widget.Toast;
/**
 * Created by talaba on 6/23/16.
 * modified 6/24/16
 */

public class MainActivity extends AppCompatActivity implements OnEditorActionListener, SharedPreferences.OnSharedPreferenceChangeListener{
PigGame game = new PigGame();
private Toolbar toolbar;

    //creating instance variables
    TextView player_id_text_view;
    TextView points_for_turn_text_view;
    TextView player_one_score_text_view;
    TextView player_two_score_text_view;
    TextView win_label_text_view;
    ImageView die_img_view;
    EditText player_one_edit_text;
    EditText player_two_edit_text;
    Button roll_button;
    Button end_button;

    //define Shared Preferences object
    private SharedPreferences savedValues;

    //array for images
    int[] images = {R.mipmap.die_1, R.mipmap.die_2, R.mipmap.die_3, R.mipmap.die_4, R.mipmap.die_5, R.mipmap.die_6, R.mipmap.die_7, R.mipmap.roll_the_dice, R.mipmap.player_one_wins, R.mipmap.player_two_wins};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

         //setting instance references
        player_id_text_view = (TextView) findViewById(R.id.player_id_text_view);
        die_img_view = (ImageView)findViewById(R.id.die_img_view);
        points_for_turn_text_view = (TextView)findViewById(R.id.points_for_turn_text_view);
        player_one_edit_text = (EditText)findViewById(R.id.player_one_edit_text);
        player_two_edit_text = (EditText)findViewById(R.id.player_two_edit_text);
        player_one_score_text_view = (TextView) findViewById(R.id.player_one_score_text_view);
        player_two_score_text_view = (TextView) findViewById(R.id.player_two_score_text_view);
        win_label_text_view = (TextView)findViewById(R.id.win_label_text_view);
        roll_button = (Button)findViewById(R.id.roll_button);
        end_button = (Button)findViewById(R.id.end_button);


        //get SharedPreferences object
        savedValues = getSharedPreferences("savedValues", MODE_PRIVATE);

        //setting action listener for edit text
        player_one_edit_text.setOnEditorActionListener(this);
        player_two_edit_text.setOnEditorActionListener(this);

        //toolbar
        toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //get the preferences from settings
       loadPreferences();

    }

    //preferences from user changed settings in runtime
    public void loadPreferences()
    {
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String maxClickString = settings.getString("limit_clicks", "0");
        game.setMaxNumClicks(Integer.valueOf(Integer.valueOf(maxClickString)));
        game.sevenSides = settings.getBoolean("add_seventh_side", false);
        game.setComputerTurnedOn(settings.getBoolean("turn_on_AI", false));
        String winningScoreString = settings.getString("winning_score", "100");
        game.setWinningScore(Integer.valueOf(winningScoreString));
        settings.registerOnSharedPreferenceChangeListener(MainActivity.this);//this will update my values after the initial onCreate
    }

    //display menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_activity, menu);
        return true;
    }

    //define option items actions
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_settings:
                Intent settingsIntent = new Intent(this, SettingsActivity.class);
                startActivity(settingsIntent);
                return true;
            case R.id.menu_about:
                Toast.makeText(this, "I'm Talaba and this is my first app!", Toast.LENGTH_LONG).show();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }



            //store values on pause
    @Override
    public void onPause()
    {
        //saving instance variables
        Editor editor = savedValues.edit();
        editor.putInt("currentImgIndex", game.getCurrentImageIndex());
        editor.putInt("currentScore", game.getCurrentScore());
        editor.putString("currentPlayer", game.getCurrentPlayer());
        editor.putInt("playerOneScore", game.getPlayerOneScore());
        editor.putInt("playerTwoScore", game.getPlayerTwoScore());
        editor.putInt("countForWinner", game.count);
        editor.putInt("maxNumClicks", game.getMaxNumClicks());
        editor.putBoolean("maxClicksBool", game.isMaxClicks());

        editor.commit();
        super.onPause();
    }

    @Override
    public void onResume(){
        super.onResume();

        //get instance variables and set properties
        die_img_view.setImageResource(images[savedValues.getInt("currentImgIndex", 7)]);
        points_for_turn_text_view.setText(savedValues.getInt("currentScore", 0)+"");
        player_id_text_view.setText(savedValues.getString("currentPlayer","player1"));
        player_one_score_text_view.setText(savedValues.getInt("playerOneScore",0)+"");
        player_two_score_text_view.setText(savedValues.getInt("playerTwoScore",0)+"");
        game.count = savedValues.getInt("countForWinner", 0);

    }
    //roll button handler
    public void roll_die(View v) {
        game.addOneToCount();// this count adds one to the User click so we can limit on request
        if(game.getPlayerTurnCount() >= game.getMaxNumClicks() && game.isMaxClicks() == true)
        {
            game.rollDice();
            guiDisplay();
            game.resetCount();
            Toast.makeText(this, "Switching players, max turn count reached", Toast.LENGTH_LONG).show();
            end_button.performClick();
        }
        else
        {
            game.rollDice();
            guiDisplay();
             }
    }

    //end turn button handler
    public void end_turn(View v) {
            game.resetCount();
            game.endTurn();
            guiDisplay();

        if(game.getComputerTurn() == true && game.isComputerTurnedOn()== true)
        {
            game.resetCount();
            computerRoll();
        }

    }

    //AI turn
    //made it so the computer always rolls 3 times (unless rolls 1)
    public void computerRoll()
    {
        Log.i("hello", "i started computers turn");
        roll_button.callOnClick();

        for(int i = 0; i< 4; i++) {
            if(game.getCurrentImageIndex()!=0) {
                roll_button.callOnClick();
            }
            else{
                break;
            }
            break;
        }
        game.setComputerTurn(false);
        end_button.callOnClick();
    }

    //new game button handler
    public void new_game(View v){
        player_one_edit_text.setText("");
        player_two_edit_text.setText("");
        game.setCurrentImageIndex(7);
        game.setCurrentScore(0);
        game.setPlayerName("player1", "player2");
        game.setPlayersScores(0,0);
        game.winner = "NA";
        game.count = 0;
        win_label_text_view.setText("'s turn");
        game.count = 0; // this count is for keeping track of the winner, (if player one reaches max the player2 gets another turn)
        game.resetCount();// reseting the count for the players turns (for limiting turns)

        if(game.getCurrentPlayer() == game.player2)
        {
            game.switchPlayer();
        }
        guiDisplay();

    }

    public void displayWinner() {
        if (game.winner == game.player1) {
            die_img_view.setImageResource(images[8]);
        }
        else{
            die_img_view.setImageResource(images[9]);
        }

        player_one_score_text_view.setText(game.winner + " wins!!");
        player_two_score_text_view.setText("player1 score: " + game.getPlayerOneScore() + " player2 score: " + game.getPlayerTwoScore());
    }

    //display die image and current player
    public void guiDisplay() {

        if(game.winner == "NA" || game.count ==1) { // if "game.count" = 1 that means the 1st player has already reached
            if(game.count == 1)                     // the winning score but player two still gets another turn.
            {
                game.count++;
                player_id_text_view.setText(game.getCurrentPlayer());
                die_img_view.setImageResource(images[game.getCurrentImageIndex()]);
                points_for_turn_text_view.setText(game.getCurrentScore() + "");
                player_one_score_text_view.setText(game.getPlayerOneScore() + "");
                player_two_score_text_view.setText(game.getPlayerTwoScore() + "");
                if( game.getCurrentImageIndex()==0 && game.getComputerTurn() == true && game.isComputerTurnedOn()== true )
                {
                    //Log.i("hello", "i should be about to call the computer to start rolling");
                    computerRoll();
                }
            }
            else {
                player_id_text_view.setText(game.getCurrentPlayer());
                die_img_view.setImageResource(images[game.getCurrentImageIndex()]);
                points_for_turn_text_view.setText(game.getCurrentScore() + "");
                player_one_score_text_view.setText(game.getPlayerOneScore() + "");
                player_two_score_text_view.setText(game.getPlayerTwoScore() + "");
                if( game.getCurrentImageIndex()==0 && game.getComputerTurn() == true && game.isComputerTurnedOn()== true )
                {
                    computerRoll();
                }
            }

        }
        else if(game.winner == game.player2){
            displayWinner();
        }
        else if (game.winner == game.player1 && game.count < 1)
        {
            game.count++;
            player_id_text_view.setText(game.getCurrentPlayer());
            die_img_view.setImageResource(images[game.getCurrentImageIndex()]);
            points_for_turn_text_view.setText(game.getCurrentScore() + "");
            player_one_score_text_view.setText(game.getPlayerOneScore() + "");
            player_two_score_text_view.setText(game.getPlayerTwoScore() + "");
        }
        else{
            if(game.getPlayerOneScore() < game.getPlayerTwoScore())
            {
                game.winner = game.player2;
            }
            else{
                game.winner = game.player1;
            }
            displayWinner();
        }
    }

    //setting players names once typed into edit texts.
    @Override
    public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
        if (i == EditorInfo.IME_ACTION_DONE || i == EditorInfo.IME_ACTION_UNSPECIFIED) {
            game.setPlayerName(player_one_edit_text.getText().toString(), player_two_edit_text.getText().toString());
        }
        return false;
    }

    //this will make it so my user set preferences will be recieved even after the game has started
    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
        loadPreferences();
    }
}



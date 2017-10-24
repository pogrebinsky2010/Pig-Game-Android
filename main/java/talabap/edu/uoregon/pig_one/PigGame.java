package talabap.edu.uoregon.pig_one;

import android.util.Log;
import android.support.v7.app.AppCompatActivity;
import java.util.Random;

/**
 * Created by talaba on 6/23/16.
 * modified 6/24/16
 */
public class PigGame extends AppCompatActivity {

//variables to hold scores
    int ranNum; // this is for the random number (use it for indexing image array also)
    int count =0;//utility variable for winner
    private static int score=0;
    private static int maxNumClicks = 0;
    private static boolean maxClicks = false;
    private static int playerOneNumClicks=0;
    private static int playerTwoNumClicks = 0;
    private static int winningScore = 100;
    private static int numOfSides = 6;
    private static int finalTurnScore=0;
    private static int currentImageIndex;
    private static int playerOneScore=0;
    private static int playerTwoScore=0;
    private static boolean computerTurn = false;
    private static boolean computerTurnedOn = false;
    public static boolean sevenSides = false;
    public static String winner = "NA";
    public static boolean playerOneTurn = true;
    public static String player1 = "Player1";
    public static String player2 = "Player2";
    Random rand = new Random(); //RANDOM NUMBER FOR DICE AND IMAGE INDEX


    // keeping track of current turns dice score
    public void rollDice() {
        if(sevenSides)
        {
            numOfSides = 7;
        }
        else{
            numOfSides = 6;
        }

         ranNum = rand.nextInt(numOfSides)+1;
        currentImageIndex = ranNum-1;
        Log.i("hello", String.valueOf(currentImageIndex));
         if(ranNum!= 1) {
             score += ranNum;
         }
         else {
            score = 0;
           switchPlayer();
        }
    }
    //setting current players turn
    public void switchPlayer(){
        if(playerOneTurn)
        {

            playerOneTurn = false;
            computerTurn = true;
        }
        else if(playerOneTurn ==false) {
            computerTurn = false;
            playerOneTurn = true;
        }
    }

    //returning players name
    public String getCurrentPlayer(){
        if(playerOneTurn)
        {
            return player1;
        }
        else if(playerOneTurn== false) {
            return player2;
        }
        return "you should not be here??";
    }

    // found using getters and setters are handy for saving state
    public int getCurrentImageIndex()
    {
        return currentImageIndex;
    }
    public void setCurrentImageIndex(int newIndex)
    {
        currentImageIndex = newIndex;
    }
    public void setWinningScore(int _score){winningScore = _score;}
    public int getCurrentScore()
    {
        return score;
    }
    public void setCurrentScore(int newScore)
    {
        score = newScore;
    }
    public int getPlayerOneScore()
    {
        return playerOneScore;
    }
    public int getPlayerTwoScore()
    {
        return playerTwoScore;
    }
    public boolean getComputerTurn(){return computerTurn;};
    public void setComputerTurn(boolean turn){computerTurn = turn;};
    public boolean isComputerTurnedOn(){return computerTurnedOn;};
    public void setComputerTurnedOn(boolean change){computerTurnedOn = change;};
    public int getMaxNumClicks(){return maxNumClicks;}
    public boolean isMaxClicks(){return maxClicks;}
    public void setMaxClicks(boolean isMax){maxClicks = isMax;}
    public void setMaxNumClicks(int newNum)
    {
            setMaxClicks(true);
            maxNumClicks = newNum;
    }

    public void setPlayerName(String _player1, String _player2){
        //trying to avoid setting one players name to ""
        if(player1 =="" && player2 == "")
        {
            player1 = "player1";
            player2 = "player2";

        }
        else if(_player1 == "" && _player2 != "")
        {
            player1 = "player1";
            player2 = _player2;
        }
        else if(_player2 == "")
        {
            player2 = "player2";
            player1 = _player1;
        }
        else {
            player1 = _player1;
            player2 = _player2;
        }
    }
    public int getPlayerTurnCount()
    {
      if(getCurrentPlayer() == player1)
      {
          return playerOneNumClicks;
      }
        else{
          return playerTwoNumClicks;
      }
    }
    public void addOneToCount()
    {
        if(playerOneTurn) {
            playerOneNumClicks++;
        }
        else{
            playerTwoNumClicks++;
        }
    }
    public void resetCount()
    {
            playerOneNumClicks = 0;
            playerTwoNumClicks = 0;
    }
    public void endTurn()
    {

        currentImageIndex = 7;
        finalTurnScore = score;
        score = 0;
            if (playerOneTurn == true) {
                playerOneScore += finalTurnScore;
                if (playerOneScore>= winningScore) {
                    winner = player1;
                    switchPlayer();
                } else {
                    switchPlayer();
                }
            } else {
                playerTwoScore += finalTurnScore;
                if (playerTwoScore >= winningScore) {
                    winner = player2;

                } else {
                    switchPlayer();
                }
            }
        }
    public void setPlayersScores(int _playerOneScore, int _playerTwoScore){
        playerOneScore = _playerOneScore;
        playerTwoScore = _playerTwoScore;
    }
}








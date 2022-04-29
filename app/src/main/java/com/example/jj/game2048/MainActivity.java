package com.example.jj.game2048;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import android.animation.AnimatorInflater;
import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.StateListAnimator;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.preference.PreferenceManager;
import android.speech.RecognitionService;
import android.speech.RecognizerIntent;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private static final int RECOGNIZER_RESULT = 1;
    private static MainActivity mainActivity = null;
//    public int[][] gameBoardStateNumber = new int[4][4];
    public int[][] gameBoardStateNumber = {{-1,-1,-1,-1},{-1,-1,-1,-1},{-1,-1,-1,-1},{-1,-1,-1,-1}};

    private Notification notify;
    private NotificationManager notifyManager;
    Bitmap LargeBitmap = null;
    private static final int NOTIFYID_2048 = 1;

    //new function
    private MediaPlayer mediaPlayer;
    private float speed = 1.0f;
    private float pitch = 1.0f;
    private int totalTime = 0;

    public GameView mGameView;
    private TextView dispPlayScoreTextView;
    private TextView lableScoreTextView;
    private TextView dispPlayBestTextView;
    private TextView lableBestTextView;
    private LinearLayout scoreLinearLayout;
    private LinearLayout bestLinearLayout;
    public Button newGameButton;
    private TextView game2048NameTextview;
    private int score = 0;
    private int bestScore = 0;

    public MainActivity() {
        mainActivity = this;
    }

    public static MainActivity getMainActivity() {
        return mainActivity;
    }
    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);

        mGameView = findViewById(R.id.gameMainView);
        game2048NameTextview = findViewById(R.id.game2048_name_title_textview);


        dispPlayScoreTextView = findViewById(R.id.displaySocre_textview);
        lableScoreTextView = findViewById(R.id.lable_score_textview);
        dispPlayBestTextView = findViewById(R.id.displayBest_textview);
        lableBestTextView = findViewById(R.id.lable_best_textview);
        scoreLinearLayout = findViewById(R.id.score_linear_layout);
        bestLinearLayout = findViewById(R.id.best_linear_layout);
        newGameButton = findViewById(R.id.start_new_game_button);
        /*Setting property of layout: have 3 ways.
        1.use java to set
        2.set in xml file
        3. create a background xml file in drawable folder. and set background
         */
        scoreLinearLayout.setPadding(20,20,20,20);
//        scoreLinearLayout.setBackgroundColor(Color.parseColor("#bbada0"));
//        bestLinearLayout.setPadding(20,20,20,20);
//        bestLinearLayout.setBackgroundColor(Color.parseColor("#bbada0"));

//        lableScoreTextView.setBackgroundColor(Color.parseColor("#faf8ef"));
//        dispPlayScoreTextView.setBackgroundColor(Color.parseColor("#faf8ef"));
        lableScoreTextView.setTextColor(Color.WHITE);
        dispPlayScoreTextView.setTextColor(Color.WHITE);
        lableBestTextView.setTextColor(Color.WHITE);
        dispPlayBestTextView.setTextColor(Color.WHITE);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        bestScore = sharedPreferences.getInt("best", 0);


        showScore();
        showBestScore();

//        Paint mPaint1 = new Paint();
//        mPaint1.setColor(Color.parseColor("red"));
//        Canvas canvas = new Canvas();
//        canvas.drawRect(100, 100, 150, 150, mPaint1);

        setGame2048TitleStateListAnimator();
        //initNewButtonOnTouchAnim();
        game2048NameTextview.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                ObjectAnimator objectAnimator = ObjectAnimator.ofInt(game2048NameTextview,"textColor",
                        Color.parseColor("#776e65"),
                        Color.parseColor("#2196f3"),
                        Color.parseColor("#ffea00"),
                        Color.parseColor("#776e65"));
                objectAnimator.setDuration(5000);
                    objectAnimator.setEvaluator(new ArgbEvaluator());
                objectAnimator.start();
                return false;
            }
        });

        LargeBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.notice_2048_logo);
        notifyManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        backgroundMusic(1);
    }

    public void backgroundMusic(int type){

        switch(type){
            case 1:
                mediaPlayer = MediaPlayer.create(this,R.raw.smb_warning);
                break;
            case 2:
                mediaPlayer = MediaPlayer.create(this,R.raw.smb_gameover);
                break;
            case 3:
                mediaPlayer = MediaPlayer.create(this,R.raw.smb_coin);
                break;
            case 4:
                mediaPlayer = MediaPlayer.create(this,R.raw.smb_fireball);
                break;
            case 5:
                mediaPlayer = MediaPlayer.create(this,R.raw.smb_bump);
                break;
            case 6:
                mediaPlayer = MediaPlayer.create(this,R.raw.smb_powerup);
                break;
            case 7:
                mediaPlayer = MediaPlayer.create(this,R.raw.smb_breakblock);
                break;
            case 8:
                mediaPlayer = MediaPlayer.create(this,R.raw.smb_powerup_appears);
                break;
        }

        //new function:media player
//        mediaPlayer = MediaPlayer.create(this,R.raw.smb_warning);
//        mediaPlayer.setLooping(true);
//        mediaPlayer.seekTo(0);
//        mediaPlayer.setVolume(0.5f, 0.5f);
//        mediaPlayer.setPlaybackParams(mediaPlayer.getPlaybackParams().setSpeed(speed));
//        mediaPlayer.setPlaybackParams(mediaPlayer.getPlaybackParams().setPitch(pitch));
//        totalTime = mediaPlayer.getDuration();
        mediaPlayer.start();
    }

    public void clearScore(){
        score = 0;
        showScore();
    }

    public void showScore(){
        dispPlayScoreTextView.setText(score+"");
    }

    public void showBestScore(){
        dispPlayBestTextView.setText(bestScore+"");
    }

    public void addScore(int earnScore){

        if (earnScore>=128){
            backgroundMusic(8);
        }else if(earnScore>=64){
            backgroundMusic(6);

        }else if(earnScore>=32){
            backgroundMusic(5);
        }else if(earnScore>=16){
            backgroundMusic(4);
        }else if(earnScore>=8){
            backgroundMusic(3);
        }else if(earnScore>=4){
            backgroundMusic(7);
        }

        score += earnScore;
        if(score>=bestScore){
            bestScore = score;
            showBestScore();
        }
        showScore();
    }

    public void Reset() {
//        backgroundMusic(1);
        mGameView.resetGame();
        clearScore();
    }

    @Override
    protected void onResume() {
        super.onResume();
//        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
//        bestScore = sharedPreferences.getInt("best", 0);

        //cancel the notification base on the Notification channel ID.
        notifyManager.cancel(NOTIFYID_2048);

        // other way is call cancelAll() to cancel all notifications which generate by this app
        notifyManager.cancelAll();

        loadGameBoardState();
        showBestScore();
        showScore();

    }

    @Override
    protected void onPause() {
        super.onPause();
//        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
//        SharedPreferences.Editor editor =sharedPreferences.edit();
//        editor.putInt("best", bestScore);
//        editor.putBoolean("switch_on_off", false);
//        editor.commit();
        generateNotification();
        saveGameBoardState();
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        saveGameBoardState();
    }

    public void startNewGameOnClick(View view) {
        new AlertDialog.Builder(view.getContext())
                .setPositiveButton(R.string.reset, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Reset();
                        newGameButton.setBackgroundResource(R.drawable.new_game_button_background);

                        mGameView.setAlpha(0f);
                        mGameView.setRotation(0f);
                        mGameView.setVisibility(View.VISIBLE);
                        mGameView.animate().alpha(1f).rotation(360).setDuration(3000).setListener(null);

                    }
                })
                .setNegativeButton(R.string.continue_game, null)
                .setTitle(R.string.reset_dialog_title)
                .setMessage(R.string.reset_dialog_message)
                .show();
    }

    /* state-animation
     * ref:https://developer.android.com/reference/android/animation/StateListAnimator
     * the difference between an animator and an animation:
     * https://stackoverflow.com/questions/28220613/what-is-the-difference-between-an-animator-and-an-animation
     */
    private void setGame2048TitleStateListAnimator(){
        StateListAnimator stateListAnimator = AnimatorInflater.loadStateListAnimator(this,R.animator.game2048_game_title_state_change);
        game2048NameTextview.setStateListAnimator(stateListAnimator);
    }

    private void initNewButtonOnTouchAnim() {
        int[] attrs = new int[]{R.drawable.new_game_button_background};
        TypedArray typedArray = obtainStyledAttributes(attrs);
        int backgroundResource = typedArray.getResourceId(0, 0);
        newGameButton.setBackgroundResource(backgroundResource);
    }

    private void saveGameBoardState(){
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = settings.edit();
        Card[][] cardsMap = mGameView.cardsMap;

        for(int x = 0; x < cardsMap.length; x++){
            for(int y =  0; y < cardsMap[0].length; y++){
                //Log.i(TAG, x + "---" + y + ": " + cardsMap[x][y].getNum());
                editor.putInt(x + "_" + y, cardsMap[x][y].getNum());
            }
        }
        editor.putInt("best", bestScore);
        editor.putInt("score", score);
        editor.commit();
    }

    private void loadGameBoardState(){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        for (int x = 0; x < mGameView.cardsMap.length; x++) {
            for (int y = 0; y < mGameView.cardsMap[0].length; y++) {
                int value = sharedPreferences.getInt(x + "_" + y, -1);
                if (value > 0) {
                    //mGameView.cardsMap[x][y].setNum(value);
                    gameBoardStateNumber[x][y] = value;
                } else {
                    //mGameView.cardsMap[x][y].setNum(0);
                    gameBoardStateNumber[x][y] = 0;
                }
            }
        }
        bestScore = sharedPreferences.getInt("best", 0);
        score = sharedPreferences.getInt("score", 0);
    }

    private void generateNotification(){
        Log.d(TAG, "generateNotification()");
        Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        Intent mainActivityIntent = new Intent(getApplicationContext(), MainActivity.class);
        PendingIntent pendingMainActivityIntent = PendingIntent.getActivity(getApplicationContext(),
                0, mainActivityIntent, 0);

        Notification.Builder mBuilder = new Notification.Builder(this);
        mBuilder.setContentTitle("2048 Game")                        //setting title
                .setContentText("HOW TO PLAY: Use your arrow keys to move the tiles. When two tiles " +
                        "with the same number touch, they merge into one!")//setting content
                .setSubText("---- design by JJ")                    //setting subtext
//                .setTicker("this method is for android 5.0 or earlier version")             // work for Android 5.0（L）
                .setWhen(System.currentTimeMillis())           //setting time
                .setSmallIcon(R.mipmap.ic_launcher)            //setting small icon which is on the notification bar
                .setLargeIcon(LargeBitmap)                     //setting the large icon which is on the left of Notification window
                .setDefaults(Notification.DEFAULT_LIGHTS | Notification.DEFAULT_VIBRATE)    //setting light and vibrate
                .setSound(uri)  //setting notice sound
                .setAutoCancel(true)                           //setting allow click to cancel the notification
                .setContentIntent(pendingMainActivityIntent);                        //setting PendingIntent
        notify = mBuilder.build();
        notifyManager.notify(NOTIFYID_2048, notify);
    }

    public void micOnClick(View view) {
        Intent speakIntent =new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        speakIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);

        speakIntent.putExtra(RecognizerIntent.EXTRA_PROMPT,"Speak to the mic");
        startActivityForResult(speakIntent,RECOGNIZER_RESULT);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == RECOGNIZER_RESULT && resultCode == RESULT_OK)
        {
            ArrayList<String> ordersArray =new ArrayList<>();
            ordersArray = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);

            String bestOrder = ordersArray.get(0);
            Log.i(TAG, "onActivityResult_score before: " + score);
            Log.i(TAG, "onActivityResult: "+ bestOrder);
            if(bestOrder.contains("left")|| bestOrder.contains("left")){
//                Log.i(TAG, "onActivityResult_score: " + score);
                mGameView.swipeLeft();
            }else if(bestOrder.contains("right")|| bestOrder.contains("right")){
//                Log.i(TAG, "onActivityResult_score: " + score);
                mGameView.swipeRight();
            }else if(bestOrder.contains("top") || bestOrder.contains("up")){
//                Log.i(TAG, "onActivityResult_score: " + score);
                mGameView.swipeUp();
            }else if(bestOrder.contains("down") || bestOrder.contains("bottom")){
//                Log.i(TAG, "onActivityResult_score: " + score);
                mGameView.swipeDown();
            }else{
                Log.i(TAG, "onActivityResult: NO MATCH WORDS");
            }
            Log.i(TAG, "onActivityResult_score after: " + score);
//            showScore();
            SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);
            SharedPreferences.Editor editor = settings.edit();
            editor.putInt("score", score);
            editor.commit();
        }

    }
}
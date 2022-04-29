package com.example.jj.game2048;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.GridLayoutAnimationController;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class GameView extends GridLayout {

    private static final String TAG = "GameView";
    public static int cardWidth;

    private Context context;
    private final Paint paint = new Paint();

    public Card[][] cardsMap = new Card[4][4];
    private List<Point> emptyPoints = new ArrayList<>();

    public GameView(Context context) {
        super(context);
        this.context = context;
        initialGameView();
    }

    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        initialGameView();
    }

    public GameView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        initialGameView();
    }

    public GameView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.context = context;
        initialGameView();
    }

    private void initialGameView(){

        setColumnCount(4);
        setRowCount(4);
//        setBackgroundColor(0xffbbada0);
//        setBackgroundColor(Color.parseColor("#bbada0"));
        setOnTouchListener(new View.OnTouchListener(){

            private float startX;
            private float startY;
            private float offSetX;
            private float offSetY;
            //Ref: https://blog.csdn.net/jianesrq0724/article/details/54908119  (Chinese)
            //https://developer.android.com/reference/android/view/MotionEvent
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch(event.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        startX = event.getX();
                        startY = event.getY();
                        break;
                    case MotionEvent.ACTION_UP:
                        offSetX = event.getX()-startX;
                        offSetY = event.getY() - startY;
                        if(Math.abs(offSetX) > Math.abs(offSetY)){
                            if(offSetX<-5){
                                swipeLeft();
                                Log.i(TAG,"Left");
                            }
                            else if(offSetX>5){
                                swipeRight();
                                Log.i(TAG,"Right");
                            }
                        }else{
                            if(offSetY<-5){
                                swipeUp();
                                Log.i(TAG,"UP");
                            }
                            else if(offSetY>5){
                                swipeDown();
                                Log.i(TAG,"DOWN");
                            }
                        }
                        break;
                    default:
                        break;
                }
                //if return false, only run the Action.Down. If return true, run Action_UP,DOWN,MOVE
                return true;
            }
        });
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        cardWidth = (Math.min(w,h) - 30) / 4;
        addCards(cardWidth,cardWidth);
//        startGame();
        initStartGame();
    }

    private void addCards(int width, int height){
        Card card;

        for(int row = 0; row < 4; row++){
            for(int col = 0; col < 4; col++){
                card = new Card(getContext());
//                card.setBackgroundResource(R.drawable.game_cell_border_layout);
//                card.setBackgroundColor(Color.parseColor("red"));
                card.setNum(0);
//                card.setBackgroundResource(R.drawable.game_board_layout);
                addView(card,width,height);
                cardsMap[row][col] = card;
            }
        }
    }

    private void initStartGame(){

        //MainActivity.getMainActivity().clearScore();
        boolean allClear = true;
        for(int x = 0; x < 4; x++ ){
            for(int y = 0; y < 4; y++){
                cardsMap[x][y].setNum(MainActivity.getMainActivity().gameBoardStateNumber[x][y]);
                if(cardsMap[x][y].getNum()!=0){
                    allClear = false;
                }
            }
        }
        if(allClear){
            addRandomNum();
            addRandomNum();
        }
    }

    private void startGame(){

        MainActivity.getMainActivity().backgroundMusic(1);
        //MainActivity.getMainActivity().clearScore();

        for(int i = 0; i < 4; i++ ){
            for(int y = 0; y < 4; y++){
                cardsMap[i][y].setNum(0);
            }
        }

        addRandomNum();
        addRandomNum();
    }

    private void addRandomNum(){

        emptyPoints.clear();

        for(int x = 0; x < 4; x++){
            for(int y = 0; y < 4; y++){
                if(cardsMap[x][y].getNum() <=0){
                    emptyPoints.add(new Point(x,y));
                }
            }
        }
        Point point = emptyPoints.remove((int)(Math.random()*emptyPoints.size()));
        cardsMap[point.x][point.y].setNum(Math.random()>0.1?2:4);
    }

    public void swipeLeft(){

        boolean merge = false;

//        for(int row = 0; row < 4; row++){
//            for(int col = 0; col < 4; col++){
//                Log.i(TAG,"CardsMap[" + row + "][" + col + "] = " + cardsMap[row][col].getNum());
//            }
//
//        }

        for (int row = 0; row < 4; row++) {
            for (int col = 0; col < 4; col++) {
                for (int backCol = col+1; backCol< 4; backCol++) {
                    if(cardsMap[row][backCol].getNum()>0){
                        if(cardsMap[row][col].getNum()<=0){

//                            Animation animation = AnimationUtils.loadAnimation(context,R.anim.move);
//                            cardsMap[row][backCol].startAnimation(animation);
//                            int[] oldLocation = new int[2];
//                            int[] newLocation = new int[2];
//                            cardsMap[row][backCol].disPlayNumberTV.getLocationInWindow(oldLocation);
//                            cardsMap[row][col].disPlayNumberTV.getLocationInWindow(newLocation);
//                            cardsMap[row][backCol].animationMove(oldLocation,newLocation);

                            cardsMap[row][col].setNum(cardsMap[row][backCol].getNum());
                            cardsMap[row][backCol].setNum(0);

//                            zoomUpAnimation(cardsMap[row][col]);
                            cellAnimationFactory(cardsMap[row][col]);
                            col--;
                            merge = true;
                        }
                        else if(cardsMap[row][backCol].equals(cardsMap[row][col])){
                            cardsMap[row][col].setNum(cardsMap[row][col].getNum()*2);
                            cardsMap[row][backCol].setNum(0);

//                            zoomUpAnimation(cardsMap[row][col]);
                            cellAnimationFactory(cardsMap[row][col]);

                            MainActivity.getMainActivity().addScore(cardsMap[row][col].getNum());
                            merge = true;
                        }
                        break;
                    }
                }
            }
        }

        if(merge){
            addRandomNum();
            isFinished();
        }
    }

    public void swipeRight(){

        boolean merge = false;
        for (int row = 0; row < 4; row++) {
            for (int col = 3; col >= 0; col--) {
                for (int forwardCol = col-1; forwardCol >= 0; forwardCol--) {
                    if(cardsMap[row][forwardCol].getNum()>0){

                        if(cardsMap[row][col].getNum()<=0){
                            cardsMap[row][col].setNum(cardsMap[row][forwardCol].getNum());
                            cardsMap[row][forwardCol].setNum(0);

//                            zoomUpAnimation(cardsMap[row][col]);
                            cellAnimationFactory(cardsMap[row][col]);

                            col++;
                            merge = true;
                        }
                        else if(cardsMap[row][forwardCol].equals(cardsMap[row][col])){
                            cardsMap[row][col].setNum(cardsMap[row][col].getNum()*2);
                            cardsMap[row][forwardCol].setNum(0);

//                            zoomUpAnimation(cardsMap[row][col]);
                            cellAnimationFactory(cardsMap[row][col]);

                            MainActivity.getMainActivity().addScore(cardsMap[row][col].getNum());
                            merge = true;
                        }
                        break;
                    }
                }
            }
        }
        if(merge){
            addRandomNum();
            isFinished();
        }
    }

    public void swipeUp(){
        boolean merge = false;
        for (int col = 0; col < 4; col++) {
            for (int row = 0; row < 4; row++) {
                for (int backRow = row+1; backRow< 4; backRow++) {
                    if(cardsMap[backRow][col].getNum()>0){

                        if(cardsMap[row][col].getNum()<=0){
                            cardsMap[row][col].setNum(cardsMap[backRow][col].getNum());
                            cardsMap[backRow][col].setNum(0);

//                            zoomUpAnimation(cardsMap[row][col]);
                            cellAnimationFactory(cardsMap[row][col]);

                            row--;
                            merge = true;
                        }
                        else if(cardsMap[backRow][col].equals(cardsMap[row][col])){
                            cardsMap[row][col].setNum(cardsMap[row][col].getNum()*2);

//                            zoomUpAnimation(cardsMap[row][col]);
                            cellAnimationFactory(cardsMap[row][col]);

                            cardsMap[backRow][col].setNum(0);
                            MainActivity.getMainActivity().addScore(cardsMap[row][col].getNum());
                            merge = true;
                        }
                        break;
                    }
                }
            }
        }
        if(merge){
            addRandomNum();
            isFinished();
        }
    }

    public void swipeDown(){
        boolean merge = false;
        for (int col = 0; col < 4; col++) {
            for (int row = 3; row >= 0; row--) {
                for (int forwardRow = row-1; forwardRow >=0; forwardRow--) {
                    if(cardsMap[forwardRow][col].getNum()>0){

                        if(cardsMap[row][col].getNum()<=0){
                            cardsMap[row][col].setNum(cardsMap[forwardRow][col].getNum());
                            cardsMap[forwardRow][col].setNum(0);

//                            zoomUpAnimation(cardsMap[row][col]);
                            cellAnimationFactory(cardsMap[row][col]);

                            row++;
                            merge = true;
                        }
                        else if(cardsMap[forwardRow][col].equals(cardsMap[row][col])){
                            cardsMap[row][col].setNum(cardsMap[row][col].getNum()*2);
                            cardsMap[forwardRow][col].setNum(0);

//                            zoomUpAnimation(cardsMap[row][col]);
                            cellAnimationFactory(cardsMap[row][col]);

                            MainActivity.getMainActivity().addScore(cardsMap[row][col].getNum());
                            merge = true;
                        }
                        break;
                    }
                }
            }
        }
        if(merge){
            addRandomNum();
            isFinished();
        }
    }

    private void isFinished(){

        boolean complete = true;
        //method one: jump over nest loop; using goto
        //ALL:
        for (int y = 0; y < 4; y++) {
            for (int x = 0; x < 4; x++) {
                if(cardsMap[x][y].getNum()==0 ||
                    (x>0&&cardsMap[x][y].equals(cardsMap[x-1][y]))||
                    (x<3&&cardsMap[x][y].equals(cardsMap[x+1][y]))||
                    (y>0&&cardsMap[x][y].equals(cardsMap[x][y-1]))||
                    (y<3&&cardsMap[x][y].equals((cardsMap[x][y+1])))) {
                    complete = false;
                    //method one: jump over nest loop
                    //break ALL;

                    //method two: change the condition
                    x = 4;
                    y = 4;

                }
            }
        }
        if(complete){
//            new AlertDialog.Builder(getContext()).setTitle("Hello").setMessage("Game Finish").setPositiveButton("Restart",new DialogInterface.OnClickListener(){
//
//                @Override
//                public void onClick(DialogInterface dialog, int which) {
//                    startGame();
//                }
//            }).show();

            MainActivity.getMainActivity().backgroundMusic(2);
            AlertDialog mDialog =  new AlertDialog.Builder(this.getContext())
                    .setPositiveButton(R.string.reset, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            startGame();
                            MainActivity.getMainActivity().mGameView.setAlpha(0f);
                            MainActivity.getMainActivity().mGameView.setVisibility(View.VISIBLE);
                            MainActivity.getMainActivity().mGameView.animate().alpha(1f).setDuration(3000).setListener(null);
                        }
                    })
                    .setNegativeButton(R.string.continue_game, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            MainActivity.getMainActivity().newGameButton.setBackgroundResource(R.drawable.light_up_new_game_button_background);
                        }
                    })
                    .setTitle(R.string.reset_dialog_title)
                    .setMessage(R.string.reset_dialog_message)
                    .show();
            mDialog.setCancelable(false);


        }
    }

    private void drawDrawable(Canvas canvas, Drawable draw, int startingX, int startingY, int endingX, int endingY) {
        draw.setBounds(startingX, startingY, endingX, endingY);
        draw.draw(canvas);
    }

    private void zoomUpAnimation(Card card,float toX, float toY){
        AnimationSet animationSet = new AnimationSet(true);
        ScaleAnimation scaleAnimation = new ScaleAnimation(0.8f,toX,0.8f,toY,
                Animation.RELATIVE_TO_SELF,0.5f,
                Animation.RELATIVE_TO_SELF,0.5f);
        scaleAnimation.setDuration(500);
        animationSet.addAnimation(scaleAnimation);
        animationSet.setFillAfter(false);
        card.disPlayNumberTV.startAnimation(animationSet);
    }


    private void rotateAnimation(Card card,int rotationToDegress){
        RotateAnimation animation = new RotateAnimation(0,
                rotationToDegress,
                Animation.RELATIVE_TO_SELF,
                0.5f,
                Animation.RELATIVE_TO_SELF,
                0.5f);
        animation.setDuration(500);
        animation.setInterpolator(context, android.R.anim.anticipate_interpolator);
        card.disPlayNumberTV.startAnimation(animation);
    }

    private void rotateAndZoomAnimationSet(Card card,int rotation,float scaleX, float scaleY){
//        AnimatorSet.playTogether(Animator... anim)
//        AnimatorSet.playSequentially(Animator... anim)
//        AnimatorSet.play(Animator anim)
//        AnimatorSet.after(long delay)
//        AnimatorSet.with(Animator anim)
//        AnimatorSet.after(Animator anim)
//        AnimatorSet.before(Animator anim)

        ObjectAnimator rotationAnim = ObjectAnimator.ofFloat(card, "rotation", 0, rotation);
        ObjectAnimator scaleXAnim = ObjectAnimator.ofFloat(card, "scaleX", 0.8f, scaleX, 1f);
        ObjectAnimator scaleYAnim = ObjectAnimator.ofFloat(card, "scaleY", 0.8f, scaleY, 1f);

        AnimatorSet animatorSet = new AnimatorSet();

        //        animatorSet.playTogether(translationX,rotation,alpha);
//        animatorSet.playSequentially();

        animatorSet.play(rotationAnim).with(scaleXAnim).with(scaleYAnim);

        animatorSet.setDuration(500);
        animatorSet.setInterpolator(new LinearInterpolator());
        animatorSet.start();
    }

    private void cellAnimationFactory(Card card){
        int number = Integer.valueOf(card.getNum());
        switch(number){
            case 4:
            case 8:
                zoomUpAnimation(card,1.15f,1.15f);
                break;
            case 16:
            case 32:
                rotateAnimation(card,360);
                break;
            case 64:
            case 128:
//                zoomUpAnimation(card,1.3f,1.3f);
                rotateAndZoomAnimationSet(card,-360,1.15f,1.15f);
                break;
            case 256:
            case 512:
            case 1024:
            case 2048:
            case 4096:
                rotateAndZoomAnimationSet(card,360,1.3f,1.3f);
                break;
        }
//        zoomUpAnimation(card);
//        rotateAnimation(card);
    }

    public void resetGame(){
        startGame();
    }

    @Override
    protected void attachLayoutAnimationParameters(View child, ViewGroup.LayoutParams params, int index, int count) {
        Animation animation = AnimationUtils.loadAnimation(getContext(),R.anim.gridview);
        GridLayoutAnimationController controller = new GridLayoutAnimationController(animation, .2f, .2f);
        this.setLayoutAnimation(controller);
    }


}

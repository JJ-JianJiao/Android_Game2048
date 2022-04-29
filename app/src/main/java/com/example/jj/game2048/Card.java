package com.example.jj.game2048;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.w3c.dom.Text;

public class Card extends FrameLayout {

    private Context context;
    private int num  = 0;
    public TextView disPlayNumberTV;

//    private Button aButton;

    public Card(@NonNull Context context) {
        super(context);
        this.context = context;

//        aButton = new Button(getContext());
//        aButton.setText("a");
//        aButton.setWidth(10);
//        aButton.setHeight(10);

        disPlayNumberTV = new TextView(getContext());
        disPlayNumberTV.setTextSize(40);
        disPlayNumberTV.setGravity(Gravity.CENTER);
        disPlayNumberTV.setBackgroundResource(R.drawable.game_cell_border_layout);
        disPlayNumberTV.getPaint().setFakeBoldText(true);
//        disPlayNumberTV.setTextColor(Color.parseColor("red"));
//        disPlayNumberTV.setPadding(15,15,0,0);
//        disPlayNumberTV.setBackgroundColor(0x33ffffff);
//        disPlayNumberTV.setBackgroundColor(Color.parseColor("White"));

        LayoutParams layoutParams = new LayoutParams(-1,-1);
        layoutParams.setMargins(30,30,0,0);


//        addView(aButton,layoutParams);
        addView(disPlayNumberTV,layoutParams);
        setNum(0);
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
        if(num==0){
            disPlayNumberTV.setText("");
            disPlayNumberTV.setBackgroundColor(Color.parseColor("#d6cdc4"));
        }else{
            disPlayNumberTV.setText(num+"");
            if(num==2){
                disPlayNumberTV.setBackgroundColor(Color.parseColor("#eee4da"));
            }
            else if(num==4){
                disPlayNumberTV.setBackgroundColor(Color.parseColor("#ede0c8"));
            }
            else if(num==8){
                disPlayNumberTV.setBackgroundColor(Color.parseColor("#f2b179"));
            }
            else if(num==16){
                disPlayNumberTV.setBackgroundColor(Color.parseColor("#f59563"));
            }
            else if(num==32){
                disPlayNumberTV.setBackgroundColor(Color.parseColor("#f67c5f"));
            }
            else if(num==64){
                disPlayNumberTV.setBackgroundColor(Color.parseColor("#f65e3b"));
            }
            else if(num==128){
                disPlayNumberTV.setBackgroundColor(Color.parseColor("#edcf72"));
            }
            else if(num==256){
                disPlayNumberTV.setBackgroundColor(Color.parseColor("#edcc61"));
            }
            else if(num==512){
                disPlayNumberTV.setBackgroundColor(Color.parseColor("#edc850"));
            }
            else if(num==1024){
                disPlayNumberTV.setBackgroundColor(Color.parseColor("#edc53f"));
            }
            else if(num==2048){
                disPlayNumberTV.setBackgroundColor(Color.parseColor("#edc22e"));
            }
            else if(num==4096){
                disPlayNumberTV.setBackgroundColor(Color.parseColor("#3c3a32"));
            }
        }

        if(num>=8){
            disPlayNumberTV.setTextColor(Color.parseColor("#f9f6f2"));
        }else{
            disPlayNumberTV.setTextColor(Color.parseColor("#776e65"));
        }
    }

    public boolean equals(Card card) {
        return getNum() == card.getNum();
    }

    public void animationMove(int[] oldLocation, int[] newLocation){
//        Animation cardTranslate = new TranslateAnimation(oldLocation[0]-150f,oldLocation[1],0f,0f);
//        disPlayNumberTV.setAnimation(cardTranslate);
//        cardTranslate.setDuration(100000);
//        cardTranslate.start();
        Animation animation = AnimationUtils.loadAnimation(context,R.anim.move);
        disPlayNumberTV.startAnimation(animation);
    }


//    private void zoomUpAnimation(TextView tv){
//        AnimationSet animationSet = new AnimationSet(true);
////        ScaleAnimation scaleAnimation = new ScaleAnimation(1.0f,1.15f,1.0f,1.15f,
////                Animation.RELATIVE_TO_SELF,0.5f,
////                Animation.RELATIVE_TO_SELF,0.5f);
//        ScaleAnimation scaleAnimation = new ScaleAnimation(0.8f,1.15f,0.8f,1.15f,
//                Animation.RELATIVE_TO_SELF,0.5f,
//                Animation.RELATIVE_TO_SELF,0.5f);
//        scaleAnimation.setDuration(5000);
//        animationSet.addAnimation(scaleAnimation);
//        animationSet.setFillAfter(false);
//        tv.startAnimation(animationSet);
//    }
}

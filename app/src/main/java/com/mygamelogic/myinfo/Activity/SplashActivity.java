package com.mygamelogic.myinfo.Activity;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.DecelerateInterpolator;

import com.mygamelogic.myinfo.R;

/**
 * Created by admin on 15/07/18.
 */

public class SplashActivity extends AppCompatActivity{

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        setLogoAppearAnim(findViewById(R.id.imageview_splashlogo));
        setTextAppearAnim(findViewById(R.id.textview_splashtitle));
        waitForSomeTime();
    }

    private void setLogoAppearAnim(View view){
        view.setAlpha(0);
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(view, "scaleX", 0.5f, 1.0f);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(view, "scaleY", 0.5f, 1.0f);
        ObjectAnimator alpha = ObjectAnimator.ofFloat(view, "alpha", 0, 1.0f);
        AnimatorSet scaleUp = new AnimatorSet();
        scaleUp.play(scaleX).with(scaleY).with(alpha);
        scaleUp.setStartDelay(300);
        scaleUp.setInterpolator(new DecelerateInterpolator());
        scaleUp.setDuration(500);
        scaleUp.start();
    }
    private void setTextAppearAnim(View view){
        view.setAlpha(0);
        ObjectAnimator alpha = ObjectAnimator.ofFloat(view, "alpha", 0, 1.0f);
        alpha.setDuration(800);
        alpha.start();
    }

    private void waitForSomeTime(){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                gotoMainPage();
            }
        }, 2100);
    }

    private void gotoMainPage(){
        Intent intent=new Intent(this,MainActivity.class);
        startActivity(intent);
        SplashActivity.this.finish();
    }
}

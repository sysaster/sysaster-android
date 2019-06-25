package br.ufrn.dimap.sysaster.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import br.ufrn.dimap.sysaster.R;

public class MainActivity extends AppCompatActivity {

    private final int TRANSITION_TIME = 500;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final View mView = findViewById(R.id.splash_view);
        ValueAnimator animator = ValueAnimator.ofFloat(0f, 1f);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mView.setAlpha((Float) animation.getAnimatedValue());
            }
        });

        animator.setDuration(TRANSITION_TIME);
        animator.setRepeatMode(ValueAnimator.REVERSE);
        animator.setRepeatCount(10);

        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);

                Intent i = new Intent(getApplicationContext(), MenuActivity.class);
                startActivity(i);
                finish();
            }
        });

        animator.start();
    }


}

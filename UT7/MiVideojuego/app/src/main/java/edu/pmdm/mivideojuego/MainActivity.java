package edu.pmdm.mivideojuego;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        animarBoton();
        animarImageView();
        animarMan();
    }

    public void animarMan(){
        AnimationDrawable animacionRobot;
        ImageView imgRobot=findViewById(R.id.robot);
        imgRobot.setBackgroundResource(R.drawable.man);
        animacionRobot= (AnimationDrawable) imgRobot.getBackground();
        animacionRobot.start();

        AnimatorSet animadorRobot=new AnimatorSet();
        ObjectAnimator trasladar=ObjectAnimator.ofFloat(imgRobot,"translationX",0,800);
        trasladar.setDuration(10000);
        trasladar.setRepeatMode(ObjectAnimator.RESTART);
        trasladar.setRepeatCount(ObjectAnimator.INFINITE);
        animadorRobot.play(trasladar);
        animadorRobot.start();
    }

    public void animarImageView(){
        ImageView img=findViewById(R.id.imageView);
        Animation animacion= AnimationUtils.loadAnimation(this,R.anim.animacion);
        img.startAnimation(animacion);
    }
    public void animarBoton(){
        AnimatorSet btnAnimator=new AnimatorSet();
        Button btn=findViewById(R.id.button);
        //Primera animacion
        ObjectAnimator trasladar= ObjectAnimator.ofFloat(btn,"translationX",-800,0);
        trasladar.setDuration(5000);
        //Segunda animacion
        ObjectAnimator fade=ObjectAnimator.ofFloat(btn,"alpha",0f,1f);
        fade.setDuration(8000);
        //Tercera animacion
        ObjectAnimator rotar=ObjectAnimator.ofFloat(btn,"rotationY",0,360);
        rotar.setDuration(5000);
        //Cuarta animacion
        ObjectAnimator color=ObjectAnimator.ofArgb(btn,"backgroundColor", Color.argb(128,255,0,0),Color.argb(128,0,0,255));
        color.setDuration(5000);
        //Quinta animacion
        ObjectAnimator trasladarY=ObjectAnimator.ofFloat(btn,"translationY",1000,0);
        trasladarY.setDuration(5000);

        btnAnimator.play(trasladar).with(fade).with(rotar).with(color).with(trasladarY);
        btnAnimator.start();
    }
}
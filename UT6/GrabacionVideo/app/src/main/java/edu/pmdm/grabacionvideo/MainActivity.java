package edu.pmdm.grabacionvideo;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.MediaController;
import android.widget.Switch;
import android.widget.VideoView;

public class MainActivity extends AppCompatActivity implements MediaController.MediaPlayerControl {
    MediaController mc;
    MediaPlayer mp;
    Handler h;
    VideoView v;

    private final int ALTA_CALIDAD=1;
    private final int BAJA_CALIDAD=0;
    private int duracion, calidad;
    private int CAPTURA_VIDEO=1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mc= new MediaController(this);
        mc.setMediaPlayer(this);
        mc.setAnchorView(findViewById(R.id.constraintLayout));

        v=findViewById(R.id.videoView);
        v.setMediaController(mc);

        Button btnGrabar=findViewById(R.id.btnGrabar);
        btnGrabar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // calidad
                Switch swCalidad=findViewById(R.id.swCalidad);
                if(swCalidad.isChecked())
                    calidad=ALTA_CALIDAD;
                else
                    calidad=BAJA_CALIDAD;

                EditText edDuracion=findViewById(R.id.edDuracion);
                try {
                    duracion = Integer.parseInt(edDuracion.getText().toString());
                }catch(NumberFormatException n){
                    duracion=5; //por defecto
                }

                Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
                intent.putExtra(MediaStore.EXTRA_DURATION_LIMIT, duracion);
                intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, calidad);
                startActivityForResult(intent, CAPTURA_VIDEO);

            }
        });


    }


    @Override
    public void start() {
        if(!v.isPlaying())
            v.start();
    }

    @Override
    public void pause() {
        if(v.isPlaying())
            v.pause();
    }

    @Override
    public int getDuration() {
        return v.getDuration();
    }

    @Override
    public int getCurrentPosition() {
        return v.getCurrentPosition();
    }

    @Override
    public void seekTo(int i) {
        v.seekTo(i);
    }

    @Override
    public boolean isPlaying() {
        return v.isPlaying();
    }

    @Override
    public int getBufferPercentage() {
        return 0;
    }

    @Override
    public boolean canPause() {
        return true;
    }

    @Override
    public boolean canSeekBackward() {
        return true;
    }

    @Override
    public boolean canSeekForward() {
        return true;
    }

    @Override
    public int getAudioSessionId() {
        return v.getAudioSessionId();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(event.getAction()==MotionEvent.ACTION_DOWN)
            if(!mc.isShowing())
                mc.show(0);
            else
                mc.hide();
        return false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==CAPTURA_VIDEO && resultCode==RESULT_OK) {

            v.setVideoURI(data.getData()); //v es un VideoView

            h = new Handler();
            v.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mediaPlayer) {
                    h.post(new Runnable() {
                        @Override
                        public void run() {
                            mc.show(0);
                            v.start();
                        }
                    });
                }
            });


        }
    }
}

package com.example.prc6_danielmonforte;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class VideoActivity extends AppCompatActivity {

    private VideoView videoView;
    private MediaController mediaController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);

        videoView = findViewById(R.id.videoView);
        mediaController = new MediaController(this);
        Recurso recurso = getIntent().getParcelableExtra("recurso");
        String videoUri=recurso.getUri();
        Uri uri=null;
        if (recurso.getUri() != null) {
            if(recurso.getTipo()==EnumTipos.VIDEO.valor){
                int videoId=getResources().getIdentifier(videoUri, "raw", getPackageName());
                if(videoId!=0){
                    uri = Uri.parse("android.resource://" + getPackageName() + "/" +videoId);
                }
                else Toast.makeText(this,"No se pudo encontrar el recurso del video",Toast.LENGTH_SHORT).show();
            }
            else if(recurso.getTipo()==EnumTipos.STREAMING.valor){
                uri=Uri.parse(recurso.getUri());
            }
            videoView.setMediaController(mediaController);
            mediaController.setAnchorView(videoView);
            videoView.setVideoURI(uri);
            videoView.start();
        }
        else Toast.makeText(this,"No se encontró URI del video",Toast.LENGTH_SHORT).show();

        Button botonVolver=findViewById(R.id.btnVolver);
        botonVolver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (videoView != null) videoView.suspend();
        if (mediaController != null) mediaController.hide();
    }
}
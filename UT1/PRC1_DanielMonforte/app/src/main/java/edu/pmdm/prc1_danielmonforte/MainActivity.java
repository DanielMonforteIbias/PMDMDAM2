package edu.pmdm.prc1_danielmonforte;

import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        EditText series=findViewById(R.id.editTextSeries);
        EditText txtSegundosTrabajo=findViewById(R.id.editTextTrabajo);
        EditText txtSegundosDescanso=findViewById(R.id.editTextDescanso);
        ImageButton playButton=findViewById(R.id.imgBtnPlay);
        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int numeroSeries=0, segundosTrabajo=0, segundosDescanso=0;
                boolean valoresValidos=true; //Variable que controlará si se sigue o no, en base a si los valores son o no válidos
                try{ //Intentamos leer el numero de series
                    numeroSeries=Integer.parseInt(series.getText().toString());
                    if(numeroSeries<=0){ //Si el valor no es positivo
                        showToast(("El número de series debe ser mayor que 0"));
                        valoresValidos=false;
                    }
                }
                catch (Exception e){ //Si es una letra o un valor no válido
                    showToast("Número de series no válido");
                    valoresValidos=false;
                }
                try{ //Intentamos leer el numero de segundos de trabajo
                    segundosTrabajo=Integer.parseInt(txtSegundosTrabajo.getText().toString());
                    if(segundosTrabajo<=0){ //Si el valor no es positivo
                        showToast(("Los segundos de trabajo deben ser mayor que 0"));
                        valoresValidos=false;
                    }
                }
                catch(Exception e){ //Si es una letra o un valor no válido
                    showToast("Segundos de trabajo no válido");
                    valoresValidos=false;
                }
                try{ //Intentamos leer el numero de segundos de descanso
                    segundosDescanso=Integer.parseInt(txtSegundosDescanso.getText().toString());
                    if(segundosDescanso<=0){ //Si el valor no es positivo
                        showToast(("Los segundos de descanso deben ser mayor que 0"));
                        valoresValidos=false;
                    }
                }
                catch(Exception e){ //Si es una letra o un valor no válido
                    showToast("Segundos de descanso no válido");
                    valoresValidos=false;
                }
                if(valoresValidos){ //SI los valores son validos, comenzaremos la rutina de trabajo, si no, no
                    cicloTrabajo(numeroSeries,segundosTrabajo,segundosDescanso);
                }

            }
        });
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
    private void showToast(String message){
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show(); //Mostramos un Toast con el mensaje recibido
    }

    public void playSound(){
        //MediaPlayer mp= MediaPlayer.create(getApplicationContext(),R.raw.beep);
        //mp.start();
    }

    private void cicloTrabajo(int numeroSeries, int segundosTrabajo, int segundosDescanso){
        //OBTENEMOS ELEMENTOS MEDIANTE ID
        ConstraintLayout layout=findViewById(R.id.main);
        TextView txtSegundos=findViewById(R.id.txtSecondsLeft);
        TextView txtEstado=findViewById(R.id.txtState);
        TextView txtSeries=findViewById(R.id.txtSeriesLeft);
        ImageButton btnPlay=findViewById(R.id.imgBtnPlay);

        //ACTUALIZAMOS INFORMACION EN PANTALLA, COLORES...
        txtSeries.setText("SERIES LEFT: "+numeroSeries); //Actualizamos el número de series restantes
        txtEstado.setText("WORK"); //Actualizamos el estado a "WORK"
        layout.setBackgroundResource(R.drawable.degradado_verde); //Ponemos el degradado verde como fondo del layout
        btnPlay.setEnabled(false); //Desactivamos el botón para que el usuario no lo pueda pulsar mientras hay un ciclo activo
        //TIMERS
        new CountDownTimer(segundosTrabajo*1000, 1000) { //countDownInterval indica cuánto tiempo se tarda en actualizar la cuenta atrás. 1000ms es 1s
            public void onTick(long millisUntilFinished) {
                txtSegundos.setText(millisUntilFinished / 1000+"");
            }

            public void onFinish() {
                txtEstado.setText("REST");
                layout.setBackgroundColor(Color.RED);
                new CountDownTimer(segundosDescanso*1000, 1000) { //countDownInterval indica cuánto tiempo se tarda en actualizar la cuenta atrás. 1000ms es 1s
                    public void onTick(long millisUntilFinished) {
                        txtSegundos.setText(millisUntilFinished / 1000+"");
                    }

                    public void onFinish() {
                        if (numeroSeries==1){ //Si el número de series es 1, es decir, la que estamos haciendo es la última
                            txtSeries.setText("SERIES LEFT: 0");
                            txtEstado.setText("FINISHED"); //Informamos de que se ha terminado el ciclo
                            layout.setBackgroundColor(Color.WHITE); //Cambiamos el color de fondo a blanco
                            btnPlay.setEnabled(true); //Volvemos a activar el botón para poder hacer otro ciclo
                        }
                        else{ //Si no es la última serie
                            cicloTrabajo(numeroSeries-1,segundosTrabajo,segundosDescanso); //Llamamos al método con 1 serie menos que antes
                        }

                    }
                }.start();
            }
        }.start();
    }
}
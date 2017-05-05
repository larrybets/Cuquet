package cat.flx.cuquet;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;

// implementamos SensorEventlistener para capturar el movimiento y nos crea 2 metodos onSensorChanged (en este metodo metemos las posiciones de rotacion) y onAccuracyChanged

public class MainActivity extends AppCompatActivity implements CuquetView.CuquetViewListener, SensorEventListener {

    private CuquetView cuquetView;
    private TextView tvScore;

    //Imagen
    private ImageView game;

    //sensores
    private Sensor sensor;
    private SensorManager sensorManager;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        cuquetView = (CuquetView) findViewById(R.id.cuquetView);
        Button btnNewGame = (Button) findViewById(R.id.btnNewGame);
        tvScore = (TextView) findViewById(R.id.tvScore);
        btnNewGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvScore.setText("0");
                game.setVisibility(View.INVISIBLE);
                cuquetView.newGame();
            }
        });
        cuquetView.setCuquetViewListener(this);
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(sensor.TYPE_ACCELEROMETER);

        // en vez de que aparezca un texto (TOAST) al perder le meteremos una imagen la cual enlazamos aqui
        // y en el metodo gamelost mostramos la iamgen cuando pierda
       game = (ImageView) findViewById(R.id.imagenPerdedor);
       game.setVisibility(View.INVISIBLE);
    }


    @Override
    public void onResume() {
        super.onResume();
        sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL);
    }
    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        switch (event.getKeyCode()) {
            case KeyEvent.KEYCODE_A:
                cuquetView.update(0, +10);
                break;
            case KeyEvent.KEYCODE_Q:
                cuquetView.update(0, -10);
                break;
            case KeyEvent.KEYCODE_O:
                cuquetView.update(-10, 0);
                break;
            case KeyEvent.KEYCODE_P:
                cuquetView.update(+10, 0);
                break;
        }
        return super.dispatchKeyEvent(event);
    }


    @Override public void scoreUpdated(View view, int score) {
        tvScore.setText(Integer.toString(score));
    }


    @Override
    public void gameLost(View view) {
        game.setVisibility(View.VISIBLE);
    }


    @Override
    public void onSensorChanged(SensorEvent eventfinal) {
        // las posicionesde la rotacion
        float a = -eventfinal.values[0];
        float b = eventfinal.values[1];
        cuquetView.update(a, b);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {}
}

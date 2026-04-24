package dam.projecte.pj6_2;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.SeekBar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Juego extends AppCompatActivity {


    private String LOG = "dam.projecte.pj6_2";
    private boolean isReproduint= true;
    private Intent intent;

    private long tiempoInicio;
    private boolean juegoFinalizado = false;

    private String nombreDelJugador;

    private FloatingActionButton fab;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        nombreDelJugador = getIntent().getStringExtra("PLAYER_NAME");

        intent= new Intent(this, AudioIntentService.class);
        intent.putExtra("operacio", "inici");
        startService(intent);

        setContentView(R.layout.juego_principal);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Insertando vista personalizada con canvas
        VistaTablero vista = findViewById(R.id.vista);
        vista.setCadena("");

        tiempoInicio = System.currentTimeMillis();
        juegoFinalizado = false;

        fab = findViewById(R.id.fab);
        fab.setImageResource(android.R.drawable.ic_media_pause);

        //Cambios de estado del FloatingActionButton
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alternarAudio();
            }
        });


    }

    public void verificarEstadoVictoria() {
        if (JuegoIntentService.juegoGanado&& !juegoFinalizado) {
            juegoFinalizado = true; // Bloqueamos para que solo se calcule una vez

            // Calcular tiempo transcurrido
            long tiempoFin = System.currentTimeMillis();
            long segundosTotales = (tiempoFin - tiempoInicio) / 1000;

            long minutos = segundosTotales / 60;
            long segundos = segundosTotales % 60;

            String mensajeTiempo = String.format("¡Felicidades %s Ganaste en %02d:%02d!",nombreDelJugador, minutos, segundos);

            intent.putExtra("operacio", "pausa");
            startService(intent);

            Snackbar.make(fab, mensajeTiempo, Snackbar.LENGTH_INDEFINITE)
                    .setAction("CERRAR", v -> {
                        JuegoIntentService.reiniciarJuego();

                        // FORZAR A LA VISTA A REDIBUJARSE (Estará vacía ahora)
                        VistaTablero vista = findViewById(R.id.vista);

                        vista.invalidate();
                        enviarDatosAFirebase(nombreDelJugador, segundosTotales);
                        finish();
                    })
                    .show();

            stopService(new Intent(this, AudioIntentService.class));

        }
    }

    private void alternarAudio() {
        String text;

        if (isReproduint) {
            text = "Pausant Audio";
            fab.setImageResource(android.R.drawable.ic_media_play);
            intent.putExtra("operacio", "pausa");
            isReproduint = false;
        } else {
            text = "Reproduint Audio";
            fab.setImageResource(android.R.drawable.ic_media_pause);
            intent.putExtra("operacio", "inici");
            isReproduint = true;
        }

        startService(intent);

        Snackbar.make(fab, text, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_toggle_theme) {

            alternarAudio();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public static class Puntuacion {
        public String nombre;
        public long tiempoSegundos;

        public Puntuacion() {} // Necesario para Firebase

        public Puntuacion(String nombre, long tiempoSegundos) {
            this.nombre = nombre;
            this.tiempoSegundos = tiempoSegundos;
        }
    }

    private void enviarDatosAFirebase(String nombre, long tiempo) {
        // 1. Obtener referencia a la base de datos
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("ranking");

        // 2. Crear un ID único para cada entrada
        String id = myRef.push().getKey();

        // 3. Crear el objeto con los datos
        Puntuacion p = new Puntuacion(nombre, tiempo);

        // 4. Subir la información
        if (id != null) {
            myRef.child(id).setValue(p);
        }
    }
}
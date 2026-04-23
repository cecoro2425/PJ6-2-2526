package dam.projecte.pj6_2;

import android.content.Intent;
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

public class Juego extends AppCompatActivity {


    private String LOG = "dam.projecte.pj6_2";
    private boolean isReproduint= true;
    private Intent intent;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        intent= new Intent(this, AudioIntentService.class);
        intent.putExtra("operacio", "inici");
        startService(intent);

        setContentView(R.layout.juego_principal);

        //Insertando vista personalizada con canvas
        VistaTablero vista = findViewById(R.id.vista);
        vista.setCadena("");


        final FloatingActionButton fab = findViewById(R.id.fab);
        fab.setImageResource(android.R.drawable.ic_media_pause);



        //Cambios de estado del FloatingActionButton
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text;

                if (isReproduint) {
                    text = "Pausant Audio";
                    fab.setImageResource(android.R.drawable.ic_media_play);
                    intent.putExtra("operacio", "pausa");
                    startService(intent);

                    isReproduint = false;
                } else {
                    text = "Reproduint Audio";
                    fab.setImageResource(android.R.drawable.ic_media_pause);
                    intent.putExtra("operacio", "inici");
                    startService(intent);

                    isReproduint = true;
                }
                Snackbar.make(view, text, Snackbar.LENGTH_LONG).show();
            }
        });



    }

}
package dam.projecte.pj6_2;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;

public class MenuInicio extends AppCompatActivity {

    private TextView tvRanking;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.menu_inicio);

        tvRanking = findViewById(R.id.tvRanking);
        cargarUltimosRegistros();

        final TextView titulo = (TextView) findViewById(R.id.tituloInicio);


        final AnimatorSet animacio = (AnimatorSet) AnimatorInflater.loadAnimator(this, R.animator.animacio);

        animacio.setTarget(titulo);

        animacio.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator a) {
                super.onAnimationEnd(a);
                animacio.start();
            }
        });

        animacio.start();
    }

    public void iniciarJuego(View view) {
        EditText inputNombre = findViewById(R.id.etNombre);
        String nombre = inputNombre.getText().toString();

        if (nombre.trim().isEmpty()) {
            nombre = "Jugador Anónimo";
        }

        Intent intent = new Intent(this, Juego.class);
        intent.putExtra("PLAYER_NAME", nombre);
        startActivity(intent);
    }

    private void cargarUltimosRegistros() {
        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("ranking");

        // Creamos una consulta: limita a los últimos 5 registros
        Query ultimosCinco = myRef.limitToLast(5);

        ultimosCinco.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                StringBuilder builder = new StringBuilder();
                builder.append("ÚLTIMOS RÉCORDS:\n\n");

                // Creamos una lista para invertir el orden (para que el más nuevo salga arriba)
                ArrayList<String> listaResultados = new ArrayList<>();

                for (DataSnapshot data : snapshot.getChildren()) {
                    // Usamos la misma clase Puntuacion que definimos en Juego
                    Juego.Puntuacion p = data.getValue(Juego.Puntuacion.class);
                    if (p != null) {
                        String fila = p.nombre + " - " + p.tiempoSegundos + "s";
                        listaResultados.add(fila);
                    }
                }

                // Invertimos para que el último sea el primero en la lista visual
                Collections.reverse(listaResultados);

                for (String s : listaResultados) {
                    builder.append(s).append("\n");
                }

                if (listaResultados.isEmpty()) {
                    tvRanking.setText("Aún no hay partidas registradas.");
                } else {
                    tvRanking.setText(builder.toString());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                tvRanking.setText("Error al cargar ranking.");
            }
        });
    }


}
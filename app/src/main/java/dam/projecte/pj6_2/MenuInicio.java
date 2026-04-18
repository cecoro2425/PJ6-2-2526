package dam.projecte.pj6_2;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class MenuInicio extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.menu_inicio);

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
        Intent intent = new Intent(this, Juego.class);
        startActivity(intent);
    }
}
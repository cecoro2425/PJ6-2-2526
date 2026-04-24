package dam.projecte.pj6_2;

import android.app.IntentService;
import android.content.Intent;

import androidx.annotation.Nullable;



public class JuegoIntentService extends IntentService {

    public static boolean[][] tablero = new boolean[5][5];

    public static boolean juegoGanado = false;

    public JuegoIntentService() {
        super("JuegoIntentService");
    }



    @Override
    public int onStartCommand(@Nullable Intent intent, int flags, int startId) {
        if (intent != null) {
            int fila = intent.getIntExtra("fila", -1);
            int col = intent.getIntExtra("col", -1);

            if (fila >= 0 && fila < 5 && col >= 0 && col < 5) {
                tablero[fila][col] = !tablero[fila][col];

                juegoGanado = comprobarVictoria();

            }
        }
        return START_NOT_STICKY;
    }

    // Lógica optimizada para revisar el tablero
    private boolean comprobarVictoria() {
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                if (!tablero[i][j]) {
                    return false; // Si hay un false, no ha ganado
                }
            }
        }
        return true; // Si recorre todo y no hay false, victoria
    }



    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
    }

    // Método extra opcional para reiniciar el juego
    public static void reiniciarJuego() {
        tablero = new boolean[5][5];
        juegoGanado = false;
    }
}


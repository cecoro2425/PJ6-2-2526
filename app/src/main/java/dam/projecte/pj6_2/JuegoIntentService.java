package dam.projecte.pj6_2;

import android.app.IntentService;
import android.content.Intent;

import androidx.annotation.Nullable;

public class JuegoIntentService extends IntentService {
    public static boolean[][] tablero = new boolean[5][5];

    public JuegoIntentService() {
        super("JuegoIntentService");
    }

    @Override
    public int onStartCommand(@Nullable Intent intent, int flags, int startId) {
        if (intent != null) {
            int fila = intent.getIntExtra("fila", -1);
            int col = intent.getIntExtra("col", -1);

            if (fila != -1 && col != -1) {
                // Cambia el estado falso / verdadero
                tablero[fila][col] = !tablero[fila][col];
            }
        }
        return START_NOT_STICKY;
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

    }
}
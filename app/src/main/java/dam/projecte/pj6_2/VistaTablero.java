package dam.projecte.pj6_2;

import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;

class VistaTablero extends View
{
    private float x, y;
    private String cadena;


    private int filas = 5;
    private int columnas = 5;
    private float padding = 10;
    private float tamanoCuadrado;
    private float marginTop;

    private int color;
    public VistaTablero(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.VistaTablero,
                0, 0);

        try {
            x = a.getFloat(R.styleable.VistaTablero_x, 0);
            cadena = a.getString(R.styleable.VistaTablero_cadena);

        } finally {
            a.recycle();
        }
    }

    //Función para cambiar el color de los bordes dependiendo el tema
    private int getThemeColor(int attr) {
        TypedValue typedValue = new TypedValue();
        if(getContext().getTheme().resolveAttribute(attr, typedValue, true)){
            return typedValue.data;
        }
        return Color.BLACK;
    }



    @Override
    protected void onDraw(Canvas canvas) {
        /* cuadro de canvas - Prueba funcionamiento

        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(color);
        canvas.drawRect(x, y, 450  , 600, paint);

        paint.setColor(Color.BLACK);
        paint.setTextSize(60);
        canvas.drawText(cadena, 40, 250, paint);
        */

        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);

        int colorContorno = getThemeColor(R.attr.colorContorno);

        tamanoCuadrado = (getWidth() - (padding * (columnas + 1))) / columnas;

        float altoTablero = (tamanoCuadrado + padding) * filas;
        marginTop = (getHeight() - altoTablero) / 2;

        for (int fila = 0; fila < filas; fila++) {

            for (int col = 0; col < columnas; col++) {

                float posX = padding + col * (tamanoCuadrado + padding);
                float posY = padding + marginTop + fila * (tamanoCuadrado + padding);

                // Dibujo de cuadros
                //paint.setStyle(Paint.Style.FILL);
                //paint.setColor(Color.RED);
                //canvas.drawRect(posX, posY, posX + tamanoCuadrado, posY + tamanoCuadrado, paint);

                // Consulta de prueba a JuegoIntentService
                if (JuegoIntentService.tablero != null && JuegoIntentService.tablero[fila][col]) {
                    android.util.Log.d("DEBUG_TABLERO", "Dibujando casilla llena en: " + fila + "," + col);
                    paint.setStyle(Paint.Style.FILL);
                    paint.setColor(Color.RED);
                    canvas.drawRect(posX, posY, posX + tamanoCuadrado, posY + tamanoCuadrado, paint);
                }

                // Dibujamos de bordes
                paint.setStyle(Paint.Style.STROKE);
                //paint.setColor(Color.BLACK); color por defecto
                paint.setColor(colorContorno);
                paint.setStrokeWidth(5);
                canvas.drawRect(posX, posY, posX + tamanoCuadrado, posY + tamanoCuadrado, paint);

                // Dibujo de texto en cuadros (opcional)
                if (cadena != null) {
                    paint.setStyle(Paint.Style.FILL);
                    paint.setTextSize(tamanoCuadrado / 3);
                    paint.setTextAlign(Paint.Align.CENTER);
                    paint.setColor(colorContorno);

                    // Centrar el texto
                    float textX = posX + tamanoCuadrado / 2;
                    float textY = posY + (tamanoCuadrado / 2) - ((paint.descent() + paint.ascent()) / 2);
                    canvas.drawText(cadena, textX, textY, paint);
                }
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {

            int col = (int) ((event.getX() - padding) / (tamanoCuadrado + padding));
            int fila = (int) ((event.getY() - marginTop - padding) / (tamanoCuadrado + padding));

            Intent intent = new Intent(getContext(), JuegoIntentService.class);
            intent.putExtra("fila", fila);
            intent.putExtra("col", col);
            getContext().startService(intent);

            postDelayed(new Runnable() {
                @Override
                public void run() {
                    invalidate();
                }
            }, 50);
            return true;
        }
        return super.onTouchEvent(event);
    }

    public String getCadena() {
        return cadena;
    }

    public void setCadena(String cadena) {
        this.cadena = cadena;
    }

    @Override
    public float getX() {
        return x;
    }

    @Override
    public void setX(float x) {
        this.x = x;
    }

    @Override
    public float getY() {
        return y;
    }

    @Override
    public void setY(float y) {
        this.y = y;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }



}
/*
 * List Diary, an app of management of lists with definable fields
 * and of tasks with a record of its changes.
 * Copyright (C) 2020 Raul Martin Perez
 *
 * This file is part of List Diary.
 *
 * List Diary is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published
 * by the Free Software Foundation, either version 3 of the License,
 * or (at your option) any later version.
 *
 * List Diary is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty
 * of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See
 * the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with List Diary. If not, see <http://www.gnu.org/licenses/>.
 */

package com.rmartinper.listdiary;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

/**
 * A class that manages the Splash Screen.
 *
 * @author Raul Martin Perez (raumar95@gmail.com)
 */
public class SplashScreen extends AppCompatActivity {

    /**
     * The indicator that determines if you have pressed the Back button to exit.
     */
    private boolean doubleBackToExit = false; //Guarda el indicador del botón atrás pulsado para salir.

    /**
     * The Handler of the timeouts of the Splash Screen.
     */
    private Handler timeoutHandler = new Handler(Looper.getMainLooper()); //Guarda el manejador de los tiempos de espera.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState); //Inicializa todos los fragmentos de la pantalla de bienvenida.
        setContentView(R.layout.screen_splash); //Establece la vista del contenido de la pantalla de bienvenida.

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M //Si la version SDK de la compilacion actual del sistema es mayor que Marshmallow.
                && checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) { //Y no tiene permiso concedido para escribir en el almacenamiento externo.
            requestPermissions(new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE}, 100); //Solicita permiso para escribir en el almacenamiento externo con código 100.
        } else {
            timeoutHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent main = new Intent(SplashScreen.this, MainActivity.class); //Crea una intención de pasar de la pantalla de bienvenida a la actividad principal y la guarda.
                    startActivity(main); //Inicia la actividad principal.
                    finish(); //Finaliza la pantalla de bienvenida.
                }
            }, 5000); //Agrega el ejecutable al manejador para ejecutarlo pasados 5 segundos.
        }
    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExit) { //Si se ha pulsado el botón atras para salir.
            super.onBackPressed(); //Vuelve a la actividad anterior o cierra la aplicación si no hay.
            timeoutHandler.removeCallbacksAndMessages(null); //Elimina todas las devoluciones de llamada y mensajes del manejador.
        } else { //Si no se ha pulsado.
            doubleBackToExit = true; //Establece que el botón atrás ha sido pulsado para salir.
            Toast.makeText(this, R.string.press_again_exit, Toast.LENGTH_SHORT).show(); //Crea un mensaje de duración corta de pulsar de nuevo para salir y lo muestra.

            timeoutHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    doubleBackToExit = false; //Establece que el botón atrás no ha sido pulsado para salir.
                }
            }, 2000); //Agrega el ejecutable al manejador para ejecutarlo pasados 2 segundos.
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 100) { //Si el código de solicitud es 100.
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) { //Si el primer resultado de la concesión es permiso concedido.
                Toast.makeText(this, R.string.perm_granted, Toast.LENGTH_SHORT).show(); //Crea un mensaje de duración corta de permiso concedido y lo muestra.
                Intent main = new Intent(SplashScreen.this, MainActivity.class); //Crea una intención de pasar de la pantalla de bienvenida a la actividad principal y la guarda.
                startActivity(main); //Inicia la actividad principal.
            } else { //Si no es permiso concedido.
                Toast.makeText(this, R.string.perm_denied, Toast.LENGTH_SHORT).show(); //Crea un mensaje de duración corta de permiso denegado y lo muestra.
            }

            finish(); //Finaliza la pantalla de bienvenida.
        } else { //Si no es 100.
            super.onRequestPermissionsResult(requestCode, permissions, grantResults); //Recibe los resultados de la solicitud de permisos.
        }
    }
}

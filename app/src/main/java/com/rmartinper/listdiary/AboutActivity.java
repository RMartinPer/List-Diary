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

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

/**
 * A class that manages the About Activity.
 *
 * @author Raul Martin Perez (raumar95@gmail.com)
 */
public class AboutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState); //Inicializa todos los fragmentos de la actividad de acerca de.
        setContentView(R.layout.activity_about); //Establece la vista del contenido de la actividad de acerca de.

        TextView tvVersion = findViewById(R.id.about_version); //Busca la vista del texto de la versión y la guarda.
        TextView tvEmail = findViewById(R.id.about_email); //Busca la vista del texto del correo electrónico y la guarda.
        TextView tvPrivacy = findViewById(R.id.about_privacy); //Busca la vista del texto de la política de privacidad y la guarda.
        LinearLayout layoutIcons8 = findViewById(R.id.about_icons8); //Busca el diseño del Icons8 y lo guarda.

        ActionBar actionBar = getSupportActionBar(); //Guarda la barra de acción de soporte.

        if (actionBar != null) {  //Si la barra de acción no es un valor nulo.
            actionBar.setDisplayHomeAsUpEnabled(true); //Establece que el inicio se muestra como arriba.
            actionBar.setHomeButtonEnabled(true); //Establece que el botón de inicio está habilitado.
        }

        String versionName = BuildConfig.VERSION_NAME; //Guarda el nombre de versión de la configuración de la compilación.
        String version = getString(R.string.about_version, versionName); //Obtiene la cadena de la versión con el nombre como atributo y la guarda.

        tvVersion.setText(version); //Establece el texto de la vista de la versión.

        tvEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                aboutEmail(); //Inicia el selector de correos electrónicos para mandar uno.
            }
        }); //Establece el escuchador de clics de la vista de correo electrónico.

        layoutIcons8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                aboutIcons8(); //Inicia el selector de navegadores para abrir el enlace Icons8.
            }
        }); //Establece el escuchador de clics del diseño de Icons8.

        tvPrivacy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                aboutPrivacy(); //Inicia el selector de navegadores para abrir la Política de Privacidad.
            }
        }); //Establece el escuchador de clics de la vista de la Política de Privacidad.
    }

    /**
     * Opens the email selector to send one.
     */
    private void aboutEmail() {
        String email = getString(R.string.about_email); //Obtiene la cadena del correo electrónico y la guarda.
        String[] recipients = { email }; //Guarda el correo electrónico como el arreglo de los recipientes.

        Intent emailIntent = new Intent(Intent.ACTION_SENDTO); //Crea una intención de una actividad de "Enviar a" y la guarda.
        emailIntent.setData(Uri.parse("mailto:")); //Establece los datos de la intención como "Enviar un correo electrónico a".
        emailIntent.putExtra(Intent.EXTRA_EMAIL, recipients); //Pone en la intención datos extra del correo electrónico como los recipientes.

        try { //Trata de hacer el contenido del cuerpo.
            String chooser = getString(R.string.about_chooser); //Obtiene la cadena del selector y la guarda.
            startActivity(Intent.createChooser(emailIntent, chooser)); //Inicia una actividad de un selector de correos elecrónicos.
        } catch (ActivityNotFoundException ex) { //Si se produce la excepción de acividad no encontrada.
            Toast.makeText(this, R.string.about_no_email, Toast.LENGTH_SHORT).show(); //Crea un mensaje de duración corta de no correo electrónico y lo muestra.
        }
    }

    /**
     * Opens the Icons8 link in a browser.
     */
    private void aboutIcons8() {
        String icons8 = getString(R.string.icons8_link); //Obtiene la cadena del enlace de Icons8 y la guarda.

        Intent browserIntent = new Intent(Intent.ACTION_VIEW); //Crea una intención de una actividad de Vista y la guarda.
        browserIntent.setData(Uri.parse(icons8)); //Establece los datos de la intención como el enlace de Icons8.

        try { //Trata de hacer el contenido del cuerpo.
            String chooser = getString(R.string.icons8_chooser); //Obtiene la cadena del selector y la guarda.
            startActivity(Intent.createChooser(browserIntent, chooser)); //Inicia una actividad de un selector de navegadores.
        } catch (ActivityNotFoundException ex) { //Si se produce la excepción de acividad no encontrada.
            Toast.makeText(this, R.string.about_no_browser, Toast.LENGTH_SHORT).show(); //Crea un mensaje de duración corta de no navegador y lo muestra.
        }
    }

    /**
     * Opens the Privacy Policy link in a browser.
     */
    private void aboutPrivacy() {
        String privacy = getString(R.string.privacy_link); //Obtiene la cadena del enlace de la Política de Privacidad y la guarda.

        Intent browserIntent = new Intent(Intent.ACTION_VIEW); //Crea una intención de una actividad de Vista y la guarda.
        browserIntent.setData(Uri.parse(privacy)); //Establece los datos de la intención como el enlace de la Política de Privacidad.

        try { //Trata de hacer el contenido del cuerpo.
            String chooser = getString(R.string.privacy_chooser); //Obtiene la cadena del selector y la guarda.
            startActivity(Intent.createChooser(browserIntent, chooser)); //Inicia una actividad de un selector de navegadores.
        } catch (ActivityNotFoundException ex) { //Si se produce la excepción de acividad no encontrada.
            Toast.makeText(this, R.string.about_no_browser, Toast.LENGTH_SHORT).show(); //Crea un mensaje de duración corta de no navegador y lo muestra.
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) { //Si el identificador del elemento es igual de inicio.
            finish(); //Finaliza la actividad de acerca de.
            return true; //Devuelve verdadero.
        }
        return super.onOptionsItemSelected(item); //Devuelve el resultado de que un elemento de opciones sea seleccionado.
    }
}

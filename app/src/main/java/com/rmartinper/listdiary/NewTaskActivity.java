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

import android.app.AlertDialog;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.InputType;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.TooltipCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.util.Pair;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Locale;

/**
 * A class that manages the New Task Activity.
 *
 * @author Raul Martin Perez (raumar95@gmail.com)
 */
public class NewTaskActivity extends AppCompatActivity {

    /**
     * The array of the Tasks titles.
     */
    private String[] titles = new String[0]; //Guarda la matriz de los títulos de las tareas.

    /**
     * The path of the Lists file.
     */
    private String listsPath; //Inicializa la ruta del archivo listas.

    /**
     * The path of the Tasks file.
     */
    private String tasksPath; //Inicializa la ruta del archivo tareas.

    /**
     * The ArrayList of the Fields.
     */
    private ArrayList<Field> fields; //Inicializa la matriz de lista de campos.

    /**
     * The ArrayList of the Values.
     */
    private ArrayList<EditText> values = new ArrayList<>(); //Guarda la matriz de lista de valores.

    /**
     * The EditText of the title.
     */
    private EditText etNewTitle; //Inicializa el texto de edición del título.

    /**
     * The EditText of the Description.
     */
    private EditText etNewDesc; //Inicializa el texto de edición de la descripción.

    /**
     * The EditText of the start date.
     */
    private EditText etNewStart; //Inicializa el texto de edición de la fecha de inicio.

    /**
     * The Map of the Tasks.
     */
    private LinkedHashMap<Integer, Task> tasks = new LinkedHashMap<>(); //Guarda el mapa de las tareas.

    /**
     * The current List.
     */
    private List currentList; //Inicializa la lista actual.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState); //Inicializa todos los fragmentos de la actividad de nueva tarea.
        setContentView(R.layout.activity_new_task); //Establece la vista del contenido de la actividad de nueva tarea.

        listsPath = getExternalFilesDir(null) + "/lists.xml"; //Guarda la ruta del archivo listas en el almacenamiento externo.
        tasksPath = getExternalFilesDir(null) + "/tasks.xml"; //Guarda la ruta del archivo tareas en el almacenamiento externo.

        etNewTitle = findViewById(R.id.et_title_new_task); //Busca el texto de edición del título de la nueva tarea y lo guarda.
        etNewDesc = findViewById(R.id.et_desc_new_task); //Busca el texto de edición de la descipción de la nueva tarea y lo guarda.
        etNewStart = findViewById(R.id.et_start_new_task); //Busca el texto de edición del inicio de la nueva tarea y lo guarda.
        FloatingActionButton fabNewCancel = findViewById(R.id.fab_cancel_new_task); //Busca el botón de acción flotante de cancelar nueva tarea y lo guarda.
        FloatingActionButton fabNewAccept = findViewById(R.id.fab_accept_new_task); //Busca el botón de acción flotante de aceptar nueva tarea y lo guarda.

        etNewTitle.setRawInputType(InputType.TYPE_CLASS_TEXT); //Establece el tipo de entrada del texto del título como texto.
        etNewDesc.setRawInputType(InputType.TYPE_CLASS_TEXT); //Establece el tipo de entrada del texto de la descripción como texto.

        etNewStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utils.getDate(NewTaskActivity.this, view); //Abre un diálogo de selector de fechas para obtener una y escribirla.
            }
        }); //Establece el escuchador de clics del texto del inicio.

        fabNewCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAddTask(true); //Muestra el diálogo de agregar tarea para elegir si salir de la nueva tarea o no.
            }
        }); //Establece el escuchador de clics del botón de acción flotante de cancelar nueva tarea.

        fabNewAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAddTask(false); //Muestra el diálogo de agregar tarea para elegir si guardar la nueva tarea o no.
            }
        }); //Establece el escuchador de clics del botón de acción flotante de aceptar nueva tarea.

        TooltipCompat.setTooltipText(fabNewCancel, getString(R.string.hint_cancel_task)); //Establece el texto de la información de herramienta del botón de acción flotante de cancelar nueva tarea.
        TooltipCompat.setTooltipText(fabNewAccept, getString(R.string.hint_accept_task)); //Establece el texto de la información de herramienta del botón de acción flotante de aceptar nueva tarea.

        Serializable list = getIntent().getSerializableExtra("list"); //Obtiene el serializable de la intención list y lo guarda.

        if (list != null) { //Si la lista no es un valor nulo.
            currentList = (List) list; //Adapta la lista y la guarda como la actual.

            showFields(); //Muestra los campos.
            titles = Utils.parseTasks(listsPath, currentList, tasksPath, tasks); //Analiza las tareas y guarda sus títulos.
        }
    }

    /**
     * Shows the Fields in the activity.
     */
    protected void showFields() {
        fields = currentList.getFields(); //Obtiene los campos de la lista actual y los guarda.

        if (!fields.isEmpty()) { //Si los campos no están vacíos.
            LinearLayout layoutValues = findViewById(R.id.layout_values_new_task); //Busca el diseño de los valores de la nueva tarea y lo guarda.
            LinearLayout layoutDefined = findViewById(R.id.layout_defined_new_task); //Busca el diseño de la cabecera definida de la nueva tarea y lo guarda.
            layoutDefined.setVisibility(View.VISIBLE); //Establece la visibilidad del diseño de la cabecera definida como visible.

            Typeface tf = ResourcesCompat.getFont(NewTaskActivity.this, R.font.aller_bold); //Obtiene la fuente negrita de los recursos y la guarda.

            for (Field field: fields) { //Por cada campo de los campos.
                LinearLayout layout = new LinearLayout(NewTaskActivity.this); //Crea un nuevo diseño linear y lo guarda.
                TextView tv = new TextView(NewTaskActivity.this); //Crea una nueva vista de texto y la guarda.
                EditText et; //Inicializa el texto de edición.

                float layoutDp = 16f; //Guarda los píxeles de densidad independiente del diseño como 16.
                int layoutPx = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, layoutDp, getResources().getDisplayMetrics()); //Convierte los píxeles de densidad del diseño a píxeles y los guarda.

                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT); //Crea unos nuevos parámetros del diseño y los guarda.
                layoutParams.setMargins(0, layoutPx, 0, 0); //Establece los márgenes de los parámetros del diseño.

                layout.setLayoutParams(layoutParams); //Establece los parámetros del diseño.
                layout.setOrientation(LinearLayout.HORIZONTAL); //Establece la orientación del diseño como horizontal.

                layoutValues.addView(layout); //Agrega la vista del diseño al de los valores.

                String name = field.getName(); //Obtiene el nombre del campo y lo guarda.
                String type = field.getType(); //Obtiene el tipo del campo y lo guarda.
                String hint = getString(R.string.hint_task_field_header) + " " + name.toLowerCase(); //Obtiene la cadena de pista del campo y la guarda junto con el nombre en minúsculas.

                name += ":"; //Agrega los dos puntos al nombre.

                float tvWidthDp = 110f; //Guarda los píxeles de densidad independiente de la anchura de la vista de texto como 110.
                float tvMarginDp = 10f; //Guarda los píxeles de densidad independiente del margen de la vista de texto como 10.
                int tvWidthPx = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, tvWidthDp, getResources().getDisplayMetrics()); //Convierte los píxeles de densidad de la anchura de la vista de texto a píxeles y los guarda.
                int tvMarginPx = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, tvMarginDp, getResources().getDisplayMetrics()); //Convierte los píxeles de densidad del margen de la vista de texto a píxeles y los guarda.

                LinearLayout.LayoutParams tvParams = new LinearLayout.LayoutParams(tvWidthPx, LinearLayout.LayoutParams.WRAP_CONTENT); //Crea unos nuevos parámetros de diseño de la vista de texto y los guarda.
                tvParams.gravity = Gravity.CENTER_VERTICAL; //Establece la gravedad de los parámetros de la vista de texto como centro vertical
                tvParams.setMargins(0, 0, tvMarginPx, 0); //Establece los márgenes de los parámetros de la vista de texto.

                tv.setLayoutParams(tvParams); //Establece los parámetros del diseño de la vista de texto.
                tv.setTypeface(tf); //Establece el tipo de letra de la vista de texto.
                tv.setText(name); //Establece el texto de la vista de texto.
                tv.setTextColor(Color.BLACK); //Establece el color del texto de la vista de texto como negro.
                tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18); //Establece el tamaño del texto de la vista de texto como 18.

                switch (type) { //Considera el tipo.
                    case "Number": //Tipo número.
                        et = (EditText) View.inflate(NewTaskActivity.this, R.layout.value_editnumber, null); //Infla un texto de edición de números para el valor y lo guarda.
                        et.setRawInputType(InputType.TYPE_CLASS_PHONE); //Establece el tipo de entrada del texto de edición como teléfono.
                        break; //Corte.
                    case "Date": //Tipo fecha.
                        et = (EditText) View.inflate(NewTaskActivity.this, R.layout.value_editdate, null); //Infla un texto de edición de números para la fecha y lo guarda.
                        et.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Utils.getDate(NewTaskActivity.this, view); //Abre un diálogo de selector de fechas para obtener una y escribirla.
                            }
                        }); //Establece el escuchador de clics del texto de edición.
                        break; //Corte.
                    default: //Si no es ninguno de los anteriores.
                        et = (EditText) View.inflate(NewTaskActivity.this, R.layout.value_edittext, null); //Infla un texto de edición de números para el texto y lo guarda.
                        et.setRawInputType(InputType.TYPE_CLASS_TEXT); //Establece el tipo de entrada del texto de edición como texto.
                        break; //Corte.
                }

                et.setHint(hint); //Establece la pista del texto de edición.
                et.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1)); //Establece los parámetros del diseño del texto de edición.

                values.add(et); //Agrega el texto de edición a los valores.

                layout.addView(tv); //Agrega la vista del texto al diseño.
                layout.addView(et); //Agrega el texto de edición al diseño.
            }
        }
    }

    /**
     * Shows the add Task dialog to choose whether to cancel or accept the new Task.
     *
     * @param cancel the indicator of whether or not it is the cancel dialog.
     */
    protected void showAddTask(boolean cancel) {
        View addHeader = View.inflate(NewTaskActivity.this, R.layout.custom_dialog_header, null); //Infla la vista de la cabecera de adición y la guarda.

        ImageView addIcon = addHeader.findViewById(R.id.dialog_icon); //Busca el icono de la cabecera de adición y lo guarda.
        addIcon.setImageResource(R.drawable.ic_plus_blue_32dp); //Establece el recurso de la imagen del icono de adición.

        TextView addTitle = addHeader.findViewById(R.id.dialog_title); //Busca el título de la cabecera de adición y lo guarda.
        addTitle.setText(R.string.add_task_header); //Establece el texto del título de adición.

        AlertDialog.Builder addBuilder = new AlertDialog.Builder(NewTaskActivity.this, 3); //Crea una nuevo constructor de diálogo de alerta y lo guarda.
        addBuilder.setCustomTitle(addHeader); //Establece el título personalizado del constructor de adición como la cabecera.

        View addContent = View.inflate(NewTaskActivity.this, R.layout.custom_dialog_content, null); //Infla la vista del contenido de adición y la guarda.

        final AlertDialog addDialog = addBuilder.create(); //Crea un diálogo de alerta con el constructor de adición y lo guarda.
        addDialog.setView(addContent); //Establece la vista del díalogo de adición como el contenido.

        TextView addText = addContent.findViewById(R.id.custom_info); //Busca la información del contenido de adición y lo guarda.
        Button acceptBtn = addContent.findViewById(R.id.custom_accept); //Busca el botón de aceptar del contenido de adición y lo guarda.

        if (cancel) { //Si es el diálogo para cancelar.
            addText.setText(R.string.are_sure_cancel_new_task); //Establece el texto de adición como el de cancelar.

            acceptBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    addDialog.dismiss(); //Cierra el diálogo de adición.
                    Utils.backMain(NewTaskActivity.this); //Regresa a la actividad principal.
                }
            }); //Establece el escuchador de clics del botón de aceptar del contenido de adición.
        } else { //Si no lo es.
            addText.setText(R.string.are_sure_accept_new_task); //Establece el texto de adición como el de aceptar.

            acceptBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    addDialog.dismiss(); //Cierra el diálogo de adición.
                    saveNewTask(); //Guarda la nueva tarea.
                }
            }); //Establece el escuchador de clics del botón de aceptar del contenido de adición.
        }

        addContent.findViewById(R.id.custom_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addDialog.dismiss(); //Cierra el diálogo de adición.
            }
        }); //Establece el escuchador de clics del botón de cancelar del contenido de adición.

        Utils.hideKeyboard(NewTaskActivity.this); //Oculta el teclado mostrado en esta actividad.

        addDialog.show(); //Muestra el diálogo de adición.
    }

    /**
     * Gets the start date of the new Task and returns it.
     *
     * @return the start date of the new Task.
     */
    protected Date getTaskStart() {
        String taskStart = etNewStart.getText().toString(); //Obtiene la cadena del texto de edición del inicio y la guarda.

        SimpleDateFormat shortDate = new SimpleDateFormat("dd/MM/yyyy", Locale.US); //Crea un formato de fechas simples corto y lo guarda.
        Date taskDate = new Date(); //Guarda la fecha actual como la fecha de la tarea.

        if (!taskStart.isEmpty()) { //Si el inicio de la tarea no está vacío.
            try { //Trata de hacer el contenido del cuerpo.
                taskDate = shortDate.parse(taskStart); //Analiza el inicio de la tarea como una fecha y la guarda como la de la tarea.
            } catch (ParseException e) { //Si se produce una excepción de análisis.
                Toast.makeText(getApplicationContext(), "ParseException: " + e.getLocalizedMessage(), Toast.LENGTH_SHORT).show(); //Crea un mensaje de duración corta del mensaje localizado de la excepción y lo muestra.
            }
        }

        return taskDate; //Devuelve la fecha de la tarea.
    }

    /**
     * Gets the title of the new Task and returns it along with an indicator of whether it is valid.
     *
     * @return the name of the new Task and the indicator of whether it is valid.
     */
    protected Pair<String, Boolean> getTaskTitle() {
        String taskTitle = etNewTitle.getText().toString(); //Obtiene la cadena del texto de edición del título y la guarda.
        boolean titleValid = true; //Guarda el indicador de que el título es válido como verdadero.

        if (taskTitle.isEmpty()) { //Si el nuevo título está vacío.
            taskTitle = getString(R.string.no_title); //Obtiene la cadena sin título y la guarda como el nuevo título.
            int cont = 1; //Guarda el contador como 1.

            java.util.List<String> titlesList = Arrays.asList(titles); //Convierte la matriz de títulos a una lista y la guarda.

            do { //Hace el contenido del cuerpo.
                titleValid = true; //Establece que el título es válido.

                if (titlesList.contains(taskTitle)) { //Si la lista de títulos contiene el título de la tarea.
                    titleValid = false; //Establece que el título no es válido.
                    taskTitle = getString(R.string.no_title) + " (" + cont + ")"; //Obtiene la cadena sin título con el contador y la guarda como el nuevo título.
                    cont++; //Incementa el contador.
                }
            } while (!titleValid); //Mientras el título no sea válido.
        } else { //Si el título no está vacío.
            String strippedTask = Utils.stripAccents(taskTitle); //Despoja al título de la tarea de los acentos y guarda la tarea despojada.

            for (String title: titles) { //Por cada título de los títulos.
                String strippedTitle = Utils.stripAccents(title); //Despoja al título de los acentos y guarda el título despojado.

                if (strippedTask.equals(strippedTitle)) { //Si los títulos despojados de las tareas son iguales.
                    titleValid = false; //Establece que el título no es válido.
                    break; //Corte.
                }
            }
        }

        return new Pair<>(taskTitle, titleValid); //Devuelve el par del título de la tarea y el indicador de si es válido.
    }

    /**
     * Saves the new Task.
     */
    protected void saveNewTask() {
        String newDesc = etNewDesc.getText().toString(); //Obtiene la cadena del texto de edición de la descripción y la guarda.
        Date taskStart = getTaskStart(); //Obtiene la fecha de inicio de la tarea y la guarda.

        Pair<String, Boolean> titlePair = getTaskTitle(); //Obtiene el título de la nueva tarea y guarda su par del título y del indicador de su validez.
        boolean titleValid = titlePair.second; //Obtiene el segundo del par del título y lo guarda como el indicador de si es un título válido.

        if (titleValid) { //Si el título es válido.
            int newId = 1; //Guarda el nuevo identificador como 1.

            if (!tasks.isEmpty()) { //Si las tareas no están vacías.
                Integer[] ids = tasks.keySet().toArray(new Integer[0]); //Obtiene la matriz de identificadores de las claves de las tareas y las guarda.
                newId = ids[ids.length - 1]; //Obtiene el último identificador y lo guarda como el nuevo.
                newId++; //Incrementa el nuevo identificador.
            }

            String taskTitle = titlePair.first; //Obtiene el primero del par del título y lo guarda como el de la tarea.
            Description taskDesc = new Description(newDesc); //Crea una nueva descripción para la tarea y la guarda.

            Date taskDate = new Date();  //Guarda la fecha actual como la fecha de la tarea.
            ArrayList<Value> taskValues = new ArrayList<>(); //Crea una matriz de lista de valores para los valores de la tarea y la guarda.

            for (int i = 0; i < values.size(); i++) { //Por cada índice del 0 al final de los valores.
                Field field = fields.get(i); //Obtiene el campo correspondiente y lo guarda.
                EditText valueText = values.get(i); //Obtiene el texto de los valores correspondiente y lo guarda.

                int idField = field.getId(); //Obtiene el identificador del campo y lo guarda.
                String contentValue = valueText.getText().toString(); //Obtiene la cadena del texto del valor y la guarda.

                Value taskValue = new Value(idField, contentValue, taskDate); //Crea un valor de la tarea y lo guarda.
                taskValues.add(taskValue); //Agrega el valor de la tarea a los valores de la tarea.
            }

            boolean tasksChecked = Utils.checkTasks(listsPath, currentList, tasksPath, tasks); //Comprueba las tareas y guarda su resultado.

            if (tasksChecked) { //Si se han comprobado las tareas.
                int idList = currentList.getId(); //Obtiene el identificador de la lista actual y lo guarda.
                Task newTask = new Task(newId, idList, taskTitle, taskDesc, taskValues, taskStart); //Crea una nueva tarea y la guarda.
                tasks.put(newId, newTask); //Agrega la nueva tarea a las tareas en el nuevo identificador.

                String[] newTitles = Utils.writeTasksToFile(tasksPath, tasks); //Escribe las tareas en el archivo de la ruta y guarda sus títulos nuevos.
                if (newTitles.length > 0) { //Si la longitud de los títulos nuevos es mayor que 0.
                    Toast.makeText(getApplicationContext(), getString(R.string.created_new_task), Toast.LENGTH_SHORT).show(); //Crea un mensaje de duración corta de la nueva tarea creada y lo muestra.

                    etNewTitle.setText(""); //Borra el contenido del texto de edición del título.
                    etNewDesc.setText(""); //Borra el contenido del texto de edición de la descipción.
                    etNewStart.setText(""); //Borra el contenido del texto de edición del inicio.

                    for (EditText valueText: values) //Por cada texto de valor de los valores.
                        valueText.setText(""); //Borra el contenido del texto de valor.

                    Utils.backMain(NewTaskActivity.this); //Regresa a la actividad principal.
                }
            }
        } else { //Si el título no es válido.
            Toast.makeText(getApplicationContext(), getString(R.string.invalid_title_new_task), Toast.LENGTH_SHORT).show(); //Crea un mensaje de duración corta de título inválido de la nueva tarea y lo muestra.
            etNewTitle.setText(""); //Borra el contenido del texto de edición del título.
        }
    }

    @Override
    public void onBackPressed() {
        Utils.backMain(NewTaskActivity.this); //Regresa a la actividad principal.
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.about, menu); //Infla el menú de acerca de en el diseño de esta actividad.
        return super.onCreateOptionsMenu(menu); //Devuelve el resultado de crear un menú de opciones en el diseño de esta actividad.
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_about) { //Si el identificador del elemento del menú es igual al de acerca de.
            Utils.showAbout(NewTaskActivity.this); //Muestra la actividad de acerca de.
            return true; //Devuelve verdadero.
        } else { //Si no es igual.
            return super.onOptionsItemSelected(item); //Devuelve el resultado de que un elemento de opciones sea seleccionado.
        }
    }
}

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
import android.content.Intent;
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
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Locale;

/**
 * A class that manages the Edit Task Activity.
 *
 * @author Raul Martin Perez (raumar95@gmail.com)
 */
public class EditTaskActivity extends AppCompatActivity {

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
     * The ArrayList of the Values.
     */
    private ArrayList<EditText> etValues = new ArrayList<>(); //Guarda la matriz de lista de valores.

    /**
     * The EditText of the title.
     */
    private EditText etEditTitle; //Inicializa el texto de edición del título.

    /**
     * The EditText of the Description.
     */
    private EditText etEditDesc; //Inicializa el texto de edición de la descripción.

    /**
     * The EditText of the start date.
     */
    private EditText etEditStart; //Inicializa el texto de edición de la fecha de inicio.

    /**
     * The Map of the Tasks.
     */
    private LinkedHashMap<Integer, Task> tasks = new LinkedHashMap<>(); //Guarda el mapa de las tareas.

    /**
     * The current List.
     */
    private List currentList; //Inicializa la lista actual.

    /**
     * The current Task.
     */
    private Task currentTask; //Inicializa la tarea actual.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState); //Inicializa todos los fragmentos de la actividad de editar tarea.
        setContentView(R.layout.activity_edit_task); //Establece la vista del contenido de la actividad de editar tarea.

        listsPath = getExternalFilesDir(null) + "/lists.xml"; //Guarda la ruta del archivo listas en el almacenamiento externo.
        tasksPath = getExternalFilesDir(null) + "/tasks.xml"; //Guarda la ruta del archivo tareas en el almacenamiento externo.

        etEditTitle = findViewById(R.id.et_title_edit_task); //Busca el texto de edición del título de la tarea editada y lo guarda.
        etEditDesc = findViewById(R.id.et_desc_edit_task); //Busca el texto de edición de la descipción de la tarea editada y lo guarda.
        etEditStart = findViewById(R.id.et_start_edit_task); //Busca el texto de edición del inicio de la tarea editada y lo guarda.
        FloatingActionButton fabEditCancel = findViewById(R.id.fab_cancel_edit_task); //Busca el botón de acción flotante de cancelar tarea editada y lo guarda.
        FloatingActionButton fabEditDate = findViewById(R.id.fab_date_edit_task); //Busca el botón de acción flotante de fechar tarea editada y lo guarda.
        FloatingActionButton fabEditAccept = findViewById(R.id.fab_accept_edit_task); //Busca el botón de acción flotante de aceptar tarea editada y lo guarda.

        etEditTitle.setRawInputType(InputType.TYPE_CLASS_TEXT); //Establece el tipo de entrada del texto del título como texto.
        etEditDesc.setRawInputType(InputType.TYPE_CLASS_TEXT); //Establece el tipo de entrada del texto de la descripción como texto.

        etEditStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utils.getDate(EditTaskActivity.this, view); //Abre un diálogo de selector de fechas para obtener una y escribirla.
            }
        }); //Establece el escuchador de clics del texto del inicio.

        fabEditCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showEditTask(0); //Muestra el diálogo de editar tarea para elegir si salir de la tarea editada o no.
            }
        }); //Establece el escuchador de clics del botón de acción flotante de cancelar tarea editada.

        fabEditDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showEditTask(1); //Muestra el diálogo de editar tarea para elegir si guardar y fechar la tarea editada o no.
            }
        }); //Establece el escuchador de clics del botón de acción flotante de fechar tarea editada.

        fabEditAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showEditTask(2); //Muestra el diálogo de editar tarea para elegir si guardar la tarea editada o no.
            }
        }); //Establece el escuchador de clics del botón de acción flotante de aceptar tarea editada.

        TooltipCompat.setTooltipText(fabEditCancel, getString(R.string.hint_cancel_edit)); //Establece el texto de la información de herramienta del botón de acción flotante de cancelar tarea editada.
        TooltipCompat.setTooltipText(fabEditDate, getString(R.string.hint_date_edit)); //Establece el texto de la información de herramienta del botón de acción flotante de fechar tarea editada.
        TooltipCompat.setTooltipText(fabEditAccept, getString(R.string.hint_accept_edit)); //Establece el texto de la información de herramienta del botón de acción flotante de aceptar tarea editada.

        Serializable list = getIntent().getSerializableExtra("list"); //Obtiene el serializable de la intención list y lo guarda.
        Serializable task = getIntent().getSerializableExtra("task"); //Obtiene el serializable de la intención task y lo guarda.

        if ((list != null) && (task != null)) { //Si la lista y la tarea no son valores nulos.
            currentList = (List) list; //Adapta la lista y la guarda como la actual.
            currentTask = (Task) task; //Adapta la tarea y la guarda como la actual.

            String taskTitle = currentTask.getTitle(); //Obtiene el título de la tarea actual y lo guarda.
            String taskDesc = currentTask.getDescription().getContent(); //Obtiene el contenido de la descripción de la tarea actual y lo guarda.
            Date taskCreation = currentTask.getCreationDate(); //Obtiene la fecha de creación de la tarea actual y la guarda.

            SimpleDateFormat shortDate = new SimpleDateFormat("dd/MM/yyyy", Locale.US); //Crea un formato de fechas simples corto y lo guarda.
            String taskStart = shortDate.format(taskCreation); //Formatea la fecha de creación a una cadena y la guarda.

            etEditTitle.setText(taskTitle); //Establece el texto del texto de edición del título.
            etEditDesc.setText(taskDesc); //Establece el texto del texto de edición de la descripción.
            etEditStart.setText(taskStart); //Establece el texto del texto de edición del inicio.

            showFieldsAndValues(); //Muestra los campos y sus valores.
            titles = Utils.parseTasks(listsPath, currentList, tasksPath, tasks); //Analiza las tareas.
        }
    }

    /**
     * Shows the Fields and its Values in the activity.
     */
    protected void showFieldsAndValues() {
        ArrayList<Field> fields = currentList.getFields(); //Obtiene los campos de la lista actual y los guarda.

        if (!fields.isEmpty()) { //Si los campos no están vacíos.
            LinearLayout layoutValues = findViewById(R.id.layout_values_edit_task); //Busca el diseño de los valores de la tarea editada y lo guarda.
            LinearLayout layoutDefined = findViewById(R.id.layout_defined_edit_task); //Busca el diseño de la cabecera definida de la tarea editada y lo guarda.
            layoutDefined.setVisibility(View.VISIBLE); //Establece la visibilidad del diseño de la cabecera definida como visible.

            Typeface tf = ResourcesCompat.getFont(EditTaskActivity.this, R.font.aller_bold); //Obtiene la fuente negrita de los recursos y la guarda.

            ArrayList<Value> values = currentTask.getValues(); //Obtiene los valores de la tarea actual y los guarda.

            for (int i = 0; i < fields.size(); i++) { //Por cada campo de los campos.
                Field field = fields.get(i); //Obtiene el campo correspondiente y lo guarda.

                LinearLayout layout = new LinearLayout(EditTaskActivity.this); //Crea un nuevo diseño linear y lo guarda.
                TextView tv = new TextView(EditTaskActivity.this); //Crea una nueva vista de texto y la guarda.
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
                        et = (EditText) View.inflate(EditTaskActivity.this, R.layout.value_editnumber, null); //Infla un texto de edición de números para el valor y lo guarda.
                        et.setRawInputType(InputType.TYPE_CLASS_PHONE); //Establece el tipo de entrada del texto de edición como teléfono.
                        break; //Corte.
                    case "Date": //Tipo fecha.
                        et = (EditText) View.inflate(EditTaskActivity.this, R.layout.value_editdate, null); //Infla un texto de edición de números para la fecha y lo guarda.
                        et.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Utils.getDate(EditTaskActivity.this, view); //Abre un diálogo de selector de fechas para obtener una y escribirla.
                            }
                        }); //Establece el escuchador de clics del texto de edición.
                        break; //Corte.
                    default: //Si no es ninguno de los anteriores.
                        et = (EditText) View.inflate(EditTaskActivity.this, R.layout.value_edittext, null); //Infla un texto de edición de números para el texto y lo guarda.
                        et.setRawInputType(InputType.TYPE_CLASS_TEXT); //Establece el tipo de entrada del texto de edición como texto.
                        break; //Corte.
                }

                et.setHint(hint); //Establece la pista del texto de edición.
                et.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1)); //Establece los parámetros del diseño del texto de edición.

                Value value = values.get(i); //Obtiene el valor correspondiente y lo guarda.
                String content = value.getContent(); //Obtiene el contenido del valor y lo guarda.
                et.setText(content); //Establece el texto del texto de edición.

                etValues.add(et); //Agrega el texto de edición a los textos de edición de los valores.

                layout.addView(tv); //Agrega la vista del texto al diseño.
                layout.addView(et); //Agrega el texto de edición al diseño.
            }
        }
    }

    /**
     * Shows the edit Task dialog to choose on the action in the edited Task.
     *
     * @param action the action to be performed in the edited Task.
     */
    protected void showEditTask(int action) {
        View editHeader = View.inflate(EditTaskActivity.this, R.layout.custom_dialog_header, null); //Infla la vista de la cabecera de edición y la guarda.

        ImageView editIcon = editHeader.findViewById(R.id.dialog_icon); //Busca el icono de la cabecera de edición y lo guarda.
        editIcon.setImageResource(R.drawable.ic_editing_pencil_blue_32dp); //Establece el recurso de la imagen del icono de edición.

        TextView editTitle = editHeader.findViewById(R.id.dialog_title); //Busca el título de la cabecera de edición y lo guarda.
        editTitle.setText(R.string.edit_task_header); //Establece el texto del título de adición.

        AlertDialog.Builder editBuilder = new AlertDialog.Builder(EditTaskActivity.this, 3); //Crea una nuevo constructor de diálogo de alerta y lo guarda.
        editBuilder.setCustomTitle(editHeader); //Establece el título personalizado del constructor de edición como la cabecera.

        View editContent = View.inflate(EditTaskActivity.this, R.layout.custom_dialog_content, null); //Infla la vista del contenido de edición y la guarda.

        final AlertDialog editDialog = editBuilder.create(); //Crea un diálogo de alerta con el constructor de edición y lo guarda.
        editDialog.setView(editContent); //Establece la vista del díalogo de edición como el contenido.

        TextView editText = editContent.findViewById(R.id.custom_info); //Busca la información del contenido de edición y lo guarda.
        Button acceptBtn = editContent.findViewById(R.id.custom_accept); //Busca el botón de aceptar del contenido de edición y lo guarda.

        switch (action) { //Considera la acción.
            case 1: //Acción 1.
                editText.setText(R.string.are_sure_date_edit_task); //Establece el texto de edición como el de fechar.

                editContent.findViewById(R.id.custom_accept).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        editDialog.dismiss(); //Cierra el diálogo de edición.
                        saveEditTask(true); //Guarda la tarea editada fechando sus cambios.
                    }
                }); //Establece el escuchador de clics del botón de aceptar del contenido de edición.
                break; //Corte.
            case 2: //Acción 2.
                editText.setText(R.string.are_sure_accept_edit_task); //Establece el texto de edición como el de aceptar.

                acceptBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        editDialog.dismiss(); //Cierra el diálogo de edición.
                        saveEditTask(false); //Guarda la tarea editada sin fechar sus cambios.
                    }
                }); //Establece el escuchador de clics del botón de aceptar del contenido de edición.
                break; //Corte.
            default: //Si no es ninguna de las anteriores.
                editText.setText(R.string.are_sure_cancel_edit_task); //Establece el texto de edición como el de cancelar.

                acceptBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        editDialog.dismiss(); //Cierra el diálogo de edición.
                        backEditTask(); //Regresa de la actividad de editar tarea a la actividad de tarea.
                    }
                }); //Establece el escuchador de clics del botón de aceptar del contenido de edición.
                break; //Corte.
        }

        editContent.findViewById(R.id.custom_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editDialog.dismiss(); //Cierra el diálogo de edición.
            }
        }); //Establece el escuchador de clics del botón de cancelar del contenido de edición.

        Utils.hideKeyboard(EditTaskActivity.this); //Oculta el teclado mostrado en esta actividad.

        editDialog.show(); //Muestra el diálogo de edición.
    }

    /**
     * Returns of the Edit Task Activity to the Task Activity.
     */
    protected void backEditTask() {
        Intent task = new Intent(EditTaskActivity.this, TaskActivity.class); //Crea una intención de pasar de la actividad de editar tarea a la de tarea y la guarda.
        task.putExtra("list", currentList); //Agrega la lista actual como la lista a la intención.
        task.putExtra("task", currentTask); //Agrega la tarea actual como la tarea a la intención.
        startActivity(task); //Inicia la actividad de tarea.
        finish(); //Finaliza la actividad de editar tarea.
    }

    /**
     * Gets the title of the edited Task and returns it along with an indicator of whether it is valid.
     *
     * @return the name of the edited Task and the indicator of whether it is valid.
     */
    protected Pair<String, Boolean> getTaskTitle() {
        String newTitle = etEditTitle.getText().toString(); //Obtiene la cadena del texto de edición del título y la guarda.
        boolean titleValid = true; //Guarda el indicador de que el título es válido como verdadero.

        if (newTitle.isEmpty()) { //Si el nuevo título está vacío.
            newTitle = currentTask.getTitle(); //Obtiene el título de la tarea actual y lo guarda como el nuevo título.
        } else { //Si el título no está vacío.
            if (tasks.size() > 1) { //Si el tamaño de las tareas es mayor que 1.
                String currentTitle = currentTask.getTitle(); //Obtiene el título de la tarea actual y lo guarda.

                for (String title: titles)  //Por cada título de los títulos
                    if (!title.equals(currentTitle) && title.equals(newTitle)) { //Si el título no es igual al actual y es igual al nuevo.
                        titleValid = false; //Establece que el título no es válido.
                        break; //Corte.
                    }
            }
        }

        return new Pair<>(newTitle, titleValid); //Devuelve el par del nuevo título y el indicador de si es válido.
    }

    /**
     * Saves the edited Task by dating or not its changes.
     *
     * @param dateChanges the indicator of whether the changes will be dated or not in the Task.
     */
    protected void saveEditTask(boolean dateChanges) {
        String newDesc = etEditDesc.getText().toString(); //Obtiene la cadena del texto de edición de la descripción y la guarda.
        String newStart = etEditStart.getText().toString(); //Obtiene la cadena del texto de edición del inicio y la guarda.

        ArrayList<History> newHistories = new ArrayList<>(); //Crea una matriz de lista de los historiales y la guarda.
        Pair<String, Boolean> titlePair = getTaskTitle(); //Obtiene el título de la tarea editada y guarda su par del título y del indicador de su validez.
        boolean titleValid = titlePair.second; //Obtiene el segundo del par del título y lo guarda como el indicador de si es un título válido.

        if (titleValid) { //Si el título es válido.
            String newTitle = titlePair.first; //Obtiene el primero del par del título y lo guarda como el nuevo.
            String currentTitle = currentTask.getTitle(); //Obtiene el título de la tarea actual y lo guarda.

            if (!currentTitle.equals(newTitle)) { //Si el título actual no es igual al título.
                if (dateChanges) { //Si fecha los cambios.
                    History titleHistory = new History("Title", currentTitle, History.Type.TITLE); //Crea una nueva historia de tipo título y la guarda.
                    newHistories.add(titleHistory); //Agrega la historia del título a las nuevas historias.
                }

                currentTask.setTitle(newTitle); //Establece el título de la tarea actual.
            }

            SimpleDateFormat shortDate = new SimpleDateFormat("dd/MM/yyyy", Locale.US); //Crea un formato de fechas simples corto y lo guarda.
            Date newDate = new Date(); //Guarda la fecha actual como la nueva fecha.
            Date newCreation = null; //Guarda la nueva creación como un valor nulo.

            try { //Trata de hacer el contenido del cuerpo.
                newCreation = shortDate.parse(newStart); //Analiza el nuevo inicio como una fecha y la guarda como la nueva creación.
            } catch (ParseException e) { //Si se produce la excepción de análisis.
                Toast.makeText(getApplicationContext(), "ParseException: " + e.getLocalizedMessage(), Toast.LENGTH_SHORT).show(); //Crea un mensaje de duración corta del mensaje localizado de la excepción y lo muestra.
            }

            String currentDesc = currentTask.getDescription().getContent(); //Obtiene el contenido de la descripción de la tarea actual y lo guarda como la descripción actual.

            if (!currentDesc.equals(newDesc)) { //Si la descripción actual no es igual a la nueva descripción.
                if (dateChanges) { //Si fecha los cambios.
                    History descHistory = new History("Description", currentDesc, History.Type.DESCRIPTION); //Crea una nueva historia de tipo descripción y la guarda.
                    newHistories.add(descHistory); //Agrega la historia de la descripción a las nuevas historias.
                }

                Description taskDesc = new Description(newDesc, newDate); //Crea una nueva descripción para la tarea y la guarda.
                currentTask.setDescription(taskDesc); //Establece la descripción de la tarea actual.
            }

            if (newCreation != null) { //Si la nueva creación no es un valor nulo.
                Date creationDate = currentTask.getCreationDate(); //Obtiene la fecha de creación de la tarea actual y la guarda.
                String creationString = shortDate.format(creationDate); //Formatea la fecha de creación a una cadena y la guarda.

                if (!creationString.equals(newStart)) {//Si la cadena de creación no es igual al nuevo inicio.
                    if (dateChanges) { //Si fecha los cambios.
                        History creationHistory = new History("Start date", creationString, History.Type.STARTDATE); //Crea una nueva historia de tipo fecha de inicio y la guarda.
                        newHistories.add(creationHistory); //Agrega la historia de la fecha de inicio a las nuevas historias.
                    }

                    currentTask.setCreationDate(newCreation); //Establece la fecha de creación de la tarea actual.
                }
            }

            ArrayList<Value> currentValues = currentTask.getValues(); //Obtiene los valores de la tarea actual y los guarda.

            for (int i = 0; i < etValues.size(); i++) { //Por cada índice del 0 al final de los textos de edición de los valores.
                Value currentValue = currentValues.get(i); //Obtiene el valor actual correspondiente y lo guarda.
                EditText etValue = etValues.get(i); //Obtiene el texto de edición de los valores correspondiente y lo guarda.

                String textValue = etValue.getText().toString(); //Obtiene la cadena del texto de edición del valor y la guarda.
                String contentValue = currentValue.getContent(); //Obtiene el contenido del valor actual y lo guarda.

                if (!contentValue.equals(textValue)) { //Si el contenido del valor no es igual al texto del valor.
                    ArrayList<Field> currentFields = currentList.getFields(); //Obtiene los campos de la lista actual y los guarda.
                    Field currentField = currentFields.get(i); //Obtiene el campo actual correspondiente y lo guarda.
                    String fieldName = currentField.getName(); //Obtiene el nombre del campo actual y lo guarda.

                    if (dateChanges) { //Si fecha los cambios.
                        History valueHistory = new History(fieldName, currentValue.getContent()); //Crea una nueva historia del valor y la guarda.
                        newHistories.add(valueHistory); //Agrega la historia del valor a las nuevas historias.
                    }

                    currentValue.setContent(textValue); //Establece el contenido del valor actual.
                    currentValue.setCreationDate(newDate); //Establece la fecha de creación del valor actual.
                }
            }

            if (!newHistories.isEmpty()) { //Si las nuevas historias no están vacías.
                ArrayList<Record> currentRecords = currentTask.getRecords(); //Obtiene los historiales de la trea actual y los guarda.
                Record newRecord = new Record(newHistories, newDate); //Crea un nuevo historial y lo guarda.
                currentRecords.add(newRecord); //Agrega el nuevo historial a los historiales actuales.
            }

            boolean tasksChecked = Utils.checkTasks(listsPath, currentList, tasksPath, tasks); //Comprueba las tareas y guarda su resultado.

            if (tasksChecked) { //Si se han comprobado las tareas.
                int taskId = currentTask.getId(); //Obtiene el identificador de la tarea actual y lo guarda.
                tasks.put(taskId, currentTask); //Agrega la tarea actual a las tareas en el identificador de la tarea.

                String[] editedTitles = Utils.writeTasksToFile(tasksPath, tasks); //Escribe las tareas en el archivo de la ruta y guarda sus títulos editados.
                if (editedTitles.length > 0) { //Si la longitud de los títulos editados es mayor que 0.
                    Toast.makeText(getApplicationContext(), getString(R.string.edited_task), Toast.LENGTH_SHORT).show(); //Crea un mensaje de duración corta de la tarea editada y lo muestra.

                    etEditTitle.setText(""); //Borra el contenido del texto de edición del título.
                    etEditDesc.setText(""); //Borra el contenido del texto de edición de la descipción.
                    etEditStart.setText(""); //Borra el contenido del texto de edición del inicio.

                    for (EditText valueText: etValues) //Por cada texto de valor de los textos de edición de los valores.
                        valueText.setText(""); //Borra el contenido del texto de valor.

                    backEditTask(); //Regresa de la actividad de editar tarea a la actividad de tarea.
                }
            }
        } else { //Si el título no es válido.
            Toast.makeText(getApplicationContext(), getString(R.string.invalid_new_title_edit_task), Toast.LENGTH_SHORT).show(); //Crea un mensaje de duración corta de nuevo título inválido de la tarea editada y lo muestra.
            etEditTitle.setText(""); //Borra el contenido del texto de edición del título.
        }
    }

    @Override
    public void onBackPressed() {
        backEditTask(); //Regresa de la actividad de editar tarea a la actividad de tarea.
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.about, menu); //Infla el menú de acerca de en el diseño de esta actividad.
        return super.onCreateOptionsMenu(menu); //Devuelve el resultado de crear un menú de opciones en el diseño de esta actividad.
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_about) { //Si el identificador del elemento del menú es igual al de acerca de.
            Utils.showAbout(EditTaskActivity.this); //Muestra la actividad de acerca de.
            return true; //Devuelve verdadero.
        } else { //Si no es igual.
            return super.onOptionsItemSelected(item); //Devuelve el resultado de que un elemento de opciones sea seleccionado.
        }
    }
}

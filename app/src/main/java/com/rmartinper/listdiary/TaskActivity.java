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
import android.graphics.Rect;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.appcompat.widget.TooltipCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.text.HtmlCompat;

import com.github.florent37.viewtooltip.ViewTooltip;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * A class that manages the Task Activity.
 *
 * @author Raul Martin Perez (raumar95@gmail.com)
 */
public class TaskActivity extends AppCompatActivity {

    /**
     * The current View of Tooltip.
     */
    private ViewTooltip currentTooltip; //Inicializa la información de herramienta actual.

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

    /**
     * The path of the Tasks file.
     */
    private String tasksPath; //Inicializa la ruta del archivo tareas.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState); //Inicializa todos los fragmentos de la actividad de tarea.
        setContentView(R.layout.activity_task); //Establece la vista del contenido de la actividad de tarea.

        tasksPath = getExternalFilesDir(null) + "/tasks.xml"; //Guarda la ruta del archivo tareas en el almacenamiento externo.

        TextView tvDesc = findViewById(R.id.tv_desc_task); //Busca la vista de texto de la descripción de la tarea y la guarda.
        TextView tvStart = findViewById(R.id.tv_start_task); //Busca la vista de texto del inicio de la tarea y la guarda.
        FloatingActionButton fab = findViewById(R.id.fab_back_task); //Busca el botón de acción flotante de volver atrás en la tarea y lo guarda.
        Toolbar toolbar = findViewById(R.id.toolbar_task); //Busca la barra de herramientas de la tarea y la guarda.
        setSupportActionBar(toolbar); //Establece la barra de acción de soporte como la barra de herramientas.

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utils.backMain(TaskActivity.this); //Regresa a la actividad principal.
            }
        }); //Establece el escuchador de clics del botón de acción flotante.

        TooltipCompat.setTooltipText(fab, getString(R.string.hint_back_list)); //Establece el texto de la información de herramienta del botón de acción flotante.

        Serializable list = getIntent().getSerializableExtra("list"); //Obtiene el serializable de la intención list y lo guarda.
        Serializable task = getIntent().getSerializableExtra("task"); //Obtiene el serializable de la intención task y lo guarda.

        if ((list != null) && (task != null)) { //Si la lista y la tarea no son valores nulos.
            currentList = (List) list; //Adapta la lista y la guarda como la actual.
            currentTask = (Task) task; //Adapta la tarea y la guarda como la actual.

            String taskTitle = currentTask.getTitle(); //Obtiene el título de la tarea actual y lo guarda.
            taskTitle += " - "; //Agrega un guión al título de la tarea.
            taskTitle += currentList.getName(); //Agrega el nombre de la lista actual al título de la tarea.

            setToolTitle(taskTitle); //Establece el título de la barra de herramientas como el de la tarea.

            String taskDesc = currentTask.getDescription().getContent(); //Obtiene el contenido de la descripción de la tarea actual y lo guarda.

            if (taskDesc.isEmpty()) { //Si la descripción de la tarea está vacía.
                tvDesc.setText(getString(R.string.no_desc)); //Establece el texto de la vista de descripción como sin descripción.
            } else { //Si no está vacía.
                String descText = getString(R.string.list_task_desc_header); //Obtiene la cadena de la cabecera de la descripción y la guarda.
                descText += " "; //Agrega un espacio en blanco al texto de la descripción.
                descText += taskDesc; //Agrega la descripción de la tarea al texto de la descripción.

                tvDesc.setText(descText); //Establece el texto de la vista de descripción como el de la descripción.
            }

            SimpleDateFormat shortDate = new SimpleDateFormat("dd/MM/yyyy", Locale.US); //Crea un formato de fechas simples corto y lo guarda.

            Date creationTask = currentTask.getCreationDate(); //Obtiene la fecha de creación de la tarea actual y la guarda.
            String taskCreation = shortDate.format(creationTask); //Formatea la fecha de creación a una cadena y la guarda.

            String taskStart = getString(R.string.task_start_header); //Obtiene la cadena de la cabecera del inicio de la tarea y la guarda.
            taskStart += " "; //Agrega un espacio en blanco al inicio de la tarea.
            taskStart += taskCreation; //Agrega la creación de la tarea al inicio de la tarea.

            tvStart.setText(taskStart); //Establece el texto de la vista de inicio como el inicio de la tarea.

            showFieldsAndValues(); //Muestra los campos y sus valores.
            showRecords(); //Muestra los historiales.

            String listsPath = getExternalFilesDir(null) + "/lists.xml"; //Guarda la ruta del archivo listas en el almacenamiento externo.
            Utils.parseTasks(listsPath, currentList, tasksPath, tasks); //Analiza las tareas.
        }
    }

    /**
     * Shows the Fields and its Values in the activity.
     */
    protected void showFieldsAndValues() {
        LinearLayout layoutFields = findViewById(R.id.layout_fields_task); //Busca el diseño de los campos de la tarea y lo guarda.

        Typeface tf = ResourcesCompat.getFont(TaskActivity.this, R.font.aller_regular); //Obtiene la fuente regular de los recursos y la guarda.

        ArrayList<Field> fields = currentList.getFields(); //Obtiene los campos de la lista actual y los guarda.
        ArrayList<Value> values = currentTask.getValues(); //Obtiene los valores de la tarea actual y los guarda.

        for (int i = 0; i < fields.size(); i++) { //Por cada índice del 0 al final de los campos.
            Field field = fields.get(i); //Obtiene el campo correspondiente y lo guarda.
            Value value = values.get(i); //Obtiene el valor correspondiente y lo guarda.

            String valueContent = value.getContent(); //Obtiene el contenido del valor y lo guarda.
            String valueText = field.getName(); //Obtiene el nombre del campo y lo guarda.
            valueText += ": "; //Agrega dos puntos al texto del valor.

            if (valueContent.isEmpty()) { //Si el contenido del valor está vacío.
                valueText += getString(R.string.no_data); //Agrega la cadena de sin datos al texto del valor.
            } else { //Si no está vacío.
                valueText += valueContent; //Agrega el contenido del valor al texto del valor.
            }

            TextView tvValue = new TextView(TaskActivity.this); //Crea una vista de texto para el valor y lo guarda.

            float tvDp = 16f; //Guarda los píxeles de densidad independiente de la vista de texto como 16.
            int tvPx = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, tvDp, getResources().getDisplayMetrics()); //Convierte los píxeles de densidad de la vista de texto a píxeles y los guarda.

            LinearLayout.LayoutParams tvParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT); //Crea unos nuevos parámetros de diseño y los guarda.
            tvParams.setMargins(0, tvPx, 0, 0); //Establece los márgenes de los parámetros de la vista de texto.

            tvValue.setLayoutParams(tvParams); //Establece los parámetros del diseño de la vista de texto del valor.
            tvValue.setTypeface(tf); //Establece el tipo de letra de la vista de texto del valor.
            tvValue.setText(valueText); //Establece el texto de la vista de texto del valor.
            tvValue.setTextColor(Color.BLACK); //Establece el color del texto de la vista de texto del valor como negro.
            tvValue.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18); //Establece el tamaño del texto de la vista de texto del valor como 18.

            layoutFields.addView(tvValue); //Agrega la vista del texto del valor al diseño de los campos.
        }
    }

    /**
     * Shows the Records in the activity.
     */
    protected void showRecords() {
        ArrayList<Record> records = currentTask.getRecords(); //Obtiene los historiales de la tarea actual y los guarda.

        if (!records.isEmpty()) { //Si los historiales no están vacíos.
            LinearLayout layoutHeader = findViewById(R.id.layout_records_header_task); //Busca el diseño de la cabecera de los historiales de la tarea y lo guarda.
            LinearLayout layoutRecords = findViewById(R.id.layout_records_task); //Busca el diseño de las historias de la tarea y lo guarda.

            layoutHeader.setVisibility(View.VISIBLE); //Establece la visibilidad del diseño de la cabecera como visible.

            SimpleDateFormat longDate = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.US); //Crea un formato de fechas simples largo y lo guarda.

            for (Record record: records) { //Por cada historial de los historiales.
                TextView tvRecord = (TextView) View.inflate(TaskActivity.this, R.layout.record_list_item, null); //Infla una vista de texto para la historia y lo guarda.

                Date creationRecord = record.getCreationDate(); //Obtiene la fecha de creación del historial y la guarda.
                String recordCreation = longDate.format(creationRecord); //Formatea la fecha de creación a una cadena y la guarda.

                String historiesText = getHistories(record); //Obtiene las historias del historial y las guarda como su texto.
                String recordText = "<b>" + recordCreation + "</b>" + historiesText; //Guarda el texto del historial con su fecha de creación y sus historias.

                String recordDesc = getString(R.string.hint_task_history, recordCreation); //Obtiene la cadena de pista de la historia de la tarea con su creación y la guarda como la descripción del historial.
                LinearLayout.LayoutParams linearParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1); //Crea unos nuevos parámetros de diseño y los guarda.

                tvRecord.setContentDescription(recordDesc); //Establece el contenido de la descripción de la vista de texto del historial.
                tvRecord.setLayoutParams(linearParams); //Establece los parámetros del diseño de la vista de texto del historial.
                tvRecord.setText(HtmlCompat.fromHtml(recordText, HtmlCompat.FROM_HTML_MODE_LEGACY)); //Establece el texto de html de la vista de texto del historial.

                setRecordTooltip(tvRecord); //Establece la descripción emergente del registro.

                layoutRecords.addView(tvRecord); //Agrega la vista del texto del historial al diseño de los historiales.
            }
        }
    }

    /**
     * Gets the Histories of the given Record and returns them.
     *
     * @param record the Record from which to get their Histories.
     * @return the text of the Histories of the Record.
     */
    private String getHistories(Record record) {
        StringBuilder historiesBuilder = new StringBuilder(); //Crea un nuevo constructor de cadenas para las historias y lo guarda.

        ArrayList<History> histories = record.getHistories(); //Obtiene las historias del historial y las guarda.

        for (History history: histories) { //Por cada historia de las historias.
            History.Type historyType = history.getType(); //Obtiene el tipo de la historia y lo guarda.

            switch (historyType) { //Considera el tipo de la historia.
                case TITLE: //Título.
                    String titleHeader = getString(R.string.task_title_header); //Obtiene la cadena de cabecera del título y la guarda.
                    String historyTitle = titleHeader + " " + history.getValue(); //Guarda el título de la historia con su cabecera y su valor.
                    historiesBuilder.append("<br>").append(historyTitle); //Agrega el título de la historia junto con un salto de línea al constructor de las historias.
                    break; //Corte.
                case DESCRIPTION: //Descripción.
                    String descValue = history.getValue(); //Obtiene el valor de la historia y lo guarda como el de la descripción.
                    String historyDesc; //Inicializa la descripción de la historia.

                    if (descValue.isEmpty()) { //Si el valor de la descripción está vacío.
                        historyDesc = getString(R.string.no_desc); //Guarda la descripción de la historia como sin descripción.
                    } else { //Si no está vacío.
                        String descHeader = getString(R.string.list_task_desc_header); //Obtiene la cadena de cabecera de la descripción y la guarda.
                        historyDesc = descHeader + " " + descValue; //Guarda la descripción de la historia con su cabecera y su valor.
                    }

                    historiesBuilder.append("<br>").append(historyDesc); //Agrega la descripción de la historia junto con un salto de línea al constructor de las historias.
                    break; //Corte.
                case STARTDATE: //Fecha de inicio.
                    String startHeader = getString(R.string.task_start_header); //Obtiene la cadena de cabecera del inicio y la guarda.
                    String historyStart = startHeader + " " + history.getValue(); //Guarda el inicio de la historia con su cabecera y su valor.
                    historiesBuilder.append("<br>").append(historyStart); //Agrega el inicio de la historia junto con un salto de línea al constructor de las historias.
                    break; //Corte.
                default: //Si no es ninguno de los anteriores.
                    History.Action historyAction = history.getAction(); //Obtiene la acción de la historia y la guarda.
                    StringBuilder fieldBuilder = new StringBuilder(history.getField()); //Crea un nuevo constructor de cadenas con el campo de la historia de la tarea y lo guarda.
                    fieldBuilder.append(": "); //Agrega los dos puntos al constructor del campo.

                    switch (historyAction) { //Considera la acción de la historia.
                        case CREATE: //Crear.
                            fieldBuilder.append(getString(R.string.no_exist)); //Agrega la cadena no existe al constructor del campo.
                            break; //Corte.
                        case DELETE: //Eliminar.
                            fieldBuilder.append(getString(R.string.exist)); //Agrega la cadena existe al constructor del campo.
                            break; //Corte.
                        default: //Si no es ninguna de las anteriores.
                            String fieldValue = history.getValue(); //Obtiene el valor de la historia y lo guarda como el del campo.

                            if (fieldValue.isEmpty()) { //Si el valor del campo está vacío.
                                fieldBuilder.append(getString(R.string.no_data)); //Agrega la cadena sin datos al constructor del campo.
                            } else { //Si no está vacío.
                                fieldBuilder.append(fieldValue); //Agrega el valor del campo al constructor del campo.
                            }
                            break; //Corte.
                    }

                    String historyField = fieldBuilder.toString(); //Convierte el constructor del campo en una cadena y la guarda como el campo de la historia.
                    historiesBuilder.append("<br>").append(historyField); //Agrega el campo de la historia junto con un salto de línea al constructor de las historias.
                    break; //Corte.
            }
        }

        return historiesBuilder.toString(); //Devuelve el constructor de las historias convertido a una cadena.
    }

    /**
     * Sets the tooltip of the Record.
     *
     * @param tvRecord the TextView of the Record from which to set its tooltip.
     */
    private void setRecordTooltip(TextView tvRecord) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) { //Si la version SDK de la compilacion actual del sistema es mayor o igual que O.
            tvRecord.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    TextView tvRecord = (TextView) view; //Adapta la vista a una de texto y la guarda como la del historial.
                    String recordDesc = tvRecord.getContentDescription().toString(); //Obtiene el contenido de la descripción de la vista de texto del historial y lo guarda como la descripción del historial.

                    if (currentTooltip != null) //Si la información de herramienta actual no es un valor nulo.
                        currentTooltip.close(); //Cierra la información de herramienta actual.

                    int windowHeight = getWindow().getDecorView().getHeight(); //Obtiene la altura de la ventana de la actividad y la guarda.
                    int windowCenterY = windowHeight / 2; //Calcula el centro en el eje Y de la ventana y la guarda.

                    Rect viewRect = new Rect(); //Crea un rectángulo para la vista y lo guarda.
                    view.getGlobalVisibleRect(viewRect); //Obtiene el rectángulo global de la vista visible y lo guarda.
                    int viewCenterY = viewRect.centerY(); //Obtiene el centro en el eje Y del rectańgulo de la vista y lo guarda.

                    ViewTooltip.Position tooltipPos; //Inicializa la posición de la vista de la información de herramienta.
                    if (viewCenterY > windowCenterY) { //Si el centro en el eje Y de la vista es mayor que el de la ventana.
                        tooltipPos = ViewTooltip.Position.TOP; //Guarda la posición de la vista de la información de herramienta como la cima.
                    } else { //Si no es mayor.
                        tooltipPos = ViewTooltip.Position.BOTTOM; //Guarda la posición de la vista de la información de herramienta como el fondo.
                    }

                    currentTooltip = Utils.showCustomTooltip(TaskActivity.this, view, tooltipPos, recordDesc); //Muestra la información de herramienta personalizada.
                    currentTooltip.show(); //Muestra la información de herramienta actual.

                    return true; //Devuelve verdadero.
                }
            }); //Establece el escuchador de clics de larga duración de la vista de texto del historial.
        } else { //Si la version SDK es menor.
            String recordDesc = tvRecord.getContentDescription().toString(); //Obtiene el contenido de la descripción de la vista de texto del historial y lo guarda como la descripción del historial.
            TooltipCompat.setTooltipText(tvRecord, recordDesc); //Establece el texto de la información de herramienta de la vista de texto del historial.
        }
    }

    /**
     * Sets the title of the toolbar.
     *
     * @param title the title of the toolbar.
     */
    protected void setToolTitle(String title) {
        TextView toolTitle = findViewById(R.id.title_task); //Busca el título de la tarea y lo guarda.
        toolTitle.setText(title); //Establece el texto del título de la barra de herramientas.
    }

    /**
     * Opens the Edit Task Activity to modify the current Task.
     */
    protected void editTask() {
        Intent task = new Intent(TaskActivity.this, EditTaskActivity.class); //Crea una intención de pasar de la actividad de tarea a la de editar tarea y la guarda.
        task.putExtra("list", currentList); //Agrega la lista actual como la lista a la intención.
        task.putExtra("task", currentTask); //Agrega la tarea actual como la tarea a la intención.
        startActivity(task); //Inicia la actividad de editar tarea.
        finish(); //Finaliza la actividad de tarea.
    }

    /**
     * Saves the Tasks after the deletion.
     */
    private void saveDeletedTasks() {
        File tasksFile = new File(tasksPath); //Crea el archivo XML tareas del almacenamiento externo de la aplicación y lo guarda.
        boolean tasksExisted = tasksFile.exists(); //Guarda si el archivo tareas existe.

        try { //Trata de hacer el contenido del cuerpo.
            if (!tasksExisted) //Si el archivo tareas no existe.
                tasksExisted = tasksFile.createNewFile(); //Crea un nuevo archivo tareas y guarda si lo consigue.
        } catch (IOException e) { //Si se produce una excepción de E/S.
            Toast.makeText(getApplicationContext(), "IOException: " + e.getLocalizedMessage(), Toast.LENGTH_SHORT).show(); //Crea un mensaje de duración corta del mensaje localizado de la excepción y lo muestra.
        }

        if (tasksExisted) //Si el archivo tareas existe.
            Utils.writeTasksToFile(tasksPath, tasks); //Escribe las tareas en el archivo de la ruta.
    }

    /**
     * Opens the delete Task Dialog to choose whether to delete the current Task or not.
     */
    protected void deleteTask() {
        View deleteHeader = View.inflate(TaskActivity.this, R.layout.custom_dialog_header, null); //Infla la vista de la cabecera de eliminado y la guarda.

        ImageView deleteIcon = deleteHeader.findViewById(R.id.dialog_icon); //Busca el icono de la cabecera de eliminado y lo guarda.
        deleteIcon.setImageResource(R.drawable.ic_minus_blue_32dp); //Establece el recurso de la imagen del icono de eliminado.

        TextView deleteTitle = deleteHeader.findViewById(R.id.dialog_title); //Busca el título de la cabecera de eliminado y lo guarda.
        deleteTitle.setText(R.string.delete_task_header); //Establece el texto del título de eliminado.

        AlertDialog.Builder deleteBuilder = new AlertDialog.Builder(TaskActivity.this, 3); //Crea una nuevo constructor de diálogo de alerta y lo guarda.
        deleteBuilder.setCustomTitle(deleteHeader); //Establece el título personalizado del constructor de eliminado como la cabecera.

        View deleteContent = View.inflate(TaskActivity.this, R.layout.custom_dialog_content, null); //Infla la vista del contenido de eliminado y la guarda.

        final AlertDialog deleteDialog = deleteBuilder.create(); //Crea un diálogo de alerta con el constructor de eliminado y lo guarda.
        deleteDialog.setView(deleteContent); //Establece la vista del díalogo de eliminado como el contenido.

        TextView deleteText = deleteContent.findViewById(R.id.custom_info); //Busca la información del contenido de eliminado y lo guarda.
        deleteText.setText(R.string.are_sure_delete_task); //Establece el texto de eliminado.

        deleteContent.findViewById(R.id.custom_accept).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteDialog.dismiss(); //Cierra el diálogo de eliminado.

                Task taskRemoved = tasks.remove(currentTask.getId()); //Elimina la tarea actual por su identificador de las tareas y guarda si lo consigue.
                boolean taskDeleted = currentTask.equals(taskRemoved); //Comprueba si la tarea actual y la eliminada son iguales y guarda su resultado.

                if (taskDeleted) { //Si la tarea fue eliminada.
                    if (!tasks.isEmpty()) { //Si las tareas no están vacías.
                        saveDeletedTasks(); //Guarda las tareas después de la eliminación.
                    } else { //Si están vacías.
                        File tasksFile = new File(tasksPath); //Crea el archivo XML tareas del almacenamiento externo de la aplicación y lo guarda.

                        if (tasksFile.exists()) { //Si el archivo tareas existe.
                            boolean tasksDeleted = tasksFile.delete(); //Elimina el archivo tareas y guarda si lo consigue.
                            String tasksLog = "Tasks file deleted: " + tasksDeleted; //Guarda el registro de las tareas.

                            Logger logger = Logger.getLogger(TaskActivity.class.getName()); //Obtiene el registrador para la actividad de tarea y lo guarda.
                            logger.log(Level.INFO, tasksLog); //Registra al nivel de advertencia el registro de las tareas.
                        }
                    }

                    Toast.makeText(getApplicationContext(), getString(R.string.deleted_task), Toast.LENGTH_SHORT).show(); //Crea un mensaje de duración corta de la tarea eliminada y lo muestra.

                    Utils.backMain(TaskActivity.this); //Regresa a la actividad principal.
                }
            }
        }); //Establece el escuchador de clics del botón de aceptar del contenido de eliminado.
        deleteContent.findViewById(R.id.custom_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteDialog.dismiss(); //Cierra el diálogo de eliminado.
            }
        }); //Establece el escuchador de clics del botón de cancelar del contenido de eliminado.

        deleteDialog.show(); //Muestra el diálogo de eliminado.
    }

    @Override
    public void onBackPressed() {
        Utils.backMain(TaskActivity.this); //Regresa a la actividad principal.
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.task, menu); //Infla el menú de tarea en el diseño de esta actividad.
        return super.onCreateOptionsMenu(menu); //Devuelve el resultado de crear un menú de opciones en el diseño de esta actividad.
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId(); //Obtiene el identificador del elemento del menú y lo guarda.

        if (id == R.id.action_edit_task) { //Si el identificador es igual al de editar tarea.
            editTask(); //Abre la actividad de editar tarea para modificar la tarea actual.
            return true; //Devuelve verdadero.
        } else if (id == R.id.action_delete_task) { //Si el identificador es igual al de eliminar tarea.
            deleteTask(); //Abre el diálogo de eliminar tarea para elegir si eliminar la tarea actual o no.
            return true; //Devuelve verdadero.
        } else if (id == R.id.action_about_task) { //Si el identificador es igual al de acerca de en la tarea.
            Utils.showAbout(TaskActivity.this); //Muestra la actividad de acerca de.
            return true; //Devuelve verdadero.
        } else { //Si no es ninguno de los anteriores.
            return super.onOptionsItemSelected(item); //Devuelve el resultado de que un elemento de opciones sea seleccionado.
        }
    }
}

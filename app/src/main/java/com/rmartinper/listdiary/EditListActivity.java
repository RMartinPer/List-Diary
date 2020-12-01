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
import android.os.Build;
import android.os.Bundle;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.TooltipCompat;
import androidx.core.util.Pair;

import com.github.florent37.viewtooltip.ViewTooltip;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * A class that manages the Edit List Activity.
 *
 * @author Raul Martin Perez (raumar95@gmail.com)
 */
public class EditListActivity extends AppCompatActivity {

    /**
     * The pointer of the selected Field.
     */
    private int fieldPointer = -1; //Guarda el puntero del campo seleccionado.

    /**
     * The maximum position of the Fields.
     */
    private int fieldPosition = 1; //Guarda la posición máxima de los campos.

    /**
     * The ArrayList of the Fields.
     */
    private ArrayList<Field> fields = new ArrayList<>(); //Guarda la matriz de lista de campos.

    /**
     * The ArrayList of the remaining Fields.
     */
    private ArrayList<Field> remainingFields = new ArrayList<>(); //Guarda la matriz de lista de campos restantes.

    /**
     * The ArrayList of the deleted Fields.
     */
    private ArrayList<Field> deletedFields = new ArrayList<>(); //Guarda la matriz de lista de campos eliminados.

    /**
     * The ArrayList of the added Fields.
     */
    private ArrayList<Field> addedFields = new ArrayList<>(); //Guarda la matriz de lista de campos añadidos.

    /**
     * The ArrayList of the TextViews for the Fields.
     */
    private ArrayList<TextView> tvFields = new ArrayList<>(); //Guarda la matriz de lista de vistas de texto para los campos.

    /**
     * The current View of Tooltip.
     */
    private ViewTooltip currentTooltip; //Inicializa la información de herramienta actual.

    /**
     * The EditText of the name.
     */
    private EditText etEditName; //Inicializa el texto de edición del nombre.

    /**
     * The EditText of the Description.
     */
    private EditText etEditDesc; //Inicializa el texto de edición de la descripción.

    /**
     * The EditText of the Field.
     */
    private EditText etEditField; //Inicializa el texto de edición del campo.

    /**
     * The Layout of the Fields.
     */
    private LinearLayout layoutEditFields; //Inicializa el diseño de los campos.

    /**
     * The Map of the Lists.
     */
    private LinkedHashMap<Integer, List> lists = new LinkedHashMap<>(); //Guarda el mapa de las listas.

    /**
     * The Map of the Tasks.
     */
    private LinkedHashMap<Integer, Task> tasks = new LinkedHashMap<>(); //Guarda el mapa de las tareas.

    /**
     * The current List.
     */
    private List currentList; //Inicializa la lista actual.

    /**
     * The drop-down list of the types of Field.
     */
    private Spinner spinnerType; //Inicializa el desplegable de los tipos de campo.

    /**
     * The array of the Lists names.
     */
    private String[] names = new String[0]; //Guarda la matriz de los nombres de las listas.

    /**
     * The array of items in the drop-down list.
     */
    private String[] items = new String[0]; //Guarda la matriz de los elementos del desplegable.

    /**
     * The path of the Lists file.
     */
    private String listsPath; //Inicializa la ruta del archivo listas.

    /**
     * The path of the Tasks file.
     */
    private String tasksPath; //Inicializa la ruta del archivo tareas.

    /**
     * The header of the I/O exception of the Edit List Activity.
     */
    private static final String IO_EXC = "IOException: "; //Guarda el encabezado de la excepción de E/S.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState); //Inicializa todos los fragmentos de la actividad de editar lista.
        setContentView(R.layout.activity_edit_list); //Establece la vista del contenido de la actividad de editar lista.

        listsPath = getExternalFilesDir(null) + "/lists.xml"; //Guarda la ruta del archivo listas en el almacenamiento externo.
        tasksPath = getExternalFilesDir(null) + "/tasks.xml"; //Guarda la ruta del archivo tareas en el almacenamiento externo.

        etEditName = findViewById(R.id.et_name_edit_list); //Busca el texto de edición del nombre de la lista editada y lo guarda.
        etEditDesc = findViewById(R.id.et_desc_edit_list); //Busca el texto de edición de la descipción de la lista editada y lo guarda.
        etEditField = findViewById(R.id.et_field_edit_list); //Busca el texto de edición del campo de la lista editada y lo guarda.
        spinnerType = findViewById(R.id.spinner_edit_list); //Busca el desplegable de la lista editada y lo guarda.
        layoutEditFields = findViewById(R.id.layout_fields_edit_list); //Busca el diseño de los campos de la lista editada y lo guarda.
        ImageButton btnEditField = findViewById(R.id.btn_field_edit_list); //Busca el botón de agregar nuevo campo de la lista editada y lo guarda.
        FloatingActionButton fabEditCancel = findViewById(R.id.fab_cancel_edit_list); //Busca el botón de acción flotante de cancelar lista editada y lo guarda.
        FloatingActionButton fabEditDelete = findViewById(R.id.fab_delete_edit_field); //Busca el botón de acción flotante de eliminar campo de la lista editada y lo guarda.
        FloatingActionButton fabEditAccept = findViewById(R.id.fab_accept_edit_list); //Busca el botón de acción flotante de aceptar lista editada y lo guarda.

        etEditName.setRawInputType(InputType.TYPE_CLASS_TEXT); //Establece el tipo de entrada del texto del nombre como texto.
        etEditDesc.setRawInputType(InputType.TYPE_CLASS_TEXT); //Establece el tipo de entrada del texto de la descripción como texto.
        etEditField.setRawInputType(InputType.TYPE_CLASS_TEXT); //Establece el tipo de entrada del texto del campo como texto.

        btnEditField.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addEditField(); //Agrega el campo a la lista editada.
            }
        }); //Establece el escuchador de clics del botón de agregar nuevo campo.

        fabEditCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showEditList(true); //Muestra el diálogo de editar lista para elegir si salir de la lista editada o no.
            }
        }); //Establece el escuchador de clics del botón de acción flotante de cancelar lista editada.

        fabEditDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteEditField(); //Elimina el campo seleccionado de la lista editada.
            }
        }); //Establece el escuchador de clics del botón de acción flotante de eliminar campo.

        fabEditAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showEditList(false); //Muestra el diálogo de editar lista para elegir si guardar la lista editada o no.
            }
        }); //Establece el escuchador de clics del botón de acción flotante de aceptar lista editada.

        setCustomTooltip(btnEditField, ViewTooltip.Position.LEFT); //Establece la información de herramienta personalizada para el botón de agregar campo.

        TooltipCompat.setTooltipText(fabEditCancel, getString(R.string.hint_cancel_edit)); //Establece el texto de la información de herramienta del botón de acción flotante de cancelar lista editada.
        TooltipCompat.setTooltipText(fabEditDelete, getString(R.string.hint_delete_field)); //Establece el texto de la información de herramienta del botón de acción flotante de eliminar campo.
        TooltipCompat.setTooltipText(fabEditAccept, getString(R.string.hint_accept_edit)); //Establece el texto de la información de herramienta del botón de acción flotante de aceptar lista editada.

        items = getResources().getStringArray(R.array.spinner_items); //Obtiene la matriz de cadenas de los elementos del desplegable y la guarda.
        ArrayAdapter<String> adapter = new ArrayAdapter<>(EditListActivity.this, R.layout.spinner_item, items); //Crea un adaptador de matriz de los elementos y lo guarda.
        spinnerType.setAdapter(adapter); //Establece el adaptador del desplegable de los tipos.

        Serializable list = getIntent().getSerializableExtra("list");

        if (list != null) { //Si la lista no es un valor nulo.
            currentList = (List) list; //Adapta la lista y la guarda como la actual.

            String listName = currentList.getName(); //Obtiene el nombre de la lista actual y lo guarda.
            String listDesc = currentList.getDescription().getContent(); //Obtiene el contenido de la descripción de la lista actual y lo guarda.

            etEditName.setText(listName); //Establece el texto del texto de edición del nombre.
            etEditDesc.setText(listDesc); //Establece el texto del texto de edición de la descripción.

            showFields(); //Muestra los campos.
        }

        checkFiles(); //Comprueba los archivos para la aplicación.
    }

    /**
     * Shows the Fields in the activity.
     */
    protected void showFields() {
        ArrayList<Field> listFields = currentList.getFields(); //Obtiene los campos de la lista actual y los guarda.

        for (int i = 0; i < listFields.size(); i++) { //Por cada índice del 0 al final de los campos de la lista.
            Field field = listFields.get(i); //Obtiene el campo de la lista correspondiente y lo guarda.
            String fieldName = field.getName(); //Obtiene el nombre del campo y lo guarda.

            String fieldType; //Inicializa el tipo del campo.
            switch (field.getType()) { //Considera el tipo del campo.
                case "Number": //Tipo número.
                    fieldType = items[1]; //Guarda el tipo del campo como el segundo elemento.
                    break; //Corte.
                case "Date": //Tipo fecha
                    fieldType = items[2]; //Guarda el tipo del campo como el tercer elemento.
                    break; //Corte.
                default: //Si no es ninguno de los anteriores.
                    fieldType = items[0]; //Guarda el tipo del campo como el primer elemento.
                    break; //Corte.
            }

            String fieldDesc = getString(R.string.hint_show_field, fieldName, fieldType); //Obtiene la pista de mostrar el campo con su nombre y su tipo y la guarda como la descripción del campo.

            if ((i + 1) == listFields.size()) //Si el índice más 1 es igual al tamaño de los campos de la lista.
                fieldPosition = field.getId() + 1; //Obtiene el identificador del campo, lo incrementa en 1 y lo guarda como la posición del campo.

            TextView tvField = getFieldView(fieldName, fieldDesc); //Obtiene la vista del campo con su nombre y descripción y la guarda como una de texto.

            fields.add(field); //Agrega el campo a los campos.
            remainingFields.add(field); //Agrega el campo a los campos restantes.
            tvFields.add(tvField); //Agrega la vista de texto del campo a las vistas de texto de los campos.

            layoutEditFields.addView(tvField); //Agrega la vista de texto del campo al diseño de los campos.
        }
    }

    /**
     * Sets the custom tooltip for the view.
     * @param view the view from which the tooltip will be displayed.
     * @param position the position at which the tooltip will be shown.
     */
    protected void setCustomTooltip(View view, ViewTooltip.Position position) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) { //Si la version SDK de la compilacion actual del sistema es mayor o igual que O.
            view.setTag(position); //Establece la etiqueta de la vista como la posición.
            view.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    String vDesc = v.getContentDescription().toString(); //Obtiene el contenido de la descripción de la vista y lo guarda como la descripción de la vista.
                    ViewTooltip.Position pos = (ViewTooltip.Position) v.getTag(); //Obtiene la etiqueta de la vista y la guarda como la posición para la información de herramienta.

                    if (currentTooltip != null) //Si la información de herramienta actual no es un valor nulo.
                        currentTooltip.close(); //Cierra la información de herramienta actual.

                    currentTooltip = Utils.showCustomTooltip(EditListActivity.this, v, pos, vDesc); //Muestra la información de herramienta personalizada.
                    currentTooltip.show(); //Muestra la información de herramienta actual.

                    return true; //Devuelve verdadero.
                }
            }); //Establece el escuchador de clics de larga duración del botón de agregar nuevo campo.
        } else { //Si la version SDK es menor.
            String viewDesc = view.getContentDescription().toString(); //Obtiene el contenido de la descripción de la vista y lo guarda como la descripción de la vista.
            TooltipCompat.setTooltipText(view, viewDesc); //Establece el texto de la información de herramienta de la vista.
        }
    }

    /**
     * Saves the current List in the files of the app.
     */
    protected void saveCurrentList() {
        int currentId = currentList.getId(); //Obtiene el identificador de la lista actual y lo guarda.
        lists.put(currentId, currentList); //Agrega la lista actual a las listas en la posición del identificador.
        Utils.writeListsToFile(listsPath, lists); //Escribe las listas en el archivo de la ruta.

        ArrayList<Task> taskList = currentList.getTasks(); //Obtiene las tareas de la lista actual y las guarda.
        File tasksFile = new File(tasksPath); //Crea el archivo XML tareas del almacenamiento externo de la aplicación y lo guarda.
        boolean tasksExisted = tasksFile.exists(); //Guarda si el archivo tareas existe.

        if (taskList.isEmpty()) { //Si la lista de tareas está vacía.
            if (tasksExisted) { //Si el archivo tareas existe.
                boolean tasksDeleted = tasksFile.delete(); //Elimina el archivo tareas y guarda si lo consigue.
                String tasksLog = "Tasks file deleted: " + tasksDeleted; //Guarda el registro de las tareas.

                Logger logger = Logger.getLogger(EditListActivity.class.getName()); //Obtiene el registrador para la actividad de editar lista y lo guarda.
                logger.log(Level.INFO, tasksLog); //Registra al nivel de advertencia el registro de las tareas.
            }
        } else { //Si no está vacía.
            if (!tasksExisted) { //Si el archivo tareas no existe.
                try { //Trata de hacer el contenido del cuerpo.
                    tasksExisted = tasksFile.createNewFile(); //Crea un nuevo archivo vacío tareas y guarda si lo consigue.
                } catch (IOException e) { //Si se produce una excepción de E/S.
                    Toast.makeText(getApplicationContext(), IO_EXC + e.getLocalizedMessage(), Toast.LENGTH_SHORT).show(); //Crea un mensaje de duración corta del mensaje localizado de la excepción y lo muestra.
                }
            }

            if (tasksExisted) { //Si el archivo tareas existe.
                for (Task taskItem: taskList) { //Por cada elemento de la lista de tareas.
                    int id = taskItem.getId(); //Obtiene el identificador del elemento de la tarea y lo guarda.
                    tasks.put(id, taskItem); //Agrega el elemento de tarea a las tareas en la posición del identificador.
                }

                Utils.writeTasksToFile(tasksPath, tasks); //Escribe las tareas en el archivo de la ruta.
            }
        }
    }

    /**
     * Checks the files for the app.
     */
    protected void checkFiles() {
        File listsFile = new File(listsPath); //Crea el archivo XML listas del almacenamiento externo de la aplicación y lo guarda.

        if (listsFile.exists()) { //Si el archivo listas existe.
            parseFiles(); //Analiza los archivos.
        } else { //Si el archivo no existe.
            boolean listsCreated = false; //Guarda el indicador de que se ha creado el archivo listas como falso.

            try { //Trata de hacer el contenido del cuerpo.
                listsCreated = listsFile.createNewFile(); //Crea un nuevo archivo vacío listas y guarda si lo consigue.
            } catch (IOException e) { //Si se produce una excepción de E/S.
                Toast.makeText(getApplicationContext(), IO_EXC + e.getLocalizedMessage(), Toast.LENGTH_SHORT).show(); //Crea un mensaje de duración corta del mensaje localizado de la excepción y lo muestra.
            }

            if (listsCreated && (currentList != null)) //Si consiguió crear el archivo listas y la lista actual no es un valor nulo.
                saveCurrentList(); //Guarda la lista actual en los archivos de la aplicación.
        }
    }

    /**
     * Parses the files of the app.
     */
    protected void parseFiles() {
        names = Utils.parseListsFromFile(listsPath, lists); //Analiza las listas desde el archivo y las guarda junto a sus nombres.
        if (names.length > 0) { //Si la longitud de los nombres es mayor que 0.
            String[] titles = Utils.parseTasksFromFile(tasksPath, tasks); //Analiza las tareas desde el archivo y las guarda junto a sus títulos.
            if (titles.length > 0) //Si la longitud de los títulos es mayor que 0.
                Utils.addTasksToLists(lists, tasks); //Agrega las tareas a las listas.
        }
    }

    /**
     * Deletes the selected Field from the edited List.
     */
    protected void deleteEditField() {
        if (fieldPointer == -1) { //Si el puntero del campo es igual a -1.
            Toast.makeText(getApplicationContext(), getString(R.string.unselected_field_list), Toast.LENGTH_SHORT).show(); //Crea un mensaje de duración corta de campo no seleccionado de la lista y lo muestra.
        } else { //Si el puntero no es igual a -1.
            Field field = fields.get(fieldPointer); //Obtiene el campo del puntero y lo guarda.

            if (field.getId() == fieldPosition) { //Si el identificador del campo es igual que la posición del campo.
                addedFields.remove(field); //Elimina el campo de los campos añadidos.
            } else { //Si no es igual que la posición del campo.
                remainingFields.remove(field); //Elimina el campo de los campos restantes.
                deletedFields.add(field); //Agrega el campo de los campos eliminados.
            }

            fields.remove(fieldPointer); //Elimina el campo por su puntero de los campos.
            tvFields.remove(fieldPointer); //Elimina la vista de campo por su puntero de las vistas de campos.
            layoutEditFields.removeViewAt(fieldPointer); //Elimina la vista en el puntero del diseño de los campos.

            fieldPointer = -1; //Guarda el puntero del campo como -1.
        }
    }

    /**
     * Tries to add the Field by its name to the edited List.
     *
     * @param fieldName the name of the defined Field to add to the List.
     * @param strippedField the stripped name of the defined Field to add to the List.
     */
    protected void tryAddEditField(String fieldName, String strippedField) {
        boolean fieldExist = false; //Guarda el indicador de que el campo existe como falso.

        for (Field field: fields) { //Por cada campo de los campos.
            String name = field.getName(); //Obtiene el nombre del campo y lo guarda.
            String strippedName = Utils.stripAccents(name); //Despoja al nombre de los acentos y guarda el nombre despojado.

            if (strippedName.equals(strippedField)) { //Si los nombres despojados de los campos son iguales.
                fieldExist = true; //Establece que el campo existe.
                break; //Corte.
            }
        }

        if (fieldExist) { //Si el campo existe.
            Toast.makeText(getApplicationContext(), getString(R.string.existing_defined_field_list), Toast.LENGTH_SHORT).show(); //Crea un mensaje de duración corta de campo definido existente de la lista y lo muestra.
        } else { //Si no existe.
            String fieldType; //Inicializa el tipo del campo.

            int selection = spinnerType.getSelectedItemPosition(); //Obtiene la posición del elemento seleccionado del desplegable de tipos y la guarda.
            switch (selection) { //Considera la selección.
                case 1: //Selección 1.
                    fieldType = "Number"; //Guarda el tipo del campo como número.
                    break; //Corte.
                case 2: //Selección 2.
                    fieldType = "Date"; //Guarda el tipo del campo como fecha.
                    break; //Corte.
                default: //Si no es ninguno de los anteriores.
                    fieldType = "String"; //Guarda el tipo del campo como cadena.
                    break; //Corte.
            }

            String fieldDesc = getString(R.string.hint_show_field, fieldName, items[selection]); //Obtiene la pista de mostrar el campo con su nombre y el elemento de la selección y la guarda como la descripción del campo.

            Field field = new Field(fieldName, fieldType); //Crea un nuevo campo y lo guarda.
            TextView tvField = getFieldView(fieldName, fieldDesc); //Obtiene la vista del campo con su nombre y descripción y la guarda como una de texto.

            fields.add(field); //Agrega el campo a los campos.
            addedFields.add(field); //Agrega el campo a los campos añadidos.
            tvFields.add(tvField); //Agrega la vista de texto del campo a las vistas de texto de los campos.

            layoutEditFields.addView(tvField); //Agrega la vista de texto del campo al diseño de los campos.

            if (fieldPointer != -1) { //Si el puntero del campo no es igual a -1.
                TextView tvSelected = tvFields.get(fieldPointer); //Obtiene la vista de texto de campo del puntero y la guarda.
                tvSelected.setSelected(false); //Establece la vista de texto como no seleccionada.

                fieldPointer = -1; //Establece el puntero del campo como -1.
            }
        }
    }

    /**
     * Gets the View of the Field with the name and description given and returns it.
     *
     * @param fieldName the name of the Field for the View.
     * @param fieldDesc the description of the Field for the View.
     * @return the TextView for the Field.
     */
    private TextView getFieldView(String fieldName, String fieldDesc) {
        TextView tvField = (TextView) View.inflate(EditListActivity.this, R.layout.field_list_item, null); //Infla una vista de texto para el campo y lo guarda.

        tvField.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1)); //Establece los parámetros del diseño de la vista de texto del campo.
        tvField.setText(fieldName); //Establece el texto de la vista de texto del campo.
        tvField.setContentDescription(fieldDesc); //Establece el contenido de la descripción de la vista de texto del campo.
        tvField.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (fieldPointer != -1) { //Si el puntero del campo no es igual a -1.
                    TextView tvSelected = tvFields.get(fieldPointer); //Obtiene la vista de texto de campo del puntero y la guarda.
                    tvSelected.setSelected(false); //Establece la vista de texto como no seleccionada.
                }

                TextView tv = (TextView) v; //Adapta la vista como una vista de texto y la guarda.
                tv.setSelected(true); //Establece la vista de texto como seleccionada.

                fieldPointer = tvFields.indexOf(tv); //Obtiene el índice de la vista de texto de las de los campos y lo guarda como el puntero.
            }
        }); //Establece el escuchador de clics de la vista de texto del campo.

        setCustomTooltip(tvField, ViewTooltip.Position.TOP); //Establece la información de herramienta personalizada para la vista de texto del campo.

        return tvField; //Devuelve la vista de texto del campo.
    }

    /**
     * Adds the Field to the edited List.
     */
    protected void addEditField() {
        String fieldName = etEditField.getText().toString(); //Obtiene la cadena del texto de edición del campo y la guarda.

        if (fieldName.isEmpty()) { //Si el nombre del campo está vacío
            Toast.makeText(getApplicationContext(), getString(R.string.empty_field_list), Toast.LENGTH_SHORT).show(); //Crea un mensaje de duración corta de campo vacío de la lista y lo muestra.
        } else { //Si el nombre no está vacío.
            String strippedField = Utils.stripAccents(fieldName); //Despoja al nombre del campo de los acentos y guarda el campo despojado.

            String titleField = getString(R.string.task_title_field); //Obtiene la cadena campo título y la guarda como el campo del título.
            String descField = getString(R.string.list_task_desc_field); //Obtiene la cadena campo descripción y la guarda como el campo de la descripción.
            String startField = getString(R.string.task_start_field); //Obtiene la cadena campo fecha de inicio y la guarda como el campo de la fecha de inicio.

            if (strippedField.equals(titleField) || strippedField.equals(descField) ||
                    strippedField.equals(startField)) { //Si el campo despojado es igual al del título o al de la descripción o al de la fecha de inicio.
                Toast.makeText(getApplicationContext(), getString(R.string.existing_predefined_field_list), Toast.LENGTH_SHORT).show(); //Crea un mensaje de duración corta de campo predefinido existente de la lista y lo muestra.
            } else { //Si no es igual.
                tryAddEditField(fieldName, strippedField); //Intenta agregar el campo por su nombre a la lista.
            }

            etEditField.setText(""); //Borra el contenido del texto de edición del campo.
        }
    }

    /**
     * Shows the edit List dialog to choose whether to cancel or accept the edited List.
     *
     * @param cancel the indicator of whether or not it is the cancel dialog.
     */
    protected void showEditList(boolean cancel) {
        View editHeader = View.inflate(EditListActivity.this, R.layout.custom_dialog_header, null); //Infla la vista de la cabecera de edición y la guarda.

        ImageView editIcon = editHeader.findViewById(R.id.dialog_icon); //Busca el icono de la cabecera de edición y lo guarda.
        editIcon.setImageResource(R.drawable.ic_editing_pencil_blue_32dp); //Establece el recurso de la imagen del icono de edición.

        TextView editTitle = editHeader.findViewById(R.id.dialog_title); //Busca el título de la cabecera de edición y lo guarda.
        editTitle.setText(R.string.edit_list_header); //Establece el texto del título de adición.

        AlertDialog.Builder editBuilder = new AlertDialog.Builder(EditListActivity.this, 3); //Crea una nuevo constructor de diálogo de alerta y lo guarda.
        editBuilder.setCustomTitle(editHeader); //Establece el título personalizado del constructor de edición como la cabecera.

        View editContent = View.inflate(EditListActivity.this, R.layout.custom_dialog_content, null); //Infla la vista del contenido de edición y la guarda.

        final AlertDialog editDialog = editBuilder.create(); //Crea un diálogo de alerta con el constructor de edición y lo guarda.
        editDialog.setView(editContent); //Establece la vista del díalogo de edición como el contenido.

        TextView editText = editContent.findViewById(R.id.custom_info); //Busca la información del contenido de edición y lo guarda.
        Button acceptBtn = editContent.findViewById(R.id.custom_accept); //Busca el botón de aceptar del contenido de edición y lo guarda.

        if (cancel) { //Si es el diálogo para cancelar.
            editText.setText(R.string.are_sure_cancel_edit_list); //Establece el texto de edición como el de cancelar.

            acceptBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    editDialog.dismiss(); //Cierra el diálogo de edición.
                    Utils.backMain(EditListActivity.this); //Regresa a la actividad principal.
                }
            }); //Establece el escuchador de clics del botón de aceptar del contenido de edición.
        } else { //Si no lo es.
            editText.setText(R.string.are_sure_accept_edit_list); //Establece el texto de edición como el de aceptar.

            acceptBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    editDialog.dismiss(); //Cierra el diálogo de edición.
                    saveEditList(); //Guarda la lista editada.
                }
            }); //Establece el escuchador de clics del botón de aceptar del contenido de edición.
        }

        editContent.findViewById(R.id.custom_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editDialog.dismiss(); //Cierra el diálogo de edición.
            }
        }); //Establece el escuchador de clics del botón de cancelar del contenido de edición.

        Utils.hideKeyboard(EditListActivity.this); //Oculta el teclado mostrado en esta actividad.

        editDialog.show(); //Muestra el diálogo de edición.
    }

    /**
     * Gets the name of the edited List and returns it along with an indicator of whether it is valid.
     *
     * @return the name of the edited List and the indicator of whether it is valid.
     */
    protected Pair<String, Boolean> getListName() {
        String newName = etEditName.getText().toString(); //Obtiene la cadena del texto de edición del nombre y la guarda.
        boolean nameValid = true; //Guarda el indicador de que el nombre es válido como verdadero.

        if (newName.isEmpty()) { //Si el nuevo nombre está vacío.
            newName = currentList.getName(); //Obtiene el nombre de la lista actual y lo guarda como el nuevo.
        } else { //Si el nombre no está vacío.
            if (lists.size() > 1) { //Si el tamaño de las listas es mayor que 1.
                String currentName = currentList.getName(); //Obtiene el nombre de la lista actual y lo guarda.

                for (String name: names) //Por cada nombre de los nombres.
                    if (!name.equals(currentName) && name.equals(newName)) { //Si el nombre no es igual al actual y es igual al nuevo.
                        nameValid = false; //Establece que el nombre no es válido.
                        break; //Corte.
                    }
            }
        }

        return new Pair<>(newName, nameValid); //Devuelve el par del nuevo nombre y el indicador de si es válido.
    }

    /**
     * Saves the edited List.
     */
    protected void saveEditList() {
        String newDesc = etEditDesc.getText().toString(); //Obtiene la cadena del texto de edición de la descripción y la guarda.
        Pair<String, Boolean> namePair = getListName();//Obtiene el nombre de la lista editada y guarda su par del nombre y del indicador de su validez.
        boolean nameValid = namePair.second; //Obtiene el segundo del par del nombre y lo guarda como el indicador de si es un nombre válido.

        if (nameValid) { //Si el nombre es válido.
            String newName = namePair.first; //Obtiene el primero del par del nombre y lo guarda como el nuevo.
            String currentName = currentList.getName(); //Obtiene el nombre de la lista actual y lo guarda.

            if (!currentName.equals(newName)) //si el nombre actual no es igual al nombre.
                currentList.setName(newName); //Establece el nombre de la lista actual.

            Date newDate = new Date(); //Guarda la fecha actual como la nueva fecha.

            String currentDesc = currentList.getDescription().getContent(); //Obtiene el contenido de la descripción de la tarea actual y lo guarda como la descripción actual.

            if (!currentDesc.equals(newDesc)) { //Si la descripción actual no es igual a la nueva descripción.
                Description taskDesc = new Description(newDesc, newDate); //Crea una nueva descripción para la tarea y la guarda.
                currentList.setDescription(taskDesc); //Establece la descripción de la lista actual.
            }

            ArrayList<Field> newFields = remainingFields; //Guarda los nuevos campos como los restantes.

            for (Field field: addedFields) { //Por cada campo de los campos añadidos.
                field.setId(fieldPosition); //Establece el identificador del campo como la posición.
                newFields.add(field); //Agrega el campo a los nuevos campos.

                fieldPosition++; //Incrementa la posición del campo en 1.
            }

            currentList.setFields(newFields); //Establece los campos de la lista actual.

            int listId = currentList.getId(); //Obtiene el identificador de la lista actual y lo guarda.
            lists.put(listId, currentList); //Agrega la lista actual a las listas en el identificador de la lista.

            File listsFile = new File(listsPath); //Crea el archivo XML listas del almacenamiento externo de la aplicación y lo guarda.
            File tasksFile = new File(tasksPath); //Crea el archivo XML tareas del almacenamiento externo de la aplicación y lo guarda.
            boolean listsExisted = listsFile.exists(); //Guarda si el archivo listas existe.
            boolean tasksExisted = tasksFile.exists(); //Guarda si el archivo tareas existe.

            try { //Trata de hacer el contenido del cuerpo.
                if (!listsExisted) { //Si el archivo listas no existe.
                    listsExisted = listsFile.createNewFile(); //Crea un nuevo archivo listas y guarda si lo consigue.

                    if (tasksExisted) { //Si el archivo tareas existe.
                        boolean tasksDeleted = tasksFile.delete(); //Elimina el archivo tareas y guarda si lo consigue.
                        if (tasksDeleted) //Si el archivo tareas ha sido eliminado.
                            tasksExisted = tasksFile.createNewFile(); //Crea un nuevo archivo tareas y guarda si lo consigue.
                    }
                }

                if (!tasksExisted) //Si el archivo tareas no existe.
                    tasksExisted = tasksFile.createNewFile(); //Crea un nuevo archivo tareas y guarda si lo consigue.
            } catch (IOException e) { //Si se produce una excepción de E/S.
                Toast.makeText(getApplicationContext(), IO_EXC + e.getLocalizedMessage(), Toast.LENGTH_SHORT).show(); //Crea un mensaje de duración corta del mensaje localizado de la excepción y lo muestra.
            }

            boolean listsSaved = true; //Guarda el indicador de que las listas se han guardado.

            if (listsExisted) { //Si el archivo listas existe.
                String[] editedNames = Utils.writeListsToFile(listsPath, lists); //Escribe las listas en el archivo de la ruta y guarda sus nombres editados.
                listsSaved = (editedNames.length != 0); //Comprueba si la longitud de los nombres editados no es igual a 0 y guarda el resultado.
            }

            ArrayList<Task> currentTasks = currentList.getTasks(); //Obtiene las tareas de la lista actual y las guarda.
            boolean tasksSaved = true; //Guarda el indicador de que las tareas se han guardado.

            if (!currentTasks.isEmpty()) { //Si las tareas actuales no están vacías.
                for (Task task: currentTasks) { //Por cada tarea de las tareas actuales.
                    ArrayList<History> newHistories = new ArrayList<>(); //Crea una matriz de lista de los historiales y la guarda.

                    for (Field deletedField: deletedFields) { //Por cada campo de los campos eliminados.
                        int deletedId = deletedField.getId(); //Obtiene el identificador del campo eliminado y lo guarda.
                        String fieldName = deletedField.getName(); //Obtiene el nombre del campo eliminado y lo guarda.

                        Value deletedValue = task.getValue(deletedId); //Obtiene el valor asociado al identificador eliminado de la tarea y lo guarda.
                        String valueContent = ""; //Guarda el contenido del valor como una cadena vacía.

                        if (deletedValue != null) //Si el valor eliminado no es un valor nulo.
                            valueContent = deletedValue.getContent(); //Obtiene el contenido del valor eliminado y lo guarda.

                        History fieldHistory = new History(fieldName, valueContent, History.Action.DELETE); //Crea una nueva historia del campo y la guarda.
                        newHistories.add(fieldHistory); //Agrega la historia del campo a las nuevas historias.

                        task.removeValue(deletedId); //Elimina el valor del identificador de eliminado de la tarea.
                    }

                    for (Field addedField: addedFields) { //Por cada campo de los campos añadidos.
                        int addedId = addedField.getId(); //Obtiene el identificador del campo añadido y lo guarda.
                        String fieldName = addedField.getName(); //Obtiene el nombre del campo añadido y lo guarda.

                        History fieldHistory = new History(fieldName, "", History.Action.CREATE); //Crea una nueva historia del campo y la guarda.
                        newHistories.add(fieldHistory); //Agrega la historia del campo a las nuevas historias.

                        Value value = new Value(addedId, newDate); //Crea un nuevo valor //Crea un nuevo valor y lo guarda.
                        task.addValue(value); //Agrega el valor a la tarea.
                    }

                    if (!newHistories.isEmpty()) { //Si las nuevas historias no están vacías.
                        ArrayList<Record> records = task.getRecords(); //Obtiene los historiales de la tarea y los guarda.
                        Record newRecord = new Record(newHistories, newDate); //Crea un nuevo historial y lo guarda.
                        records.add(newRecord); //Agrega el nuevo historial a los historiales.
                    }

                    int taskId = task.getId(); //Obtiene el identificador de la tarea y lo guarda.
                    tasks.put(taskId, task); //Agrega la tarea a las tareas en el identificador de la tarea.
                }

                if (tasksExisted) { //Si el archivo tareas existe.
                    String[] editedTitles = Utils.writeTasksToFile(tasksPath, tasks); //Escribe las tareas en el archivo de la ruta y guarda sus títulos editados.
                    tasksSaved = (editedTitles.length != 0); //Comprueba si la longitud de los títulos editados no es igual a 0 y guarda el resultado.
                }
            }

            if (listsSaved && tasksSaved) { //Si las listas y las tareas se han guardado.
                Toast.makeText(getApplicationContext(), getString(R.string.edited_list), Toast.LENGTH_SHORT).show(); //Crea un mensaje de duración corta de la lista editada y lo muestra.

                etEditName.setText(""); //Borra el contenido del texto de edición del nombre.
                etEditDesc.setText(""); //Borra el contenido del texto de edición de la descipción.
                etEditField.setText(""); //Borra el contenido del texto de edición del campo.

                layoutEditFields.removeAllViewsInLayout(); //Elimina todas las vistas del diseño de los campos.

                Utils.backMain(EditListActivity.this); //Regresa a la actividad principal.
            }
        }
    }

    @Override
    public void onBackPressed() {
        Utils.backMain(EditListActivity.this); //Regresa a la actividad principal.
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.about, menu); //Infla el menú de acerca de en el diseño de esta actividad.
        return super.onCreateOptionsMenu(menu); //Devuelve el resultado de crear un menú de opciones en el diseño de esta actividad.
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_about) { //Si el identificador del elemento del menú es igual al de acerca de.
            Utils.showAbout(EditListActivity.this); //Muestra la actividad de acerca de.
            return true; //Devuelve verdadero.
        } else { //Si no es igual.
            return super.onOptionsItemSelected(item); //Devuelve el resultado de que un elemento de opciones sea seleccionado.
        }
    }
}

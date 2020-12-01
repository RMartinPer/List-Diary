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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * A class that manages the New List Activity.
 *
 * @author Raul Martin Perez (raumar95@gmail.com)
 */
public class NewListActivity extends AppCompatActivity {

    /**
     * The pointer of the selected Field.
     */
    private int fieldPointer = -1; //Guarda el puntero del campo seleccionado.

    /**
     * The ArrayList of the Fields.
     */
    private ArrayList<Field> fields = new ArrayList<>(); //Guarda la matriz de lista de campos.

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
    private EditText etNewName; //Inicializa el texto de edición del nombre.

    /**
     * The EditText of the Description.
     */
    private EditText etNewDesc; //Inicializa el texto de edición de la descripción.

    /**
     * The EditText of the Field.
     */
    private EditText etNewField; //Inicializa el texto de edición del campo.

    /**
     * The Layout of the Fields.
     */
    private LinearLayout layoutNewFields; //Inicializa el diseño de los campos.

    /**
     * The Map of the Lists.
     */
    private LinkedHashMap<Integer, List> lists = new LinkedHashMap<>(); //Guarda el mapa de las listas.

    /**
     * The drop-down list of the types of Field.
     */
    private Spinner spinnerType; //Inicializa el desplegable de los tipos de campo.

    /**
     * The array of the Lists names.
     */
    private String[] names = new String[0]; //Guarda la matriz de los nombres de las listas.

    /**
     * The path of the Lists file.
     */
    private String listsPath; //Inicializa la ruta del archivo listas.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState); //Inicializa todos los fragmentos de la actividad de nueva lista.
        setContentView(R.layout.activity_new_list); //Establece la vista del contenido de la actividad de nueva lista.

        listsPath = getExternalFilesDir(null) + "/lists.xml"; //Guarda la ruta del archivo listas en el almacenamiento externo.

        etNewName = findViewById(R.id.et_name_new_list); //Busca el texto de edición del nombre de la nueva lista y lo guarda.
        etNewDesc = findViewById(R.id.et_desc_new_list); //Busca el texto de edición de la descipción de la nueva lista y lo guarda.
        etNewField = findViewById(R.id.et_field_new_list); //Busca el texto de edición del campo de la nueva lista y lo guarda.
        spinnerType = findViewById(R.id.spinner_new_list); //Busca el desplegable de la nueva lista y lo guarda.
        layoutNewFields = findViewById(R.id.layout_fields_new_list); //Busca el diseño de los campos de la nueva lista y lo guarda.
        ImageButton btnNewField = findViewById(R.id.btn_field_new_list); //Busca el botón de agregar nuevo campo de la nueva lista y lo guarda.
        FloatingActionButton fabNewCancel = findViewById(R.id.fab_cancel_new_list); //Busca el botón de acción flotante de cancelar nueva lista y lo guarda.
        FloatingActionButton fabNewDelete = findViewById(R.id.fab_delete_new_field); //Busca el botón de acción flotante de eliminar campo de la nueva lista y lo guarda.
        FloatingActionButton fabNewAccept = findViewById(R.id.fab_accept_new_list); //Busca el botón de acción flotante de aceptar nueva lista y lo guarda.

        etNewName.setRawInputType(InputType.TYPE_CLASS_TEXT); //Establece el tipo de entrada del texto del nombre como texto.
        etNewDesc.setRawInputType(InputType.TYPE_CLASS_TEXT); //Establece el tipo de entrada del texto de la descripción como texto.
        etNewField.setRawInputType(InputType.TYPE_CLASS_TEXT); //Establece el tipo de entrada del texto del campo como texto.

        btnNewField.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addNewField(); //Agrega el campo a la nueva lista.
            }
        }); //Establece el escuchador de clics del botón de agregar nuevo campo.

        fabNewCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAddList(true); //Muestra el diálogo de agregar lista para elegir si salir de la nueva lista o no.
            }
        }); //Establece el escuchador de clics del botón de acción flotante de cancelar nueva lista.

        fabNewDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteNewField(); //Elimina el campo seleccionado de la nueva lista.
            }
        }); //Establece el escuchador de clics del botón de acción flotante de eliminar campo.

        fabNewAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAddList(false); //Muestra el diálogo de agregar lista para elegir si guardar la nueva lista o no.
            }
        }); //Establece el escuchador de clics del botón de acción flotante de aceptar nueva lista.

        setCustomTooltip(btnNewField, ViewTooltip.Position.LEFT); //Establece la información de herramienta personalizada para el botón de agregar campo.

        TooltipCompat.setTooltipText(fabNewCancel, getString(R.string.hint_cancel_list)); //Establece el texto de la información de herramienta del botón de acción flotante de cancelar nueva lista.
        TooltipCompat.setTooltipText(fabNewDelete, getString(R.string.hint_delete_field)); //Establece el texto de la información de herramienta del botón de acción flotante de eliminar campo.
        TooltipCompat.setTooltipText(fabNewAccept, getString(R.string.hint_accept_list)); //Establece el texto de la información de herramienta del botón de acción flotante de aceptar nueva lista.

        String[] items = getResources().getStringArray(R.array.spinner_items); //Obtiene la matriz de cadenas de los elementos del desplegable y la guarda.
        ArrayAdapter<String> adapter = new ArrayAdapter<>(NewListActivity.this, R.layout.spinner_item, items); //Crea un adaptador de matriz de los elementos y lo guarda.
        spinnerType.setAdapter(adapter); //Establece el adaptador del desplegable de los tipos.

        File listsFile = new File(listsPath); //Crea el archivo XML listas del almacenamiento externo de la aplicación y lo guarda.

        if (listsFile.exists()) { //Si el archivo listas existe.
            names = Utils.parseListsFromFile(listsPath, lists); //Analiza las listas desde el archivo y las guarda junto con sus nombres.
        } else { //Si el archivo no existe.
            deleteTasksFile(); //Elimina el archivo tareas.

            try { //Trata de hacer el contenido del cuerpo.
                boolean listsCreated = listsFile.createNewFile(); //Crea un nuevo archivo vacío listas y guarda si lo consigue.

                if (listsCreated) { //Si consiguió crear el archivo listas.
                    Date creationDate = new Date(); //Guarda la fecha actual como la fecha de creación.
                    Description descList = new Description("", creationDate); //Crea una descripción para la lista y la guarda.
                    List currentList = new List(getString(R.string.no_name), descList); //Crea una lista sin nombre y la guarda como la actual.

                    lists.put(1, currentList); //Agrega la lista actual a las listas en la posición 1.
                    Utils.writeListsToFile(listsPath, lists); //Escribe las listas en el archivo de la ruta.
                }
            } catch (IOException e) { //Si se produce una excepción de E/S.
                Toast.makeText(getApplicationContext(), "IOException: " + e.getLocalizedMessage(), Toast.LENGTH_SHORT).show(); //Crea un mensaje de duración corta del mensaje localizado de la excepción y lo muestra.
            }
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

                    currentTooltip = Utils.showCustomTooltip(NewListActivity.this, v, pos, vDesc); //Muestra la información de herramienta personalizada.
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
     * Deletes the Tasks file of the app.
     */
    protected void deleteTasksFile() {
        File tasksFile = new File(getExternalFilesDir(null) + "/tasks.xml"); //Crea el archivo XML tareas del almacenamiento externo de la aplicación y lo guarda.

        if (tasksFile.exists()) { //Si el archivo tareas existe.
            boolean tasksDeleted = tasksFile.delete(); //Elimina el archivo tareas y guarda si lo consigue.
            String tasksLog = "Tasks file deleted: " + tasksDeleted; //Guarda el registro de las tareas.

            Logger logger = Logger.getLogger(NewListActivity.class.getName()); //Obtiene el registrador para la actividad de nueva lista y lo guarda.
            logger.log(Level.INFO, tasksLog); //Registra al nivel de advertencia el registro de las tareas.
        }
    }

    /**
     * Deletes the selected Field from the new List.
     */
    protected void deleteNewField() {
        if (fieldPointer == -1) { //Si el puntero del campo es igual a -1.
            Toast.makeText(getApplicationContext(), getString(R.string.unselected_field_list), Toast.LENGTH_SHORT).show(); //Crea un mensaje de duración corta de campo no seleccionado de la lista y lo muestra.
        } else { //Si el puntero no es igual a -1.
            fields.remove(fieldPointer); //Elimina el campo por su puntero de los campos.
            tvFields.remove(fieldPointer); //Elimina la vista de campo por su puntero de las vistas de campos.
            layoutNewFields.removeViewAt(fieldPointer); //Elimina la vista en el puntero del diseño de los campos.

            fieldPointer = -1; //Guarda el puntero del campo como -1.
        }
    }

    /**
     * Tries to add the Field by its name to the new List.
     *
     * @param fieldName the name of the defined Field to add to the List.
     * @param strippedField the stripped name of the defined Field to add to the List.
     */
    protected void tryAddNewField(String fieldName, String strippedField) {
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

            String fieldDesc = getString(R.string.hint_show_field, fieldName, fieldType); //Obtiene la pista de mostrar el campo con su nombre y su tipo y la guarda como la descripción del campo.

            Field field = new Field(fieldName, fieldType); //Crea un nuevo campo y lo guarda.
            TextView tvField = (TextView) View.inflate(NewListActivity.this, R.layout.field_list_item, null); //Infla una vista de texto para el campo y lo guarda.

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

            fields.add(field); //Agrega el campo a los campos.
            tvFields.add(tvField); //Agrega la vista de texto del campo a las vistas de texto de los campos.
            layoutNewFields.addView(tvField); //Agrega la vista de texto del campo al diseño de los campos.

            if (fieldPointer != -1) { //Si el puntero del campo no es igual a -1.
                TextView tvSelected = tvFields.get(fieldPointer); //Obtiene la vista de texto de campo del puntero y la guarda.
                tvSelected.setSelected(false); //Establece la vista de texto como no seleccionada.

                fieldPointer = -1; //Establece el puntero del campo como -1.
            }
        }
    }

    /**
     * Adds the Field to the new List.
     */
    protected void addNewField() {
        final String fieldName = etNewField.getText().toString(); //Obtiene la cadena del texto de edición del campo y la guarda.

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
                tryAddNewField(fieldName, strippedField); //Intenta agregar el campo por su nombre a la lista.
            }

            etNewField.setText(""); //Borra el contenido del texto de edición del campo.
        }
    }

    /**
     * Shows the add List dialog to choose whether to cancel or accept the new List.
     *
     * @param cancel the indicator of whether or not it is the cancel dialog.
     */
    protected void showAddList(boolean cancel) {
        View addHeader = View.inflate(NewListActivity.this, R.layout.custom_dialog_header, null); //Infla la vista de la cabecera de adición y la guarda.

        ImageView addIcon = addHeader.findViewById(R.id.dialog_icon); //Busca el icono de la cabecera de adición y lo guarda.
        addIcon.setImageResource(R.drawable.ic_plus_blue_32dp); //Establece el recurso de la imagen del icono de adición.

        TextView addTitle = addHeader.findViewById(R.id.dialog_title); //Busca el título de la cabecera de adición y lo guarda.
        addTitle.setText(R.string.add_list_header); //Establece el texto del título de adición.

        AlertDialog.Builder addBuilder = new AlertDialog.Builder(NewListActivity.this, 3); //Crea una nuevo constructor de diálogo de alerta y lo guarda.
        addBuilder.setCustomTitle(addHeader); //Establece el título personalizado del constructor de adición como la cabecera.

        View addContent = View.inflate(NewListActivity.this, R.layout.custom_dialog_content, null); //Infla la vista del contenido de adición y la guarda.

        final AlertDialog addDialog = addBuilder.create(); //Crea un diálogo de alerta con el constructor de adición y lo guarda.
        addDialog.setView(addContent); //Establece la vista del díalogo de adición como el contenido.

        TextView addText = addContent.findViewById(R.id.custom_info); //Busca la información del contenido de adición y lo guarda.
        Button acceptBtn = addContent.findViewById(R.id.custom_accept); //Busca el botón de aceptar del contenido de adición y lo guarda.

        if (cancel) { //Si es el diálogo para cancelar.
            addText.setText(R.string.are_sure_cancel_new_list); //Establece el texto de adición como el de cancelar.

            acceptBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    addDialog.dismiss(); //Cierra el diálogo de adición.
                    Utils.backMain(NewListActivity.this); //Regresa a la actividad principal.
                }
            }); //Establece el escuchador de clics del botón de aceptar del contenido de adición.
        } else { //Si no lo es.
            addText.setText(R.string.are_sure_accept_new_list); //Establece el texto de adición como el de aceptar.

            acceptBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    addDialog.dismiss(); //Cierra el diálogo de adición.
                    saveNewList(); //Guarda la nueva lista.
                }
            }); //Establece el escuchador de clics del botón de aceptar del contenido de adición.
        }

        addContent.findViewById(R.id.custom_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addDialog.dismiss(); //Cierra el diálogo de adición.
            }
        }); //Establece el escuchador de clics del botón de cancelar del contenido de adición.

        Utils.hideKeyboard(NewListActivity.this); //Oculta el teclado mostrado en esta actividad.

        addDialog.show(); //Muestra el diálogo de adición.
    }

    /**
     * Gets the name of the new List and returns it along with an indicator of whether it is valid.
     *
     * @return the name of the new List and the indicator of whether it is valid.
     */
    protected Pair<String, Boolean> getListName() {
        String listName = etNewName.getText().toString(); //Obtiene la cadena del texto de edición del nombre y la guarda.
        boolean nameValid = true; //Guarda el indicador de que el nombre es válido como verdadero.

        if (listName.isEmpty()) { //Si el nombre de la lista está vacío.
            listName = getString(R.string.no_name); //Obtiene la cadena sin nombre y la guarda como el nombre de la lista.
            int cont = 1; //Guarda el contador como 1.

            java.util.List<String> namesList = Arrays.asList(names); //Convierte la matriz de nombres a una lista y la guarda.

            do { //Hace el contenido del cuerpo.
                nameValid = true; //Establece que el nombre es válido.

                if (namesList.contains(listName)) { //Si la lista de nombres contiene el nombre de la lista.
                    nameValid = false; //Establece que el nombre no es válido.
                    listName = getString(R.string.no_name) + " (" + cont + ")"; //Obtiene la cadena sin nombre con el contador y la guarda como el nombre de la lista.
                    cont++; //Incrementa el contador.
                }
            } while (!nameValid); //Mientras el nombre no sea válido.
        } else { //Si el nombre no está vacío.
            String strippedList = Utils.stripAccents(listName); //Despoja al nombre de la lista de los acentos y guarda la lista despojada.

            for (String name: names) { //Por cada nombre de los nombres.
                String strippedName = Utils.stripAccents(name); //Despoja al nombre de los acentos y guarda el nombre despojado.

                if (strippedList.equals(strippedName)) { //Si los nombres despojados de las listas son iguales.
                    nameValid = false; //Establece que el nombre no es válido.
                    break; //Corte.
                }
            }
        }

        return new Pair<>(listName, nameValid); //Devuelve el par del nombre de la lista y el indicador de si es válido.
    }

    /**
     * Saves the new List.
     */
    protected void saveNewList() {
        String newDesc = etNewDesc.getText().toString(); //Obtiene la cadena del texto de edición de la descripción y la guarda.
        Pair<String, Boolean> namePair = getListName();//Obtiene el nombre de la nueva lista y guarda su par del nombre y del indicador de su validez.
        boolean nameValid = namePair.second; //Obtiene el segundo del par del nombre y lo guarda como el indicador de si es un nombre válido.

        if (nameValid) { //Si el nombre es válido.
            Integer[] ids = lists.keySet().toArray(new Integer[0]); //Obtiene la matriz de identificadores de las claves de las listas y las guarda.
            int listId = ids[ids.length - 1]; //Obtiene el último identificador y lo guarda.
            listId++; //Incrementa el identificador de la lista.

            String listName = namePair.first; //Obtiene el primero del par del nombre y lo guarda como el de la lista.
            Date listDate = new Date(); //Guarda la fecha actual como la fecha de la lista.
            Description listDesc = new Description(newDesc);

            for (int i = 0; i < fields.size(); i++) { //Por cada índice del 0 al final de ls campos.
                int fieldId = i + 1; //Guarda el identificador del campo como el índice más 1.
                Field field = fields.get(i); //Obtiene el campo correspondiente y lo guarda.
                field.setId(fieldId); //Establece el identificador del campo.

                Date date = new Date(); //Guarda la fecha actual.
                field.setCreationDate(date); //Establece la fecha de creación del campo.
            }

            List newList = new List(listId, listName, listDesc, fields, listDate); //crea una nueva lista y la guarda.
            lists.put(listId, newList); //Agrega la nueva lista a las listas en el identificador de la lista.

            File listsFile = new File(listsPath); //Crea el archivo XML listas del almacenamiento externo de la aplicación y lo guarda.
            boolean listsExisted = listsFile.exists(); //Guarda si el archivo listas existe.

            if (!listsExisted) { //Si el archivo listas no existe.
                try { //Trata de hacer el contenido del cuerpo.
                    listsExisted = listsFile.createNewFile(); //Crea un nuevo archivo listas y guarda si lo consigue.
                } catch (IOException e) { //Si se produce una excepción de E/S.
                    Toast.makeText(getApplicationContext(), "IOException: " + e.getLocalizedMessage(), Toast.LENGTH_SHORT).show(); //Crea un mensaje de duración corta del mensaje localizado de la excepción y lo muestra.
                }

                deleteTasksFile(); //Elimina el archivo tareas.
            }

            if (listsExisted) { //Si el archivo listas existe.
                String[] newNames = Utils.writeListsToFile(listsPath, lists); //Escribe las listas en el archivo de la ruta y guarda sus nombres nuevos.
                if (newNames.length > 0) { //Si la longitud de los nombres nuevos es mayor que 0.
                    Toast.makeText(getApplicationContext(), getString(R.string.created_new_list), Toast.LENGTH_SHORT).show(); //Crea un mensaje de duración corta de la nueva lista creada y lo muestra.

                    etNewName.setText(""); //Borra el contenido del texto de edición del nombre.
                    etNewDesc.setText(""); //Borra el contenido del texto de edición de la descipción.
                    etNewField.setText(""); //Borra el contenido del texto de edición del campo.

                    layoutNewFields.removeAllViewsInLayout(); //Elimina todas las vistas del diseño de los campos.

                    Utils.backMain(NewListActivity.this); //Regresa a la actividad principal.
                }
            }
        } else { //Si el nombre no es válido.
            Toast.makeText(getApplicationContext(), getString(R.string.invalid_name_new_list), Toast.LENGTH_SHORT).show(); //Crea un mensaje de duración corta de nombre inválido de la nueva lista y lo muestra.
            etNewName.setText(""); //Borra el contenido del texto de edición del nombre.
        }
    }

    @Override
    public void onBackPressed() {
        Utils.backMain(NewListActivity.this); //Regresa a la actividad principal.
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.about, menu); //Infla el menú de acerca de en el diseño de esta actividad.
        return super.onCreateOptionsMenu(menu); //Devuelve el resultado de crear un menú de opciones en el diseño de esta actividad.
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_about) { //Si el identificador del elemento del menú es igual al de acerca de.
            Utils.showAbout(NewListActivity.this); //Muestra la actividad de acerca de.
            return true; //Devuelve verdadero.
        } else { //Si no es igual.
            return super.onOptionsItemSelected(item); //Devuelve el resultado de que un elemento de opciones sea seleccionado.
        }
    }
}

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
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.appcompat.widget.TooltipCompat;
import androidx.core.util.Pair;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.rmartinper.filepicker.controller.DialogSelectionListener;
import com.rmartinper.filepicker.model.DialogConfigs;
import com.rmartinper.filepicker.model.DialogProperties;
import com.rmartinper.filepicker.view.FilePickerDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * A class that manages the Main Activity.
 *
 * @author Raul Martin Perez (raumar95@gmail.com)
 */
public class MainActivity extends AppCompatActivity {

    /**
     * The indicator that determines if you have pressed the Back button to exit.
     */
    private boolean doubleBackToExit = false; //Guarda el indicador del botón atrás pulsado para salir.

    /**
     * The Drawer.
     */
    private DrawerLayout drawer; //Inicializa el cajón.

    /**
     * The Floating Action Button for a new Task.
     */
    private FloatingActionButton fabTask; //Inicializa el botón de acción flotante para una nueva tarea.

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
     * The List View of the Navigation View.
     */
    private ListView navView; //Inicializa la vista de lista de la vista de navegación.

    /**
     * The List View of the Tasks.
     */
    private ListView taskView; //Inicializa la vista de lista de las tareas.

    /**
     * The List View of the Tasks when doing a search.
     */
    private ListView taskSearchView; //Inicializa la vista de lista de las tareas al hacer una búsqueda.

    /**
     * The Text View of the List description.
     */
    private TextView tvDesc; //Inicializa la vista de texto de la descripción de la Lista.

    /**
     * The array of the Lists names.
     */
    private String[] names = new String[0]; //Guarda la matriz de los nombres de las listas.

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
     * The first key String with which to encrypt and decrypt Lists and Tasks.
     */
    private static final String FIRST_KEY = "lPJRZJJbkSo858buIIc5PB3nEg8Z1kKt"; //Guarda la cadena de la primera clave para encriptar y desencriptar listas y tareas.

    /**
     * The first initialization vector String with which to encrypt and decrypt Lists and Tasks.
     */
    private static final String FIRST_IV = "lIB5GvX8RO3tE0Xv"; //Guarda la cadena del primer vector de inicialización para encriptar y desencriptar listas y tareas.

    /**
     * The second key String with which to encrypt and decrypt Lists and Tasks.
     */
    private static final String SECOND_KEY = "tpaz6YJF94QOF6wP5acQE6OwPsYDTJKh"; //Guarda la cadena de la segunda clave para encriptar y desencriptar listas y tareas.

    /**
     * The second initialization vector String with which to encrypt and decrypt Lists and Tasks.
     */
    private static final String SECOND_IV = "tqCOydNpVaA73vHf"; //Guarda la cadena del segundo vector de inicialización para encriptar y desencriptar listas y tareas.

    /**
     * The separator of the data files.
     */
    private static final String SEP = "<listdiary>"; //Guarda el separador de los archivos de los datos.

    /**
     * The header of the I/O exception of the Main Activity.
     */
    private static final String IO_EXC = "IOException: "; //Guarda el encabezado de la excepción de E/S.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState); //Inicializa todos los fragmentos de la actividad principal.
        setContentView(R.layout.activity_main); //Establece la vista del contenido de la actividad principal.

        navView = findViewById(R.id.list_nav); //Busca la vista de lista de la navegación y la guarda.
        tvDesc = findViewById(R.id.tv_desc_main); //Busca la vista de texto de la descripción del principal y la guarda.
        taskView = findViewById(R.id.list_tasks); //Busca la vista de lista de las tareas y la guarda.
        taskSearchView = findViewById(R.id.list_tasks_search); //Busca la vista de lista de las tareas cuando buscas y la guarda.
        drawer = findViewById(R.id.layout_drawer); //Busca el diseño del cajón y lo guarda.
        fabTask = findViewById(R.id.fab_add_new_task); //Busca el botón de acción flotante de agregar nueva tarea y lo guarda.
        FloatingActionButton fabSettings = findViewById(R.id.fab_settings); //Busca el botón de acción flotante de configuración y lo guarda.
        FloatingActionButton fabList = findViewById(R.id.fab_add_new_list); //Busca el botón de acción flotante de agregar nueva lista y lo guarda.
        Toolbar toolbar = findViewById(R.id.toolbar_main); //Busca la barra de herramientas del principal y la guarda.
        setSupportActionBar(toolbar); //Establece la barra de acción de soporte como la barra de herramientas.

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M //Si la version SDK de la compilacion actual del sistema es mayor que Marshmallow.
                && checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) //Y no tiene permiso concedido para escribir en el almacenamiento externo.
            requestPermissions(new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE}, 100); //Solicita permiso para escribir en el almacenamiento externo con código 100.

        File filesDir = getExternalFilesDir(null); //Obtiene el directorio de los archivos en el almacenamiento externo y lo guarda.
        if (filesDir == null) //Si el directorio de los archivos es un valor nulo.
            filesDir = getFilesDir(); //Obtiene el directorio de los archivos y lo guarda.

        if (filesDir != null) { //Si el directorio de los archivos no es un valor nulo.
            listsPath = filesDir.getPath() + "/lists.xml"; //Guarda la ruta del archivo listas en el directorio de los archivos.
            tasksPath = filesDir.getPath() + "/tasks.xml"; //Guarda la ruta del archivo tareas en el directorio de los archivos.
        }

        checkFiles(); //Comprueba los archivos para la aplicación.

        fabTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addNewTask(); //Abre la actividad de nueva tarea para crear una nueva tarea y agregarla a la lista actual
            }
        }); //Establece el escuchador de clics del botón de acción flotante de agregar nueva tarea.

        fabSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawer.closeDrawer(GravityCompat.START); //Cierra el cajón.
                showSettings(); //Muestra el diálogo de la configuración para importar o exportar datos.
            }
        }); //Establece el escuchador de clics del botón de acción flotante de la configuración.

        fabList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawer.closeDrawer(GravityCompat.START); //Cierra el cajón.
                addNewList(); //Abre la actividad de nueva lista para crear una nueva lista y agregarla a la aplicación
            }
        }); //Establece el escuchador de clics del botón de acción flotante de agregar nueva lista.

        TooltipCompat.setTooltipText(fabTask, getString(R.string.hint_add_task)); //Establece el texto de la información de herramienta del botón de acción flotante de agregar nueva tarea.
        TooltipCompat.setTooltipText(fabSettings, getString(R.string.hint_settings)); //Establece el texto de la información de herramienta del botón de acción flotante de configuración.
        TooltipCompat.setTooltipText(fabList, getString(R.string.hint_add_list)); //Establece el texto de la información de herramienta del botón de acción flotante de agregar nueva lista.

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(MainActivity.this, drawer,
                toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close); //Crea un nuevo indicador del cajón de la barra de acción y lo guarda.
        drawer.addDrawerListener(toggle); //Agrega el escuchador del cajón.
        toggle.syncState(); //Sincroniza el indicador con el estado del cajón.

        ArrayAdapter<String> adapter = new ArrayAdapter<>(MainActivity.this, R.layout.nav_list_item, names); //Crea un adaptador de matriz de los nombres y lo guarda.
        navView.setAdapter(adapter); //Establece el adaptador de la vista de lista de la nacegación.
        navView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                drawer.closeDrawer(GravityCompat.START); //Cierra el cajón.

                List[] listsValues = lists.values().toArray(new List[0]); //Guarda los valores de las listas.

                if (listsValues.length > position) { //Si la longitud de los valores de las listas es mayor que la posición.
                    currentList = listsValues[position]; //Accede al valor de la lista de la posición y lo guarda como la lista actual.
                    showCurrentList(); //Muestra la lista actual en el diseño.
                }
            }
        }); //Establece el escuchador de clics en los elementos de la vista de navegación.

        taskView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Task currentTask = currentList.getTask(position); //Obtiene la tarea de la posición de la lista actual y la guarda como la actual.

                Intent task = new Intent(MainActivity.this, TaskActivity.class); //Crea una intención de pasar de la actividad principal a la de tarea y la guarda.
                task.putExtra("list", currentList); //Agrega la lista actual como la lista a la intención.
                task.putExtra("task", currentTask); //Agrega la tarea actual como la tarea a la intención.
                startActivity(task); //Inicia la actividad de tarea.
                finish(); //Finaliza la actividad principal.
            }
        }); //Establece el escuchador de clics en los elementos de la vista de las tareas.

        taskSearchView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String text = parent.getItemAtPosition(position).toString(); //Obtiene la cadena del elemento de la posición y la guarda.

                Task searchTask = null; //Guarda la tarea de la búsqueda como un valor nulo.

                for (Map.Entry<Integer, Task> entry: tasks.entrySet()) { //Por cada entrada de las tareas
                    Task value = entry.getValue(); //Obtiene el valor de la entrada y lo guarda como una tarea.
                    String title = value.getTitle(); //Obtiene el título del valor y lo guarda.

                    if (title.equals(text)) //Si el título es igual al texto.
                        searchTask = value; //Guarda la tarea de la búsqueda como el valor.
                }

                if (searchTask != null) { //Si la tarea de la búsqueda no es un valor nulo.
                    int idList = searchTask.getIdList(); //Obtiene el identificador de lista de la tarea de la búsqueda y lo guarda.
                    List searchList = lists.get(idList); //Obtiene la lista del identificador y la guarda como la de la búsqueda.

                    Intent task = new Intent(MainActivity.this, TaskActivity.class); //Crea una intención de pasar de la actividad principal a la de tarea y la guarda.
                    task.putExtra("list", searchList); //Agrega la lista de la búsqueda como la lista a la intención.
                    task.putExtra("task", searchTask); //Agrega la tarea de la búsqueda como la tarea a la intención.
                    startActivity(task); //Inicia la actividad de tarea.
                    finish(); //Finaliza la actividad principal.
                }
            }
        }); //Establece el escuchador de clics en los elementos de la vista de las tareas cuando se busca.
    }

    /**
     * Checks the files for the app.
     */
    protected void checkFiles() {
        File listsFile = new File(listsPath); //Crea el archivo XML listas del almacenamiento externo de la aplicación y lo guarda.

        if (!listsFile.exists()) { //Si el archivo listas no existe.
            deleteAppFile(tasksPath); //Elimina el archivo tareas de la aplicación.

            try { //Trata de hacer el contenido del cuerpo.
                boolean listsCreated = listsFile.createNewFile(); //Crea un nuevo archivo vacío listas y guarda si lo consigue.

                if (listsCreated) { //Si consiguió crear el archivo listas.
                    Date creationDate = new Date(); //Guarda la fecha actual como la fecha de creación.
                    String name = getString(R.string.no_name); //Guarda la cadena sin nombre como el nombre.
                    names = new String[]{name}; //Guarda los nombres como una matriz de cadenas del nombre.

                    setToolTitle(name); //Establece el título de la barra de herramientas como el nombre.
                    tvDesc.setText(R.string.no_desc); //Establece el texto de la vista de descripción como sin descripción.

                    Description descList = new Description("", creationDate); //Crea una descripción para la lista y la guarda.
                    currentList = new List(name, descList); //Crea una lista con el nombre y la descripción y la guarda como la actual.

                    lists.put(1, currentList); //Agrega la lista actual a las listas en la posición 1.
                    Utils.writeListsToFile(listsPath, lists); //Escribe las listas en el archivo de la ruta.
                }
            } catch (IOException e) { //Si se produce una excepción de E/S.
                Toast.makeText(getApplicationContext(), IO_EXC + e.getLocalizedMessage(), Toast.LENGTH_SHORT).show(); //Crea un mensaje de duración corta del mensaje localizado de la excepción y lo muestra.
            }
        } else { //Si el archivo listas existe.
            parseFiles(); //Analiza los archivos.

            SharedPreferences preferences = getSharedPreferences("data", Context.MODE_PRIVATE); //Obtiene las preferencias del archivo data en modo privado y las guarda.
            int initialList = preferences.getInt("currentList", 1); //Obtiene el entero de la lista actual y lo guarda como la inicial.
            currentList = lists.get(initialList); //Obtiene la lista inicial y la guarda como la actual.

            if (currentList == null) { //Si la lista actual es un valor nulo.
                Map.Entry<Integer, List> firstEntry = lists.entrySet().iterator().next(); //Obtiene la primera entrada de las listas y la guarda.
                currentList = firstEntry.getValue(); //Guarda el valor de la primera entrada como la lista actual.
            }

            showCurrentList(); //Muestra la lista actual en el diseño.
        }
    }

    /**
     * Parses the files of the app.
     */
    protected void parseFiles() {
        names = Utils.parseListsFromFile(listsPath, lists); //Analiza las listas desde el archivo y las guarda junto a sus nombres.
        if (names.length > 0) { //Si la longitud de los nombres es mayor que 0.
            titles = Utils.parseTasksFromFile(tasksPath, tasks); //Analiza las tareas desde el archivo y las guarda junto a sus títulos.
            if (titles.length > 0) //Si la longitud de los títulos es mayor que 0.
                Utils.addTasksToLists(lists, tasks); //Agrega las tareas a las listas.
        }
    }

    /**
     * Deletes the app file of the path.
     *
     * @param filePath the file path to delete.
     */
    protected void deleteAppFile(String filePath) {
        File file = new File(filePath); //Crea el archivo de la ruta y lo guarda.

        if (file.exists()) { //Si el archivo existe.
            boolean fileDeleted = file.delete(); //Elimina el archivo y guarda si lo consigue.
            String fileLog = "File of the path '" + filePath + "' deleted: " + fileDeleted; //Guarda el registro del archivo.

            Logger logger = Logger.getLogger(MainActivity.class.getName()); //Obtiene el registrador para la actividad principal y lo guarda.
            logger.log(Level.INFO, fileLog); //Registra al nivel de advertencia el registro del archivo.
        }
    }

    /**
     * Sets the title of the toolbar.
     *
     * @param title the title of the toolbar.
     */
    protected void setToolTitle(String title) {
        TextView toolTitle = findViewById(R.id.title_main); //Busca el título del principal y lo guarda.
        toolTitle.setText(title); //Establece el texto del título de la barra de herramientas.
    }

    /**
     * Saves the current List in the preferences.
     *
     * @param currentId the identifier of the current List.
     */
    protected void saveCurrentList(int currentId) {
        SharedPreferences preferences = getSharedPreferences("data", Context.MODE_PRIVATE); //Obtiene las preferencias del archivo data en modo privado y las guarda.
        SharedPreferences.Editor editor = preferences.edit(); //Crea un editor de las preferencias y lo guarda.
        editor.putInt("currentList", currentId); //Agrega el identificador actual como la lista actual en el editor.
        editor.apply(); //Confirma los cambios del editor en las preferencias.
    }

    /**
     * Shows the current List in the layout.
     */
    protected void showCurrentList() {
        saveCurrentList(currentList.getId()); //Guarda la lista actual.
        setToolTitle(currentList.getName()); //Establece el título de la barra de herramientas como el nombre de la lista actual.

        String descList = currentList.getDescription().getContent(); //Obtiene el contenido de la descripción de la lista actual y lo guarda.
        if (descList.equals("")) { //Si la descripción de la lista es igual a una cadena vacía.
            tvDesc.setText(getString(R.string.no_desc)); //Establece el texto de la vista de descripción como sin descripción.
        } else { //Si no es una cadena vacía.
            String descText = getString(R.string.list_task_desc_header); //Obtiene la cadena de la descripción y la guarda como el texto de la descripción.
            descText = descText.concat(" "); //Concatena un espacio en blanco con el texto de la descripción y lo guarda.
            descText = descText.concat(descList); //Concatena la descripción de la lista con su texto y lo guarda.

            tvDesc.setText(descText); //Establece el texto de la vista de descripción como el texto de la descripción.
        }

        ArrayList<Task> taskList = currentList.getTasks(); //Obtiene las tareas de la lista actual y las guarda.
        if (!taskList.isEmpty()) { //Si la lista de tareas no está vacía.
            ArrayList<String> titleList = new ArrayList<>(); //Crea una matriz de lista de cadenas para los títulos y la guarda.

            for (Task task: taskList) { //Por cada tarea de la lista de tareas.
                String title = task.getTitle(); //Obtiene el título de la tarea y lo guarda.
                titleList.add(title); //Agrega el título a la lista.
            }

            String[] taskTitles = titleList.toArray(new String[0]); //Convierte la lista de títulos a una matriz de cadenas y la guarda como los títulos de las tareas.
            ArrayAdapter<String> adapter = new ArrayAdapter<>(MainActivity.this, R.layout.task_list_item, taskTitles); //Crea un adaptador de matriz de los títulos de las tareas y lo guarda.
            taskView.setAdapter(adapter); //Establece el adaptador de la vista de lista de las tareas.
        } else { //Si está vacía.
            ListAdapter adapter = taskView.getAdapter(); //Obtiene el adaptador de la vista de lista de las tareas y lo guarda.
            if (adapter != null) //Si el adaptador no es un valor nulo.
                taskView.setAdapter(null); //Establece el adaptador de la vista de lista de las tareas como un valor nulo.
        }
    }

    /**
     * Shows the configuration dialog to import or export data.
     */
    protected void showSettings() {
        View settingsHeader = View.inflate(MainActivity.this, R.layout.custom_dialog_header, null); //Infla la vista de la cabecera de la configuración y la guarda.

        ImageView settingsIcon = settingsHeader.findViewById(R.id.dialog_icon); //Busca el icono de la cabecera de la configuración y lo guarda.
        settingsIcon.setImageResource(R.drawable.ic_settings_blue_26dp); //Establece el recurso de la imagen del icono de la configuración.

        TextView settingsTitle = settingsHeader.findViewById(R.id.dialog_title); //Busca el título de la cabecera de la configuración y lo guarda.
        settingsTitle.setText(R.string.action_settings); //Establece el texto del título de la configuración.

        AlertDialog.Builder settingsBuilder = new AlertDialog.Builder(MainActivity.this, 3); //Crea una nuevo constructor de diálogo de alerta y lo guarda.
        settingsBuilder.setCustomTitle(settingsHeader); //Establece el título personalizado del constructor de la configuración como la cabecera.

        View settingsContent = View.inflate(MainActivity.this, R.layout.settings_dialog_content, null); //Infla la vista del contenido de la configuración y la guarda.

        final AlertDialog settingsDialog = settingsBuilder.create(); //Crea un diálogo de alerta con el constructor de la configuración y lo guarda.
        settingsDialog.setView(settingsContent); //Establece la vista del díalogo de la configuración como el contenido.

        settingsContent.findViewById(R.id.settings_export).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                settingsDialog.dismiss(); //Cierra el diálogo de la configuración.
                String title = getString(R.string.select_folder); //Obtiene la cadena de seleccionar carpeta y la guarda.

                DialogProperties properties = new DialogProperties(true); //Crea unas nuevas propiedades de diálogo y las guarda.
                properties.setSelectionType(DialogConfigs.DIR_SELECT); //Establece el tipo de selección de las propiedades como el directorio.

                FilePickerDialog dialog = new FilePickerDialog(MainActivity.this, properties); //Crea un nuevo diálogo de selector de archivos y lo guarda.
                dialog.setTitle(title); //Establece el título del diálogo.
                dialog.setDialogSelectionListener(new DialogSelectionListener() {
                    @Override
                    public void onSelectedFilePaths(String[] files) {
                        String file = files[0]; //Accede al primer archivo de la matriz y la guarda.
                        exportData(file); //Exporta los datos de la aplicación al almacenamiento externo.
                    }
                }); //Establece el escuchador de selección del diálogo.
                dialog.show(); //Muestra el diálogo.
            }
        }); //Establece el escuchador de clics del botón de exportar datos del contenido de la configuración.
        settingsContent.findViewById(R.id.settings_import).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                settingsDialog.dismiss(); //Cierra el diálogo de la configuración.
                String title = getString(R.string.select_file); //Obtiene la cadena de seleccionar archivo y la guarda.
                String[] extensions = new String[]{".ldd"}; //Guarda las extensiones como una matriz de cadenas con la extensión ldd.

                DialogProperties properties = new DialogProperties(true); //Crea unas nuevas propiedades de diálogo y las guarda.
                properties.setExtensions(extensions); //Guarda las extensiones de las propiedades.

                FilePickerDialog dialog = new FilePickerDialog(MainActivity.this, properties); //Crea un nuevo diálogo de selector de archivos y lo guarda.
                dialog.setTitle(title); //Establece el título del diálogo.
                dialog.setDialogSelectionListener(new DialogSelectionListener() {
                    @Override
                    public void onSelectedFilePaths(String[] files) {
                        String file = files[0]; //Accede al primer archivo de la matriz y la guarda.
                        importData(file); //Importa los datos del almacenamiento externo a la aplicación.
                    }
                }); //Establece el escuchador de selección del diálogo.
                dialog.show(); //Muestra el diálogo.
            }
        }); //Establece el escuchador de clics del botón de importar datos del contenido de la configuración.
        settingsContent.findViewById(R.id.settings_exit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                settingsDialog.dismiss(); //Cierra el diálogo de la configuración.
            }
        }); //Establece el escuchador de clics del botón de salir del contenido de la configuración.

        settingsDialog.show(); //Muestra el diálogo de la configuración.
    }

    /**
     * Reads the text contents of the file and returns them.
     *
     * @param file the file to read.
     * @return the contents and the result of the reading of the file.
     */
    @SuppressLint("NewApi")
    protected Pair<String, Boolean> readFile(File file) {
        boolean fileRead = file.canRead(); //Guarda el indicador de si se ha leído el archivo.
        StringBuilder stringBuilder = new StringBuilder(); //Crea un nuevo constructor de cadenas y lo guarda.

        if (fileRead) //Si se puede leer el archivo.
            try (BufferedReader bufferedReader = new BufferedReader(new FileReader(file))) { //Trata de crear un lector del búfer y leer de él.
                String line; //Inicializa la línea.

                while ((line = bufferedReader.readLine()) != null) { //Mientras la línea leída no sea un valor nulo.
                    stringBuilder.append(line); //Agrega la línea al constructor de cadenas.
                    stringBuilder.append('\n'); //Agrega la nueva línea al constructor de cadenas.
                }
            } catch (IOException e) { //Si se produce una excepción de E/S.
                Toast.makeText(getApplicationContext(), IO_EXC + e.getLocalizedMessage(), Toast.LENGTH_SHORT).show(); //Crea un mensaje de duración corta del mensaje localizado de la excepción y lo muestra.
                fileRead = false; //Establece que no se ha leído el archivo.
            }

        String contents = stringBuilder.toString(); //Convierte el constructor de cadenas a una cadena y la guarda como los contenidos.
        return new Pair<>(contents, fileRead); //Devuelve el par de los contenidos y el resultado de la lectura.
    }

    /**
     * Writes the text in the file and returns if successful.
     *
     * @param text the text to write to the file.
     * @param file the file in which to write the text.
     * @return the indicator whether the file has been read.
     */
    @SuppressLint("NewApi")
    protected boolean writeFile(String text, File file) {
        boolean fileWritten = file.canWrite(); //Guarda el indicador de si se ha escrito en el archivo.

        if (fileWritten) //Si se puede escribir en el archivo.
            try (FileWriter fileWriter = new FileWriter(file)) { //Trata de crear un escritor del archivo y escribir en él.
                fileWriter.write(text); //Escribe el texto en el escritor del archivo.
            } catch (IOException e) { //Si se produce una excepción de E/S.
                Toast.makeText(getApplicationContext(), IO_EXC + e.getLocalizedMessage(), Toast.LENGTH_SHORT).show(); //Crea un mensaje de duración corta del mensaje localizado de la excepción y lo muestra.
                fileWritten = false; //Establece que no se ha escrito en el archivo.
            }

        return fileWritten; //Devuelve si se ha escrito en el archivo.
    }

    /**
     * Reads the Tasks for export.
     *
     * @param listsText the Lists text where to add the Tasks.
     * @return the text for the data with the tasks.
     */
    protected String readTasksExport(String listsText) {
        String dataText = listsText; //Guarda el texto de los datos como el de listas.

        File tasksFile = new File(tasksPath); //Crea el archivo XML tareas del almacenamiento externo de la aplicación y lo guarda.
        Pair<String, Boolean> tasksPair = readFile(tasksFile); //Lee el archivo tareas y guarda su par de los contenidos y del resultado.
        boolean tasksRead = tasksPair.second; //Obtiene el segundo del par de tareas y lo guarda como el indicador de la lectura de tareas.

        if (tasksRead) { //Si se ha leído el archivo tareas.
            String tasksText = tasksPair.first; //Obtiene el primero del par de tareas y lo guarda como el texto de la lectura de tareas.
            String tasksEncrypted = AES256GCM.encryptToStrBase64(FIRST_KEY, FIRST_IV, tasksText); //Encripta el texto de tareas con la clave y el vector de inicialización y lo guarda.

            if (!tasksEncrypted.isEmpty()) //Si el encriptado de tareas no está vacío.
                dataText += SEP + tasksEncrypted; //Agrega el encriptado de tareas junto con el separador al texto de los datos.
        }

        return dataText; //Devuelve el texto de los datos.
    }

    /**
     * Creates a data file and returns if successful.
     *
     * @param file the data file to create.
     * @return the indicator whether the data file has been created.
     */
    protected boolean createDataFile(File file) {
        boolean fileExisted = file.exists(); //Guarda si el archivo existe.

        if (fileExisted) { //Si el archivo existe.
            writeFile("", file); //Escribe una cadena vacía en el archivo.
        } else { //Si el archivo no existe.
            try { //Trata de hacer el contenido del cuerpo.
                fileExisted = file.createNewFile(); //Crea un nuevo archivo y guarda si lo consigue.
            } catch (IOException e) { //Si se produce una excepción de E/S.
                Toast.makeText(getApplicationContext(), IO_EXC + e.getLocalizedMessage(), Toast.LENGTH_SHORT).show(); //Crea un mensaje de duración corta del mensaje localizado de la excepción y lo muestra.
            }
        }

        return fileExisted; //Devuelve si el archivo existe.
    }

    /**
     * Exports data from the application to external storage.
     *
     * @param folderPath the path of the folder to which to export the data.
     */
    protected void exportData(String folderPath) {
        int exportResult = R.string.data_unsuccess_exported; //Guarda el resultado de la exportación como sin éxito.

        File listsFile = new File(listsPath); //Crea el archivo XML listas del almacenamiento externo de la aplicación y lo guarda.
        Pair<String, Boolean> listsPair = readFile(listsFile); //Lee el archivo listas y guarda su par de los contenidos y del resultado.
        boolean listsRead = listsPair.second; //Obtiene el segundo del par de listas y lo guarda como el indicador de la lectura de listas.

        if (listsRead) { //Si se ha leído el archivo listas.
            String listsText = listsPair.first; //Obtiene el primero del par de listas y lo guarda como el texto de la lectura de listas.

            if (!listsText.isEmpty()) { //Si el texto de listas no está vacío.
                String listsEncrypted = AES256GCM.encryptToStrBase64(FIRST_KEY, FIRST_IV, listsText); //Encripta el texto de listas con la clave y el vector de inicialización y lo guarda.
                String dataText = readTasksExport(listsEncrypted); //Lee las tareas para la exportación y guarda el texto de los datos.
                String dataEncrypted = AES256GCM.encryptToStrBase64(SECOND_KEY, SECOND_IV, dataText); //Encripta el texto de los datos con la clave y el vector de inicialización y lo guarda.

                SimpleDateFormat formatter = new SimpleDateFormat("yyMMddHHmmss", Locale.US); //Crea un formato de fechas simples y lo guarda.
                String fileDate = formatter.format(new Date()); //Formatea la fecha actual a una cadena y la guarda.
                String fileName = "data" + fileDate + ".ldd"; //Guarda el nombre del archivo.

                String dataPath = folderPath + File.separator + fileName; //Guarda la ruta del archivo de los datos.
                File dataFile = new File(dataPath); //Crea un nuevo archivo de los datos de la ruta y lo guarda.
                boolean dataCreated = createDataFile(dataFile); //Crea el archivo de los datos y guarda si lo consigue.

                if (dataCreated) { //Si se ha creado el archivo de lso datos.
                    boolean dataWritten = writeFile(dataEncrypted, dataFile); //Escribe el escriptado de los datos en su archivo y guarda si lo consigue.
                    if (dataWritten) { //Si se ha escrito en el archivo de los datos.
                        exportResult = R.string.data_success_exported; //Guarda el resultado de la exportación como con éxito.
                    } else { //Si no se ha escrito.
                        deleteAppFile(dataPath); //Elimina el archivo de los datos de la aplicación.
                    }
                }
            }
        }

        Toast.makeText(getApplicationContext(), exportResult, Toast.LENGTH_SHORT).show(); //Crea un mensaje de duración corta del resultado de la exportación y lo muestra.
    }

    /**
     * Write the imported texts of the Tasks and returns if successful.
     *
     * @param tasksText the Tasks text to write.
     * @return the indicator whether the imported Tasks has been written.
     */
    protected boolean writeTasksImported(String tasksText) {
        boolean tasksImported = false; //Guarda el indicador de las tareas importadas como falso.

        if (!tasksText.isEmpty()) { //Si el texto de tareas no está vacío.
            File tasksFile = new File(tasksPath); //Crea el archivo XML tareas del almacenamiento externo de la aplicación y lo guarda.
            boolean tasksExisted = tasksFile.exists(); //Guarda si el archivo taraes existe.

            try { //Trata de hacer el contenido del cuerpo.
                if (!tasksExisted) //Si el archivo tareas no existe.
                    tasksExisted = tasksFile.createNewFile(); //Crea un nuevo archivo vacío tareas y guarda si lo consigue.

                if (tasksExisted) { //Si el archivo tareas existe.
                    String tasksDecrypted = AES256GCM.decryptFromStrBase64(FIRST_KEY, FIRST_IV, tasksText); //Desencripta el texto de tareas con la clave y el vector de inicialización y lo guarda.

                    if (!tasksDecrypted.isEmpty()) //Si el desencriptado de tareas no está vacío.
                        tasksImported = writeFile(tasksDecrypted, tasksFile); //Escribe el desencriptado de tareas en su archivo y guarda si lo consigue.
                }
            } catch (IOException e) { //Si se produce una excepción de E/S.
                Toast.makeText(getApplicationContext(), IO_EXC + e.getLocalizedMessage(), Toast.LENGTH_SHORT).show(); //Crea un mensaje de duración corta del mensaje localizado de la excepción y lo muestra.
                tasksImported = false; //Establece que las tareas no han sido importadas.
            }
        } else { //Si está vacío.
            deleteAppFile(tasksPath); //Elimina el archivo tareas de la aplicación.
        }

        return tasksImported; //Devuelve si las tareas no han sido importadas.
    }

    /**
     * Write the imported texts of the Lists and the Tasks and returns if successful.
     *
     * @param listsText the Lists text to write.
     * @param tasksText the Tasks text to write.
     * @return the indicator whether the imported data has been written.
     */
    protected boolean writeDataImported(String listsText, String tasksText) {
        boolean dataImported = false; //Guarda el indicador de los datos importados como falso.

        String listsDecrypted = AES256GCM.decryptFromStrBase64(FIRST_KEY, FIRST_IV, listsText); //Desencripta el texto de listas con la clave y el vector de inicialización y lo guarda.

        if (!listsDecrypted.isEmpty()) { //Si el desencriptado de listas no está vacío.
            File listsFile = new File(listsPath); //Crea el archivo XML listas del almacenamiento externo de la aplicación y lo guarda.
            boolean listsExisted = listsFile.exists(); //Guarda si el archivo listas existe.

            try { //Trata de hacer el contenido del cuerpo.
                if (!listsExisted) //Si el archivo listas no existe.
                    listsExisted = listsFile.createNewFile(); //Crea un nuevo archivo vacío listas y guarda si lo consigue.

                if (listsExisted) { //Si el archivo listas existe.
                    dataImported = writeFile(listsDecrypted, listsFile); //Escribe el desencriptado de listas en su archivo y guarda si lo consigue.

                    if (dataImported) //Si los datos han sido importados.
                        dataImported = writeTasksImported(tasksText); //Escribe las tareas importadas y guarda si lo consigue.
                }
            } catch (IOException e) { //Si se produce una excepción de E/S.
                Toast.makeText(getApplicationContext(), IO_EXC + e.getLocalizedMessage(), Toast.LENGTH_SHORT).show(); //Crea un mensaje de duración corta del mensaje localizado de la excepción y lo muestra.
                dataImported = false; //Establece que los datos no han sido importados.
            }
        }

        return dataImported; //Devuelve si los datos han sido importados.
    }

    /**
     * Imports data from external storage to the application.
     *
     * @param filePath the file path from which to import the data.
     */
    protected void importData(String filePath) {
        boolean importResult = false; //Guarda el indicador del resultado de la importación como falso.

        File dataFile = new File(filePath); //Crea un nuevo archivo de los datos y lo guarda.
        Pair<String, Boolean> dataPair = readFile(dataFile); //Lee el archivo de los datos y guarda su par de los contenidos y del resultado.
        boolean dataRead = dataPair.second; //Obtiene el segundo del par de los datos y lo guarda como el indicador de la lectura de los datos.

        if (dataRead) { //Si se ha leído el archivo de los datos.
            String dataText = dataPair.first; //Obtiene el primero del par de los datos y lo guarda como el texto de la lectura de los datos.
            String dataDecrypted = AES256GCM.decryptFromStrBase64(SECOND_KEY, SECOND_IV, dataText); //Desencripta el texto de los datos con la clave y el vector de inicialización y lo guarda.

            if (!dataDecrypted.isEmpty()) { //Si el desencriptado de los datos no está vacío.
                String listsText; //Inicializa el texto de listas.
                String tasksText; //Inicializa el texto de tareas.

                if (dataDecrypted.contains(SEP)) { //Si el desencriptado de los datos contiene el separador.
                    String[] texts = dataDecrypted.split(SEP); //Separa el desencriptado de los datos por el separador y guarda los textos separados resultantes.

                    listsText = texts[0]; //Guarda el texto de listas como el primer texto.
                    tasksText = texts[1]; //Guarda el texto de tareas como el segundo texto.
                } else { //Si no contiene el separador.
                    listsText = dataDecrypted; //Guarda el texto de listas como el desencriptado de los datos.
                    tasksText = ""; //Guarda el texto de tareas como una cadena vacía.
                }

                importResult = writeDataImported(listsText, tasksText); //Escribe los datos importados y guarda si lo consigue.
            }
        }

        if (importResult) { //Si la importación da resultado.
            parseFiles(); //Analiza los archivos.

            ArrayAdapter<String> adapter = new ArrayAdapter<>(MainActivity.this, R.layout.nav_list_item, names); //Crea un adaptador de matriz de los nombres y lo guarda.
            navView.setAdapter(adapter); //Establece el adaptador de la vista de lista de la nacegación.

            Map.Entry<Integer, List> firstEntry = lists.entrySet().iterator().next(); //Obtiene la primera entrada de las listas y la guarda.
            currentList = firstEntry.getValue(); //Guarda el valor de la primera entrada como la lista actual.
            showCurrentList(); //Muestra la lista actual en el diseño.

            Toast.makeText(getApplicationContext(), R.string.data_success_imported, Toast.LENGTH_SHORT).show(); //Crea un mensaje de duración corta de los datos importados con éxito y lo muestra.
        } else { //Si no da resultado.
            Toast.makeText(getApplicationContext(), R.string.data_unsuccess_imported, Toast.LENGTH_SHORT).show(); //Crea un mensaje de duración corta de los datos importados con éxito y lo muestra.
        }
    }

    /**
     * Opens the New List Activity to create a new List and add it to the app.
     */
    protected void addNewList() {
        Intent list = new Intent(MainActivity.this, NewListActivity.class); //Crea una intención de pasar de la actividad principal a a la de nueva lista y la guarda.
        startActivity(list); //Inicia la actividad de nueva lista.
        finish(); //Finaliza la actividad principal.
    }

    /**
     * Opens the New Task Activity to create a new Task and add it to the current List.
     */
    protected void addNewTask() {
        Intent task = new Intent(MainActivity.this, NewTaskActivity.class); //Crea una intención de pasar de la actividad principal a a la de nueva tarea y la guarda.
        task.putExtra("list", currentList); //Agrega la lista actual como la lista a la intención.
        startActivity(task); //Inicia la actividad de nueva tarea.
        finish(); //Finaliza la actividad principal.
    }

    /**
     * Opens the Edit List Activity to modify the current List.
     */
    protected void editList() {
        Intent list = new Intent(MainActivity.this, EditListActivity.class); //Crea una intención de pasar de la actividad principal a a la de editar lista y la guarda.
        list.putExtra("list", currentList); //Agrega la lista actual como la lista a la intención.
        startActivity(list); //Inicia la actividad de editar lista.
        finish(); //Finaliza la actividad principal.
    }

    /**
     * Saves the Lists after the deletion.
     */
    protected void saveDeletedLists() {
        if (lists.isEmpty()) { //Si las listas están vacías.
            Description descList = new Description(); //Crea una descripción para la lista y la guarda.
            List list = new List(getString(R.string.no_name), descList); //Crea una lista sin nombre y la guarda.
            lists.put(1, list); //Agrega la lista a las listas en la posición 1.
        }

        File listsFile = new File(listsPath); //Crea el archivo XML listas del almacenamiento externo de la aplicación y lo guarda.
        boolean listsExisted = listsFile.exists(); //Guarda si el archivo listas existe.

        try { //Trata de hacer el contenido del cuerpo.
            if (!listsExisted) //Si el archivo listas no existe.
                listsExisted = listsFile.createNewFile(); //Crea un nuevo archivo listas y guarda si lo consigue.
        } catch (IOException e) { //Si se produce una excepción de E/S.
            Toast.makeText(getApplicationContext(), IO_EXC + e.getLocalizedMessage(), Toast.LENGTH_SHORT).show(); //Crea un mensaje de duración corta del mensaje localizado de la excepción y lo muestra.
        }

        if (listsExisted) //Si el archivo listas existe.
            names = Utils.writeListsToFile(listsPath, lists); //Escribe las listas en el archivo de la ruta y guarda sus nombres.
    }

    /**
     * Saves the Tasks after the deletion.
     */
    protected void saveDeletedTasks() {
        File tasksFile = new File(tasksPath); //Crea el archivo XML tareas del almacenamiento externo de la aplicación y lo guarda.
        boolean tasksExisted = tasksFile.exists(); //Guarda si el archivo tareas existe.

        if (!tasks.isEmpty()) { //Si las tareas no están vacías.
            try { //Trata de hacer el contenido del cuerpo.
                if (!tasksExisted) //Si el archivo tareas no existe.
                    tasksExisted = tasksFile.createNewFile(); //Crea un nuevo archivo tareas y guarda si lo consigue.
            } catch (IOException e) { //Si se produce una excepción de E/S.
                Toast.makeText(getApplicationContext(), IO_EXC + e.getLocalizedMessage(), Toast.LENGTH_SHORT).show(); //Crea un mensaje de duración corta del mensaje localizado de la excepción y lo muestra.
            }

            if (tasksExisted) //Si el archivo tareas existe.
                titles = Utils.writeTasksToFile(tasksPath, tasks); //Escribe las tareas en el archivo de la ruta y guarda sus títulos.
        } else { //Si están vacías.
            if (tasksExisted) { //Si el archivo tareas existe.
                boolean tasksDeleted = tasksFile.delete(); //Elimina el archivo tareas y guarda si lo consigue.
                if (tasksDeleted) //Si consiguió eliminar el archivo tareas.
                    titles = new String[0]; //Guarda los títulos como una matriz de cadenas vacía.
            }
        }
    }

    /**
     * Opens the delete List Dialog to choose whether to delete the current List or not.
     */
    protected void deleteList() {
        View deleteHeader = View.inflate(MainActivity.this, R.layout.custom_dialog_header, null); //Infla la vista de la cabecera de eliminado y la guarda.

        ImageView deleteIcon = deleteHeader.findViewById(R.id.dialog_icon); //Busca el icono de la cabecera de eliminado y lo guarda.
        deleteIcon.setImageResource(R.drawable.ic_minus_blue_32dp); //Establece el recurso de la imagen del icono de eliminado.

        TextView deleteTitle = deleteHeader.findViewById(R.id.dialog_title); //Busca el título de la cabecera de eliminado y lo guarda.
        deleteTitle.setText(R.string.delete_list_header); //Establece el texto del título de eliminado.

        AlertDialog.Builder deleteBuilder = new AlertDialog.Builder(MainActivity.this, 3); //Crea una nuevo constructor de diálogo de alerta y lo guarda.
        deleteBuilder.setCustomTitle(deleteHeader); //Establece el título personalizado del constructor de eliminado como la cabecera.

        View deleteContent = View.inflate(MainActivity.this, R.layout.custom_dialog_content, null); //Infla la vista del contenido de eliminado y la guarda.

        final AlertDialog deleteDialog = deleteBuilder.create(); //Crea un diálogo de alerta con el constructor de eliminado y lo guarda.
        deleteDialog.setView(deleteContent); //Establece la vista del díalogo de eliminado como el contenido.

        TextView deleteText = deleteContent.findViewById(R.id.custom_info); //Busca la información del contenido de eliminado y lo guarda.
        deleteText.setText(R.string.are_sure_delete_list); //Establece el texto de eliminado.

        deleteContent.findViewById(R.id.custom_accept).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteDialog.dismiss(); //Cierra el diálogo de eliminado.

                List listRemoved = lists.remove(currentList.getId());//Elimina la lista actual por su identificador de las listas.
                boolean listDeleted = currentList.equals(listRemoved); //Comprueba si la lista actual y la eliminada son iguales y guarda su resultado.

                if (listDeleted) { //Si la lista fue eliminada.
                    saveDeletedLists(); //Guarda las listas eliminadas.

                    ArrayList<Task> currentTasks = currentList.getTasks(); //Obtiene las tareas de la lista actual y las guarda.
                    for (Task task: currentTasks) //Por cada tarea de las tareas actuales.
                        tasks.remove(task.getId()); //Elimina la tarea por su identificador de las tareas.

                    saveDeletedTasks(); //Guarda las tareas eliminadas.

                    Toast.makeText(getApplicationContext(), getString(R.string.deleted_list), Toast.LENGTH_SHORT).show(); //Crea un mensaje de duración corta de la lista eliminada y lo muestra.

                    ArrayAdapter<String> adapter = new ArrayAdapter<>(MainActivity.this, R.layout.nav_list_item, names); //Crea un adaptador de matriz de los nombres y lo guarda.
                    navView.setAdapter(adapter); //Establece el adaptador de la vista de lista de la nacegación.

                    Integer[] ids = lists.keySet().toArray(new Integer[0]); //Obtiene la matriz de identificadores de las claves de las listas y las guarda.
                    int newId = ids[0]; //Obtiene el primer identificador y lo guarda.

                    currentList = lists.get(newId); //Obtiene la lista del nuevo identificador y la guarda como la actual.
                    if (currentList != null) //Si la lista actual no es un valor nulo.
                        showCurrentList(); //Muestra la lista actual en el diseño.
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
        if (drawer.isDrawerOpen(GravityCompat.START)) { //Si el cajón está abierto.
            drawer.closeDrawer(GravityCompat.START); //Cierra el cajón.
        } else { //Si no está abierto.
            if (doubleBackToExit) { //Si se ha pulsado el botón atras para salir.
                super.onBackPressed(); //Vuelve a la actividad anterior o cierra la aplicación si no hay.
            } else { //Si no se ha pulsado.
                doubleBackToExit = true; //Establece que el botón atrás ha sido pulsado para salir.
                Toast.makeText(getApplicationContext(), R.string.press_again_exit, Toast.LENGTH_SHORT).show(); //Crea un mensaje de duración corta de pulsar de nuevo para salir y lo muestra.

                new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        doubleBackToExit = false; //Establece que el botón atrás no ha sido pulsado para salir.
                    }
                }, 2000); //Agrega el ejecutable al nuevo manejador para ejecutarlo pasados 2 segundos.
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu); //Infla el menú principal en el diseño de esta actividad.
        final MenuItem editItem = menu.findItem(R.id.action_edit_list); //Busca el elemento de editar lista en el menú y lo guarda.
        final MenuItem deleteItem = menu.findItem(R.id.action_delete_list); //Busca el elemento de eliminar lista en el menú y lo guarda.
        final MenuItem aboutItem = menu.findItem(R.id.action_about_main); //Busca el elemento de acerca de en el principal en el menú y lo guarda.
        MenuItem searchItem = menu.findItem(R.id.action_search_tasks); //Busca el elemento de buscar tareas en el menú y lo guarda.
        SearchView searchView = (SearchView) searchItem.getActionView(); //Obtiene la vista de acción del elemento de búsqueda y la guarda como una de búsqueda.

        searchItem.setOnActionExpandListener(new MenuItem.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                editItem.setVisible(false); //Establece que el elemento de editar no sea visible.
                deleteItem.setVisible(false); //Establece que el elemento de eliminar no sea visible.
                aboutItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS); //Establece que el elemento de acerca de se muestre siempre.

                ArrayAdapter<String> adapter = new ArrayAdapter<>(MainActivity.this, R.layout.task_list_item, titles); //Crea un adaptador de matriz de los títulos de las tareas y lo guarda.
                taskSearchView.setAdapter(adapter); //Establece el adaptador de la vista de lista de las tareas cuando se busca.

                tvDesc.setVisibility(View.GONE); //Establece la visibilidad de la vista de texto de la descripción como ausente.
                taskView.setVisibility(View.GONE); //Establece la visibilidad de la vista de lista de las tareas como ausente.
                taskSearchView.setVisibility(View.VISIBLE); //Establece la visibilidad de la vista de lista de las tareas cuando se busca como visible.

                fabTask.hide(); //Oculta el botón de acción flotante de agregar nueva tarea.
                return true; //Devuelve verdadero.
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                editItem.setVisible(true); //Establece que el elemento de editar sea visible.
                deleteItem.setVisible(true); //Establece que el elemento de eliminar sea visible.
                aboutItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER); //Establece que el elemento de acerca de no se muestre nunca.

                taskSearchView.setVisibility(View.GONE); //Establece la visibilidad de la vista de lista de las tareas cuando se busca como ausente.
                tvDesc.setVisibility(View.VISIBLE); //Establece la visibilidad de la vista de texto de la descripción como visible.
                taskView.setVisibility(View.VISIBLE); //Establece la visibilidad de la vista de lista de las tareas como visible.

                fabTask.show(); //Muestra el botón de acción flotante de agregar nueva tarea.
                return true; //Devuelve verdadero.
            }
        });

        searchView.setQueryHint(getString(R.string.hint_search)); //Establece la sugerencia de consulta de la vista de búsqueda.
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return true; //Devuelve verdadero.
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                String strippedText = Utils.stripAccents(newText); //Despoja al texto de los acentos y guarda el texto despojado.
                ArrayList<String> titlesList = new ArrayList<>(); //Crea una matriz de lista de cadenas para los títulos y la guarda.

                for (String title: titles) { //Por cada título de la matriz.
                    String strippedTitle = Utils.stripAccents(title); //Despoja al título de los acentos y guarda el título despojado.

                    if (strippedTitle.contains(strippedText)) //Si el titulo despojado contiene el texto despojado.
                        titlesList.add(title); //Agrega el título a la lista de los títulos.
                }

                String[] newTitles = titlesList.toArray(new String[0]); //Convierte la lista de los títulos a una matriz de cadenas y la guarda como los nuevos títulos.
                ArrayAdapter<String> adapter = new ArrayAdapter<>(MainActivity.this, R.layout.task_list_item, newTitles); //Crea un adaptador de matriz de los nuevos títulos y lo guarda.
                taskSearchView.setAdapter(adapter); //Establece el adaptador de la vista de lista de las tareas cuando se busca.

                return true; //Devuelve verdadero
            }
        }); //Establece el escuchador del texto de consulta de la vista de búsqueda.

        return super.onCreateOptionsMenu(menu); //Devuelve el resultado de crear un menú de opciones en el diseño de esta actividad.
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId(); //Obtiene el identificador del elemento del menú y lo guarda.

        if (id == R.id.action_search_tasks) { //Si el identificador es igual al de buscar tareas.
            return true; //Devuelve verdadero.
        } else if (id == R.id.action_edit_list) { //Si el identificador es igual al de editar lista.
            editList(); //Abre la actividad de editar lista para modificar la lista actual.
            return true; //Devuelve verdadero.
        } else if (id == R.id.action_delete_list) { //Si el identificador es igual al de eliminar lista.
            deleteList(); //Abre el diálogo de eliminar lista para elegir si eliminar la lista actual o no.
            return true; //Devuelve verdadero.
        } else if (id == R.id.action_about_main) { //Si el identificador es igual al de acerca de en el principal.
            Utils.showAbout(MainActivity.this); //Muestra la actividad de acerca de.
            return true; //Devuelve verdadero.
        } else { //Si no es ninguno de los anteriores.
            return super.onOptionsItemSelected(item); //Devuelve el resultado de que un elemento de opciones sea seleccionado.
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 100) { //Si el código de solicitud es 100.
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) { //Si el primer resultado de la concesión es permiso concedido.
                Toast.makeText(getApplicationContext(), R.string.perm_granted, Toast.LENGTH_SHORT).show(); //Crea un mensaje de duración corta de permiso concedido y lo muestra.
            } else { //Si no es permiso concedido.
                Toast.makeText(getApplicationContext(), R.string.perm_denied, Toast.LENGTH_SHORT).show(); //Crea un mensaje de duración corta de permiso denegado y lo muestra.
                finish(); //Finaliza la pantalla de bienvenida.
            }
        } else { //Si no es 100.
            super.onRequestPermissionsResult(requestCode, permissions, grantResults); //Recibe los resultados de la solicitud de permisos.
        }
    }

}

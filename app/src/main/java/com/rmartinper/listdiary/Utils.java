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

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.TypedValue;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import com.github.florent37.viewtooltip.ViewTooltip;
import com.takisoft.datetimepicker.DatePickerDialog;
import com.takisoft.datetimepicker.widget.DatePicker;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.IOException;
import java.text.Normalizer;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

/**
 * A class that contains utility methods.
 *
 * @author Raul Martin Perez (raumar95@gmail.com)
 */
class Utils {

    /**
     * The Logger for the Utils.
     */
    private static final Logger LOGGER = Logger.getLogger(Utils.class.getName()); //Obtiene el registrador para las utilidades y lo guarda.

    /**
     * The long date format for the formatters of the Utils.
     */
    private static final String LONG_DATE = "dd/MM/yyyy HH:mm:ss"; //Guarda el formato para fechas largo.

    /**
     * The short date format for the formatters of the Utils.
     */
    private static final String SHORT_DATE = "dd/MM/yyyy"; //Guarda el formato para fechas corto.

    /**
     * The header of the parse exception of the Utils.
     */
    private static final String PARSE_EXC = "ParseException: %s"; //Guarda el encabezado de la excepción de análisis.

    /**
     * The header of the I/O exception of the Utils.
     */
    private static final String IO_EXC = "IOException: %s"; //Guarda el encabezado de la excepción de E/S.

    /**
     * The identifier tag for the XML files.
     */
    private static final String ID = "id"; //Guarda la etiqueta del identificador.

    /**
     * The name tag for the XML files.
     */
    private static final String NAME = "name"; //Guarda la etiqueta del nombre.

    /**
     * The title tag for the XML files.
     */
    private static final String TITLE = "title"; //Guarda la etiqueta del título.

    /**
     * The description tag for the XML files.
     */
    private static final String DESCRIPTION = "description"; //Guarda la etiqueta de la descripción.

    /**
     * The field tag for the XML files.
     */
    private static final String FIELD = "field"; //Guarda la etiqueta del campo.

    /**
     * The value tag for the XML files.
     */
    private static final String VALUE = "value"; //Guarda la etiqueta del valor.

    /**
     * The creation date tag for the XML files.
     */
    private static final String CREATION_DATE = "creationDate"; //Guarda la etiqueta de la fecha de creación.

    /**
     * The String of the slash for the Date Picker Dialog.
     */
    private static final String SLASH = "/"; //Guarda la cadena de la barra oblicua.

    /**
     * The String of the zero for the Date Picker Dialog.
     */
    private static final String ZERO = "0"; //Guarda la cadena del cero.

    /**
     * The value of the color of the background of the View of Tooltip.
     */
    private static final int TOOLTIP_BKGD_COLOR = Color.parseColor("#CC000000"); //Guarda el color del fondo de la información de herramienta.

    /**
     * The value of the color of the text of the View of Tooltip.
     */
    private static final int TOOLTIP_TEXT_COLOR = Color.parseColor("#FFFFFFFF"); //Guarda el color del texto de la información de herramienta.

    /**
     * Constructs a new Utils.
     */
    private Utils() {
        super(); //Llama al constructor superior.
    }

    /**
     * Creates a document builder.
     *
     * @return the document builder.
     */
    private static DocumentBuilder createDocBuilder() {
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance(); //Crea una nueva instancia de una fábrica de constructores de documentos y la guarda.
        try { //Trata de hacer el contenido del cuerpo.
            dbFactory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true); //Establece que la fábrica procesa de forma segura.
        } catch (ParserConfigurationException e) { //Si se produce una excepción de configuración del analizador.
            String eMsg = String.format("ParserConfigurationException: The feature '%s' is probably not supported by your XML processor", e.getMessage()); //Guarda el mensaje de la excepción.
            LOGGER.log(Level.INFO, eMsg); //Registra al nivel de información el mensaje de la excepción.
        }

        DocumentBuilder dBuilder = null; //Guarda el constructor de documentos como un valor nulo.
        try { //Trata de hacer el contenido del cuerpo.
            dBuilder = dbFactory.newDocumentBuilder(); //Crea un nuevo constructor de documentos y lo guarda.
        } catch (ParserConfigurationException e) { //Si se produce una excepción de configuración del analizador.
            String eMsg = String.format("ParserConfigurationException: %s", e.getMessage()); //Guarda el mensaje de la excepción.
            LOGGER.log(Level.WARNING, eMsg); //Registra al nivel de advertencia el mensaje de la excepción.
        }

        return dBuilder; //Devuelve el constructor de documentos.
    }

    /**
     * Transforms the document to the file.
     *
     * @param doc the document to transform.
     * @param file the file to save the transformation.
     */
    private static void transformDoc(Document doc, @NonNull File file) {
        DOMSource source = new DOMSource(doc); //Crea una fuente de modelo del objeto del documento y la guarda.
        StreamResult result = new StreamResult(file); //Crea un resultado de la transformación del archivo y lo guarda.

        TransformerFactory transformerFactory = TransformerFactory.newInstance(); //Crea una nueva instancia de una fábrica de transformadores y la guarda.
        try { //Trata de hacer el contenido del cuerpo.
            transformerFactory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true); //Establece que la fábrica procesa de forma segura.
        } catch (TransformerConfigurationException e) { //Si se produce una excepción de configuración del transformador.
            String eMsg = String.format("TransformerConfigurationException: The feature '%s' is probably not supported by your XML processor", e.getMessage()); //Guarda el mensaje de la excepción.
            LOGGER.log(Level.INFO, eMsg); //Registra al nivel de información el mensaje de la excepción.
        }

        try { //Trata de hacer el contenido del cuerpo.
            Transformer transformer = transformerFactory.newTransformer(); //Crea un nuevo transformador y lo guarda.
            transformer.setOutputProperty(OutputKeys.METHOD, "xml"); //Establece la propiedad de salida del transformador del método como XML.
            transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8"); //Establece la propiedad de salida del transformador de la codificación como UTF-8.
            transformer.setOutputProperty(OutputKeys.STANDALONE, "no"); //Establece la propiedad de salida del transformador de autónomo como no.
            transformer.setOutputProperty(OutputKeys.INDENT, "yes"); //Establece la propiedad de salida del transformador de la sangría como sí.
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "3"); //Establece la propiedad de salida del transformador de la cantidad de sangría como 3.
            transformer.transform(source, result); //Transforma la fuente al resultado.
        } catch (TransformerException e) { //Si se produce una excepción del transformador.
            String eMsg = String.format("TransformerException: %s", e.getMessage()); //Guarda el mensaje de la excepción.
            LOGGER.log(Level.WARNING, eMsg); //Registra al nivel de advertencia el mensaje de la excepción.
        }
    }

    /**
     * Gets the Fields of the node list.
     *
     * @param nodeList the node list from which to get the Fields.
     * @param formatter the formatter to parse the dates of the Fields.
     * @return the list of the Fields.
     */
    private static ArrayList<Field> getFields(@NonNull NodeList nodeList, SimpleDateFormat formatter) {
        ArrayList<Field> fieldList = new ArrayList<>(); //Crea una matriz de lista de los campos y la guarda.

        try { //Trata de hacer el contenido del cuerpo.
            for (int i = 0; i < nodeList.getLength(); i++) { //Por cada índice del 0 al final de la lista de nodos.
                Node node = nodeList.item(i); //Obtiene el nodo correspondiente de la lista de nodos y lo guarda.
                String idField = node.getAttributes().item(0).getTextContent(); //Obtiene la cadena del primer atributo del nodo y la guarda.
                String nameField = node.getAttributes().item(1).getTextContent(); //Obtiene la cadena del segundo atributo del nodo y la guarda.
                String typeField = node.getAttributes().item(2).getTextContent(); //Obtiene la cadena del tercer atributo del nodo y la guarda.
                String creationField = node.getAttributes().item(3).getTextContent(); //Obtiene la cadena del cuarto atributo del nodo y la guarda.

                int fieldId = Integer.parseInt(idField); //Analiza el identificador de campo como un entero y lo guarda.
                Date fieldCreation = formatter.parse(creationField); //Analiza la creación del campo como una fecha y la guarda.

                Field field = new Field(fieldId, nameField, typeField, fieldCreation); //Crea un nuevo campo y lo guarda.
                fieldList.add(field); //Agrega el campo a la lista de campos.
            }
        } catch (ParseException e) { //Si se produce una excepción de análisis.
            String eMsg = String.format(PARSE_EXC, e.getMessage()); //Guarda el mensaje de la excepción.
            LOGGER.log(Level.WARNING, eMsg); //Registra al nivel de advertencia el mensaje de la excepción.
        }

        return fieldList; //Devuelve la lista de campos.
    }

    /**
     * Parses the Lists from the path file and saves them.
     *
     * @param listsPath the file path of the Lists.
     * @param lists the map of the Lists.
     * @return the names of the parsed Lists.
     */
    protected static String[] parseListsFromFile(String listsPath, @NonNull LinkedHashMap<Integer, List> lists) {
        SimpleDateFormat longDate = new SimpleDateFormat(LONG_DATE, Locale.US); //Crea un formato de fechas simples largo y lo guarda.

        lists.clear(); //Elimina todas las listas.
        ArrayList<String> namesList = new ArrayList<>(); //Crea una matriz de lista de cadenas para los nombres y la guarda.
        File listsFile = new File(listsPath); //Crea el archivo XML listas de la ruta y lo guarda.

        if (listsFile.exists() && (listsFile.length() != 0)) { //Si el archivo listas existe y su longitud no es igual a 0.
            DocumentBuilder dBuilder = createDocBuilder(); //Crea un constructor de documentos y lo guarda.

            if (dBuilder != null) { //Si el constructor de documentos no es un valor nulo.
                try { //Trata de hacer el contenido del cuerpo.
                    Document doc = dBuilder.parse(listsFile); //Analiza los datos del archivo de las listas y guarda el resultado en el documento.
                    doc.getDocumentElement().normalize(); //Normaliza el elemento del documento.
                    NodeList nList = doc.getElementsByTagName("list"); //Obtiene los nodos del documento con la etiqueta list y los guarda como una lista.

                    for (int i = 0; i < nList.getLength(); i++) { //Por cada índice del 0 al final de la lista de nodos.
                        Node node = nList.item(i); //Obtiene el nodo correspondiente de la lista de nodos y lo guarda.

                        if (node.getNodeType() == Node.ELEMENT_NODE) { //Si el nodo es de tipo elemento.
                            Element element = (Element) node; //Adapta el nodo a un elemento y lo guarda.
                            String id = element.getAttribute(ID); //Obtiene el atributo id del elemento y lo guarda.
                            String name = element.getAttribute(NAME); //Obtiene el atributo name del elemento y lo guarda.
                            String creationList = element.getAttribute(CREATION_DATE); //Obtiene el atributo creationDate del elemento y lo guarda.

                            Node description = element.getElementsByTagName(DESCRIPTION).item(0); //Obtiene el primer nodo del elemento de etiqueta description y lo guarda.
                            String creationDesc = description.getAttributes().item(0).getTextContent(); //Obtiene la cadena del primer atributo de la descripción y la guarda.
                            String contentDesc = description.getTextContent(); //Obtiene el contenido de la descripción y lo guarda.

                            NodeList fieldList = element.getElementsByTagName(FIELD); //Obtiene los nodos del elemento con la etiqueta field y los guarda como una lista.
                            ArrayList<Field> fieldArray = getFields(fieldList, longDate); //Obtiene los campos de la lista y los guarda.

                            int listId = Integer.parseInt(id); //Analiza el identificador como un entero y lo guarda.
                            Date listCreation = longDate.parse(creationList); //Analiza la creación de la lista como una fecha y la guarda.
                            Date descCreation = longDate.parse(creationDesc); //Analiza la creación de la descripción como una fecha y la guarda.

                            Description desc = new Description(contentDesc, descCreation); //Crea una nueva descripción y la guarda.
                            List list = new List(listId, name, desc, fieldArray, listCreation); //Crea una nueva lista y la guarda.

                            namesList.add(name); //Agrega el nombre a la lista de nombres.
                            lists.put(listId, list); //Agrega la lista a las listas en el identificador de la lista.
                        }
                    }
                } catch (IOException | ParseException | SAXException e) { //Si se produce alguna excepción.
                    String eMsg = String.format("Exception: %s", e.getMessage()); //Guarda el mensaje de la excepción.
                    LOGGER.log(Level.WARNING, eMsg); //Registra al nivel de advertencia el mensaje de la excepción.

                    lists.clear(); //Elimina todas las listas.
                    namesList.clear(); //Elimina todos los nombres de la lista.
                }
            }
        }

        return namesList.toArray(new String[0]); //Devuelve la lista de los nombres convertida a una matriz de cadenas.
    }

    /**
     * Gets the Values of the node list.
     *
     * @param nodeList the node list from which to get the Values.
     * @param formatter the formatter to parse the dates of the Values.
     * @return the list of the Values.
     */
    private static ArrayList<Value> getValues(@NonNull NodeList nodeList, SimpleDateFormat formatter) {
        ArrayList<Value> valueList = new ArrayList<>(); //Crea una matriz de lista de los valores y la guarda.

        try { //Trata de hacer el contenido del cuerpo.
            for (int i = 0; i < nodeList.getLength(); i++) { //Por cada índice del 0 al final de la lista de nodos.
                Node node = nodeList.item(i); //Obtiene el nodo correspondiente de la lista de nodos y lo guarda.
                String idField = node.getAttributes().item(0).getTextContent(); //Obtiene la cadena del primer atributo del nodo y la guarda.
                String creationValue = node.getAttributes().item(1).getTextContent(); //Obtiene la cadena del segundo atributo del nodo y la guarda.
                String contentValue = node.getTextContent(); //Obtiene el contenido del nodo y lo guarda.

                int fieldId = Integer.parseInt(idField); //Analiza el identificador de campo como un entero y lo guarda.
                Date valueCreation = formatter.parse(creationValue); //Analiza la creación del valor como una fecha y la guarda.

                Value value = new Value(fieldId, contentValue, valueCreation); //Crea un nuevo valor y lo guarda.
                valueList.add(value); //Agrega el valor a la matriz de valores.
            }
        } catch (ParseException e) { //Si se produce una excepción de análisis.
            String eMsg = String.format(PARSE_EXC, e.getMessage()); //Guarda el mensaje de la excepción.
            LOGGER.log(Level.WARNING, eMsg); //Registra al nivel de advertencia el mensaje de la excepción.
        }

        return valueList; //Devuelve la lista de valores.
    }

    /**
     * Gets the Histories of the node list.
     *
     * @param nodeList the node list from which to get the Histories.
     * @return the list of the Histories.
     */
    private static ArrayList<History> getHistories(@NonNull NodeList nodeList) {
        ArrayList<History> historyList = new ArrayList<>(); //Crea una matriz de lista de las historias y la guarda.

        for (int i = 0; i < nodeList.getLength(); i++) { //Por cada índice del 0 al final de la lista de nodos.
            Node historyNode = nodeList.item(i); //Obtiene el nodo correspondiente de la lista de nodos y lo guarda.
            String fieldHistory = historyNode.getAttributes().item(0).getTextContent(); //Obtiene la cadena del primer atributo del nodo y la guarda.
            String valueHistory = historyNode.getAttributes().item(1).getTextContent(); //Obtiene la cadena del segundo atributo del nodo y la guarda.
            String typeCodeHistory = historyNode.getAttributes().item(2).getTextContent(); //Obtiene la cadena del tercer atributo del nodo y la guarda.
            String actionCodeHistory = historyNode.getAttributes().item(3).getTextContent(); //Obtiene la cadena del cuarto atributo del nodo y la guarda.

            int codeTypeHistory = Integer.parseInt(typeCodeHistory); //Analiza el código del tipo de la historia como un entero y lo guarda.
            int codeActionHistory = Integer.parseInt(actionCodeHistory); //Analiza el código de la acción de la historia como un entero y lo guarda.

            History.Type typeHistory; //Inicializa el tipo de la historia.
            switch (codeTypeHistory) { //Considera el código del tipo de la historia.
                case 1: //Código 1.
                    typeHistory = History.Type.TITLE; //Guarda el tipo de la historia como el tipo título.
                    break; //Corte.
                case 2: //Código 2.
                    typeHistory = History.Type.DESCRIPTION; //Guarda el tipo de la historia como el tipo descripción.
                    break; //Corte.
                case 3: //Código 3.
                    typeHistory = History.Type.STARTDATE; //Guarda el tipo de la historia como el tipo fecha de inicio.
                    break; //Corte.
                default: //Si no es ninguno de los anteriores.
                    typeHistory = History.Type.FIELD; //Guarda el tipo de la historia como el tipo campo.
                    break; //Corte.
            }

            History.Action actionHistory; //Inicializa la acción de la historia.
            switch (codeActionHistory) { //Considera el código de la acción de la historia.
                case 1: //Código 1.
                    actionHistory = History.Action.CREATE; //Guarda la acción de la historia como la acción crear.
                    break; //Corte.
                case 2: //Código 2.
                    actionHistory = History.Action.DELETE; //Guarda la acción de la historia como la acción eliminar.
                    break; //Corte.
                default: //Si no es ninguno de los anteriores.
                    actionHistory = History.Action.UPDATE; //Guarda la acción de la historia como la acción actualizar
                    break; //Corte.
            }

            History history = new History(fieldHistory, valueHistory, typeHistory, actionHistory); //Crea una nueva historia y la guarda.
            historyList.add(history); //Agrega la historia a la lista de historias.
        }

        return historyList; //Devuelve la lista de historias.
    }

    /**
     * Gets the Records of the node list.
     *
     * @param nodeList the node list from which to get the Records.
     * @param formatter the formatter to parse the dates of the Records.
     * @return the list of the Records.
     */
    private static ArrayList<Record> getRecords(NodeList nodeList, SimpleDateFormat formatter) {
        ArrayList<Record> recordList = new ArrayList<>(); //Crea una matriz de lista de los historiales y la guarda.

        try { //Trata de hacer el contenido del cuerpo.
            for (int i = 0; i < nodeList.getLength(); i++) { //Por cada índice del 0 al final de la lista de nodos.
                Node node = nodeList.item(i); //Obtiene el nodo correspondiente de la lista de nodos y lo guarda.

                if (node.getNodeType() == Node.ELEMENT_NODE) { //Si el nodo es de tipo elemento.
                    Element recordElement = (Element) node; //Adapta el nodo a un elemento del historial y lo guarda.
                    String creationRecord = recordElement.getAttributes().item(0).getTextContent(); //Obtiene la cadena del primer atributo del elemento del historial y la guarda.
                    NodeList historyList = recordElement.getElementsByTagName("history"); //Obtiene los nodos del elemento del historial con la etiqueta history y los guarda como una lista.

                    Date recordCreation = formatter.parse(creationRecord); //Analiza la creación del historial como una fecha y la guarda.
                    ArrayList<History> historyArray = getHistories(historyList); //Obtiene las historias de la lista y las guarda.

                    Record record = new Record(historyArray, recordCreation); //Crea un nuevo historial y lo guarda.
                    recordList.add(record); //Agrega el historial a la lista de historiales.
                }
            }
        } catch (ParseException e) { //Si se produce una excepción del análisis.
            String eMsg = String.format(PARSE_EXC, e.getMessage()); //Guarda el mensaje de la excepción.
            LOGGER.log(Level.WARNING, eMsg); //Registra al nivel de advertencia el mensaje de la excepción.
        }

        return recordList; //Devuelve la lista de historiales.
    }

    /**
     * Parses the Tasks from the path file and saves them.
     *
     * @param tasksPath the file path of the Tasks.
     * @param tasks the map of the Tasks.
     * @return the titles of the parsed Tasks.
     */
    protected static String[] parseTasksFromFile(String tasksPath, @NonNull LinkedHashMap<Integer, Task> tasks) {
        SimpleDateFormat longDate = new SimpleDateFormat(LONG_DATE, Locale.US); //Crea un formato de fechas simples largo y lo guarda.
        SimpleDateFormat shortDate = new SimpleDateFormat(SHORT_DATE, Locale.US); //Crea un formato de fechas simples corto y lo guarda.

        tasks.clear(); //Elimina todas las tareas.
        File tasksFile = new File(tasksPath); //Crea el archivo XML tareas de la ruta y lo guarda.
        ArrayList<String> titlesList = new ArrayList<>(); //Crea una matriz de lista de cadenas para los títulos y la guarda.

        if (tasksFile.exists() && (tasksFile.length() != 0)) { //Si el archivo tareas existe y su longitud no es igual a 0.
            DocumentBuilder dBuilder = createDocBuilder(); //Crea un constructor de documentos y lo guarda.

            if (dBuilder != null) { //Si el constructor de documentos no es un valor nulo.
                try { //Trata de hacer el contenido del cuerpo.
                    Document doc = dBuilder.parse(tasksFile); //Analiza los datos del archivo de las tareas y guarda el resultado en el documento.
                    doc.getDocumentElement().normalize(); //Normaliza el elemento del documento.
                    NodeList nList = doc.getElementsByTagName("task"); //Obtiene los nodos del documento con la etiqueta task y los guarda como una lista.

                    for (int i = 0; i < nList.getLength(); i++) { //Por cada índice del 0 al final de la lista de nodos.
                        Node node = nList.item(i); //Obtiene el nodo correspondiente de la lista de nodos y lo guarda.

                        if (node.getNodeType() == Node.ELEMENT_NODE) { //Si el nodo es de tipo elemento.
                            Element element = (Element) node; //Adapta el nodo a un elemento y lo guarda.
                            String id = element.getAttribute(ID); //Obtiene el atributo id del elemento y lo guarda.
                            String title = element.getAttribute(TITLE); //Obtiene el atributo title del elemento y lo guarda.
                            String idList = element.getAttribute("idList"); //Obtiene el atributo idList del elemento y lo guarda.
                            String creationTask = element.getAttribute(CREATION_DATE); //Obtiene el atributo creationDate del elemento y lo guarda.

                            Node description = element.getElementsByTagName(DESCRIPTION).item(0); //Obtiene el primer nodo del elemento de etiqueta description y lo guarda.
                            String creationDesc = description.getAttributes().item(0).getTextContent(); //Obtiene la cadena del primer atributo de la descripción y la guarda.
                            String contentDesc = description.getTextContent(); //Obtiene el contenido de la descripción y lo guarda.

                            NodeList valueList = element.getElementsByTagName(VALUE); //Obtiene los nodos del elemento con la etiqueta value y los guarda como una lista.
                            ArrayList<Value> valueArray = getValues(valueList, longDate); //Obtiene los valores de la lista y los guarda.

                            NodeList recordList = element.getElementsByTagName("record"); //Obtiene los nodos del elemento con la etiqueta record y los guarda como una lista.
                            ArrayList<Record> recordArray = getRecords(recordList, longDate); //Obtiene los historiales de la lista y los guarda.

                            int taskId = Integer.parseInt(id); //Analiza el identificador como un entero y lo guarda.
                            int listId = Integer.parseInt(idList); //Analiza el identificador de lista como un entero y lo guarda.

                            Date taskCreation = shortDate.parse(creationTask); //Analiza la creación de la tarea como una fecha y la guarda.
                            Date descCreation = longDate.parse(creationDesc); //Analiza la creación de la descripción como una fecha y la guarda.

                            Description desc = new Description(contentDesc, descCreation); //Crea una nueva descripción y la guarda.
                            Task task = new Task(taskId, listId, title, desc, valueArray, recordArray, taskCreation); //Crea una nueva tarea y la guarda.

                            titlesList.add(title); //Agrega el título a la lista de títulos.
                            tasks.put(taskId, task); //Agrega la tarea a las tareas en el identificador de la tarea.
                        }
                    }
                } catch (IOException | ParseException | SAXException e) { //Si se produce alguna excepción.
                    String eMsg = String.format("Exception: %s", e.getMessage()); //Guarda el mensaje de la excepción.
                    LOGGER.log(Level.WARNING, eMsg); //Registra al nivel de advertencia el mensaje de la excepción.

                    tasks.clear(); //Elimina todas las tareas.
                    titlesList.clear(); //Elimina todos los títulos de la lista.
                }
            }
        }

        return titlesList.toArray(new String[0]); //Devuelve la lista de los títulos convertida a una matriz de cadenas.
    }

    /**
     * Adds the Tasks to the Lists..
     *
     * @param lists the map of the Lists.
     * @param tasks the map of the Tasks.
     */
    protected static void addTasksToLists(@NonNull LinkedHashMap<Integer, List> lists, @NonNull LinkedHashMap<Integer, Task> tasks) {
        if (!lists.isEmpty() && !tasks.isEmpty()) //Si las listas y las tareas no están vacías.
            for (Map.Entry<Integer, Task> entry: tasks.entrySet()) { //Por cada entrada de las tareas.
                Task task = entry.getValue(); //Obtiene el valor de la entrada y lo guarda como una tarea.
                int idList = task.getIdList(); //Obtiene el identificador de lista de la tarea y lo guarda.

                List list = lists.get(idList); //Obtiene la lista del identificador y la guarda.
                if (list != null) //Si la lista no es un valor nulo.
                    list.addTask(task); //Agrega la tarea a la lista.
            }
    }

    /**
     * Writes the Lists in the path file.
     *
     * @param listsPath the file path of the Lists.
     * @param lists the map of the Lists.
     * @return the names of the written Lists.
     */
    protected static String[] writeListsToFile(String listsPath, @NonNull LinkedHashMap<Integer, List> lists) {
        SimpleDateFormat longDate = new SimpleDateFormat(LONG_DATE, Locale.US); //Crea un formato de fechas simples largo y lo guarda.

        File listsFile = new File(listsPath); //Crea el archivo XML listas de la ruta y lo guarda.
        ArrayList<String> namesList = new ArrayList<>(); //Crea una matriz de lista de cadenas para los nombres y la guarda.

        DocumentBuilder dBuilder = createDocBuilder(); //Crea un constructor de documentos y lo guarda.

        if (dBuilder != null) { //Si el constructor de documentos no es un valor nulo.
            Document doc = dBuilder.newDocument(); //Crea un nuevo documento y lo guarda.

            Element rootElem = doc.createElement("lists"); //Crea el elemento lists y lo guarda como el elemento raíz.
            doc.appendChild(rootElem); //Agrega como hijo el elemento raíz en el documento.

            for (Map.Entry<Integer, List> entry: lists.entrySet()) { //Por cada entrada de las listas.
                List list = entry.getValue(); //Obtiene el valor de la entrada y lo guarda como una lista.

                Element listElem = doc.createElement("list"); //Crea el elemento list y lo guarda.
                rootElem.appendChild(listElem); //Agrega como hijo el elemento de lista en el raíz.

                int id = list.getId(); //Obtiene el identificador de la lista y lo guarda.
                String idList = String.valueOf(id); //Obtiene el valor del identificador como una cadena y la guarda.

                Attr listIdAttr = doc.createAttribute(ID); //Crea el atributo id y lo guarda.
                listIdAttr.setValue(idList); //Establece el valor del atributo id como el identificador de la lista.
                listElem.setAttributeNode(listIdAttr); //Agrega el nodo de atributo id al elemento de lista.

                String name = list.getName(); //Obtiene el nombre de la lista y lo guarda.
                namesList.add(name); //Agrega el nombre a la lista de nombres.

                Attr listNameAttr = doc.createAttribute(NAME); //Crea el atributo name y lo guarda.
                listNameAttr.setValue(list.getName()); //Establece el valor del atributo name como el nombre.
                listElem.setAttributeNode(listNameAttr); //Agrega el nodo de atributo name al elemento de lista.

                Date creationDate = list.getCreationDate(); //Obtiene la fecha de creación de la lista y la guarda.
                String creationString = longDate.format(creationDate); //Formatea la fecha de creación a una cadena y la guarda.

                Attr listDateAttr = doc.createAttribute(CREATION_DATE); //Crea el atributo creationDate y lo guarda.
                listDateAttr.setValue(creationString); //Establece el valor del atributo creationDate como la cadena de creación.
                listElem.setAttributeNode(listDateAttr); //Agrega el nodo de atributo creationDate al elemento de lista.

                Description desc = list.getDescription(); //Obtiene la descripción de la lista.

                creationDate = desc.getCreationDate(); //Obtiene la fecha de creación de la descripción y la guarda.
                creationString = longDate.format(creationDate); //Formatea la fecha de creación a una cadena y la guarda.

                Element descElem = doc.createElement(DESCRIPTION); //Crea el elemento description y lo guarda.
                listElem.appendChild(descElem); //Agrega como hijo el elemento de descripción en el de lista.

                Attr descDateAttr = doc.createAttribute(CREATION_DATE); //Crea el atributo creationDate y lo guarda.
                descDateAttr.setValue(creationString); //Establece el valor del atributo creationDate como la cadena de creación.
                descElem.setAttributeNode(descDateAttr); //Agrega el nodo de atributo creationDate al elemento de descripción.
                descElem.setTextContent(desc.getContent()); //Establece el contenido de texto como el contenido de la descripción de su elemento.

                ArrayList<Field> fields = list.getFields(); //Obtiene los campos de la lista y los guarda.

                Element fieldsElem = doc.createElement("fields"); //Crea el elemento fields y lo guarda.
                listElem.appendChild(fieldsElem); //Agrega como hijo el elemento de campos en el de lista.

                for (Field field: fields) { //Por cada campo de los campos.
                    Element fieldElem = doc.createElement(FIELD); //Crea el elemento field y lo guarda.
                    fieldsElem.appendChild(fieldElem); //Agrega como hijo el elemento de campo en el de campos.

                    id = field.getId(); //Obtiene el identificador del campo y lo guarda.
                    String idField = String.valueOf(id); //Obtiene el valor del identificador como una cadena y la guarda.

                    Attr fieldIdAttr = doc.createAttribute(ID); //Crea el atributo id y lo guarda.
                    fieldIdAttr.setValue(idField); //Establece el valor del atributo id como el identificador del campo.
                    fieldElem.setAttributeNode(fieldIdAttr); //Agrega el nodo de atributo id al elemento de campo.

                    Attr fieldNameAttr = doc.createAttribute(NAME); //Crea el atributo name y lo guarda.
                    fieldNameAttr.setValue(field.getName()); //Establece el valor del atributo name como el nombre del campo.
                    fieldElem.setAttributeNode(fieldNameAttr); //Agrega el nodo de atributo name al elemento de campo.

                    Attr fieldTypeAttr = doc.createAttribute("type"); //Crea el atributo type y lo guarda.
                    fieldTypeAttr.setValue(field.getType()); //Establece el valor del atributo type como el tipo del campo.
                    fieldElem.setAttributeNode(fieldTypeAttr); //Agrega el nodo de atributo type al elemento de campo.

                    creationDate = field.getCreationDate(); //Obtiene la fecha de creación del campo y la guarda.
                    creationString = longDate.format(creationDate); //Formatea la fecha de creación a una cadena y la guarda.

                    Attr fieldDateAttr = doc.createAttribute(CREATION_DATE); //Crea el atributo creationDate y lo guarda.
                    fieldDateAttr.setValue(creationString); //Establece el valor del atributo creationDate como la cadena de creación.
                    fieldElem.setAttributeNode(fieldDateAttr); //Agrega el nodo de atributo creationDate al elemento de campo.
                }
            }

            transformDoc(doc, listsFile); //Transforma el documento al archivo listas.
        }

        return namesList.toArray(new String[0]); //Convierte la lista de los nombres a una matriz de cadenas y la guarda como los nombres.
    }

    /**
     * Writes the Tasks in the path file.
     *
     * @param tasksPath the file path of the Tasks.
     * @param tasks the map of the Tasks.
     * @return the titles of the written Tasks.
     */
    protected static String[] writeTasksToFile(String tasksPath, @NonNull LinkedHashMap<Integer, Task> tasks) {
        SimpleDateFormat longDate = new SimpleDateFormat(LONG_DATE, Locale.US); //Crea un formato de fechas simples largo y lo guarda.
        SimpleDateFormat shortDate = new SimpleDateFormat(SHORT_DATE, Locale.US); //Crea un formato de fechas simples corto y lo guarda.

        File tasksFile = new File(tasksPath); //Crea el archivo XML tareas de la ruta y lo guarda.
        ArrayList<String> titlesList = new ArrayList<>(); //Crea una matriz de lista de cadenas para los títulos y la guarda.

        DocumentBuilder dBuilder = createDocBuilder(); //Crea un constructor de documentos y lo guarda.

        if (dBuilder != null) { //Si el constructor de documentos no es un valor nulo.
            Document doc = dBuilder.newDocument(); //Crea un nuevo documento y lo guarda.

            Element rootElem = doc.createElement("tasks"); //Crea el elemento tasks y lo guarda como el elemento raíz.
            doc.appendChild(rootElem); //Agrega como hijo el elemento raíz en el documento.

            for (Map.Entry<Integer, Task> entry: tasks.entrySet()) { //Por cada entrada de las tareas.
                Task task = entry.getValue(); //Obtiene el valor de la entrada y lo guarda como una tarea.

                Element taskElem = doc.createElement("task"); //Crea el elemento task y lo guarda.
                rootElem.appendChild(taskElem); //Agrega como hijo el elemento de tarea en el raíz.

                int id = task.getId(); //Obtiene el identificador de la tarea y lo guarda.
                String idTask = String.valueOf(id); //Obtiene el valor del identificador como una cadena y la guarda.

                Attr taskIdAttr = doc.createAttribute(ID); //Crea el atributo id y lo guarda.
                taskIdAttr.setValue(idTask); //Establece el valor del atributo id como el identificador de la tarea.
                taskElem.setAttributeNode(taskIdAttr); //Agrega el nodo de atributo id al elemento de tarea.

                String title = task.getTitle(); //Obtiene el título de la tarea y lo guarda.
                titlesList.add(title); //Agrega el título a la lista de títulos.

                Attr taskTitleAttr = doc.createAttribute(TITLE); //Crea el atributo title y lo guarda.
                taskTitleAttr.setValue(task.getTitle()); //Establece el valor del atributo title como el título de la tarea.
                taskElem.setAttributeNode(taskTitleAttr); //Agrega el nodo de atributo title al elemento de tarea.

                int idList = task.getIdList(); //Obtiene el identificador de lista de la tarea y lo guarda.
                String listId = String.valueOf(idList); //Obtiene el valor del identificador de lista como una cadena y la guarda.

                Attr taskIdListAttr = doc.createAttribute("idList"); //Crea el atributo idList y lo guarda.
                taskIdListAttr.setValue(listId); //Establece el valor del atributo idList como el identificador de lista.
                taskElem.setAttributeNode(taskIdListAttr); //Agrega el nodo de atributo idList al elemento de tarea.

                Date creationDate = task.getCreationDate(); //Obtiene la fecha de creación de la tarea y la guarda.
                String creationString = shortDate.format(creationDate); //Formatea la fecha de creación a una cadena y la guarda.

                Attr taskDateAttr = doc.createAttribute(CREATION_DATE); //Crea el atributo creationDate y lo guarda.
                taskDateAttr.setValue(creationString); //Establece el valor del atributo creationDate como la cadena de creación.
                taskElem.setAttributeNode(taskDateAttr); //Agrega el nodo de atributo creationDate al elemento de tarea.

                Description desc = task.getDescription(); //Obtiene la descripción de la tarea.

                creationDate = desc.getCreationDate(); //Obtiene la fecha de creación de la descripción y la guarda.
                creationString = longDate.format(creationDate); //Formatea la fecha de creación a una cadena y la guarda.

                Element descElem = doc.createElement(DESCRIPTION); //Crea el elemento description y lo guarda.
                taskElem.appendChild(descElem); //Agrega como hijo el elemento de descripción en el de tarea.

                Attr descDateAttr = doc.createAttribute(CREATION_DATE); //Crea el atributo creationDate y lo guarda.
                descDateAttr.setValue(creationString); //Establece el valor del atributo creationDate como la cadena de creación.
                descElem.setAttributeNode(descDateAttr); //Agrega el nodo de atributo creationDate al elemento de descripción.
                descElem.setTextContent(desc.getContent()); //Establece el contenido de texto como el contenido de la descripción de su elemento.

                ArrayList<Value> values = task.getValues(); //Obtiene los valores de la tarea y los guarda.

                Element valuesElem = doc.createElement("values"); //Crea el elemento values y lo guarda.
                taskElem.appendChild(valuesElem); //Agrega como hijo el elemento de valores en el de tarea.

                for (Value value: values) { //Por cada valor de los valores.
                    Element valueElem = doc.createElement(VALUE); //Crea el elemento value y lo guarda.
                    valuesElem.appendChild(valueElem); //Agrega como hijo el elemento de valor en el de valores.

                    int idField = value.getIdField(); //Obtiene el identificador de campo del valor y lo guarda.
                    String fieldId = String.valueOf(idField); //Obtiene el valor del identificador como una cadena y la guarda.

                    Attr valueIdFieldAttr = doc.createAttribute("idField"); //Crea el atributo idField y lo guarda.
                    valueIdFieldAttr.setValue(fieldId); //Establece el valor del atributo idField como el identificador de campo.
                    valueElem.setAttributeNode(valueIdFieldAttr); //Agrega el nodo de atributo idField al elemento de valor.

                    creationDate = value.getCreationDate(); //Obtiene la fecha de creación del valor y la guarda.
                    creationString = longDate.format(creationDate); //Formatea la fecha de creación a una cadena y la guarda.

                    Attr valueDateAttr = doc.createAttribute(CREATION_DATE); //Crea el atributo creationDate y lo guarda.
                    valueDateAttr.setValue(creationString); //Establece el valor del atributo creationDate como la cadena de creación.
                    valueElem.setAttributeNode(valueDateAttr); //Agrega el nodo de atributo creationDate al elemento de valor.
                    valueElem.setTextContent(value.getContent()); //Establece el contenido de texto como el contenido del valor de su elemento.
                }

                ArrayList<Record> records = task.getRecords(); //Obtiene los historiales de la tarea y los guarda.

                Element recordsElem = doc.createElement("records"); //Crea el elemento records y lo guarda.
                taskElem.appendChild(recordsElem); //Agrega como hijo el elemento de historiales en el de tarea.

                for (Record record: records) { //Por cada historial de los historiales.
                    Element recordElem = doc.createElement("record"); //Crea el elemento record y lo guarda.
                    recordsElem.appendChild(recordElem); //Agrega como hijo el elemento de historial en el de historiales.

                    creationDate = record.getCreationDate(); //Obtiene la fecha de creación del historial y la guarda.
                    creationString = longDate.format(creationDate); //Formatea la fecha de creación a una cadena y la guarda.

                    Attr recordDateAttr = doc.createAttribute(CREATION_DATE); //Crea el atributo creationDate y lo guarda.
                    recordDateAttr.setValue(creationString); //Establece el valor del atributo creationDate como la cadena de creación.
                    recordElem.setAttributeNode(recordDateAttr); //Agrega el nodo de atributo creationDate al elemento de historial.

                    ArrayList<History> histories = record.getHistories(); //Obtiene las historias del historial y las guarda.

                    for (History history: histories) { //Por cada historia de las historias.
                        Element historyElem = doc.createElement("history"); //Crea el elemento history y lo guarda.
                        recordElem.appendChild(historyElem); //Agrega como hijo el elemento de historia en el de historial.

                        Attr historyFieldAttr = doc.createAttribute(FIELD); //Crea el atributo field y lo guarda.
                        historyFieldAttr.setValue(history.getField()); //Establece el valor del atributo field como el campo de la historia.
                        historyElem.setAttributeNode(historyFieldAttr); //Agrega el nodo de atributo field al elemento de historia.

                        Attr historyValueAttr = doc.createAttribute(VALUE); //Crea el atributo value y lo guarda.
                        historyValueAttr.setValue(history.getValue()); //Establece el valor del atributo value como el valor de la historia.
                        historyElem.setAttributeNode(historyValueAttr); //Agrega el nodo de atributo value al elemento de historia.

                        int typeCode = history.getType().getTypeCode(); //Obtiene el código del tipo de la historia y lo guarda.
                        String codeType = String.valueOf(typeCode); //Obtiene el valor del código del tipo como una cadena y la guarda.

                        Attr historyTypeAttr = doc.createAttribute("type"); //Crea el atributo type y lo guarda.
                        historyTypeAttr.setValue(codeType); //Establece el valor del atributo type como el código del tipo.
                        historyElem.setAttributeNode(historyTypeAttr); //Agrega el nodo de atributo type al elemento de historia.

                        int actionCode = history.getAction().getActionCode(); //Obtiene el código de la acción de la historia y lo guarda.
                        String codeAction = String.valueOf(actionCode); //Obtiene el valor del código de la acción como una cadena y la guarda.

                        Attr historyActionAttr = doc.createAttribute("action"); //Crea el atributo action y lo guarda.
                        historyActionAttr.setValue(codeAction); //Establece el valor del atributo action como el código de la acción.
                        historyElem.setAttributeNode(historyActionAttr); //Agrega el nodo de atributo action al elemento de historia.
                    }
                }
            }

            transformDoc(doc, tasksFile); //Transforma el documento al archivo tareas.
        }

        return titlesList.toArray(new String[0]); //Convierte la lista de los títulos a una matriz de cadenas y la guarda como los títulos.
    }

    /**
     * Writes the List in the path file.
     *
     * @param listsPath the file path of the Lists.
     * @param list the List to be written.
     */
    protected static void writeList(String listsPath, @NonNull List list) {
        try { //Trata de hacer el contenido del cuerpo.
            File listsFile = new File(listsPath); //Crea el archivo XML listas del almacenamiento externo de la aplicación y lo guarda.

            if (!listsFile.exists()) {//Si el archivo listas no existe.
                boolean listsCreated = listsFile.createNewFile(); //Crea un nuevo archivo vacío listas y guarda si lo consigue.

                if (listsCreated) { //Si consiguió crear el archivo listas.
                    LinkedHashMap<Integer, List> lists = new LinkedHashMap<>(); //Guarda el mapa de las listas.
                    int currentId = list.getId(); //Obtiene el identificador de la lista y lo guarda.

                    lists.put(currentId, list); //Agrega la lista a las listas en la posición del identificador.
                    writeListsToFile(listsPath, lists); //Escribe las listas en el archivo de la ruta.
                }
            }
        } catch (IOException e) { //Si se produce una excepción de E/S.
            String eMsg = String.format(IO_EXC, e.getMessage()); //Guarda el mensaje de la excepción.
            LOGGER.log(Level.WARNING, eMsg); //Registra al nivel de advertencia el mensaje de la excepción.
        }
    }

    /**
     * Parses the Tasks.
     *
     * @param listsPath the file path of the Lists to parse.
     * @param list the List from which the parser starts.
     * @param tasksPath the file path of the Tasks to check.
     * @param tasks the map of the Tasks for the parser.
     * @return the titles of the parsed Tasks.
     */
    protected static String[] parseTasks(String listsPath, @NonNull List list, String tasksPath, @NonNull LinkedHashMap<Integer, Task> tasks) {
        String[] titles = new String[0]; //Guarda los títulos como una matriz de cadenas vacía.

        try { //Trata de hacer el contenido del cuerpo.
            File tasksFile = new File(tasksPath); //Crea el archivo XML tareas del almacenamiento externo de la aplicación y lo guarda.

            if (tasksFile.exists()) { //Si el archivo tareas existe.
                titles = parseTasksFromFile(tasksPath, tasks); //Analiza las tareas desde el archivo y guarda sus títulos.
            } else { //Si el archivo no existe.
                boolean tasksCreated = tasksFile.createNewFile(); //Crea un nuevo archivo vacío tareas y guarda si lo consigue.

                if (tasksCreated) { //Si consiguió crear el archivo tareas.
                    ArrayList<String> titlesList = new ArrayList<>(); //Crea una matriz de lista de cadenas para los títulos y la guarda.
                    ArrayList<Task> taskList = list.getTasks(); //Obtiene las tareas de la lista y las guarda.

                    for (Task taskItem: taskList) { //Por cada elemento de la lista de tareas.
                        int id = taskItem.getId(); //Obtiene el identificador del elemento de la tarea y lo guarda.
                        String title = taskItem.getTitle(); //Obtiene el título del elemento de la tarea y lo guarda.

                        titlesList.add(title); //Agrega el título a la lista de títulos.
                        tasks.put(id, taskItem); //Agrega el elemento de tarea a las tareas en la posición del identificador.
                    }

                    titles = titlesList.toArray(new String[0]); //Convierte la lista de los títulos a una matriz de cadenas y la guarda como los títulos.

                    writeTasksToFile(tasksPath, tasks); //Escribe las tareas en el archivo de la ruta.
                }
            }

            writeList(listsPath, list); //Escribe la lista en el archivo de la ruta.
        } catch (IOException e) { //Si se produce una excepción de E/S.
            String eMsg = String.format(IO_EXC, e.getMessage()); //Guarda el mensaje de la excepción.
            LOGGER.log(Level.WARNING, eMsg); //Registra al nivel de advertencia el mensaje de la excepción.
        }

        return titles; //Devuelve los títulos.
    }

    /**
     * Checks the Tasks.
     *
     * @param listsPath the file path of the Lists to check.
     * @param list the List from which the check starts.
     * @param tasksPath the file path of the Tasks to check.
     * @param tasks the map of the Tasks for the check.
     * @return the indicator that tasks have been checked.
     */
    protected static boolean checkTasks(String listsPath, @NonNull List list, String tasksPath, @NonNull LinkedHashMap<Integer, Task> tasks) {
        File tasksFile = new File(tasksPath); //Crea el archivo tareas de la ruta y lo guarda.
        boolean tasksExisted = tasksFile.exists(); //Guarda si el archivo tareas existe.

        if (!tasksExisted) { //Si el archivo tareas no existe.
            try { //Trata de hacer el contenido del cuerpo.
                tasksExisted = tasksFile.createNewFile(); //Crea un nuevo archivo tareas y guarda si lo consigue.
            } catch (IOException e) { //Si se produce una excepción de E/S.
                String eMsg = String.format(IO_EXC, e.getMessage()); //Guarda el mensaje de la excepción.
                LOGGER.log(Level.WARNING, eMsg); //Registra al nivel de advertencia el mensaje de la excepción.
            }

            ArrayList<Task> taskList = list.getTasks(); //Obtiene las tareas de la lista y las guarda.
            tasks.clear(); //Elimina todas las tareas.

            for (Task task: taskList) { //Por cada tarea de la lista de tareas.
                int id = task.getId(); //Obtiene el identificador de la tarea y lo guarda.
                tasks.put(id, task); //Agrega la tarea a las tareas en el identificador.
            }

            writeList(listsPath, list); //Escribe la lista en el archivo de la ruta.
        }

        return tasksExisted; //Devuelve si existe el archivo tareas.
    }

    /**
     * Hides the keyboard shown in the activity.
     *
     * @param activity the activity in which the keyboard is shown.
     */
    protected static void hideKeyboard(@NonNull AppCompatActivity activity) {
        View currentFocus = activity.getCurrentFocus(); //Obtiene la imagen enfocada actual y la guarda.
        if (currentFocus != null) { //Si el foco actual no es un valor nulo.
            InputMethodManager systemService = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE); //Obtiene el servicio del método de entrada y lo guarda.
            if (systemService != null) //Si el servicio del sistema no es un valor nulo.
                systemService.hideSoftInputFromWindow(currentFocus.getWindowToken(), 0); //Oculta la ventana de entrada del foco actual.
        }
    }

    /**
     * Strips the accents from the string and returns the stripped string.
     * @param s the string to be stripped.
     * @return the string without the accents.
     */
    protected static String stripAccents(@NonNull String s) {
        String normalized = Normalizer.normalize(s.toLowerCase(), Normalizer.Form.NFD); //Normaliza la cadena en minúsculas y la guarda como la cadena normalizada.
        normalized = normalized.replaceAll("\\p{InCombiningDiacriticalMarks}+", ""); //Elimina todos los diacríticos de la cadena normalizada y la guarda.
        return normalized; //Devuelve la cadena normalizada.
    }

    /**
     * Shows the custom tooltip in the view.
     * @param activity the activity in which the tooltip will be shown.
     * @param view the view to which the tooltip is attached.
     * @param position the position at which the tooltip will be shown.
     * @param text the text displayed in the tooltip.
     */
    protected static ViewTooltip showCustomTooltip(@NonNull AppCompatActivity activity, @NonNull View view, ViewTooltip.Position position, String text) {
        Typeface typeface = ResourcesCompat.getFont(activity, R.font.aller_regular); //Obtiene la fuente regular de los recursos y la guarda.

        ViewTooltip viewTooltip = ViewTooltip.on(activity, view); //Crea una vista de la información de herramienta para la vista y la guarda.
        viewTooltip.arrowWidth(0); //Establece la anchura de la flecha de la vista de información de herramienta como 0.
        viewTooltip.arrowHeight(0); //Establece la altura de la flecha de la vista de información de herramienta como 0.
        viewTooltip.autoHide(true, 2000); //Establece que la vista de información de herramienta se oculta automáticamente después de 2 segundos.
        viewTooltip.clickToHide(true); //Establece que la vista de información de herramienta se oculta cuando se hace clic en ella.
        viewTooltip.color(TOOLTIP_BKGD_COLOR); //Establece el color de la vista de información de herramienta.
        viewTooltip.corner(20); //Establece el radio de la esquina de la vista de información de herramienta como 20.
        viewTooltip.distanceWithView(10); //Establece la distancia con la vista de la vista de información de herramienta como 10.
        viewTooltip.padding(23, 16, 25, 20); //Establece el relleno de la vista de información de herramienta como 23, 16, 25 y 20.
        viewTooltip.position(position); //Establece la posición de la vista de información de herramienta.
        viewTooltip.text(text); //Establece el texto de la vista de información de herramienta.
        viewTooltip.textColor(TOOLTIP_TEXT_COLOR); //Establece el color del texto de la vista de información de herramienta.
        viewTooltip.textSize(TypedValue.COMPLEX_UNIT_SP, 16); //Establece el tamaño del texto de la vista de información de herramienta como 16.
        viewTooltip.textTypeFace(typeface); //Establece el tipo de letra de la vista de información de herramienta.

        return viewTooltip; //Devuelve la vista de la información de herramienta.
    }

    /**
     * Opens a Date Picker Dialog to get one and write it.
     *
     * @param activity the activity in which the dialog will be opened.
     * @param view the view in which to write the gotten date.
     */
    protected static void getDate(@NonNull AppCompatActivity activity, @NonNull View view) {
        final EditText et = (EditText) view; //Adapta la vista a un texto de edición y lo guarda.
        final Calendar cal = Calendar.getInstance(); //Obtiene la instancia del calendario actual y la guarda.

        final int dayCal = cal.get(Calendar.DAY_OF_MONTH); //Obtiene el día del mes del calendario y lo guarda.
        final int monthCal = cal.get(Calendar.MONTH); //Obtiene el mes del calendario y lo guarda.
        final int yearCal = cal.get(Calendar.YEAR); //Obtiene el año del calendario y lo guarda.

        DatePickerDialog dateDialog = new DatePickerDialog(activity, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                final int currentMonth = month + 1; //Calcula el mes actual y lo guarda.

                String formattedDay = (dayOfMonth < 10)? ZERO + dayOfMonth: String.valueOf(dayOfMonth); //Crea el día formateado con el cero y lo guarda.
                String formattedMonth = (currentMonth < 10)? ZERO + currentMonth: String.valueOf(currentMonth); //Crea el mes formateado con el cero y lo guarda.
                String formattedDate = formattedDay + SLASH + formattedMonth + SLASH + year; //Crea la fecha formateada con la barra oblicua y la guarda.

                et.setText(formattedDate); //Establece el texto del texto de edición.
            }
        }, yearCal, monthCal, dayCal); //Crea un nuevo diálogo de selector de fechas para la fecha actual y lo guarda.

        dateDialog.show(); //Muestra el diálogo de fechas.
    }

    /**
     * Returns to the Main Activity.
     *
     * @param activity the activity from which you start.
     */
    protected static void backMain(@NonNull AppCompatActivity activity) {
        Intent main = new Intent(activity, MainActivity.class); //Crea una intención de pasar de esta actividad a la principal y la guarda.
        activity.startActivity(main); //Inicia la actividad principal.
        activity.finish(); //Finaliza la actividad.
    }

    /**
     * Shows the About Activity.
     *
     * @param activity the activity from which you start.
     */
    protected static void showAbout(@NonNull AppCompatActivity activity) {
        Intent about = new Intent(activity, AboutActivity.class); //Crea una intención de pasar de esta actividad a la de acerca de y la guarda.
        activity.startActivity(about); //Inicia la actividad de acerca de.
    }

}

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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

/**
 * A class that stores the data of a Task, including its Values ​​and Records.
 *
 * @author Raul Martin Perez (raumar95@gmail.com)
 */
class Task implements Serializable {

    /**
     * The identifier of the Task.
     */
    private int id; //Declara el identificador de la tarea.

    /**
     * The List identifier of the Task.
     */
    private int idList; //Declara el identificador de lista de la tarea.

    /**
     * The title of the Task.
     */
    private String title; //Declara el título de la tarea.

    /**
     * The Description of the Task.
     */
    private Description description; //Declara la descripción de la tarea.

    /**
     * The Values of the Task.
     */
    private ArrayList<Value> values; //Declara los valores de la tarea.

    /**
     * The Records of the Task.
     */
    private ArrayList<Record> records; //Declara los historiales de la tarea.

    /**
     * The creation date of the Task.
     */
    private Date creationDate; //Declara la fecha de creación de la tarea.

    /**
     * Constructs a new Task with the given data.
     *
     * @param idList the List identifier of the new Task.
     * @param title the title of the new Task.
     * @param description the Description of the new Task.
     */
    Task(int idList, String title, Description description) {
        //Llama al constructor con un uno, el identificador de lista, el título, la descripción,
        //una lista vacía de valores, una lista vacía de historiales y la fecha actual.
        this(1, idList, title, description, new ArrayList<Value>(), new ArrayList<Record>(), new Date());
    }

    /**
     * Constructs a new Task with the given data.
     *
     * @param id the identifier of the new Task.
     * @param idList the List identifier of the new Task.
     * @param title the title of the new Task.
     * @param description the Description of the new Task.
     * @param values the Values of the new Task.
     * @param creationDate the creation date of the new Task.
     */
    Task(int id, int idList, String title, Description description, ArrayList<Value> values, Date creationDate) {
        //Llama al constructor con el identificador, el identificador de lista, el título,
        //la descripción, los valores, una lista vacía de historiales y la fecha de creación.
        this(id, idList, title, description, values, new ArrayList<Record>(), creationDate);
    }

    /**
     * Constructs a new Task with the given data.
     *
     * @param id the identifier of the new Task.
     * @param idList the List identifier of the new Task.
     * @param title the title of the new Task.
     * @param description the Description of the new Task.
     * @param values the Values of the new Task.
     * @param records the Records of the new Task.
     * @param creationDate the creation date of the new Task.
     */
    Task(int id, int idList, String title, Description description, ArrayList<Value> values, ArrayList<Record> records, Date creationDate) {
        this.id = id; //Guarda el identificador de la nueva tarea como el pasado.
        this.idList = idList; //Guarda el identificador de lista de la nueva tarea como el pasado.
        this.title = title; //Guarda el título de la nueva tarea como el pasado.
        this.description = description; //Guarda la descripción de la nueva tarea como la pasada.
        this.values = values; //Guarda los valores de la nueva tarea como los pasados.
        this.records = records; //Guarda los historiales de la nueva tarea como los pasados.
        this.creationDate = creationDate; //Guarda la fecha de creación de la nueva tarea como la pasada.
    }

    /**
     * Gets the identifier of this Task and returns it.
     *
     * @return the identifier of this Task.
     */
    int getId() {
        return id; //Devuelve el identificador de esta tarea.
    }

    /**
     * Sets the new identifier of this Task.
     *
     * @param id the new identifier of this task.
     */
    void setId(int id) {
        this.id = id; //Guarda el identificador de esta tarea como el pasado.
    }

    /**
     * Gets the List identifier of this Task and returns it.
     *
     * @return the List identifier of this Task.
     */
    int getIdList() {
        return idList; //Devuelve el identificador de lista de esta tarea.
    }

    /**
     * Sets the new List identifier of this Task.
     *
     * @param idList the new List identifier of this Task.
     */
    void setIdList(int idList) {
        this.idList = idList; //Guarda el identificador de lista de esta tarea como el pasado.
    }

    /**
     * Gets the title of this Task and returns it.
     *
     * @return the title of this Task.
     */
    String getTitle() {
        return title; //Devuelve el título de esta tarea.
    }

    /**
     * Sets the new title of this Task.
     *
     * @param title the new title of this Task.
     */
    void setTitle(String title) {
        this.title = title; //Guarda el título de esta tarea como el pasado.
    }

    /**
     * Gets the Description of this Task and returns it.
     *
     * @return the Description of this Task.
     */
    Description getDescription() {
        return description; //Devuelve la desripción de esta tarea.
    }

    /**
     * Sets the new Description of this Task.
     *
     * @param description the new Description of this Task.
     */
    void setDescription(Description description) {
        this.description = description; //Guarda la descripción de esta tarea como la pasada.
    }

    /**
     * Gets the Values of this Task and returns it.
     *
     * @return the Values of this Task.
     */
    ArrayList<Value> getValues() {
        return values; //Devuelve los valores de esta tarea.
    }

    /**
     * Sets the new Values of this Task.
     *
     * @param values the new Values of this Task.
     */
    void setValues(ArrayList<Value> values) {
        this.values = values; //Guarda los valores de esta tarea como los pasados.
    }

    /**
     * Gets the Records of this Task and returns it.
     *
     * @return the Records of this Task.
     */
    ArrayList<Record> getRecords() {
        return records; //Devuelve los historiales de esta tarea.
    }

    /**
     * Sets the new Records of this Task.
     *
     * @param records the new Records of this Task.
     */
    void setRecords(ArrayList<Record> records) {
        this.records = records; //Guarda los historiales de esta tarea como los pasados.
    }

    /**
     * Gets the creation date of this Task and returns it.
     *
     * @return the creation date of this Task.
     */
    Date getCreationDate() {
        return creationDate; //Devuelve la fecha de creación de esta tarea.
    }

    /**
     * Sets the new creation date of this Task.
     *
     * @param creationDate the new creation date of this Task.
     */
    void setCreationDate(Date creationDate) {
        this.creationDate = creationDate; //Guarda la fecha de creación de esta tarea como la pasada.
    }

    /**
     * Gets the Value of the Field identifier from this Task and returns it if exists.
     *
     * @param idField the Field identifier associated with the Value to get from this Task.
     * @return the Value of the Field identifier from this Task.
     */
    Value getValue(int idField) {
        for (Value value: values) { //Por cada valor de los valores.
            int valueId = value.getIdField(); //Guarda el identificador de campo del valor.

            if (valueId == idField) //Si los identificadores son iguales.
                return value; //Devuelve el valor.
        }

        return null; //Devuelve un valor nulo.
    }

    /**
     * Adds the Value to this Task and returns if it has been successful.
     *
     * @param value the Value to add in this Task.
     * @return true if the Value has been successfully added to this Task.
     */
    boolean addValue(Value value) {
        return values.add(value); //Agrega el valor a los valores de esta tarea y devuelve si ha tenido éxito.
    }

    /**
     * Removes the Value of the Field identifier from this Task and returns if it has been successful.
     *
     * @param idField the Field identifier associated with the Value to remove from this Task.
     * @return true if the Value has been successfully removed from this Task.
     */
    boolean removeValue(int idField) {
        boolean result = false; //Inicializa el resultado como falso.

        Iterator<Value> iterator = values.iterator(); //Guarda el iterador de los valores.

        while (iterator.hasNext()) { //Mientras el iterador tenga un siguiente valor.
            Value value = iterator.next(); //Guarda el siguiente valor del iterador.
            int valueId = value.getIdField(); //Guarda el identificador de campo del valor.

            if (valueId == idField) { //Si los identificadores son iguales.
                iterator.remove(); //Elimina el valor actual del iterador.
                result = true; //Establece el resultado como verdadero.
            }
        }

        return result; //Devuelve el resultado.
    }

    /**
     * Removes the Value from this Task and returns if it has been successful.
     *
     * @param value the Value to remove from this Task.
     * @return true if the Value has been successfully removed from this Task.
     */
    boolean removeValue(Value value) {
        return values.remove(value); //Elimina el valor de los valores de esta tarea y devuelve si ha tenido éxito.
    }

    /**
     * Compares the specified object with this Task and returns whether they are equal.
     *
     * @param o the object to compare.
     * @return true if the specified object is equal to this Task.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) { //Si esta tarea es igual que el objeto.
            return true; //Devuelve verdadero.
        }
        if (o == null || o.getClass() != getClass()) { //Si el objeto es igual a un valor nulo o su clase no es igual a la de esta tarea.
            return false; //Devuelve falso.
        }
        Task t = (Task) o; //Adapta el objeto a una tarea y la guarda.
        return id == t.id &&
                idList == t.idList &&
                title.equals(t.title) &&
                description.equals(t.description) &&
                values.equals(t.values) &&
                records.equals(t.records) &&
                creationDate.equals(t.creationDate); //Devuelve si los datos de ambas tareas son iguales.
    }

    /**
     * Calculate the hash code value for this Task and return it.
     *
     * @return the hash code value of this Task.
     */
    @Override
    public int hashCode() {
        int hash = 7; //Guarda el hash como 7.
        hash = 17 * hash + this.id; //Calcula el hash con el identificador y lo guarda.
        hash = 17 * hash + this.idList; //Calcula el hash con el identificador de lista y lo guarda.
        hash = 17 * hash + (this.title != null ? this.title.hashCode() : 0); //Calcula el hash con el título y lo guarda.
        hash = 17 * hash + (this.description != null ? this.description.hashCode() : 0); //Calcula el hash con la descripción y lo guarda.
        hash = 17 * hash + (this.values != null ? this.values.hashCode() : 0); //Calcula el hash con los valores y lo guarda.
        hash = 17 * hash + (this.records != null ? this.records.hashCode() : 0); //Calcula el hash con los historiales y lo guarda.
        hash = 17 * hash + (this.creationDate != null ? this.creationDate.hashCode() : 0); //Calcula el hash con la fecha de creación y lo guarda.
        return hash; //Devuelve el hash.
    }
}

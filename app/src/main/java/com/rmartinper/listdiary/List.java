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

/**
 * A class that stores the data of a List, including its Fields and Tasks.
 *
 * @author Raul Martin Perez (raumar95@gmail.com)
 */
class List implements Serializable {

    /**
     * The identifier of the List.
     */
    private int id; //Declara el identificador de la lista.

    /**
     * The name of the List.
     */
    private String name; //Declara el nombre de la lista.

    /**
     * The Description of the List.
     */
    private Description description; //Declara la descripción de la lista.

    /**
     * The Fields of the List.
     */
    private ArrayList<Field> fields; //Declara los campos de la lista.

    /**
     * The Tasks of the List.
     */
    private ArrayList<Task> tasks; //Declara las tareas de la lista.

    /**
     * The creation date of the List.
     */
    private Date creationDate; //Declara la fecha de creación de la lista.

    /**
     * Constructs a new List with the given data.
     *
     * @param name the name of the new List.
     * @param description the Description of the new List.
     */
    List(String name, Description description) {
        //Llama al constructor con un uno, el nombre, la descripción,
        //los una lista vacía de campos, una lista vacía de tareas y la fecha actual.
        this(1, name, description, new ArrayList<Field>(), new ArrayList<Task>(), new Date());
    }

    /**
     * Constructs a new List with the given data.
     *
     * @param id the identifier of the new List.
     * @param name the name of the new List.
     * @param description the Description of the new List.
     * @param fields the Fields of the new List.
     * @param creationDate the creation date of the new List.
     */
    List(int id, String name, Description description, ArrayList<Field> fields, Date creationDate) {
        //Llama al constructor con el identificador, el nombre, la descripción,
        //los campos, una lista vacía de tareas y la fecha de creación.
        this(id, name, description, fields, new ArrayList<Task>(), creationDate);
    }

    /**
     * Constructs a new List with the given data.
     *
     * @param id the identifier of the new List.
     * @param name the name of the new List.
     * @param description the Description of the new List.
     * @param fields the Fields of the new List.
     * @param tasks the Tasks of the new List.
     * @param creationDate the creation date of the new List.
     */
    List(int id, String name, Description description, ArrayList<Field> fields, ArrayList<Task> tasks, Date creationDate) {
        this.id = id; //Guarda el identificador de la nueva lista como el pasado.
        this.name = name; //Guarda el nombre de la nueva lista como el pasado.
        this.description = description; //Guarda la descripción de la nueva lista como la pasada.
        this.fields = fields; //Guarda los campos de la nueva lista como los pasados.
        this.tasks = tasks; //Guarda las tareas de la nueva lista como las pasadas.
        this.creationDate = creationDate; //Guarda la fecha de creación de la nueva lista como la pasada.
    }

    /**
     * Gets the identifier of this List and returns it.
     *
     * @return the identifier of this List.
     */
    int getId() {
        return id; //Devuelve el identificador de esta lista.
    }

    /**
     * Sets the new identifier of this List.
     *
     * @param id the new identifier of this List.
     */
    void setId(int id) {
        this.id = id; //Guarda el identificador de esta lista como el pasado.
    }

    /**
     * Gets the name of this List and returns it.
     *
     * @return the name of this List.
     */
    String getName() {
        return name; //Devuelve el nombre de esta lista.
    }

    /**
     * Sets the new name of this List.
     *
     * @param name the new name of this List.
     */
    void setName(String name) {
        this.name = name; //Guarda el nombre de esta lista como el pasado.
    }

    /**
     * Gets the Description of this List and returns it.
     *
     * @return the Description of this List.
     */
    Description getDescription() {
        return description; //Devuelve la descripción de esta lista.
    }

    /**
     * Sets the new Description of this List.
     *
     * @param description the new Description of this List.
     */
    void setDescription(Description description) {
        this.description = description; //Guarda la descripción de esta lista como la pasada.
    }

    /**
     * Gets the Fields of this List and returns it.
     *
     * @return the Fields of this List.
     */
    ArrayList<Field> getFields() {
        return fields; //Devuelve los campos de esta lista.
    }

    /**
     * Sets the new Fields of this List.
     *
     * @param fields the new Fields of this List.
     */
    void setFields(ArrayList<Field> fields) {
        this.fields = fields; //Guarda los campos de esta lista como los pasados.
    }

    /**
     * Gets the Tasks of this List and returns it.
     *
     * @return the Tasks of this List.
     */
    ArrayList<Task> getTasks() {
        return tasks; //Devuelve las tareas de esta lista.
    }

    /**
     * Sets the new Tasks of this List.
     *
     * @param tasks the new Tasks of this List.
     */
    void setTasks(ArrayList<Task> tasks) {
        this.tasks = tasks; //Guarda las tareas de esta lista como las pasadas.
    }

    /**
     * Gets the creation date of this List and returns it.
     *
     * @return the creation date of this List.
     */
    Date getCreationDate() {
        return creationDate; //Devuelve la fecha de creación de esta lista.
    }

    /**
     * Sets the new creation date of this List.
     *
     * @param creationDate the new creation date of this List.
     */
    void setCreationDate(Date creationDate) {
        this.creationDate = creationDate; //Guarda el fecha de creación de esta lista como la pasada.
    }

    /**
     * Gets the Field of the index from this List and returns it.
     *
     * @param index the index of the Field to get from this List.
     * @return the Field of the index from this List.
     */
    Field getField(int index) {
        return fields.get(index); //Devuelve el campo obtenido en el índice en los campos de esta lista.
    }

    /**
     * Adds the Field to this List and returns if it has been successful.
     *
     * @param field the Field to add in this List.
     * @return true if the Field has been successfully added to this List.
     */
    boolean addField(Field field) {
        return fields.add(field); //Agrega el campo a los campos de esta lista y devuelve si ha tenido éxito.
    }

    /**
     * Removes the Field from this List and returns if it has been successful.
     *
     * @param field the Field to remove from this List.
     * @return true if the Field has been successfully removed from this List.
     */
    boolean removeField(Field field) {
        return fields.remove(field); //Elimina el campo de los campos de esta lista y devuelve si ha tenido éxito.
    }

    /**
     * Gets the Task of the index from this List and returns it.
     *
     * @param index the index of the Task to get from this List.
     * @return the Task of the index from this List.
     */
    Task getTask(int index) {
        return tasks.get(index); //Devuelve la tarea obtenido en el índice en las tareas de esta lista.
    }

    /**
     * Adds the Task to this List and returns if it has been successful.
     *
     * @param task the Task to add in this List.
     * @return true if the Task has been successfully added to this List.
     */
    boolean addTask(Task task) {
        return tasks.add(task); //Agrega la tarea a las tareas de esta lista y devuelve si ha tenido éxito.
    }

    /**
     * Removes the Task from this List and returns if it has been successful.
     *
     * @param task the Task to remove from this List.
     * @return true if the Task has been successfully removed from this List.
     */
    boolean removeTask(Task task) {
        return tasks.remove(task); //Elimina la tarea de las tareas de esta lista y devuelve si ha tenido éxito.
    }

    /**
     * Compares the specified object with this List and returns whether they are equal.
     *
     * @param o the object to compare.
     * @return true if the specified object is equal to this List.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) { //Si esta lista es igual que el objeto.
            return true; //Devuelve verdadero.
        }
        if (o == null || o.getClass() != getClass()) { //Si el objeto es igual a un valor nulo o su clase no es igual a la de esta lista.
            return false; //Devuelve falso.
        }
        List l = (List) o; //Adapta el objeto a una lista y la guarda.
        return id == l.id &&
                name.equals(l.name) &&
                description.equals(l.description) &&
                fields.equals(l.fields) &&
                tasks.equals(l.tasks) &&
                creationDate.equals(l.creationDate); //Devuelve si los datos de ambas listas son iguales.
    }

    /**
     * Calculate the hash code value for this List and return it.
     *
     * @return the hash code value of this List.
     */
    @Override
    public int hashCode() {
        int hash = 7; //Guarda el hash como 7.
        hash = 17 * hash + this.id; //Calcula el hash con el identificador y lo guarda.
        hash = 17 * hash + (this.name != null ? this.name.hashCode() : 0); //Calcula el hash con el nombre y lo guarda.
        hash = 17 * hash + (this.description != null ? this.description.hashCode() : 0); //Calcula el hash con la descripción y lo guarda.
        hash = 17 * hash + (this.fields != null ? this.fields.hashCode() : 0); //Calcula el hash con los campos y lo guarda.
        hash = 17 * hash + (this.tasks != null ? this.tasks.hashCode() : 0); //Calcula el hash con las tareas y lo guarda.
        hash = 17 * hash + (this.creationDate != null ? this.creationDate.hashCode() : 0); //Calcula el hash con la fecha de creación y lo guarda.
        return hash; //Devuelve el hash.
    }
}

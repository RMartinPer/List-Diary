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
import java.util.Date;

/**
 * A class that stores the data of a Value.
 *
 * @author Raul Martin Perez (raumar95@gmail.com)
 */
class Value implements Serializable {

    /**
     * The Field identifier of the Value.
     */
    private int idField; //Declara el identificador de campo del valor.

    /**
     * The content of the Value.
     */
    private String content; //Declara el contenido del valor.

    /**
     * The creation date of the Value.
     */
    private Date creationDate; //Declara la fecha de creación del valor.

    /**
     * Constructs a new Value with the given data.
     *
     * @param idField the Field identifier of the new Value.
     */
    Value(int idField) {
        this(idField, "", new Date()); //Llama al constructor con el identificador de campo, una cadena vacía y la fecha actual.
    }

    /**
     * Constructs a new Value with the given data.
     *
     * @param idField the Field identifier of the new Value.
     * @param creationDate the creation date of the new Value.
     */
    Value(int idField, Date creationDate) {
        this(idField, "", creationDate); //Llama al constructor con el identificador de campo, una cadena vacía y la fecha de creación.
    }

    /**
     * Constructs a new Value with the given data.
     *
     * @param idField the Field identifier of the new Value.
     * @param content the content of the new Value.
     * @param creationDate the creation date of the new Value.
     */
    Value(int idField, String content, Date creationDate) {
        this.idField = idField; //Guarda el identificador de campo del nuevo valor como el pasado.
        this.content = content; //Guarda el contenido del nuevo valor como el pasado.
        this.creationDate = creationDate; //Guarda la fecha de creación de la nuevo valor como la pasada.
    }

    /**
     * Gets the Field identifier of this Value and returns it.
     *
     * @return the Field identifier of this Value.
     */
    int getIdField() {
        return idField; //Devuelve el identificador de campo de este valor.
    }

    /**
     * Sets the new Field identifier of this Value.
     *
     * @param idField the new Field identifier of this Value.
     */
    void setIdField(int idField) {
        this.idField = idField; //Guarda el identificador de campo de este valor como el pasado.
    }

    /**
     * Gets the content of this Value and returns it.
     *
     * @return the content of this Value.
     */
    String getContent() {
        return content; //Devuelve el contenido de este valor.
    }

    /**
     * Sets the new content of this Value.
     *
     * @param content the new content of this Value.
     */
    void setContent(String content) {
        this.content = content; //Guarda el contenido de este valor como el pasado.
    }

    /**
     * Gets the creation date of this Value and returns it.
     *
     * @return the creation date of this Value.
     */
    Date getCreationDate() {
        return creationDate; //Devuelve la fecha de creación de este valor.
    }

    /**
     * Sets the new creation date of this Value.
     *
     * @param creationDate the new creation date of this Value.
     */
    void setCreationDate(Date creationDate) {
        this.creationDate = creationDate; //Guarda la fecha de creación de este valor como la pasada.
    }

    /**
     * Compares the specified object with this Value and returns whether they are equal.
     *
     * @param o the object to compare.
     * @return true if the specified object is equal to this Value.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) { //Si este valor es igual que el objeto.
            return true; //Devuelve verdadero.
        }
        if (o == null || o.getClass() != getClass()) { //Si el objeto es igual a un valor nulo o su clase no es igual a la de este valor.
            return false; //Devuelve falso.
        }
        Value v = (Value) o; //Adapta el objeto a un valor y lo guarda.
        return idField == v.idField &&
                content.equals(v.content) &&
                creationDate.equals(v.creationDate); //Devuelve si los datos de ambos valores son iguales.
    }

    /**
     * Calculate the hash code value for this Value and return it.
     *
     * @return the hash code value of this Value.
     */
    @Override
    public int hashCode() {
        int hash = 7; //Guarda el hash como 7.
        hash = 17 * hash + this.idField; //Calcula el hash con el identificador de campo y lo guarda.
        hash = 17 * hash + (this.content != null ? this.content.hashCode() : 0); //Calcula el hash con el contenido y lo guarda.
        hash = 17 * hash + (this.creationDate != null ? this.creationDate.hashCode() : 0); //Calcula el hash con la fecha de creación y lo guarda.
        return hash; //Devuelve el hash.
    }
}

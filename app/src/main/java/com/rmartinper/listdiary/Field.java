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
 * A class that stores the data of a Field.
 *
 * @author Raul Martin Perez (raumar95@gmail.com)
 */
class Field implements Serializable {

    /**
     * The identifier of the Field.
     */
    private int id; //Declara el identificador del campo.

    /**
     * The name of the Field.
     */
    private String name; //Declara el nombre del campo.

    /**
     * The type of the Field.
     */
    private String type; //Declara el tipo del campo.

    /**
     * The creation date of the Field.
     */
    private Date creationDate; //Declara la fecha de creación del campo.

    /**
     * Constructs a new Field with the given data.
     *
     * @param name the name of the new Field.
     */
    Field(String name) {
        this(1, name, "String", new Date()); //Llama al constructor con un uno, el nombre, la cadena String y la fecha actual.
    }

    /**
     * Constructs a new Field with the given data.
     *
     * @param name the name of the new Field.
     * @param type the type of the new Field.
     */
    Field(String name, String type) {
        this(1, name, type, new Date()); //Llama al constructor con un uno, el nombre, el tipo y la fecha actual.
    }

    /**
     * Constructs a new Field with the given data.
     *
     * @param id the identifier of the new Field.
     * @param name the name of the new Field.
     * @param type the type of the new Field.
     */
    Field(int id, String name, String type) {
        this(id, name, type, new Date()); //Llama al constructor con el identificador, el nombre, el tipo y la fecha actual.
    }

    /**
     * Constructs a new Field with the given data.
     *
     * @param id the identifier of the new Field.
     * @param name the name of the new Field.
     * @param type the type of the new Field.
     * @param creationDate the creation date of the new Field.
     */
    Field(int id, String name, String type, Date creationDate) {
        this.id = id; //Guarda el identificador del nuevo campo como el pasado.
        this.name = name; //Guarda el nombre del nuevo campo como el pasado.
        this.type = type; //Guarda el tipo de la nuevo campo como el pasado.
        this.creationDate = creationDate; //Guarda la fecha de creación del nuevo campo como la pasada.
    }

    /**
     * Gets the identifier of this Field and returns it.
     *
     * @return the identifier of this Field.
     */
    int getId() {
        return id; //Devuelve el identificador de este campo.
    }

    /**
     * Sets the new identifier of this Field.
     *
     * @param id the new identifier of this Field.
     */
    void setId(int id) {
        this.id = id; //Guarda el identificador de este campo como el pasado.
    }

    /**
     * Gets the name of this Field and returns it.
     *
     * @return the name of this Field.
     */
    String getName() {
        return name; //Devuelve el nombre de este campo.
    }

    /**
     * Sets the new name of this Field.
     *
     * @param name the new name of this Field.
     */
    void setName(String name) {
        this.name = name; //Guarda el nombre de este campo como el pasado.
    }

    /**
     * Gets the type of this Field and returns it.
     *
     * @return the type of this Field.
     */
    String getType() {
        return type; //Devuelve el tipo de este campo.
    }

    /**
     * Sets the new type of this Field.
     *
     * @param type the new type of this Field.
     */
    void setType(String type) {
        this.type = type; //Guarda el tipo de este campo como el pasado.
    }

    /**
     * Gets the creation date of this Field and returns it.
     *
     * @return the creation date of this Field.
     */
    Date getCreationDate() {
        return creationDate; //Devuelve la fecha de creación de este campo.
    }

    /**
     * Sets the new creation date of this Field.
     *
     * @param creationDate the new creation date of this Field.
     */
    void setCreationDate(Date creationDate) {
        this.creationDate = creationDate; //Guarda el fecha de creación de este campo como la pasada.
    }

    /**
     * Compares the specified object with this Field and returns whether they are equal.
     *
     * @param o the object to compare.
     * @return true if the specified object is equal to this Field.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) { //Si este campo es igual que el objeto.
            return true; //Devuelve verdadero.
        }
        if (o == null || o.getClass() != getClass()) { //Si el objeto es igual a un valor nulo o su clase no es igual a la de este campo.
            return false; //Devuelve falso.
        }
        Field f = (Field) o; //Adapta el objeto a un campo y lo guarda.
        return id == f.id &&
                name.equals(f.name) &&
                type.equals(f.type) &&
                creationDate.equals(f.creationDate); //Devuelve si los datos de ambos campos son iguales.
    }

    /**
     * Calculate the hash code value for this Field and return it.
     *
     * @return the hash code value of this Field.
     */
    @Override
    public int hashCode() {
        int hash = 7; //Guarda el hash como 7.
        hash = 17 * hash + this.id; //Calcula el hash con el identificador y lo guarda.
        hash = 17 * hash + (this.name != null ? this.name.hashCode() : 0); //Calcula el hash con el nombre y lo guarda.
        hash = 17 * hash + (this.type != null ? this.type.hashCode() : 0); //Calcula el hash con el tipo y lo guarda.
        hash = 17 * hash + (this.creationDate != null ? this.creationDate.hashCode() : 0); //Calcula el hash con la fecha de creación y lo guarda.
        return hash; //Devuelve el hash.
    }
}

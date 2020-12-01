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
 * A class that stores the data of a Description.
 *
 * @author Raul Martin Perez (raumar95@gmail.com)
 */
class Description implements Serializable {

    /**
     * The content of the Description.
     */
    private String content; //Declara el contenido de la descripción.

    /**
     * The creation date of the Description.
     */
    private Date creationDate; //Declara la fecha de creación de la descripción.

    /**
     * Constructs a new empty Description.
     */
    Description() {
        this("", new Date()); //Llama al constructor con una cadena vacía y la fecha actual.
    }

    /**
     * Constructs a new Description with the given data.
     *
     * @param content the content of the new Description.
     */
    Description(String content) {
        this(content, new Date()); //Llama al constructor con el contenido y la fecha actual.
    }

    /**
     * Constructs a new Description with the given data.
     *
     * @param content the content of the new Description.
     * @param creationDate the creation date of the new Description.
     */
    Description(String content, Date creationDate) {
        this.content = content; //Guarda el contenido de la nueva descripción como el pasado.
        this.creationDate = creationDate; //Guarda la fecha de creación de la nueva descripción como la pasada.
    }

    /**
     * Gets the content of this Description and returns it.
     *
     * @return the content of this Description.
     */
    String getContent() {
        return content; //Devuelve el contenido de esta descripción.
    }

    /**
     * Sets the new content of this Description.
     *
     * @param content the new content of this Description.
     */
    void setContent(String content) {
        this.content = content; //Guarda el contenido de esta descripción como el pasado.
    }

    /**
     * Gets the creation date of this Description and returns it.
     *
     * @return the creation date of this Description.
     */
    Date getCreationDate() {
        return creationDate; //Devuelve la fecha de creación de esta descripción.
    }

    /**
     * Sets the new creation date of this Description.
     *
     * @param creationDate the new creation date of this Description.
     */
    void setCreationDate(Date creationDate) {
        this.creationDate = creationDate; //Guarda la fecha de creación de esta descripción como la pasada.
    }

    /**
     * Compares the specified object with this Description and returns whether they are equal.
     *
     * @param o the object to compare.
     * @return true if the specified object is equal to this Description.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) { //Si esta descripción es igual que el objeto.
            return true; //Devuelve verdadero.
        }
        if (o == null || o.getClass() != getClass()) { //Si el objeto es igual a un valor nulo o su clase no es igual a la de esta descripción.
            return false; //Devuelve falso.
        }
        Description d = (Description) o; //Adapta el objeto a una descripción y la guarda.
        return content.equals(d.content) &&
                creationDate.equals(d.creationDate); //Devuelve si los datos de ambas descripciones son iguales.
    }

    /**
     * Calculate the hash code value for this Description and return it.
     *
     * @return the hash code value of this Description.
     */
    @Override
    public int hashCode() {
        int hash = 7; //Guarda el hash como 7.
        hash = 17 * hash + (this.content != null ? this.content.hashCode() : 0); //Calcula el hash con el contenido y lo guarda.
        hash = 17 * hash + (this.creationDate != null ? this.creationDate.hashCode() : 0); //Calcula el hash con la fecha de creación y lo guarda.
        return hash; //Devuelve el hash.
    }
}

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
 * A class that stores the data of a Record, including its Histories.
 *
 * @author Raul Martin Perez (raumar95@gmail.com)
 */
class Record implements Serializable {

    /**
     * The Histories of the Record.
     */
    private ArrayList<History> histories; //Declara las historias del historial.

    /**
     * The creation date of the Record.
     */
    private Date creationDate; //Declara la fecha de creación del historial.

    /**
     * Constructs a new Record with the given data.
     *
     * @param histories the Histories of the new Record.
     */
    Record(ArrayList<History> histories) {
        this(histories, new Date()); //Llama al constructor con las historias y la fecha actual.
    }

    /**
     * Constructs a new Record with the given data.
     *
     * @param histories the Histories of the new Record.
     * @param creationDate the creation date of the new Record.
     */
    Record(ArrayList<History> histories, Date creationDate) {
        this.histories = histories; //Guarda las historias del nuevo historial como las pasadas.
        this.creationDate = creationDate; //Guarda la fecha de creación del nuevo historial como la pasada.
    }

    /**
     * Gets the Histories of this Record and returns it.
     *
     * @return the Histories of this Record.
     */
    ArrayList<History> getHistories() {
        return histories; //Devuelve las historias de este historial.
    }

    /**
     * Gets the creation date of this Record and returns it.
     *
     * @return the creation date of this Record.
     */
    Date getCreationDate() {
        return creationDate; //Devuelve la fecha de creación de este historial.
    }

    /**
     * Compares the specified object with this Record and returns whether they are equal.
     *
     * @param o the object to compare.
     * @return true if the specified object is equal to this Record.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) { //Si este historial es igual que el objeto.
            return true; //Devuelve verdadero.
        }
        if (o == null || o.getClass() != getClass()) { //Si el objeto es igual a un valor nulo o su clase no es igual a la de este historial.
            return false; //Devuelve falso.
        }
        Record r = (Record) o; //Adapta el objeto a un historial y lo guarda.
        return histories.equals(r.histories) &&
                creationDate.equals(r.creationDate); //Devuelve si los datos de ambos historiales son iguales.
    }

    /**
     * Calculate the hash code value for this Record and return it.
     *
     * @return the hash code value of this Record.
     */
    @Override
    public int hashCode() {
        int hash = 7; //Guarda el hash como 7.
        hash = 17 * hash + (this.histories != null ? this.histories.hashCode() : 0); //Calcula el hash con las historias y lo guarda.
        hash = 17 * hash + (this.creationDate != null ? this.creationDate.hashCode() : 0); //Calcula el hash con la fecha de creación y lo guarda.
        return hash; //Devuelve el hash.
    }
}

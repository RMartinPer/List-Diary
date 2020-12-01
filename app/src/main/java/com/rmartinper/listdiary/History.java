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

/**
 * A class that stores the data of a History.
 *
 * @author Raul Martin Perez (raumar95@gmail.com)
 */
class History implements Serializable {

    /**
     * An enumeration of the Types of a History.
     */
    enum Type {
        TITLE(1), //Declara el tipo "Título" de código 1.
        DESCRIPTION(2), //Declara el tipo "Descripción" de código 2.
        STARTDATE(3), //Declara el tipo "Fecha de Inicio" de código 3.
        FIELD(4); //Declara el tipo "Campo" de código 4.

        /**
         * The code of the Type.
         */
        private final int typeCode; //Declara el código del tipo.

        /**
         * Constructs a new Type with the code.
         *
         * @param typeCode the code of the new Type.
         */
        Type(int typeCode) {
            this.typeCode = typeCode; //Guarda el código del nuevo tipo como el pasado.
        }

        /**
         * Gets the code of this Type and returns it.
         *
         * @return the code of this Type.
         */
        int getTypeCode() {
            return typeCode; //Devuelve el código de este tipo.
        }
    }

    /**
     * An enumeration of the Actions of a History.
     */
    enum Action {
        CREATE(1), //Declara la acción "Crear" de código 1.
        DELETE(2), //Declara la acción "Eliminar" de código 2.
        UPDATE(3); //Declara la acción "Actualizar" de código 3.

        /**
         * The code of the Action.
         */
        private final int actionCode; //Declara el código de la acción.

        /**
         * Constructs a new Action with the code.
         *
         * @param actionCode the code of the new Action.
         */
        Action(int actionCode) {
            this.actionCode = actionCode; //Guarda el código de la nueva acción como el pasado.
        }

        /**
         * Gets the code of this Action and returns it.
         *
         * @return the code of this Action.
         */
        int getActionCode() {
            return actionCode; //Devuelve el código de esta acción.
        }
    }

    /**
     * The field of the History.
     */
    private String field; //Declara el campo de la historia.

    /**
     * The value of the History.
     */
    private String value; //Declara el valor de la historia.

    /**
     * The Type of the History.
     */
    private Type type; //Declara el tipo de la historia.

    /**
     * The Action of the History.
     */
    private Action action; //Declara la acción de la historia.

    /**
     * Constructs a new History with the given data.
     *
     * @param field the field of the new History.
     * @param value the value of the new History.
     */
    History(String field, String value) {
        this(field, value, Type.FIELD, Action.UPDATE); //Llama al constructor con el campo, el valor, el tipo Campo y la acción Actualizar.
    }

    /**
     * Constructs a new History with the given data.
     *
     * @param field the field of the new History.
     * @param value the value of the new History.
     * @param type the Type of the new History.
     */
    History(String field, String value, Type type) {
        this(field, value, type, Action.UPDATE); //Llama al constructor con el campo, el valor, el tipo y la acción Actualizar.
    }

    /**
     * Constructs a new History with the given data.
     *
     * @param field the field of the new History.
     * @param value the value of the new History.
     * @param action the Action of the new History.
     */
    History(String field, String value, Action action) {
        this(field, value, Type.FIELD, action); //Llama al constructor con el campo, el valor, el tipo Campo y la acción.
    }

    /**
     * Constructs a new History with the given data.
     *
     * @param field the field of the new History.
     * @param value the value of the new History.
     * @param type the Type of the new History.
     * @param action the Action of the new History.
     */
    History(String field, String value, Type type, Action action) {
        this.field = field; //Guarda el campo de la nueva historia como el pasado.
        this.value = value; //Guarda el valor de la nueva historia como el pasado.
        this.type = type; //Guarda el tipo de la nueva historia como el pasado.
        this.action = action; //Guarda la acción de la nueva historia como la pasada.
    }

    /**
     * Gets the field of this History and returns it.
     *
     * @return the field of this History.
     */
    String getField() {
        return field; //Devuelve el campo de esta historia.
    }

    /**
     * Gets the value of this History and returns it.
     *
     * @return the value of this History.
     */
    String getValue() {
        return value; //Devuelve el valor de esta historia.
    }

    /**
     * Gets the Type of this History and returns it.
     *
     * @return the Type of this History.
     */
    Type getType() {
        return type; //Devuelve el tipo de esta historia.
    }

    /**
     * Gets the Action of this History and returns it.
     *
     * @return the Action of this History.
     */
    Action getAction() {
        return action; //Devuelve la acción de esta historia.
    }

    /**
     * Compares the specified object with this History and returns whether they are equal.
     *
     * @param o the object to compare.
     * @return true if the specified object is equal to this History.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) { //Si esta historia es igual que el objeto.
            return true; //Devuelve verdadero.
        }
        if (o == null || o.getClass() != getClass()) { //Si el objeto es igual a un valor nulo o su clase no es igual a la de esta historia.
            return false; //Devuelve falso.
        }
        History h = (History) o; //Adapta el objeto a una historia y la guarda.
        return field.equals(h.field) &&
                value.equals(h.value) &&
                type.equals(h.type) &&
                action.equals(h.action); //Devuelve si los datos de ambas listas son iguales.
    }

    /**
     * Calculate the hash code value for this History and return it.
     *
     * @return the hash code value of this History.
     */
    @Override
    public int hashCode() {
        int hash = 7; //Guarda el hash como 7.
        hash = 17 * hash + (this.field != null ? this.field.hashCode() : 0); //Calcula el hash con el campo y lo guarda.
        hash = 17 * hash + (this.value != null ? this.value.hashCode() : 0); //Calcula el hash con el valor y lo guarda.
        hash = 17 * hash + (this.type != null ? this.type.getTypeCode() : 0); //Calcula el hash con el tipo y lo guarda.
        hash = 17 * hash + (this.action != null ? this.action.getActionCode() : 0); //Calcula el hash con la acción y lo guarda.
        return hash; //Devuelve el hash.
    }
}

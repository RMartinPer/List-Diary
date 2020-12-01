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

import android.util.Base64;

import java.nio.charset.Charset;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * A class that uses an AES-256-GCM algorithm to encrypt and decrypt texts.
 *
 * @author Raul Martin Perez (raumar95@gmail.com)
 */
class AES256GCM {

    /**
     * The Logger for the AES-256-GCM algorithm.
     */
    private static final Logger LOGGER = Logger.getLogger(AES256GCM.class.getName()); //Obtiene el registrador para el algoritmo AES-256-GCM y lo guarda.

    /**
     * The Charset in the AES encryption and decryption.
     */
    private static final Charset CHARSET = Charset.forName("UTF-8"); //Guarda el set de caracteres en el cifrado y descifrado AES.

    /**
     * Constructs a new AES-256-GCM algorithm.
     */
    private AES256GCM() {
        super(); //Llama al constructor superior.
    }

    /**
     * Encrypts the text String with the key and initialization vector Strings.
     *
     * @param keyStr the key String with which to encrypt.
     * @param ivStr the initialization vector String with which to encrypt.
     * @param textStr the text String to encrypt.
     * @return the bytes resulting from encrypting the text with the key and initialization vector.
     */
    private static byte[] encrypt(String keyStr, String ivStr, String textStr) {
        return encrypt(keyStr.getBytes(CHARSET), ivStr.getBytes(CHARSET), textStr.getBytes(CHARSET)); //Devuelve el resultado del encriptado de los bytes de la clave, del vector de inicialización y del texto.
    }

    /**
     * Encrypts the text bytes with the key and initialization vector bytes.
     *
     * @param keyBytes the key bytes with which to encrypt.
     * @param ivBytes the initialization vector bytes with which to encrypt.
     * @param textBytes the text bytes to encrypt.
     * @return the bytes resulting from encrypting the text with the key and initialization vector.
     */
    private static byte[] encrypt(byte[] keyBytes, byte[] ivBytes, byte[] textBytes) {
        try { //Trata de hacer el contenido del cuerpo.
            Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding"); //Obtiene una instancia del cifrador AES y la guarda.
            cipher.init(Cipher.ENCRYPT_MODE,
                    new SecretKeySpec(keyBytes, "AES"),
                    new IvParameterSpec(ivBytes)); //Inicializa el cifrador en modo encriptación con la clave y el vector de inicialización.
            return cipher.doFinal(textBytes); //Encripta los bytes del texto y devuelve los bytes resultantes.
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidAlgorithmParameterException | InvalidKeyException | BadPaddingException | IllegalBlockSizeException e) { //Si se produce alguna excepción.
            String eMsg = String.format("Exception: %s", e.getMessage()); //Guarda el mensaje de la excepción.
            LOGGER.log(Level.INFO, eMsg); //Registra al nivel de información el mensaje de la excepción.
        }
        return new byte[0]; //Devuelve un arreglo de bytes vacío.
    }

    /**
     * Decrypts the text String with the key and initialization vector Strings.
     *
     * @param keyStr the key String with which to decrypt.
     * @param ivStr the initialization vector String with which to decrypt.
     * @param textStr the text String to decrypt.
     * @return the bytes resulting from decrypting the text with the key and initialization vector.
     */
    private static byte[] decrypt(String keyStr, String ivStr, String textStr) {
        try { //Trata de hacer el contenido del cuerpo.
            byte[] textBytes = Base64.decode(textStr.getBytes(CHARSET), Base64.DEFAULT); //Descodifica los bytes de la cadena de texto en base 64 y guarda los bytes resultantes.
            return decrypt(keyStr.getBytes(CHARSET), ivStr.getBytes(CHARSET), textBytes); //Devuelve el resultado del desencriptado de los bytes de la clave, del vector de inicialización y del texto.
        } catch (IllegalArgumentException e) { //Si se produce una excepción de argumento ilegal.
            String eMsg = String.format("IllegalArgumentException: %s", e.getMessage()); //Guarda el mensaje de la excepción.
            LOGGER.log(Level.INFO, eMsg); //Registra al nivel de información el mensaje de la excepción.
        }
        return new byte[0]; //Devuelve un arreglo de bytes vacío.
    }

    /**
     * Decrypts the text bytes with the key and initialization vector bytes.
     *
     * @param keyBytes the key bytes with which to decrypt.
     * @param ivBytes the initialization vector bytes with which to decrypt.
     * @param textBytes the text bytes to decrypt.
     * @return the bytes resulting from decrypting the text with the key and initialization vector.
     */
    private static byte[] decrypt(byte[] keyBytes, byte[] ivBytes, byte[] textBytes) {
        try { //Trata de hacer el contenido del cuerpo.
            Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding"); //Obtiene una instancia del cifrador AES y la guarda.
            cipher.init(Cipher.DECRYPT_MODE,
                    new SecretKeySpec(keyBytes, "AES"),
                    new IvParameterSpec(ivBytes)); //Inicializa el cifrador en modo desencriptación con la clave y el vector de inicialización.
            return cipher.doFinal(textBytes); //Desencripta los bytes del texto y devuelve los bytes resultantes.
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidAlgorithmParameterException | InvalidKeyException | BadPaddingException | IllegalBlockSizeException e) { //Si se produce alguna excepción.
            String eMsg = String.format("Exception: %s", e.getMessage()); //Guarda el mensaje de la excepción.
            LOGGER.log(Level.INFO, eMsg); //Registra al nivel de información el mensaje de la excepción.
        }
        return new byte[0]; //Devuelve un arreglo de bytes vacío.
    }

    /**
     * Encrypts the text String with the key and initialization vector Strings.
     *
     * @param key the key String with which to encrypt.
     * @param iv the initialization vector String with which to encrypt.
     * @param text the text String to encrypt.
     * @return the String resulting from encrypting the text with the key and initialization vector.
     */
    protected static String encryptToStrBase64(String key, String iv, String text) {
        byte[] bytes = encrypt(key, iv, text); //Encripta el texto con la clave y el vector de inicialización y guarda los bytes resultantes.
        return new String(Base64.encode(bytes, Base64.DEFAULT), CHARSET); //Devuelve la cadena de los bytes codificados en base 64.
    }

    /**
     * Decrypts the text String with the key and initialization vector Strings.
     *
     * @param key the key String with which to decrypt.
     * @param iv the initialization vector String with which to decrypt.
     * @param text the text String to decrypt.
     * @return the String resulting from decrypting the text with the key and initialization vector.
     */
    protected static String decryptFromStrBase64(String key, String iv, String text) {
        byte[] bytes = decrypt(key, iv, text); //Desencripta el texto con la clave y el vector de inicialización y guarda los bytes resultantes.
        return new String(bytes, CHARSET); //Devuelve la cadena de los bytes.
    }
}

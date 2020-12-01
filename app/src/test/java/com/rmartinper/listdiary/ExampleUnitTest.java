package com.rmartinper.listdiary;

import org.junit.Test;

import java.text.Normalizer;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void stripDiacritics_isCorrect() {
        String diacritical = "Tĥïŝ ĩš â fůňķŷ Ñ Šťŕĭńġ"; //Guarda la cadena diacrítica.
        String original = "This is a funky N String"; //Guarda la cadena original.

        String stripped = Normalizer.normalize(diacritical, Normalizer.Form.NFD)
                .replaceAll("\\p{InCombiningDiacriticalMarks}+", ""); //Elimina todos las marcas diacríticas del diacrítico y lo guarda.

        assertEquals(original, stripped); //Afirma que la cadena original es igual a la desnudada.
    }
}
package com.pmk.demotransferfile;

import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class GenerateFileTest {

    @Test
    void shouldCreateFileTest() throws IOException {
        //Given
        GenerateFile generateFile = new GenerateFile(123_000_000, "test.txt");
        //When
        String result = generateFile.createFile();
        //Then
        assertEquals("generate file: " + generateFile.getName() + " with size: " + generateFile.getSize() + " bytes", result);
        assertEquals(123_000_000, generateFile.getSize());
        assertEquals("test.txt", generateFile.getName());
    }

}
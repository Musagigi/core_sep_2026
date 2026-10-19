package org.example.hw.exceptions;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class CheckedExceptions {

    public void throwsFileNotFoundTest() throws FileNotFoundException {
        new FileInputStream("test");
    }

}

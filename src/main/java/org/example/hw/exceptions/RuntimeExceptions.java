package org.example.hw.exceptions;

import java.util.ArrayList;
import java.util.List;

public class RuntimeExceptions {

    public void throwsNullPointerException() {
        String s = null;
        s.length();
    }

    public void throwsArrayIndexOutOfBoundsException() {
        int[] array = new int[1];
        int q = array[2];
    }

    public void throwsNumberFormatException() {
        Integer.parseInt("qwe");
    }

    public void throwsIllegalArgumentException() {
        throw new IllegalArgumentException("illegal argument");
    }

    public static void throwsClassCastException() {
        //
    }

}

package org.example.hw.exceptions;

import java.util.ArrayList;
import java.util.List;

public class Errors {

    public void throwsOOMException() {
        List<int[]> list = new ArrayList<>();
        while (true) {
            list.add(new int[1_000_000]);
        }
    }

    public void throwsStackOverFlowError() {
        throwsStackOverFlowError();
    }
}

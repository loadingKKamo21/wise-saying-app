package org.example;

import java.io.IOException;
import org.example.error.GlobalExceptionHandler;

public class Main {
    public static void main(String[] args) throws IOException {
        GlobalExceptionHandler.initialize();
        App.run();
    }
}
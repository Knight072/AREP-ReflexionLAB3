package edu.escuelaing.arep;

import java.io.IOException;

public class App {
    public static void main(String[] args) {
        // Set the static file location
        HttpServer.getInstance().setStaticFileLocation("target/classes/public");
        loadSpring();
        try {
            HttpServer.getInstance().start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void loadSpring() {
        PathServices.getInstance();
    }
}

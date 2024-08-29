package edu.escuelaing.arep;

import java.util.ArrayList;

@RestController
public class HelloService {
    @GetMapping("/Hello")
    public static String hello() {
        return "Hello World";
    }

    @GetMapping("/GetPi")
    public static Double getPi(){
        return Math.PI;
    }

    @GetMapping("/Estudiantes")
    public static ArrayList<String> getStudent(){
        ArrayList<String> estudiantes = new ArrayList<>();
        estudiantes.add("Daniel");
        estudiantes.add("Juan");
        return estudiantes;
    }
}

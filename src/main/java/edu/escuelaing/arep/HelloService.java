package edu.escuelaing.arep;

@RestController
public class HelloService {
    @GetMapping("/Hello")
    public static String hello() {
        return "Hello World";
    }
}

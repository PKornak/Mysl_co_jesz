package com.kornak;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Klasa obslugująca okienko popUp
 */
public class popUpController {
    popUpController(){    }

    /** Obsługa wczytywania pliku tekstowego z poradami i losowe wybieranie jednej z nich. */
    public String getTip() {
        String fileName = "src/lines.txt";
        List<String> list = new ArrayList<>();

        try (Stream<String> stream = Files.lines(Paths.get(fileName))) {
            list = stream.collect(Collectors.toList());


        } catch (IOException e) {
            e.printStackTrace();
        }

        return list.get((int) (Math.random()*list.size()));
    }

}

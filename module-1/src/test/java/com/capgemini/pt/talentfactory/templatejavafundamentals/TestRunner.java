package com.capgemini.pt.talentfactory.templatejavafundamentals;

import static org.junit.jupiter.api.Assertions.assertEquals;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import org.junit.jupiter.api.Test;

public class TestRunner {
    @Test
    public void testCreateOneRestaurant() {
        InputStream stdin = System.in;
        System.setIn(new ByteArrayInputStream("1\nTaska das Bifanas\nRua da Estação\n8989-666\nN\n5\n".getBytes()));
        Runner runner = new Runner(new RestauranteService());
        try {
            runner.run();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        assertEquals(1, runner.getTotalRestaurantes());
        System.setIn(stdin);
    }
}
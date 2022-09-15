package com.mike.springforgraphql;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.function.Function;

class Junk {
    @Test
    void lambdas() {
        // Function takes string input, returns Integer - always returns 2
        Function<String, Integer> stringIntegerFunction = str -> 2;

        Assertions.assertEquals(stringIntegerFunction.apply(""), 2);
        Assertions.assertEquals(stringIntegerFunction.apply("x"), 2);


        // Declare interface with handle method - what happens if we add another method - does that break the functional interface???/
        interface MyHandler {
            String handle(String one, Integer two);
        }

        // Implementation of MyHandler Interface - takes a String param and Integer param, returns a String
        // which is the two parameters concatenated
        MyHandler withExplicit = (one, two) -> one + ":" + two;

        Assertions.assertEquals(withExplicit.handle("one", 2), "one:2");


        // Implementation of MyHandler Interface - takes a String param and Integer param, returns a String
        // which is the two parameters concatenated
        MyHandler withExplicit2 = (one, two) -> one + ":::::::::::::" + two;

        Assertions.assertEquals(withExplicit2.handle("one", 2), "one:::::::::::::2");

        // Assign function to withVar
        var withVar = (MyHandler) (one, two) -> one + ':' + two;
        Assertions.assertEquals(withVar.handle("one", 2), "one:2");
        MyHandler delegate = this::doHandle;
        Assertions.assertEquals(delegate.handle("one", 2), "one:2");
    }

    private String doHandle(String one, Integer two) {
        return one + ':' + two;
    }
}

package com.example.assignments;

import java.util.Random;

public class PersonFactory {

    public static Person newPerson() {
        Person person = new Person();
        person.setGrade(new Random().nextInt(25));
        return person;
    }
}

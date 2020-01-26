package com.hr.assignments;

import java.util.Random;

public class PersonFactory {

    public static Person newPerson() {
        Person person = new Person();
        person.setId(new Random().nextLong());
        person.setGrade(new Random().nextInt(20));
        return person;
    }
}

package com.hr.assignments;

public class PersonFactory {

    public static Person newPerson() {
        Person person = new Person();
        person.setId(1L);
        person.setGrade(11);
        return person;
    }
}

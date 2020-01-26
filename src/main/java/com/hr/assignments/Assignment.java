package com.hr.assignments;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Assignment implements java.io.Serializable {

    static final long serialVersionUID = 1L;

    private com.hr.assignments.Person person;
    private java.lang.String text;

    public Assignment() {
    }

    public Assignment(Person person) {
        this.person = person;
    }

    public com.hr.assignments.Person getPerson() {
        return this.person;
    }

    public void setPerson(com.hr.assignments.Person person) {
        this.person = person;
    }

    public java.lang.String getText() {
        return this.text;
    }

    public void setText(java.lang.String text) {
        this.text = text;
    }

    public Assignment(com.hr.assignments.Person person, java.lang.String text) {
        this.person = person;
        this.text = text;
    }

    @Override
    public String toString() {
        return "Assignment{" +
                "person=" + person +
                ", text='" + text + '\'' +
                '}';
    }
}

package com.hr.assignments;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Person implements java.io.Serializable {

    static final long serialVersionUID = 1L;

    private java.lang.Long id;

    private int grade;

    private java.lang.String text;

    public Person() {
    }

    public java.lang.Long getId() {
        return this.id;
    }

    public void setId(java.lang.Long id) {
        this.id = id;
    }

    public int getGrade() {
        return this.grade;
    }

    public void setGrade(int grade) {
        this.grade = grade;
    }

    public java.lang.String getText() {
        return this.text;
    }

    public void setText(java.lang.String text) {
        this.text = text;
    }

    public Person(java.lang.Long id, int grade, java.lang.String text) {
        this.id = id;
        this.grade = grade;
        this.text = text;
    }

    @Override
    public String toString() {
        return "Person{" +
                "id=" + id +
                ", grade=" + grade +
                ", text='" + text + '\'' +
                '}';
    }
}

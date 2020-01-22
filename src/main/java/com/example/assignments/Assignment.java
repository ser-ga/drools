package com.example.assignments;

public class Assignment implements java.io.Serializable {

    static final long serialVersionUID = 1L;

    private java.lang.Long id;

    private java.lang.String text;

    public Assignment() {
    }

    public java.lang.Long getId() {
        return this.id;
    }

    public void setId(java.lang.Long id) {
        this.id = id;
    }

    public java.lang.String getText() {
        return this.text;
    }

    public void setText(java.lang.String text) {
        this.text = text;
    }

    public Assignment(java.lang.Long id, java.lang.String text) {
        this.id = id;
        this.text = text;
    }

    @Override
    public String toString() {
        return "Assignment{" +
                "id=" + id +
                ", text='" + text + '\'' +
                '}';
    }
}

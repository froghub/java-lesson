package ru.domain.java3.lesson2.entity;


public class Author {
    private int id ;
    private String firstName;
    private String lastName;
    private String patronymic;



    public Author(int id, String firstName, String lastName, String patronymic) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.patronymic=patronymic;

    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public int getId() {

        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getPatronymic() {
        return patronymic;
    }

    @Override
    public String toString() {
        return "ID: "+id+". Имя: "+firstName+". Фамилия: "+lastName+ ". Отчество: "+patronymic;
    }
}

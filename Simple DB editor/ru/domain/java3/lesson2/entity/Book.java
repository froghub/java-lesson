package ru.domain.java3.lesson2.entity;

/**
 * Created by Антон Владимирович on 05.10.2016.
 */
public class Book {
    private int id;
    private String name;
    private int authorId;

    public Book(int id, String name, int authorId){
        this.id=id;
        this.name=name;
        this.authorId=authorId;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getAuthorId() {
        return authorId;
    }

    @Override
    public String toString() {
        return "ID: "+id+" Название: "+name+" Автор ID: "+authorId;
    }
}

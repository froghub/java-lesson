package ru.domain.java3.lesson2.DAO;


public class Facade {
    public static AuthorDAO getAuthorDAO() {
        return new AuthorDAO();
    }
    public static BookDAO getBookDAO(){
        return new BookDAO();
    }
}

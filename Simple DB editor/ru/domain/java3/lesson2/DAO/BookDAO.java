package ru.domain.java3.lesson2.DAO;

import ru.domain.java3.lesson2.AppLogger;
import ru.domain.java3.lesson2.entity.Book;
import ru.domain.java3.lesson2.exceptions.EntityDeleteError;
import ru.domain.java3.lesson2.exceptions.EntityInsertError;
import ru.domain.java3.lesson2.exceptions.EntityUpdateError;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;


public class BookDAO implements DAO<Book,Integer> {

    @Override
    public Collection<Book> selectAll(){
        ArrayList<Book> list= new ArrayList<>();
        try(Statement statement = DBConnector.getConnection().createStatement()) {
            ResultSet resultSet = statement.executeQuery("SELECT * FROM `book`");
            while (resultSet.next()){
                list.add(new Book(resultSet.getInt(1),resultSet.getString("name"),resultSet.getInt("author_id")));
            }

        }catch (SQLException sqlEx){
            AppLogger.writeLog("BookDAO selectAll() error",AppLogger.ERROR);
            AppLogger.writeLog(sqlEx.getMessage(),AppLogger.ERROR);
        }
        return list;
    }
    public Collection<Book> select(Book book){
        ArrayList<Book> list = new ArrayList<>();
        try(PreparedStatement ps = DBConnector.getConnection().prepareStatement(
                "SELECT * FROM `book` WHERE name=? AND author_id=?"
        )){

            ps.setObject(1,book.getName());
            ps.setObject(2,book.getAuthorId());
            ResultSet rs= ps.executeQuery();
            while (rs.next()){
                list.add(new Book(rs.getInt(1),rs.getString("name"),rs.getInt("author_id")));
            }

        }catch (SQLException sqlEx){
            AppLogger.writeLog("BookDAO select(entity) error",AppLogger.ERROR);
            AppLogger.writeLog(sqlEx.getMessage(),AppLogger.ERROR);
        }
        return list;
    }

    @Override
    public void insert(Book entity) throws EntityInsertError {
        try(PreparedStatement preparedStatement = DBConnector.getConnection().prepareStatement(
                "INSERT INTO `book`(name,author_id) VALUES (?,?)"
        )){
            preparedStatement.setObject(1,entity.getName());
            preparedStatement.setObject(2,entity.getAuthorId());
            preparedStatement.execute();

        }catch (SQLException sqlEx){
            AppLogger.writeLog("BookDAO insert(entity) error",AppLogger.ERROR);
            AppLogger.writeLog(sqlEx.getMessage(),AppLogger.ERROR);
            throw new EntityInsertError();
        }
    }

    @Override
    public void update(Book entity) throws EntityUpdateError {
        try(PreparedStatement preparedStatement = DBConnector.getConnection().prepareStatement(
                "UPDATE `book` SET   name=?,author_id=? WHERE id=? "
        )){
            preparedStatement.setObject(1,entity.getName());
            preparedStatement.setObject(2,entity.getAuthorId());
            preparedStatement.setObject(3,entity.getId());
            int rows = preparedStatement.executeUpdate();
            if (rows==0) {
                throw new EntityUpdateError();
            }

        }catch (SQLException sqlEx){
            AppLogger.writeLog("BookDAO update() error",AppLogger.INFO);

        }
    }

    @Override
    public void delete(Integer id) throws EntityDeleteError {
        try(PreparedStatement preparedStatement = DBConnector.getConnection().prepareStatement(
                "DELETE FROM `book` WHERE id=?"
        )){
            preparedStatement.setObject(1,id);
            preparedStatement.execute();
            int affectedRows = preparedStatement.getUpdateCount();
            if (affectedRows==0) throw new EntityDeleteError();

        }catch (SQLException sqlEx){
            AppLogger.writeLog("BookDAO delete1() error",AppLogger.INFO);
        }
    }
}

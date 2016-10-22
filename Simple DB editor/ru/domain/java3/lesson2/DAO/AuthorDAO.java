package ru.domain.java3.lesson2.DAO;


import ru.domain.java3.lesson2.AppLogger;
import ru.domain.java3.lesson2.entity.Author;
import ru.domain.java3.lesson2.exceptions.EntityDeleteError;
import ru.domain.java3.lesson2.exceptions.EntityInsertError;
import ru.domain.java3.lesson2.exceptions.EntityUpdateError;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;



public class AuthorDAO implements  DAO<Author,Integer> {



   public Collection<Author> selectAll(){
       ArrayList<Author> list = new ArrayList<>();
       try(Statement statement = DBConnector.getConnection().createStatement()){
           ResultSet rs = statement.executeQuery("SELECT * FROM `author`");

           while (rs.next()) {
              list.add(new Author(rs.getInt(1),rs.getString("first_name"),rs.getString("second_name"),rs.getString("patronymic")));
           }
       }catch (SQLException sqlEx){
           AppLogger.writeLog("AuthorDAO.selectAll() error",AppLogger.ERROR);
           AppLogger.writeLog(sqlEx.getMessage(),AppLogger.ERROR);
       }
        return list;
    }
    public Collection<Author> select(Author author){
        ArrayList<Author> list = new ArrayList<>();
         try(PreparedStatement ps = DBConnector.getConnection().prepareStatement(
                 "SELECT * FROM `author` WHERE first_name=?  AND second_name=? AND patronymic=?"
         )){
             ps.setObject(1,author.getFirstName());
             ps.setObject(2,author.getLastName());
             ps.setObject(3,author.getPatronymic());
            ResultSet rs= ps.executeQuery();
             while (rs.next()){
                 list.add(new Author(rs.getInt(1),rs.getString("first_name"),rs.getString("second_name"),rs.getString("patronymic")));
             }

         }catch (SQLException sqlEx){
             AppLogger.writeLog("AuthorDAO select(entity) error",AppLogger.ERROR);
             AppLogger.writeLog(sqlEx.getMessage(),AppLogger.ERROR);
         }
            return list;
    }


   public  void insert(Author author) throws EntityInsertError {

        try(PreparedStatement preparedStatement = DBConnector.getConnection().prepareStatement(
               "INSERT INTO `author`(first_name,second_name,patronymic) VALUES (?,?,?)")){

            preparedStatement.setObject(1,author.getFirstName());
            preparedStatement.setObject(2,author.getLastName());
            preparedStatement.setObject(3,author.getPatronymic());
            preparedStatement.execute();
        }
        catch (SQLException sqlEx){
            AppLogger.writeLog("AuthorDAO insert(entity) error",AppLogger.ERROR);
            AppLogger.writeLog(sqlEx.getMessage(),AppLogger.ERROR);
            throw new EntityInsertError();
        }

    }

   public void update(Author author) throws EntityUpdateError{

       try(PreparedStatement preparedStatement = DBConnector.getConnection().prepareStatement(
               "UPDATE `author` SET first_name=?, second_name=?,patronymic=? WHERE id=?"
       )){
           preparedStatement.setObject(1,author.getFirstName());
           preparedStatement.setObject(2,author.getLastName());
           preparedStatement.setObject(3,author.getPatronymic());
           preparedStatement.setObject(4,author.getId());
           int rows = preparedStatement.executeUpdate();
           if (rows==0) {
               throw new EntityUpdateError();
           }

       }
       catch(SQLException sqlEx){
           AppLogger.writeLog("AuthorDAO update(entity) error",AppLogger.INFO);

           throw new EntityUpdateError();
       }
    }

    public void delete(Integer id) throws EntityDeleteError{

        try(PreparedStatement preparedStatement = DBConnector.getConnection().prepareStatement(
                "DELETE FROM `author` WHERE id=?"
        )){
            preparedStatement.setObject(1,id);
            preparedStatement.execute();
            int affectedRows = preparedStatement.getUpdateCount();
            if (affectedRows==0) throw new EntityDeleteError();
        }
        catch(SQLException sqlEx){
            AppLogger.writeLog("AuthorDAO delete(id) error",AppLogger.INFO);

        }
    }


}

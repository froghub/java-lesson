package ru.domain.java3.lesson2.DAO;


import ru.domain.java3.lesson2.AppLogger;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class DBConnector {
    private static   Connection connection;


    private static final String URL_CONNECTION = "jdbc:mysql://localhost/lesson2?user=root&password=mysqlroot";
    private DBConnector() throws SQLException {
         connection = DriverManager.getConnection(URL_CONNECTION);

             AppLogger.writeLog("DB connection: OK",AppLogger.INFO);
    }

    public static Connection getConnection() {
        if (connection==null){
           try
           {
               new DBConnector();
           }catch (SQLException e){
               AppLogger.writeLog("DB connection failed",AppLogger.ERROR);
           }

        }
        return connection;
    }
}

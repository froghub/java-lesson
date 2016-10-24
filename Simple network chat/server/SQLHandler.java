package server;

import java.sql.*;

public class SQLHandler {
    private static Connection connection;
    private static PreparedStatement statement;


    public static void connect(String dbName) throws ClassNotFoundException, SQLException {
        Class.forName("org.sqlite.JDBC");
//        Class.forName("org.postgresql.Driver");
        connection = DriverManager.getConnection("jdbc:sqlite:" + dbName);
//        connection = DriverManager.getConnection("jdbc:postgresql://127.0.0.1:5432/chat","postgres","default");
    }

   /* public static String getNick(String login, String password) throws SQLException {
        Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery(
                "select nick from \"users_table\" where login = \"" + login
                + "\" and password = \"" + password + "\"");
        String nick = null;
        while (rs.next()) {
            nick = rs.getString(1);
        }
        return nick;
    } */
   public static String getNick(String login, String password) throws SQLException {
       Statement statement = connection.createStatement();
       ResultSet rs = statement.executeQuery(
               "SELECT nick FROM users_table WHERE login = '" + login
                       + "' AND password = '" + password + "' ");
       String nick = null;
         while (rs.next()) {
           nick = rs.getString(1);

       }

       return nick;
   }
    public static void setNick(String oldNick,String newNick) throws SQLException {
        Statement statement = connection.createStatement();
        statement.executeQuery(
                "UPDATE users_table SET nick = '"+newNick+"' where nick =  '"+oldNick+"'");

    }

    public static boolean checkAdminStatus(String login) throws SQLException {
        Statement statement = connection.createStatement();
        ResultSet rs= statement.executeQuery(
                "SELECT role FROM users_table WHERE login='"+login+"'");
        String role = null;
        while (rs.next()) {
            role = rs.getString(1);
        }
        if (role!=null&&role.equals("admin")) {
            return true;
        }
        else {
            return false;
        }
    }

}

package io.socket.com.example.mohamedabdelhady.tasks.souqcomadmin;

import android.os.StrictMode;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Database {

    Statement statement;

    public Connection ConnectDB(){

        Connection conn = null;

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        String ServerName = "jdbc:jtds:sqlserver://den1.mssql6.gear.host/souqc";
        String DBName = "souqc";
        String DBPass = "Ahmed.19";

        try {
            Class.forName("net.sourceforge.jtds.jdbc.Driver");
            conn = DriverManager.getConnection(ServerName,DBName,DBPass);
            statement = conn.createStatement();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return  conn;
    } // Connection method

    public int InsertUpdateDelete(String SqlCode){
            int i = 0;
        try {
            i = statement.executeUpdate(SqlCode);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return i;
    } // InsertUpdateDelete Method

    public ResultSet select(String SqlCode){
        ResultSet resultSet = null;
        try {
            resultSet = statement.executeQuery(SqlCode);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resultSet;
    } // Select method

}

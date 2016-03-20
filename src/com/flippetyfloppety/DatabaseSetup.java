package com.flippetyfloppety;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseSetup {
    private Connection connection = null;

    public DatabaseSetup() {
        try {
            DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
            String url = "jdbc:oracle:thin:@localhost:1522:ug";
            this.connection = DriverManager.getConnection(url, "ora_q2d9", "a32916132");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public Connection getConnection(){
        return this.connection;
    }

    public boolean closeConnection() {
        try {
            this.connection.close();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}

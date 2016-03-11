package com.flippetyfloppety;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

public class Main {

    public static void main(String[] args) {
        DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
    }

    public static Connection getConnection(String url, String userid, String password) throws SQLException {
        try {
            Connection con = DriverManager.getConnection(
                    "jdbc:oracle:thin:@dbhost.ugrad.cs.ubc.ca:1521:ug", "q2d9", "a32916132");
            con.setAutoCommit(false);
            ScriptRunner runner = new ScriptRunner(con, false);
            runner.runScript(new BufferedReader(new FileReader("all.sql")));
            closeConnection(con);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static boolean closeConnection(Connection con) {
        con.close();
    }
}

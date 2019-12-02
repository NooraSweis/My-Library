package com.example.dbproject;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DBConnection {

    final String DB_URL = "jdbc:mysql://localhost:3306/library_db";
    final String USER = "nls";
    final String PASS = "123";

    Connection con = null;
    private Statement st = null;
    private ResultSet rs = null;

//    public DBConnection() {
//        try {
//            Class.forName("com.mysql.jdbc.Driver");
//            con = DriverManager.getConnection(DB_URL, USER, PASS);
//            if (con != null)
//                st = con.createStatement();
//            System.out.println("con & st initialized");
//
//        } catch (ClassNotFoundException e) {
//            e.printStackTrace();
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//    }

    public DBConnection() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            try {
                con = DriverManager.getConnection(DB_URL, USER, PASS);
            } catch (SQLException ex) {
                // log an exception. fro example:
                System.out.println("Failed to create the database connection.");
            }
        } catch (ClassNotFoundException ex) {
            // log an exception. for example:
            System.out.println("Driver not found.");
        }
    }

    public void getData() {
        try {
            String query = "SELECT * FROM books";
            rs = st.executeQuery(query);

            System.out.println("Records from database: ");

            while (rs.next()) {
                String title = rs.getString("book_title");
                String author = rs.getString("author");
                System.out.println("Book:  " + title + "  Author:  " + author);
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}

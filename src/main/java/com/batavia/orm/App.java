package com.batavia.orm;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class App 
{
    public static void main( String[] args )
    {
        System.out.println( "Hello World!" );

        String url = "jdbc:postgresql://db.jfcjtfntdrivmxlkwgwf.supabase.co:5432/postgres?user=postgres&password=butewahmansion@21";
        try {
            Connection conn = DriverManager.getConnection(url);
            System.out.println("Connected to the PostgreSQL server successfully.");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}

package org.example;

import java.sql.ResultSet;
import java.sql.Statement;

/**
 * Hello world!
 *
 */
public class Main 
{
    public static void main( String[] args ) {
        String url = "jdbc:mysql://localhost:3306/MY_DATABASE";
        String user = "root";
        String password = "5991";

        try {

            java.sql.Connection con = java.sql.DriverManager.getConnection(url, user, password);
            Statement stmt = con.createStatement();
            System.out.println("Connected");

            //ResultSet rs = stmt.executeQuery("SELECT * FROM Students WHERE Major = 'Computer Science' ORDER BY 1 DESC");
            ResultSet rs = stmt.executeQuery("SELECT Major, COUNT(*) AS StudentCount FROM Students GROUP BY Major");
            //ResultSet rs = stmt.executeQuery("SELECT * FROM Students WHERE Major IS NULL");

            while (rs.next()) {
                //System.out.println(rs.getInt("StudentID") + " " + rs.getString("Name"));
                System.out.println(rs.getString("Major") + " " + rs.getInt("StudentCount"));
                //System.out.println(rs.getString("Name") );
            }

        } catch (Exception e) {
            System.out.println("Connection Failed! Check output console");
            e.printStackTrace();
            return;
        }
    }
}

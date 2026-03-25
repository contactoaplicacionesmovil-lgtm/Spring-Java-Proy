package org.example;

import java.sql.*;

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

        try(Connection connection = DriverManager.getConnection(url, user, password)) {
            System.out.println("Connected to database");

            insertStudent(connection);
            getStudentsByMajor(connection);
            callAverageGPA(connection);
            callGetStudentsByMajor(connection);
            simulateSQLError(connection);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void callGetStudentsByMajor(Connection connection) throws SQLException{
        System.out.println("Calling stored procedure GetStudentsByMajor");
        try(CallableStatement callableStatement = connection.prepareCall(
                "{CALL GetStudentsByMajor(?)}")
        ){
            callableStatement.setString(1,"Computer Science");

            ResultSet resultSet = callableStatement.executeQuery();
            while(resultSet.next()){
                System.out.println(resultSet.getString("Name"));
                System.out.println("GPA:"+ resultSet.getDouble("GPA"));
            }
        }

    }

    private static void getStudentsByMajor(Connection connection) throws SQLException{
        System.out.println("Get students by major");
        try(PreparedStatement ps = connection.prepareStatement(
                "SELECT * FROM Students WHERE Major = ?"
        )){
            ps.setString(1,"Computer Science");
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                System.out.println(rs.getString("Name"));
                System.out.println("GPA:"+ rs.getDouble("GPA"));
            }


        }
    }

    public static void insertStudent(Connection conn) throws SQLException {
        System.out.println("Insert data into the Students table");

        try(PreparedStatement ps = conn.prepareStatement(
                "INSERT INTO Students(Name,Major,GPA) VALUES (?, ?, ?)"
        )) {
            ps.setString(1,"Jhon");
            ps.setString(2,"Computer Science");
            ps.setDouble(3,3.8);

            int rowsAffected = ps.executeUpdate();
            System.out.println(rowsAffected);

        }

    }

    public static void callAverageGPA(Connection conn) throws SQLException {
        System.out.println("Calling stored procedure GetAverageGPA");

        try(CallableStatement callableStatement = conn.prepareCall(
                "{CALL GETAVERAGEGPA(?)}"
        )){
            callableStatement.registerOutParameter(1,Types.DOUBLE);
            callableStatement.execute();

            double result = callableStatement.getDouble(1);
            System.out.println("Average GPA: "+ result);

        }
    }

    public static void simulateSQLError(Connection conn) {
        System.out.println("------------------------");
        System.out.println("Simulating SQL Error");

        try (PreparedStatement preparedStatement = conn.prepareStatement(
                "SELECT * FROM NonExistentTable")) {
            preparedStatement.executeQuery();
        } catch (SQLException e) {
            System.err.println("SQL Error: " + e.getMessage());
            e.printStackTrace();
        }
    }


}

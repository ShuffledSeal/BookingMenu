package groupproject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import java.util.LinkedHashMap;

public class DatabaseManager {

    private String url;
    private String host;
    private String port;
    private String database;
    private String user;
    private String pass;
    private Connection conn;

    public DatabaseManager() {
        config();
    }

    private void config() {
        Properties prop = new Properties();
        try {
            File file = new File("src\\main\\resources\\database.config");
            if (!file.exists()) {
                System.out.println("database config file not found, creating...");
                file.createNewFile();
                FileWriter fw = new FileWriter(file);
                fw.write(
                        "database.port = PORT" +
                                System.lineSeparator() +
                                "database.host = localhost" +
                                System.lineSeparator() +
                                "database.database = DATABASE" +
                                System.lineSeparator() +
                                "database.user = USER" +
                                System.lineSeparator() +
                                "database.password = PASSWORD");
                fw.close();
                System.out.println("A new database config file has been created under app/src/main/resources");
                System.out.println("Please fill in the details in this config file for your postgres database");
                return;
            }
            prop.load(new FileInputStream(file));
        } catch (FileNotFoundException ex) {
            System.out.println("file app/src/main/resources/database.config not found");
        } catch (IOException ex) {
            System.out.println("IO exception:");
            ex.printStackTrace();
        }
        user = prop.getProperty("database.user");
        host = prop.getProperty("database.host");
        port = prop.getProperty("database.port");
        database = prop.getProperty("database.database");
        url = "jdbc:postgresql://" + host + ":" + port + "/"
                + database;
        pass = prop.getProperty("database.password");

        if (port.equals("PORT")) {
            System.out.println("Please fill in your port in database.config, i.e. 5432");
        }
        if (database.equals("DATABASE")) {
            System.out.println("Please fill in your database name in database.config, i.e. postgres");
        }
        if (user.equals("USER")) {
            System.out.println("Please fill in your user in database.config i.e. postgres");
        }
        if (pass.equals("PASSWORD")) {
            System.out.println("Please fill in your postgres password in database.config");
        }

    }

    public Connection connect() {
        conn = null;
        if (database == null) {
            System.out.println("database is null, cannot connect. Maybe check your database settings?");
            return null;
        }
        System.out.println("connecting to " + url + "...");
        try {
            Class.forName("org.postgresql.Driver");
            conn = DriverManager.getConnection(url, user, pass);
            System.out.println("connected to postgres database!");
        } catch (SQLException e) {
            System.out.println("error connecting to database:");
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            System.out.println("Expected driver org.postgresql.Driver not found");
        }
        return conn;
    }

    /**
     * Returns data from the database following the passed query
     * 
     * @param query
     * @return A DatabaseResult object
     */
    public DatabaseResult select(String query) {
        DatabaseResult dbr = new DatabaseResult();
        if (!(query.toLowerCase().startsWith("select"))) {
            System.out.println("Invalid SQL action for selecting: " + query);
            return dbr;
        }
        try {
            if (conn == null || conn.isClosed()) {
                System.out.println("connection to database is null or closed, reconnecting...");
                connect();
            }
            Statement s = conn.createStatement();
            ResultSet rs = s.executeQuery(query);
            while (rs.next()) {
                ResultSetMetaData rsm = rs.getMetaData();
                LinkedHashMap<String, String> hashMap = new LinkedHashMap<>();
                for (int i = 1; i <= rsm.getColumnCount(); i++) {
                    hashMap.put(rsm.getColumnLabel(i), rs.getString(i));
                }
                dbr.addNewResource(hashMap);
            }
        } catch (SQLException e) {
            System.out.println("Error selecting from database with query: " + query);
            System.out.println(e.getMessage());
        }
        return dbr;
    }

    private boolean tableModification(String query, String action) {
        if (!(query.toLowerCase().startsWith(action))) {
            System.out.println("Invalid SQL action for " + action + ": " + query);
            return false;
        }
        try {
            if (conn == null || conn.isClosed()) {
                System.out.println("conneciton to database is null or clsoed, reconnecting");
                connect();
            }
            Statement s = conn.createStatement();
            s.executeQuery(query);
            return true;
        } catch (SQLException e) {
            System.out.println("Error inserting into database with query: " + query);
            System.out.println(e.getMessage());
            return false;
        }
    }

    /**
     * Insert data into the database
     * 
     * @param query
     * @return A boolean or success or fail
     */
    public boolean insert(String query) {
        return tableModification(query, "insert");
    }

    /**
     * Update data already existing in the database
     * 
     * @param query
     * @return A boolean or success or fail
     */
    public boolean update(String query) {
        return tableModification(query, "update");
    }

    /**
     * Delete data in the database
     * 
     * @param query
     * @return A boolean or success or fail
     */
    public boolean delete(String query) {
        return tableModification(query, "delete");
    }

}
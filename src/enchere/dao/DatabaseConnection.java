package enchere.dao;

import enchere.model.*;

import java.sql.*;

public class DatabaseConnection {
    static final String CONN_URL = "jdbc:oracle:thin:@oracle1.ensimag.fr:1521:oracle1";
    static final String USER = "leducb";
    static final String PASSWD = "leducb";

    private static Connection conn = null;
    public static User userConnecte;

    public DatabaseConnection() {
    }

    public static Connection getConnection() {
        if (conn == null) {
            try {

                System.out.print("Connecting to the database... ");
                conn = DriverManager.getConnection(CONN_URL, USER, PASSWD);
                conn.setAutoCommit(false);
                conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
                System.out.println("connected");

            } catch (SQLException e) {
                System.err.println("failed");
                e.printStackTrace(System.err);
                return null;
            }
        }
        return conn;
    }

    public static void closeConnection() {

        if (conn == null) {
            return;
        }
        try {
            conn.close();
        } catch (SQLException e) {
            System.err.println("failed");
            e.printStackTrace(System.err);
        }
    }
}

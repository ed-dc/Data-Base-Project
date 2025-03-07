package enchere.dao;

import enchere.model.*;
import enchere.ui.FonctionUI;

import java.sql.*;

public class UserDAO {
    private final Connection connection;

    public UserDAO(Connection connection) {
        this.connection = connection;
    }

    /**
     * Renvoie le compte utilisateur créé ou null si cela échoue
     *
     * @param email
     * @param lastName
     * @param firstName
     * @param addresse
     * @return
     */
    public User creerUser(String email, String lastName, String firstName, String addresse) {

        try {
            connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
            connection.setAutoCommit(false);

            String sql =
                    "INSERT INTO USR (email, lastName, firstName, address) "
                            + "VALUES (?, ?, ?, ?)";
            PreparedStatement stmt =
                    connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            stmt.setString(1, email);
            stmt.setString(2, lastName);
            stmt.setString(3, firstName);
            stmt.setString(4, addresse);

            int rowsInserted = stmt.executeUpdate();

            if (rowsInserted > 0) {
                ResultSet rs = stmt.getGeneratedKeys();
                if (rs.next()) {
                    // System.out.println("User créé");

                    FonctionUI.nextSuccessMessage = "Compte créé";

                    connection.commit();

                    return new User(email, lastName, firstName, addresse);
                }
            }

            return null;
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException rollbackException) {
                FonctionUI.nextErrorMessage = "Error during rollback";
            }
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Renvoie vrai si l'email existe
     *
     * @param email
     * @return
     */
    public boolean emailExist(String email) {

        try {
            connection.setAutoCommit(false);
            connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);

            String sql = "SELECT * FROM USR WHERE EMAIL = ?";

            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();
            connection.commit();
            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public User getUser(String email) {

        try {
            connection.setAutoCommit(false);
            connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);

            String sql = "SELECT * FROM USR WHERE EMAIL = ?";
            PreparedStatement stmt = connection.prepareStatement(sql);

            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String lastName = rs.getString("lastName");
                String firstName = rs.getString("firstName");
                String address = rs.getString("address");
                connection.commit();
                return new User(email, lastName, firstName, address);
            }

            connection.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}

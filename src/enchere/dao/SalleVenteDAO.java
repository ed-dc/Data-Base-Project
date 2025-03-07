package enchere.dao;

import enchere.ui.FonctionUI;

import java.sql.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;

public class SalleVenteDAO {

    private final Connection connection;

    public SalleVenteDAO(Connection connection) {
        this.connection = connection;
    }

    public List<String> getSalleVente() {
        try {
            connection.setAutoCommit(false);
            connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);

            String sql = "SELECT idSaleRoom,NAMECATEGORY FROM SALEROOM";
            PreparedStatement stmt = connection.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            List<String> ids = new ArrayList<>();
            while (rs.next()) {
                ids.add(rs.getInt("idSaleRoom") + " " + rs.getString("nameCategory"));
            }

            connection.commit();
            return ids;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * get all sales in a sale room return a 1 list of string : {idSale mailUserSelling idProduct
     * idSaleRoom startingPrice isUpward isRevocable isLimited isUnique}
     *
     * @param id
     * @return
     */
    public ArrayList<String> getSaleBySaleRoomId(int id) {
        ArrayList<String> sales = new ArrayList<>();

        try {
            connection.commit();
            connection.setAutoCommit(false);
            connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);

            String sql = "SELECT * FROM SALE WHERE idSaleRoom = ?";
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                sales.add(
                        rs.getInt("idSale")
                                + " "
                                + rs.getString("mailUserSelling")
                                + " "
                                + rs.getInt("idProduct")
                                + " "
                                + rs.getInt("idSaleRoom")
                                + " "
                                + rs.getInt("startingPrice")
                                + " "
                                + rs.getBoolean("isUpward")
                                + " "
                                + rs.getBoolean("isRevocable")
                                + " "
                                + rs.getBoolean("isLimited")
                                + " "
                                + rs.getBoolean("isUnique"));
            }

            connection.commit();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return sales;
    }

    public ArrayList<String> getAllSale() {
        ArrayList<String> sales = new ArrayList<>();

        try {
            connection.setAutoCommit(false);
            connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);

            String sql = "SELECT * FROM SALE";
            PreparedStatement stmt = connection.prepareStatement(sql);

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                sales.add(
                        rs.getInt("idSale")
                                + " "
                                + rs.getString("mailUserSelling")
                                + " "
                                + rs.getInt("idProduct")
                                + " "
                                + rs.getInt("idSaleRoom")
                                + " "
                                + rs.getInt("startingPrice")
                                + " "
                                + rs.getBoolean("isUpward")
                                + " "
                                + rs.getBoolean("isRevocable")
                                + " "
                                + rs.getBoolean("isLimited")
                                + " "
                                + rs.getBoolean("isUnique"));
            }

            connection.commit();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return sales;
    }

    public ArrayList<String> getAllCategoryName() {
        ArrayList<String> categories = new ArrayList<>();

        try {
            connection.setAutoCommit(false);
            connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);

            String sql = "SELECT * FROM CATEGORY";
            PreparedStatement stmt = connection.prepareStatement(sql);

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                categories.add(rs.getString("name"));
            }

            connection.commit();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return categories;
    }

    public int createSaleRoom(String nameCategory) {
        try {
            connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
            connection.setAutoCommit(false);

            ArrayList<String> categories = getAllCategoryName();

            // on vérifie que la categorie existe
            if (!categories.contains(nameCategory)) {
                return -1;
            }

            String sql = "INSERT INTO SALEROOM (nameCategory) VALUES (?)";
            PreparedStatement stmt =
                    connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            stmt.setString(1, nameCategory);
            int rowsInserted = stmt.executeUpdate();

            if (rowsInserted > 0) {
                connection.commit();
                return rowsInserted;
            }

            connection.rollback();

            return -1;
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException rollbackException) {
                FonctionUI.nextErrorMessage = "Error during rollback";
            }

            throw new RuntimeException(e);
        }
    }

    public String getCategoryOfSaleRoom(int idSaleRoom) {
        try {
            connection.setAutoCommit(false);
            connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);

            String sql = "SELECT nameCategory FROM SALEROOM WHERE idSaleRoom = ?";
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, idSaleRoom);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getString("nameCategory");
            }

            connection.commit();
            return null;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}

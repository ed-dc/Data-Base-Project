package enchere.dao;

import enchere.ui.FonctionUI;

import java.sql.*;
import java.util.ArrayList;

public class ProduitDAO {
    private final Connection connection;

    public ProduitDAO(Connection connection) {
        this.connection = connection;
    }

    public boolean addProduct(
            String categoryName, String productName, int startingPrice, int quantity) {

        if (!isCategoryExist(categoryName)) {
            FonctionUI.nextErrorMessage = "Cette catégorie n'existe pas";
            return false;
        }

        try {
            connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
            connection.setAutoCommit(false);

            String sql =
                    "INSERT INTO PRODUCT (nameCategory, name, costPrice, "
                            + "quantity) VALUES (?,?,?,?)";
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, categoryName);
            stmt.setString(2, productName);
            stmt.setInt(3, startingPrice);
            stmt.setInt(4, quantity);
            stmt.executeUpdate();

            connection.commit();

        } catch (Exception e) {
            FonctionUI.nextErrorMessage = e.getMessage();
            try {
                connection.rollback();
            } catch (SQLException rollbackException) {
                FonctionUI.nextErrorMessage = "Error during rollback";
            }

            return false;
        }
        FonctionUI.nextSuccessMessage = "Produit créé avec succès";
        return true;
    }

    public ArrayList<String> getAllProduct() {
        ArrayList<String> stringList = new ArrayList<>();

        try {
            connection.setAutoCommit(false);
            connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);

            String sql = "SELECT * FROM PRODUCT";
            PreparedStatement stmt = connection.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                stringList.add(
                        rs.getString("name")
                                + ";"
                                + rs.getInt("costPrice")
                                + ";"
                                + rs.getInt("quantity")
                                + ";"
                                + rs.getInt("idProduct"));
            }

            connection.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return stringList;
    }

    public String getProductById(int id) {
        try {
            connection.setAutoCommit(false);
            connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);

            String sql = "SELECT * FROM PRODUCT WHERE idProduct = ?";
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getString("name")
                        + ";"
                        + rs.getInt("costPrice")
                        + ";"
                        + rs.getInt("quantity")
                        + ";"
                        + rs.getInt("idProduct");
            }

            connection.commit();
            return null;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public ArrayList<String> getInfoById(int id) {

        ArrayList<String> characteristics = new ArrayList<>();

        try {
            connection.setAutoCommit(false);
            connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);

            String sql =
                    "SELECT ch.name AS characteristic_name, ch.value AS "
                            + "characteristic_value FROM CHARACTERISTIC "
                            + "ch WHERE ch.idProduct = ?";
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, id + "");
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                characteristics.add(
                        rs.getString("characteristic_name")
                                + " : "
                                + rs.getString("characteristic_value")
                                + ";");
            }

            connection.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            connection.setAutoCommit(false);
            connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);

            String sql =
                    "SELECT c.name AS category_name, c.description AS "
                            + "category_description FROM PRODUCT p JOIN CATEGORY c ON "
                            + "p.nameCategory = c.name where p.idProduct = ?";
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, id + "");
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                characteristics.add(
                        rs.getString("category_name")
                                + " : "
                                + rs.getString("category_description")
                                + ";");
            }

            connection.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return characteristics;
    }

    public String getNameById(int id) {
        try {
            connection.setAutoCommit(false);
            connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);

            String sql = "SELECT name FROM PRODUCT WHERE idProduct = ?";
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, id + "");
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                connection.commit();
                return rs.getString("name");
            }

            connection.commit();
            return "";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public boolean isCategoryExist(String name) {

        try {
            connection.setAutoCommit(false);
            connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);

            String sql = "SELECT * FROM CATEGORY WHERE name = ?";
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, name);
            ResultSet rs = stmt.executeQuery();

            connection.commit();
            return rs.next();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public String getCategoryOfProduct(int idProduct) {
        try {
            connection.setAutoCommit(false);
            connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);

            String sql = "SELECT nameCategory FROM PRODUCT WHERE idProduct = ?";
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, idProduct);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                connection.commit();
                return rs.getString("nameCategory");
            }

            connection.commit();
            return null;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public int getQuantityOfProduct(int idProduct) {
        try {
            connection.setAutoCommit(false);
            connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);

            String sql = "SELECT quantity FROM PRODUCT WHERE idProduct = ?";
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, idProduct);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                connection.commit();
                return rs.getInt("quantity");
            }

            connection.commit();
            return 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}

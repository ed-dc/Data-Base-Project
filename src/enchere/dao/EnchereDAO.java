package enchere.dao;

import enchere.ui.FonctionUI;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class EnchereDAO {
    private final Connection connection;
    private final FinEnchereDAO finEnchereDAO;

    public EnchereDAO(Connection connection) {
        this.connection = connection;
        this.finEnchereDAO = new FinEnchereDAO(connection);
    }

    public boolean encherir(String obj, int idSalleVente, int prix, int quantite)
            throws NumberFormatException, SQLException {

        connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
        connection.setAutoCommit(false);

        int idSale = Integer.valueOf(obj.split(" ")[0]);
        String[] infosOffre = getOffer(idSale);
        int prevQuantity = getQuantiteByUser(DatabaseConnection.userConnecte.getEmail(), idSale);
        boolean isMontante = Boolean.valueOf(obj.split(" ")[5]);

        // Vente descendante prix de revient dépassé
        if (!isMontante) {
            try {
                String sql = "SELECT costPrice FROM PRODUCT WHERE idProduct = ?";
                PreparedStatement stmt = connection.prepareStatement(sql);
                stmt.setInt(1, Integer.valueOf(obj.split(" ")[2]));
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    int prixRevient = rs.getInt("costPrice");
                    if (prix < prixRevient) {
                        finEnchereDAO.endSale(idSale, true, true);
                    }
                }
            } catch (Exception e) {
                FonctionUI.nextErrorMessage = e.getMessage();
            }
        }

        if (finEnchereDAO.isSaleFinish(idSale)) {
            finEnchereDAO.endSale(idSale, Boolean.valueOf(obj.split(" ")[6]), true);
            FonctionUI.nextErrorMessage = "Cette vente est terminée";
            connection.commit();
            return false;
        }

        // On verifie si l'offre est unique est ce que la personne a deja encherit
        // dessus
        if (Boolean.valueOf(obj.split(" ")[8])
                && dejaEncherit(DatabaseConnection.userConnecte.getEmail())) {
            FonctionUI.nextErrorMessage = "Vous ne pouvez pas encherir 2 fois sur cette vente";
            connection.commit();
            return false;
        }

        if (infosOffre[2] != null) {
            // Vente montante et Prix inf à précédante enchère
            if (isMontante && (prix <= Integer.valueOf(infosOffre[2]))) {
                FonctionUI.nextErrorMessage = "Le prix est inferieur à l'enchère actuelle";
                connection.commit();
                return false;
            }
        }

        if (isMontante && (prix <= Integer.valueOf(obj.split(" ")[4]))) {
            FonctionUI.nextErrorMessage = "Le prix est inferieur au prix de départ";
            connection.commit();
            return false;
        }

        // Addition des quantité et check si dépasse pas la quantité dispo

        // System.err.println("quantite = " + quantite);
        // System.err.println("prevQuantity = " + prevQuantity);
        // System.err.println("x : " + getQuantiteById(Integer.valueOf(obj.split("
        // ")[2])));

        if (!Boolean.valueOf(obj.split(" ")[8])
                && (quantite + prevQuantity)
                        > getQuantiteById(Integer.valueOf(obj.split(" ")[2]))) {
            FonctionUI.nextErrorMessage =
                    "Vous demandez plus de produits que ce qui est disponible";
            connection.commit();
            return false;
        }

        // On peut alors faire la requête SQL
        try {
            String sql =
                    "INSERT INTO OFFER (mailUserOffering, idSale, purchasePrice, "
                            + "dateOffer, quantity, wasAccepted) VALUES (?,?,?,?,?,?)";
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, DatabaseConnection.userConnecte.getEmail());
            stmt.setInt(2, idSale);
            stmt.setInt(3, prix);
            stmt.setDate(4, new java.sql.Date(System.currentTimeMillis()));
            stmt.setInt(5, quantite + prevQuantity);
            stmt.setInt(6, 0);
            stmt.executeUpdate();

            if (!Boolean.valueOf(obj.split(" ")[5])) {
                finEnchereDAO.endSale(idSale, false, true);
            }

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
        FonctionUI.nextSuccessMessage = "Enchère réussi";
        return true;
    }

    private boolean dejaEncherit(String email) {
        try {
            connection.setAutoCommit(false);
            connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);

            String sql = "SELECT * FROM OFFER WHERE mailUserOffering = ?";
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();

            connection.commit();
            return rs.next();
        } catch (Exception e) {
            FonctionUI.nextErrorMessage = e.getMessage();
        }
        return false;
    }

    private int getQuantiteByUser(String email, int idsale) {
        int qqt = 0;
        try {
            connection.setAutoCommit(false);
            connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);

            String sql =
                    "SELECT quantity FROM OFFER WHERE mailUserOffering = ? " + "AND idsale = ?";
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, email);
            stmt.setInt(2, idsale);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                qqt += rs.getInt("quantity");
            }

            connection.commit();
        } catch (Exception e) {
            FonctionUI.nextErrorMessage = e.getMessage();
        }
        return qqt;
    }

    private int getQuantiteById(int idProduct) {
        try {
            connection.setAutoCommit(false);
            connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);

            String sql = "SELECT quantity FROM PRODUCT WHERE idProduct = ?";
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, idProduct);
            ResultSet rs = stmt.executeQuery();
            rs.next();

            connection.commit();
            return rs.getInt("quantity");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    // return String : idOffer; mailUserOffering; purchasePrice; dateOffer
    public String[] getOffer(int id) {
        String[] infos = new String[4];

        try {
            connection.setAutoCommit(false);
            connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);

            String sql =
                    " SELECT o.idOffer, o.mailUserOffering, o.purchasePrice, "
                            + "o.dateOffer FROM Offer o JOIN Sale s ON o.idSale = "
                            + "s.idSale WHERE s.idSale = ? AND s.isUpward = 1 AND "
                            + "o.purchasePrice >= s.startingPrice AND o.purchasePrice = "
                            + "(SELECT MAX(purchasePrice) FROM Offer WHERE idSale = ?)";

            PreparedStatement stmt = connection.prepareStatement(sql);

            stmt.setInt(1, id);
            stmt.setInt(2, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    infos[0] = String.valueOf(rs.getInt("idOffer"));
                    infos[1] = rs.getString("mailUserOffering");
                    infos[2] = String.valueOf(rs.getInt("purchasePrice"));
                    infos[3] = String.valueOf(rs.getDate("dateOffer"));
                }
            }

            connection.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return infos;
    }
}

package enchere.dao;

import enchere.ui.FonctionUI;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class FinEnchereDAO {
    private final Connection connection;

    public FinEnchereDAO(Connection connection) {
        this.connection = connection;
    }

    /**
     * Check if the sale is finished and if it is finished, set the isFinish to true
     *
     * @param saleId
     * @return
     * @throws SQLException
     */
    public boolean isSaleFinish(int saleId) throws SQLException {
        String sql = "SELECT isFinish FROM SALE WHERE idSale = ? ";
        PreparedStatement stmt = connection.prepareStatement(sql);
        stmt.setInt(1, saleId);
        ResultSet rs = stmt.executeQuery();

        if (rs.next())
            if (rs.getBoolean("isFinish")) {
                return true;
            } else {
                connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
                connection.setAutoCommit(false);

                String sql2 = "SELECT endTime FROM LIMITEDTIME WHERE idSale = ?";
                PreparedStatement stmt2 = connection.prepareStatement(sql2);
                stmt2.setInt(1, saleId);
                ResultSet rs2 = stmt2.executeQuery();

                // if it's time limited
                if (rs2.next()) {
                    LocalDateTime now = LocalDateTime.now();

                    Timestamp nowTimeStamp = Timestamp.valueOf(now);
                    Timestamp endTimeStamp = rs2.getTimestamp("endTime");

                    connection.commit();
                    return nowTimeStamp.compareTo(endTimeStamp) >= 0;
                } else {
                    // String sql3 = "SELECT startTime FROM SALE WHERE idSale = ?";
                    // PreparedStatement stmt3 = connection.prepareStatement(sql3);
                    // stmt3.setInt(1, saleId);
                    // ResultSet rs3 = stmt3.executeQuery();
                    //
                    //// get current date
                    // LocalDateTime now = LocalDateTime.now();
                    // Timestamp timeStampNow = Timestamp.valueOf(now);
                    //
                    // if (rs3.next()) {
                    // Timestamp timeEnd =
                    // new Timestamp(
                    // rs3.getTimestamp("startTime").getTime()
                    // + TimeUnit.MINUTES.toMillis(10));
                    //
                    // connection.commit();
                    // return timeStampNow.compareTo(timeEnd) >= 0;
                    // }

                    // get current date
                    LocalDateTime now = LocalDateTime.now();
                    Timestamp timeStampNow = Timestamp.valueOf(now);

                    String sql3 = "SELECT MAX(dateOffer) AS lastOfferDate FROM OFFER "
                            + "WHERE idSale = ?";
                    PreparedStatement stmt3 = connection.prepareStatement(sql3);
                    stmt3.setInt(1, saleId);
                    ResultSet rs3 = stmt3.executeQuery();

                    if (rs3.next()) {
                        Timestamp time = rs3.getTimestamp("lastOfferDate");
                        if (time == null) {
                            String sql4 = "SELECT startTime FROM SALE WHERE idSale = ?";
                            PreparedStatement stmt4 = connection.prepareStatement(sql4);
                            stmt4.setInt(1, saleId);
                            ResultSet rs4 = stmt4.executeQuery();

                            if (rs4.next()) {
                                Timestamp timeEnd = new Timestamp(
                                        rs4.getTimestamp("startTime").getTime()
                                                + TimeUnit.MINUTES.toMillis(10));

                                connection.commit();
                                return timeStampNow.compareTo(timeEnd) >= 0;
                            }
                            connection.commit();
                            return false;
                        }
                        Timestamp timeEnd = new Timestamp(time.getTime() + TimeUnit.MINUTES.toMillis(10));
                        System.err.println(timeEnd);

                        connection.commit();
                        return timeStampNow.compareTo(timeEnd) >= 0;
                    }

                    connection.commit();
                    return false;
                }
            }

        connection.commit();
        return false;
    }

    /**
     * Set the offer state of the sale
     *
     * @param saleId
     * @return nuber of offer which have been updated
     * @throws SQLException
     */
    public int setOfferState(int saleId, String winner) throws SQLException {
        // Get all offer for the sale

        String sql = "SELECT idOffer, mailUserOffering FROM OFFER WHERE idSale = ?";
        PreparedStatement stmt = connection.prepareStatement(sql);
        stmt.setInt(1, saleId);
        ResultSet rs = stmt.executeQuery();

        String winnerId = winner.split(" ")[0];
        int nbOfferUpdated = 0;

        while (rs.next()) {
            // Get the offer
            int offerId = rs.getInt("idOffer");
            boolean isFromWinner = winnerId.equals(rs.getString("mailUserOffering"));

            String sql2 = "UPDATE OFFER SET wasAccepted = ? WHERE idOffer = ?";
            PreparedStatement stmt2 = connection.prepareStatement(sql2);
            stmt2.setBoolean(1, isFromWinner);
            stmt2.setInt(2, offerId);
            int ndUpdated = stmt2.executeUpdate();

            if (ndUpdated != 1) {
                throw new SQLException("Error while updating the offer");
            }

            nbOfferUpdated++;
        }

        return nbOfferUpdated;
    }

    /**
     * Find the winner of the sale
     *
     * @param saleId
     * @return a list of the offer who have won
     * @throws SQLException
     */
    public String findWinner(int idSale) throws SQLException {
        String sql = "SELECT isUpward FROM SALE WHERE idSale = ?";
        PreparedStatement stmt = connection.prepareStatement(sql);
        stmt.setInt(1, idSale);
        ResultSet rs = stmt.executeQuery();

        if (rs.next()) {
            if (rs.getBoolean("isUpward")) { // Upward
                String sql2 = "SELECT o.mailUserOffering, SUM(o.quantity) AS "
                        + "totalQuantity, SUM(o.purchasePrice) AS "
                        + "totalOffered FROM OFFER o WHERE o.idSale = ? GROUP BY "
                        + "o.mailUserOffering ORDER BY totalOffered DESC";
                PreparedStatement stmt2 = connection.prepareStatement(sql2);
                stmt2.setInt(1, idSale);
                ResultSet rs2 = stmt2.executeQuery();
                if (rs2.next()) {
                    return rs2.getString("mailUserOffering")
                            + " -> "
                            + "quantité : "
                            + rs2.getInt("totalQuantity")
                            + " prix : "
                            + rs2.getInt("totalOffered");
                }
            } else { // Downward
                String sql2 = "SELECT o.mailUserOffering AS userEmail, o.purchasePrice AS "
                        + "price, o.quantity AS quantity FROM "
                        + "Offer o WHERE o.idSale = ? AND o.dateOffer = ( SELECT "
                        + "MIN(dateOffer) FROM "
                        + "Offer WHERE idSale = ?)";
                PreparedStatement stmt2 = connection.prepareStatement(sql2);
                stmt2.setInt(1, idSale);
                stmt2.setInt(2, idSale);
                ResultSet rs2 = stmt2.executeQuery();
                if (rs2.next()) {
                    return rs2.getString("userEmail")
                            + " -> "
                            + "quantité : "
                            + rs2.getInt("quantity")
                            + " prix : "
                            + rs2.getInt("price");
                }
            }
        }

        // return "winner@gmail.com -> quantité : 100 prix : 20€";
        return "";
    }

    /**
     * Refresh all the sale
     *
     * @return the number of sale finished
     * @throws SQLException
     */
    public int refreshAllSale() throws SQLException {
        int nbSaleFinished = 0;

        try {
            connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
            connection.setAutoCommit(false);

            // Select all saleId
            String sql = "SELECT idSale FROM SALE WHERE isFinish = 0";
            PreparedStatement stmt = connection.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                int saleId = rs.getInt("idSale");
                if (isSaleFinish(saleId)) {
                    // Trigger the function to find the winner update offer and sale state
                    endSale(saleId, false, true);
                    nbSaleFinished++;
                }
            }

            connection.commit();

        } catch (Exception e) {
            try {
                connection.rollback();
            } catch (SQLException rollbackException) {
                FonctionUI.nextErrorMessage = "Error during rollback";
            }
            throw e;
        }

        return nbSaleFinished;
    }

    /**
     * End the sale for the associated id
     *
     * @param saleId
     * @param isSuperUser true if the user is a super user (super User is allowed to
     *                    update all sale
     *                    state)
     * @return return the winner if he exist or an empty string
     * @throws SQLException
     */
    public String endSale(int saleId, boolean revoc, boolean isSuperUser) throws SQLException {
        String winner = "";

        try {
            connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
            connection.setAutoCommit(false);

            String sql = "SELECT mailUserSelling FROM SALE WHERE idSale = ?";
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, saleId);
            ResultSet rs = stmt.executeQuery();

            String userSaleId = "";

            // Find the userID for the Sale
            if (rs.next()) {
                userSaleId = rs.getString("mailUserSelling");

                // Multiple sale with the same id
                if (rs.next()) {
                    return null;
                }
            }

            // User isNotAllowed to end the sale
            if (!isSuperUser && !userSaleId.equals(DatabaseConnection.userConnecte.getEmail())) {
                return null;
            }

            // Update the sale

            String sql2 = "UPDATE SALE SET isFinish = 1 WHERE idSale = ?";
            PreparedStatement stmt2 = connection.prepareStatement(sql2);
            stmt2.setInt(1, saleId);
            int rowsUpdated = stmt2.executeUpdate();

            // If the update is not done
            if (rowsUpdated != 1) {
                return null;
            }

            // Trigger the function to find the winner if not revocable
            if (!revoc) {
                winner = findWinner(saleId);
                setOfferState(saleId, winner);
            }

            connection.commit();
        } catch (Exception e) {
            try {
                connection.rollback();
            } catch (SQLException rollbackException) {
                FonctionUI.nextErrorMessage = "Error during rollback";
            }
            throw e;
        }

        return winner;
    }

    public List<String> getMyRooms() throws SQLException {

        if (DatabaseConnection.userConnecte == null) {
            throw new SQLException("Veuillez vous connecter pour voir vos salles de vente");
        }

        String sql = "SELECT idSale, idProduct, isFinish FROM SALE WHERE " + "mailUserSelling = ?";
        PreparedStatement stmt = connection.prepareStatement(sql);
        stmt.setString(1, DatabaseConnection.userConnecte.getEmail());
        ResultSet rs = stmt.executeQuery();

        List<String> myRooms = new ArrayList<>();
        while (rs.next()) {
            myRooms.add(
                    rs.getInt("idSale")
                            + " "
                            + rs.getInt("idProduct")
                            + " "
                            + (rs.getBoolean("isFinish") ? "Finie" : "Active"));
        }

        return myRooms;
    }
}

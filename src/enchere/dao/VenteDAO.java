package enchere.dao;

import enchere.ui.FonctionUI;

import java.sql.*;
import java.time.*;
import java.util.ArrayList;

public class VenteDAO {

    private final Connection connection;
    private final SalleVenteDAO salleVenteDAO;
    private final ProduitDAO produitDAO;
    private final int TEMPS_ENCHERE_MINUTE = 10;

    // private final Calendar cal = new java.util.GregorianCalendar(1982, 0, 1);

    public VenteDAO(Connection connection) {
        this.connection = connection;
        this.salleVenteDAO = new SalleVenteDAO(connection);
        this.produitDAO = new ProduitDAO(connection);
    }

    public boolean createVente(
            int idSaleRoom,
            int startingPrice,
            boolean isMontante,
            boolean isRevoquable,
            boolean isLimited,
            boolean isUnique,
            int idProduct,
            int discountStep,
            int timeSale) {

        // FonctionUI.nextErrorMessage = "Type de vente non supporté";
        // return false;

        try {
            connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
            connection.setAutoCommit(false);

            String salleVenteCategory = salleVenteDAO.getCategoryOfSaleRoom(idSaleRoom);
            String productCategory = produitDAO.getCategoryOfProduct(idProduct);

            // on verifie si la categorie de la salle de vente et du produit sont les
            // memes
            if (!salleVenteCategory.equals(productCategory)) {
                FonctionUI.nextErrorMessage =
                        "La catégorie de la salle de vente et du " + "produit ne correspondent pas";

                connection.commit();
                return false;
            }

            ArrayList<String> allVente = salleVenteDAO.getSaleBySaleRoomId(idSaleRoom);

            // s'il y a deja des ventes on regardes si elles sont du meme type de
            // vente
            if (allVente.size() > 0) {

                String[] firstVente = allVente.get(0).split(" ");
                if (Boolean.parseBoolean(firstVente[5]) != isMontante
                        || Boolean.parseBoolean(firstVente[6]) != isRevoquable
                        || Boolean.parseBoolean(firstVente[7]) != isLimited
                        || Boolean.parseBoolean(firstVente[8]) != isUnique) {
                    FonctionUI.nextErrorMessage =
                            "Les ventes de la salle doivent etre de meme type de vente";
                    connection.commit();
                    return false;
                }
            }

            // Condition à supprimer ?

            // on verifie si le produit est deja en vente dans une autre salle
            allVente = salleVenteDAO.getAllSale();
            for (String vente : allVente) {
                String[] venteSplit = vente.split(" ");
                if (Integer.parseInt(venteSplit[2]) == idProduct) {
                    FonctionUI.nextErrorMessage =
                            "Le produit est déjà en vente dans une autre salle";
                    connection.commit();
                    return false;
                }
            }

            if (DatabaseConnection.userConnecte == null) {
                FonctionUI.nextErrorMessage = "Vous devez etre connecté pour créer une vente";
                connection.commit();
                return false;
            }

            connection.commit();
        } catch (Exception e) {
            FonctionUI.nextErrorMessage = "Ereur de transaction";
        }

        // Date debut_enchere = new Date(cal.getTime().getTime());

        // //fin enchere 10 min apres le debut
        // cal.add(Calendar.MINUTE, 10);

        // get current date
        LocalDateTime dateDebut = LocalDateTime.now();
        // add 10 minutes
        LocalDateTime dateFin = dateDebut.plusMinutes(timeSale);

        Timestamp timeStampDebut = Timestamp.valueOf(dateDebut);
        Timestamp timeStampFin = Timestamp.valueOf(dateFin);

        Integer idSale = null;

        try {
            connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
            connection.setAutoCommit(false);

            String sql =
                    "INSERT INTO SALE "
                            + "(mailUserSelling,idProduct,idSaleRoom,startingPrice,"
                            + "isUpward,isRevocable,isLimited,isUnique,isFinish, "
                            + "startTime) VALUES (?,?,?,?,?,?,?,?,?,?)";
            java.sql.PreparedStatement stmt =
                    connection.prepareStatement(sql, java.sql.Statement.RETURN_GENERATED_KEYS);
            stmt.setString(1, DatabaseConnection.userConnecte.getEmail());
            stmt.setInt(2, idProduct);
            stmt.setInt(3, idSaleRoom);
            stmt.setInt(4, startingPrice);
            stmt.setBoolean(5, isMontante);
            stmt.setBoolean(6, isRevoquable);
            stmt.setBoolean(7, isLimited);
            stmt.setBoolean(8, isUnique);
            stmt.setBoolean(9, false);
            stmt.setTimestamp(10, timeStampDebut);

            int affectedRows = stmt.executeUpdate();

            if (affectedRows > 0) {
                ResultSet rs = stmt.getGeneratedKeys();
                if (rs.next()) {
                    Object idSaleObj = rs.getObject(1);
                    String rowidSale = idSaleObj.toString();
                    String sql2 = "SELECT idSale FROM SALE WHERE rowid = ? ";
                    PreparedStatement stmt2 = connection.prepareStatement(sql2);
                    stmt2.setString(1, rowidSale);
                    ResultSet rs2 = stmt2.executeQuery();

                    if (rs2.next()) {
                        idSale = rs2.getInt("idSale");
                    }
                }
            }

            if (idSale == null) {
                FonctionUI.nextErrorMessage = "Error: the sale is not in the database";
                // rollback
                connection.rollback();
            }

            try {
                if (isLimited) {
                    String sql3 = "INSERT INTO LIMITEDTIME (endTime,idSale) VALUES (?,?)";
                    PreparedStatement stmt3 = connection.prepareStatement(sql3);

                    stmt3.setTimestamp(1, timeStampFin);
                    stmt3.setInt(2, idSale);

                    stmt3.executeQuery();
                }

                if (!isMontante) {
                    String sql3 = "INSERT INTO DOWNWARDSALE (discountStep,idSale) VALUES (?,?)";
                    PreparedStatement stmt3 = connection.prepareStatement(sql3);

                    stmt3.setInt(1, discountStep);
                    stmt3.setInt(2, idSale);

                    stmt3.executeQuery();
                }

                connection.commit();
            } catch (Exception e) {
                FonctionUI.nextErrorMessage = "----" + e.getMessage();
                System.err.println(e);

                try {
                    connection.rollback();
                } catch (SQLException rollbackException) {
                    FonctionUI.nextErrorMessage = "Error during rollback";
                }

                return false;
            }

        } catch (Exception e) {
            FonctionUI.nextErrorMessage = e.getMessage();
            System.err.println(e);
            return false;
        }

        FonctionUI.nextSuccessMessage = "Vente créée";
        return true;
    }

    public int getPriceDownward(int idSale, int idProduct, int startingPrice) {
        int price = 0;
        try {
            connection.setAutoCommit(false);
            connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);

            String sql = "SELECT startTime FROM SALE WHERE idSale = ? AND idProduct = ?";
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, idSale);
            stmt.setInt(2, idProduct);
            ResultSet rs1 = stmt.executeQuery();

            try {
                String sql2 = "SELECT discountStep FROM DOWNWARDSALE where idSale = ?";
                PreparedStatement stmt2 = connection.prepareStatement(sql2);
                stmt2.setInt(1, idSale);
                ResultSet rs2 = stmt2.executeQuery();

                if (rs1.next() && rs2.next()) {
                    Timestamp timeStampNow = Timestamp.valueOf(LocalDateTime.now());
                    long timeNow = timeStampNow.getTime();
                    long elapsedMin = (timeNow - rs1.getTimestamp("startTime").getTime()) / 60000;

                    price = (int) (startingPrice - (rs2.getInt("discountStep") * elapsedMin));
                }

                connection.commit();
            } catch (Exception e) {
                FonctionUI.nextErrorMessage = e.getMessage();
            }
        } catch (Exception e) {
            FonctionUI.nextErrorMessage = e.getMessage();
        }
        return price;
    }
}

package enchere.service;

import enchere.dao.*;
import enchere.env.*;
import enchere.ui.*;

import java.sql.*;
import java.util.List;

public class VenteService {

    private VenteDAO venteDAO;
    private EnchereDAO enchereDAO;
    private ProduitDAO produitDAO;

    public VenteService(Connection connection) {
        this.venteDAO = new VenteDAO(connection);
        this.enchereDAO = new EnchereDAO(connection);
        this.produitDAO = new ProduitDAO(connection);
    }

    public void CreateSale(int idSaleRoom) {
        // FonctionUI.displayQuestion("Création d'une vente dans la salle " +
        // idSaleRoom, "Quel est le prix de départ de la vente ?");
        int discountStep = 0;
        boolean isMontante =
                FonctionUI.questionYesNo(
                        "Création d'une vente dans la salle " + idSaleRoom,
                        "La vente est-elle montante ?");

        if (!isMontante) {
            discountStep =
                    FonctionUI.intQuestion(
                            "Création d'une vente dans la salle " + idSaleRoom,
                            "De combien réduire le prix chaque minute ?",
                            "Veuillez entrer un nombre");
        }

        boolean isRevoquable =
                FonctionUI.questionYesNo(
                        "Création d'une vente dans la salle " + idSaleRoom,
                        "La vente est-elle révocable ?");

        boolean isLimited =
                FonctionUI.questionYesNo(
                        "Création d'une vente dans la salle " + idSaleRoom,
                        "La vente est-elle limitée ?");

        int timeSale = 10;
        if (isLimited) {
            timeSale =
                    FonctionUI.intQuestion(
                            "Création d'une vente dans la salle " + idSaleRoom,
                            "Combien de temps dure la vente ?",
                            "Veuillez entrer un nombre");
        }
        boolean isUnique =
                FonctionUI.questionYesNo(
                        "Création d'une vente dans la salle " + idSaleRoom,
                        "La vente est-elle unique ?");
        int startingPrice =
                FonctionUI.intQuestion(
                        "Création d'une vente dans la salle " + idSaleRoom,
                        "Quel est le prix de départ de la vente ?",
                        "Veuillez entrer un nombre");
        int idProduct =
                FonctionUI.intQuestion(
                        "Création d'une vente dans la salle " + idSaleRoom,
                        "Quel est l'id du produit ?",
                        "Veuillez entrer un nombre");

        venteDAO.createVente(
                idSaleRoom,
                startingPrice,
                isMontante,
                isRevoquable,
                isLimited,
                isUnique,
                idProduct,
                discountStep,
                timeSale);
    }

    public void afficherMenuVenteObjet(String objet, int idSalleVente) {
        // obj : idVente; mailUserSelling; idProduct; idSaleRoom; startingPrice;
        // isUpward; isRevocable; isLimited; isUnique

        int idProduct = Integer.valueOf(objet.split(" ")[2]);
        String productName = produitDAO.getNameById(idProduct);
        String[] offre = enchereDAO.getOffer(Integer.valueOf(objet.split(" ")[0]));
        int choix = 3;

        if (!Boolean.valueOf(objet.split(" ")[5])) {
            int price =
                    venteDAO.getPriceDownward(
                            Integer.valueOf(objet.split(" ")[0]),
                            Integer.valueOf(objet.split(" ")[2]),
                            Integer.valueOf(objet.split(" ")[4]));
            choix =
                    FonctionUI.displayMenueChoixMultiple(
                            "Menu de vente de l'objet " + productName,
                            (price >= 0)
                                    ? "Vente descendante, prix actuel : " + price + "€"
                                    : "Cette vente est terminée",
                            List.of("Enchérir", "Raffraichir", "Retour"),
                            "");
        } else if (offre[2] == null) {
            choix =
                    FonctionUI.displayMenueChoixMultiple(
                            "Menu de vente de l'objet " + productName,
                            "Pas d'enchère sur ce produit, prix de départ : "
                                    + objet.split(" ")[4]
                                    + "€",
                            List.of("Enchérir", "Raffraichir", "Retour"),
                            "");
        } else {
            choix =
                    FonctionUI.displayMenueChoixMultiple(
                            "Menu de vente de l'objet " + productName,
                            "Prix actuel de l'enchère : " + offre[2] + "€, daté du " + offre[3],
                            List.of("Enchérir", "Raffraichir", "Retour"),
                            "");
        }

        switch (choix) {
            case 1:
                afficherEnchere(
                        objet, idProduct, idSalleVente, Boolean.valueOf(objet.split(" ")[5]));
                break;
            case 2:
                afficherMenuVenteObjet(objet, idSalleVente);
                break;
            case 3:
                return;
            default:
                return;
        }
    }

    public void afficherEnchere(String objet, int idProduct, int idSalleVente, boolean upward) {
        String name = produitDAO.getNameById(idProduct);
        int prix;
        if (upward) {
            prix =
                    FonctionUI.intQuestion(
                            "Enchère sur l'objet " + name,
                            "Quel est votre prix ?",
                            "Veuillez entrer un nombre");
        } else {
            prix =
                    venteDAO.getPriceDownward(
                            Integer.valueOf(objet.split(" ")[0]),
                            Integer.valueOf(objet.split(" ")[2]),
                            Integer.valueOf(objet.split(" ")[4]));
        }
        int quantite =
                FonctionUI.intQuestion(
                        "Enchère sur l'objet " + name,
                        "Quelle quantité voulez-vous ?",
                        "Veuillez entrer un nombre");
        try {
            enchereDAO.encherir(objet, idSalleVente, prix, quantite);
        } catch (NumberFormatException | SQLException e) {
            e.printStackTrace();
        }
    }
}

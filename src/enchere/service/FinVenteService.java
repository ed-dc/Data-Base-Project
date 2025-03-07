package enchere.service;

import enchere.dao.*;
import enchere.ui.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FinVenteService {
    private FinEnchereDAO finEnchereDAO;

    public FinVenteService(Connection connection) {
        this.finEnchereDAO = new FinEnchereDAO(connection);
    }

    public void refreshAllSale() {
        try {
            int nbRefreshed = finEnchereDAO.refreshAllSale();
            FonctionUI.nextSuccessMessage = nbRefreshed + (" Vente on été mise à " + "jour");
            return;
        } catch (SQLException e) {
            FonctionUI.nextErrorMessage = "Erreur pendant la mise à jour des ventes";
            System.err.println(e);
        }
    }

    public void voirFinVente() {
        int nbSalles = 0;
        try {
            List<String> myRooms = finEnchereDAO.getMyRooms();
            nbSalles = myRooms.size();

            List<String> listeNomString = new ArrayList<>();
            for (String salle : myRooms) {
                listeNomString.add(
                        "Vente n°"
                                + salle.split(" ")[0]
                                + " (produit n°"
                                + salle.split(" ")[1]
                                + ") "
                                + salle.split(" ")[2]);
            }
            // ajouter un retour
            listeNomString.add("Retour");
            int choix = FonctionUI.displayMenueChoixMultiple(
                    "Choisissez votre salle de vente",
                    "Entrez le numéro de la salle de vente",
                    listeNomString,
                    "");

            if (choix > 0 && choix < listeNomString.size()) {
                afficherMenuFinVente(myRooms, choix - 1);
            } else if (choix == 0) {
                return;
            } else {
                FonctionUI.nextErrorMessage = "Veuillez entrer un nombre entre 0 et " + (listeNomString.size() - 1);
                voirFinVente();
            }

        } catch (NumberFormatException e) {
            FonctionUI.nextErrorMessage = "Veuillez entrer un nombre entre 0 et " + nbSalles;
            voirFinVente();
        } catch (SQLException e) {
            FonctionUI.nextErrorMessage = "Veillez vous connecter pour voir vos salles de vente" + e;
            voirFinVente();
        }
    }

    public void afficherMenuFinVente(List<String> myRooms, int choix) {
        try {
            List<String> winners = new ArrayList<String>();
            String winner = finEnchereDAO.endSale(Integer.valueOf(myRooms.get(choix).split(" ")[0]), false, false);
            winners.add(winner);
            winners.add("Retour");

            int choixx = FonctionUI.displayMenueChoixMultiple(
                    "Voici les vainqueurs de votre enchère",
                    "Faite 0 pour retourner au menu de fin de vente",
                    winners,
                    "");

            voirFinVente();
            if (choixx == 0) {
                return;
            } else {
                throw new NumberFormatException();
            }
        } catch (NumberFormatException e) {
            System.out.println("Veuillez entrer un nombre valide*");
        } catch (SQLException e) {
            FonctionUI.nextErrorMessage = "Erreur lors de lors de la cloture de la vente en sql";
            System.err.println(e);
            voirFinVente();
        }
    }
}

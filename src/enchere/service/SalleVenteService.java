package enchere.service;

import enchere.dao.*;
import enchere.env.*;
import enchere.ui.FonctionUI;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SalleVenteService {

    private SalleVenteDAO salleVenteDAO;
    private VenteService venteService;
    private ProduitDAO produitDAO;

    public SalleVenteService(Connection connection) {
        this.salleVenteDAO = new SalleVenteDAO(connection);
        this.venteService = new VenteService(connection);
        this.produitDAO = new ProduitDAO(connection);
    }

    public void voirSallesVente() {
        List<String> salles = salleVenteDAO.getSalleVente();

        try {
            List<String> listeNomString = new ArrayList<>();
            for (String salle : salles) {
                listeNomString.add(
                        "Salle " + salle.split(" ")[0] + " (" + salle.split(" ")[1] + ")");
            }
            // ajouter un retour
            listeNomString.add("Retour");
            int choix =
                    FonctionUI.displayMenueChoixMultiple(
                            "Choisissez votre salle de vente",
                            "Entrez le numéro de la salle de vente",
                            listeNomString,
                            "");

            if (choix > 0 && choix < listeNomString.size()) {
                afficherMenuSalleVente(Integer.parseInt(salles.get(choix - 1).split(" ")[0]));
            } else if (choix == 0 || choix == listeNomString.size()) {
                return;
            } else {
                FonctionUI.nextErrorMessage =
                        "Veuillez entrer un nombre entre 0 et " + listeNomString.size();
                voirSallesVente();
            }

        } catch (NumberFormatException e) {
            FonctionUI.nextErrorMessage = "Veuillez entrer un nombre entre 0 et " + salles.size();
            voirSallesVente();
        }
    }

    public void afficherMenuSalleVente(int idSalleVente) {
        int choix =
                FonctionUI.displayMenueChoixMultiple(
                        "Menu de la salle de vente " + idSalleVente,
                        "choisissez votre action",
                        List.of("Voir les objets en vente", "Créer une vente", "Retour"),
                        "");
        try {
            switch (choix) {
                case 1:
                    voirObjetsEnVente(idSalleVente);
                    afficherMenuSalleVente(idSalleVente);
                    break;
                case 2:
                    venteService.CreateSale(idSalleVente);
                    afficherMenuSalleVente(idSalleVente);
                    break;
                // case 3:
                // voirSallesVente();
                // break;
                default:
                    System.out.println("Choix invalide");
                    break;
            }
        } catch (NumberFormatException e) {
            System.out.println("Veuillez entrer un nombre valide*");
        }
    }

    public void voirObjetsEnVente(int idSalleVente) {
        ArrayList<String> Objets = salleVenteDAO.getSaleBySaleRoomId(idSalleVente);
        ArrayList<String> stringList = new ArrayList<>();

        for (String objet : Objets) {
            String productInfo = produitDAO.getProductById(Integer.parseInt(objet.split(" ")[2]));
            String nomProduit = productInfo.split(";")[0];
            String vendeur = "(" + objet.split(" ")[1] + ")";
            String quantity = productInfo.split(";")[2];
            String id = productInfo.split(";")[3];
            stringList.add(nomProduit + " " + vendeur + " id:" + id + " qqt:" + quantity);
        }

        stringList.add("Retour");

        int choix =
                FonctionUI.displayMenueChoixMultiple(
                        "Objets en vente", "selectionnez l'objet pour enchérir", stringList, "");
        // afficherMenuSalleVente(idSalleVente);
        if (choix == 0 || choix == stringList.size()) {
            return;
        }
        venteService.afficherMenuVenteObjet(Objets.get(choix - 1), idSalleVente);
    }

    public void creerSalleVente() {

        String nameCategory =
                FonctionUI.displayQuestion(
                        "Création d'une salle de vente",
                        "Quel est le nom de la catégorie de la salle de vente ?",
                        "");

        boolean isExist = produitDAO.isCategoryExist(nameCategory);
        if (!isExist) {
            FonctionUI.nextErrorMessage = "La catégorie n'existe pas";
            return;
        }
        int idRoom = salleVenteDAO.createSaleRoom(nameCategory);
        FonctionUI.nextSuccessMessage = "Salle de vente créée avec succès";
        voirSallesVente();
    }
}

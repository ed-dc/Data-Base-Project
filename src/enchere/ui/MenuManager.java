package enchere.ui;

import enchere.dao.*;
import enchere.env.*;
import enchere.model.*;
import enchere.service.*;

import java.sql.*;
import java.util.List;

public class MenuManager {

    private final UserService userService;
    private final Connection connection;
    private final SalleVenteService salleVenteService;
    private final productService productService;
    private final FinVenteService finVenteService;
    // private final EnchereService enchereService;
    public static String ErrorMessage = "";

    public MenuManager(Connection connection) {

        this.connection = connection;
        this.userService = new UserService(connection);
        this.salleVenteService = new SalleVenteService(connection);
        this.productService = new productService(connection);
        this.finVenteService = new FinVenteService(connection);

        // this.enchereService = new EnchereService(connection);
    }

    public void afficherMenuPrincipal() {
        while (true) {
            if (DatabaseConnection.userConnecte == null) {
                afficherMenuConnexion();
            } else {
                afficherMenuUser();
            }
        }
    }

    private void afficherMenuConnexion() {

        int choix =
                FonctionUI.displayMenueChoixMultiple(
                        "Menu Connexion",
                        "Choisissez votre action",
                        List.of("Se connecter", "Créer un compte", "Quitter"),
                        ErrorMessage);

        ErrorMessage = "";

        try {

            switch (choix) {
                case 1:
                    DatabaseConnection.userConnecte = userService.connecterUser();
                    break;
                case 2:
                    DatabaseConnection.userConnecte = userService.creerUser();
                    break;
                case 0:
                    FonctionUI.quitterApplication();
                    break;
                default:
                    System.out.println("Choix invalide");
            }
        } catch (NumberFormatException e) {
            System.out.println("Veuillez entrer un nombre valide");
        }
    }

    private void afficherMenuUser() {
        // Global.enchereAscii();
        // System.out.println("1. Voir les salles de vente");
        // System.out.println("2. Créer une salle de vente");
        // System.out.println("3. Se déconnecter");
        // System.out.println("0. Quitter");

        int choix =
                FonctionUI.displayMenueChoixMultiple(
                        "ENCHERE",
                        "choisissez votre action",
                        List.of(
                                "Voir les salles de vente",
                                "Créer une salle de vente",
                                "Voir les produits à la vente",
                                "Créer un nouveau produit",
                                "Mettre à jour les ventes",
                                "Mettre fin à mes ventes",
                                "Se déconnecter",
                                "Quitter"),
                        "");

        try {

            switch (choix) {
                case 1:
                    salleVenteService.voirSallesVente();
                    afficherMenuUser();
                    break;
                case 2:
                    salleVenteService.creerSalleVente();
                    break;
                case 3:
                    productService.displayProduct();
                    afficherMenuUser();
                    break;
                case 4:
                    productService.createProduct();
                    afficherMenuUser();
                    break;
                case 5:
                    finVenteService.refreshAllSale();
                    break;
                case 6:
                    finVenteService.voirFinVente();
                    break;
                case 7:
                    deconnecterUser();
                    break;
                    
                case 0:
                    FonctionUI.quitterApplication();
                    break;
                default:
                    System.out.println("Choix invalide");
            }
        } catch (NumberFormatException e) {
            System.out.println("Veuillez entrer un nombre valide");
        }
    }

    private void deconnecterUser() {
        DatabaseConnection.userConnecte = null;
    }
}

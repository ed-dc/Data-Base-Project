package enchere.service;

import enchere.dao.*;
import enchere.env.*;
import enchere.ui.FonctionUI;

import java.sql.*;
import java.util.ArrayList;

public class productService {

    private ProduitDAO produitDAO;

    public productService(Connection connection) {
        this.produitDAO = new ProduitDAO(connection);
    }

    public void displayProduct() {
        ArrayList<String> stringList = produitDAO.getAllProduct();
        ArrayList<Integer> ids = new ArrayList<>();

        for (int i = 0; i < stringList.size(); i++) {
            String[] parts = stringList.get(i).split(";");
            if (parts.length < 4) { // Vérifie si les données sont valides
                FonctionUI.nextErrorMessage = stringList.get(1);
                continue;
            }
            String name = parts[0];
            String costPrice = parts[1] + "€";
            String quantity = "  quantité:" + parts[2];
            String id = " id:" + parts[3];
            ids.add(Integer.parseInt(parts[3]));

            String formatted = name + " " + costPrice + " " + quantity + " " + id;
            stringList.set(i, formatted);
        }

        stringList.add("Rafraichir");
        stringList.add("Retour");

        int choix =
                FonctionUI.displayMenueChoixMultiple(
                        "Objets en vente",
                        "selectionnez un objet pour voir caractéristique(s) ou catégorie(s)",
                        stringList,
                        "");

        if (choix == (stringList.size() - 1)) {
            displayProduct();
        } else if (choix != 0) {
            System.err.println("choix = " + choix + " id = " + ids.get(choix - 1));
            displayCharacteristicProduct(ids.get(choix - 1));
            displayProduct();
        }
    }

    public void createProduct() {
        String categoryName =
                FonctionUI.displayQuestion(
                        "Création d'un produit", "Quel est le nom de la catégorie ?", "");
        String name =
                FonctionUI.displayQuestion(
                        "Création d'un produit", "Quel est le nom de votre produit ?", "");
        int price =
                FonctionUI.intQuestion(
                        "Création d'un produit",
                        "Quel est le prix de départ de votre produit ?",
                        "");
        int quantity =
                FonctionUI.intQuestion(
                        "Création d'un produit",
                        "Quel est la quantité de produit disponible ?",
                        "");

        produitDAO.addProduct(categoryName, name, price, quantity);
    }

    public void displayCharacteristicProduct(int id) {

        ArrayList<String> characteristics = produitDAO.getInfoById(id);
        String name = produitDAO.getNameById(id);

        FonctionUI.displayMenueAffichage(
                "Objets en vente",
                "Caractéristiques et catégories de " + name,
                characteristics,
                "");
    }
}

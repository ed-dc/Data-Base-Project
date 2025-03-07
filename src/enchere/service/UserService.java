package enchere.service;

import enchere.dao.*;
import enchere.env.*;
import enchere.model.*;
import enchere.ui.*;
import java.sql.*;

public class UserService {

    private final Connection connection;
    private final UserDAO UserDAO;

    public UserService(Connection connection) {
        this.UserDAO = new UserDAO(connection);
        this.connection = connection;
    }

    public User creerUser()
    {
        String email = FonctionUI.displayQuestion("Connexion", "Email", "");

        if(UserDAO.emailExist(email))
        {
            FonctionUI.nextErrorMessage = "l'email existe déjà";
            return null;
        }

        String lastName = FonctionUI.displayQuestion("Connexion", "LastName", "");
        String firstName = FonctionUI.displayQuestion("Connexion", "FirstName", "");
        String addresse = FonctionUI.displayQuestion("Connexion", "addresse", "");


        User user = UserDAO.creerUser(email, lastName, firstName, addresse);
        if(user == null)
        {
            System.out.println("Erreur lors de la création du compte");
            return null;
        }
        System.out.print("Compte créé avec succès");
        return user;
    }

    /**
     * Renvoie l'email si la connexion est réussie et null sinon
     * @return
     */
    public User connecterUser()
    {
       
        String email = FonctionUI.displayQuestion("Connexion", "Email","");
        

        User user = UserDAO.getUser(email);
        if(user == null)
        {
            MenuManager.ErrorMessage = "l'email n'existe pas";
            return null;
        }
        System.out.print("Compte connecté avec succès");
        return user;
    }


}

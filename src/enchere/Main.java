package enchere;

import enchere.ui.*;
import enchere.dao.*;
import java.sql.Connection;
import java.sql.SQLException;



public class Main {
    public static void main(String[] args) {
        
        Connection connection = DatabaseConnection.getConnection();
        MenuManager menuManager = new MenuManager(connection);
        menuManager.afficherMenuPrincipal();

    }
}

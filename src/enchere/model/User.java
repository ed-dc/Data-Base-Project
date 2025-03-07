package enchere.model;

public class User {

    private String email;
    private String lastName;
    private String firstName;
    private String adress;

    public User(String email, String lastName, String firstName, String adress) {
        this.email = email;
        this.lastName = lastName;
        this.firstName = firstName;
        this.adress = adress;
    }

   

    public String getEmail() {
        return email;
    }

    public String getLastName() {
        return lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getAdress() {
        return adress;
    }



    @Override
    public String toString() {
        return "User{" +
              
                ", email='" + email + '\'' +
                ", lastName='" + lastName + '\'' +
                ", firstName='" + firstName + '\'' +
                ", adress='" + adress + '\'' +
                '}';
    }
    
}

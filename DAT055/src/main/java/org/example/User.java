package org.example;

/**
 * Representerar en användare i chattapplikationen.
 * Denna klass lagrar information om användarens användarnamn.
 */
public class User {
    //private String id;
    private String username;


    /**
     * Skapar en ny användare med angivet användarnamn.
     * Användarnamnet används för att identifiera användaren i chatten
     *
     * @param username Användarens användarnamn
     */
    public User(String username) {
        // this.id = id;
        this.username = username;
    }

    /**
     * Hämtar användarens användarnamn
     * Användarnamnet används för att identifiera användaren
     *
     * @return Användarens användarnamn som en sträng.
     */
    public String getUsername() {
        return username;
    }
}
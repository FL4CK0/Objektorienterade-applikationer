package org.example;

public class Controller {



    public static void main(String[] args) {
        HostNetwork server = new HostNetwork();
        UserNetwork client = new UserNetwork();

        // Optionally run these on separate threads if both need to be started from the same main method
        new Thread(server::startServer).start();
        new Thread(client::connectToServer).start();
    }
}
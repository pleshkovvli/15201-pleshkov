package ru.nsu.ccfit.pleshkov.lab3.client;

public class ClientMain {

    public static void main(String[] args) {
        Client client = new Client();
        client.setGui(new ClientGUI(client, client::addChatMessage, client::addLogoutMessage, client::addListMessage, client::endIt));
    }
}

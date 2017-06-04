package ru.nsu.ccfit.pleshkov.lab3;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

public class ClientMain {
    final static private String CLIENT_NAME = "pleshkov.client";

    public static void main(String[] args) throws InterruptedException {
        try {
            Client client = new Client();
            client.setGui(new ClientGUI());
            while(true) {
                Socket socket = new Socket(InetAddress.getLocalHost(),Server.PORT);
                client.setHandler(new ClientXMLMessagesHandler(socket, CLIENT_NAME));
                client.getHandler().addMessageObserver(new MessageObserver<ServerMessage>() {
                    @Override
                    public void update(ServerMessage message) {
                        message.process(client);
                    }
                });
                client.getGui().init(
                        client.getHandler()::addLoginMessage,
                        client.getHandler()::addChatMessage,
                        client.getHandler()::addLogoutMessage,
                        client.getHandler()::addListMessage);
                client.getHandler().begin("Writer", "Reader");
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(0);
        }
    }
}

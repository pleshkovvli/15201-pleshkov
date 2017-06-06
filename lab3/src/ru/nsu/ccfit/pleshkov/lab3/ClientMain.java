package ru.nsu.ccfit.pleshkov.lab3;

import java.io.IOException;
import java.net.Socket;

public class ClientMain {
    final static private String XML_CLIENT_NAME = "pleshkov.xml-client";
    final static private String OBJECTS_CLIENT_NAME = "pleshkov.objects-client";

    static private boolean loop = true;

    public static void main(String[] args) {
        if(args.length == 0) {
            System.out.println("Usage: java -jar Client.jar /path/config.txt");
        }
        final Config config;
        try {
            config = ConfigParser.parse(args[0]);
        } catch (BadParsingException e) {
            System.err.println(e.getMessage());
            return;
        }
        Client client = new Client();
        client.setGui(new ClientGUI());
        Thread myThread = Thread.currentThread();
        while(loop) {
            client.getGui().init(new Observer() {
                private boolean in = true;

                @Override
                public void update(String message) {
                    try {
                        if(in) {
                            Socket socket = new Socket(config.getAddress(),config.getPort());
                            if(config.getType().equals("xml")) {
                                client.setHandler(new ClientXMLMessagesHandler(socket, XML_CLIENT_NAME,client));
                            } else {
                                client.setHandler(new ClientObjectMessagesHandler(socket,OBJECTS_CLIENT_NAME,client));
                            }
                            client.getHandler().addMessageObserver(new MessageObserver<ServerMessage>() {
                                @Override
                                public void update(ServerMessage message) {
                                    message.process(client);
                                }
                            });
                            client.begin();
                            in = false;
                        }
                        client.addLoginMessage(message);
                    } catch (IOException e) {
                        loop = false;
                    }
                }
            },
                    client::addChatMessage,
                    client::addLogoutMessage,
                    client::addListMessage,
                    client::endIt,
                    () -> {
                        loop = false;
                        myThread.interrupt();
                    });
            client.waitEnd();
        }
    }
}

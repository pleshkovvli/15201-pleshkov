package ru.nsu.ccfit.pleshkov.lab3.client;

import org.junit.Assert;
import org.junit.Test;
import ru.nsu.ccfit.pleshkov.lab3.BadParsingException;
import ru.nsu.ccfit.pleshkov.lab3.MessageObserver;
import ru.nsu.ccfit.pleshkov.lab3.Observer;
import ru.nsu.ccfit.pleshkov.lab3.messages.ServerMessage;
import ru.nsu.ccfit.pleshkov.lab3.server.Server;
import sun.util.resources.cldr.ms.CalendarData_ms_MY;

import java.io.IOException;
import java.net.Socket;

import static org.junit.Assert.*;

public class ClientMainTest {
    @Test
    public void test() throws InterruptedException {
        Server.main(new String[0]);

        Thread myThread = Thread.currentThread();
        Config config;
        try {
            config = ConfigParser.parse("config/config-xml");
        } catch (BadParsingException e) {
            Assert.fail();
            return;
        }
        Client client = new Client();
        client.setGui(new ClientGUI());
        try {
            client.getGui().init(new Observer() {
                                     private boolean in = true;
                                     @Override
                                     public void update(String message) {
                                         try {
                                             if(in) {
                                                 Socket socket = new Socket(config.getAddress(),config.getPort());
                                                 if(config.getType().equals("xml")) {
                                                     client.setHandler(new ClientXMLMessagesHandler(socket, "xml",client));
                                                 } else {
                                                     client.setHandler(new ClientObjectMessagesHandler(socket,"onj",client));
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
                                             client.getGui().connectionError();
                                         }
                                     }
                                 },
                    client::addChatMessage,
                    client::addLogoutMessage,
                    client::addListMessage,
                    client::endIt,
                    () -> {
                        //myThread.interrupt();
                    });
            client.getGui().forceLogin("xml");
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail();
        }

        Config config1;
        try {
            config1 = ConfigParser.parse("config/config-obj");
        } catch (BadParsingException e) {
            Assert.fail();
            return;
        }

        Client objclient = new Client();
        objclient.setGui(new ClientGUI());
        try {
            objclient.getGui().init(new Observer() {
                                        private boolean in = true;
                                        @Override
                                        public void update(String message) {
                                            try {
                                                if(in) {
                                                    Socket socket = new Socket(config1.getAddress(),config1.getPort());
                                                    if(config1.getType().equals("xml")) {
                                                        objclient.setHandler(new ClientXMLMessagesHandler(socket, "xml",objclient));
                                                    } else {
                                                        objclient.setHandler(new ClientObjectMessagesHandler(socket,"obj",objclient));
                                                    }
                                                    objclient.getHandler().addMessageObserver(new MessageObserver<ServerMessage>() {
                                                        @Override
                                                        public void update(ServerMessage message) {
                                                            message.process(objclient);
                                                        }
                                                    });
                                                    objclient.begin();
                                                    in = false;
                                                }
                                                objclient.addLoginMessage(message);
                                            } catch (IOException e) {
                                                objclient.getGui().connectionError();
                                            }
                                        }
                                    },
                    objclient::addChatMessage,
                    objclient::addLogoutMessage,
                    objclient::addListMessage,
                    objclient::endIt,
                    () -> {
                        //myThread.interrupt();
                    });
            objclient.getGui().forceLogin("obj");
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail();
        }
        Thread.sleep(2000);
        for(int i = 0; i < 1000; i++) {
            objclient.getHandler().addChatMessage(String.valueOf(i));
            client.getHandler().addChatMessage(String.valueOf(i));
        }
        client.waitEnd();
        objclient.waitEnd();
    }
}
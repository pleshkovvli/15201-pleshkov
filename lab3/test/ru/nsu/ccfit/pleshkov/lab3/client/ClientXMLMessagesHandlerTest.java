package ru.nsu.ccfit.pleshkov.lab3.client;

import org.junit.Assert;
import org.junit.Test;
import ru.nsu.ccfit.pleshkov.lab3.BadParsingException;
import ru.nsu.ccfit.pleshkov.lab3.server.Server;

import java.io.IOException;
import java.net.Socket;

import static org.junit.Assert.*;

public class ClientXMLMessagesHandlerTest {
    @Test
    public void test() {
        Server.main(new String[0]);

        final Config config;
        try {
            config = ConfigParser.parse("config/config1");
        } catch (BadParsingException e) {
            Assert.fail();
            return;
        }
        Client client = new Client();
        try {
            Socket socket = new Socket(config.getAddress(),2000);
            client.setHandler(new ClientXMLMessagesHandler(socket, "xml", client));
        } catch (IOException e) {
            e.printStackTrace();
            Assert.fail();
        }
    }
}
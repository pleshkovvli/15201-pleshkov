package ru.nsu.ccfit.pleshkov.lab3.client;

import org.junit.Test;
import ru.nsu.ccfit.pleshkov.lab3.server.Server;

import java.net.InetAddress;

public class ClientMainTest {
    @Test
    public void test() throws InterruptedException {
        Server.main(new String[0]);
        Client xml = new Client();
        xml.setGui(new ClientGUI(xml, xml::addChatMessage, xml::addLogoutMessage,
                xml::addListMessage, xml::endIt));
        xml.forceLogin(new Config("XML", 2000, InetAddress.getLoopbackAddress(), "xml"));
        Client obj = new Client();
        obj.setGui(new ClientGUI(obj, obj::addChatMessage, obj::addLogoutMessage,
                obj::addListMessage, obj::endIt));
        obj.forceLogin(new Config("OBJ", 3000, InetAddress.getLoopbackAddress(), "objects"));
        Thread.sleep(5000);
        for(int i = 0; i < 1000; ++i) {
            StringBuilder message = new StringBuilder();
            for(int j = 0; j < 100; ++j) {
                message.append(String.valueOf(i * j));
                if(i % 7 == 0) {
                    message.append("  ");
                }
                if(i % 5 == 0) {
                    message.append("\n");
                }
                if(i % 3 == 0) {
                    message.append("\\n");
                }
            }
            xml.addChatMessage(message.toString());
            StringBuilder anotherMessage = new StringBuilder();
            for(int j = 0; j < 100; ++j) {
                if(i % 7 == 0) {
                    anotherMessage.append("  ");
                }
                if(i % 5 == 0) {
                    anotherMessage.append("\n");
                }
                if(i % 3 == 0) {
                    anotherMessage.append("\\n");
                }
                anotherMessage.append(String.valueOf(i * j));
            }
            obj.addChatMessage(anotherMessage.toString());
        }
        xml.addChatMessage("finish");
        obj.addChatMessage("finish");
        Thread.sleep(30000);
    }
}
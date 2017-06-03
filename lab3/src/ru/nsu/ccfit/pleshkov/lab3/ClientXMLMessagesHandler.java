package ru.nsu.ccfit.pleshkov.lab3;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.ArrayList;

class ClientXMLMessagesHandler extends ClientMessagesHandler {

    private DataInputStream messagesReader;
    private DataOutputStream messagesWriter;

    ClientXMLMessagesHandler(Socket socket) throws IOException {
        super(socket);
        messagesWriter = new DataOutputStream(socket.getOutputStream());
        messagesWriter.flush();
        messagesReader = new DataInputStream(socket.getInputStream());
    }


    @Override
    protected void endWriting() {

    }

    @Override
    protected void endReading() {
        try {
            messagesReader.close();
            if(!getSocket().isClosed()) {
                getSocket().close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected ServerMessage readMessage() throws IOException, FailedReadException {
        try {
            DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            int length = messagesReader.readInt();
            byte[] bytes = new byte[length];
            int read = 0;
            while (read < length) {
                read += messagesReader.read(bytes,read,length - read);
            }
            //System.out.println(new String(bytes));
            Document document = builder.parse(new InputSource(
                    new InputStreamReader(new ByteArrayInputStream(bytes),"UTF-8")));
            String type = document.getDocumentElement().getTagName();
            if(type.equals("error")) {
                return new ServerErrorMessage(document.getElementsByTagName("reason").item(0).getTextContent());
            }
            if(type.equals("event")) {
                String event = document.getDocumentElement().getAttribute("name");
                if(event.equals("message")) {
                    return new ServerChatMessage(document.getElementsByTagName("message").item(0).getTextContent(),
                            document.getElementsByTagName("name").item(0).getTextContent());
                }
                if(event.equals("userlogin")) {
                    return new ServerUserloginMessage(document.getElementsByTagName("name").item(0).getTextContent());
                }
                if(event.equals("userlogout")) {
                    return new ServerUserlogoutMessage(document.getElementsByTagName("name").item(0).getTextContent());
                }
            }
            if(type.equals("success")) {
                NodeList list = document.getElementsByTagName("user");
                if(list.getLength() == 0) {
                    list = document.getElementsByTagName("session");
                    if(list.getLength() > 0) {
                        return new ServerSuccessLoginMessage(Integer.valueOf(list.item(0).getTextContent()));
                    }
                    return new ServerSuccessMessage();
                }
                ArrayList<User> listusers = new ArrayList<>();
                for(int i = 0; i < list.getLength(); i++) {
                    listusers.add(new User(list.item(i).getChildNodes().item(0).getTextContent(),
                            list.item(i).getChildNodes().item(1).getTextContent()));
                }
                return new ServerSuccessListMessage(listusers);
            }
        } catch (ParserConfigurationException e) {
            throw new FailedReadException(e);
        } catch (SAXException e) {
            e.printStackTrace();
        }
        throw new FailedReadException();
    }

    Document doc;

    protected void process(ClientChatMessage message) {
        Element event = doc.createElement("command");
        Attr eventName = doc.createAttribute("name");
        eventName.setValue("message");
        event.setAttributeNode(eventName);
        doc.appendChild(event);
        Element messageText = doc.createElement("message");
        messageText.setTextContent(message.getMessage());
        Element sender = doc.createElement("session");
        sender.setTextContent(String.valueOf(message.getSessionID()));
        event.appendChild(messageText);
        event.appendChild(sender);
    }

    protected void process(ClientListMessage message) {
        Element event = doc.createElement("command");
        Attr eventName = doc.createAttribute("name");
        eventName.setValue("list");
        event.setAttributeNode(eventName);
        doc.appendChild(event);
        Element sender = doc.createElement("session");
        sender.setTextContent(String.valueOf(message.getSessionID()));
        event.appendChild(sender);
    }

    protected void process(ClientLoginMessage message) {
        Element event = doc.createElement("command");
        Attr eventName = doc.createAttribute("name");
        eventName.setValue("login");
        event.setAttributeNode(eventName);
        doc.appendChild(event);
        Element sender = doc.createElement("name");
        sender.setTextContent(message.getUserName());
        event.appendChild(sender);
        Element type = doc.createElement("type");
        type.setTextContent(message.getClientName());
        event.appendChild(type);
    }

    protected void process(ClientLogoutMessage message) {
        Element event = doc.createElement("command");
        Attr eventName = doc.createAttribute("name");
        eventName.setValue("logout");
        event.setAttributeNode(eventName);
        doc.appendChild(event);
        Element sender = doc.createElement("session");
        sender.setTextContent(String.valueOf(message.getSessionID()));
        event.appendChild(sender);
    }

    @Override
    protected void writeMessage(ClientMessage message) throws IOException {
        DocumentBuilder builder;
        try {
            builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
            return;
        }
        doc = builder.newDocument();
        message.process(this);
        StringWriter sw = new StringWriter();
        try {
            TransformerFactory tf = TransformerFactory.newInstance();
            Transformer transformer = tf.newTransformer();
            transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
            transformer.transform(new DOMSource(doc), new StreamResult(sw));
        }  catch (TransformerException e) {
            e.printStackTrace();
        }
        //System.out.println(sw.toString());
        ByteBuffer utfMessage = Charset.forName("UTF-8").encode(sw.toString());
        messagesWriter.writeInt(utfMessage.position());
        byte[] bytes = new byte[utfMessage.position()];
        utfMessage.get(bytes,0,utfMessage.position());
        messagesWriter.write(bytes);
    }
}

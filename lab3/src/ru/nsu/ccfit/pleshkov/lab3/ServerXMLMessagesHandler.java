package ru.nsu.ccfit.pleshkov.lab3;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

class ServerXMLMessagesHandler extends ServerMessagesHandler implements ServerMessagesProcessor {
    private DataInputStream messagesReader;
    private DataOutputStream messagesWriter;

    ServerXMLMessagesHandler(Socket socket) throws IOException {
        super(socket);
        messagesWriter = new DataOutputStream(socket.getOutputStream());
        messagesWriter.flush();
        messagesReader = new DataInputStream(socket.getInputStream());
    }

    @Override
    protected void close() {
        try {
            messagesWriter.close();
            messagesReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected ClientMessage readMessage() throws IOException, FailedReadException {
        ClientMessage message;
        try {
            DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            int length = messagesReader.readInt();
            byte[] bytes = new byte[length];
            int read = 0;
            while (read < length) {
                read += messagesReader.read(bytes,read,length - read);
            }
            Document document = builder.parse(new InputSource(
                    new InputStreamReader(new ByteArrayInputStream(bytes),"UTF-8")));
            String type = document.getDocumentElement().getAttribute("name");
            if(type.equals("message")) {
                message =  new ClientChatMessage(document.getElementsByTagName("message").item(0).getTextContent(),
                        Integer.valueOf(document.getElementsByTagName("session").item(0).getTextContent()));
            } else
            if(type.equals("list")) {
                message =  new ClientListMessage(Integer.valueOf(document.getElementsByTagName("session").item(0).getTextContent()));
            } else
            if(type.equals("login")) {
                message =  new ClientLoginMessage(document.getElementsByTagName("name").item(0).getTextContent(),
                        document.getElementsByTagName("type").item(0).getTextContent());
            } else
            if(type.equals("logout")) {
                message =  new ClientLogoutMessage(Integer.valueOf(document.getElementsByTagName("session").
                        item(0).getTextContent()));
            } else {
                throw new FailedReadException("Unknown type");
            }
        } catch (ParserConfigurationException | SAXException e) {
            throw new FailedReadException(e);
        }
        getLogger().info("Reading message " + message.getClass().getSimpleName());
        return message;
    }

    private Document doc;

    @Override
    public void process(ServerErrorMessage message) {
        Element error = doc.createElement("error");
        doc.appendChild(error);
        Element reason = doc.createElement("reason");
        reason.setTextContent(message.getReason());
        error.appendChild(reason);
    }

    @Override
    public void process(ServerChatMessage message) {
        Element event = doc.createElement("event");
        Attr eventName = doc.createAttribute("name");
        eventName.setValue("message");
        event.setAttributeNode(eventName);
        doc.appendChild(event);
        Element messageText = doc.createElement("message");
        messageText.setTextContent(message.getMessage());
        Element sender = doc.createElement("name");
        sender.setTextContent(message.getName());
        event.appendChild(messageText);
        event.appendChild(sender);
    }

    @Override
    public void process(ServerUserloginMessage message) {
        Element event = doc.createElement("event");
        Attr eventName = doc.createAttribute("name");
        eventName.setValue("userlogin");
        event.setAttributeNode(eventName);
        doc.appendChild(event);
        Element sender = doc.createElement("name");
        sender.setTextContent(message.getName());
        event.appendChild(sender);
        Element type = doc.createElement("type");
        type.setTextContent(message.getType());
        event.appendChild(type);
    }

    @Override
    public void process(ServerUserlogoutMessage message) {
        Element event = doc.createElement("event");
        Attr eventName = doc.createAttribute("name");
        eventName.setValue("userlogout");
        event.setAttributeNode(eventName);
        doc.appendChild(event);
        Element sender = doc.createElement("name");
        sender.setTextContent(message.getName());
        event.appendChild(sender);
    }

    @Override
    public void process(ServerSuccessLoginMessage message) {
        Element success = doc.createElement("success");
        int id = message.getSessionID();
        Element session = doc.createElement("session");
        session.setTextContent(String.valueOf(id));
        success.appendChild(session);
        doc.appendChild(success);
    }

    @Override
    public void process(ServerSuccessMessage message) {
        Element success = doc.createElement("success");
        doc.appendChild(success);
    }

    @Override
    public void process(ServerSuccessListMessage message) {
        Element success = doc.createElement("success");
        doc.appendChild(success);
        Element listusers = doc.createElement("listusers");
        ArrayList<User> list = message.getListusers();
        for (User user : list) {
            Element userElement = doc.createElement("user");
            Element name = doc.createElement("name");
            Element type = doc.createElement("type");
            name.setTextContent(user.getName());
            type.setTextContent(user.getType());
            userElement.appendChild(name);
            userElement.appendChild(type);
            listusers.appendChild(userElement);
        }
        success.appendChild(listusers);
    }

    @Override
    protected void writeMessage(ServerMessage message) throws IOException {
        DocumentBuilder builder;
        try {
            builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
            return;
        }
        doc = builder.newDocument();
        message.process(this);
        ByteArrayOutputStream ba = new ByteArrayOutputStream();
        try {
            TransformerFactory tf = TransformerFactory.newInstance();
            Transformer transformer = tf.newTransformer();
            transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
            transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
            transformer.transform(new DOMSource(doc), new StreamResult(ba));
        }  catch (TransformerException e) {
            e.printStackTrace();
        }
        messagesWriter.writeInt(ba.size());
        messagesWriter.write(ba.toByteArray());
        getLogger().info("Writing message " + message.getClass().getSimpleName());
    }

    @Override
    protected void endWriting() {
        try {
            super.endWriting();
            messagesReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void endReading() {
        super.endReading();
    }
}

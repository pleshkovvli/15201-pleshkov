package ru.nsu.ccfit.pleshkov.lab3.server;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import ru.nsu.ccfit.pleshkov.lab3.FailedReadException;
import ru.nsu.ccfit.pleshkov.lab3.ServerMessagesProcessor;
import ru.nsu.ccfit.pleshkov.lab3.UnknownMessageException;
import ru.nsu.ccfit.pleshkov.lab3.User;
import ru.nsu.ccfit.pleshkov.lab3.messages.*;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.List;

class ServerXMLMessagesHandler extends ServerMessagesHandler implements ServerMessagesProcessor {
    private DataInputStream messagesReader;
    private DataOutputStream messagesWriter;
    private DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

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


    private String getByName(Document document, String name) throws UnknownMessageException {
        Node element = document.getElementsByTagName(name).item(0);
        if(element == null) {
            throw new UnknownMessageException("No " + name + "was found");
        }
        return element.getTextContent();
    }

    @Override
    protected ClientMessage readMessage() throws IOException, FailedReadException, UnknownMessageException {
        ClientMessage message;
        try {
            DocumentBuilder builder = factory.newDocumentBuilder();
            int length = messagesReader.readInt();
            if(length <= 0) {
                throw new FailedReadException();
            }
            byte[] bytes = new byte[length];
            int read = 0;
            while (read < length) {
                try {
                    int result = messagesReader.read(bytes,read,length - read);
                    if(result < 0) {
                        throw new FailedReadException();
                    }
                    read += result;
                } catch (SocketTimeoutException e) {
                    if(getSocket().isClosed())  {
                        throw e;
                    }
                }
            }
            Document document = builder.parse(new InputSource(
                    new InputStreamReader(new ByteArrayInputStream(bytes,0,length),"UTF-8")));
            String type = document.getDocumentElement().getAttribute("name");

            switch (type) {
                case "message": message = new ClientChatMessage(getByName(document,"message"),
                        Integer.valueOf(getByName(document,"session"))); break;
                case "list": message = new ClientListMessage(Integer.valueOf(getByName(document,"session"))); break;
                case "login": message = new ClientLoginMessage(getByName(document,"name"), getByName(document,"type"));
                    break;
                case "logout": message =  new ClientLogoutMessage(Integer.valueOf(getByName(document,"session"))); break;
                default: throw new FailedReadException("Unknown type");
            }
        } catch (SAXException e) {
            throw new UnknownMessageException(e.getMessage());
        } catch (ParserConfigurationException e) {
            throw new FailedReadException(e);
        } catch (NullPointerException e) {
            e.printStackTrace();
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
        Element reason = doc.createElement("message");
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
        List<User> list = message.getListusers();
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
            builder = factory.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
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
}

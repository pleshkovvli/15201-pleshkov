package ru.nsu.ccfit.pleshkov.lab3.client;

import org.w3c.dom.*;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import ru.nsu.ccfit.pleshkov.lab3.*;
import ru.nsu.ccfit.pleshkov.lab3.messages.*;

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
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;

class ClientXMLMessagesHandler extends ClientMessagesHandler implements ClientMessagesProcessor {

    private DataInputStream messagesReader;
    private DataOutputStream messagesWriter;
    private DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

    ClientXMLMessagesHandler(Socket socket, String clientName, Client client) throws IOException {
        super(socket,clientName,client);
        messagesWriter = new DataOutputStream(socket.getOutputStream());
        messagesWriter.flush();
        messagesReader = new DataInputStream(socket.getInputStream());
    }


    @Override
    protected void endWriting() {

    }

    @Override
    protected void endReading() {

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

    private String getByName(Element mainElement, String name) throws UnknownMessageException {
        Node element = mainElement.getElementsByTagName(name).item(0);
        if(element == null) {
            throw new UnknownMessageException("No " + name + "was found");
        }
        return element.getTextContent();
    }

    @Override
    protected ServerMessage readMessage() throws IOException, FailedReadException, UnknownMessageException {
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
            String type = document.getDocumentElement().getTagName();
            //String me = new String(bytes,0,length,"UTF-8");
            //System.out.println(me);
            if(type.equals("error")) {
                return new ServerErrorMessage(getByName(document,"message"));
            }
            if(type.equals("event")) {
                String event = document.getDocumentElement().getAttribute("name");
                if(event.equals("message")) {
                    return new ServerChatMessage(getByName(document,"message"), getByName(document,"name"));
                }
                if(event.equals("userlogin")) {
                    String clientType;
                    Node typeNode = document.getElementsByTagName("type").item(0);
                    if(typeNode == null) {
                        clientType = "unknown";
                    } else {
                        clientType = typeNode.getTextContent();
                    }
                    return new ServerUserloginMessage(getByName(document,"name"), clientType);
                }
                if(event.equals("userlogout")) {
                    return new ServerUserlogoutMessage(getByName(document,"name"));
                }
            }
            if(type.equals("success")) {
                NodeList list = document.getElementsByTagName("user");
                if(list.getLength() == 0) {
                    Node session = document.getElementsByTagName("session").item(0);
                    if(session != null) {
                        return new ServerSuccessLoginMessage(Integer.valueOf(session.getTextContent()));
                    }
                    return new ServerSuccessMessage();
                }
                ArrayList<User> listusers = new ArrayList<>();
                for(int i = 0; i < list.getLength(); i++) {
                    Element user = (Element) list.item(i);
                    listusers.add(new User(getByName(user,"name"),getByName(user,"type")));
                }
                return new ServerSuccessListMessage(listusers);
            }
            throw new UnknownMessageException(type);
        } catch (SAXException e) {
            throw new UnknownMessageException(e.getMessage());
        } catch (ParserConfigurationException e) {
            throw new FailedReadException(e);
        } catch (NullPointerException e) {
            e.printStackTrace();
            throw new FailedReadException(e);
        }
    }

    private Document doc;

    @Override
    public void process(ClientChatMessage message) {
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

    @Override
    public void process(ClientListMessage message) {
        Element event = doc.createElement("command");
        Attr eventName = doc.createAttribute("name");
        eventName.setValue("list");
        event.setAttributeNode(eventName);
        doc.appendChild(event);
        Element sender = doc.createElement("session");
        sender.setTextContent(String.valueOf(message.getSessionID()));
        event.appendChild(sender);
    }

    @Override
    public void process(ClientLoginMessage message) {
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

    @Override
    public void process(ClientLogoutMessage message) {
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
            return;
        }
        doc = builder.newDocument();
        message.process(this);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try {
            TransformerFactory tf = TransformerFactory.newInstance();
            Transformer transformer = tf.newTransformer();
            transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
            transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
            transformer.transform(new DOMSource(doc), new StreamResult(byteArrayOutputStream));
        }  catch (TransformerException e) {
            e.printStackTrace();
        }
        messagesWriter.writeInt(byteArrayOutputStream.size());
        messagesWriter.write(byteArrayOutputStream.toByteArray());
    }
}

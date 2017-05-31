package ru.nsu.ccfit.pleshkov.lab3;

import com.sun.org.apache.regexp.internal.RE;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
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

public class ClientXMLMessagesHandler extends ClientMessagesHandler {

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
    protected void initReading() throws IOException, InterruptedException, FailedReadException {
        try {
            String type = "";
            Document document = null;
            while(!type.equals("success")) {
                DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
                int length = messagesReader.readInt();
                byte[] bytes = new byte[length];
                int read = 0;
                while (read < length) {
                    read += messagesReader.read(bytes,read,length - read);
                }
                System.out.println(new String(bytes));
                document = builder.parse(new ByteArrayInputStream(bytes));
                type = document.getDocumentElement().getTagName();
                if(type.equals("error")) {
                    Client.getGui().errorText("ERROR: " + document.getElementsByTagName("reason").item(0).getTextContent());
                }
            }
            setSessionID(Integer.valueOf(document.getElementsByTagName("session").item(0).getTextContent()));
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        }
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
    protected Message readMessage() throws IOException, FailedReadException {
        try {
            DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            int length = messagesReader.readInt();
            byte[] bytes = new byte[length];
            int read = 0;
            while (read < length) {
                read += messagesReader.read(bytes,read,length - read);
            }
            System.out.println(new String(bytes));
            Document document = builder.parse(new ByteArrayInputStream(bytes));
            String type = document.getDocumentElement().getTagName();
            if(type.equals("error")) {
                return new Message(document.getElementsByTagName("reason").item(0).getTextContent(), MessageType.ERROR);
            }
            if(type.equals("event")) {
                String event = document.getDocumentElement().getAttribute("name");
                if(event.equals("message")) {
                    return new Message(document.getElementsByTagName("message").item(0).getTextContent(),
                            MessageType.MESSAGE,document.getElementsByTagName("name").item(0).getTextContent());
                }
                if(event.equals("userlogin")) {
                    return new Message(document.getElementsByTagName("name").item(0).getTextContent(),MessageType.USERLOGIN);
                }
                if(event.equals("userlogout")) {
                    return new Message(document.getElementsByTagName("name").item(0).getTextContent(),MessageType.USERLOGOUT);
                }
            }
            if(type.equals("success")) {
                NodeList list = document.getElementsByTagName("user");
                if(list.getLength() == 0) {
                    return new Message("",MessageType.SUCCESS);
                }
                StringBuilder stringBuilder = new StringBuilder();
                for(int i = 0; i < list.getLength(); i++) {
                    stringBuilder.append(list.item(i).getTextContent());
                    stringBuilder.append("\n");
                }
                return new Message(stringBuilder.toString(),MessageType.LIST);
            }
        } catch (ParserConfigurationException e) {
            throw new FailedReadException(e);
        } catch (SAXException e) {
            e.printStackTrace();
        }
        throw new FailedReadException();
    }

    @Override
    protected void writeMessage(Message message) throws IOException {
        DocumentBuilder builder;
        try {
            builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
            return;
        }
        Document doc = builder.newDocument();
        if(message.getType() == MessageType.MESSAGE) {
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
        if(message.getType() == MessageType.LIST) {
            Element event = doc.createElement("command");
            Attr eventName = doc.createAttribute("name");
            eventName.setValue("list");
            event.setAttributeNode(eventName);
            doc.appendChild(event);
            Element sender = doc.createElement("session");
            sender.setTextContent(String.valueOf(message.getSessionID()));
            event.appendChild(sender);
        }
        if(message.getType() == MessageType.LOGIN) {
            Element event = doc.createElement("command");
            Attr eventName = doc.createAttribute("name");
            eventName.setValue("login");
            event.setAttributeNode(eventName);
            doc.appendChild(event);
            Element sender = doc.createElement("name");
            sender.setTextContent(message.getMessage());
            event.appendChild(sender);
        }
        if(message.getType() == MessageType.LOGOUT) {
            Element event = doc.createElement("command");
            Attr eventName = doc.createAttribute("name");
            eventName.setValue("logout");
            event.setAttributeNode(eventName);
            doc.appendChild(event);
            Element sender = doc.createElement("session");
            sender.setTextContent(String.valueOf(message.getSessionID()));
            event.appendChild(sender);
        }
        StringWriter sw = new StringWriter();
        try {
            TransformerFactory tf = TransformerFactory.newInstance();
            Transformer transformer = tf.newTransformer();
            //transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
            //transformer.setOutputProperty(OutputKeys.METHOD, "xml");
            //transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            //transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
            transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
            transformer.transform(new DOMSource(doc), new StreamResult(sw));
        }  catch (TransformerException e) {
            e.printStackTrace();
        }
        System.out.println(sw.toString());
        messagesWriter.writeInt(sw.toString().length());
        messagesWriter.writeBytes(sw.toString());
    }
}

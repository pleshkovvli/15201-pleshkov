package ru.nsu.ccfit.pleshkov.lab3;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.net.Socket;

class ServerXMLMessagesHandler extends ServerMessagesHandler {
    private DataInputStream messagesReader;
    private DataOutputStream messagesWriter;

    ServerXMLMessagesHandler(Socket socket) throws IOException {
        super(socket);
        messagesWriter = new DataOutputStream(socket.getOutputStream());
        messagesWriter.flush();
        messagesReader = new DataInputStream(socket.getInputStream());
        //BufferedReader r = new BufferedReader(new InputStreamReader(socket.getInputStream()));
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
            //System.out.println(new String(bytes));
            Document document = builder.parse(new ByteArrayInputStream(bytes));
            String type = document.getDocumentElement().getAttribute("name");
            if(type.equals("message")) {
                return new Message(document.getElementsByTagName("message").item(0).getTextContent(),
                        MessageType.MESSAGE,Integer.valueOf(document.getElementsByTagName("session").item(0).getTextContent()));
            }
            if(type.equals("list")) {
                return new Message("",MessageType.LIST,Integer.valueOf(document.getElementsByTagName("session").item(0).getTextContent()));
            }
            if(type.equals("login")) {
                return new Message(document.getElementsByTagName("name").item(0).getTextContent(),MessageType.LOGIN, 0);
            }
            if(type.equals("logout")) {
                return new Message("",MessageType.LOGOUT,
                        Integer.valueOf(document.getElementsByTagName("session").item(0).getTextContent()));
            }
        } catch (ParserConfigurationException e) {
            throw new FailedReadException(e);
        } catch (SAXException e) {
            e.printStackTrace();
        }
        return null;
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
        if(message.getType() == MessageType.ERROR) {
            Element error = doc.createElement("error");
            doc.appendChild(error);
            Element reason = doc.createElement("reason");
            reason.setTextContent(message.getMessage());
            error.appendChild(reason);
        }
        if(message.getType() == MessageType.MESSAGE) {
            Element event = doc.createElement("event");
            Attr eventName = doc.createAttribute("name");
            eventName.setValue("message");
            event.setAttributeNode(eventName);
            doc.appendChild(event);
            Element messageText = doc.createElement("message");
            messageText.setTextContent(message.getMessage());
            Element sender = doc.createElement("name");
            sender.setTextContent(message.getSender());
            event.appendChild(messageText);
            event.appendChild(sender);
        }
        if(message.getType() == MessageType.USERLOGIN) {
            Element event = doc.createElement("event");
            Attr eventName = doc.createAttribute("name");
            eventName.setValue("userlogin");
            event.setAttributeNode(eventName);
            doc.appendChild(event);
            Element sender = doc.createElement("name");
            sender.setTextContent(message.getMessage());
            event.appendChild(sender);
        }
        if(message.getType() == MessageType.USERLOGOUT) {
            Element event = doc.createElement("event");
            Attr eventName = doc.createAttribute("name");
            eventName.setValue("userlogout");
            event.setAttributeNode(eventName);
            doc.appendChild(event);
            Element sender = doc.createElement("name");
            sender.setTextContent(message.getMessage());
            event.appendChild(sender);
        }
        if(message.getType() == MessageType.SUCCESS) {
            Element success = doc.createElement("success");
            String id = message.getMessage();
            if(id != null) {
                Element session = doc.createElement("session");
                session.setTextContent(message.getMessage());
                success.appendChild(session);
            }
            doc.appendChild(success);
        }
        if(message.getType() == MessageType.LIST) {
            Element success = doc.createElement("success");
            doc.appendChild(success);
            Element listusers = doc.createElement("listusers");
            String list = message.getMessage();
            int i = 0;
            while(!list.isEmpty()) {
                i++;
                String userName = list.substring(0,list.indexOf('$'));
                Element user = doc.createElement("user");
                Element name = doc.createElement("name");
                Element type = doc.createElement("type");
                name.setTextContent(userName);
                type.setTextContent(String.valueOf(i));
                user.appendChild(name);
                user.appendChild(type);
                listusers.appendChild(user);
                list = list.substring(list.indexOf('$') + 1);
            }
            success.appendChild(listusers);
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
        //System.out.println(sw.toString());
        messagesWriter.writeInt(sw.toString().length());
        messagesWriter.writeBytes(sw.toString());
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
        try {
            super.endReading();
            messagesWriter.close();
            if(!getSocket().isClosed()) {
                getSocket().close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

package ru.nsu.ccfit.pleshkov.lab3;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

class ClientObjectMessagesHandler extends ClientMessagesHandler {

    ClientObjectMessagesHandler(Socket socket) throws IOException {
        super(socket);
        messagesReader = new ObjectInputStream(socket.getInputStream());
        messagesWriter = new ObjectOutputStream(socket.getOutputStream());
    }


    private ObjectInputStream messagesReader;
    private ObjectOutputStream messagesWriter;

    @Override
    protected void endWriting() {
        try {
            messagesWriter.close();
            if(!getSocket().isClosed()) {
                getSocket().close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void initReading() throws IOException, InterruptedException, FailedReadException {
        try {
            Message message =  (Message) messagesReader.readObject();
            while(message.getType() != MessageType.SUCCESS) {
                if(message.getType() == MessageType.ERROR) {
                    Client.getGui().errorText("ERROR: " + message.getMessage());
                }
                message =  (Message) messagesReader.readObject();
            }
            setSessionID(Integer.valueOf(message.getMessage()));
        } catch (ClassNotFoundException e)  {
            throw new FailedReadException(e);
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
            return (Message) messagesReader.readObject();
        } catch (ClassNotFoundException e)  {
            throw new FailedReadException(e);
        }
    }

    @Override
    protected void writeMessage(Message message) throws IOException {
        messagesWriter.writeObject(message);
        if(message.getType() == MessageType.LOGOUT) {
            endIt();
        }
    }
}

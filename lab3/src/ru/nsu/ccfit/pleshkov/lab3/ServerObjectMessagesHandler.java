package ru.nsu.ccfit.pleshkov.lab3;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

class ServerObjectMessagesHandler extends ServerMessagesHandler {

    private ObjectInputStream messagesReader;
    private ObjectOutputStream messagesWriter;

    ServerObjectMessagesHandler(Socket socket) throws IOException {
        super(socket);
        messagesWriter = new ObjectOutputStream(socket.getOutputStream());
        messagesWriter.flush();
        messagesReader = new ObjectInputStream(socket.getInputStream());
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
        super.readMessage();
        try {
            return (ClientMessage) messagesReader.readObject();
        } catch (ClassNotFoundException e)  {
            throw new FailedReadException(e);
        }
    }

    @Override
    protected void writeMessage(ServerMessage message) throws IOException {
        super.writeMessage(message);
        messagesWriter.writeObject(message);
    }

    @Override
    protected void endWriting() {
        super.endWriting();
    }

    @Override
    protected void endReading() {
        super.endReading();
    }
}

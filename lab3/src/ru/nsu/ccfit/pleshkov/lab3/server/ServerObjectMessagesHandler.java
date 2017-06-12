package ru.nsu.ccfit.pleshkov.lab3.server;

import ru.nsu.ccfit.pleshkov.lab3.base.FailedReadException;
import ru.nsu.ccfit.pleshkov.lab3.messages.ClientMessage;
import ru.nsu.ccfit.pleshkov.lab3.messages.ServerMessage;

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
        try {
            ClientMessage message = (ClientMessage) messagesReader.readObject();
            getLogger().info("Reading message " + message.getClass().getSimpleName());
            return message;
        } catch (ClassNotFoundException e)  {
            throw new FailedReadException(e);
        }
    }

    @Override
    protected void writeMessage(ServerMessage message) throws IOException {
        messagesWriter.writeObject(message);
        getLogger().info("Writing message " + message.getClass().getSimpleName());
    }
}

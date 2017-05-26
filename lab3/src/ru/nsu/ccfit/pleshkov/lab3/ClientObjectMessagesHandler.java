package ru.nsu.ccfit.pleshkov.lab3;

import java.io.*;
import java.net.Socket;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class ClientObjectMessagesHandler extends MessagesHandler {
    public BlockingQueue<Message> getQueue() {
        return queue;
    }

    public ClientObjectMessagesHandler(Socket socket) throws IOException {
        super(socket);
        messagesReader = new ObjectInputStream(socket.getInputStream());
        messagesWriter = new ObjectOutputStream(socket.getOutputStream());
    }

    BlockingQueue<Message> queue = new ArrayBlockingQueue<Message>(100);

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    private Client client;

    BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
    BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(System.out));
    ObjectInputStream messagesReader;
    ObjectOutputStream messagesWriter;
    @Override
    protected void endWriting() {
        try {
            writer.close();
            messagesReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void initWriting() throws IOException, FailedWriteException {
        try {
            client.setLogin(queue.take().getMessage());
            client.getGui().startMessages();
        } catch (InterruptedException e) {
            return;
        }
        writeMessage(new Message(client.getLogin(),MessageType.LOGIN,client.getLogin()));
    }

    @Override
    protected void initReading() throws IOException, FailedReadException {
        try {
            Message message =  (Message) messagesReader.readObject();
            if(message.getType() != MessageType.SUCCESS) {
                throw new FailedReadException();
            }
        } catch (ClassNotFoundException e)  {
            throw new FailedReadException(e);
        }
    }

    @Override
    protected void endReading() {
        try {
            reader.close();
            messagesWriter.close();
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
    }

    @Override
    protected Message getMessage() throws IOException {
        try {
            return queue.take();
        } catch (InterruptedException e) {
            return null;
        }
    }

    @Override
    protected void handleMessage(Message message) throws IOException {
        client.getGui().updateText(message.getSender() +": " +message.getMessage() + "\n");
        //writer.write(message.getSender() +": " +message.getMessage() + "\n");
        //writer.flush();
    }
};
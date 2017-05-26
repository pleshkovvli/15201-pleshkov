package ru.nsu.ccfit.pleshkov.lab3;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;

public class ClientGUI extends JFrame {
    private JTextArea history;
    private JTextField message;
    private JPanel Panel;
    private JLabel MessageText;
    private MessageForm messageForm;
    private Messages messages;

    public ClientGUI(Observer messagesObserver) {
        setBounds(400, 200, 600, 500);
        setContentPane(Panel);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        messageForm = new MessageForm(message);
        messageForm.addObserver(messagesObserver);
        messages = new Messages(history);
        setVisible(true);
    }

    public void startMessages() {
        messageForm.form.removeActionListener(messageForm.form.getActionListeners()[0]);
        messageForm.form.addActionListener((ActionEvent e) -> {
            messageForm.currentMessage = messageForm.form.getText();
            updateText("∽ - " + messageForm.currentMessage);
            messageForm.form.setText("");
            messageForm.notifyObservers();
        });
    }

    private class Messages {
        private JTextArea messages;
        Messages(JTextArea messages) {
            this.messages = messages;
        }

        public void updateText(String mes) {
            messages.setText(messages.getText() + "\n" + mes);
        }
    }

    public void updateText(String mes) {
        messages.updateText(mes);
    }

    private class MessageForm implements Observable {
        public MessageForm(JTextField form) {
            this.form = form;
            form.addActionListener((ActionEvent e) -> {
                currentMessage = form.getText();
                //updateText("∽ - " + currentMessage);
                form.setText("");
                notifyObservers();
            });
        }

        private ArrayList<Observer> observers = new ArrayList<>();

        private JTextField form;

        private String currentMessage;

        @Override
        public void removeObserver(Observer observer) {
            if(observers.contains(observer)) {
                observers.remove(observer);
            }
        }

        @Override
        public void addObserver(Observer observer) {
            observers.add(observer);
        }

        @Override
        public void notifyObservers() {
            for(Observer observer : observers) {
                observer.update(currentMessage);
            }
        }

    }
}

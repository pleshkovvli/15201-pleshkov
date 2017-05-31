package ru.nsu.ccfit.pleshkov.lab3;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;

public class ClientGUI extends JFrame {
    private JTextArea messagesHistory;
    private JTextField message;
    private JPanel Panel;
    private JLabel messageText;
    private JButton logoutButton;
    private JButton listButton;
    private JTextField loginText;
    private JButton loginButton;
    private ClickButton logoutClick;
    private ClickButton listClick;
    private ClickButton loginClick;
    private MessageForm messageForm;
    private Messages messages;

    ClientGUI() {
        setContentPane(Panel);
        setBounds(400, 200, 600, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        listButton.setText("List");
        logoutButton.setText("Logout");
        loginButton.setText("Login");
        messageText.setText("Login");
        messageForm = new MessageForm(message);
        messages = new Messages(messagesHistory);
        logoutClick = new ClickButton(logoutButton) {
            @Override
            public void notifyObservers() {
                for(Observer observer : observers) {
                    observer.update("",MessageType.LOGOUT);
                }
            }
        };
        loginClick = new ClickButton(loginButton) {
            @Override
            public void notifyObservers() {
                String text = loginText.getText();
                if ((text != null) && (!text.isEmpty())) {
                    for(Observer observer : observers) {
                        observer.update(text, MessageType.LOGIN);
                    }
                }
            }
        };
        listClick = new ClickButton(listButton) {
            @Override
            public void notifyObservers() {
                for(Observer observer : observers) {
                    observer.update("", MessageType.LIST);
                }
            }
        };
        setVisible(true);
    }

    void init(Observer messagesObserver) {
        messageForm.addObserver(messagesObserver);
        logoutClick.addObserver(messagesObserver);
        loginClick.addObserver(messagesObserver);
        listClick.addObserver(messagesObserver);
    }

    void startMessages() {
        messageText.setText(messageForm.currentMessage);
        messageForm.form.addActionListener((ActionEvent e) -> {
            messageForm.currentMessage = messageForm.form.getText();
            updateText("∽ - " + messageForm.currentMessage);
            messageForm.form.setText("");
            messageForm.notifyObservers();
        });
    }

    abstract class ClickButton implements Observable {
        ArrayList<Observer> observers = new ArrayList<>();

        ClickButton(JButton button) {
            this.button = button;
            button.addActionListener((ActionEvent e) -> notifyObservers());
        }

        private JButton button;

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
    }

    private class Messages {
        private JTextArea messages;
        Messages(JTextArea messages) {
            this.messages = messages;
        }

        void updateText(String mes) {
            messages.setText(messages.getText() + "\n" + mes);
        }

        void errorText(String mes) {
            messages.setText(messages.getText() + "\n" + mes);
        }
    }

    void updateText(String mes) {
        messages.updateText(mes);
    }
    void errorText(String mes) {
        messages.errorText(mes);
    }

    private class MessageForm implements Observable {
        MessageForm(JTextField form) {
            this.form = form;
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
                observer.update(currentMessage, MessageType.MESSAGE);
            }
        }

    }
}

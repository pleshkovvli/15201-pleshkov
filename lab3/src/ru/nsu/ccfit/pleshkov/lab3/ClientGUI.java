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
    private LoginClickButton loginClick;
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
        logoutClick = new ClickButton(logoutButton);
        loginClick = new LoginClickButton(loginButton);
        listClick = new ClickButton(listButton);
        setVisible(true);
    }

    void init(Observer messagesObserver, Observer loginObserver,
              SimpleObserver logoutObserver, SimpleObserver listObserver) {
        messageForm.addObserver(messagesObserver);
        logoutClick.addSimpleObserver(logoutObserver);
        loginClick.addObserver(loginObserver);
        listClick.addSimpleObserver(listObserver);
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

    private class LoginClickButton implements Observable {
        ArrayList<Observer> observers = new ArrayList<>();

        LoginClickButton(JButton button) {
            this.button = button;
            button.addActionListener((ActionEvent e) -> notifyObservers());
        }

        private JButton button;

        @Override
        public void notifyObservers() {
            for(Observer observer : observers) {
                observer.update(loginText.getText());
            }
        }

        @Override
        public void removeObserver(Observer observer) {
            observers.remove(observer);
        }

        @Override
        public void addObserver(Observer observer) {
            observers.add(observer);
        }
    }

    private class ClickButton implements SimpleObservable {
        ArrayList<SimpleObserver> observers = new ArrayList<>();

        ClickButton(JButton button) {
            this.button = button;
            button.addActionListener((ActionEvent e) -> notifySimpleObservers());
        }

        private JButton button;

        @Override
        public void notifySimpleObservers() {
            for(SimpleObserver observer : observers) {
                observer.update();
            }
        }

        @Override
        public void removeSimpleObserver(SimpleObserver observer) {
            observers.remove(observer);
        }

        @Override
        public void addSimpleObserver(SimpleObserver observer) {
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
                observer.update(currentMessage);
            }
        }

    }
}

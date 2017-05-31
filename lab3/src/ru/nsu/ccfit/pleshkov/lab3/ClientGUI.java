package ru.nsu.ccfit.pleshkov.lab3;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;

public class ClientGUI extends JFrame {
    private JTextArea history;
    private JTextField message;
    private JPanel Panel;
    private JLabel MessageText;
    private JButton logoutButton;
    private JButton listButton;
    private JTextField loginText;
    private LogoutButton button;
    private LogoutButton list;
    private MessageForm messageForm;
    private Messages messages;

    public JTextArea getHistory() {
        return history;
    }

    public LogoutButton getButton() {
        return button;
    }

    ClientGUI() {
        setContentPane(Panel);
        setBounds(400, 200, 600, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        listButton.setText("List");
        messageForm = new MessageForm(message);
        messages = new Messages(history);
        button = new LogoutButton(logoutButton) {
            @Override
            public void notifyObservers() {
                for(Observer observer : observers) {
                    if(isLogin) {
                        if ((loginText.getText() != null) && (!loginText.getText().isEmpty())) {
                            observer.update(loginText.getText(), MessageType.LOGIN);
                        }
                    } else {
                        observer.update("",MessageType.LOGOUT);
                    }
                }
            }
        };
        list = new LogoutButton(listButton) {
            @Override
            public void notifyObservers() {
                for(Observer observer : observers) {
                    observer.update("", MessageType.LIST);
                }
            }
        };
        setVisible(true);
    }

    void init(Observer messagesObserver, Observer initObserver) {
        messageForm.addObserver(messagesObserver);

        button.addObserver(messagesObserver);
        list.addObserver(messagesObserver);
    }

    void startMessages() {
        MessageText.setText(messageForm.currentMessage);
        logoutButton.setText("Logout");
        messageForm.form.addActionListener((ActionEvent e) -> {
            messageForm.currentMessage = messageForm.form.getText();
            updateText("∽ - " + messageForm.currentMessage);
            messageForm.form.setText("");
            messageForm.notifyObservers();
        });
    }

    abstract class LogoutButton implements Observable {
        ArrayList<Observer> observers = new ArrayList<>();

        public boolean isLogin() {
            return isLogin;
        }

        public void setLogin(boolean login) {
            if(login) {
                button.setText("Login");
            } else {
                button.setText("Logout");
            }
            isLogin = login;
        }

        protected boolean isLogin = true;

        LogoutButton(JButton button) {
            this.button = button;
            button.setText("");
            button.addActionListener((ActionEvent e) -> {
                notifyObservers();
            });
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
    }

    void updateText(String mes) {
        messages.updateText(mes);
    }

    private class MessageForm implements Observable {
        MessageForm(JTextField form) {
            this.form = form;
            MessageText.setText("Login");
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

package ru.nsu.ccfit.pleshkov.lab3;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.concurrent.ArrayBlockingQueue;

public class ClientGUI extends JFrame implements ClientInterface {
    private JPanel Panel;
    private JTextField currentMessage;
    private JButton logoutButton;
    private JTextPane previousMessages;
    private JTextPane userlist;
    private JLabel loginText;
    private LogoutButton logoutClick;
    private MessageForm messageForm;
    private Messages messages;
    private LoginDialog dialog;
    private Userlist list;
    private SimpleObserver listObserver;

    ClientGUI() {
        setContentPane(Panel);
        setBounds(400, 200, 600, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        logoutButton.setText("Logout");
        messageForm = new MessageForm(currentMessage);
        logoutClick = new LogoutButton(logoutButton);
        messages = new Messages(previousMessages);
        list = new Userlist(userlist);
    }

    void init(Observer loginObserver, Observer messagesObserver,
              SimpleObserver logoutObserver, SimpleObserver listObserver) {
        messageForm.clearObservers();
        messageForm.addObserver(messagesObserver);
        dialog = new LoginDialog(ClientGUI.this);
        dialog.addObserver(loginObserver);
        logoutClick.clearObservers();
        logoutClick.addSimpleObserver(logoutObserver);
        this.listObserver = listObserver;
    }

    void startMessages() {
        dialog.dispose();
        listObserver.update();
        this.setVisible(true);
        messageForm.addAction((ActionEvent e) -> {
            messageForm.currentText = messageForm.form.getText();
            messages.updateText(messageForm.currentText);
            messageForm.form.setText("");
            messageForm.notifyObservers();
        });
    }

    private class LogoutButton extends ClickButton {
        LogoutButton(JButton button) {
            super(button);
        }

        @Override
        public void notifySimpleObservers() {
            super.notifySimpleObservers();
            list.clear();
            ClientGUI.this.setVisible(false);
        }

        void clearObservers() {
            Iterator<SimpleObserver> iterator = observers.listIterator(0);
            while (iterator.hasNext()) {
                iterator.next();
                iterator.remove();
            }
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


    private class Userlist {
        private JTextPane users;
        private StyledDocument document;
        private SimpleAttributeSet userAttributes = new SimpleAttributeSet();

        private LinkedList<User> list = new LinkedList<>();

        Userlist(JTextPane users) {
            this.users = users;
            document = users.getStyledDocument();
        }

        void getUser(String user, String type) {
            try {
                String text = user + " via " + type +"\n"  ;
                list.add(new User(user,type));
                document.insertString(document.getLength(),text,userAttributes);
            } catch (BadLocationException e) {
                e.printStackTrace();
            }
        }

        void clear() {
            users.setText("");
        }

        void getList(String mes) {
            Iterator<User> iterator = list.listIterator(0);
            while (iterator.hasNext()) {
                iterator.next();
                iterator.remove();
            }
            BufferedReader reader = new BufferedReader(new StringReader(mes));
            String line = "";
            while (line != null) {
                try {
                    line = reader.readLine();
                    if(line != null) {
                        String user = line.substring(0,line.lastIndexOf("$"));
                        String type = line.substring(line.lastIndexOf("$") + 1);
                        getUser(user,type);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    return;
                }
            }
        }

        void removeUser(String mes) {
            try {
                int length = 0;
                Iterator<User> iterator = list.listIterator(0);
                User user = iterator.next();
                String userName = user.getName();
                if(mes.equals(userName)) {
                    document.remove(length, userName.length() + user.getType().length() + 6);
                    iterator.remove();
                    return;
                }
                while (iterator.hasNext()) {
                    length += userName.length() + user.getType().length() + 6;
                    user = iterator.next();
                    userName = user.getName();
                    if(mes.equals(userName)) {
                        document.remove(length, userName.length() + user.getType().length() + 6);
                        iterator.remove();
                        break;
                    }
                }

            } catch (BadLocationException e) {
                e.printStackTrace();
            }
        }
    }

    private class Messages {
        private JTextPane messages;
        private StyledDocument document;
        private SimpleAttributeSet messageAttributes = new SimpleAttributeSet();
        private SimpleAttributeSet errorAttributes = new SimpleAttributeSet();


        private LimitedQueue<String> messagesQueue = new LimitedQueue<>(10);

        Messages(JTextPane messages) {
            this.messages = messages;
            document = messages.getStyledDocument();
            StyleConstants.setForeground(errorAttributes,Color.RED);
        }

        void updateText(String mes) {
            try {
                String text = mes + "\n";
                String last = messagesQueue.preemptiveAdd(text);
                if(last != null) {
                    shrinkText(last.length());
                }
                document.insertString(document.getLength(),text,messageAttributes);
            } catch (BadLocationException e) {
                e.printStackTrace();
            }
        }

        void shrinkText(int length) {
            try {
                document.remove(0,length);
            } catch (BadLocationException e) {
                e.printStackTrace();
            }
        }

        void errorText(String mes) {
            try {
                String text = mes + "\n";
                String last = messagesQueue.preemptiveAdd(text);
                if(last != null) {
                    shrinkText(last.length());
                }
                document.insertString(document.getLength(),mes + "\n",errorAttributes);
            } catch (BadLocationException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void getMessage(String message, String sender)  {
        messages.updateText(sender + ": " + message);
    }
    @Override
    public void getError(String error) {
        messages.errorText(error);
    }
    @Override
    public void getList(String listString) {
        list.getList(listString);
    }

    @Override
    public void getUserlogin(String user, String type) {
        list.getUser(user,type);
    }

    @Override
    public void getUserlogout(String user) {
        list.removeUser(user);
    }

    private class MessageForm implements Observable {
        MessageForm(JTextField form) {
            this.form = form;
        }

        private ArrayList<Observer> observers = new ArrayList<>();

        private final Object lock = new Object();

        private JTextField form;

        private String currentText;
        private ArrayList<ActionListener> listeners = new ArrayList<>();

        void addAction(ActionListener listener) {
            listeners.add(listener);
            form.addActionListener(listener);
        }

        @Override
        public void removeObserver(Observer observer) {
            synchronized (lock) {
                if(observers.contains(observer)) {
                    observers.remove(observer);
                }
            }
        }

        @Override
        public void addObserver(Observer observer) {
            synchronized (lock) {
                observers.add(observer);
            }
        }

        @Override
        public void notifyObservers() {
            synchronized (lock) {
                for(Observer observer : observers) {
                    observer.update(currentText);
                }
            }
        }

        void clearObservers() {
            synchronized (lock) {
                Iterator<Observer> iterator = observers.listIterator(0);
                while (iterator.hasNext()) {
                    iterator.next();
                    iterator.remove();
                }
                Iterator<ActionListener> listenerIterator = listeners.listIterator(0);
                while (listenerIterator.hasNext()) {
                    form.removeActionListener(listenerIterator.next());
                    listenerIterator.remove();
                }
            }
        }
    }

    private class LimitedQueue<E> extends ArrayBlockingQueue<E> {
        LimitedQueue(int limit) {
            super(limit);
        }

        final private Object lock = new Object();

        E preemptiveAdd(E o) {
            synchronized (lock) {
                E out = null;
                if(remainingCapacity() == 0) {
                    out = super.remove();
                }
                super.add(o);
                return out;
            }
        }
    }
}

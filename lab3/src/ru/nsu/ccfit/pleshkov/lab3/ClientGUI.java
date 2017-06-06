package ru.nsu.ccfit.pleshkov.lab3;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Semaphore;

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
    private SimpleObserver fin;
    private Semaphore semaphore;

    private BlockingQueue<String> sendedMessages = new ArrayBlockingQueue<>(10);

    ClientGUI() {
        setContentPane(Panel);
        setBounds(400, 200, 600, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        logoutButton.setText("Logout");
        messageForm = new MessageForm(currentMessage);
        logoutClick = new LogoutButton(logoutButton);
        messages = new Messages(previousMessages);
        list = new Userlist(userlist);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                logoutClick.notifySimpleObservers();
            }
        });
    }

    void init(Observer loginObserver, Observer messagesObserver,
              SimpleObserver logoutObserver, SimpleObserver listObserver,
              SimpleObserver fin, SimpleObserver terminate) {
        this.fin = fin;
        messageForm.clearObservers();
        messageForm.addObserver((String message) -> SwingUtilities.invokeLater(() -> messagesObserver.update(message)));
        dialog = new LoginDialog(() -> {
            terminate.update();
            this.dispose();
        });
        dialog.addObserver((String message) -> SwingUtilities.invokeLater(() -> {
            loginObserver.update(message);
            loginText.setText(message);
        }));
        logoutClick.clearObservers();
        //logoutClick.addSimpleObserver(() -> SwingUtilities.invokeLater(logoutObserver::update));
        logoutClick.addSimpleObserver(logoutObserver);
        this.listObserver = listObserver;
    }

    void startMessages() {
        semaphore = new Semaphore(0);
        dialog.dispose();
        listObserver.update();
        this.setVisible(true);
    }

    private class LogoutButton extends ClickButton {
        LogoutButton(JButton button) {
            super(button);
        }

        @Override
        public void notifySimpleObservers() {
            super.notifySimpleObservers();
            try {
                semaphore.acquire();
                //semaphore.tryAcquire(1000, TimeUnit.MILLISECONDS);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            list.clear();
            ClientGUI.this.setVisible(false);
            fin.update();
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
            button.addActionListener((ActionEvent e) -> notifySimpleObservers());
        }

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
                String text = user + " via " + type + "\n";
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

        void removeUser(String removingUser) {
            try {
                int length = 0;
                Iterator<User> iterator = list.listIterator(0);
                User user = iterator.next();
                String userName = user.getName();
                if(removingUser.equals(userName)) {
                    document.remove(length, userName.length() + user.getType().length() + 6);
                    iterator.remove();
                    return;
                }
                while (iterator.hasNext()) {
                    length += userName.length() + user.getType().length() + 6;
                    user = iterator.next();
                    userName = user.getName();
                    if(removingUser.equals(userName)) {
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
        private StyledDocument document;
        private SimpleAttributeSet messageAttributes = new SimpleAttributeSet();
        private SimpleAttributeSet errorAttributes = new SimpleAttributeSet();

        private LimitedQueue<String> messagesQueue = new LimitedQueue<>(10);

        Messages(JTextPane messages) {
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
    public void showSuccess() {
        if(sendedMessages.size() > 0) {
            try {
                messages.updateText(sendedMessages.take());
            }  catch (InterruptedException e) {
                e.printStackTrace();
            }
        } else {
            semaphore.release();
        }
    }

    @Override
    public void showMessage(String message, String sender)  {
        messages.updateText(sender + ": " + message);
    }

    @Override
    public void showError(String error) {
        messages.errorText(error);
    }

    @Override
    public void showList(String listString) {
        list.getList(listString);
    }

    @Override
    public void showUserlogin(String user, String type) {
        list.getUser(user,type);
    }

    @Override
    public void showUserlogout(String user) {
        list.removeUser(user);
    }

    private class MessageForm implements Observable {
        MessageForm(JTextField form) {
            this.form = form;
            addAction((ActionEvent e) -> {
                currentText = form.getText();
                sendedMessages.add(currentText);
                //messages.updateText(messageForm.currentText);
                messageForm.form.setText("");
                messageForm.notifyObservers();
            });
        }

        private ArrayList<Observer> observers = new ArrayList<>();

        private JTextField form;

        private String currentText;

        void addAction(ActionListener listener) {
            form.addActionListener(listener);
        }

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
                observer.update(currentText);
            }
        }

        void clearObservers() {
            Iterator<Observer> iterator = observers.listIterator(0);
            while (iterator.hasNext()) {
                iterator.next();
                iterator.remove();
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

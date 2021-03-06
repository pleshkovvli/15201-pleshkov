package ru.nsu.ccfit.pleshkov.lab3.client;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;
import ru.nsu.ccfit.pleshkov.lab3.base.*;

import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class ClientGUI extends JFrame implements ClientInterface {
    private JPanel Panel;
    private JScrollPane currentMessage;
    private JButton logoutButton;
    private JTextPane previousMessagesText;
    private JTextPane userListText;
    private JTextArea currentMessageText;
    private JScrollPane previousMessages;
    private JScrollPane userList;
    private JButton reconnectButton;
    private ClickButton reconnect;
    private LogoutButton logoutClick;
    private MessageForm messageForm;
    private Messages messagesHistory;
    private LoginDialog dialog;
    private Userlist connectedUsers;
    private SimpleObserver listObserver;
    private Config login;

    static final private String WINDOW_NAME = "Chat";


    ClientGUI(Observer<Config> start, Observer<String> messagesObserver,
              SimpleObserver logoutObserver, SimpleObserver listObserver, SimpleObserver fin) {
        setContentPane(Panel);
        setBounds(400, 200, 600, 500);
        messageForm = new MessageForm(currentMessageText);
        logoutClick = new LogoutButton(logoutButton);
        messagesHistory = new Messages(previousMessagesText);
        connectedUsers = new Userlist(userListText);
        messageForm.addObserver((String message) -> SwingUtilities.invokeLater(() -> messagesObserver.update(message)));
        dialog = new LoginDialog(() -> SwingUtilities.invokeLater(() -> {
            fin.update();
            this.dispose();
        }));
        dialog.addObserver((Config config) -> SwingUtilities.invokeLater(() -> {
            login = config;
            start.update(config);
        }));
        reconnect = new ClickButton(reconnectButton);
        reconnect.addSimpleObserver(() -> SwingUtilities.invokeLater(() -> start.update(login)));
        reconnectButton.setText("Reconnect");
        reconnectButton.setVisible(false);
        logoutClick.addSimpleObserver(() -> SwingUtilities.invokeLater(logoutObserver::update));
        this.listObserver = listObserver;
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                logoutClick.notifySimpleObservers();
            }
        });
    }

    @Override
    public void forceLogin(Config config) {
        dialog.forceLogin(config);
    }

    @Override
    public void startMessaging() {
        dialog.setVisible(false);
        messagesHistory.clearText();
        SwingUtilities.invokeLater(listObserver::update);
        setTitle(WINDOW_NAME + ": " + login.getName());
        this.setVisible(true);
    }

    {
// GUI initializer generated by IntelliJ IDEA GUI Designer
// >>> IMPORTANT!! <<<
// DO NOT EDIT OR ADD ANY CODE HERE!
        $$$setupUI$$$();
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        Panel = new JPanel();
        Panel.setLayout(new GridLayoutManager(8, 4, new Insets(0, 0, 0, 0), -1, -1));
        final Spacer spacer1 = new Spacer();
        Panel.add(spacer1, new GridConstraints(1, 3, 2, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_FIXED, 1, null, new Dimension(20, 10), null, 0, false));
        final Spacer spacer2 = new Spacer();
        Panel.add(spacer2, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_FIXED, 1, null, new Dimension(10, 10), null, 0, false));
        logoutButton = new JButton();
        logoutButton.setText("Button");
        Panel.add(logoutButton, new GridConstraints(6, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, 1, 1, null, new Dimension(200, 30), new Dimension(200, 30), 0, false));
        final Spacer spacer3 = new Spacer();
        Panel.add(spacer3, new GridConstraints(7, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(10, 10), null, 0, false));
        final Spacer spacer4 = new Spacer();
        Panel.add(spacer4, new GridConstraints(4, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(10, 10), null, 0, false));
        currentMessage = new JScrollPane();
        Panel.add(currentMessage, new GridConstraints(5, 2, 2, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, new Dimension(300, 70), null, 0, false));
        currentMessageText = new JTextArea();
        currentMessageText.setLineWrap(true);
        currentMessageText.setWrapStyleWord(true);
        currentMessage.setViewportView(currentMessageText);
        previousMessages = new JScrollPane();
        Panel.add(previousMessages, new GridConstraints(1, 2, 3, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, new Dimension(300, 350), null, 0, false));
        previousMessagesText = new JTextPane();
        previousMessages.setViewportView(previousMessagesText);
        userList = new JScrollPane();
        Panel.add(userList, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, new Dimension(150, 350), null, 0, false));
        userListText = new JTextPane();
        userListText.setText("");
        userList.setViewportView(userListText);
        reconnectButton = new JButton();
        reconnectButton.setText("Button");
        Panel.add(reconnectButton, new GridConstraints(5, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(200, 30), new Dimension(200, 30), 0, false));
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return Panel;
    }

    private class LogoutButton extends ClickButton {
        LogoutButton(JButton button) {
            super(button);
            logoutButton.setText("Logout");
        }

        @Override
        public void notifySimpleObservers() {
            super.notifySimpleObservers();
        }
    }

    private class ClickButton implements SimpleObservable {
        ArrayList<SimpleObserver> observers = new ArrayList<>();

        ClickButton(JButton button) {
            button.addActionListener((ActionEvent e) -> notifySimpleObservers());
        }

        @Override
        public void notifySimpleObservers() {
            observers.forEach(SimpleObserver::update);
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
            users.setEditorKit(new WrapEditorKit());
            users.addFocusListener(new FocusListener() {

                @Override
                public void focusLost(FocusEvent e) {
                    users.setEditable(true);

                }

                @Override
                public void focusGained(FocusEvent e) {
                    users.setEditable(false);

                }
            });
            this.users = users;
            document = users.getStyledDocument();
        }

        void addUser(String user, String type) {
            try {
                Iterator<User> iterator = list.listIterator(0);
                while (iterator.hasNext()) {
                    User current = iterator.next();
                    if (current.getType().equals(type) && current.getName().equals(user)) {
                        return;
                    }
                }
                String text = user + " via " + type + "\n";
                document.insertString(document.getLength(), text, userAttributes);
                list.add(new User(user, type));
            } catch (BadLocationException e) {
                showError("Failed to change list");
            }
        }

        void clear() {
            Iterator<User> iterator = list.listIterator(0);
            while (iterator.hasNext()) {
                iterator.next();
                iterator.remove();
            }
            users.setText("");
        }

        void setList(java.util.List<? extends UserElement> list) {
            clear();
            Iterator<? extends UserElement> iterator = list.listIterator(0);
            while (iterator.hasNext()) {
                UserElement userElement = iterator.next();
                addUser(userElement.getName(), userElement.getType());
            }
        }

        void removeUser(String removingUser) {
            try {
                int length = 0;
                Iterator<User> iterator = list.listIterator(0);
                if (!iterator.hasNext()) {
                    return;
                }
                User user = iterator.next();
                String userName = user.getName();
                if (removingUser.equals(userName)) {
                    document.remove(length, userName.length() + user.getType().length() + 6);
                    iterator.remove();
                    return;
                }
                while (iterator.hasNext()) {
                    length += userName.length() + user.getType().length() + 6;
                    user = iterator.next();
                    userName = user.getName();
                    if (removingUser.equals(userName)) {
                        document.remove(length, userName.length() + user.getType().length() + 6);
                        iterator.remove();
                        break;
                    }
                }
            } catch (BadLocationException e) {
                showError("Failed to change list");
            }
        }
    }

    private class WrapEditorKit extends StyledEditorKit {
        ViewFactory defaultFactory = new WrapColumnFactory();

        public ViewFactory getViewFactory() {
            return defaultFactory;
        }

    }

    private class WrapColumnFactory implements ViewFactory {
        public View create(Element elem) {
            String kind = elem.getName();
            if (kind != null) {
                switch (kind) {
                    case AbstractDocument.ContentElementName:
                        return new WrapLabelView(elem);
                    case AbstractDocument.ParagraphElementName:
                        return new ParagraphView(elem);
                    case AbstractDocument.SectionElementName:
                        return new BoxView(elem, View.Y_AXIS);
                    case StyleConstants.ComponentElementName:
                        return new ComponentView(elem);
                    case StyleConstants.IconElementName:
                        return new IconView(elem);
                }
            }
            return new LabelView(elem);
        }
    }

    private class WrapLabelView extends LabelView {
        WrapLabelView(Element elem) {
            super(elem);
        }

        public float getMinimumSpan(int axis) {
            switch (axis) {
                case View.X_AXIS:
                    return 0;
                case View.Y_AXIS:
                    return super.getMinimumSpan(axis);
                default:
                    throw new IllegalArgumentException("Invalid axis: " + axis);
            }
        }

    }


    private class Messages {

        private JTextPane messages;
        private StyledDocument document;
        private SimpleAttributeSet unhandledMessageAttributes = new SimpleAttributeSet();
        private SimpleAttributeSet failedMessageAttributes = new SimpleAttributeSet();
        private SimpleAttributeSet messageAttributes = new SimpleAttributeSet();
        private SimpleAttributeSet errorAttributes = new SimpleAttributeSet();

        private BlockingQueue<Position> unhandledMessagesQueue = new ArrayBlockingQueue<>(20);

        Messages(JTextPane messages) {
            this.messages = messages;
            messages.addFocusListener(new FocusListener() {

                @Override
                public void focusLost(FocusEvent e) {
                    messages.setEditable(true);

                }

                @Override
                public void focusGained(FocusEvent e) {
                    messages.setEditable(false);

                }
            });
            messages.setEditorKit(new WrapEditorKit());
            DefaultCaret caret = (DefaultCaret) messages.getCaret();
            caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
            document = messages.getStyledDocument();
            StyleConstants.setForeground(errorAttributes, Color.RED);
            StyleConstants.setForeground(unhandledMessageAttributes, Color.GRAY);
            StyleConstants.setForeground(failedMessageAttributes, Color.RED);
            StyleConstants.setForeground(messageAttributes, Color.BLACK);
        }

        void approveMessage() {
            if (!unhandledMessagesQueue.isEmpty()) {
                Position position = unhandledMessagesQueue.poll();
                document.setCharacterAttributes(position.getOffset(), position.getLength(),
                        messageAttributes, true);
            }
        }

        void declineMessage() {
            if (!unhandledMessagesQueue.isEmpty()) {
                Position position = unhandledMessagesQueue.poll();
                document.setCharacterAttributes(position.getOffset(), position.getLength(),
                        failedMessageAttributes, true);
            }
        }

        void printMessage(String mes) {
            try {
                String text = mes + "\n";
                document.insertString(document.getLength(), text, messageAttributes);
            } catch (BadLocationException e) {
                showError("Failed to print message");
            }
        }

        void printMyMessage(String mes) {
            try {
                String text = mes + "\n";
                unhandledMessagesQueue.add(new Position(document.getLength(), text.length()));
                document.insertString(document.getLength(), text, unhandledMessageAttributes);
            } catch (BadLocationException e) {
                showError("Failed to show message");
            }
        }

        void printError(String mes) {
            try {
                String text = mes + "\n";
                document.insertString(document.getLength(), text, errorAttributes);
            } catch (BadLocationException e) {
                e.printStackTrace();
            }
        }

        void clearText() {
            messages.setText("");
        }

        private class Position {
            int getOffset() {
                return offset;
            }

            int getLength() {
                return length;
            }

            private final int offset;

            Position(int offset, int length) {
                this.offset = offset;
                this.length = length;
            }

            private final int length;
        }
    }

    @Override
    public void approveMessage() {
        messagesHistory.approveMessage();
    }

    @Override
    public void declineMessage() {
        messagesHistory.declineMessage();
    }

    @Override
    public void showLoginError(String message) {
        dialog.showError(message);
    }

    @Override
    public void showLogout() {
        this.setVisible(false);
        dialog.setVisible(true);
    }

    @Override
    public void showMessage(String message, String sender) {
        messagesHistory.printMessage(sender + ": " + message);
    }

    @Override
    public void showError(String error) {
        messagesHistory.printError(error);
    }

    @Override
    public void connectionError() {
        new ErrorDialog("Failed to connect with server");
    }

    @Override
    public void connectionBroken() {
        showError("Connection broken");
        reconnectButton.setVisible(true);
    }

    @Override
    public void connectionEstablished() {
        reconnectButton.setVisible(false);
    }

    @Override
    public void showList(java.util.List<? extends UserElement> list) {
        connectedUsers.setList(list);
    }

    @Override
    public void showUserlogin(String user, String type) {
        connectedUsers.addUser(user, type);
    }

    @Override
    public void showUserlogout(String user) {
        connectedUsers.removeUser(user);
    }

    private class MessageForm implements Observable<String> {
        MessageForm(JTextArea form) {
            this.form = form;
            addAction(() -> {
                currentText = form.getText();
                messagesHistory.printMyMessage("~: " + currentText);
                messageForm.notifyObservers();
                messageForm.form.setText("");
            });
        }

        private ArrayList<Observer<String>> observers = new ArrayList<>();

        private JTextArea form;

        private String currentText;
        final static private String NEW_LINE = "NEW_LINE";
        final static private String NEW_MESSAGE = "NEW_MESSAGE";


        private void addAction(SimpleObserver listener) {
            InputMap input = form.getInputMap();
            KeyStroke enter = KeyStroke.getKeyStroke("ENTER");
            KeyStroke shiftEnter = KeyStroke.getKeyStroke("ctrl ENTER");
            input.put(shiftEnter, NEW_LINE);
            input.put(enter, NEW_MESSAGE);

            ActionMap actions = form.getActionMap();
            actions.put(NEW_MESSAGE, new AbstractAction() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    listener.update();
                }
            });
            actions.put(NEW_LINE, new AbstractAction() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    form.append("\n");
                }
            });
        }

        @Override
        public void removeObserver(Observer<String> observer) {
            if (observers.contains(observer)) {
                observers.remove(observer);
            }
        }

        @Override
        public void addObserver(Observer<String> observer) {
            observers.add(observer);
        }

        @Override
        public void notifyObservers() {
            for (Observer<String> observer : observers) {
                observer.update(currentText);
            }
        }
    }
}

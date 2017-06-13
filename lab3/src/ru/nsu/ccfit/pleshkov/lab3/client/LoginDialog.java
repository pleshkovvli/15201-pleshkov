package ru.nsu.ccfit.pleshkov.lab3.client;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import ru.nsu.ccfit.pleshkov.lab3.base.Observable;
import ru.nsu.ccfit.pleshkov.lab3.base.Observer;
import ru.nsu.ccfit.pleshkov.lab3.base.SimpleObserver;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;
import java.awt.*;
import java.awt.event.*;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;

public class LoginDialog extends JDialog implements Observable<Config> {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextField loginField;
    private JRadioButton xmlButton;
    private JRadioButton objectsButton;
    private JTextField ipField;
    private JTextField portField;
    private JLabel errorText;
    private SimpleObserver terminate;
    private ButtonGroup group;

    private Config config;

    final private static String WINDOW_NAME = "Login";

    private ArrayList<Observer<Config>> observers = new ArrayList<>();

    LoginDialog(SimpleObserver terminate) {
        this.terminate = terminate;
        setBounds(400, 200, 250, 150);
        setContentPane(contentPane);
        setModal(false);
        setTitle(WINDOW_NAME);
        getRootPane().setDefaultButton(buttonOK);
        pack();
        loginField.setDocument(new LimitDocument(30));
        ipField.setDocument(new LimitDocument(50));
        portField.setDocument(new LimitDocument(5));
        group = new ButtonGroup();
        group.add(xmlButton);
        xmlButton.setText("XML");
        group.add(objectsButton);
        objectsButton.setText("Objects");
        xmlButton.setSelected(true);
        errorText.setText("");
        errorText.setForeground(Color.RED);
        loginField.setText("login");
        ipField.setText("127.0.0.1");
        portField.setText("2000");
        buttonOK.addActionListener((ActionEvent e) -> onOK());
        loginField.getDocument().addDocumentListener(new DocumentListener() {
            public void changedUpdate(DocumentEvent e) {
                getUsualColor();
            }

            public void removeUpdate(DocumentEvent e) {
                getUsualColor();
            }

            public void insertUpdate(DocumentEvent e) {
                getUsualColor();
            }

            void getUsualColor() {
                setForeground(Color.BLACK);
            }
        });
        buttonCancel.addActionListener((ActionEvent e) -> onCancel());

        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        contentPane.registerKeyboardAction((ActionEvent e) -> onCancel(),
                KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
        setVisible(true);
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
        contentPane = new JPanel();
        contentPane.setLayout(new GridLayoutManager(2, 1, new Insets(10, 10, 10, 10), -1, -1));
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        contentPane.add(panel1, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, 1, null, null, null, 0, false));
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1, true, false));
        panel1.add(panel2, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        buttonOK = new JButton();
        buttonOK.setText("OK");
        panel2.add(buttonOK, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        buttonCancel = new JButton();
        buttonCancel.setText("Cancel");
        panel2.add(buttonCancel, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, 1, 1, null, null, null, 0, false));
        final JPanel panel3 = new JPanel();
        panel3.setLayout(new GridLayoutManager(3, 2, new Insets(0, 0, 0, 0), -1, -1));
        contentPane.add(panel3, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        loginField = new JTextField();
        panel3.add(loginField, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, new Dimension(150, -1), null, 0, false));
        xmlButton = new JRadioButton();
        xmlButton.setText("RadioButton");
        panel3.add(xmlButton, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, 1, 1, null, null, null, 0, false));
        objectsButton = new JRadioButton();
        objectsButton.setText("RadioButton");
        panel3.add(objectsButton, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, 1, 1, null, null, null, 0, false));
        ipField = new JTextField();
        panel3.add(ipField, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(100, -1), null, 0, false));
        errorText = new JLabel();
        errorText.setText("Label");
        panel3.add(errorText, new GridConstraints(2, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        portField = new JTextField();
        panel3.add(portField, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(100, -1), null, 0, false));
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return contentPane;
    }

    private class LimitDocument extends PlainDocument {
        final private int limit;

        LimitDocument(int limit) {
            super();
            this.limit = limit;
        }

        @Override
        public void insertString(int offset, String str, AttributeSet attr) throws BadLocationException {
            if (str == null) return;

            if ((getLength() + str.length()) <= limit) {
                super.insertString(offset, str, attr);
            }
        }
    }

    void forceLogin(Config config) {
        this.config = config;
        notifyObservers();
    }


    private void onOK() {
        String name = loginField.getText();
        StringBuilder message = new StringBuilder("Error: ");
        String type = null;
        boolean failed = false;
        if (xmlButton.isSelected()) {
            type = "xml";
        } else if (objectsButton.isSelected()) {
            type = "objects";
        } else {
            failed = true;
            message.append("type unchecked, ");
        }
        int port = 0;
        try {
            port = Integer.valueOf(portField.getText());
            if (port < 1024 || port > 65535) {
                failed = true;
                message.append("invalid port value, ");
            }
        } catch (NumberFormatException e) {
            failed = true;
            message.append("invalid port format, ");
        }
        InetAddress address = null;
        try {
            address = InetAddress.getByName(ipField.getText());
        } catch (UnknownHostException e) {
            failed = true;
            message.append("invalid host, ");
        }
        if (failed) {
            errorText.setText(message.substring(0, message.length() - 2));
            pack();
        } else {
            errorText.setText("");
            pack();
            config = new Config(name, port, address, type);
            notifyObservers();
        }
    }

    private void onCancel() {
        terminate.update();
        dispose();
    }

    void showError(String message) {
        errorText.setText("Error: " + message);
        pack();
    }

    @Override
    public void notifyObservers() {

        for (Observer<Config> observer : observers) {
            observer.update(config);
        }
    }

    @Override
    public void removeObserver(Observer<Config> observer) {
        observers.remove(observer);
    }

    @Override
    public void addObserver(Observer<Config> observer) {
        observers.add(observer);
    }

}

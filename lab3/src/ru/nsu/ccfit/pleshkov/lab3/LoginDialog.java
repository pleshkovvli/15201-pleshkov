package ru.nsu.ccfit.pleshkov.lab3;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class LoginDialog extends JDialog implements Observable {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextField loginField;
    private SimpleObserver terminate;

    final private static String WINDOW_NAME = "Login";

    private ArrayList<Observer> observers = new ArrayList<>();

    LoginDialog(SimpleObserver terminate) {
        this.terminate = terminate;
        setContentPane(contentPane);
        setModal(false);
        setTitle(WINDOW_NAME);
        getRootPane().setDefaultButton(buttonOK);
        pack();
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

    private void onOK() {
        notifyObservers();
    }

    private void onCancel() {
        terminate.update();
        dispose();
    }

    void showError() {
        loginField.setForeground(Color.RED);
    }

    @Override
    public void notifyObservers() {
        for(Observer observer : observers) {
            observer.update(loginField.getText());
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

package ru.nsu.ccfit.pleshkov.lab3;

import javax.swing.*;
import java.awt.event.*;
import java.util.ArrayList;

public class LoginDialog extends JDialog implements Observable {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextField loginField;
    private SimpleObserver terminate;

    private ArrayList<Observer> observers = new ArrayList<>();

    LoginDialog(SimpleObserver terminate) {
        this.terminate = terminate;
        setContentPane(contentPane);
        setModal(false);
        getRootPane().setDefaultButton(buttonOK);
        pack();
        buttonOK.addActionListener((ActionEvent e) -> onOK());
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

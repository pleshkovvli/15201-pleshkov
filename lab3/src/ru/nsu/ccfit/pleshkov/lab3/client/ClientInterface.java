package ru.nsu.ccfit.pleshkov.lab3.client;

import ru.nsu.ccfit.pleshkov.lab3.UserElement;

import java.util.List;

interface ClientInterface {
    void showMessage(String message, String sender);
    void showError(String error);
    void showList(List<? extends UserElement> list);
    void showSuccess();
    void showUserlogin(String user, String type);
    void showUserlogout(String user);
}

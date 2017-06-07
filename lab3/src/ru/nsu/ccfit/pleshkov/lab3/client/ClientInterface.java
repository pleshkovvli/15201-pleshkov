package ru.nsu.ccfit.pleshkov.lab3.client;

interface ClientInterface {
    void showMessage(String message, String sender);
    void showError(String error);
    void showList(String list);
    void showSuccess();
    void showUserlogin(String user, String type);
    void showUserlogout(String user);
}

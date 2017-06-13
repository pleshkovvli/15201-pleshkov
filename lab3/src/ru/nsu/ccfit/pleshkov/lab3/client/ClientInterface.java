package ru.nsu.ccfit.pleshkov.lab3.client;

import ru.nsu.ccfit.pleshkov.lab3.base.UserElement;

import java.util.List;

interface ClientInterface {
    void showMessage(String message, String sender);
    void showError(String error);
    void showList(List<? extends UserElement> list);
    void showUserlogin(String user, String type);
    void showUserlogout(String user);
    void forceLogin(Config config);
    void connectionError();
    void approveMessage();
    void declineMessage();
    void showLogout();
    void showLoginError(String message);
    void connectionEstablished();
    void connectionBroken();
    void startMessaging();
}

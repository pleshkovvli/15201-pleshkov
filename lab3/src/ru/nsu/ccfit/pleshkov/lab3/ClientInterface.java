package ru.nsu.ccfit.pleshkov.lab3;

interface ClientInterface {
    void getMessage(String message, String sender);
    void getError(String error);
    void getList(String list);
    void getUserlogin(String user, String type);
    void getUserlogout(String user);
}

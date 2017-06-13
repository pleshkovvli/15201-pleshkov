package ru.nsu.ccfit.pleshkov.lab3.base;

import ru.nsu.ccfit.pleshkov.lab3.messages.*;

public interface ServerMessagesProcessor {
    void process(ServerErrorMessage message);
    void process(ServerSuccessLoginMessage message);
    void process(ServerSuccessListMessage message);
    void process(ServerSuccessMessage message);
    void process(ServerChatMessage message);
    void process(ServerUserloginMessage message);
    void process(ServerUserlogoutMessage message);
}

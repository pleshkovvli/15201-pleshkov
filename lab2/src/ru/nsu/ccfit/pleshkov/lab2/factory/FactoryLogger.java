package ru.nsu.ccfit.pleshkov.lab2.factory;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;

public class FactoryLogger implements Observer {
    private static final String LOG_FILE_NAME = "/home/vl/factory.log";
    private static final String DELIMITER = ": ";
    private static final String SPACE = " ";
    private static final String DEALER = "Dealer";
    private static final String AUTO = "Auto";
    private static final String BODY = "Body";
    private static final String MOTOR = "Motor";
    private static final String ACCESSORY = "Accessory";
    private File logFile;

    FactoryLogger() throws IOException {
        if(!Files.exists(Paths.get(LOG_FILE_NAME))) {
            Files.createFile(Paths.get(LOG_FILE_NAME));
        }
        logFile = Paths.get(LOG_FILE_NAME).toFile();
    }

    public void update(int newData) {

    }

    public void log(int DealerNumber, Car car) throws IOException {
        try(OutputStreamWriter stream = new OutputStreamWriter(new FileOutputStream(logFile))) {
            stream.write(LocalDateTime.now().toString());
            stream.write(DELIMITER);
            stream.write(DEALER);
            stream.write(SPACE);
            stream.write(String.valueOf(DealerNumber));
            stream.write(DELIMITER);
            stream.write(AUTO);
            stream.write(SPACE);
            stream.write(String.valueOf(car.getID()));
            stream.write(DELIMITER);
            stream.write(BODY);
            stream.write(SPACE);
            stream.write(String.valueOf(car.getBodyID()));
            stream.write(DELIMITER);
            stream.write(MOTOR);
            stream.write(SPACE);
            stream.write(String.valueOf(car.getEngineID()));
            stream.write(DELIMITER);
            stream.write(ACCESSORY);
            stream.write(SPACE);
            stream.write(String.valueOf(car.getAccessoryID()));
            stream.write('\n');
            stream.flush();
        }
    }
}
